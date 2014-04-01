package ch.usi.dag.dislserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ch.usi.dag.disl.DiSL;
import ch.usi.dag.disl.exception.DiSLException;
import ch.usi.dag.disl.util.Constants;

import com.googlecode.dex2jar.Method;
import com.googlecode.dex2jar.reader.DexFileReader;
import com.googlecode.dex2jar.v3.Dex2jar;
import com.googlecode.dex2jar.v3.DexExceptionHandlerImpl;


public class Worker extends Thread {
    // Android Specific Code
    // the file containing the names of jars or apks that won't be instrumented
    private static final String PROP_PKG_BLACKLIST = "pkg.blacklist";

    private static final String PKG_BLACKLIST = System.getProperty (
        PROP_PKG_BLACKLIST, "pkg.blacklist");

    private String blacklist = ""; // content read from PKG_BLACKLIST

    // the file containging the names of a list of process names that will be
    // observed
    private static final String PROP_PROC_OBSERVELIST = "proc.observelist";

    private static final String PROC_OBSERVELIST = System.getProperty (
        PROP_PROC_OBSERVELIST, "proc.observelist");

    private String observeList = "";

    // specify the jar library that containing all needed analysis classes to be
    // added into core.jar(The stubs)
    private static final String PROP_INSTR_LIB_PATH = "instr.lib";

    private static final String instrLibPath = System.getProperty (
        PROP_INSTR_LIB_PATH, "lib/instr.jar");

    private static final String PROP_BUILTIN_LIB_PATH = "builtin.lib";

    private static final String builtinLibPath = System.getProperty (
        PROP_INSTR_LIB_PATH, "lib/built-in.jar");

    // the code to store the java bytecode which may be needed by the SVM server
    // TODO use DislClass+jarname as cache entry
    private static final ConcurrentHashMap <String, byte []> bytecodeMap = new ConcurrentHashMap <String, byte []> ();

    // not used, but may be needed if we want to set a flag to switch between
    // instrument the core.jar or not
    private static boolean appOnly = true;

    private static final boolean debug = Boolean
        .getBoolean (DiSLServer.PROP_DEBUG);

    private static final String PROP_UNINSTR = "dislserver.uninstrumented";

    private static final String uninstrPath =
        System.getProperty (PROP_UNINSTR, null);

    private static final String PROP_INSTR = "dislserver.instrumented";

    private static final String instrPath =
        System.getProperty (PROP_INSTR, null);

    // used for replays
    private static final byte [] emptyByteArray = new byte [0];

    private final NetMessageReader sc;

    private final DiSL disl;

    private final AtomicLong instrumentationTime = new AtomicLong ();


    Worker (final NetMessageReader sc, final DiSL disl) {
        this.sc = sc;
        this.disl = disl;
    }


