package ch.usi.dag.disl.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import ch.usi.dag.disl.exception.DiSLFatalException;

public abstract class AsmHelper {

    public static boolean offsetBefore(InsnList ilst, int from, int to) {

        if (from >= to) {
            return false;
        }

        for (int i = from; i < to; i++) {

            if (ilst.get(i).getOpcode() != -1) {
                return true;
            }
        }

        return false;
    }

    public static AbstractInsnNode loadConst(final Object value) {
        if (value instanceof Boolean) {
            return new InsnNode(
                    ((Boolean) value) ? Opcodes.ICONST_1 : Opcodes.ICONST_0);

        } else if (value instanceof Byte ||
                value instanceof Short ||
                value instanceof Integer) {
            int intValue = 0;
            if (value instanceof Integer) {
                intValue = ((Integer) value).intValue();
            } else if (value instanceof Short) {
                intValue = ((Short) value).intValue();
            } else if (value instanceof Byte) {
                intValue = ((Byte) value).intValue();
            }

            switch (intValue) {
                case -1:
                    return new InsnNode(Opcodes.ICONST_M1);
                case 0:
                    return new InsnNode(Opcodes.ICONST_0);
                case 1:
                    return new InsnNode(Opcodes.ICONST_1);
                case 2:
                    return new InsnNode(Opcodes.ICONST_2);
                case 3:
                    return new InsnNode(Opcodes.ICONST_3);
                case 4:
                    return new InsnNode(Opcodes.ICONST_4);
                case 5:
                    return new InsnNode(Opcodes.ICONST_5);
                default:
                    if (intValue >= Byte.MIN_VALUE && intValue <= Byte.MAX_VALUE) {
                        return new IntInsnNode(Opcodes.BIPUSH, intValue);
                    } else if (intValue >= Short.MIN_VALUE && intValue <= Short.MAX_VALUE) {
                        return new IntInsnNode(Opcodes.SIPUSH, intValue);
                    } else {
                        // Make sure LDC argument is an Integer
                        return new LdcInsnNode(Integer.valueOf(intValue));
                    }
            }

        } else if (value instanceof Long) {
            final long longValue = ((Long) value).longValue();

            if (longValue == 0) {
                return new InsnNode(Opcodes.LCONST_0);
            } else if (longValue == 1) {
                return new InsnNode(Opcodes.LCONST_1);
            }

            // default to LDC

        } else if (value instanceof Float) {
            final float floatValue = ((Float) value).floatValue();

            if (floatValue == 0) {
                return new InsnNode(Opcodes.FCONST_0);
            } else if (floatValue == 1) {
                return new InsnNode(Opcodes.FCONST_1);
            } else if (floatValue == 2) {
                return new InsnNode(Opcodes.FCONST_2);
            }

            // default to LDC

        } else if (value instanceof Double) {
            final double doubleValue = ((Double) value).doubleValue();

            if (doubleValue == 0) {
                return new InsnNode(Opcodes.DCONST_0);
            } else if (doubleValue == 1) {
                return new InsnNode(Opcodes.DCONST_1);
            }

            // default to LDC
        }

        return new LdcInsnNode(value);
    }

    public static String getStringConstOperand(final AbstractInsnNode insn) {
        if (insn.getOpcode() == Opcodes.LDC) {
            final LdcInsnNode ldcNode = (LdcInsnNode) insn;
            if (ldcNode.cst instanceof String) {
                return (String) ldcNode.cst;
            } else {
                throw new DiSLFatalException("LDC operand is not a String");
            }

        } else {
            throw new DiSLFatalException(String.format(
                    "Expected LdcInsnNode, found %s (%s)",
                    insn.getClass().getSimpleName(), AsmOpcodes.valueOf(insn)
                    ));
        }
    }

