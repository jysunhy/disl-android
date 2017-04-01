package ch.usi.dag.disldroidserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ch.usi.dag.disl.DiSL;


public class JavaTest {
    public static void main (final String args []) {
        // 1 jar file
        // 2 DISL
        // 3 outputfolder
        try {
            JarFile inputJar;
            inputJar = new JarFile (
                args [0]);

            Enumeration <JarEntry> inputEntry;
            inputEntry = inputJar.entries ();

            DiSL curdisl = null;
            curdisl = new DiSL (false, args [1], null);

            final String outputFile = (args.length > 2 ? args [2] : ".")
                + "/" + "test.instr.jar";
            FileOutputStream fos;
            fos = new FileOutputStream (new File (outputFile));
            final ZipOutputStream zos = new ZipOutputStream (fos);
            boolean isCore = false;

            final byte [] buffer = new byte [8192];
            int bytesRead;

            while (inputEntry.hasMoreElements ()) {
                final ZipEntry ze = inputEntry.nextElement ();
                final String entryName = ze.getName ();
                final InputStream is = inputJar.getInputStream (ze);
                if (!ze.isDirectory ()) {
                    if (entryName.endsWith (".class")) {
                        try {

                            boolean isInstrumented = true;
                            final String className = entryName.substring (
                                0, entryName.lastIndexOf (".class"));

                            if (className.equals ("java/lang/Object")) {
                                isCore = true;
                            }
                            if (className.startsWith ("java/")) {
                                isCore = true;
                            }
                            if (className.startsWith ("javax/")) {
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
                                // java.lang.Thread needs instrumentation for
                                // bypass
                                // support
                                if (curdisl == null) {
                                    isInstrumented = false;
                                    code = bout.toByteArray ();
                                } else {
                                    /*
                                     * if(className.equals ("java/lang/Thread"))
                                     * { final DiSL disl = new DiSL (true,
                                     * "output/build/analysis/ch/usi/dag/empty/disl/DiSLClass.class"
                                     * , ""); code = disl.instrument
                                     * (bout.toByteArray ()); } else
                                     */
                                    code = curdisl.instrument (bout.toByteArray ());

                                }
                                if (code == null) {
                                    isInstrumented = false;
                                    code = bout.toByteArray ();
                                } else {
                                    isInstrumented = true;
                                    System.out.println (entryName
                                        + " is instrumented");
                                }
                            }
                            if (isInstrumented || true) {
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
            inputJar.close ();
        } catch (final Exception e) {

        }
    }
}
