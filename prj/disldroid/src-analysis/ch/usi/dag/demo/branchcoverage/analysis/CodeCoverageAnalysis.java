package ch.usi.dag.demo.branchcoverage.analysis;

import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.demo.branchcoverage.util.CodeCoverageUtil;
import ch.usi.dag.demo.logging.WebLogger;
import ch.usi.dag.disl.util.Constants;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;


public class CodeCoverageAnalysis extends RemoteAnalysis  {
    static class MethodCoverage {
        MethodCoverage(final int branchNumber, final int bbNumber){
            branches = new int[branchNumber];
            basicBlocks = new int[bbNumber];
        }
        int[] branches;
        int[] basicBlocks;

    }
    @SuppressWarnings ("serial")
    static class ClassProfile extends ConcurrentHashMap <String, MethodCoverage> {
        public ClassProfile (final ClassNode classNode) {
            for (final MethodNode methodNode : classNode.methods) {
                final String key = methodNode.name
                    + Constants.STATIC_CONTEXT_METHOD_DELIM + methodNode.desc;
                int counter = 0;
                for (final AbstractInsnNode instr : methodNode.instructions.toArray ()) {
                    counter += CodeCoverageUtil.getBranchCount (instr);
                }
                put (key, new MethodCoverage (counter, 0));
            }
        }
    }

    @SuppressWarnings ("serial")
    static class ProcessProfile extends ConcurrentHashMap <String, ClassProfile> {
    }

    //public void branchTaken (final ShadowString classSignature,
    //final ShadowString methodSignature, final int index,
    public void branchTaken (final String classSignature,
        final String methodSignature, final int index,
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

        if(classProfile.get (innerKey) == null){
            //System.out.println ("array null at "+outerKey+" "+innerKey);
            return;
        }
        int times = 0;
        try {
            times = ++classProfile.get (innerKey).branches [index];
        }catch (final Exception e){
            e.printStackTrace ();
            System.err.println("Error details: "+outerKey+"."+innerKey+" "+classProfile.get (innerKey).branches.length);
        }

        WebLogger.branchTaken (context.getProcessID (), context.getPname (),
            classSignature.toString (), methodSignature.toString (), index,
            classProfile.get (innerKey).branches.length, times);
    }

    public void bbTaken (final String classSignature,
        final String methodSignature, final int index,
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

        if(classProfile.get (innerKey) == null){
            //System.out.println ("array null at "+outerKey+" "+innerKey);
            return;
        }
        int times = 0;
        try {
            times = ++classProfile.get (innerKey).basicBlocks [index];
        }catch (final Exception e){
            e.printStackTrace ();
            System.err.println("Error details: "+outerKey+"."+innerKey+" "+classProfile.get (innerKey).basicBlocks.length);
        }

        WebLogger.branchTaken (context.getProcessID (), context.getPname (),
            classSignature.toString (), methodSignature.toString (), index,
            classProfile.get (innerKey).basicBlocks.length, times);
    }

    public static boolean printResult(final Context context){
        final ProcessProfile processProfile = context.getState ("bc", ProcessProfile.class);
        if(processProfile == null || context.isDead ()) {
            return false;
        }
        for (final String classSignature : processProfile.keySet ()) {
            final ClassProfile classProfile = processProfile.get (classSignature);
            for (final String methodSignature : classProfile.keySet ()) {
                final MethodCoverage coverage = classProfile.get (methodSignature);
                final int total = coverage.branches.length;
                int covered = 0;
                for (final int count : coverage.branches) {
                    if (count > 0) {
                        covered++;
                    }
                }
                if(covered!=0) {
                    WebLogger.reportBranchCoverage (context.getProcessID (), context.getPname (),
                        classSignature, methodSignature, covered, total);
                }
            }
        }
        return true;
    }

    @Override
    public void atExit (final Context context) {
    }


    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {
    }
    static class OutputThread extends Thread {
        @Override
        public void run(){
            while(true) {
                try {
                    for(int i =0; i < ShadowAddressSpace.getContexts ().size ();i++)
                    {
                        CodeCoverageAnalysis.printResult (ShadowAddressSpace.getContexts ().get (i));
                    }
                    Thread.sleep(5000);
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
}

