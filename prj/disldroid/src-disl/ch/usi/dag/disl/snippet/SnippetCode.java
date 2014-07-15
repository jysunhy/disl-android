package ch.usi.dag.disl.snippet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.TryCatchBlockNode;

import ch.usi.dag.disl.coderep.Code;
import ch.usi.dag.disl.coderep.StaticContextMethod;
import ch.usi.dag.disl.localvar.SyntheticLocalVar;
import ch.usi.dag.disl.localvar.ThreadLocalVar;
import ch.usi.dag.disl.util.AsmHelper.ClonedCode;

/**
 * Stores the information about snippet code.
 */
public class SnippetCode extends Code {

    private boolean usesProcessorContext;
    // integer (key) is an index of an instruction in snippet code that invokes
    // processor
    private Map<Integer, ProcInvocation> invokedProcessors;

    public SnippetCode(InsnList instructions,
            List<TryCatchBlockNode> tryCatchBlocks,
            Set<SyntheticLocalVar> referencedSLV,
            Set<ThreadLocalVar> referencedTLV,
            boolean containsHandledException,
            Set<StaticContextMethod> staticContexts,
            boolean usesDynamicContext,
            boolean usesClassContext,
            boolean usesProcessorContext,
            Map<Integer, ProcInvocation> invokedProcessors
            ) {

        super(instructions, tryCatchBlocks, referencedSLV, referencedTLV,
                staticContexts, usesDynamicContext, usesClassContext,
                containsHandledException);
        this.invokedProcessors = invokedProcessors;
        this.usesProcessorContext = usesProcessorContext;
    }

    /**
     * Returns list of all argument processors referenced in the code.
     */
    public Map<Integer, ProcInvocation> getInvokedProcessors() {
        return invokedProcessors;
    }


    public SnippetCode clone () {
        // clone code first
        ClonedCode cc = ClonedCode.create (getInstructions (), getTryCatchBlocks ());

        return new SnippetCode (
            cc.getInstructions (), cc.getTryCatchBlocks (),
            new HashSet <SyntheticLocalVar> (getReferencedSLVs ()),
            new HashSet <ThreadLocalVar> (getReferencedTLVs ()),
            containsHandledException (),
            new HashSet <StaticContextMethod> (getStaticContexts ()),
            usesDynamicContext (), usesClassContext (), usesProcessorContext,
            new HashMap <Integer, ProcInvocation> (invokedProcessors)
        );
    }
}