    private void putExtraClassesIntoJar (
        final ZipOutputStream zos, final JarFile instrlib) throws IOException {
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
                        zos.putNextEntry (curnze);
                        final ByteArrayOutputStream boutinstr = new ByteArrayOutputStream ();
                        while ((bytesRead = curis.read (buffer)) != -1) {
                            boutinstr.write (buffer, 0, bytesRead);
                        }
                        zos.write (boutinstr.toByteArray (), 0, boutinstr.size ());

                        bytecodeMap.put (
                            curClassName.replace ('/', '.'),
                            boutinstr.toByteArray ());
                        zos.closeEntry ();
                    } catch (final Exception e) {
                        e.printStackTrace ();
                    }
                }
            } else {
                final ZipEntry curnze = new ZipEntry (curName);
                zos.putNextEntry (curnze);
                while ((bytesRead = curis.read (buffer)) != -1) {
                    zos.write (buffer, 0, bytesRead);
                }
                zos.closeEntry ();
            }
        }
    }


    private byte [] instrumentJar (final String jarName, final byte [] dexCode)
    throws IOException,
    FileNotFoundException {
        if(debug) {
            System.out.println (jarName);
        }
        byte [] instrClass = null;
        // create tmp file in /tmp
        final File dex2JarFile = File.createTempFile (
            jarName, ".tmp");
        final DexFileReader reader = new DexFileReader (dexCode);

        final DexExceptionHandlerImpl handler = new DexExceptionHandlerImpl ().skipDebug (true);

        Dex2jar.from (reader).withExceptionHandler (handler).reUseReg (
            false)
            .topoLogicalSort (false)
            .skipDebug (true)
            .optimizeSynchronized (false)
            .printIR (false)
            .verbose (false)
            .to (dex2JarFile);

        if (debug) {
            final Map <Method, Exception> exceptions = handler.getExceptions ();
            if (exceptions.size () > 0) {
                final File errorFile = new File (jarName
                    + "-error.zip");
                handler.dumpException (reader, errorFile);
                System.err.println ("Detail Error Information in File "
                    + errorFile);
            }
        }

        // Now open the tmp jar file, and instrument only
        // the .class files
        final JarFile dex2JarJar = new JarFile (
            dex2JarFile.getAbsolutePath ());

        Enumeration <JarEntry> entryEnum;
        entryEnum = dex2JarJar.entries ();

        boolean skipInstrument = false;

        // compare the jar name, and decide whether to skip instrument or not
        if (blacklist.contains (jarName)) {
            System.out.println (jarName + " is in blacklist");
            skipInstrument = true;
        }
        if (!skipInstrument) {
            System.out.println ("Instrumenting Jar: " + jarName);
        } else {
            System.out.println ("Skip instrumenting Jar: " + jarName);
        }

        final String instrumentedJarName = "instrumented_" + jarName;

        final File instrumentedJarFile = new File (instrumentedJarName);

        final FileOutputStream fos = new FileOutputStream (instrumentedJarFile);
        final ZipOutputStream zos = new ZipOutputStream (fos);
        final byte [] buffer = new byte [8192];
        int bytesRead;
        while (entryEnum.hasMoreElements ()) {
            final ZipEntry ze = entryEnum.nextElement ();
            final String entryName = ze.getName ();
            InputStream is = dex2JarJar.getInputStream (ze);
            if (!ze.isDirectory ()) {
                if (entryName.endsWith (".class")) {

                    try {
                        final ZipEntry nze = new ZipEntry (entryName);
                        // final ClassReader cr = new ClassReader(is);

                        final String className = entryName.substring (
                            0, entryName.lastIndexOf (".class"));

                        zos.putNextEntry (nze);
                        // TODO
                        // add cache here
                        byte [] code = null;
                        final ByteArrayInputStream bin;
                        if (code == null) {
                            // special case here
                            if (className.equals ("java/text/SimpleDateFormat")) {
                                final File tmp = new File (
                                    "lib/SimpleDateFormat.class");
                                is = new FileInputStream (tmp);
                            }

                            final ByteArrayOutputStream bout = new ByteArrayOutputStream ();
                            while ((bytesRead = is.read (buffer)) != -1) {
                                bout.write (buffer, 0, bytesRead);
                            }
                            // java.lang.Thread needs instrumentation for bypass
                            // support
                            if (skipInstrument
                                && !className.equals ("java/lang/Thread")) {
                                code = bout.toByteArray ();
                            } else {
                                code = instrument (
                                    className, bout.toByteArray ());
                            }
                            if (code == null) {
                                if (debug) {
                                    System.out.println (className
                                        + " cannot be instrumented");
                                }
                                code = bout.toByteArray ();
                            }
                            bytecodeMap.put (className.replace ('/', '.'), code);
                        }
                        bin = new ByteArrayInputStream (code);

                        while ((bytesRead = bin.read (buffer)) != -1) {
                            zos.write (buffer, 0, bytesRead);
                        }

                        zos.closeEntry ();
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

        // put the needed stuff into core.jar: built-in classes(bypass,
        // AREDispatch) + analysis stubs
        if (jarName.equals ("core.jar"))
        {

            final JarFile instrlib = new JarFile (
                instrLibPath);
            putExtraClassesIntoJar (zos, instrlib);
            instrlib.close ();

            final JarFile builtinlib = new JarFile (
                builtinLibPath);
            putExtraClassesIntoJar (zos, builtinlib);
            builtinlib.close ();
        }
        zos.finish ();
        zos.close ();
        fos.flush ();
        fos.close ();
        dex2JarJar.close ();

        File outputDex = null;
        File realJar = null;
        try {
            final Class <?> c = Class.forName ("com.android.dx.command.Main");
            final java.lang.reflect.Method m = c.getMethod (
                "main", String [].class);
            realJar = new File (instrumentedJarName);
            outputDex = new File (realJar.getName ()
                + ".dex");

            final List <String> ps = new ArrayList <String> ();
            if (jarName.equals ("ext.jar")
                || jarName.equals ("core.jar")) {
                ps.addAll (Arrays.asList (
                    "--dex", "--core-library", "--no-strict",
                    "--output=" + outputDex.getCanonicalPath (),
                    realJar.getCanonicalPath ()));
            } else {
                ps.addAll (Arrays.asList (
                    "--dex", "--no-strict",
                    "--output=" + outputDex.getCanonicalPath (),
                    realJar.getCanonicalPath ()));
            }
            m.invoke (
                null, new Object [] { ps.toArray (new String [0]) });
        } catch (final Exception e) {
            System.err.println ("call dx error");
            e.printStackTrace ();
        }
        // now read the instrumented dex file and pass
        // return it as byte[]
        instrClass = Files.readAllBytes (Paths.get (outputDex.getAbsolutePath ()));
        outputDex.deleteOnExit ();
        realJar.deleteOnExit ();
        dex2JarFile.deleteOnExit ();
        return instrClass;
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


    private void ReadPkgBlackList () throws Exception {
        final FileReader reader = new FileReader (PKG_BLACKLIST);
        final BufferedReader br = new BufferedReader (reader);
        String s1 = null;
        while ((s1 = br.readLine ()) != null) {
            blacklist += s1;
            blacklist += ";";
        }
        br.close ();
        reader.close ();
    }


    private void ReadObserveList () throws Exception
    {
        final FileReader reader = new FileReader (PROC_OBSERVELIST);
        final BufferedReader br = new BufferedReader (reader);
        String s1 = null;
        while ((s1 = br.readLine ()) != null) {
            observeList += s1;
            observeList += ";";
        }
        br.close ();
        reader.close ();
    }


    private void instrumentationLoop () throws Exception {

        ReadPkgBlackList ();
        ReadObserveList ();

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
                        final Path fullPath = Paths.get (new String (
                            nm.getControl ()));
                        final String jarName = fullPath.getFileName ().toString ();
                        final byte [] dexCode = nm.getClassCode ();

                        // read java class
                        if (dexCode.length == 0) // request from disl remote&&
                                                 // !className.contains
                                                 // ("android/test/")
                                                 // server querying for loaded
                                                 // bytecode
                        {
                            final boolean configMsg = fullPath.toString ().equals (
                                "-");
                            if (configMsg) {
                                instrClass = observeList.getBytes ();
                            } else {
                                instrClass = bytecodeMap.get (fullPath.toString ());
                                if (instrClass == null) {
                                    System.err.println ("The class "
                                        + fullPath.toString ()
                                        + " has not been loaded");
                                }
                            }
                        } else {
                            instrClass = instrumentJar (jarName, dexCode);
                        }

                    }
                    instrumentationTime.addAndGet (System.nanoTime () - startTime);
                } catch (final Exception e) {

                    // instrumentation error
                    // send the client a description of the server-side error

                    String errToReport = e.getMessage ();

                    // during debug send the whole message
                    if (debug) {
                        final StringWriter sw = new StringWriter ();
                        e.printStackTrace (new PrintWriter (sw));
                        errToReport = sw.toString ();
                    }

                    // error protocol:
                    // control contains the description of the server-side error
                    // class code is an array of size zero
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


    private byte [] instrument (String className, final byte [] origCode)
    throws DiSLServerException, DiSLException {

        // backup for empty class name
        if (className == null || className.isEmpty ()) {
            className = UUID.randomUUID ().toString ();
        }

        // dump uninstrumented
        if (uninstrPath != null) {
            dump (className, origCode, uninstrPath);
        }

        // instrument
        final byte [] instrCode = disl.instrument (origCode);

        // dump instrumented
        if (instrPath != null && instrCode != null) {
            dump (className, instrCode, instrPath);
        }

        return instrCode;
    }


    private void dump (
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
