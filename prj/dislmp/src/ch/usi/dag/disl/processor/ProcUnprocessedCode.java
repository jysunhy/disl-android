package ch.usi.dag.disl.processor;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.TryCatchBlockNode;

import ch.usi.dag.disl.coderep.Code;
import ch.usi.dag.disl.coderep.UnprocessedCode;
import ch.usi.dag.disl.exception.ReflectionException;
import ch.usi.dag.disl.exception.StaticContextGenException;
import ch.usi.dag.disl.localvar.LocalVars;

public class ProcUnprocessedCode extends UnprocessedCode {

	private boolean usesArgumentContext;

	public ProcUnprocessedCode(InsnList instructions,
			List<TryCatchBlockNode> tryCatchBlocks,
			Set<String> declaredStaticContexts, boolean usesDynamicContext,
			boolean usesClassContext,
			boolean usesArgumentContext) {
		super(instructions, tryCatchBlocks, declaredStaticContexts,
				usesDynamicContext, usesClassContext);
		this.usesArgumentContext = usesArgumentContext;
	}

	public ProcCode process(LocalVars allLVs) throws StaticContextGenException,
			ReflectionException {

		// process code
		Code code = super.process(allLVs);

		return new ProcCode(code.getInstructions(), code.getTryCatchBlocks(),
				code.getReferencedSLVs(), code.getReferencedTLVs(),
				code.containsHandledException(), code.getStaticContexts(),
				code.usesDynamicContext(), code.usesClassContext(),
				usesArgumentContext);
	}
}
