package ch.usi.dag.disl.processor.generator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import ch.usi.dag.disl.exception.DiSLFatalException;
import ch.usi.dag.disl.exception.ProcessorException;
import ch.usi.dag.disl.guard.GuardHelper;
import ch.usi.dag.disl.processor.Proc;
import ch.usi.dag.disl.processor.ProcArgType;
import ch.usi.dag.disl.processor.ProcMethod;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.snippet.ProcInvocation;
import ch.usi.dag.disl.snippet.Shadow;
import ch.usi.dag.disl.snippet.Snippet;

public class ProcGenerator {

    Map<Proc, ProcInstance> insideMethodPIs = new HashMap<Proc, ProcInstance>();

    public PIResolver compute(Map<Snippet, List<Shadow>> snippetMarkings)
            throws ProcessorException {

        PIResolver piResolver = new PIResolver();

        // for each snippet
        for (Snippet snippet : snippetMarkings.keySet()) {

            Map<Integer, ProcInvocation> invokedProcs = snippet.getCode()
                    .getInvokedProcessors();

            for (Shadow shadow : snippetMarkings.get(snippet)) {

                // for each processor defined in snippet
                for (Integer instrPos : invokedProcs.keySet()) {

                    ProcInvocation prcInv = invokedProcs.get(instrPos);

                    ProcInstance prcInst = null;

                    // handle apply type
                    switch (prcInv.getProcApplyType()) {

                    case METHOD_ARGS: {
                        prcInst = computeInsideMethod(shadow, prcInv);
                        break;
                    }

                    case CALLSITE_ARGS: {
                        prcInst = computeBeforeInvocation(shadow, prcInv);
                        break;
                    }

                    default:
                        throw new DiSLFatalException(
                                "Proc computation not defined");
                    }

                    if(prcInst != null) {
                        // add result to processor instance resolver
                        piResolver.set(shadow, instrPos, prcInst);
                    }
                }
            }
        }

        return piResolver;
    }

    private ProcInstance computeInsideMethod(Shadow shadow,
            ProcInvocation prcInv) {

        // all instances of inside method processor will be the same
        // if we have one, we can use it multiple times

        ProcInstance procInst = insideMethodPIs.get(prcInv.getProcessor());

        if (procInst == null) {
            procInst = createProcInstance(ArgumentProcessorMode.METHOD_ARGS,
                    shadow.getMethodNode().desc, shadow, prcInv);
        }

        return procInst;
    }

    private ProcInstance computeBeforeInvocation(Shadow shadow,
            ProcInvocation prcInv) throws ProcessorException {

        // NOTE: ProcUnprocessedCode checks that CALLSITE_ARGS is
        // used only with BytecodeMarker

        // because it is BytecodeMarker, it should have only one end
        if(shadow.getRegionEnds().size() > 1) {
            throw new DiSLFatalException(
                    "Expected only one end in marked region");
        }

        // get instruction from the method code
        // the method invocation is the instruction marked as end
        AbstractInsnNode instr = shadow.getRegionEnds().get(0);

        String fullMethodName = shadow.getClassNode().name + "."
                + shadow.getMethodNode().name;

        // check - method invocation
        if (!(instr instanceof MethodInsnNode)) {
            throw new ProcessorException("ArgumentProcessor "
                    + prcInv.getProcessor().getName()
                    + " is not applied before method invocation in method "
                    + fullMethodName);
        }

        MethodInsnNode methodInvocation = (MethodInsnNode) instr;

        return createProcInstance(ArgumentProcessorMode.CALLSITE_ARGS,
                methodInvocation.desc, shadow, prcInv);
    }

    private ProcInstance createProcInstance(ArgumentProcessorMode procApplyType,
            String methodDesc, Shadow shadow, ProcInvocation prcInv) {

        List<ProcMethodInstance> procMethodInstances =
            new LinkedList<ProcMethodInstance>();

        // get argument types
        Type[] argTypeArray = Type.getArgumentTypes(methodDesc);

        // create processor method instances for each argument if applicable
        for (int i = 0; i < argTypeArray.length; ++i) {

            List<ProcMethodInstance> pmis = createMethodInstances(i,
                    argTypeArray.length, argTypeArray[i],
                    prcInv.getProcessor(), shadow, prcInv);

            procMethodInstances.addAll(pmis);
        }

        if(procMethodInstances.isEmpty()) {
            return null;
        }

        // create new processor instance
        return new ProcInstance(procApplyType, procMethodInstances);
    }

    private List<ProcMethodInstance> createMethodInstances(int argPos,
            int argsCount, Type argType, Proc processor, Shadow shadow,
            ProcInvocation prcInv) {

        ProcArgType methodArgType = ProcArgType.valueOf(argType);

        List<ProcMethodInstance> result = new LinkedList<ProcMethodInstance>();

        // traverse all methods and find the proper ones
        for (ProcMethod method : processor.getMethods()) {

            // check argument type
            if(method.getTypes().contains(methodArgType)) {

                ProcMethodInstance pmi = new ProcMethodInstance(argPos,
                        argsCount, methodArgType, argType.getDescriptor(),
                        method.getCode());

                // check guard
                if (isPMGuardApplicable(method.getGuard(), shadow, pmi)) {

                    // add method
                    result.add(pmi);
                }
            }
        }

        return result;
    }

    private boolean isPMGuardApplicable(Method guard, Shadow shadow,
            ProcMethodInstance pmi) {

        // evaluate processor method guard
        return GuardHelper.guardApplicable(guard, shadow, pmi.getArgPos(),
                pmi.getArgTypeDesc(), pmi.getArgsCount());
    }
}
