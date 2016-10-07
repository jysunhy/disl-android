package ch.usi.dag.disldroidserver;

import java.io.File;



public class DexManager {
    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }

    public static void main (final String [] args) {
    }
    String instrServerIP = "127.0.0.1";
    int instrPort = 6667;

    String androidFolder = "android_folder";

    public static void instrument (
        final String name, final byte[] dex, final int length) {

    }

    static int BUFFER_SIZE = 32 * 1024;
}
