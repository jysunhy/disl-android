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

import org.apache.commons.io.IOUtils;

import ch.usi.dag.disl.DiSL;
import ch.usi.dag.disldroidserver.OfflineInstrumentation;

public class AppBlocker {

    public static void main (String [] args) throws Exception{
        if (args.length < 1) {
            System.out.println ("args error");
            return;
        }
        String dir = "";
        try {
            String apkFilePath = args [0];
            dir = unzip (apkFilePath);
            deleteFile (dir + "/META-INF");
            String dislClass = "lib-class/SandboxBlocker.class";
            File dexFile = new File(dir + "/classes.dex");
            OfflineInstrumentation.instrumentJar (OfflineInstrumentation.readbytes (dexFile), new DiSL (false, dislClass, ""), dir + "/out-classes.dex");
            dexFile.delete ();
            File newDexFile = new File(dir + "/out-classes.dex");
            File temp = new File(dir + "/classes.dex");
            newDexFile.renameTo (temp);
            zip (dir, new File (apkFilePath).getParent () + "/repackage-" + new File (apkFilePath).getName ());
            deleteFile(dir);
            System.out.println ("repackage done");
        } catch (Exception e) {
            System.out.println (e.toString ());
            deleteFile(dir);
        }
    }

    public static void zip(String dir, String filePath) throws Exception {
        File directoryToZip = new File(dir);
        List<File> fileList = new ArrayList<File>();
        getAllFiles(directoryToZip, fileList);
        if(new File(filePath).exists ()) {
            new File(filePath).delete ();
        }
        writeZipFile(directoryToZip, fileList, filePath);
    }

    public static void getAllFiles(File dir, List<File> fileList) throws Exception{
        File[] files = dir.listFiles();
        for (File file : files) {
            fileList.add(file);
            if (file.isDirectory()) {
                getAllFiles(file, fileList);
            }
        }
    }

    public static void writeZipFile(File directoryToZip, List<File> fileList, String filePath) throws Exception {
        FileOutputStream fos = new FileOutputStream(filePath);
        ZipOutputStream zos = new ZipOutputStream(fos);
        for (File file : fileList) {
            if (!file.isDirectory()) { 
                addToZip(directoryToZip, file, zos);
            }
        }
        zos.close();
        fos.close();
    }

    public static void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
                file.getCanonicalPath().length());
        ZipEntry zipEntry = new ZipEntry(zipFilePath);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[2048];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        zos.closeEntry();
        fis.close();
    }
    
    public static String unzip (String filePath) throws Exception {
        String targetDir = filePath + "-unzip";
        if(new File(targetDir).exists ()) {
            deleteFile(targetDir);
        }
        new File(targetDir).mkdir ();
        ZipFile zipFile = new ZipFile (filePath);
        Enumeration <? extends ZipEntry> entries = zipFile.entries ();
        while (entries.hasMoreElements ()) {
            ZipEntry entry = entries.nextElement ();
            File targetFile = new File (targetDir, entry.getName ());
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
                InputStream input = zipFile.getInputStream (entry);
                OutputStream output = new FileOutputStream (targetFile);
                IOUtils.copy (input, output);
                output.close ();
                input.close ();
            }
        }
        zipFile.close ();
        return targetDir;
    }

    public static void deleteFile (String filePath) throws Exception {
        Path directory = Paths.get(filePath);
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

        });
    }

}
