package ch.usi.dag.branchcoverage.analysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.branchcoverage.util.CodeCoverageLogger;
import ch.usi.dag.branchcoverage.util.CodeCoverageUtil;
import ch.usi.dag.disldroidreserver.remoteanalysis.VMExitListener;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;


public class CodeCoverageAnalysis implements VMExitListener {

    String getClassName(final String methodId){
        return methodId.substring (0, methodId.indexOf (';'));
    }

    public void branchTaken (final Context context, final ShadowString classSignature, final ShadowString methodSignature, final int index) {
        final ClassNode clazz = context.getClassNodeFor(classSignature.toString ());

        HashMap <String, HashMap<String, int[]>> store = (HashMap <String, HashMap<String, int[]>>) context.getStore ();
        if(store==null){
            store = new HashMap <String, HashMap<String, int[]>>();
            context.setStore (store);
        }
        HashMap<String, int[]> branchMap = null;
        if(!store.containsKey (classSignature.toString ())){
            branchMap= new HashMap<String, int[]>();
            store.put (classSignature.toString (), branchMap);
            for (final MethodNode mnode : clazz.methods){
                branchMap.put (CodeCoverageUtil.getMethodSignature (mnode), new int[CodeCoverageUtil.getBranchCount (mnode)]);
            }
        } else {
            branchMap= store.get (classSignature.toString ());
        }
        branchMap.get (methodSignature.toString ())[index]++;
    }

    public void printResult (final Context context) {
        final HashMap <String, HashMap<String, int[]>> store = (HashMap <String, HashMap<String, int[]>>) context.getStore ();
        final Set <Entry <String, HashMap <String, int []>>> entries = store.entrySet ();
        final Iterator <Entry <String, HashMap <String, int []>>> clazzIter = entries.iterator ();
        while(clazzIter.hasNext ()){
            final Entry <String, HashMap <String, int []>> cur = clazzIter.next ();
            final HashMap <String, int []> methds = cur.getValue ();
            final Iterator <Entry <String, int []>> methdIter = methds.entrySet ().iterator ();
            while(methdIter.hasNext ()){
                final Entry <String, int []> item = methdIter.next ();
                int cnt=0;
                final int sum=item.getValue ().length;
                for(int i = 0; i < sum; i++){
                    if(item.getValue ()[i]!=0) {
                        cnt++;
                    }
                }
                CodeCoverageLogger.printCoverage (cur.getKey (), item.getKey (), cnt, sum);
            }
        }
    }

    @Override
    public void onVMExit (final Context context) {
        printResult (context);
    }
}
