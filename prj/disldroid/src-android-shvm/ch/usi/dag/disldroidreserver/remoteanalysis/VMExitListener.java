package ch.usi.dag.disldroidreserver.remoteanalysis;

import ch.usi.dag.disldroidreserver.shadow.Context;

public interface VMExitListener {
    public void onVMExit (final Context context);
}
