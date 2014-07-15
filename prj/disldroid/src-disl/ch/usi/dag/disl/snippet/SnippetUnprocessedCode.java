package ch.usi.dag.disl.snippet;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

import ch.usi.dag.disl.coderep.Code;
import ch.usi.dag.disl.coderep.UnprocessedCode;
import ch.usi.dag.disl.dynamicbypass.DynamicBypass;
import ch.usi.dag.disl.exception.ProcessorException;
import ch.usi.dag.disl.exception.ReflectionException;
import ch.usi.dag.disl.exception.StaticContextGenException;
import ch.usi.dag.disl.localvar.LocalVars;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.marker.Marker;
import ch.usi.dag.disl.processor.Proc;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.util.AsmHelper;
import ch.usi.dag.disl.util.AsmHelper.Insns;

/**
 * Contains unprocessed code of the Snippet.
 */
public class SnippetUnprocessedCode extends UnprocessedCode {

    private String className;
    private String methodName;
    private boolean dynamicBypass;
    private boolean usesProcessorContext;

    /**
     * Creates unprocessed code structure.
     */
    public SnippetUnprocessedCode(String className, String methodName,
            InsnList instructions, List<TryCatchBlockNode> tryCatchBlocks,
            Set<String> declaredStaticContexts, boolean usesDynamicContext,
            boolean dynamicBypass, boolean usesClassContext,
            boolean usesProcessorContext) {

        super(instructions, tryCatchBlocks, declaredStaticContexts,
                usesDynamicContext, usesClassContext);
        this.className = className;
        this.methodName = methodName;
        this.dynamicBypass = dynamicBypass;
        this.usesProcessorContext = usesProcessorContext;
    }

    /**
     * Processes the stored data and creates snippet code structure.
     */
    public SnippetCode process(LocalVars allLVs, Map<Type, Proc> processors,
            Marker marker, boolean exceptHandler, boolean useDynamicBypass)
            throws StaticContextGenException, ReflectionException,
            ProcessorException {

        // process code
        Code code = super.process(allLVs);

        // process snippet code

        InsnList instructions = code.getInstructions();
        List<TryCatchBlockNode> tryCatchBlocks = code.getTryCatchBlocks();

        // *** CODE PROCESSING ***
        // !NOTE ! : Code processing has to be done before "processors in use"
        // analysis otherwise the instruction reference produced by this
        // analysis may be wrong
        // NOTE: methods are modifying arguments

        if (useDynamicBypass && dynamicBypass) {
            insertDynamicBypass(instructions);
        }

        if (exceptHandler) {
            // catch all exceptions
            // it is forbidden to throw an exception in a snippet
            insertExceptionHandler(instructions, tryCatchBlocks);
        }

        // *** CODE ANALYSIS ***

        Map<Integer, ProcInvocation> invokedProcessors =
            new HashMap<Integer, ProcInvocation>();

        int insnIndex = 0;
        for (AbstractInsnNode insn : Insns.selectAll (instructions)) {
            // *** Parse processors in use ***
            // no other modifications to the code should be done before weaving
            // otherwise, produced instruction reference can be invalid

            ProcessorInfo processor = insnInvokesProcessor (
                insn, insnIndex, processors, marker
            );

            if (processor != null) {
                invokedProcessors.put (
                    processor.getInstrPos (),
                    processor.getProcInvoke ()
                );
            }

            insnIndex++;
        }

        return new SnippetCode(
            instructions, tryCatchBlocks, code.getReferencedSLVs(),
            code.getReferencedTLVs(), code.containsHandledException(),
            code.getStaticContexts(), code.usesDynamicContext(),
            code.usesClassContext(), usesProcessorContext,
            invokedProcessors
        );
    }

    private static class ProcessorInfo {

        private Integer instrPos;
        private ProcInvocation procInvoke;

        public ProcessorInfo(Integer instrPos, ProcInvocation procInvoke) {
            super();
            this.instrPos = instrPos;
            this.procInvoke = procInvoke;
        }

        public Integer getInstrPos() {
            return instrPos;
        }

        public ProcInvocation getProcInvoke() {
            return procInvoke;
        }
    }