    public static int getIntConstOperand(final AbstractInsnNode insn) {

        switch (insn.getOpcode()) {
            case Opcodes.ICONST_M1:
                return -1;
            case Opcodes.ICONST_0:
                return 0;
            case Opcodes.ICONST_1:
                return 1;
            case Opcodes.ICONST_2:
                return 2;
            case Opcodes.ICONST_3:
                return 3;
            case Opcodes.ICONST_4:
                return 4;
            case Opcodes.ICONST_5:
                return 5;

            case Opcodes.BIPUSH:
            case Opcodes.SIPUSH:
                return ((IntInsnNode) insn).operand;

            case Opcodes.LDC: {
                final LdcInsnNode ldc = (LdcInsnNode) insn;
                if (ldc.cst instanceof Integer) {
                    return (Integer) ldc.cst;
                } else {
                    throw new DiSLFatalException("LDC operand is not an integer");
                }
            }

            default:
                throw new DiSLFatalException(String.format(
                        "Cannot extract integer constant operand from %s (%s)",
                        insn.getClass().getSimpleName(), AsmOpcodes.valueOf(insn)
                        ));
        }
    }

    @SuppressWarnings("serial")
    public static final Map<String, Type> PRIMITIVE_TYPES = new HashMap<String, Type>() {
                                                              {
                                                                  put(Boolean.class, Type.BOOLEAN_TYPE);
                                                                  put(Byte.class, Type.BYTE_TYPE);
                                                                  put(Character.class, Type.CHAR_TYPE);
                                                                  put(Short.class, Type.SHORT_TYPE);
                                                                  put(Integer.class, Type.INT_TYPE);
                                                                  put(Float.class, Type.FLOAT_TYPE);
                                                                  put(Long.class, Type.LONG_TYPE);
                                                                  put(Double.class, Type.DOUBLE_TYPE);
                                                              }

                                                              void put(Class<?> typeClass, Type type) {
                                                                  put(Type.getInternalName(typeClass), type);
                                                              }
                                                          };

    public static Type getTypeConstOperand(final AbstractInsnNode insn) {
        //
        // Class literals are loaded on the stack in two ways:
        //
        // Literals for common classes are loaded using the LDC instruction,
        // referencing a constant pool item.
        //
        // Literals for primitive types are loaded by accessing the static
        // TYPE field in the corresponding boxing class for the type. The
        // field is a specialized Class<> instance, so the determine the type,
        // we need to look at the owner.
        //
        if (insn.getOpcode() == Opcodes.LDC) {
            final LdcInsnNode ldcNode = (LdcInsnNode) insn;
            if (ldcNode.cst instanceof Type) {
                return (Type) ldcNode.cst;
            } else {
                throw new DiSLFatalException("LDC operand is not a Type");
            }

        } else if (insn.getOpcode() == Opcodes.GETSTATIC) {
            //
            // When accessing a static TYPE field in a class boxing a primitive
            // type, the result is the primitive type class literal.
            //
            final FieldInsnNode fieldNode = (FieldInsnNode) insn;
            if ("TYPE".equals(fieldNode.name)) {
                final Type result = PRIMITIVE_TYPES.get(fieldNode.owner);
                if (result == null) {
                    throw new DiSLFatalException(String.format(
                            "Expected primitive boxing class, found %s", fieldNode.owner
                            ));
                }

                return result;

            } else {
                throw new DiSLFatalException(String.format(
                        "Expected access to static TYPE field, found %s", fieldNode.name
                        ));
            }

        } else {
            throw new DiSLFatalException(String.format(
                    "Expected LdcInsnNode, found %s (%s)",
                    insn.getClass().getSimpleName(), AsmOpcodes.valueOf(insn)
                    ));
        }
    }

    /**
     * Returns {@code true} if the given instruction loads a type constant
     * (i.e. a class literal) on the stack, {@code false} otherwise.
     * 
     * @param insn
     *            the instruction to check
     * @return
     *         {@code true} if the instruction loads a type constant on the
     *         stack, {@code false} otherwise.
     */
    public static boolean isTypeConstLoadInsn(final AbstractInsnNode insn) {
        if (insn.getOpcode() == Opcodes.LDC) {
            return ((LdcInsnNode) insn).cst instanceof Type;
        } else if (insn.getOpcode() == Opcodes.GETSTATIC) {
            final FieldInsnNode fieldInsn = (FieldInsnNode) insn;
            final Type type = PRIMITIVE_TYPES.get(fieldInsn.owner);
            return "TYPE".equals(fieldInsn.name) && type != null;

        } else {
            return false;
        }
    }

