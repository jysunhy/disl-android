package ch.usi.dag.disldroidserver;

import java.io.File;
import java.util.HashMap;

public class DiSLFinder {
    public static class DiSLClassFile{
        public DiSLClassFile (final File file, final String signature) {
            super ();
            this.file = file;
            this.signature = signature;
        }
        File file;
        String signature;
    }
    HashMap<String, DiSLClassFile> dislfileMap = new HashMap <String, DiSLFinder.DiSLClassFile> ();

    public void readDir(final File dir){
        if(!dir.isDirectory ()) {
            return;
        }
        System.out.println(FolderWorker.lineSeparator);
        System.out.println("Scanning available analysis...");
        readDir(dir, dir.getAbsolutePath ());
        System.out.println("");
    }

    public DiSLClassFile getByName(final String sig){
        return dislfileMap.get (sig);
    }
    private void readDir(final File dir, final String prefix){
        for(final File entry : dir.listFiles ()){
            if(entry.isDirectory ()){
                readDir(entry, prefix);
            }else {
                if(entry.getAbsolutePath ().contains ("DiSLClass")){
                    String sig = entry.getAbsolutePath ().substring (prefix.length ()).replace ('/', '.');
                    if(sig.startsWith (".")) {
                        sig = sig.substring (1);
                    }
                    if(sig.endsWith (".class")){
                        sig = sig.substring (0, sig.length ()-6);
                    }
                    if(sig.endsWith (".java")) {
                        sig = sig.substring (0, sig.length ()-5);
                    }

                    System.out.println("found analysis "+sig);
                    dislfileMap.put (sig, new DiSLClassFile (entry, sig));
                }
            }
        }
    }

    public String[] getAnalyses(){
        return dislfileMap.keySet ().toArray (new String[0]);
    }

    static DiSLFinder instance = null;

    public static DiSLFinder getInstance () {
        if(instance == null) {
            instance = new DiSLFinder();
            instance.readDir (new File("output/build/instr"));
        }
        return instance;
    }
}
