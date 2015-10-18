package ch.usi.dag.demo.branchcoverage.analysis;

import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.demo.branchcoverage.util.CodeCoverageUtil;
import ch.usi.dag.demo.logging.DemoLogger;
import ch.usi.dag.demo.logging.WebLogger;
import ch.usi.dag.disl.util.Constants;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;


public class CodeCoverageAnalysis extends RemoteAnalysis  {

    static String analysisTag = "BranchCoverage";
    static class OutputThread extends Thread {
        @Override
        public void run(){
            while(true) {
                try {
                    boolean clean = true;
                    for(int i =0; i < ShadowAddressSpace.getContexts ().size ();i++)
                    {
                        if(CodeCoverageAnalysis.printResult (ShadowAddressSpace.getContexts ().get (i), clean)){
                            clean = false;
                        }
                    }
                    Thread.sleep(10000);
                }catch(final Exception e)
                {
                    e.printStackTrace ();
                    break;
                }
            }
        }
      }

    static {
        final Thread thd = new OutputThread();
        thd.start ();
    }

    @SuppressWarnings ("serial")
    static class ClassProfile extends ConcurrentHashMap <String, int []> {

        public ClassProfile (final ClassNode classNode) {
            for (final MethodNode methodNode : classNode.methods) {
                final String key = methodNode.name
                    + Constants.STATIC_CONTEXT_METHOD_DELIM + methodNode.desc;
                int counter = 0;

                for (final AbstractInsnNode instr : methodNode.instructions.toArray ()) {
                    counter += CodeCoverageUtil.getBranchCount (instr);
                }

                put (key, new int [counter]);
            }
        }

    }


    @SuppressWarnings ("serial")
    static class ProcessProfile extends ConcurrentHashMap <String, ClassProfile> {
        private boolean changed = false;

        boolean isChanged () {
            return changed;
        }

        void setChanged (final boolean changed) {
            this.changed = changed;
        }
    }


    public void branchTaken (final ShadowString classSignature,
        final ShadowString methodSignature, final int index,
        final Context context) {
        ProcessProfile processProfile = context.getState ("bc", ProcessProfile.class);

        if (processProfile == null) {
            final ProcessProfile temp = new ProcessProfile ();
            processProfile = (ProcessProfile) context.setStateIfAbsent ("bc", temp);
            if(processProfile == null) {
                processProfile = temp;
            }
        }

        final String outerKey = classSignature.toString ();
        final String innerKey = methodSignature.toString ();
        ClassProfile classProfile = processProfile.get (outerKey);

        if (classProfile == null) {
            final ClassProfile temp = new ClassProfile (
                context.getClassNodeFor (outerKey));
            classProfile = processProfile.putIfAbsent (outerKey, temp);

            if (classProfile == null) {
                classProfile = temp;
            }
        }

        final int times = ++classProfile.get (innerKey) [index];

        DemoLogger.info (analysisTag, "branch taken at Method "+methodSignature+" of "+classSignature+", taken times: "+ times);
        WebLogger.branchTaken (context.getProcessID (), context.getPname (), classSignature.toString (), methodSignature.toString (), index, classProfile.get (innerKey).length, times);
        if(times == 1) {
            processProfile.setChanged (true);
        }
    }


    @Override
    public void atExit (final Context context) {
        // Dumping code coverage profile
        //printResult (context);
    }


    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {
        // TODO Auto-generated method stub

    }

    public static boolean printResult(final Context context, final boolean clean){
        // Dumping code coverage profile
        final ProcessProfile processProfile = context.getState ("bc", ProcessProfile.class);
        if(processProfile == null) {
            return false;
        }
//        if(!processProfile.isChanged ()) {
//            return false;
//        }
        DemoLogger.info (analysisTag, "**************************************************");
        for (final String classSignature : processProfile.keySet ()) {
            final ClassProfile classProfile = processProfile.get (classSignature);
            //System.out.println ("class: " + classSignature);

            for (final String methodSignature : classProfile.keySet ()) {
                final int [] coverage = classProfile.get (methodSignature);
                final int total = coverage.length;
                int covered = 0;

                for (final int count : coverage) {
                    if (count > 0) {
                        covered++;
                    }
                }

                if(covered!=0) {
                    DemoLogger.info (analysisTag,
                        context.getPname ()+" coverage report:"
                        +"class: " + classSignature
                        + "\t method: "+ methodSignature + " "
                        + covered + " / " + total +" = "+(total==0?"Nil":((double)covered)/total));
                    WebLogger.reportBranchCoverage (context.getProcessID (), context.getPname (), classSignature, methodSignature, covered, total, clean);
                }

//                System.out.println ("\t method: "
//                    + methodSignature + " " + covered + " / " + total +" = "+(total==0?"Nil":((double)covered)/total));
            }
        }
        processProfile.setChanged (false);
        DemoLogger.info (analysisTag, "**************************************************");
        return true;
    }
}