    public static VarInsnNode loadThis() {
        return loadObjectVar(0);
    }

    public static VarInsnNode loadObjectVar(final int slot) {
        return loadVar(Type.getType(Object.class), slot);
    }

    public static VarInsnNode loadVar(final Type type, final int slot) {
        return new VarInsnNode(type.getOpcode(Opcodes.ILOAD), slot);
    }

    public static VarInsnNode storeObjectVar(final int slot) {
        return storeVar(Type.getType(Object.class), slot);
    }

    public static VarInsnNode storeVar(final Type type, final int slot) {
        return new VarInsnNode(type.getOpcode(Opcodes.ISTORE), slot);
    }

    public static InsnNode loadDefault() {
        return loadDefault(Type.getType(Object.class));
    }

    public static InsnNode loadDefault(Type type) {
        switch (type.getSort()) {
            case Type.BOOLEAN:
            case Type.BYTE:
            case Type.CHAR:
            case Type.INT:
            case Type.SHORT:
                return new InsnNode(Opcodes.ICONST_0);
            case Type.LONG:
                return new InsnNode(Opcodes.LCONST_0);
            case Type.FLOAT:
                return new InsnNode(Opcodes.FCONST_0);
            case Type.DOUBLE:
                return new InsnNode(Opcodes.DCONST_0);
            case Type.OBJECT:
                // XXX LB: consider putting Type.ARRAY here as well
                return new InsnNode(Opcodes.ACONST_NULL);
            default:
                throw new DiSLFatalException(
                        "No default value for type: " + type.getDescriptor());
        }
    }

    public static TypeInsnNode checkCast(final Type type) {
        return new TypeInsnNode(Opcodes.CHECKCAST, type.getDescriptor());
    }

    public static FieldInsnNode getField(
            final String owner, final String name, final String desc
            ) {
        return new FieldInsnNode(Opcodes.GETFIELD, owner, name, desc);
    }

    public static FieldInsnNode putField(
            final String owner, final String name, final String desc
            ) {
        return new FieldInsnNode(Opcodes.PUTFIELD, owner, name, desc);
    }

    public static FieldInsnNode getStatic(
            final String owner, final String name, final String desc
            ) {
        return new FieldInsnNode(Opcodes.GETSTATIC, owner, name, desc);
    }

    public static FieldInsnNode putStatic(
            final String owner, final String name, final String desc
            ) {
        return new FieldInsnNode(Opcodes.PUTSTATIC, owner, name, desc);
    }

    public static MethodInsnNode invokeStatic(
            final Type ownerType, final String methodName, final Type methodType
            ) {
        return new MethodInsnNode(
                Opcodes.INVOKESTATIC, ownerType.getInternalName(),
                methodName, methodType.getDescriptor());
    }

    public static int getInternalParamIndex(MethodNode method, int parIndex) {

        Type[] types = Type.getArgumentTypes(method.desc);

        if (parIndex >= types.length) {
            throw new DiSLFatalException("Parameter index out of bound");
        }

        int index = 0;

        for (int i = 0; i < parIndex; i++) {

            // add number of occupied slots
            index += types[i].getSize();
        }

        if ((method.access & Opcodes.ACC_STATIC) == 0) {
            index += 1;
        }

        return index;
    }

