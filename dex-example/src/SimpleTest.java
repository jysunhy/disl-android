import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.ow2.asmdex.*;
import org.ow2.asmdex.tree.*;
import org.ow2.asmdex.specificAnnotationVisitors.*;

public class SimpleTest {
	
		public static void main(String args[]) {

		System.out.println("in");
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
		File inFile = new File("input.dex");
		File outFile = new File("output.dex");
		int api = Opcodes.ASM4;
                ApplicationReader ar = new ApplicationReader(api,inFile);
                ApplicationNode appnode = new ApplicationNode(Opcodes.ASM4);
                ar.accept(appnode, 0);
		List<ClassNode> classes = appnode.classes;

//                System.err.println("=== Instrumenting class " + clazz.name + " ===");

		for( ClassNode clazz : classes) {	
                List<MethodNode> methods = clazz.methods;
                for (MethodNode method : methods) {
                        if ((method.access & Opcodes.ACC_NATIVE) != 0)
                                continue;
                        if ((method.access & Opcodes.ACC_ABSTRACT) != 0)
                                continue;

//                        InsnList instructions = method.instructions;

//                        instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC,
//                                        "ch/usi/dag/experiment/runtime/MyAnalysis",
//                                        "onMethodEntry", "(Ljava/lang/String;)V"));
//                        instructions.insert(new LdcInsnNode(""));
//                        Iterator<AbstractInsnNode> i = instructions.iterator();
//                        while(i.hasNext()){
//                                AbstractInsnNode curInsn = i.next();
//                                if(curInsn instanceof LineNumberNode){
//                                        System.out.println(((LineNumberNode)curInsn).line);
//                                }else
//                                {
 //                                       System.out.println("opcode:"+curInsn.getType());
//                                }
//                        }

 	               }
		}
                ApplicationWriter aw = new ApplicationWriter();
                ar.accept(aw,0);

		FileOutputStream os = null;
		byte [] b = aw.toByteArray();
		os = new FileOutputStream(outFile);
		os.write(b);
}catch(Exception e){
	System.err.println("exception:"+e.toString());
}
		return;

        }
}

