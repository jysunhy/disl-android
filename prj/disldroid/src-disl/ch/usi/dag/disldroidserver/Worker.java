package ch.usi.dag.disldroidserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import ch.usi.dag.disl.DiSL;
import ch.usi.dag.disl.exception.DiSLException;
import ch.usi.dag.disl.staticcontext.DexStaticContext;
import ch.usi.dag.disl.util.Constants;

import com.googlecode.d2j.dex.Dex2jar;
import com.googlecode.d2j.reader.BaseDexFileReader;
import com.googlecode.d2j.reader.MultiDexFileReader;
import com.googlecode.dex2jar.tools.BaksmaliBaseDexExceptionHandler;


public class Worker extends Thread {
    private static boolean cacheUsed = true;

    private static final ConcurrentHashMap <String, byte []> bytecodeMap = new ConcurrentHashMap <String, byte []> ();

    public static final ConcurrentHashMap <String, String> classNodeDexName = new ConcurrentHashMap <String, String> ();

    static boolean incremental = false;

    private static void newClass (
        final String name, final byte [] bytes, final String dexName) {
        bytecodeMap.put (
            name,
            bytes);
        final ClassNode cn = new ClassNode ();;
        final ClassReader cr = new ClassReader (bytes);
        cr.accept (cn, 0);
//        System.out.println("putting "+name);
        DexStaticContext.classNodeMap.put (name, cn);
        classNodeDexName.put (name, dexName);
    }

    private static final ConcurrentHashMap <String, byte []> cacheMap = new ConcurrentHashMap <String, byte []> ();

    private static final String PROP_UNINSTR = "dislserver.uninstrumented";

    private static final String uninstrPath =
        System.getProperty (PROP_UNINSTR, null);

    private static final String PROP_INSTR = "dislserver.instrumented";

    private static final String instrPath =
        System.getProperty (PROP_INSTR, null);

    private static final byte [] emptyByteArray = new byte [0];

    private final NetMessageReader sc;

    private final AtomicLong instrumentationTime = new AtomicLong ();


    Worker (final NetMessageReader sc) {
        this.sc = sc;
    }

    private static String getCacheHash (
        final byte [] bcode, final byte [] dislclassesHash) {
        try {
            final MessageDigest md = MessageDigest.getInstance ("MD5");
            md.update (bcode);
            if (dislclassesHash != null) {
                md.update (dislclassesHash);
            }
            return Arrays.toString (md.digest ());
        } catch (final Exception e) {
            e.printStackTrace ();
        }
        return Arrays.toString (bcode) + Arrays.toString (dislclassesHash);
    }


    static void putExtraClassesIntoJar (
        final String originalJarName, final ZipOutputStream zos,
        final String extraClassesJarName)
    throws IOException {
        final JarFile originalJar = new JarFile (originalJarName);
        final JarFile extraClassesJar = new JarFile (extraClassesJarName);

        Enumeration <JarEntry> entryEnum;
        entryEnum = originalJar.entries ();

        final byte [] buffer = new byte [1024];

        int bytesRead = 0;
        while (entryEnum.hasMoreElements ()) {
            final ZipEntry ze = entryEnum.nextElement ();
            final String entryName = ze.getName ();
            final InputStream is = originalJar.getInputStream (ze);

            final ZipEntry nze = new ZipEntry (entryName);
            zos.putNextEntry (nze);
            while ((bytesRead = is.read (buffer)) != -1) {
                zos.write (buffer, 0, bytesRead);
            }
            zos.closeEntry ();
        }

        putExtraClassesIntoJar (zos, extraClassesJar, originalJarName);

        originalJar.close ();
        extraClassesJar.close ();
    }


