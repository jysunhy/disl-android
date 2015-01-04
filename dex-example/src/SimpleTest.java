import java.io.*;
import java.util.*;

import org.ow2.asmdex.*;
import org.ow2.asmdex.tree.*;
import org.ow2.asmdex.specificAnnotationVisitors.*;

public class SimpleTest {

    public static byte [] readbytes (final File file) {
        final byte [] res = null;
        FileInputStream fis = null;
        DataInputStream dis = null;
        ByteArrayOutputStream bout = null;
        try {
            fis = new FileInputStream (file);
            dis = new DataInputStream (fis);
            bout = new ByteArrayOutputStream ();
            int temp;
            int size = 0;
            final byte [] b = new byte [2048];
            while ((temp = dis.read (b)) != -1) {
                bout.write (b, 0, temp);
                size += temp;
            }
            fis.close ();
            dis.close ();
        } catch (final Exception e) {
            e.printStackTrace ();
        }

        return bout.toByteArray ();
    }
    public static void main(String args[]) {

        /*
           FileOutputStream os = null;
           int api = Opcodes.ASM4;
           File inFile = new File("input.dex");
           File outFile = new File("output.dex");
        //... // Argument validation
        //AnnotRulesManager rm = ...; // Rules to apply
        ApplicationReader ar = new ApplicationReader(api, inFile);
        ApplicationWriter aw = new ApplicationWriter();
        //ApplicationVisitor aa = new ApplicationAdapterAnnotateCalls(api, rm, aw);
        //ar.accept(aa, 0);
        byte [] b = aw.toByteArray();
        os = new FileOutputStream(outFile);
        os.write(b);

*/
        try{
            File inFile = new File(args[0]);
            File outFile = new File(args[0]+".asmdex");
            int api = Opcodes.ASM4;
            byte[] inputbytes= readbytes(inFile);
            long startTime = System.currentTimeMillis();
            //ApplicationReader ar = new ApplicationReader(api,inputbytes);
            ApplicationReader ar = new ApplicationReader(api,inFile);
            //ApplicationReader ar2 = new ApplicationReader(api,new byte[1024]);
            //System.out.println("ar ready ");
            ApplicationNode appnode = new ApplicationNode(Opcodes.ASM4);
            ar.accept(appnode, 0);
            long midTime = System.currentTimeMillis();
            /*
               List<ClassNode> classes = appnode.classes;

            //                System.err.println("=== Instrumenting class " + clazz.name + " ===");
            System.out.println("classes: " + classes.size());

            for( ClassNode clazz : classes) {	
            List<MethodNode> methods = clazz.methods;
            System.out.println("in class " + methods.size());
            for (MethodNode method : methods) {
            if ((method.access & Opcodes.ACC_NATIVE) != 0)
            continue;
            if ((method.access & Opcodes.ACC_ABSTRACT) != 0)
            continue;

            InsnList instructions = method.instructions;

            instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC,
            "ch/usi/dag/experiment/runtime/MyAnalysis",
            "onMethodEntry", "(Ljava/lang/String;)V"));
            instructions.insert(new LdcInsnNode(""));
            Iterator<AbstractInsnNode> i = instructions.iterator();
            while(i.hasNext()){
            AbstractInsnNode curInsn = i.next();
            if(curInsn instanceof LineNumberNode){
            System.out.println(((LineNumberNode)curInsn).line);
            }else
            {
            System.out.println("opcode:"+curInsn.getType());
            }
            }

            }
            }
            */

            ApplicationWriter aw = new ApplicationWriter();
            appnode.accept(aw);
            //           System.out.println("aw ready ");

            FileOutputStream os = null;
            byte [] b = aw.toByteArray();
            long endTime = System.currentTimeMillis();
            os = new FileOutputStream(outFile);
            os.write(b);
            System.out.printf("%s %s %s %s %s\n",inputbytes.length, b.length, midTime-startTime, endTime-midTime, endTime-startTime);
        }catch(Exception e){
            e.printStackTrace();
        }
        return;

    }
}

