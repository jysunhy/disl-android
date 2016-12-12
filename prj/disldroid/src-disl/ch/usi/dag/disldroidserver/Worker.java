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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import ch.usi.dag.disl.util.Constants;
import ch.usi.dag.disldroidserver.DiSLConfig.Dex;

import com.googlecode.dex2jar.reader.DexFileReader;
import com.googlecode.dex2jar.v3.Dex2jar;
import com.googlecode.dex2jar.v3.DexExceptionHandlerImpl;


public class Worker extends Thread {
    // Android Specific Code
    // the file containing the names of jars or apks that won't be instrumented
    private static final boolean ON_ANDROID_DEVICE = Boolean.getBoolean ("on.android");

    private static final String PROP_PKG_BLACKLIST = "pkg.blacklist";

    private static final String PKG_BLACKLIST = System.getProperty (
        PROP_PKG_BLACKLIST, "pkg.blacklist");

    //private String blacklist = ""; // content read from PKG_BLACKLIST

    private static boolean cacheUsed = true;

    // the file containging the names of a list of process names that will be
    // observed
    private static final String PROP_PROC_OBSERVELIST = "proc.observelist";

    private static final String PROC_OBSERVELIST = System.getProperty (
        PROP_PROC_OBSERVELIST, "proc.observelist");

    //private String observeList = "";

    // specify the jar library that containing all needed analysis classes to be
    // added into core.jar(The stubs)
    private static final String PROP_INSTR_LIB_PATH = "instr.lib";

    private static final String instrLibPath = System.getProperty (
        PROP_INSTR_LIB_PATH, "output/lib/analysis.jar");

    private static final String[] extraLibs = {"lib/rv-monitor-rt.jar","lib/rv-monitor.jar"};


    private static final String PROP_BUILTIN_LIB_PATH = "builtin.lib";

    //private static final String builtinLibPath = "lib/built-in-emma.jar";
    //private static final String builtinLibPath = System.getProperty (PROP_BUILTIN_LIB_PATH, "output/lib/builtin.jar");
    private static final String builtinLibPath = System.getProperty (PROP_BUILTIN_LIB_PATH, "");

    // the code to store the java bytecode which may be needed by the SVM server
    // TODO use DislClass+jarname as cache entry


    private static final ConcurrentHashMap <String, byte []> bytecodeMap = new ConcurrentHashMap <String, byte []> ();
    public static final ConcurrentHashMap <String, ClassNode> classNodeMap = new ConcurrentHashMap <String, ClassNode> ();
    public static final ConcurrentHashMap <String, String> classNodeDexName = new ConcurrentHashMap <String, String> ();

    private static void newClass(final String name, final byte[] bytes, final String dexName){
        //System.out.println ("new class "+name);
        bytecodeMap.put (
            name,
            bytes);
        final ClassNode cn = new ClassNode ();;
        final ClassReader cr = new ClassReader (bytes);
        cr.accept (cn, 0);
        classNodeMap.put (name, cn);
        classNodeDexName.put (name, dexName);
    }

    public static boolean isSelfOrChildOf(final String class1, final String class2){
        if(class1.equals (class2)){
            return true;
        }
        final ClassNode cn1 = classNodeMap.get (class1);
        final ClassNode cn2 = classNodeMap.get (class2);

        ClassNode cur = cn1;
        while(cur != null){
            if(cur.superName == null) {
                return false;
            }
            if(cur.superName == class2){
                return true;
            }
            cur = classNodeMap.get (cur.superName);
        }

        return false;
    }

    public static boolean isInterfaceOf(final String class1, final String class2){
        if(class1.equals (class2)){
            return true;
        }
        final ClassNode cn1 = classNodeMap.get (class1);
        final ClassNode cn2 = classNodeMap.get (class2);

        for(final String it1 : cn1.interfaces){
            if(it1.equals (class2)) {
                return true;
            }
        }
        return false;
    }

