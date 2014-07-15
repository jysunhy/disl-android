package ch.usi.dag.disl.classparser;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.SourceValue;

import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.exception.ParserException;
import ch.usi.dag.disl.localvar.LocalVars;
import ch.usi.dag.disl.localvar.SyntheticLocalVar;
import ch.usi.dag.disl.localvar.ThreadLocalVar;
import ch.usi.dag.disl.util.AsmHelper;
import ch.usi.dag.disl.util.AsmHelper.Insns;
import ch.usi.dag.disl.util.Constants;
import ch.usi.dag.disl.util.FrameHelper;
import ch.usi.dag.disl.util.Insn;

/**
 * Parses DiSL class with local variables
 */
abstract class AbstractParser {

    protected LocalVars allLocalVars = new LocalVars();

    public LocalVars getAllLocalVars() {
        return allLocalVars;
    }


    // ****************************************
    // Local Variables Parsing and Processing
    // ****************************************

    // returns local vars defined in this class
    protected void processLocalVars (final ClassNode dislClass) throws ParserException {

        // parse local variables
        LocalVars localVars = parseLocalVars(dislClass.name, dislClass.fields);

        // add local vars from this class to all local vars from all classes
        allLocalVars.putAll(localVars);

        // get static initialization code
        MethodNode cinit = null;
        for (MethodNode method : dislClass.methods) {

            // get the code
            if (method.name.equals(Constants.STATIC_INIT_NAME)) {
                cinit = method;
                break;
            }
        }

        // parse init code for local vars and assigns them accordingly
        if (cinit != null && cinit.instructions != null) {
            parseInitCodeForSLV(cinit.instructions, localVars.getSyntheticLocals());
            parseInitCodeForTLV(dislClass.name, cinit, localVars.getThreadLocals());
        }
    }


    private LocalVars parseLocalVars(String className, List<FieldNode> fields)
            throws ParserException {

        // NOTE: if two synthetic local vars with the same name are defined
        // in different files they will be prefixed with class name as it is
        // also in byte code

        LocalVars result = new LocalVars();

        for (FieldNode field : fields) {

            if (field.invisibleAnnotations == null) {
                throw new ParserException("DiSL annotation for field "
                        + className + "." + field.name + " is missing");
            }

            if (field.invisibleAnnotations.size() > 1) {
                throw new ParserException("Field " + className + "."
                        + field.name + " may have only one anotation");
            }

            AnnotationNode annotation =
                field.invisibleAnnotations.get(0);

            Type annotationType = Type.getType(annotation.desc);

            // thread local
            if (annotationType.equals(Type.getType(
                    ch.usi.dag.disl.annotation.ThreadLocal.class))) {

                ThreadLocalVar tlv = parseThreadLocal(className, field,
                        annotation);

                result.getThreadLocals().put(tlv.getID(), tlv);

                continue;
            }

            // synthetic local
            if (annotationType.equals(Type.getType(SyntheticLocal.class))) {
                SyntheticLocalVar slv = parseSyntheticLocal(className, field, annotation);
                result.getSyntheticLocals().put(slv.getID(), slv);
                continue;
            }

            throw new ParserException("Field " + className + "."
                    + field.name + " has unsupported DiSL annotation");
        }

        return result;
    }

    private static class TLAnnotationData {

        public boolean inheritable = false; // default
    }

    private ThreadLocalVar parseThreadLocal(String className, FieldNode field,
            AnnotationNode annotation) throws ParserException {

        // check if field is static
        if ((field.access & Opcodes.ACC_STATIC) == 0) {
            throw new ParserException("Field " + className + "." + field.name
                    + " declared as ThreadLocal but is not static");
        }

        // parse annotation
        TLAnnotationData tlad = new TLAnnotationData();
        ParserHelper.parseAnnotation(tlad, annotation);

        Type fieldType = Type.getType(field.desc);

        // default value will be set later on
        return new ThreadLocalVar(className, field.name, fieldType,
                tlad.inheritable);
    }

    private static class SLAnnotationData {

        // see code below for default
        public String[] initialize = null;
    }

    private SyntheticLocalVar parseSyntheticLocal(String className,
            FieldNode field, AnnotationNode annotation)
            throws ParserException {

        // check if field is static
        if ((field.access & Opcodes.ACC_STATIC) == 0) {
            throw new ParserException("Field " + field.name + className
                    + "." + " declared as SyntheticLocal but is not static");
        }

        // parse annotation data
        SLAnnotationData slad = new SLAnnotationData();
        ParserHelper.parseAnnotation(slad, annotation);

        // default val for init
        SyntheticLocal.Initialize slvInit = SyntheticLocal.Initialize.ALWAYS;

        if(slad.initialize != null) {

            // enum is converted to array
            //  - first value is class name
            //  - second value is value name
            slvInit = SyntheticLocal.Initialize.valueOf(slad.initialize[1]);
        }

        // field type
        Type fieldType = Type.getType(field.desc);

        return new SyntheticLocalVar(className, field.name, fieldType, slvInit);
    }


