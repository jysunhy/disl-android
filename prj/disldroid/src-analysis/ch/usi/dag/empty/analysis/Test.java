package ch.usi.dag.empty.analysis;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.Forkable;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;

public class Test extends RemoteAnalysis{
    public void test(){
        System.out.println("Success");
    }
    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {
    }

    @Override
    public void atExit (final Context context) {
    }
}

