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
        this.dexMap.put (dex.dexname, dex);
    }
    public static class Dex {
        public Dex (
            final String dexname, final int status) {
            super ();
            this.dexname = dexname;
            this.status = status;
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
                if(status > 0){
                    res.addDex (new Dex(para[0], status, para[2], para[3]));
                    if(para[3].equals (DEFAULT_EXCL)) {
                        System.out.println("config: "+para[0] + " using "+para[2]);
                    }else {
                        System.out.println("config: "+para[0] + " using "+para[2]+" with exclusion defined in "+para[3]);
                    }
                }else {
                    res.addDex (new Dex(para[0], status));
                }
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
                System.out.println(line);
                final String para[] = line.split (" ");
                final int status = Integer.parseInt (para[1]);
                Dex dex;
                if(status > 0){
                    dex = new Dex(para[0], status, para[2], para[3]);
                }else {
                    dex = new Dex(para[0], status);
                }
                final Dex tmp = queryUser (para[0], status);
                if(tmp != null) {
                    System.out.println("update entry for "+para[0]+" "+tmp.status+" "+tmp.dislClasses+" "+tmp.exclusionListFile);
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
            System.out.println("skip exclusion list? [y/n]");
            if(!checkYN()){
                while(true) {
                    System.out.println("set path to exclusion list:");
                    final String excl = getString ();
                    if(!new File(excl).exists ()){
                        System.out.println("cannot find "+excl);
                        continue;
                    }else {
                        dex.exclusionListFile = excl;
                        break;
                    }
                }
            } else {
                dex.exclusionListFile = DEFAULT_EXCL;
            }
        }else {
            dex = new Dex (dexName, -1);
        }
        return dex;
    }
    static Scanner scanner = new Scanner (System.in);
    private static boolean checkYN(){
        final String confirm = scanner.nextLine ();
        return confirm.equals ("Y") || confirm.equals ("y") || confirm.equals("");
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
                //there is a google ads dex with a hashed name ads[-][0-9]*.jar
                final String fullName = dex.dexname;
                final String shortName = fullName.substring(fullName.lastIndexOf('/')+1);
                if(shortName.startsWith ("ads")){
                    continue;
                }
                fw.write (dex.toString ()+"\n");
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

    public boolean needQuery (final String name) {
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
        System.out.println("update entry for "+dexName+" "+dex.status+" "+dex.dislClasses+" "+dex.exclusionListFile);
        this.addDex (dex);
        final File lf = new File(FolderWorker.localConfig);
        this.writeToFile (lf);
        FolderWorker.adbPushFile (FolderWorker.getDevice (), FolderWorker.configPathInDevice, lf);
    }

}