    /**
     * Adds a label to the end of the given instruction list and replaces all
     * types of RETURN instructions in the list with a GOTO instruction to jump
     * to the label at the end of the instruction list.
     * 
     * @param insnList
     *            list of instructions to perform the replacement on
     */
    public static void replaceRetWithGoto(final InsnList insnList) {

        // collect all RETURN instructions
        List<AbstractInsnNode> returnInsns = new LinkedList<AbstractInsnNode>();
        for (AbstractInsnNode instr : allInsnsFrom(insnList)) {
            if (isReturn(instr.getOpcode())) {
                returnInsns.add(instr);
            }
        }

        if (returnInsns.size() > 1) {
            //
            // Replace all RETURN instructions with a GOTO instruction to
            // jump to a label at the end of the list.
            //
            final LabelNode endLabel = new LabelNode(new Label());
            for (final AbstractInsnNode insn : returnInsns) {
                insnList.insertBefore(insn, new JumpInsnNode(Opcodes.GOTO, endLabel));
                insnList.remove(insn);
            }

            insnList.add(endLabel);

        } else if (returnInsns.size() == 1) {
            // there is only one return at the end
            insnList.remove(returnInsns.get(0));
        }
    }

    public static final class ClonedCode {
        private final InsnList                instructions;
        private final List<TryCatchBlockNode> tryCatchBlocks;

        public ClonedCode(
                final InsnList instructions, final List<TryCatchBlockNode> tryCatchBlocks) {
            this.instructions = instructions;
            this.tryCatchBlocks = tryCatchBlocks;
        }

        public InsnList getInstructions() {
            return instructions;
        }

        public List<TryCatchBlockNode> getTryCatchBlocks() {
            return tryCatchBlocks;
        }

        public static ClonedCode create(
                final InsnList instructions, final List<TryCatchBlockNode> tryCatchBlocks
                ) {
            final Map<LabelNode, LabelNode> replacementLabels =
                    AsmHelper.createReplacementLabelMap(instructions);

            return new ClonedCode(
                    AsmHelper.cloneInsnList(instructions, replacementLabels),
                    AsmHelper.cloneTryCatchBlocks(tryCatchBlocks, replacementLabels));
        }
    }

    /**
     * Returns a clone of the given instruction list.
     * 
     * @param insnList
     *            instruction list to clone.
     * 
     * @return
     *         A cloned instruction list.
     */
    public static InsnList cloneInsnList(final InsnList insnList) {
        //
        // To clone an instruction list, we have to clone the labels and
        // use the cloned labels to clone the individual instructions.
        //
        return cloneInsnList(insnList, createReplacementLabelMap(insnList));
    }

    private static Map<LabelNode, LabelNode> createReplacementLabelMap(
            final InsnList insnList
            ) {
        //
        // Clone all the labels and key them to the original label.
        //
        final Map<LabelNode, LabelNode> result = new HashMap<LabelNode, LabelNode>();
        for (final AbstractInsnNode insn : allInsnsFrom(insnList)) {
            if (insn instanceof LabelNode) {
                final LabelNode clone = new LabelNode(new Label());
                final LabelNode original = (LabelNode) insn;
                result.put(original, clone);
            }
        }

        return result;
    }

    private static InsnList cloneInsnList(
            final InsnList insnList,
            final Map<LabelNode, LabelNode> replacementLabels
            ) {
        //
        // Clone individual instructions using the clone label map.
        //
        final InsnList result = new InsnList();
        for (final AbstractInsnNode insn : allInsnsFrom(insnList)) {
            result.add(insn.clone(replacementLabels));
        }

        return result;
    }

    private static List<TryCatchBlockNode> cloneTryCatchBlocks(
            final List<TryCatchBlockNode> tryCatchBlocks,
            final Map<LabelNode, LabelNode> replacementLabels
            ) {
        final List<TryCatchBlockNode> result = new LinkedList<TryCatchBlockNode>();
        for (final TryCatchBlockNode tcb : tryCatchBlocks) {
            final TryCatchBlockNode tcbClone = new TryCatchBlockNode(
                    replacementLabels.get(tcb.start),
                    replacementLabels.get(tcb.end),
                    replacementLabels.get(tcb.handler),
                    tcb.type
                    );
            result.add(tcbClone);
        }

        return result;
    }

    //

    public static AbstractInsnNode skipVirtualInsnsForward(AbstractInsnNode instr) {
        return skipVirtualInsns(instr, true);
    }

    public static AbstractInsnNode skipVirtualInsns(AbstractInsnNode instr,
            boolean isForward) {

        while (instr != null && isVirtualInstr(instr)) {
            instr = isForward ? instr.getNext() : instr.getPrevious();
        }

        return instr;
    }

