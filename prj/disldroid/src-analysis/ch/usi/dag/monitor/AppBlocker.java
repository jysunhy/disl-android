package ch.usi.dag.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import ch.usi.dag.disl.DiSL;
import ch.usi.dag.disldroidserver.OfflineInstrumentation;
//import org.apache.commons.io.IOUtils;

public class AppBlocker {

    public static void main (final String [] args) throws Exception{
        if (args.length < 1) {
            System.out.println ("args error");
            return;
        }
        String dir = "";
        try {
            final String apkFilePath = args [0];
            dir = unzip (apkFilePath);
            deleteFile (dir + "/META-INF");
            final String dislClass = "lib-class/SandboxBlocker.class";
            final File dexFile = new File(dir + "/classes.dex");
            OfflineInstrumentation.instrumentJar ("a",OfflineInstrumentation.readbytes (dexFile), new DiSL (false, dislClass, ""), dir + "/out-classes.dex");
            dexFile.delete ();
            final File newDexFile = new File(dir + "/out-classes.dex");
            final File temp = new File(dir + "/classes.dex");
            newDexFile.renameTo (temp);
            zip (dir, new File (apkFilePath).getParent () + "/repackage-" + new File (apkFilePath).getName ());
            deleteFile(dir);
            System.out.println ("repackage done");
        } catch (final Exception e) {
            System.out.println (e.toString ());
            deleteFile(dir);
        }
    }

    public static void zip(final String dir, final String filePath) throws Exception {
        final File directoryToZip = new File(dir);
        final List<File> fileList = new ArrayList<File>();
        getAllFiles(directoryToZip, fileList);
        if(new File(filePath).exists ()) {
            new File(filePath).delete ();
        }
        writeZipFile(directoryToZip, fileList, filePath);
    }

    public static void getAllFiles(final File dir, final List<File> fileList) throws Exception{
        final File[] files = dir.listFiles();
        for (final File file : files) {
            fileList.add(file);
            if (file.isDirectory()) {
                getAllFiles(file, fileList);
            }
        }
    }

    public static void writeZipFile(final File directoryToZip, final List<File> fileList, final String filePath) throws Exception {
        final FileOutputStream fos = new FileOutputStream(filePath);
        final ZipOutputStream zos = new ZipOutputStream(fos);
        for (final File file : fileList) {
            if (!file.isDirectory()) {
                addToZip(directoryToZip, file, zos);
            }
        }
        zos.close();
        fos.close();
    }

    public static void addToZip(final File directoryToZip, final File file, final ZipOutputStream zos) throws Exception {
        final FileInputStream fis = new FileInputStream(file);
        final String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
                file.getCanonicalPath().length());
        final ZipEntry zipEntry = new ZipEntry(zipFilePath);
        zos.putNextEntry(zipEntry);

        final byte[] bytes = new byte[2048];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        zos.closeEntry();
        fis.close();
    }

    public static String unzip (final String filePath) throws Exception {
        final String targetDir = filePath + "-unzip";
        if(new File(targetDir).exists ()) {
            deleteFile(targetDir);
        }
        new File(targetDir).mkdir ();
        final ZipFile zipFile = new ZipFile (filePath);
        final Enumeration <? extends ZipEntry> entries = zipFile.entries ();
        while (entries.hasMoreElements ()) {
            final ZipEntry entry = entries.nextElement ();
            final File targetFile = new File (targetDir, entry.getName ());
            if (entry.isDirectory ()) {
                targetFile.mkdirs ();
            }
            else {
                if (!targetFile.getParentFile ().exists ()) {
                    targetFile.getParentFile ().mkdirs ();
                }
                if (!targetFile.exists ()) {
                    targetFile.createNewFile ();
                }
                final InputStream input = zipFile.getInputStream (entry);
                final OutputStream output = new FileOutputStream (targetFile);
                //IOUtils.copy (input, output);
                final byte[] buf = new byte[1024];
                int rd = -1;
                while((rd = input.read (buf)) > 0){
                    output.write (buf,0,rd);
                }
                output.close ();
                input.close ();
            }
        }
        zipFile.close ();
        return targetDir;
    }

    public static void deleteFile (final String filePath) throws Exception {
        final Path directory = Paths.get(filePath);
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

        });
    }

}
