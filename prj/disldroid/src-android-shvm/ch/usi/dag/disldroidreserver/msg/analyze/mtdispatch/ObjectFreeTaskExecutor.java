package ch.usi.dag.disldroidreserver.msg.analyze.mtdispatch;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ch.usi.dag.disldroidreserver.DiSLREServer;
import ch.usi.dag.disldroidreserver.exception.DiSLREServerFatalException;
import ch.usi.dag.disldroidreserver.msg.analyze.AnalysisResolver;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.NetReferenceHelper;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;

class ObjectFreeTaskExecutor extends Thread {

	protected final ATEManager ateManager;

	protected final BlockingQueue<ObjectFreeTask> taskQueue =
			new LinkedBlockingQueue<ObjectFreeTask>();

	public ObjectFreeTaskExecutor(final ATEManager ateManager) {
		super();
		this.ateManager = ateManager;
	}

	public void addTask(final ObjectFreeTask oft) {
		taskQueue.add(oft);
	}

	private void invokeObjectFreeAnalysisHandlers(final ShadowAddressSpace shadowAddressSpace, final long objectFreeID) {

		// TODO free events should be sent to analysis that sees the shadow object

        // retrieve shadow object
        final ShadowObject obj = shadowAddressSpace.getShadowObject (objectFreeID);

        if (DiSLREServer.debug
            && NetReferenceHelper.get_class_id (objectFreeID) != 2) {
            System.out.println (Thread.currentThread ().getName ()
                + ": PROCESS-"
                + shadowAddressSpace.getContext ().pid () + " Free object "
                + Long.toHexString (objectFreeID));
        }

        if (obj != null) {

            // get all analysis objects
            final Set <RemoteAnalysis> raSet = AnalysisResolver.getAllAnalyses ();

            // invoke object free
            for (final RemoteAnalysis ra : raSet) {
                ra.objectFree (shadowAddressSpace.getContext (), obj);
            }

            // release shadow object
            shadowAddressSpace.freeShadowObject (obj);
        }
	}


	@Override
    public void run() {

		try {

			ObjectFreeTask oft = taskQueue.take();

			// main working loop
			while(! oft.isSignalingEnd()) {

				// wait for all analysis executors to finish the closing epoch
				ateManager.waitForAllToProcessEpoch(oft.getClosingEpoch());

				// invoke object free analysis handler for each free object
				for(final long objectFreeID : oft.getObjFreeIDs()) {
					invokeObjectFreeAnalysisHandlers(oft.getShadowAddressSpace (), objectFreeID);
				}

				// get task to process
				oft = taskQueue.take();
			}

		} catch (final InterruptedException e) {
			throw new DiSLREServerFatalException(
					"Object free thread interupted while waiting on task", e);
		}
	}
}
