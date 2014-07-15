package ch.usi.dag.bc.disl;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.ROClassWriter;
import ch.usi.dag.disl.transformer.Transformer;

public class ClinitTransformer implements Transformer {

    @Override
    public byte [] transform (final byte [] classfileBuffer) throws Exception {
        final ClassReader classReader = new ClassReader(classfileBuffer);

        final ClassNode classNode = new ClassNode(Opcodes.ASM4);
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

        for (final MethodNode methodNode : classNode.methods) {
            if ("<clinit>".equals (methodNode.name)){
                return classfileBuffer;
            }
        }

        final MethodNode clinit = new MethodNode (Opcodes.ASM4, "<clinit>", "()V", null, null);
        clinit.instructions.add (new InsnNode (Opcodes.RETURN));

        classNode.methods.add (clinit);

        final ClassWriter classWriter = new ROClassWriter (ClassWriter.COMPUTE_MAXS);

        // return as bytes
        classNode.accept (classWriter);
        return classWriter.toByteArray ();
    }

    @Override
    public boolean propagateUninstrumentedClasses () {
        return false;
    }

}
