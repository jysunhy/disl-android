package ch.usi.dag.disldroidserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ch.usi.dag.disl.DiSL;

import com.googlecode.d2j.dex.Dex2jar;
import com.googlecode.d2j.reader.BaseDexFileReader;
import com.googlecode.d2j.reader.MultiDexFileReader;
import com.googlecode.dex2jar.tools.BaksmaliBaseDexExceptionHandler;


public class OfflineInstrumentation {

    static boolean isCore = false;


    public static void instrumentJar (final String name, final byte [] dexCode, final DiSL curdisl, final String outDexPath)
    throws Exception
    {
        if(true){
            final FileOutputStream fos = new FileOutputStream (new File("offline/append_"+name+".dex"));
              fos.write (dexCode, 0, dexCode.length);
              fos.flush ();
              fos.close ();
        }

        // create tmp file in system temporary files
        File dex2JarFile = null;
        //dex2JarFile = File.createTempFile ("offline_dex2jar", ".jar");
        dex2JarFile = new File("offline/"+name+".jar");
        //final DexFileReader reader = new DexFileReader (dexCode);
        final BaseDexFileReader reader = MultiDexFileReader.open(dexCode);
        final BaksmaliBaseDexExceptionHandler handler = false ? null : new BaksmaliBaseDexExceptionHandler();

        //final DexExceptionHandlerImpl handler = new DexExceptionHandlerImpl ().skipDebug (true);

        Dex2jar.from(reader).withExceptionHandler(handler).reUseReg(false).topoLogicalSort()
        .skipDebug(true).optimizeSynchronized(false).printIR(false)
        .noCode(false).to(dex2JarFile.toPath ());

//        Dex2jar.from (reader).reUseReg (
//            false)
//            .topoLogicalSort (false)
//            .skipDebug (true)
//            .optimizeSynchronized (false)
//            .printIR (false)
////            .verbose (false)
//            .to (new ZipPath());

//        final Map <com.googlecode.dex2jar.Method, Exception> exceptions = handler.getExceptions ();
//        if (exceptions.size () > 0) {
//            final File errorFile = new File ("error.zip");
//            handler.dumpException (reader, errorFile);
//            System.err.println ("Detail Error Information in File "
//                + errorFile);
//        }

        // Now open the tmp jar file, and instrument only
        // the .class files

        System.out.println("DEX2JAR ENDS");

        final JarFile dex2JarJar = new JarFile (
            dex2JarFile.getAbsolutePath ());

        Enumeration <JarEntry> entryEnum;
        entryEnum = dex2JarJar.entries ();

        final File instrumentedJarFile =
        //File.createTempFile (
        //    "offline_instrumented", ".jar");;
        new File("offline/instr_"+name+".jar");
        final FileOutputStream fos = new FileOutputStream (instrumentedJarFile);
        final ZipOutputStream zos = new ZipOutputStream (fos);

        if (curdisl != null && curdisl.wrapperClass != null) {
            final ZipEntry wrapperEntry = new ZipEntry (curdisl.wrapperClassName
                + ".class");
            zos.putNextEntry (wrapperEntry);
            zos.write (curdisl.wrapperClass);
            zos.closeEntry ();
        }

        final byte [] buffer = new byte [8192];
        int bytesRead;

        while (entryEnum.hasMoreElements ()) {
            final ZipEntry ze = entryEnum.nextElement ();
            final String entryName = ze.getName ();
            final InputStream is = dex2JarJar.getInputStream (ze);
            if (!ze.isDirectory ()) {
                if (entryName.endsWith (".class")) {
                    try {

                        boolean isInstrumented = true;
                        final String className = entryName.substring (
                            0, entryName.lastIndexOf (".class"));

                        if(className.equals ("java/lang/Object")){
                            isCore = true;
                        }
                        if(className.startsWith("java/")){
                            isCore = true;
                        }
                        if(className.startsWith("javax/")){
                            isCore = true;
                        }

                        byte [] code = null;
                        final ByteArrayInputStream bin;
                        if (code == null) {
                            // special case here
                            final ByteArrayOutputStream bout = new ByteArrayOutputStream ();
                            while ((bytesRead = is.read (buffer)) != -1) {
                                bout.write (buffer, 0, bytesRead);
                            }
                            // java.lang.Thread needs instrumentation for bypass
                            // support
                            if (curdisl == null) {
                                isInstrumented = false;
                                code = bout.toByteArray ();
                            } else {
                                /*if(className.equals ("java/lang/Thread")) {
                                    final DiSL disl = new DiSL (true, "output/build/analysis/ch/usi/dag/empty/disl/DiSLClass.class", "");
                                    code = disl.instrument (bout.toByteArray ());
                                } else */
                                    code = curdisl.instrument (bout.toByteArray ());

                            }
                            if (code == null) {
                                isInstrumented = false;
                                code = bout.toByteArray ();
                            } else {
                                isInstrumented = true;
                                System.out.println (entryName + " is instrumented");
                            }
                        }
                        if(isInstrumented || true) {
                            final ZipEntry nze = new ZipEntry (entryName);
                            zos.putNextEntry (nze);
                            bin = new ByteArrayInputStream (code);
                            while ((bytesRead = bin.read (buffer)) != -1) {
                                zos.write (buffer, 0, bytesRead);
                            }
                            zos.closeEntry ();
                        }

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
        zos.finish ();
        zos.close ();
        fos.flush ();
        fos.close ();
        dex2JarJar.close ();

        //System.out.println("now run dx");
//        if(true) {
//            return;
//        }
        final File outputDex = new File("offline/append_"+name+".dex");
        try {
            final Class <?> c = Class.forName ("com.android.dx.command.Main");
            final java.lang.reflect.Method m = c.getMethod (
                "main", String [].class);
//            outputDex = new File ("");

            final List <String> ps = new ArrayList <String> ();
            if (isCore) {
                ps.addAll (Arrays.asList (
                    "--dex", "--core-library", "--no-strict",
                    "--output=" + outputDex.getCanonicalPath (),
//                    "--verbose",
                    //"--incremental",
                    instrumentedJarFile.getCanonicalPath ()));
            } else {
                ps.addAll (Arrays.asList (
                    "--dex", "--no-strict",
                    "--output=" + outputDex.getCanonicalPath (),
//                    "--verbose",
                    //"--incremental",
                    instrumentedJarFile.getCanonicalPath ()));
            }
            m.invoke (
                null, new Object [] { ps.toArray (new String [0]) });
        } catch (final Exception e) {
            System.err.println ("call dx error");
            e.printStackTrace ();
        }
        //instrumentedJarFile.deleteOnExit ();
        //dex2JarFile.deleteOnExit ();
    }


    public static byte [] readbytes (final File file) {
        final byte [] res = null;
        FileInputStream fis = null;
        DataInputStream dis = null;
        ByteArrayOutputStream bout = null;
        try {
            fis = new FileInputStream (file);
            dis = new DataInputStream (fis);
            bout = new ByteArrayOutputStream ();
            int temp;
            int size = 0;
            final byte [] b = new byte [2048];
            while ((temp = dis.read (b)) != -1) {
                bout.write (b, 0, temp);
                size += temp;
            }
            fis.close ();
            dis.close ();
        } catch (final Exception e) {
            e.printStackTrace ();
        }

        return bout.toByteArray ();
    }


    public static void main (final String [] args) {
        if (args.length < 2) {
            System.out.println ("Usage: PATH_TO_INPUT_DEX PATH_TO_DISLCLASSES_SEPARTED_WITH_COMMA [exclfile]");
        } else {
            final File inputDex = new File (args [0]);
            final String name = args[0].replace ('/', '_');
            try {
                if(args.length > 2) {
                    instrumentJar (name, readbytes (inputDex), new DiSL (false, args[1], args[2]), "output.dex");
                }else {
                    instrumentJar (name, readbytes (inputDex), new DiSL (false, args[1], null), "output.dex");
                }
            } catch (final Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            }
        }
    }

}
