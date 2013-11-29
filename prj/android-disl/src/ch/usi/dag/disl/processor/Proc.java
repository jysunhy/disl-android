package ch.usi.dag.disl.processor;

import java.util.List;

import ch.usi.dag.disl.exception.ReflectionException;
import ch.usi.dag.disl.exception.StaticContextGenException;
import ch.usi.dag.disl.localvar.LocalVars;

public class Proc {

	private String name;
	private List<ProcMethod> methods;

	public Proc(String name, List<ProcMethod> methods) {
		super();
		this.name = name;
		this.methods = methods;
	}

	public String getName() {
		return name;
	}
	
	public List<ProcMethod> getMethods() {
		return methods;
	}
	
	public void init(LocalVars allLVs) throws StaticContextGenException,
			ReflectionException {
		
		for(ProcMethod method : methods) {
			method.init(allLVs);
		}
	}
}