    private static final ConcurrentHashMap <String, byte []> cacheMap = new ConcurrentHashMap <String, byte []> ();

    // not used, but may be needed if we want to set a flag to switch between
    // instrument the core.jar or not
    private static boolean appOnly = true;

    private static final String PROP_UNINSTR = "dislserver.uninstrumented";

    private static final String uninstrPath =
        System.getProperty (PROP_UNINSTR, null);

    private static final String PROP_INSTR = "dislserver.instrumented";

    private static final String instrPath =
        System.getProperty (PROP_INSTR, null);

    // used for replays
    private static final byte [] emptyByteArray = new byte [0];

    private final NetMessageReader sc;

    //private DiSL disl = null;
//    public static HashMap<String, DiSL> dislMap = new HashMap <String, DiSL>();

    private final AtomicLong instrumentationTime = new AtomicLong ();




    Worker (final NetMessageReader sc, final DiSL dislArg) {
        this.sc = sc;
        //this.disl = dislArg;
        //this.disl = disl;
    }

//    Worker (final NetMessageReader sc) {
//        this.sc = sc;
//        //this.disl = null;
//    }


    // hash for speed up of duplicate instrumentation
    // current DiSL class cannot be changed during a single run
    // in future, if it can be changed during run, we should add also the
    // DiSLClass bytecode as cacheHashKey
    private static String getCacheHash (final byte [] bcode, final byte [] dislclassesHash) {
        try {
            final MessageDigest md = MessageDigest.getInstance ("MD5");
            md.update (bcode);
            if(dislclassesHash != null) {
                md.update (dislclassesHash);
            }
            return Arrays.toString (md.digest ());
        } catch (final Exception e) {
            e.printStackTrace ();
        }
        return Arrays.toString (bcode) + Arrays.toString (dislclassesHash);
    }


