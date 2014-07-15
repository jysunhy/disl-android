package ch.usi.dag.disl.classparser;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.VarInsnNode;

import ch.usi.dag.disl.exception.DiSLFatalException;
import ch.usi.dag.disl.exception.ParserException;
import ch.usi.dag.disl.exception.ReflectionException;
import ch.usi.dag.disl.util.AsmHelper.Insns;
import ch.usi.dag.disl.util.ReflectionHelper;

// package visible
abstract class ParserHelper {

    public static Class<?> getGuard(Type guardType) throws ReflectionException {

        if(guardType == null) {
            return null;
        }

        return ReflectionHelper.resolveClass(guardType);
    }

    // NOTE: first parameter is modified by this function
    public static <T> void parseAnnotation(T parsedDataObject,
            AnnotationNode annotation) {

        try {

            // nothing to do
            if(annotation.values == null) {
                return;
            }

            Iterator<?> it = annotation.values.iterator();

            while (it.hasNext()) {

                // get attribute name
                String name = (String) it.next();

                // find correct field
                Field attr = parsedDataObject.getClass().getField(name);

                if (attr == null) {

                    throw new DiSLFatalException("Unknow attribute "
                            + name
                            + " in annotation "
                            + Type.getType(annotation.desc).toString()
                            + ". This may happen if annotation class is changed"
                            + "  but parser class is not.");
                }

                // set attribute value into the field
                attr.set(parsedDataObject, it.next());
            }

        } catch (Exception e) {
            throw new DiSLFatalException(
                    "Reflection error wihle parsing annotation", e);
        }
    }

    public static void usesContextProperly(String className, String methodName,
            String methodDescriptor, InsnList instructions)
            throws ParserException {

        Type[] types = Type.getArgumentTypes(methodDescriptor);
        int maxArgIndex = 0;

        // count the max index of arguments
        for (int i = 0; i < types.length; i++) {

            // add number of occupied slots
            maxArgIndex += types[i].getSize();
        }

        // The following code assumes that all disl snippets are static
        for (AbstractInsnNode instr : Insns.selectAll (instructions)) {

            switch (instr.getOpcode()) {
            // test if the context is stored somewhere else
            case Opcodes.ALOAD: {

                int local = ((VarInsnNode) instr).var;

                if (local < maxArgIndex
                        && instr.getNext().getOpcode() == Opcodes.ASTORE) {
                    throw new ParserException("In method " + className
                            + "." + methodName + " - method parameter"
                            + " (context) cannot be stored into local"
                            + " variable");
                }

                break;
            }
                // test if something is stored in the context
            case Opcodes.ASTORE: {

                int local = ((VarInsnNode) instr).var;

                if (local < maxArgIndex) {
                    throw new ParserException("In method " + className
                            + "." + methodName + " - method parameter"
                            + " (context) cannot be overwritten");
                }

                break;
            }
            }
        }
    }
}
