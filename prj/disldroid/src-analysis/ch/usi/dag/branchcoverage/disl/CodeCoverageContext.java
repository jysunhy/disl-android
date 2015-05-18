package ch.usi.dag.branchcoverage.disl;

import java.util.LinkedList;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import ch.usi.dag.branchcoverage.util.CodeCoverageUtil;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;


public class CodeCoverageContext extends MethodStaticContext {

    public int getIndex () {
        final AbstractInsnNode current = staticContextData.getRegionStart ();
        int index = 0;

        if (CodeCoverageUtil.isCondBranch (current)) {
            for (final AbstractInsnNode instr : staticContextData.getMethodNode ().instructions.toArray ()) {
                if (instr.equals (current)) {
                    return index;
                } else {
                    index += CodeCoverageUtil.getBranchCount (instr);
                }
            }
        }

        if (!(current instanceof LabelNode)) {
            return -1;
        }

        for (final AbstractInsnNode instr : staticContextData.getMethodNode ().instructions.toArray ()) {
            if (CodeCoverageUtil.isCondBranch (instr)
                && current.equals (((JumpInsnNode) instr).label)) {
                return index + 1;
            } else if (instr instanceof TableSwitchInsnNode) {
                final TableSwitchInsnNode tsin = (TableSwitchInsnNode) instr;
                final LinkedList <LabelNode> list = new LinkedList <LabelNode> ();
                list.add (tsin.dflt);

                for (final LabelNode label : tsin.labels) {
                    if (!list.contains (label)) {
                        list.add (label);
                    }
                }

                final int lIndex = list.indexOf (current);

                if (lIndex != -1) {
                    return index + lIndex;
                }
            } else if (instr instanceof LookupSwitchInsnNode) {
                final LookupSwitchInsnNode lsin = (LookupSwitchInsnNode) instr;
                final LinkedList <LabelNode> list = new LinkedList <LabelNode> ();
                list.add (lsin.dflt);

                for (final LabelNode label : lsin.labels) {
                    if (!list.contains (label)) {
                        list.add (label);
                    }
                }

                final int lIndex = list.indexOf (current);

                if (lIndex != -1) {
                    return index + lIndex;
                }
            }

            index += CodeCoverageUtil.getBranchCount (instr);
        }

        return -1;
    }
}