    static void putExtraClassesIntoJar (
        final String originalJarName, final ZipOutputStream zos ,
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
//            if (entryName.equals ("java/lang/Thread.class")) {// Java lang
//                                                              // Thread should
//                                                              // be replaced for
//                                                              // the bypass
//                                                              // field
//                final ByteArrayOutputStream bout = new ByteArrayOutputStream ();
//                final InputStream is = originalJar.getInputStream (ze);
//
//                final ZipEntry nze = new ZipEntry (entryName);
//                zos.putNextEntry (nze);
//                while ((bytesRead = is.read (buffer)) != -1) {
//                    bout.write (buffer, 0, bytesRead);
//                }
//                try {
//                    zos.write (instrument (
//                        "java/lang/Thread", bout.toByteArray (), getDiSL ("core.jar")==null?getDiSL ("core.jar"):disl));
//                } catch (final Exception e) {
//                    e.printStackTrace ();
//                }
//
//                zos.closeEntry ();
//            } else
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
        final ZipOutputStream zos, final JarFile instrlib, final String dexName) throws IOException {
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
                            zos.putNextEntry (curnze);
                            final ByteArrayOutputStream boutinstr = new ByteArrayOutputStream ();
                            while ((bytesRead = curis.read (buffer)) != -1) {
                                boutinstr.write (buffer, 0, bytesRead);
                            }
                            zos.write (
                                boutinstr.toByteArray (), 0, boutinstr.size ());
                            newClass (curClassName, boutinstr.toByteArray (), dexName);

                            zos.closeEntry ();
                        } catch (final Exception e) {
                            //System.out.println("duplicate");
                            //e.printStackTrace ();
                        }
                    }
                } else {
                    try{
                    final ZipEntry curnze = new ZipEntry (curName);
                    zos.putNextEntry (curnze);
                    while ((bytesRead = curis.read (buffer)) != -1) {
                        zos.write (buffer, 0, bytesRead);
                    }
                    zos.closeEntry ();
                    }catch(final Exception e){
                        //e.printStackTrace ();
                        if(AndroidInstrumenter.debug) {
                            System.out.println("duplicate entry, ignore");
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
                fos.write (b,0,temp);
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


    static byte [] preInstrumentJar (final String jarName, final String originalJarName, final byte [] dexCode) {
        File outputDex = null;
        File realJar = null;
        byte [] res = null;
        try {

            final String instrumentedJarName = "preinstrumented_" + jarName;
            if (jarName.equals ("core.jar")) {
                final FileOutputStream fos = new FileOutputStream (instrumentedJarName);
                final ZipOutputStream zos = new ZipOutputStream (fos);

                if(!builtinLibPath.equals ("")) {
                    putExtraClassesIntoJar (
                        originalJarName, zos, builtinLibPath);
                }

                zos.finish ();
                zos.close ();
                fos.flush ();
                fos.close ();

            } else {
                copy (originalJarName, instrumentedJarName);
            }

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
            //res = Files.readAllBytes (Paths.get (outputDex.getAbsolutePath ()));
            res = Utils.readbytes (outputDex);
        } catch (final Exception e) {
            System.err.println ("call dx error");
            e.printStackTrace ();
        }
        // now read the instrumented dex file and pass
        // return it as byte[]
        if (!AndroidInstrumenter.debug) {
            outputDex.deleteOnExit ();
        }
        if (res == null) {
            return res;
        }
        return res;

    }

    static DiSL getDiSL(final String jarName){
        DiSL newdisl = null;
        final Dex dex = DiSLConfig.dexMap.get(jarName);
        if(AndroidInstrumenter.dislMap.get (jarName)==null){
            if(dex == null){
                if(DiSLConfig.default_disl_classes.equals ("")) {
                    return null;
                }
                try{
                    newdisl = new DiSL (DiSLConfig.default_bypass, DiSLConfig.default_disl_classes,"");
                }catch (final Exception e){
                    e.printStackTrace ();
                }

            }else{
                if(dex.dislClass.equals ("")) {
                    return null;
                }
                try {
                    newdisl = new DiSL(dex.bypass, dex.dislClass,"");
                    //newdisl = new DiSL(true, dex.dislClass,"");
                } catch (final DiSLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        if(newdisl != null) {
            AndroidInstrumenter.dislMap.put (jarName, newdisl);
        }
        return newdisl;
    }

    static synchronized byte [] instrumentJar (final String jarName, final byte [] dexCode)
    throws IOException,
    FileNotFoundException {
        if (AndroidInstrumenter.debug) {
            System.out.println (jarName);
        }
        System.out.println("Start instrumenting "+jarName);

        if(jarName.startsWith ("prog-")){
            System.out.println("Skipping facebook packages first");
            final FileOutputStream fos = new FileOutputStream (new File("dexes/"+jarName));
              fos.write (dexCode, 0, dexCode.length);
              fos.flush ();
              fos.close ();
            return dexCode;
        }

//        if(true){
//            final FileOutputStream fos = new FileOutputStream (new File("dexes/"+jarName));
//            fos.write (dexCode, 0, dexCode.length);
//            fos.flush ();
//            fos.close ();
//            return dexCode;
//        }
        byte [] instrClass = null;
        final DiSL curdisl = getDiSL(jarName);
        if (cacheUsed) {
            String key ="";
            if(curdisl != null) {
                key = getCacheHash (dexCode, curdisl.dislclassesHash);
            } else {
                key = getCacheHash (dexCode, null);
            }
            instrClass = cacheMap.get (key);
            if (instrClass != null) {
                System.out.println (jarName + " " + key + " hits cache");
                return instrClass;
            }
        }

        final long start = System.nanoTime ();

        // create tmp file in system temporary files
        File dex2JarFile = null;
        if(ON_ANDROID_DEVICE) {
            dex2JarFile = new File("/data/"+jarName+".tmp");
        } else {
            dex2JarFile = File.createTempFile (
            jarName, ".tmp");
        }

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

        System.out.println(dex2JarFile.toString ());

        if (AndroidInstrumenter.debug) {
            final Map <com.googlecode.dex2jar.Method, Exception> exceptions = handler.getExceptions ();
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
        //read the jar before instrumentation
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

                        final byte [] code = null;
                        if (code == null) {
                            final ByteArrayOutputStream bout = new ByteArrayOutputStream ();
                            while ((bytesRead = is.read (buffer)) != -1) {
                                bout.write (buffer, 0, bytesRead);
                            }
                            newClass (className, bout.toByteArray (), jarName);
                        }
                    } catch (final Exception e) {
                        e.printStackTrace ();
                    }
                }

            } else {
            }
        }

        //Do the instrumentation
        String instrumentedJarName;;
        if(ON_ANDROID_DEVICE) {
            instrumentedJarName= "/data/"+ "instrumented_" + jarName;
        } else {
            instrumentedJarName= "/tmp/instrumented_" + jarName;
        }
        final File instrumentedJarFile= new File (instrumentedJarName);
        final FileOutputStream fos = new FileOutputStream (instrumentedJarFile);
        final ZipOutputStream zos = new ZipOutputStream (fos);

        if(curdisl!= null && curdisl.wrapperClass != null){
            System.out.println("found wrapper class");
            final ZipEntry wrapperEntry = new ZipEntry (curdisl.wrapperClassName+".class");
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
                        final ZipEntry nze = new ZipEntry (entryName);

                        final String className = entryName.substring (
                            0, entryName.lastIndexOf (".class"));

                        zos.putNextEntry (nze);
                        byte [] code = null;
                        final ByteArrayInputStream bin;
                        if (code == null) {
                            final ByteArrayOutputStream bout = new ByteArrayOutputStream ();
                            while ((bytesRead = is.read (buffer)) != -1) {
                                bout.write (buffer, 0, bytesRead);
                            }
                            //newClass (className, bout.toByteArray ());
                            // java.lang.Thread needs instrumentation for bypass
                            // support
//                            if (className.equals ("java/lang/Thread")) {
//                                code = instrument (
//                                    className, bout.toByteArray (), disl);
//                            } else
                            if(curdisl == null){
                                code = bout.toByteArray ();
                            }else{
                                code = instrument (
                                    className, bout.toByteArray (), curdisl);
                            }
                            if (code == null) {
                                if (AndroidInstrumenter.debug) {
                                    System.out.println (className
                                        + " need not be instrumented");
                                }
                                code = bout.toByteArray ();
                            }


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
            putExtraClassesIntoJar (zos, instrlib, jarName);
            instrlib.close ();

            if(!builtinLibPath.equals ("")) {
                final JarFile builtinlib = new JarFile (
                    builtinLibPath);

                putExtraClassesIntoJar (zos, builtinlib, jarName);
                builtinlib.close ();
            }
            for(final String name: extraLibs){
                final JarFile extraLib = new JarFile (name);
                putExtraClassesIntoJar (zos, extraLib, name);
                extraLib.close ();
            }
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
            outputDex = new File (instrumentedJarName
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
            System.out.println ("running dx");
            m.invoke (
                null, new Object [] { ps.toArray (new String [0]) });
        } catch (final Exception e) {
            System.err.println ("call dx error");
            e.printStackTrace ();
        }
        // now read the instrumented dex file and pass
        // return it as byte[]
        instrClass = Utils.readbytes (outputDex);
        //outputDex.deleteOnExit ();
        //realJar.deleteOnExit ();
        //dex2JarFile.deleteOnExit ();

        if (cacheUsed) {
            if(curdisl != null) {
                cacheMap.put (getCacheHash (dexCode, curdisl.dislclassesHash), instrClass);
            } else {
                //use original one
                cacheMap.put (getCacheHash (dexCode, null), instrClass);
            }
        }
        System.out.println("Instrumentation time for "+jarName+":"+(System.nanoTime ()-start)/1000000.0 + "ms" + " size from "+dexCode.length+" bytes "+" to "+instrClass.length);
        //if(curdisl==null) {
        //    return dexCode;
        //} else {
            return instrClass;
        //}
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


//    private void ReadPkgBlackList () throws Exception {
//        final FileReader reader = new FileReader (PKG_BLACKLIST);
//        final BufferedReader br = new BufferedReader (reader);
//        String s1 = null;
//        while ((s1 = br.readLine ()) != null) {
//            blacklist += s1;
//            blacklist += ";";
//        }
//        br.close ();
//        reader.close ();
//    }


//    private void ReadObserveList () throws Exception
//    {
//        final FileReader reader = new FileReader (PROC_OBSERVELIST);
//        final BufferedReader br = new BufferedReader (reader);
//        String s1 = null;
//        while ((s1 = br.readLine ()) != null) {
//            observeList += s1;
//            observeList += ";";
//        }
//        br.close ();
//        reader.close ();
//    }

    private void instrumentationLoop () throws Exception {

        if(!DiSLConfig.parseXml ()){
			if(AndroidInstrumenter.debug) {
                System.out.println("Update DiSL classes");
            }
			AndroidInstrumenter.dislMap = new HashMap <String, DiSL>();
		}
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
                        final String fullPath =new String (
                            nm.getControl ());
						if(AndroidInstrumenter.debug) {
                            System.out.println(fullPath);
                        }

                        final byte [] dexCode = nm.getClassCode ();

                        // read java class
                        if (dexCode.length == 0) // request from disl remote&&
                                                 // !className.contains
                                                 // ("android/test/")
                                                 // server querying for loaded
                                                 // bytecode
                        {
                            final boolean configMsg = fullPath.equals (
                                "-");
                            if (configMsg) {

                                String observeList=""+(DiSLConfig.default_proc_observed?"1;":"0;");
                                final Collection<DiSLConfig.Proc> list =DiSLConfig.procMap.values ();
                                final Iterator<DiSLConfig.Proc> iter = list.iterator ();
                                while(iter.hasNext ()){
									final DiSLConfig.Proc tmp = iter.next();
									if(tmp.isObserved != DiSLConfig.default_proc_observed) {
                                        observeList += tmp.procname+";";
                                    }
                                }
                                instrClass = observeList.getBytes ();
                            } else {
                                instrClass = bytecodeMap.get (fullPath.replace ('.', '/'));
                                if (instrClass == null) {
                                    System.err.println ("The class "
                                        + fullPath.toString ()
                                        + " has not been loaded");
                                }
                            }
                        } else {
                            final String jarName = fullPath.substring(fullPath.lastIndexOf ('/')+1);
                            if(DiSLConfig.dexMap.get (jarName) == null || DiSLConfig.dexMap.get (jarName).preinstrumented_path.equals ("")) {
                                instrClass = instrumentJar (jarName, dexCode);
                                //javamop.Guard.printCounters ();
                            }
                            else {
                                instrClass = preInstrumentJar (jarName, DiSLConfig.dexMap.get (jarName).preinstrumented_path, dexCode);
                             //instrClass = dexCode;
                            }
                        }

                    }
                    instrumentationTime.addAndGet (System.nanoTime () - startTime);
                } catch (final Exception e) {

                    // instrumentation error
                    // send the client a description of the server-side error

                    String errToReport = e.getMessage ();

                    // during debug send the whole message
                    if (AndroidInstrumenter.debug) {
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


    static byte [] instrument (String className, final byte [] origCode, final DiSL disl)
    throws DiSLServerException, DiSLException {
		if(AndroidInstrumenter.debug) {
            System.out.println("instrumenting "+className);
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
        }catch (final Exception e) {
            e.printStackTrace();
            instrCode = origCode;
            System.out.println("Using original code for "+className);
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
