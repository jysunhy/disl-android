package ch.usi.dag.disldroidserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import ch.usi.dag.disl.DiSL;

public class UserConfiguration {
    private static final String DEFAULT_EXCL = "NONE";
    static DiSLFinder finder = DiSLFinder.getInstance();
    public UserConfiguration(){}
    static UserConfiguration instance = null;
    public static UserConfiguration getInstance(){
        if(instance == null) {
            instance = new UserConfiguration();
        }
        return instance;
    }
    public static void setInstance(final UserConfiguration newInstance){
        instance = newInstance;
    }

    public void addDex(final Dex dex){
        if(dex.dexname.contains ("core.jar")){
            if(dex.exclusionListFile.equals (DEFAULT_EXCL)){
                dex.exclusionListFile = "excl.sample";
            }
            if(dex.status == -1){
                dex.status = 0;
            }
        }
        this.dexMap.put (dex.dexname, dex);
    }
    public static class Dex {
        public Dex (
            final String dexname, final int status) {
            super ();
            this.dexname = dexname;
            this.status = status;
            this.exclusionListFile = DEFAULT_EXCL;
        }
        public Dex (
            final String dexname, final int status, final String dislClass, final String exclusionListFile) {
            super ();
            this.dexname = dexname;
            this.status = status;
            this.dislClasses = dislClass;
            this.exclusionListFile = exclusionListFile;
        }
        @Override
        public String toString(){
            String res = dexname + " " + status;
            if(status > 0) {
                res += " " + dislClasses + " " + exclusionListFile;
            }
            return res;
        }
        public String dexname = "";
        public int status = -1;
        public String dislClasses = "";
        public String exclusionListFile = "";
    }
    HashMap<String, Dex> dexMap = new HashMap <String, UserConfiguration.Dex> ();