    private ProcessorInfo insnInvokesProcessor(AbstractInsnNode instr, int i,
            Map<Type, Proc> processors, Marker marker)
            throws ProcessorException, ReflectionException {

        final String APPLY_METHOD = "apply";

        // check method invocation
        if (!(instr instanceof MethodInsnNode)) {
            return null;
        }

        MethodInsnNode min = (MethodInsnNode) instr;

        // check if the invocation is processor invocation
        if (!(min.owner.equals(Type.getInternalName(ArgumentProcessorContext.class))
                && min.name.equals(APPLY_METHOD))) {
            return null;
        }

        // resolve load parameter instruction
        AbstractInsnNode secondParam = instr.getPrevious();
        AbstractInsnNode firstParam = secondParam.getPrevious();

        // NOTE: object parameter is ignored - will be removed by weaver

        // first parameter has to be loaded by LDC
        if (firstParam == null || firstParam.getOpcode() != Opcodes.LDC) {
            throw new ProcessorException("In snippet " + className + "."
                    + methodName + " - pass the first (class)"
                    + " argument of a ProcMethod.apply method direcltly."
                    + " ex: ProcMethod.apply(ProcMethod.class,"
                    + " ArgumentProcessorMode.METHOD_ARGS)");
        }

        // second parameter has to be loaded by GETSTATIC
        if (secondParam == null || secondParam.getOpcode() != Opcodes.GETSTATIC) {
            throw new ProcessorException("In snippet " + className + "."
                    + methodName + " - pass the second (type)"
                    + " argument of a ProcMethod.apply method direcltly."
                    + " ex: ProcMethod.apply(ProcMethod.class,"
                    + " ArgumentProcessorMode.METHOD_ARGS)");
        }

        Object asmType = ((LdcInsnNode) firstParam).cst;

        if (!(asmType instanceof Type)) {
            throw new ProcessorException("In snippet " + className + "."
                    + methodName + " - unsupported processor type "
                    + asmType.getClass().toString());
        }

        Type processorType = (Type) asmType;

        ArgumentProcessorMode procApplyType = ArgumentProcessorMode
                .valueOf(((FieldInsnNode) secondParam).name);

        // if the processor apply type is CALLSITE_ARGS
        // the only allowed marker is BytecodeMarker
        if(ArgumentProcessorMode.CALLSITE_ARGS.equals(procApplyType)
                && marker.getClass() != BytecodeMarker.class) {
            throw new ProcessorException(
                    "ArgumentProcessor applied in mode CALLSITE_ARGS in method "
                    + className + "." + methodName
                    + " can be used only with BytecodeMarker");
        }

        Proc processor = processors.get(processorType);

        if (processor == null) {
            throw new ProcessorException("In snippet " + className + "."
                    + methodName + " - unknow processor used: "
                    + processorType.getClassName());
        }

        ProcInvocation prcInv = new ProcInvocation(processor, procApplyType);

        // get instruction index

        return new ProcessorInfo(i, prcInv);
    }

    private void insertExceptionHandler(InsnList instructions,
            List<TryCatchBlockNode> tryCatchBlocks) {

        // NOTE: snippet should not throw an exception
        // this method inserts try-finally for each snippet and fails
        // immediately in the case of exception produced by snippet

        // inserts
        // try {
        // ... original code
        // } finally {
        //   System.err.println("...");
        //   e.printStackTrace(); // possible here :)
        //   System.exit(...);
        // }

        // resolve types
        Type typeSystem = Type.getType(System.class);
        Type typePS = Type.getType(PrintStream.class);
        Type typeString = Type.getType(String.class);
        Type typeThrowable = Type.getType(Throwable.class);

        // add try label at the beginning
        LabelNode tryBegin = new LabelNode();
        instructions.insert(tryBegin);

        // ## try {

        // ## }

        // normal flow should jump after handler
        LabelNode handlerEnd = new LabelNode();
        instructions.add(new JumpInsnNode(Opcodes.GOTO, handlerEnd));

        // ## after normal flow

        // add try label at the end
        LabelNode tryEnd = new LabelNode();
        instructions.add(tryEnd);

        // ## after abnormal flow - exception handler

        // add handler begin
        LabelNode handlerBegin = new LabelNode();
        instructions.add(handlerBegin);

        // add error report

        // put error stream on the stack
        instructions.add(new FieldInsnNode(Opcodes.GETSTATIC,
                typeSystem.getInternalName(),
                "err",
                typePS.getDescriptor()));

        // put error report on the stack
        instructions.add(new LdcInsnNode(
                "Snippet " + className + "." + methodName
                + " introduced exception that was not"
                + " handled. This would change the application control flow."
                + " Exiting..."));

        // invoke printing
        instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                typePS.getInternalName(),
                "println",
                "(" + typeString.getDescriptor() + ")V"));

        // duplicate exception reference on the stack
        instructions.add(new InsnNode(Opcodes.DUP));

        // invoke printing stack trace
        instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                typeThrowable.getInternalName(),
                "printStackTrace",
                "()V"));

        // add system exit

        // add exit code
        final int EXIT_CODE = 666;
        instructions.add(AsmHelper.loadConst(EXIT_CODE));

        // invoke System.exit()
        instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                typeSystem.getInternalName(), "exit", "(I)V"));

        // add throw exception just for proper stack frame calculation
        instructions.add(new InsnNode(Opcodes.ATHROW));

        // add handler end
        instructions.add(handlerEnd);

        // ## add handler to the list
        tryCatchBlocks.add(new TryCatchBlockNode(tryBegin, tryEnd,
                handlerBegin, null));

    }

    private void insertDynamicBypass(InsnList instructions) {

        // inserts
        // DynamicBypass.activate();
        // ... original code
        // DynamicBypass.deactivate();

        // resolve type
        Type typeDB = Type.getType(DynamicBypass.class);

        // add invocation of activate at the beginning
        instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC,
                typeDB.getInternalName(), "activate", "()V"));

        // ## after normal flow

        // add invocation of deactivate - normal flow
        instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                typeDB.getInternalName(), "deactivate", "()V"));
    }
}
