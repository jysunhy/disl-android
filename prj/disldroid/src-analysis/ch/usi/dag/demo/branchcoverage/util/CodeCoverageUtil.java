package ch.usi.dag.demo.branchcoverage.util;

import java.util.HashSet;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;


public class CodeCoverageUtil {

    public static boolean isCondBranch (final AbstractInsnNode instr) {
        switch (instr.getOpcode ()) {
        case Opcodes.IFEQ:
        case Opcodes.IFNE:
        case Opcodes.IFLT:
        case Opcodes.IFGE:
        case Opcodes.IFGT:
        case Opcodes.IFLE:
        case Opcodes.IF_ICMPEQ:
        case Opcodes.IF_ICMPNE:
        case Opcodes.IF_ICMPLT:
        case Opcodes.IF_ICMPGE:
        case Opcodes.IF_ICMPGT:
        case Opcodes.IF_ICMPLE:
        case Opcodes.IF_ACMPEQ:
        case Opcodes.IF_ACMPNE:
        case Opcodes.IFNULL:
        case Opcodes.IFNONNULL:
            return true;

        default:
            return false;
        }
    }


    public static int getBranchCount (final AbstractInsnNode instr) {
        if (isCondBranch (instr)) {
            return 2;
        } else if (instr instanceof LookupSwitchInsnNode) {
            final LookupSwitchInsnNode lsin = (LookupSwitchInsnNode) instr;
            final HashSet <LabelNode> labels = new HashSet <LabelNode> ();
            labels.add (lsin.dflt);
            labels.addAll (lsin.labels);
            return labels.size ();
        } else if (instr instanceof TableSwitchInsnNode) {
            final TableSwitchInsnNode tsin = (TableSwitchInsnNode) instr;
            final HashSet <LabelNode> labels = new HashSet <LabelNode> ();
            labels.add (tsin.dflt);
            labels.addAll (tsin.labels);
            return labels.size ();
        }

        return 0;
    }

}
