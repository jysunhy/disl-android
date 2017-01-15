package ch.usi.dag.disldroidserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.RemoteFile;


public class FolderWorker {
    private static final String PROP_DISL_CONFIG              = "config.path";
    public static final String dislConfigPath = System.getProperty (PROP_DISL_CONFIG, null);
    public static final String localConfig = dislConfigPath == null?"config.local":dislConfigPath;


    public static String lineSeparator = "********************************************";

    public static String configPathInDevice = "/data/app/disl.config";

    public static boolean isFromFolder = true;

    public static UserConfiguration config = null;

    public static String curDex = "";

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

    static JadbDevice getDevice(){
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
        //System.out.println ("Device detected " + device.getSerial ());
        return device;
    }

    public void start () {
        //
        new File("adbfolder/send/table").mkdirs ();
        new File("adbfolder/send/dex").mkdirs ();
        new File("adbfolder/recv/table").mkdirs ();
        new File("adbfolder/recv/dex").mkdirs ();
        cleanFolder (new File ("adbfolder"));

        if(dislConfigPath == null) {
            final File originalConfig = new File("adbfolder/disl.config.original");
            adbPullFile (getDevice (), configPathInDevice, originalConfig);
            config = UserConfiguration.parseRawInteractive (originalConfig);
        }else {
            config = UserConfiguration.parseRaw (new File(dislConfigPath));
        }
        UserConfiguration.setInstance (config);
        final File updatedConfig = new File("adbfolder/disl.config");
        config.writeToFile (updatedConfig);
        adbPushFile (getDevice(), configPathInDevice, updatedConfig);
        adbPushFile (getDevice(), "/data/app/disl.ok", updatedConfig);
        //DiSLConfig.parseXml ();
//        DiSLConfig.parseRaw ();
        while (true) {
            //AndroidInstrumenter.checkConfigXMLChange ();
            adbPull (getDevice (), "/data/app/send/table/", "adbfolder/send/table/", true);

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
                            System.out.println("empty table file, delete for re download");
                            fileEntry.delete ();
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
                            //System.out.println ("Notice uninstrumented " + name);
                            System.out.println(FolderWorker.lineSeparator);
                            //System.out.println ("bytecode " + name + " to be instrumented");
                            final File localDex = new File("adbfolder/send/dex/"+fname.replace ("dextable", "dex"));
                            while(!localDex.exists () || localDex.length ()<size){
                                adbPullFile(getDevice (), "/data/app/send/dex/"+fname.replace ("dextable", "dex"), localDex);
                            }
                            byte [] dexCode;
                            do {
                                dexCode = Utils.readbytes (localDex);
                            } while (dexCode.length != size);
                            byte [] instrClass = null;
if(true) {
//if(false) {
                            final String jarName = name.substring (name.lastIndexOf ('/') + 1);

                            if (config.needQuery (name)) {
                                System.out.println ("Configuration for "+name+" is missing, configure it now");
                                config.queryAndUpdate(name);
                            }

                            if(config.shouldInstrument(name))
                            {
                                System.out.println("Instrumenting "+name);
                                curDex = name;
                                instrClass = Worker.instrumentJar (name, dexCode);
                            }
}
                            if (instrClass == null) {
                                instrClass = dexCode;
                                System.out.println ("Skip instrumenting "
                                    + name + "");
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
                            System.out.println ();
                            adbPushFile (getDevice (), "/data/app/recv/dex/"+fname.replace ("dextable", "dex"), instrF);
                            adbPushFile(getDevice (), "/data/app/recv/table/"+fname, tableF);
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
                    if(skipOverride && lf.exists () && lf.length () >1) {
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
        //System.out.println("pulling "+pathInDevice);
        try {
                    device.pull (new RemoteFile (pathInDevice), lf);
        } catch (final Exception e) {
            e.printStackTrace ();
            //System.out.println("pull "+pathInDevice+" fails, it's not ready or removed by the android");
        }
        //System.out.println("pulling "+pathInDevice+" finishes");
    }

    public static void adbPushFile (
        final JadbDevice device, final String pathInDevice, final File lf) {
        //System.out.println("pushing "+pathInDevice);
        try {
                    device.push (lf, new RemoteFile (pathInDevice));
        } catch (final Exception e) {
            e.printStackTrace ();
        }
        //System.out.println("pushing "+pathInDevice+" finishes");
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
}
