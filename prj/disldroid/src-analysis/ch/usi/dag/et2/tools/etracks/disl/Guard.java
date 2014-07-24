package ch.usi.dag.et2.tools.etracks.disl;

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

    /**
     * Selects constructor methods of the {@link Object} class only.
     */
    static final class ObjectConstructorOnly {
        @GuardMethod
        public static boolean isObjectConstructor (final MethodStaticContext msc) {
            return msc.isMethodConstructor () && __classIsObject (msc.thisClassName ());
        }
    }


    /**
     * Selects constructor methods of all classes except {@link Object}.
     */
    static final class NonObjectConstructors {
        @GuardMethod
        public static boolean isNonObjectConstructor (final MethodStaticContext msc) {
          return msc.isMethodConstructor () && !__classIsObject (msc.thisClassName ());
        }
    }


    /**
     * Selects all instance methods except constructors.
     */
    static final class InstanceMethodsExceptConstructors {
        @GuardMethod
        public static boolean isInstanceMethodButNotConstructor (final MethodStaticContext msc) {
            return !msc.isMethodStatic() && !msc.isMethodConstructor ();
        }
    }


    /**
     * Selects all static methods (including class initializers).
     */
    static final class StaticMethods {
        @GuardMethod
        public static boolean isStaticMethod (final MethodStaticContext msc) {
            return msc.isMethodStatic ();
        }
    }

    //

    /**
     * Selects methods that accept at least one reference argument. This
     * automatically excludes {@link Object} constructors, which don't take any
     * arguments at all.
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
     * Selects methods that return a reference results. This automatically
     * excludes all constructors, because they don't return anything.
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
            return __classIsObject (misc.getOwnerInternalName ()) && misc.isConstructor ();
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

    static final class ReferenceFieldsOutsideInitializer {
        @GuardMethod
        public static boolean isApplicable (final FieldAccessStaticContext fsc, final MethodStaticContext msc) {
            return __typeIsReference (fsc.getDescriptor ()) && !msc.isMethodInitializer ();
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

    private static boolean __classIsObject (final String internalName) {
        return Type.getInternalName (Object.class).equals (internalName);
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