    //
    // Parses the initialization code for synthetic local variables. Such code
    // can only contain assignment from constants of basic types, or a single
    // method call.
    //
    private void parseInitCodeForSLV (
        InsnList initInsns, final Map <String, SyntheticLocalVar> slvs
    ) {
        //
        // Mark the first instruction of a block of initialization code and scan
        // the code. Ignore any instructions that do not access fields and stop
        // scanning when encountering any RETURN instruction.
        //
        // When encountering a field access instruction for a synthetic local
        // variable field, copy the code starting at the instruction marked as
        // first and ending with the field access instruction. Then mark the
        // instruction following the field access as the first instruction of
        // the next initialization block.
        //
        AbstractInsnNode firstInitInsn = initInsns.getFirst ();
        for (final AbstractInsnNode insn : Insns.selectAll (initInsns)) {
            if (AsmHelper.isReturn (insn.getOpcode ())) {
                break;
            }

            //
            // Only consider instructions access fields. This will leave us only
            // with GETFIELD, PUTFIELD, GETSTATIC, and PUTSTATIC instructions.
            //
            // RFC LB: Could we only consider PUTSTATIC instructions?
            //
            if (insn instanceof FieldInsnNode) {
                final FieldInsnNode lastInitInsn = (FieldInsnNode) insn;

                //
                // Skip accesses to fields that are not synthetic locals.
                //
                final SyntheticLocalVar slv = slvs.get (SyntheticLocalVar.fqFieldNameFor (
                    lastInitInsn.owner, lastInitInsn.name
                ));
                if (slv == null) {
                    // RFC LB: Advance firstInitInsn here as well?
                    continue;
                }

                //
                // Clone the initialization code between the current first
                // initialization instruction and this field access instruction,
                // which marks the end of the initialization code.
                //
                if (slv.hasInitCode ()) {
                    System.out.printf (
                        "DiSL: warning, replacing initialization code "+
                        "for synthetic local variable %s\n", slv.getID ()
                    );
                }
                slv.setInitCode (simpleInsnListClone (
                    firstInitInsn, lastInitInsn
                ));

                firstInitInsn = insn.getNext ();
            }
        }
    }


    private InsnList simpleInsnListClone (
        final AbstractInsnNode first, final AbstractInsnNode last
    ) {
        //
        // Clone the instructions from "first" to "last", inclusive.
        // Therefore at least one instruction will be always copied.
        //
        final InsnList result = new InsnList ();

        final AbstractInsnNode end = last.getNext ();
        final Map <LabelNode, LabelNode> dummy = Collections.emptyMap ();

        for (AbstractInsnNode insn = first; insn != end; insn = insn.getNext ()) {
            //
            // Clone only real instructions, we should not need labels.
            //
            if (! Insn.isVirtual (insn)) {
                result.add (insn.clone (dummy));
            }
        }

        return result;
    }


    private void parseInitCodeForTLV (
        final String className, final MethodNode cinitMethod,
        final Map <String, ThreadLocalVar> tlvs
    ) throws ParserException {
        Frame <SourceValue> [] frames =
            FrameHelper.getSourceFrames (className, cinitMethod);

        // analyze instructions in each frame
        // one frame should cover one field initialization
        for (int i = 0; i < frames.length; i++) {
            AbstractInsnNode instr = cinitMethod.instructions.get (i);

            // if the last instruction puts some value into the field...
            if (instr.getOpcode() != Opcodes.PUTSTATIC) {
                continue;
            }

            final FieldInsnNode fieldInsn = (FieldInsnNode) instr;

            //
            // Skip accesses to fields that are not thread locals.
            //
            final ThreadLocalVar tlv = tlvs.get (ThreadLocalVar.fqFieldNameFor (
                className, fieldInsn.name
            ));
            if (tlv == null) {
                continue;
            }

            // get the instruction that put the field value on the stack
            Set <AbstractInsnNode> sources =
                frames [i].getStack (frames [i].getStackSize () - 1).insns;

            if (sources.size () != 1) {
                throw new ParserException(String.format (
                    "Thread local variable %s can be only initialized "+
                    "by a single constant", tlv.getName()
                ));
            }

            AbstractInsnNode source = sources.iterator().next();

            // analyze oppcode and set the proper default value
            switch (source.getOpcode()) {
            // not supported
            // case Opcodes.ACONST_NULL:
            // var.setDefaultValue(null);
            // break;

            case Opcodes.ICONST_M1:
                tlv.setDefaultValue(-1);
                break;

            case Opcodes.ICONST_0:

                if (fieldInsn.desc.equals("Z")) {
                    tlv.setDefaultValue(false);
                } else {
                    tlv.setDefaultValue(0);
                }

                break;

            case Opcodes.LCONST_0:
                tlv.setDefaultValue(0);
                break;

            case Opcodes.FCONST_0:
            case Opcodes.DCONST_0:
                tlv.setDefaultValue(0.0);
                break;

            case Opcodes.ICONST_1:

                if (fieldInsn.desc.equals("Z")) {
                    tlv.setDefaultValue(true);
                } else {
                    tlv.setDefaultValue(1);
                }

                break;
            case Opcodes.LCONST_1:
                tlv.setDefaultValue(1);
                break;

            case Opcodes.FCONST_1:
            case Opcodes.DCONST_1:
                tlv.setDefaultValue(1.0);
                break;

            case Opcodes.ICONST_2:
            case Opcodes.FCONST_2:
                tlv.setDefaultValue(2);
                break;

            case Opcodes.ICONST_3:
                tlv.setDefaultValue(3);
                break;

            case Opcodes.ICONST_4:
                tlv.setDefaultValue(4);
                break;

            case Opcodes.ICONST_5:
                tlv.setDefaultValue(5);
                break;

            case Opcodes.BIPUSH:
            case Opcodes.SIPUSH:
                tlv.setDefaultValue(((IntInsnNode) source).operand);
                break;

            case Opcodes.LDC:
                tlv.setDefaultValue(((LdcInsnNode) source).cst);
                break;

            default:
                throw new ParserException("Initialization is not"
                        + " defined for thread local variable " + tlv.getName());
            }
        }
    }

}
