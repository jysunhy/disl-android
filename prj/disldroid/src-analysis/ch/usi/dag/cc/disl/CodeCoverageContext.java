package ch.usi.dag.cc.disl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.disl.util.cfg.CtrlFlowGraph;


public class CodeCoverageContext extends MethodStaticContext {

    ConcurrentHashMap <String, Integer> methodTotalEdges = new ConcurrentHashMap <> ();


    public String thisMethodFullNameWithDesc () {
        return thisMethodFullName () + thisMethodDescriptor ();
    }


    public int getClassBranchCount () {
        Integer total;

        if ((total = methodTotalEdges.get (thisClassName ())) == null) {
            int method = 0;
            int edge = 0;

            for (final MethodNode methodNode : staticContextData.getClassNode ().methods) {
                edge += CodeCoverageUtil.getBranchCount (methodNode);

                if ((methodNode.access & Opcodes.ACC_ABSTRACT) == 0
                    && (methodNode.access & Opcodes.ACC_NATIVE) == 0) {
                    method++;
                }
            }

            if ((total = methodTotalEdges.putIfAbsent (thisClassName (), edge)) == null) {
                System.out.printf ("DiSL-META: %s %d %d\n", thisClassName (),
                    edge, method);
                total = edge;
            }
        }

        return total;
    }


    public int getMethodBranchCount () {
        return CodeCoverageUtil.getBranchCount (staticContextData.getMethodNode ());
    }


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

            index += CodeCoverageUtil.getBranchCount (instr);
        }

        return -1;
    }


    // Context for basic blocks
    private String genKey (final ClassNode classNode, final MethodNode methodNode) {
        return classNode.name + methodNode.name + methodNode.desc;
    }


    protected HashMap <String, CtrlFlowGraph> cache = new HashMap <String, CtrlFlowGraph> ();


    private CtrlFlowGraph getCFG (
        final ClassNode classNode, final MethodNode methodNode) {
        final String key = genKey (classNode, methodNode);

        CtrlFlowGraph res = cache.get (key);

        if (res == null) {
            res = new CtrlFlowGraph (methodNode);
            cache.put (key, res);
        }

        return res;
    }


    public int getClassBBCount () {
        int total = 0;
        final ClassNode classNode = staticContextData.getClassNode ();

        for (final MethodNode methodNode : classNode.methods) {
            total += getCFG (classNode, methodNode).getNodes ().size ();
        }
        return total;
    }


    public int getMethodBBCount () {
        final ClassNode classNode = staticContextData.getClassNode ();
        final MethodNode methodNode = staticContextData.getMethodNode ();
        return getCFG (classNode, methodNode).getNodes ().size ();
    }


    public int getMethodBBindex () {
        final ClassNode classNode = staticContextData.getClassNode ();
        final MethodNode methodNode = staticContextData.getMethodNode ();
        return getCFG (classNode, methodNode).getIndex (
            staticContextData.getRegionStart ());
    }

}
