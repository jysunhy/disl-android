package ch.usi.dag.disl.utilinstr.codemerger;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.CodeSizeEvaluator;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

import ch.usi.dag.disl.dynamicbypass.DynamicBypassCheck;
import ch.usi.dag.disl.exception.DiSLFatalException;
import ch.usi.dag.disl.util.AsmHelper.ClonedCode;

public abstract class CodeMerger {

	private static final String DBCHECK_CLASS = Type.getInternalName(
			DynamicBypassCheck.class);
	private static final String DBCHECK_METHOD = "executeUninstrumented";
	private static final String DBCHECK_DESC = "()Z";
	
	private static final int ALLOWED_SIZE = 64 * 1024; // 64KB limit

	// NOTE: the originalCN and instrumentedCN will be destroyed in the process
	// NOTE: abstract or native methods should not be included in the
	//       changedMethods list
	public static ClassNode mergeClasses(ClassNode originalCN,
			ClassNode instrumentedCN, Set<String> changedMethods,
			boolean splitLongMethods) {

		// NOTE: that instrumentedCN can contain added fields
		//       - has to be returned
		
		if(changedMethods == null) {
			throw new DiSLFatalException(
					"Set of changed methods cannot be null");
		}

		// no changed method - no merging
		if (changedMethods.isEmpty()) {
			return instrumentedCN;
		}

		// merge methods one by one
		for (MethodNode instrMN : instrumentedCN.methods) {
			
			// We will construct the merged method node in the instrumented
			// class node
			
			// skip unchanged methods
			if(! changedMethods.contains(instrMN.name + instrMN.desc)) {
				continue;
			}

			MethodNode origMN = getMethodNode(originalCN, instrMN.name,
					instrMN.desc);

			InsnList ilist = instrMN.instructions;
			List<TryCatchBlockNode> tcblist = instrMN.tryCatchBlocks;
			
			// crate copy of the original instruction list
			// this copy will be destroyed during merging
			// we need the original code if the method is too long
			ClonedCode origCodeCopy = ClonedCode.create (
				origMN.instructions, origMN.tryCatchBlocks
			);
			
			// create copy of the lists for splitting
			ClonedCode splitCopy = null;
			if (splitLongMethods) {
				splitCopy = ClonedCode.create (ilist, tcblist);
			}

			// add reference to the original code
			LabelNode origCodeL = new LabelNode();
			ilist.add(origCodeL);

			// add original code
			ilist.add(origCodeCopy.getInstructions());
			// add exception handlers of the original code
			tcblist.addAll(origCodeCopy.getTryCatchBlocks());

			// if the dynamic bypass is activated (non-zero value returned)
			// then jump to original code
			ilist.insert(new JumpInsnNode(Opcodes.IFNE, origCodeL));
			ilist.insert(new MethodInsnNode(Opcodes.INVOKESTATIC,
					DBCHECK_CLASS, DBCHECK_METHOD, DBCHECK_DESC));
			
			// calculate the code size and if it is larger then allowed size,
			// skip it
			CodeSizeEvaluator cse = new CodeSizeEvaluator(null);
			instrMN.accept(cse);
			
			if (cse.getMaxSize() > ALLOWED_SIZE) {
				
				if(splitLongMethods) {
					
					// return originally instrumented code back to the instrMN
					instrMN.instructions = splitCopy.getInstructions();
					instrMN.tryCatchBlocks = splitCopy.getTryCatchBlocks();
					
					// split methods
					splitLongMethods(instrumentedCN, origMN, instrMN);
					
					// next method
					continue;
				}
				
				// insert original code into the instrumented method node
				instrMN.instructions = origMN.instructions;
				instrMN.tryCatchBlocks = origMN.tryCatchBlocks;
				
				// print error msg
				System.err.println("WARNING: code of the method "
						+ instrumentedCN.name + "." + instrMN.name
						+ " is larger ("
						+ cse.getMaxSize()
						+ ") then allowed size (" +
						+ ALLOWED_SIZE
						+ ") - skipping");
			}
		}
		
		return instrumentedCN;
	}

	private static void splitLongMethods(ClassNode instrumentedCN,
			MethodNode origMN, MethodNode instrMN) {
		
		// TODO jb ! add splitting for to long methods
		//  - ignore clinit - output warning
		//  - output warning if splitted is to large and ignore
		
		// check the code size of the instrumented method
		// add if to the original method that jumps to the renamed instrumented method
		// add original method to the instrumented code
		// rename instrumented method
	}

	private static MethodNode getMethodNode(ClassNode cnToSearch,
			String methodName, String methodDesc) {

		for (MethodNode mn : cnToSearch.methods) {
			if (methodName.equals(mn.name) && methodDesc.equals(mn.desc)) {
				return mn;
			}
		}

		throw new RuntimeException(
				"Code merger fatal error: method for merge not found");
	}
}
