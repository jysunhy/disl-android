package ch.usi.dag.disldroidreserver.msg.analyze.mtdispatch;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerFatalException;
import ch.usi.dag.disldroidreserver.msg.analyze.AnalysisInvocation;

/**
 * Thread processing analysis tasks
 */
class AnalysisThread extends Thread {

	final protected AnalysisTaskExecutor taskExecutor;

	public AnalysisThread(AnalysisTaskExecutor taskHolder) {
		this.taskExecutor = taskHolder;
	}
	
	public void run() {
		
		try {

			// get task to process
			AnalysisTask at = taskExecutor.getTask();
			
			while(! at.isSignalingEnd()) {
				
				// invoke all methods in this task
				for(AnalysisInvocation ai : at.getInvocations()) {
					ai.invoke();
				}
				
				// get task to process
				at = taskExecutor.getTask();
			}
		
		} catch (InterruptedException e) {
			throw new DiSLREServerFatalException(
					"Object free thread interupted while waiting on task", e);
		}
	}
	
}
