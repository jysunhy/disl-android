package ch.usi.dag.et.tools.etracks.disl;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.staticcontext.BytecodeStaticContext;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;


/**
 * Utility class containing assorted guards for the instrumentation.
 *
 * @author Lubomir Bulej
 * @author Aibek Sarimbekov
 */
final class Guard {

    private Guard () {
        // prevent instantiation from outside
    }

    //
    // Guards
    //

    static final class ObjectConstructorOnly {
        @GuardMethod
        public static boolean isObjectConstructor (final MethodStaticContext msc) {
            return __isObjectConstructor (msc.thisClassName (), msc.thisMethodName ());
        }
    }


    static final class NonObjectConstructors {
        @GuardMethod
        public static boolean isNonObjectConstructor (final MethodStaticContext msc) {
          return __methodIsConstructor (msc.thisMethodName()) && !__isObjectClass (msc.thisClassName ());
        }
    }



    static final class InstanceMethodsExceptConstructors {
        @GuardMethod
        public static boolean isInstanceMethodButNotConstructor (final MethodStaticContext msc) {
            return !msc.isMethodStatic() && !__methodIsConstructor (msc.thisMethodName ());
        }
    }


    static final class StaticMethods {
        @GuardMethod
        public static boolean isStaticMethod (final MethodStaticContext msc) {
            return msc.isMethodStatic ();
        }
    }

    //

    /**
     * Indicates whether a method or constructor invocation accepts any
     * reference arguments. This should automatically exclude {@link Object}
     * constructors, which don't take any arguments at all.
     */
    static final class ReferenceAcceptingInvocations {
        @GuardMethod
        public static boolean invocationAcceptsReferences (
            final MethodStaticContext msc
        ) {
            final String methodDesc = msc.thisMethodDescriptor ();
            for (final Type argumentType : Type.getArgumentTypes (methodDesc)) {
                if (__typeIsReference (argumentType)) {
                    return true;
                }
            }

            return false;
        }
    }


    /**
     * Indicates whether a method returns a reference result. This automatically
     * excludes constructors, because they don't return anything.
     */
    static final class ReferenceReturningInvocations {
        @GuardMethod
        public static boolean invocationReturnsReference (
            final MethodStaticContext msc
        ) {
            final String methodDesc = msc.thisMethodDescriptor ();
            return __typeIsReference (Type.getReturnType (methodDesc));
        }
    }

    //

    static final class ObjectConstructorInvocations {
        @GuardMethod
        public static boolean isObjectConstructorInvocation (
            final MethodInvocationStaticContext misc
        ) {
            return __isObjectConstructor (misc.getOwnerInternalName (), misc.getName ());
        }
    }

    //

    static final class PrimitiveFields {
        @GuardMethod
        public static boolean isApplicable (final FieldAccessStaticContext fsc) {
            return !__typeIsReference (fsc.getDescriptor ());
        }
    }

    static final class ReferenceFields {
        @GuardMethod
        public static boolean isApplicable (final FieldAccessStaticContext fsc) {
            return __typeIsReference (fsc.getDescriptor ());
        }
    }

    //

    static final class SinglePrimitiveArrayAllocations {
        @GuardMethod
        public static boolean isApplicable (final BytecodeStaticContext bsc, final MultiArrayStaticContext masc) {
            final int opcode = bsc.getBytecodeNumber ();
            final boolean isNewArray = (opcode == Opcodes.NEWARRAY);
            final boolean isMultiANewArray = (opcode == Opcodes.MULTIANEWARRAY);

            return isNewArray || (
                isMultiANewArray && __isOneDimensionalPrimitiveMultiArray (
                    masc.getDimensions (), masc.getElementTypeDescriptor ()
                )
            );
        }

        private static boolean __isOneDimensionalPrimitiveMultiArray (
            final int dimensionCount, final String elementTypeDescriptor
        ) {
            return dimensionCount == 1 && ! __typeIsReference (elementTypeDescriptor);
        }
    }


    static final class SingleReferenceArrayAllocations {
        @GuardMethod
        public static boolean isApplicable (final BytecodeStaticContext bsc, final MultiArrayStaticContext masc) {
            final int opcode = bsc.getBytecodeNumber ();
            final boolean isANewArray = (opcode == Opcodes.ANEWARRAY);
            final boolean isMultiANewArray = (opcode == Opcodes.MULTIANEWARRAY);

            return isANewArray || (
                isMultiANewArray && __isOneDimensionalReferenceMultiArray (
                    masc.getDimensions (), masc.getElementTypeDescriptor ()
                )
            );
        }

        private static boolean __isOneDimensionalReferenceMultiArray (
            final int dimensionCount, final String elementTypeDescriptor
        ) {
            return dimensionCount == 1 && __typeIsReference (elementTypeDescriptor);
        }
    }

    //

    static final class MultiPrimitiveArrayAllocations {
        @GuardMethod
        public static boolean isApplicable (final BytecodeStaticContext bsc, final MultiArrayStaticContext masc) {
            final boolean isMultiArray = (bsc.getBytecodeNumber () == Opcodes.MULTIANEWARRAY);

            return isMultiArray && __isMultiDimensionalPrimitiveMultiArray (
                masc.getDimensions (), masc.getElementTypeDescriptor ()
            );
        }

        private static boolean __isMultiDimensionalPrimitiveMultiArray (
            final int dimensionCount, final String elementTypeDescriptor
        ) {
            return dimensionCount > 1 && ! __typeIsReference (elementTypeDescriptor);
        }
    }


    static final class MultiReferenceArrayAllocations {
        @GuardMethod
        public static boolean isApplicable (final BytecodeStaticContext bsc, final MultiArrayStaticContext masc) {
            final boolean isMultiArray = (bsc.getBytecodeNumber () == Opcodes.MULTIANEWARRAY);

            return isMultiArray && __isMultiDimensionalReferenceMultiArray (
                masc.getDimensions (), masc.getElementTypeDescriptor ()
            );
        }

        private static boolean __isMultiDimensionalReferenceMultiArray (
            final int dimensionCount, final String elementTypeDescriptor
        ) {
            return dimensionCount > 1 && __typeIsReference (elementTypeDescriptor);
        }
    }


    //
    // Utility methods
    //

    private static boolean __isObjectConstructor (
        final String internalClassName, final String methodName
    ) {
        return __isObjectClass (internalClassName) && __methodIsConstructor (methodName);
    }


    private static boolean __isObjectClass (final String internalClassName) {
        return Type.getInternalName (Object.class).equals (internalClassName);
    }


    private static boolean __methodIsConstructor (final String methodName) {
        return "<init>".equals (methodName);
    }


    private static boolean __methodIsInitializer (final String methodName) {
        return "<clinit>".equals (methodName);
    }

    //

    private static boolean __typeIsReference (final String typeDescriptor) {
        return __typeIsReference (Type.getType (typeDescriptor));
    }

    private static boolean __typeIsReference (final Type type) {
        final int sort = type.getSort ();
        return sort == Type.OBJECT || sort == Type.ARRAY;
    }

}
