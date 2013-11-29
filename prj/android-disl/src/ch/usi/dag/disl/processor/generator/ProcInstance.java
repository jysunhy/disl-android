package ch.usi.dag.disl.processor.generator;

import java.util.List;

import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;

public class ProcInstance {

	private ArgumentProcessorMode procApplyType;
	private List<ProcMethodInstance> methods;

	public ProcInstance(ArgumentProcessorMode procApplyType,
			List<ProcMethodInstance> methods) {
		super();
		this.procApplyType = procApplyType;
		this.methods = methods;
	}

	public ArgumentProcessorMode getProcApplyType() {
		return procApplyType;
	}
	
	public List<ProcMethodInstance> getMethods() {
		return methods;
	}
}