    static void putExtraClassesIntoJar (
        final ZipOutputStream zos, final JarFile instrlib, final String dexName)
    throws IOException {
        try {
            int bytesRead;
            final byte [] buffer = new byte [8192];
            final Enumeration <JarEntry> i_entries = instrlib.entries ();
            while (i_entries.hasMoreElements ()) {

                final ZipEntry cur = i_entries.nextElement ();
                final String curName = cur.getName ();
                if (curName.startsWith ("META-INF")) {
                    continue;
                }
                final InputStream curis = instrlib.getInputStream (cur);
                if (!cur.isDirectory ()) {
                    if (curName.endsWith (".class")) {
                        try {
                            final ZipEntry curnze = new ZipEntry (curName);
                            final String curClassName = curName.substring (
                                0, curName.lastIndexOf (".class"));
                            //System.out.println ("instrumented class " + curClassName);
                            zos.putNextEntry (curnze);
                            final ByteArrayOutputStream boutinstr = new ByteArrayOutputStream ();
                            while ((bytesRead = curis.read (buffer)) != -1) {
                                boutinstr.write (buffer, 0, bytesRead);
                            }
                            zos.write (
                                boutinstr.toByteArray (), 0, boutinstr.size ());
                            newClass (
                                curClassName, boutinstr.toByteArray (), dexName);

                            zos.closeEntry ();
                        } catch (final Exception e) {
                            // System.out.println("duplicate");
                            // e.printStackTrace ();
                        }
                    }
                } else {
                    try {
                        final ZipEntry curnze = new ZipEntry (curName);
                        zos.putNextEntry (curnze);
                        while ((bytesRead = curis.read (buffer)) != -1) {
                            zos.write (buffer, 0, bytesRead);
                        }
                        zos.closeEntry ();
                    } catch (final Exception e) {
                        // e.printStackTrace ();
                        if (AndroidInstrumenter.debug) {
                            System.out.println ("duplicate entry, ignore " + curName);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace ();
        }
    }


    public static long copy (final String srcFilename, final String
        outFilename)
    throws IOException {
        FileInputStream fis = null;
        DataInputStream dis = null;
        FileOutputStream fos = null;
        DataOutputStream out = null;
        long size = 0;
        try {
            final File srcFile = new File (srcFilename);
            final File outFile = new File (outFilename);
            fis = new FileInputStream (srcFile);
            dis = new DataInputStream (fis);
            fos = new FileOutputStream (outFile);
            out = new DataOutputStream (fos);
            int temp;
            final byte [] b = new byte [2048];
            while ((temp = dis.read (b)) != -1) {
                fos.write (b, 0, temp);
                size += temp;
            }
        } catch (final FileNotFoundException ex) {
            throw ex;
        } finally {
            if (fis != null) {
                fis.close ();
            }
            if (out != null) {
                out.close ();
            }
        }
        return size;
    }


    static int idx = 0;

    static String flattenName(final String fullName){
        return fullName.replace ('/', '_');
    }

    static synchronized byte [] instrumentJar (
        final String fullName, final byte [] dexCode)
    throws IOException,
    FileNotFoundException {
        boolean isCore = false;

        // keep a local copy of the original dex
        final FileOutputStream dexofs = new FileOutputStream (new File ("dexes/"
            + flattenName(fullName)));
        dexofs.write (dexCode, 0, dexCode.length);
        dexofs.flush ();
        dexofs.close ();

        // the instrumented dex file
        final FileOutputStream instrofs = new FileOutputStream (new File (
            "dexes/instr_" + flattenName(fullName) + ".dex"));
        instrofs.write (dexCode, 0, dexCode.length);
        instrofs.flush ();
        instrofs.close ();

        byte [] instrClass = null;
        final DiSL curdisl = UserConfiguration.getInstance ().getDiSL (fullName);

        //System.out.println ("Start instrumenting " + fullName);

        if (cacheUsed) {
            String key = "";
            if (curdisl != null) {
                key = getCacheHash (dexCode, curdisl.dislclassesHash);
            } else {
                key = getCacheHash (dexCode, null);
            }
            instrClass = cacheMap.get (key);
            if (instrClass != null) {
                System.out.println (fullName + " " + key + " hits cache");
                return instrClass;
            }
        }

        final long start = System.nanoTime ();

        // create tmp file in system temporary files
        File dex2JarFile = null;
        dex2JarFile = File.createTempFile (flattenName(fullName), ".tmp");

        // final DexFileReader reader = new DexFileReader (dexCode);
        final BaseDexFileReader reader = MultiDexFileReader.open (dexCode);
        final BaksmaliBaseDexExceptionHandler handler = false
            ? null : new BaksmaliBaseDexExceptionHandler ();

        // final DexExceptionHandlerImpl handler = new DexExceptionHandlerImpl
        // ().skipDebug (true);

        Dex2jar.from (reader).withExceptionHandler (handler).reUseReg (false).topoLogicalSort ()
            .skipDebug (true).optimizeSynchronized (false).printIR (false)
            .noCode (false).to (dex2JarFile.toPath ());

        //System.out.println (dex2JarFile.toString ());

        final JarFile dex2JarJar = new JarFile (
            dex2JarFile.getAbsolutePath ());

        Enumeration <JarEntry> entryEnum;
        // read the jar before instrumentation
        entryEnum = dex2JarJar.entries ();
        final byte [] buffer = new byte [8192];
        int bytesRead;
        while (entryEnum.hasMoreElements ()) {
            final ZipEntry ze = entryEnum.nextElement ();
            final String entryName = ze.getName ();
            final InputStream is = dex2JarJar.getInputStream (ze);
            if (!ze.isDirectory ()) {
                if (entryName.endsWith (".class")) {
                    try {
                        final String className = entryName.substring (
                            0, entryName.lastIndexOf (".class"));

                        if (className.startsWith ("java/")) {
                            isCore = true;
                        }
                        if (className.startsWith ("javax/")) {
                            isCore = true;
                        }

                        final byte [] code = null;
                        if (code == null) {
                            final ByteArrayOutputStream bout = new ByteArrayOutputStream ();
                            while ((bytesRead = is.read (buffer)) != -1) {
                                bout.write (buffer, 0, bytesRead);
                            }
                            newClass (className, bout.toByteArray (), fullName);
                        }
                    } catch (final Exception e) {
                        e.printStackTrace ();
                    }
                }

            } else {}
        }

        // Do the instrumentation
        String instrumentedJarName;;
        instrumentedJarName = "/tmp/instrumented_" + flattenName(fullName) + ".jar";
        final File instrumentedJarFile = new File (instrumentedJarName);
        final FileOutputStream fos = new FileOutputStream (instrumentedJarFile);
        final ZipOutputStream zos = new ZipOutputStream (fos);

        if (curdisl != null && curdisl.wrapperClass != null) {
            System.out.println ("found wrapper class");
            final ZipEntry wrapperEntry = new ZipEntry (curdisl.wrapperClassName
                + ".class");
            zos.putNextEntry (wrapperEntry);
            zos.write (curdisl.wrapperClass);
            zos.closeEntry ();
        }
        entryEnum = dex2JarJar.entries ();
        while (entryEnum.hasMoreElements ()) {
            final ZipEntry ze = entryEnum.nextElement ();
            final String entryName = ze.getName ();
            final InputStream is = dex2JarJar.getInputStream (ze);
            if (!ze.isDirectory ()) {
                if (entryName.endsWith (".class")) {

                    try {
                        final String className = entryName.substring (
                            0, entryName.lastIndexOf (".class"));

                        byte [] code = null;
                        final ByteArrayInputStream bin;
                        boolean isInstrumented = true;
                        if (code == null) {
                            final ByteArrayOutputStream bout = new ByteArrayOutputStream ();
                            while ((bytesRead = is.read (buffer)) != -1) {
                                bout.write (buffer, 0, bytesRead);
                            }
                            if (curdisl == null) {
                                isInstrumented = false;
                                code = bout.toByteArray ();
                            } else {
                                code = instrument (
                                    className, bout.toByteArray (), curdisl);
                            }
                            if (code == null) {
                                if (AndroidInstrumenter.debug) {
                                    System.out.println (className
                                        + " need not be instrumented");
                                }
                                code = bout.toByteArray ();
                                isInstrumented = false;
                            }

                        }
                        if (!incremental || isInstrumented) {
                            //System.out.println ("put entry for " + entryName);
                            final ZipEntry nze = new ZipEntry (entryName);
                            zos.putNextEntry (nze);
                            bin = new ByteArrayInputStream (code);
                            while ((bytesRead = bin.read (buffer)) != -1) {
                                zos.write (buffer, 0, bytesRead);
                            }
                            zos.closeEntry ();
                        } else {}
                    } catch (final Exception e) {
                        e.printStackTrace ();
                    }
                }

            } else {
                final ZipEntry nze = new ZipEntry (entryName);
                zos.putNextEntry (nze);
                while ((bytesRead = is.read (buffer)) != -1) {
                    zos.write (buffer, 0, bytesRead);
                }
                zos.closeEntry ();
            }
        }

        if (fullName.contains ("core.jar") || fullName.contains ("core-libart.jar"))
        {
            final JarFile rvlib = new JarFile ("output/lib/rv.jar");
            putExtraClassesIntoJar (zos, rvlib, fullName);
            rvlib.close ();

            final JarFile analysislib = new JarFile ("output/lib/analysis.jar");
            putExtraClassesIntoJar (zos, analysislib, fullName);
            analysislib.close ();

            final JarFile processinglib = new JarFile ("output/lib/processings.jar");
            putExtraClassesIntoJar (zos, processinglib, fullName);
            analysislib.close ();
        }
        zos.finish ();
        zos.close ();
        fos.flush ();
        fos.close ();
        dex2JarJar.close ();
        // dex2JarFile.deleteOnExit ();
        File outputDex = null;
        File realJar = null;
        outputDex = new File ("dexes/instr_" + flattenName(fullName) + ".dex");
        {
            try {
                final Class <?> c = Class.forName ("com.android.dx.command.Main");
                final java.lang.reflect.Method m = c.getMethod (
                    "main", String [].class);
                //System.out.println ("instrumentedJarName " + instrumentedJarName);
                realJar = new File (instrumentedJarName);
                // realJar.deleteOnExit ();
                // outputDex.deleteOnExit ();
                final List <String> ps = new ArrayList <String> ();
                // if (jarName.equals ("ext.jar")
                // || jarName.equals ("core.jar")) {
                if (isCore) {
                    if(incremental) {
                        ps.addAll (Arrays.asList (
                            "--dex", "--core-library",
                            // "--no-strict",
                            "--output=" + outputDex.getCanonicalPath (),
                            //"--verbose",
                            "--incremental",
                            realJar.getCanonicalPath ()));
                    }else {
                        ps.addAll (Arrays.asList (
                            "--dex", "--core-library",
                            // "--no-strict",
                            "--output=" + outputDex.getCanonicalPath (),
                            //"--verbose",
                            //"--incremental",
                            realJar.getCanonicalPath ()));
                    }
                } else {
                    if(incremental) {
                        ps.addAll (Arrays.asList (
                            "--dex",
                            // "--no-strict",
                            "--output=" + outputDex.getCanonicalPath (),
                            //"--verbose",
                            "--incremental",
                            realJar.getCanonicalPath ()));
                    }else {
                        ps.addAll (Arrays.asList (
                            "--dex",
                            // "--no-strict",
                            "--output=" + outputDex.getCanonicalPath (),
                            //"--verbose",
                            //"--incremental",
                            realJar.getCanonicalPath ()));
                    }
                }
                //System.out.println ("running dx");
                m.invoke (
                    null, new Object [] { ps.toArray (new String [0]) });
            } catch (final Throwable e) {
                System.err.println ("call dx error");
                e.printStackTrace ();
                return dexCode;
            }
        }

        // now read the instrumented dex file and pass
        // return it as byte[]
        instrClass = Utils.readbytes (outputDex);

        if (cacheUsed) {
            if (curdisl != null) {
                cacheMap.put (
                    getCacheHash (dexCode, curdisl.dislclassesHash), instrClass);
            } else {
                // use original one
                cacheMap.put (getCacheHash (dexCode, null), instrClass);
            }
        }
        System.out.println ("Instrumentation time for "
            + fullName + ":" + (System.nanoTime () - start) / 1000000.0 + "ms"
//            + " size from " + dexCode.length + " bytes " + " to "
//            + instrClass.length
            );
        // if(curdisl==null) {
        // return dexCode;
        // } else {
        return instrClass;
        // }
    }


    @Override
    public void run () {

        try {

            instrumentationLoop ();

            sc.close ();
        } catch (final Throwable e) {
            DiSLServer.reportError (e);
        } finally {
            DiSLServer.workerDone (instrumentationTime.get ());
        }
    }


    private void instrumentationLoop () throws Exception {
        try {
            while (true) {
                final NetMessage nm = sc.readMessage ();
                // communication closed by the client
                if (nm.getControl ().length == 0 && nm.getClassCode ().length == 0) {
                    return;
                }

                byte [] instrClass;

                try {

                    final long startTime = System.nanoTime ();

                    {
                        final String fullPath = new String (
                            nm.getControl ());
                        if (AndroidInstrumenter.debug) {
                            System.out.println (fullPath);
                        }

                        final byte [] dexCode = nm.getClassCode ();

                        // read java class
                        if (dexCode.length == 0) // request from disl remote&&
                                                 // !className.contains
                                                 // ("android/test/")
                                                 // server querying for loaded
                                                 // bytecode
                        {
                            instrClass = bytecodeMap.get (fullPath.replace (
                                '.', '/'));
                            if (instrClass == null) {
                                System.err.println ("The class "
                                    + fullPath.toString ()
                                    + " has not been loaded");
                            }
                        } else {
                            //System.out.println ("DEX PATH " + fullPath);
                            final String jarName = fullPath.substring (fullPath.lastIndexOf ('/') + 1);
                            instrClass = instrumentJar (fullPath, dexCode);
                        }
                    }
                    instrumentationTime.addAndGet (System.nanoTime () - startTime);
                } catch (final Exception e) {
                    String errToReport = e.getMessage ();
                    if (AndroidInstrumenter.debug) {
                        final StringWriter sw = new StringWriter ();
                        e.printStackTrace (new PrintWriter (sw));
                        errToReport = sw.toString ();
                    }

                    final String errMsg = "Instrumentation error for class "
                        + new String (nm.getControl ()) + ": " + errToReport;

                    sc.sendMessage (new NetMessage (errMsg.getBytes (),
                        emptyByteArray));

                    throw e;
                }

                NetMessage replyData = null;

                if (instrClass != null) {
                    // class was modified - send modified data
                    replyData = new NetMessage (emptyByteArray, instrClass);
                }
                else {
                    // zero length means no modification
                    replyData = new NetMessage (emptyByteArray, emptyByteArray);
                }

                sc.sendMessage (replyData);
                if (true) {
                    break;
                }
            }

        } catch (final IOException e) {
            throw new DiSLServerException (e);
        }
    }


    static byte [] instrument (
        String className, final byte [] origCode, final DiSL disl)
    throws DiSLServerException, DiSLException {
        if (AndroidInstrumenter.debug) {
            System.out.println ("instrumenting " + className);
        }

        // backup for empty class name
        if (className == null || className.isEmpty ()) {
            className = UUID.randomUUID ().toString ();
        }

        // dump uninstrumented
        if (uninstrPath != null) {
            dump (className, origCode, uninstrPath);
        }
        byte [] instrCode;
        // instrument
        try {
            instrCode = disl.instrument (origCode);
        } catch (final Exception e) {
            e.printStackTrace ();
            instrCode = origCode;
            System.out.println ("Using original code for " + className);
        }

        // dump instrumented
        if (instrPath != null && instrCode != null) {
            dump (className, instrCode, instrPath);
        }

        return instrCode;
    }


    static void dump (
        final String className, final byte [] codeAsBytes, final String path)
    throws DiSLServerException {

        try {

            // extract the class name and package name
            final int i = className.lastIndexOf (Constants.PACKAGE_INTERN_DELIM);
            final String onlyClassName = className.substring (i + 1);
            final String packageName = className.substring (0, i + 1);

            // construct path to the class
            final String pathWithPkg = path + File.separator + packageName;

            // create directories
            new File (pathWithPkg).mkdirs ();

            // dump the class code
            final FileOutputStream fo = new FileOutputStream (pathWithPkg
                + onlyClassName + Constants.CLASS_EXT);
            fo.write (codeAsBytes);
            fo.close ();
        } catch (final FileNotFoundException e) {
            throw new DiSLServerException (e);
        } catch (final IOException e) {
            throw new DiSLServerException (e);
        }
    }
}
