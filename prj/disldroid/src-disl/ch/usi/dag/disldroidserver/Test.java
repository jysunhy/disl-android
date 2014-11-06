package ch.usi.dag.disldroidserver;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.DiSL;
import ch.usi.dag.disl.exception.DiSLException;


public class Test {
    static byte[] ba = null;

    public static ClassNode getDiSLClass(final ClassNode targetAPIClassNode){
        final ClassNode resultNode = new ClassNode();
        resultNode.version = Opcodes.V1_6;
        resultNode.access = Opcodes.ACC_PUBLIC;
        resultNode.name= "MonitorDiSLClass";
        resultNode.superName = "java/lang/Object";

        for(final MethodNode mn : targetAPIClassNode.methods){
            if((mn.access & Opcodes.ACC_STATIC) > 0){
                final MethodNode newMn = new MethodNode (Opcodes.ACC_STATIC+ Opcodes.ACC_PUBLIC, mn.name, mn.desc, null, null);
                newMn.instructions = mn.instructions;
                newMn.parameters = mn.parameters;
                newMn.localVariables = mn.localVariables;
                newMn.maxLocals=mn.maxLocals;
                newMn.maxStack=mn.maxStack;
                resultNode.methods.add (newMn);
            }else {
                final MethodNode newMn = new MethodNode (mn.access, mn.name, mn.desc, null, null);
                newMn.instructions = mn.instructions;
                newMn.parameters = mn.parameters;
                newMn.localVariables = mn.localVariables;
                newMn.maxLocals=mn.maxLocals;
                newMn.maxStack=mn.maxStack;
                resultNode.methods.add (newMn);
            }
        }
        return resultNode;
    }

    /**
     * @param args
     */
    public static void main (final String [] args) {

 /*       final ClassNode cn = new ClassNode ();
        ClassReader cr;
        try {
            cr = new ClassReader (new FileInputStream ("/home/haiyang/workspace/ASM/bin/test/TargetClass.class"));
            cr.accept (cn, 0);
            final ClassNode outputNode = Test.getDiSLClass (cn);
            final ClassWriter cw0 = new ClassWriter(0);
            outputNode.accept(cw0);
            final byte[] b = cw0.toByteArray();
            {
                final BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(outputNode.name+".class"));
                bos.write(b);
                bos.flush();
                bos.close();
            }

        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
*/

        // TODO Auto-generated method stub
        try {
            final ClassNode wrapperClassNode = new ClassNode ();
            wrapperClassNode.version = Opcodes.V1_6;
            wrapperClassNode.access = Opcodes.ACC_PUBLIC;
            wrapperClassNode.name= "test/APIWrapper";
            wrapperClassNode.superName = "java/lang/Object";

            final ClassNode cn = new ClassNode ();
            final ClassReader cr0 = new ClassReader (new FileInputStream ("output/build/analysis/ch/usi/dag/monitor/disl/DiSLClass.class"));
            cr0.accept (cn, 0);
            for(final MethodNode mn : cn.methods){
                if(mn.invisibleAnnotations!=null && mn.invisibleAnnotations.size ()>0){
                    final String annotationName = mn.invisibleAnnotations.get (0).desc;
                    if(annotationName.equals ("Lch/usi/dag/disl/annotation/Monitor;")){
                        wrapperClassNode.methods.add (mn);
                    }
                }
            }
            final ClassWriter cw0 = new ClassWriter(0);
            wrapperClassNode.accept(cw0);
            final byte[] b = cw0.toByteArray();
            {
                final BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream("APIWrapper.class"));
                bos.write(b);
                bos.flush();
                bos.close();
            }

            final DiSL disl = new DiSL (false, "output/build/analysis/ch/usi/dag/monitor/disl/DiSLClass.class","");
            //final ClassNode cn2 = new ClassNode();
            final ClassReader cr = new ClassReader (new FileInputStream ("/home/haiyang/workspace/ASM/bin/test/TargetClass.class"));
            //cr.accept (cn2, 0);
            final ClassWriter cw = new ClassWriter(0);
            cr.accept (cw, 0);
            ba= disl.instrument (cw.toByteArray ());
            {
            final BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream("TargetClass.class"));
            bos.write(ba);
            bos.flush();
            bos.close();
            }
        } catch (final DiSLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
