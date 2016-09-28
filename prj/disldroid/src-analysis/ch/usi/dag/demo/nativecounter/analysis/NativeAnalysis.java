package ch.usi.dag.demo.nativecounter.analysis;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;



public class NativeAnalysis extends RemoteAnalysis{
    public static class NativeInfo {
        public NativeInfo (final String fromPkg, final String toPkg) {
            this.fromPackage = fromPkg;
            this.toPackage = toPkg;
        }
        public String fromPackage;
        // public String fromDex;
        public String toPackage;
        // public String toDex;
        @Override
        public int hashCode () {
            return (fromPackage + toPackage).hashCode ();
        }
        @Override
        public boolean equals(final Object obj){
            if(obj == null) {
                return false;
            }
            return this.hashCode () == obj.hashCode ();
        }
    }

    static class NativeRecord {
        public int counter = 0;
    }

    static ConcurrentHashMap <NativeInfo, NativeRecord> invocationCounter = new ConcurrentHashMap <NativeInfo, NativeRecord> ();

    static int countDown = 100;

    public void nativeCall (
        final Context ctx, final ShadowString from, final ShadowString to) {
        final NativeInfo nativeInfo = new NativeInfo(from.toString (), to.toString ());
        NativeRecord record = new NativeRecord ();
        final NativeRecord tmp = invocationCounter.putIfAbsent (nativeInfo, record);
        if (tmp != null) {
            record = tmp;
        }
        record.counter++;
        countDown--;
        if (countDown < 0) {
            System.out.println ("#################################################");
            for (final Entry <NativeInfo, NativeRecord> entry : invocationCounter.entrySet ()) {
                System.out.println (entry.getKey ().fromPackage
                    + " => " + entry.getKey ().toPackage + " : "
                    + entry.getValue ().counter);
            }
            countDown = 100;
        }
    }


    @Override
    public void atExit (final Context context) {

    }

    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {

    }
}