    /**
     * Returns the first non-virtual instruction preceding a given instruction.
     * 
     * @param startInsn
     *            the starting instruction
     * 
     * @return
     *         The first non-virtual instruction preceding the given
     *         instruction,
     *         or {@code null} if there is no such instruction.
     */
    public static AbstractInsnNode prevNonVirtualInsn(final AbstractInsnNode startInsn) {
        AbstractInsnNode insn = startInsn;
        while (insn != null) {
            insn = insn.getPrevious();
            if (!isVirtualInstr(insn)) {
                return insn;
            }
        }

        // not found
        return null;
    }

    /**
     * Returns the first non-virtual instruction following a given instruction.
     * 
     * @param startInsn
     *            the starting instruction
     * 
     * @return
     *         The first non-virtual instruction following the given
     *         instruction,
     *         or {@code null} if there is no such instruction.
     */
    public static AbstractInsnNode nextNonVirtualInsn(final AbstractInsnNode start) {
        AbstractInsnNode insn = start;
        while (insn != null) {
            insn = insn.getNext();
            if (!isVirtualInstr(insn)) {
                return insn;
            }
        }

        // not found
        return null;
    }

    public static boolean isReferenceType(Type type) {
        return type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY;
    }

    public static boolean isVirtualInstr(AbstractInsnNode insn) {
        return insn.getOpcode() == -1;
    }

    // detects if the instruction list contains only return
    public static boolean containsOnlyReturn(InsnList ilst) {

        AbstractInsnNode instr = ilst.getFirst();

        while (instr != null && isVirtualInstr(instr)) {
            instr = instr.getNext();
        }

        if (instr == null) {
            throw new IllegalArgumentException("instr is null");
        }

        return isReturn(instr.getOpcode());
    }

    public static boolean isStaticFieldAccess(int opcode) {
        return opcode == Opcodes.GETSTATIC || opcode == Opcodes.PUTSTATIC;
    }

    public static boolean isReturn(int opcode) {
        return opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN;
    }

