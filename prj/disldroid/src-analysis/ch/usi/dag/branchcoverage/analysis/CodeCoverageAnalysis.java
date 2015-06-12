package ch.usi.dag.branchcoverage.analysis;

import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.branchcoverage.util.CodeCoverageUtil;
import ch.usi.dag.disl.util.Constants;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;


public class CodeCoverageAnalysis extends RemoteAnalysis  {

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

    }


    public void branchTaken (final ShadowString classSignature,
        final ShadowString methodSignature, final int index,
        final Context context) {
        ProcessProfile processProfile = context.getState (ProcessProfile.class);

        if (processProfile == null) {
            final ProcessProfile temp = new ProcessProfile ();
            processProfile = (ProcessProfile) context.setStateIfAbsent (temp);
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

        classProfile.get (innerKey) [index]++;
    }


    @Override
    public void atExit (final Context context) {
        // Dumping code coverage profile
        final ProcessProfile processProfile = context.getState (ProcessProfile.class);

        for (final String classSignature : processProfile.keySet ()) {
            final ClassProfile classProfile = processProfile.get (classSignature);
            System.out.println ("class: " + classSignature);

            for (final String methodSignature : classProfile.keySet ()) {
                final int [] coverage = classProfile.get (methodSignature);
                final int total = coverage.length;
                int covered = 0;

                for (final int count : coverage) {
                    if (count > 0) {
                        covered++;
                    }
                }

                System.out.println ("\t method: "
                    + methodSignature + " " + covered + " / " + total);
            }
        }
    }


    @Override
    public void objectFree (final Context context, final ShadowObject netRef) {
        // TODO Auto-generated method stub

    }
}
