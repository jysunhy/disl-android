package ch.usi.dag.disl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.CodeSizeEvaluator;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

import ch.usi.dag.disl.dynamicbypass.BypassCheck;
import ch.usi.dag.disl.exception.DiSLFatalException;
import ch.usi.dag.disl.util.AsmHelper.ClonedCode;


abstract class CodeMerger {

    private static final String BPC_CLASS = Type.getInternalName (BypassCheck.class);

    private static final String BPC_METHOD = "executeUninstrumented";

    private static final String BPC_DESC = "()Z";

    private static final int ALLOWED_SIZE = 64 * 1024; // 64KB limit


    // NOTE: the originalCN and instrumentedCN will be destroyed in the process
    // NOTE: abstract or native methods should not be included in the
    // changedMethods list
    public static ClassNode mergeClasses (final ClassNode originalCN,
        final ClassNode instrumentedCN, final Set <String> changedMethods) {

        // NOTE: that instrumentedCN can contain added fields
        // - has to be returned

        if (changedMethods == null) {
            throw new DiSLFatalException (
                "Set of changed methods cannot be null");
        }

        // no changed method - no merging
        if (changedMethods.isEmpty ()) {
            return instrumentedCN;
        }

        // merge methods one by one
        for (final MethodNode instrMN : instrumentedCN.methods) {

            // We will construct the merged method node in the instrumented
            // class node

            // skip unchanged methods
            if (!changedMethods.contains (instrMN.name + instrMN.desc)) {
                continue;
            }

            final MethodNode origMN = getMethodNode (originalCN, instrMN.name,
                instrMN.desc);

            final InsnList ilist = instrMN.instructions;
            final List <TryCatchBlockNode> tcblist = instrMN.tryCatchBlocks;

            // crate copy of the original instruction list
            // this copy will be destroyed during merging
            // we need the original code if the method is too long
            final ClonedCode origCodeCopy = ClonedCode.create (
                origMN.instructions, origMN.tryCatchBlocks
                );

            // add reference to the original code
            final LabelNode origCodeL = new LabelNode ();
            ilist.add (origCodeL);

            // add original code
            ilist.add (origCodeCopy.getInstructions ());
            // add exception handlers of the original code
            tcblist.addAll (origCodeCopy.getTryCatchBlocks ());

            // if the dynamic bypass is activated (non-zero value returned)
            // then jump to original code
            ilist.insert (new JumpInsnNode (Opcodes.IFNE, origCodeL));
            ilist.insert (new MethodInsnNode (Opcodes.INVOKESTATIC,
                BPC_CLASS, BPC_METHOD, BPC_DESC));

        }

        return instrumentedCN;
    }


    // NOTE: the originalCN and instrumentedCN will be destroyed in the process
    // NOTE: abstract or native methods should not be included in the
    // changedMethods list
    public static ClassNode handleLongMethod (final ClassNode originalCN,
        final ClassNode instrumentedCN, final boolean splitLongMethods) {

        // merge methods one by one
        for (final MethodNode instrMN : instrumentedCN.methods) {
            // calculate the code size and if it is larger then allowed size,
            // skip it
            final CodeSizeEvaluator cse = new CodeSizeEvaluator (null);
            instrMN.accept (cse);

            if (cse.getMaxSize () > ALLOWED_SIZE) {

                final MethodNode origMN = getMethodNode (originalCN, instrMN.name,
                    instrMN.desc);

                if (splitLongMethods) {
                    // split methods
                    splitLongMethods (instrumentedCN, origMN, instrMN);
                } else {

                    // insert original code into the instrumented method node
                    instrMN.instructions = origMN.instructions;
                    instrMN.tryCatchBlocks = origMN.tryCatchBlocks;

                    // print error msg
                    System.err.println ("WARNING: code of the method "
                        + instrumentedCN.name + "." + instrMN.name
                        + " is larger ("
                        + cse.getMaxSize ()
                        + ") then allowed size (" +
                        +ALLOWED_SIZE
                        + ") - skipping");
                }
            }
        }

        return instrumentedCN;
    }


    private static void splitLongMethods (final ClassNode instrumentedCN,
        final MethodNode origMN, final MethodNode instrMN) {

        // TODO jb ! add splitting for to long methods
        // - ignore clinit - output warning
        // - output warning if splitted is to large and ignore

        // check the code size of the instrumented method
        // add if to the original method that jumps to the renamed instrumented
        // method
        // add original method to the instrumented code
        // rename instrumented method
    }


    private static MethodNode getMethodNode (final ClassNode cnToSearch,
        final String methodName, final String methodDesc) {

        for (final MethodNode mn : cnToSearch.methods) {
            if (methodName.equals (mn.name) && methodDesc.equals (mn.desc)) {
                return mn;
            }
        }

        throw new RuntimeException (
            "Code merger fatal error: method for merge not found");
    }
}
