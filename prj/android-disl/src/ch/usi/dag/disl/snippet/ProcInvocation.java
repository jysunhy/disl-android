package ch.usi.dag.disl.snippet;

import ch.usi.dag.disl.processor.Proc;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;

public class ProcInvocation {

	private Proc processor;
	private ArgumentProcessorMode procApplyType;
	
	public ProcInvocation(Proc processor, ArgumentProcessorMode procApplyType) {
		super();
		this.processor = processor;
		this.procApplyType = procApplyType;
	}

	public Proc getProcessor() {
		return processor;
	}

	public ArgumentProcessorMode getProcApplyType() {
		return procApplyType;
	}
}
