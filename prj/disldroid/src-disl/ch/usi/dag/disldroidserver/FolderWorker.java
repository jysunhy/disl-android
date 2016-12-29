package ch.usi.dag.disldroidserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class FolderWorker extends Thread {
    public static boolean isFromFolder = false;

    public FolderWorker () {
    }

    String androidFolder = "android_folder";
    String androidFolder2 = "android_folder2";


    static class DexStruct {
        String path;
        //int idx;
        int size;
        int instrumentedSize;
        public DexStruct (final String path, final int size) {
            super ();
            //System.out.println(path+" "+size+" "+idx);
            this.path = path;
            //this.idx = idx;
            this.size = size;
            instrumentedSize = 0;
        }
    }

    HashMap<String, DexStruct> dexes = new HashMap <String, FolderWorker.DexStruct> ();

    long getFileSize(final File file){
        FileInputStream fis;
        long res = 0;
        try {
            fis = new FileInputStream (file);
            res = fis.getChannel ().size ();
            fis.close ();
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void run () {
        final File folder0 = new File (androidFolder);
        for(final File f : folder0.listFiles ()){
            f.delete ();
        }
        final File folder1 = new File (androidFolder2);
        for(final File f : folder1.listFiles ()){
            f.delete ();
        }

        while (true) {
            AndroidInstrumenter.checkConfigXMLChange();
            try {
                //System.out.println("pulling");
                //final Process p = Runtime.getRuntime().exec(new String[]{"zsh","-c","adb pull /data/data/ /Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder"});
                //p.waitFor ();
                final List<String> commands = new ArrayList<String>();
                commands.add("/Users/haiyang/Library/Android/sdk/platform-tools/adb");
                commands.add("pull");
                commands.add("/data/data/disl/table");
                commands.add("/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder");
                final ProcessBuilder pb = new ProcessBuilder (commands);
                pb.start ();
            } catch (final Exception e1) {
                e1.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            final File folder = new File (androidFolder);
            //final File folder2 = new File (androidFolder2);
            for (final File fileEntry : folder.listFiles ()) {
                if (!fileEntry.isDirectory ()) {
                    final String fname = fileEntry.getName ();
                    final String [] arr = fname.split ("_");
                    switch(arr[0]){
                        case "dextable":
                            Scanner scanner;
                            try {
                                scanner = new Scanner (fileEntry);
                                final String line = scanner.nextLine ();
                                final String [] tmp = line.split (" ");
                                if(tmp.length != 3) {
                                    continue;
                                }
                                final String name = tmp[0];
                                final int size = Integer.parseInt (tmp[1]);
                                final int isize = Integer.parseInt (tmp[2]);
                                if(!dexes.containsKey (fname)){
                                    dexes.put (fname, new DexStruct (name,size));
                                }
                                if(isize == 0){
                                    if(AndroidInstrumenter.debug) {
                                        System.out.println("Notice uninstrumented "+name);
                                    }
                                    try {
                                        final List<String> commands = new ArrayList<String>();
                                        commands.add("/Users/haiyang/Library/Android/sdk/platform-tools/adb");
                                        commands.add("pull");
                                        commands.add("/data/data/disl/dex/"+fname.replace ("dextable", "dex"));
                                        commands.add("/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder");
                                        final ProcessBuilder pb = new ProcessBuilder (commands);
                                        pb.start ();
                                    } catch (final Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    try {
                                        Thread.sleep(2000);
                                    } catch (final InterruptedException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                    byte[] dexCode;
                                    do{
                                        dexCode = Utils.readbytes (new File(androidFolder+"/"+fname.replace ("dextable", "dex")));
                                    }while(dexCode.length != size);
                                    byte[] instrClass;
                                    final String jarName = name.substring(name.lastIndexOf ('/')+1);
                                    if(DiSLConfig.dexMap.get (jarName) == null || DiSLConfig.dexMap.get (jarName).preinstrumented_path.equals ("")) {
                                        instrClass = Worker.instrumentJar (jarName, dexCode);
                                        //javamop.Guard.printCounters ();
                                    }
                                    else {
                                        instrClass = Worker.preInstrumentJar (jarName, DiSLConfig.dexMap.get (jarName).preinstrumented_path, dexCode);
                                    }
                                    if(instrClass == null){
                                        instrClass = dexCode;
                                        System.out.println("instrumentation failure "+name);
                                    }
                                    final File instrF = new File(androidFolder2+"/"+fname.replace ("dextable", "instrdex"));
                                    instrF.createNewFile ();
                                    final FileOutputStream fos = new FileOutputStream (instrF);
                                    fos.write (instrClass, 0, instrClass.length);
                                    fos.flush ();
                                    fos.close ();
                                    final FileWriter fw = new FileWriter (new File(androidFolder2+"/"+fname));
                                    fw.write (name+" "+size+" "+instrClass.length);
                                    fw.flush ();
                                    fw.close ();
                                    //new File(androidFolder+"/"+fname).delete ();
                                    final FileWriter fw0 = new FileWriter (new File(androidFolder+"/"+fname));
                                    fw0.write (name+" "+size+" "+instrClass.length);
                                    fw0.flush ();
                                    fw0.close ();
                                    dexes.get (fname).instrumentedSize = instrClass.length;

                                    try {
                                        //System.out.println("pushing and cleaning");
                                        //final Process p = Runtime.getRuntime().exec(new String[]{"zsh","-c","adb push /Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder2/ /data/app/ "});
                                        final List<String> commands = new ArrayList<String>();
                                        commands.add("/Users/haiyang/Library/Android/sdk/platform-tools/adb");
                                        commands.add("push");
                                        commands.add("/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder2/"+fname.replace ("dextable", "instrdex"));
                                        commands.add("/data/app/");
                                        final ProcessBuilder pb = new ProcessBuilder (commands);
                                        pb.start ();
//                                        final List<String> commands2 = new ArrayList<String>();
//                                        commands2.add("rm");
//                                        commands2.add("/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder2/*");
//                                        final ProcessBuilder pb2 = new ProcessBuilder (commands);
//                                        pb2.start ();
                                        //final Process p2 = Runtime.getRuntime().exec(new String[]{"zsh","-c","rm /Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder2/*"});
                                        //p2.waitFor ();
                                    } catch (final Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    try {
                                        //System.out.println("pushing and cleaning");
                                        //final Process p = Runtime.getRuntime().exec(new String[]{"zsh","-c","adb push /Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder2/ /data/app/ "});
                                        final List<String> commands = new ArrayList<String>();
                                        commands.add("/Users/haiyang/Library/Android/sdk/platform-tools/adb");
                                        commands.add("push");
                                        commands.add("/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder2/"+fname);
                                        commands.add("/data/app/");
                                        final ProcessBuilder pb = new ProcessBuilder (commands);
                                        pb.start ();
//                                        final List<String> commands2 = new ArrayList<String>();
//                                        commands2.add("rm");
//                                        commands2.add("/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder2/*");
//                                        final ProcessBuilder pb2 = new ProcessBuilder (commands);
//                                        pb2.start ();
                                        //final Process p2 = Runtime.getRuntime().exec(new String[]{"zsh","-c","rm /Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder2/*"});
                                        //p2.waitFor ();
                                    } catch (final Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    try {
                                        Thread.sleep(2000);
                                    } catch (final InterruptedException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                }else {
                                }
                                scanner.close ();
                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case "dex":
                            break;
                        case "instrdex":
                            break;
                        default:
                            break;
                    }


                }
            }
        }
    }
    public static void main(final String args[]){
        try {
            //final Process p = Runtime.getRuntime().exec(new String[]{"zsh","-c","ls"});
            //Runtime.getRuntime().exec ("ls").waitFor ();
            final List<String> commands = new ArrayList<String>();
            commands.add("/Users/haiyang/Library/Android/sdk/platform-tools/adb");
            commands.add("pull");
            commands.add("/data/data/disl/");
            commands.add("/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder");
            final ProcessBuilder pb = new ProcessBuilder (commands);
            pb.start ();
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }
}
