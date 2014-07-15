package ch.usi.dag.bc.disl;

import java.util.HashMap;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.staticcontext.AbstractStaticContext;
import ch.usi.dag.disl.util.cfg.CtrlFlowGraph;


public class BBCContext extends AbstractStaticContext {

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
