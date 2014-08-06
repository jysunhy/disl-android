package ch.usi.dag.bc.disl;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;


public class BCContext extends MethodStaticContext {

    ConcurrentHashMap <String, Integer> methodTotalEdges = new ConcurrentHashMap <String, Integer>();

    public String thisMethodFullNameWithDesc () {
        return thisMethodFullName () + thisMethodDescriptor ();
    }


    public int getClassBranchCount () {
        Integer total;

        if ((total = methodTotalEdges.get (thisClassName ())) == null) {
            int method = 0;
            int edge = 0;

            for (final MethodNode methodNode : staticContextData.getClassNode ().methods) {
                edge += BCUtil.getBranchCount (methodNode);

                if ((methodNode.access & Opcodes.ACC_ABSTRACT) == 0
                    && (methodNode.access & Opcodes.ACC_NATIVE) == 0) {
                    method++;
                }
            }

            if ((total = methodTotalEdges.putIfAbsent (thisClassName (), edge)) == null) {
                System.out.printf ("BCC: %s %d %d %d\n", thisClassName (), edge, method, BCUtil.getBBCount (staticContextData.getClassNode ()));
                total = edge;
            }
        }

        return total;
    }


    public int getMethodBranchCount () {
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

            index += BCUtil.getBranchCount (instr);
        }

        return -1;
    }

}
