package ch.usi.dag.disldroidserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.RemoteFile;


public class FolderWorker extends Thread {
    public static boolean isFromFolder = true;


    public FolderWorker () {
    }


    static class DexStruct {
        String path;

        // int idx;
        int size;

        int instrumentedSize;


        public DexStruct (final String path, final int size) {
            super ();
            // System.out.println(path+" "+size+" "+idx);
            this.path = path;
            // this.idx = idx;
            this.size = size;
            instrumentedSize = 0;
        }
    }


    HashMap <String, DexStruct> dexes = new HashMap <String, FolderWorker.DexStruct> ();


    long getFileSize (final File file) {
        FileInputStream fis;
        long res = 0;
        try {
            fis = new FileInputStream (file);
            res = fis.getChannel ().size ();
            fis.close ();
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        }
        return res;
    }


    @Override
    public void run () {
        //
        cleanFolder (new File ("adbfolder"));
        final JadbConnection jadb = new JadbConnection ();
        List <JadbDevice> devices = null;
        while (devices == null || devices.isEmpty ()) {
            try {
                devices = jadb.getDevices ();
                Thread.sleep (1000);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        final JadbDevice device = devices.get (0);
        System.out.println ("Device detected " + device.getSerial ());
        DiSLConfig.parseXml ();
        while (true) {
            //AndroidInstrumenter.checkConfigXMLChange ();
            adbPull (device, "/data/app/send/table/", "adbfolder/send/table/", true);

            final File folder = new File ("adbfolder/send/table/");
            for (final File fileEntry : folder.listFiles ()) {
                if (!fileEntry.isDirectory ()) {
                    final String fname = fileEntry.getName ();
                    final String [] arr = fname.split ("_");
                    Scanner scanner;
                    try {
                        scanner = new Scanner (fileEntry);
                        String [] tmp = null;
                        try {
                            final String line = scanner.nextLine ();
                            tmp = line.split (" ");
                            if (tmp.length != 3) {
                                continue;
                            }
                            scanner.close ();
                        }catch (final Throwable e){
                            scanner.close ();
                            continue;
                        }

                        final String name = tmp [0];
                        final int size = Integer.parseInt (tmp [1]);
                        final int isize = Integer.parseInt (tmp [2]);
                        if (!dexes.containsKey (fname)) {
                            dexes.put (fname, new DexStruct (name, size));
                        }
                        if (isize == 0) {
                            System.out.println ("Notice uninstrumented " + name);
                            final File localDex = new File("adbfolder/send/dex/"+fname.replace ("dextable", "dex"));
                            while(!localDex.exists () || localDex.length ()<size){
                                adbPullFile(device, "/data/app/send/dex/"+fname.replace ("dextable", "dex"), localDex);
                            }
                            byte [] dexCode;
                            do {
                                dexCode = Utils.readbytes (localDex);
                            } while (dexCode.length != size);
                            byte [] instrClass;
                            final String jarName = name.substring (name.lastIndexOf ('/') + 1);
                            if (DiSLConfig.dexMap.get (jarName) == null
                                || DiSLConfig.dexMap.get (jarName).preinstrumented_path.equals ("")) {
                                instrClass = Worker.instrumentJar (jarName, dexCode);
                                // javamop.Guard.printCounters ();
                            }
                            else {
                                instrClass = Worker.preInstrumentJar (
                                    jarName,
                                    DiSLConfig.dexMap.get (jarName).preinstrumented_path,
                                    dexCode);
                            }
                            if (instrClass == null) {
                                instrClass = dexCode;
                                System.out.println ("instrumentation failure "
                                    + name);
                            }
                            final File instrF = new File ("adbfolder/recv/dex/" + fname.replace ("dextable", "dex"));
                            instrF.createNewFile ();
                            final FileOutputStream fos = new FileOutputStream (
                                instrF);
                            fos.write (instrClass, 0, instrClass.length);
                            fos.flush ();
                            fos.close ();
                            final File tableF = new File ("adbfolder/recv/table/" + fname);
                            final FileWriter fw = new FileWriter (tableF);
                            fw.write (name + " " + size + " " + instrClass.length);
                            fw.flush ();
                            fw.close ();
                            // new File(androidFolder+"/"+fname).delete ();
                            final FileWriter fw0 = new FileWriter (new File ("adbfolder/send/table/" + fname));
                            fw0.write (name + " " + size + " " + instrClass.length);
                            fw0.flush ();
                            fw0.close ();
                            dexes.get (fname).instrumentedSize = instrClass.length;

                            adbPushFile (device, "/data/app/recv/dex/"+fname.replace ("dextable", "dex"), instrF);
                            adbPushFile(device, "/data/app/recv/table/"+fname, tableF);
                        } else {}
                    } catch (final Exception e) {
                        e.printStackTrace ();
                    }

                }
            }
        }
    }


    private static void adbPull (
        final JadbDevice device, final String pathInDevice, final String localFolder, final boolean skipOverride) {
        try {
            final List <RemoteFile> fl = device.list (pathInDevice);
            final int idx = 0;
            for (final RemoteFile f : fl) {
                final String filename = f.getPath ();
                if (!filename.startsWith (".")) {

                    final File lf = new File (localFolder + filename);
                    if(lf.exists () && skipOverride) {
                        continue;
                    } else {
                        adbPullFile(device, pathInDevice + filename, lf);
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace ();
        }
    }

    private static void adbPullFile (
        final JadbDevice device, final String pathInDevice, final File lf) {
        System.out.println("pulling "+pathInDevice);
        try {
                    device.pull (new RemoteFile (pathInDevice), lf);
        } catch (final Exception e) {
            System.out.println("pull "+pathInDevice+" fails, it's not ready or removed by the android");
        }
        System.out.println("pulling "+pathInDevice+" finishes");
    }

    private static void adbPushFile (
        final JadbDevice device, final String pathInDevice, final File lf) {
        System.out.println("pushing "+pathInDevice);
        try {
                    device.push (lf, new RemoteFile (pathInDevice));
        } catch (final Exception e) {
            e.printStackTrace ();
        }
        System.out.println("pushing "+pathInDevice+" finishes");
    }



    private static void cleanFolder (final File folder) {
        for (final File f : folder.listFiles ()) {
            if (f.isDirectory ()) {
                cleanFolder (f);
            } else {
                f.delete ();
            }
        }
    }


    public static void main (final String args []) {
        try {
            // final Process p = Runtime.getRuntime().exec(new
            // String[]{"zsh","-c","ls"});
            // Runtime.getRuntime().exec ("ls").waitFor ();
            final List <String> commands = new ArrayList <String> ();
            commands.add ("/Users/haiyang/Library/Android/sdk/platform-tools/adb");
            commands.add ("pull");
            commands.add ("/data/data/disl/");
            commands.add ("/Users/haiyang/Documents/WorkSpace/Github/disl-android/prj/disldroid/android_folder");
            final ProcessBuilder pb = new ProcessBuilder (commands);
            pb.start ();
        } catch (final Exception e) {
            e.printStackTrace ();
        }

    }
}
