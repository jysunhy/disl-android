package ch.usi.dag.disl.coderep;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.TryCatchBlockNode;

import ch.usi.dag.disl.localvar.SyntheticLocalVar;
import ch.usi.dag.disl.localvar.ThreadLocalVar;
import ch.usi.dag.disl.util.AsmHelper.ClonedCode;

/**
 * Stores various information about a piece of java bytecode.
 */
public class Code {

	private InsnList instructions;
	private List<TryCatchBlockNode> tryCatchBlocks;
	private Set<SyntheticLocalVar> referencedSLVs;
	private Set<ThreadLocalVar> referencedTLVs;
	private Set<StaticContextMethod> staticContexts;
	private boolean usesDynamicContext;
	private boolean usesClassContext;
	// the code contains handler that handles exception and doesn't propagate
	// it further - can cause stack inconsistency that has to be handled
	private boolean containsHandledException;

	/**
	 * Constructs the Code structure.
	 */
	public Code(InsnList instructions, List<TryCatchBlockNode> tryCatchBlocks,
			Set<SyntheticLocalVar> referencedSLVs,
			Set<ThreadLocalVar> referencedTLVs,
			Set<StaticContextMethod> staticContexts,
			boolean usesDynamicContext,
			boolean usesClassContext,
			boolean containsHandledException) {
		super();
		this.instructions = instructions;
		this.tryCatchBlocks = tryCatchBlocks;
		this.referencedSLVs = referencedSLVs;
		this.referencedTLVs = referencedTLVs;
		this.staticContexts = staticContexts;
		this.usesDynamicContext = usesDynamicContext;
		this.usesClassContext = usesClassContext; 
		this.containsHandledException = containsHandledException;
	}
	
	/**
	 * Returns an ASM instruction list.
	 */
	public InsnList getInstructions() {
		return instructions;
	}

	/**
	 * Returns list of exceptions (as represented in ASM).
	 */
	public List<TryCatchBlockNode> getTryCatchBlocks() {
		return tryCatchBlocks;
	}

	/**
	 * Returns list of all synthetic local variables referenced in the code. 
	 */
	public Set<SyntheticLocalVar> getReferencedSLVs() {
		return referencedSLVs;
	}
	
	/**
	 * Returns list of all thread local variables referenced in the code. 
	 */
	public Set<ThreadLocalVar> getReferencedTLVs() {
		return referencedTLVs;
	}

	/**
	 * Returns list of all static contexts referenced in the code. 
	 */
	public Set<StaticContextMethod> getStaticContexts() {
		return staticContexts;
	}

	/**
	 * Returns true if the code is using dynamic context. False otherwise. 
	 */
	public boolean usesDynamicContext() {
		return usesDynamicContext;
	}
	
	/**
	 * Returns true if the code is using class context. False otherwise. 
	 */
	public boolean usesClassContext() {
		return usesClassContext;
	}
	
	/**
	 * Returns true if the code contains catch block (handles exception). 
	 */
	public boolean containsHandledException() {
		return containsHandledException;
	}
	
	public Code clone() {
		// clone code first
		ClonedCode cc = ClonedCode.create (instructions, tryCatchBlocks);
		
		return new Code (
			cc.getInstructions (), cc.getTryCatchBlocks (),
			new HashSet <SyntheticLocalVar> (referencedSLVs),
			new HashSet <ThreadLocalVar> (referencedTLVs),
			new HashSet <StaticContextMethod> (staticContexts),
			usesDynamicContext, usesClassContext, containsHandledException
		);
	}
}
