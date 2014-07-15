package ch.usi.dag.disldroidreserver.msg.analyze.mtdispatch;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerFatalException;

/**
 * Manages executors
 */
class ATEManager {

	// we need concurrent for waitForAllToProcessEpoch method
	protected final ConcurrentMap<Long, AnalysisTaskExecutor> liveExecutors =
			new ConcurrentHashMap<Long, AnalysisTaskExecutor>();

	protected final BlockingQueue<AnalysisTaskExecutor> endingExecutors =
			new LinkedBlockingQueue<AnalysisTaskExecutor>();

	/**
	 * Retrieves executor. Creates new one if it does not exists.
	 */
	public AnalysisTaskExecutor getExecutor(final long id) {

		AnalysisTaskExecutor ate = liveExecutors.get(id);

		// create new executor
		if(ate == null) {

			ate = new AnalysisTaskExecutor(this);
			liveExecutors.put(id, ate);
		}

		return ate;
	}

	/**
	 * Retrieves all live executors
	 */
	public Iterable<AnalysisTaskExecutor> getAllLiveExecutors() {

		return Collections.unmodifiableCollection(liveExecutors.values());
	}

	/**
	 * Moves executor from live queue to the ending queue
	 */
	public void executorIsEnding(final long id) {

        AnalysisTaskExecutor removedATE = liveExecutors.remove (id);

        if (removedATE == null) {
            removedATE = new AnalysisTaskExecutor (this);
        }

		try {
			endingExecutors.put(removedATE);
		} catch (final InterruptedException e) {
			throw new DiSLREServerFatalException(
					"Cannot add executor to the ending queue", e);
		}
	}

	/**
	 * Changes global epoch in all executors
	 */
	public void globalEpochChange(final long newEpoch) {

		for(final AnalysisTaskExecutor ate : liveExecutors.values()) {
			ate.globalEpochChanged(newEpoch);
		}

		for(final AnalysisTaskExecutor ate : endingExecutors) {
			ate.globalEpochChanged(newEpoch);
		}
	}

	/**
	 * Waits for all executors to process an epoch
	 */
	public void waitForAllToProcessEpoch(final long epochToProcess) {

		try {

			for(final AnalysisTaskExecutor ate : liveExecutors.values()) {
				ate.waitForEpochProcessing(epochToProcess);
			}

			for(final AnalysisTaskExecutor ate : endingExecutors) {
				ate.waitForEpochProcessing(epochToProcess);
			}

		} catch (final InterruptedException e) {
			throw new DiSLREServerFatalException(
					"Interupt occured while waiting for processing of an epoch",
					e);
		}
	}

	/**
	 * Waits for all executors to end
	 */
	public void waitForAllToEnd() {

		try {

			for(final AnalysisTaskExecutor ate : liveExecutors.values()) {
				ate.awaitTermination();
			}

			for(final AnalysisTaskExecutor ate : endingExecutors) {
				ate.awaitTermination();
			}

		} catch (final InterruptedException e) {
			throw new DiSLREServerFatalException(
					"Interupt occured while waiting for executor termination",
					e);
		}
	}

	/**
	 * Announces executor end. Can be called concurrently.
	 */
	public void executorEndConcurrentCallback(final AnalysisTaskExecutor ate) {
		endingExecutors.remove(ate);
	}
}