    public static boolean isBranch(AbstractInsnNode instruction) {

        int opcode = instruction.getOpcode();

        return instruction instanceof JumpInsnNode
                || instruction instanceof LookupSwitchInsnNode
                || instruction instanceof TableSwitchInsnNode
                || opcode == Opcodes.ATHROW || opcode == Opcodes.RET
                || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN);
    }

    public static boolean isConditionalBranch(AbstractInsnNode instruction) {

        int opcode = instruction.getOpcode();

        return (instruction instanceof JumpInsnNode && opcode != Opcodes.GOTO);
    }

    public static boolean mightThrowException(AbstractInsnNode instruction) {

        switch (instruction.getOpcode()) {

        // NullPointerException, ArrayIndexOutOfBoundsException
            case Opcodes.BALOAD:
            case Opcodes.DALOAD:
            case Opcodes.FALOAD:
            case Opcodes.IALOAD:
            case Opcodes.LALOAD:
            case Opcodes.BASTORE:
            case Opcodes.CASTORE:
            case Opcodes.DASTORE:
            case Opcodes.FASTORE:
            case Opcodes.IASTORE:
            case Opcodes.LASTORE:
            case Opcodes.AALOAD:
            case Opcodes.CALOAD:
            case Opcodes.SALOAD:
            case Opcodes.SASTORE:
                // NullPointerException, ArrayIndexOutOfBoundsException,
                // ArrayStoreException
            case Opcodes.AASTORE:
                // NullPointerException
            case Opcodes.ARRAYLENGTH:
            case Opcodes.ATHROW:
            case Opcodes.GETFIELD:
            case Opcodes.PUTFIELD:
                // NullPointerException, StackOverflowError
            case Opcodes.INVOKEINTERFACE:
            case Opcodes.INVOKESPECIAL:
            case Opcodes.INVOKEVIRTUAL:
                // StackOverflowError
            case Opcodes.INVOKESTATIC:
                // NegativeArraySizeException
            case Opcodes.ANEWARRAY:
                // NegativeArraySizeException, OutOfMemoryError
            case Opcodes.NEWARRAY:
            case Opcodes.MULTIANEWARRAY:
                // OutOfMemoryError, InstantiationError
            case Opcodes.NEW:
                // OutOfMemoryError
            case Opcodes.LDC:
                // ClassCastException
            case Opcodes.CHECKCAST:
                // ArithmeticException
            case Opcodes.IDIV:
            case Opcodes.IREM:
            case Opcodes.LDIV:
            case Opcodes.LREM:
                // New instruction in JDK7
            case Opcodes.INVOKEDYNAMIC:
                return true;

            default:
                return false;
        }
    }

    // helper method for boxValueOnStack
    private static MethodInsnNode constructValueOf(
            final Class<?> boxClass, final Class<?> primitiveClass
            ) {
        final Type boxType = Type.getType(boxClass);
        final Type primitiveType = Type.getType(primitiveClass);
        final Type methodType = Type.getMethodType(boxType, primitiveType);

        return invokeStatic(boxType, "valueOf", methodType);
    }

    /**
     * Returns instruction that will call the method to box the instruction
     * residing on the stack
     * 
     * @param valueType
     *            type to be boxed
     */
    public static MethodInsnNode boxValueOnStack(final Type valueType) {
        switch (valueType.getSort()) {
            case Type.BOOLEAN:
                return constructValueOf(Boolean.class, boolean.class);
            case Type.BYTE:
                return constructValueOf(Byte.class, byte.class);
            case Type.CHAR:
                return constructValueOf(Character.class, char.class);
            case Type.DOUBLE:
                return constructValueOf(Double.class, double.class);
            case Type.FLOAT:
                return constructValueOf(Float.class, float.class);
            case Type.INT:
                return constructValueOf(Integer.class, int.class);
            case Type.LONG:
                return constructValueOf(Long.class, long.class);
            case Type.SHORT:
                return constructValueOf(Short.class, short.class);

            default:
                throw new DiSLFatalException(
                        "Impossible to box type: " + valueType.getDescriptor());
        }
    }

    // Get the first valid mark of a method.
    // For a constructor, the return value will be the instruction after
    // the object initialization.
    public static AbstractInsnNode findFirstValidMark(MethodNode method) {

        AbstractInsnNode first = method.instructions.getFirst();

        // This is not a constructor. Just return the first instruction
        if (!method.name.equals(Constants.CONSTRUCTOR_NAME)) {
            return first;
        }

        // AdviceAdapter will help us with identifying the proper place where
        // the constructor to super is called

        // just need an object that will hold a value
        // - we need access to the changeable boolean via reference
        class DataHolder {
            boolean trigger = false;
        }
        final DataHolder dh = new DataHolder();

        MethodVisitor emptyVisitor = new MethodVisitor(Opcodes.ASM4) {
        };
        AdviceAdapter adapter = new AdviceAdapter(
                Opcodes.ASM4, emptyVisitor,
                method.access, method.name, method.desc
                ) {
                    public void onMethodEnter() {
                        dh.trigger = true;
                    }
                };

        // Iterate instruction list till the instruction right after the
        // object initialization
        adapter.visitCode();

        for (AbstractInsnNode iterator : allInsnsFrom(method.instructions)) {

            iterator.accept(adapter);

            // first instruction will be instruction after constructor call
            if (dh.trigger) {
                first = iterator.getNext();
                break;
            }
        }

        return first;
    }

    //

    /**
     * Returns an {@link Iterable} for a list of instructions. The
     * {@link InsnList} in ASM unfortunately does not implement the
     * {@link Iterable} interface, even though it provides an iterator.
     * 
     * @param list
     *            the instruction list to create the iterable for.
     * @return
     *         An {@link Iterable} instance for the given instruction list.
     */
    public static final Iterable<AbstractInsnNode> allInsnsFrom(final InsnList list) {
        return new Iterable<AbstractInsnNode>() {
            @Override
            public Iterator<AbstractInsnNode> iterator() {
                return list.iterator();
            }
        };
    }

}