    public static UserConfiguration parseRaw (final File configFile) {
        final UserConfiguration res = new UserConfiguration ();
        if(!configFile.exists ()){
            return res;
        }
        System.out.println(FolderWorker.lineSeparator);
        System.out.println("Reading config from "+configFile.getAbsolutePath ());
        try {
            final Scanner fileScanner = new Scanner (configFile);
            while(fileScanner.hasNextLine ()){
                final String line = fileScanner.nextLine ();
                //System.out.println(line);
                final String para[] = line.split (" ");
                final int status = Integer.parseInt (para[1]);
                Dex dex;
                if(status > 0){
                    dex = new Dex(para[0], status, para[2], para[3]);
                    //if(para[3].equals (DEFAULT_EXCL)) {
                    System.out.println("config: "+para[0] + " using "+para[2]);
                    //}else {
                    //    System.out.println("config: "+para[0] + " using "+para[2]+" with exclusion defined in "+para[3]);
                    //}
                }else {
                    dex = new Dex(para[0], status);
                }
                res.addDex (dex);
            }
            fileScanner.close ();
        } catch (final FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println();
        return res;
    }

    public static UserConfiguration parseRawInteractive (final File configFile) {
        final UserConfiguration res = new UserConfiguration ();
        try {
            final Scanner fileScanner = new Scanner (configFile);
            while(fileScanner.hasNextLine ()){
                final String line = fileScanner.nextLine ();
                if(line.endsWith (DEFAULT_EXCL)){
                    System.out.println(line.substring (0, line.length ()-DEFAULT_EXCL.length ()-1));
                }else {
                    System.out.println(line);
                }
                final String para[] = line.split (" ");
                final int status = Integer.parseInt (para[1]);
                Dex dex;
                if(status > 0){
                    dex = new Dex(para[0], status, para[2], DEFAULT_EXCL);
                }else {
                    dex = new Dex(para[0], status);
                }
                final Dex tmp = queryUser (para[0], status);
                if(tmp != null) {
                    System.out.println("update entry for "+para[0]+" "+tmp.status+" "+tmp.dislClasses);
                    dex = tmp;
                }
                res.addDex (dex);
            }
            fileScanner.close ();
        } catch (final FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    private static Dex queryUser(final String dexName, final int status){
        Dex dex = null;
        if(status != 0){
            System.out.println("keep original config for "+dexName+"? [y/n]");
            if(checkYN ())
            {
                return dex;
            }
        }
        System.out.println("skip instrument "+dexName+" or not? [y/n]");
        if(!checkYN()){
            dex = new Dex (dexName, 1);
            while(true) {
                System.out.println("choose analysis (separated by ':'):");
                final String[] analyses = finder.getAnalyses ();
                for(int i = 0; i < analyses.length; i++){
                    System.out.println("\t["+i+"]."+analyses[i]);
                }
                final String analysisProp = getString ();
                dex.dislClasses = "";
                for(final String k:analysisProp.split (":")){
                    try{
                        final int idx = Integer.parseInt (k);
                        if(idx >= analyses.length || idx < 0) {
                            System.out.println("index "+k+" out of range");
                            continue;
                        }
                        dex.dislClasses+=analyses[idx]+":";
                    }catch(final NumberFormatException e){
                        System.out.println("number required separated by ':'"+k);
                        continue;
                    }
                }
                if(dex.dislClasses.endsWith (":")){
                    dex.dislClasses = dex.dislClasses.substring (0, dex.dislClasses.length ()-1);
                }
                break;
            }
//            System.out.println("skip exclusion list? [y/n]");
//            if(!checkYN()){
//                while(true) {
//                    System.out.println("set path to exclusion list:");
//                    final String excl = getString ();
//                    if(!new File(excl).exists ()){
//                        System.out.println("cannot find "+excl);
//                        continue;
//                    }else {
//                        dex.exclusionListFile = excl;
//                        break;
//                    }
//                }
//            } else {
//                dex.exclusionListFile = DEFAULT_EXCL;
//            }

            dex.exclusionListFile = DEFAULT_EXCL;
        }else {
            dex = new Dex (dexName, -1);
        }
        return dex;
    }
    static Scanner scanner = new Scanner (System.in);
    private static boolean checkYN(){
//        final String confirm = "";
//        while(confirm.equals("")){
//            confirm = scanner.nextLine ();
//        }
        final String confirm = scanner.nextLine ();
        System.out.println(confirm);
        return confirm.equals ("Y") || confirm.equals ("y") || confirm.equals ("");
    }
    private static boolean fakeCheckYN(){
      final String confirm = "y";
      System.out.println(confirm);
      return confirm.equals ("Y") || confirm.equals ("y") || confirm.equals ("");
  }
    public static String getString(){
        final String res = scanner.nextLine ();
        return res;
    }



    public void writeToFile(final File outputFile){
        FileWriter fw;
        try {
            fw = new FileWriter (outputFile);
            for(final Dex dex: dexMap.values ()){
                if(isBuiltin (dex.dexname)){
                    fw.write (dex.toString ()+"\n");
                }
            }
            for(final Dex dex: dexMap.values ()){
                if(!isBuiltin (dex.dexname)){
                    if(isAdvertisementDex (dex.dexname)) {
                        continue;
                    }
                    fw.write (dex.toString ()+"\n");
                }
            }
            fw.flush ();
            fw.close ();
        }catch (final IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(final String args[]) {
        final UserConfiguration config = UserConfiguration.parseRawInteractive (new File("disl.config"));
        config.writeToFile (new File("output.disl.config"));
    }

    boolean isAdvertisementDex(final String dexName){
        final String shortName = dexName.substring(dexName.lastIndexOf('/')+1);
        return shortName.startsWith ("ads");
    }

    public boolean needQuery (final String name) {
        if(isAdvertisementDex (name)){
            return false;
        }
        if(!dexMap.containsKey (name)) {
            return true;
        }
        return dexMap.get (name).status == 0;
    }

    public boolean shouldInstrument (final String name) {
        if(!dexMap.containsKey (name)) {
            return false;
        }
        return dexMap.get (name).status > 0;
    }

    DiSL getDiSL(final String dexName){
        if(dexMap.get (dexName).dislClasses == null || dexMap.get (dexName).dislClasses.equals ("")){
            return null;
        }
        final String disls = dexMap.get (dexName).dislClasses;
        String dislClassPaths="";
        for(final String disl:disls.split (":")){
            dislClassPaths += finder.getByName (disl).file.getAbsolutePath ()+",";
        }
        if(dislClassPaths.endsWith (",")){
            dislClassPaths = dislClassPaths.substring (0, dislClassPaths.length ()-1);
        }
        String excl = dexMap.get (dexName).exclusionListFile;
        if(excl.equals (DEFAULT_EXCL)){
            excl = null;
        }else if(!new File(excl).exists ()){
            System.out.println("warning: cannot find exclusion file "+ excl + " for "+dexName);
            excl = null;
        }
        DiSL newdisl = null;
        try {
            newdisl = new DiSL(false, dislClassPaths, excl);
        } catch (final Exception e) {
            e.printStackTrace();
        }
//        final Dex dex = DiSLConfig.dexMap.get(jarName);
//        if(AndroidInstrumenter.dislMap.get (jarName)==null){
//            if(dex == null){
//                if(DiSLConfig.default_disl_classes.equals ("")) {
//                    return null;
//                }
//                try{
//                    newdisl = new DiSL (DiSLConfig.default_bypass, DiSLConfig.default_disl_classes,"");
//                }catch (final Exception e){
//                    e.printStackTrace ();
//                }
//
//            }else{
//                if(dex.dislClass.equals ("")) {
//                    return null;
//                }
//                try {
//                    newdisl = new DiSL(dex.bypass, dex.dislClass,"");
//                    //newdisl = new DiSL(true, dex.dislClass,"");
//                } catch (final DiSLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//        if(newdisl != null) {
//            AndroidInstrumenter.dislMap.put (jarName, newdisl);
//        }
        return newdisl;
    }
    public void queryAndUpdate (final String dexName) {
        final Dex dex = queryUser(dexName, 0);
        if(dex.exclusionListFile.equals (DEFAULT_EXCL)) {
            System.out.println("update entry for "+dexName+" "+dex.status+" "+dex.dislClasses);
        } else {
            System.out.println("update entry for "+dexName+" "+dex.status+" "+dex.dislClasses+" "+dex.exclusionListFile);
        }
        this.addDex (dex);
        final File lf = new File(FolderWorker.localConfig);
        this.writeToFile (lf);
        FolderWorker.adbPushFile (FolderWorker.getDevice (), FolderWorker.configPathInDevice, lf);
    }
    static String[] nexus5BuiltIn={
        "/system/framework/core.jar",
        "/system/framework/conscrypt.jar",
        "/system/framework/okhttp.jar",
        "/system/framework/core-junit.jar",
        "/system/framework/bouncycastle.jar",
        "/system/framework/ext.jar",
        "/system/framework/framework.jar",
        "/system/framework/framework2.jar",
        "/system/framework/telephony-common.jar",
        "/system/framework/voip-common.jar",
        "/system/framework/mms-common.jar",
        "/system/framework/android.policy.jar",
        "/system/framework/services.jar",
        "/system/framework/apache-xml.jar",
        "/system/framework/webviewchromium.jar",
        "/system/framework/javax.obex.jar",
        "/system/framework/com.google.android.media.effects.jar",
        "/system/framework/com.android.future.usb.accessory.jar",
        "/system/framework/android.test.runner.jar",
        "/system/framework/com.google.widevine.software.drm.jar",
        "/system/framework/com.google.android.maps.jar",
        "/system/framework/qcrilhook.jar",
        "/system/framework/com.android.location.provider.jar",
        "/system/framework/serviceitems.jar",
        "/system/framework/am.jar",
        "/system/framework/bmgr.jar",
        "/system/framework/bu.jar",
        "/system/framework/content.jar",
        "/system/framework/ime.jar",
        "/system/framework/input.jar",
        "/system/framework/media_cmd.jar",
        "/system/framework/monkey.jar",
        "/system/framework/pm.jar",
        "/system/framework/requestsync.jar",
        "/system/framework/settings.jar",
        "/system/framework/svc.jar",
        "/system/framework/uiautomator.jar",
        "/system/framework/wm.jar",
        "/system/priv-app/SettingsProvider.apk",
        "/system/priv-app/VoiceDialer.apk",
        "/system/app/MediaUploader.apk",
        "/system/priv-app/GooglePartnerSetup.apk",
        "/system/app/GoogleHome.apk",
        "/system/app/Chrome.apk",
        "/system/priv-app/DefaultContainerService.apk",
        "/system/priv-app/DownloadProvider.apk",
        "/system/app/HoloSpiralWallpaper.apk",
        "/system/app/TimeService.apk",
        "/system/app/YouTube.apk",
        "/system/app/DownloadProviderUi.apk",
        "/system/app/PinyinIME.apk",
        "/system/priv-app/Dialer.apk",
        "/system/app/WAPPushManager.apk",
        "/system/priv-app/ConfigUpdater.apk",
        "/system/priv-app/WallpaperCropper.apk",
        "/system/priv-app/Keyguard.apk",
        "/system/app/DeskClock.apk",
        "/system/app/PackageInstaller.apk",
        "/system/app/VideoEditor.apk",
        "/system/priv-app/Settings.apk",
        "/system/app/GenieWidget.apk",
        "/system/priv-app/BackupRestoreConfirmation.apk",
        "/system/app/Maps.apk",
        "/system/app/PlayGames.apk",
        "/system/app/DocumentsUI.apk",
        "/system/app/CertInstaller.apk",
        "/system/app/SoundRecorder.apk",
        "/system/priv-app/InputDevices.apk",
        "/system/app/Bluetooth.apk",
        "/system/priv-app/MusicFX.apk",
        "/system/app/CalendarGoogle.apk",
        "/system/priv-app/GoogleServicesFramework.apk",
        "/system/app/Music2.apk",
        "/system/priv-app/GoogleFeedback.apk",
        "/system/app/TelephonyProvider.apk",
        "/system/priv-app/Wallet.apk",
        "/system/app/PhotoTable.apk",
        "/system/app/NfcNci.apk",
        "/system/priv-app/GoogleLoginService.apk",
        "/system/app/NoiseField.apk",
        "/system/app/ChromeBookmarksSyncAdapter.apk",
        "/system/app/Magazines.apk",
        "/system/app/QuickSearchBox.apk",
        "/system/app/HTMLViewer.apk",
        "/system/app/GoogleTTS.apk",
        "/system/app/Gmail2.apk",
        "/system/app/GalleryGoogle.apk",
        "/system/app/LatinImeGoogle.apk",
        "/system/app/OpenWnn.apk",
        "/system/priv-app/MediaProvider.apk",
        "/system/app/Videos.apk",
        "/system/app/LiveWallpapers.apk",
        "/system/priv-app/Velvet.apk",
        "/system/app/BasicDreams.apk",
        "/system/priv-app/VpnDialogs.apk",
        "/system/priv-app/CalendarProvider.apk",
        "/system/app/Drive.apk",
        "/system/app/PlusOne.apk",
        "/system/priv-app/PrebuiltGmsCore.apk",
        "/system/app/PacProcessor.apk",
        "/system/priv-app/ContactsProvider.apk",
        "/system/priv-app/ProxyHandler.apk",
        "/system/priv-app/FusedLocation.apk",
        "/system/app/Provision.apk",
        "/system/app/GoogleEars.apk",
        "/system/app/Books.apk",
        "/system/priv-app/Tag.apk",
        "/system/app/MagicSmokeWallpapers.apk",
        "/system/priv-app/TeleService.apk",
        "/system/app/Street.apk",
        "/system/priv-app/SetupWizard.apk",
        "/system/app/Email.apk",
        "/system/app/UpdateSetting.apk",
        "/system/app/KeyChain.apk",
        "/system/app/VisualizationWallpapers.apk",
        "/system/app/PhaseBeam.apk",
        "/system/priv-app/ExternalStorageProvider.apk",
        "/system/app/Calculator.apk",
        "/system/priv-app/SystemUI.apk",
        "/system/app/UserDictionaryProvider.apk",
        "/system/app/PrintSpooler.apk",
        "/system/priv-app/Mms.apk",
        "/system/priv-app/SharedStorageBackup.apk",
        "/system/priv-app/GoogleBackupTransport.apk",
        "/system/priv-app/Contacts.apk",
        "/system/priv-app/Shell.apk",
        "/system/priv-app/talkback.apk",
        "/system/app/GoogleContactsSyncAdapter.apk",
        "/system/app/Camera2.apk",
        "/system/priv-app/Phonesky.apk",
        "/system/app/Exchange2.apk",
        "/system/app/QuickOffice.apk",
        "/system/priv-app/OneTimeInitializer.apk",
        "/system/app/GoogleEarth.apk",
        "/system/app/Galaxy4.apk",
        "/system/app/LiveWallpapersPicker.apk",
        "/system/app/Keep.apk",
        "/system/app/Hangouts.apk"
    };

    private static boolean isBuiltin(final String name){
        for(final String dexName : nexus5BuiltIn){
            if(name.equals (dexName)) {
                return true;
            }
        }
        return false;
    }
}
