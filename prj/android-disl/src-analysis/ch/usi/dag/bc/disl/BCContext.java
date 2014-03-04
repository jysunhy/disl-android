package ch.usi.dag.bc.disl;

import java.util.LinkedList;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;


public class BCContext extends MethodStaticContext {

    public String thisMethodFullNameWithDesc () {
        return thisMethodFullName () + thisMethodDescriptor ();
    }


    public int getTotal () {
        int counter = 0;

        for (final MethodNode methodNode : staticContextData.getClassNode ().methods) {
            counter += BCUtil.getBranchCount (methodNode);
        }

        return counter;
    }


    public int getLocal () {
        return BCUtil.getBranchCount (staticContextData.getMethodNode ());
    }


    public int getIndex () {
        final AbstractInsnNode current = staticContextData.getRegionStart ();
        int index = 0;

        if (BCUtil.isCondBranch (current)) {

            for (final AbstractInsnNode instr : staticContextData.getMethodNode ().instructions.toArray ()) {
                if (instr.equals (current)) {
                    return index;
                } else {
                    index += BCUtil.getBranchCount (instr);
                }
            }
        }

        if (!(current instanceof LabelNode)) {
            return -1;
        }

        for (final AbstractInsnNode instr : staticContextData.getMethodNode ().instructions.toArray ()) {
            if (BCUtil.isCondBranch (instr)
                && current.equals (((JumpInsnNode) instr).label)) {
                return index + 1;
            } else if (instr instanceof TableSwitchInsnNode) {
                final TableSwitchInsnNode tsin = (TableSwitchInsnNode) instr;
                final LinkedList <LabelNode> list = new LinkedList <> ();
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
                final LinkedList <LabelNode> list = new LinkedList <> ();
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

            index += BCUtil.getBranchCount (instr);
        }

        return -1;
    }

}