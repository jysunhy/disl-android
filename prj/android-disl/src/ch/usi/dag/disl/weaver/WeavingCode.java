package ch.usi.dag.disl.weaver;

import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.SourceValue;

import ch.usi.dag.disl.classcontext.ClassContext;
import ch.usi.dag.disl.coderep.Code;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.exception.DiSLFatalException;
import ch.usi.dag.disl.exception.DynamicContextException;
import ch.usi.dag.disl.processor.generator.PIResolver;
import ch.usi.dag.disl.processor.generator.ProcInstance;
import ch.usi.dag.disl.processor.generator.ProcMethodInstance;
import ch.usi.dag.disl.processorcontext.ArgumentContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.disl.snippet.Shadow;
import ch.usi.dag.disl.snippet.Snippet;
import ch.usi.dag.disl.snippet.SnippetCode;
import ch.usi.dag.disl.staticcontext.generator.SCGenerator;
import ch.usi.dag.disl.util.AsmHelper;
import ch.usi.dag.disl.util.FrameHelper;
import ch.usi.dag.disl.weaver.pe.MaxCalculator;
import ch.usi.dag.disl.weaver.pe.PartialEvaluator;

public class WeavingCode {

	final static String PROP_PE = "disl.parteval";

	private WeavingInfo info;
	private MethodNode method;
	private SnippetCode code;
	private InsnList iList;
	private AbstractInsnNode[] iArray;
	private Snippet snippet;
	private Shadow shadow;
	private AbstractInsnNode weavingLoc;
	private int maxLocals;

	public WeavingCode(WeavingInfo weavingInfo, MethodNode method,
			SnippetCode src, Snippet snippet, Shadow shadow,
			AbstractInsnNode loc) {

		this.info = weavingInfo;
		this.method = method;
		this.code = src.clone();
		this.snippet = snippet;
		this.shadow = shadow;
		this.weavingLoc = loc;

		this.iList = code.getInstructions();
		this.iArray = iList.toArray();

		maxLocals = MaxCalculator
				.getMaxLocal(iList, method.desc, method.access);
	}

	// Search for an instruction sequence that match the pattern
	// of fetching static information.
	public void fixStaticInfo(SCGenerator staticInfoHolder) {

		// TODO LB: iterate over a copy unless we are sure an iterator is OK
		for (AbstractInsnNode instr : iList.toArray()) {

			AbstractInsnNode previous = instr.getPrevious();
			// Static information are represented by a static function call with
			// a null as its parameter.
			if (instr.getOpcode() != Opcodes.INVOKEVIRTUAL || previous == null
					|| previous.getOpcode() != Opcodes.ALOAD) {
				continue;
			}

			MethodInsnNode invocation = (MethodInsnNode) instr;

			if (staticInfoHolder.contains(shadow, invocation.owner,
					invocation.name)) {

				Object const_var = staticInfoHolder.get(shadow,
						invocation.owner, invocation.name);

				if (const_var != null) {
					// Insert a ldc instruction
					code.getInstructions().insert(instr,
							AsmHelper.loadConst(const_var));
				} else {
					// push null onto the stack
					code.getInstructions().insert(instr,
							new InsnNode(Opcodes.ACONST_NULL));
				}

				// remove the pseudo instructions
				iList.remove(previous);
				iList.remove(instr);
			}
		}
	}

	public void fixClassInfo() {
		// TODO LB: iterate over a copy unless we are sure an iterator is OK
		for (AbstractInsnNode instr : iList.toArray()) {

			AbstractInsnNode previous = instr.getPrevious();

			if (instr.getOpcode() != Opcodes.INVOKEINTERFACE
					|| previous == null || previous.getOpcode() != Opcodes.LDC) {
				continue;
			}

			LdcInsnNode ldc = (LdcInsnNode) previous;
			MethodInsnNode invoke = (MethodInsnNode) instr;

			if (!((ldc.cst instanceof String)
					&& invoke.owner.equals(Type
							.getInternalName(ClassContext.class)) && invoke.name
						.equals("asClass"))) {
				continue;
			}

			// User should guarantee that internal name of a class is passed to
			// the ClassContext.
			Type clazz = Type.getObjectType(ldc.cst.toString());
			iList.insert(instr, new LdcInsnNode(clazz));
			iList.remove(ldc.getPrevious());
			iList.remove(ldc);
			iList.remove(invoke);
		}
	}

	private void preFixDynamicInfoCheck() throws DynamicContextException {
		for (AbstractInsnNode instr : AsmHelper.allInsnsFrom (iList)) {

			// it is invocation...
			if (instr.getOpcode() != Opcodes.INVOKEINTERFACE) {
				continue;
			}

			MethodInsnNode invoke = (MethodInsnNode) instr;

			// ... of dynamic context
			if (!invoke.owner
					.equals(Type.getInternalName(DynamicContext.class))) {
				continue;
			}

			if (invoke.name.equals("getThis")
					|| invoke.name.equals("getException")) {
				continue;
			}

			AbstractInsnNode secondOperand = instr.getPrevious();
			AbstractInsnNode firstOperand = secondOperand.getPrevious();

			// first operand test
			switch (firstOperand.getOpcode()) {
			case Opcodes.ICONST_M1:
			case Opcodes.ICONST_0:
			case Opcodes.ICONST_1:
			case Opcodes.ICONST_2:
			case Opcodes.ICONST_3:
			case Opcodes.ICONST_4:
			case Opcodes.ICONST_5:
			case Opcodes.BIPUSH:
				break;

			default:
				throw new DynamicContextException("In snippet "
						+ snippet.getOriginClassName() + "."
						+ snippet.getOriginMethodName()
						+ " - pass the first (pos)"
						+ " argument of a dynamic context method direcltly."
						+ " ex: getStackValue(1, int.class)");
			}

			// second operand test
			if (! AsmHelper.isTypeConstLoadInsn (secondOperand)) {
				throw new DynamicContextException("In snippet "
						+ snippet.getOriginClassName() + "."
						+ snippet.getOriginMethodName()
						+ " - pass the second (type)"
						+ " argument of a dynamic context method direcltly."
						+ " ex: getStackValue(1, int.class)");
			}
		}
	}

	private static final int INVALID_SLOT = -1;
	private static List<String> primitiveTypes;

	static {
		primitiveTypes = new LinkedList<String>();
		primitiveTypes.add("java/lang/Boolean");
		primitiveTypes.add("java/lang/Byte");
		primitiveTypes.add("java/lang/Character");
		primitiveTypes.add("java/lang/Double");
		primitiveTypes.add("java/lang/Float");
		primitiveTypes.add("java/lang/Integer");
		primitiveTypes.add("java/lang/Long");
	}

	// Search for an instruction sequence that stands for a request for dynamic
	// information, and replace them with a load instruction.
	// NOTE that if the user requests for the stack value, some store
	// instructions will be inserted to the target method, and new local slot
	// will be used for storing this.
	public void fixDynamicInfo(boolean throwing) throws DynamicContextException {

		preFixDynamicInfoCheck();

		Frame<BasicValue> basicframe = info.getBasicFrame(weavingLoc);
		Frame<SourceValue> sourceframe = info.getSourceFrame(weavingLoc);
		int exceptionslot = INVALID_SLOT;

		if (throwing) {
			exceptionslot = method.maxLocals;
			method.maxLocals++;
		}

		// TODO LB: iterate over a copy unless we are sure an iterator is OK
		for (AbstractInsnNode instr : iList.toArray()) {
			// pseudo function call
			if (instr.getOpcode() != Opcodes.INVOKEINTERFACE) {
				continue;
			}

			MethodInsnNode invoke = (MethodInsnNode) instr;

			if (!invoke.owner
					.equals(Type.getInternalName(DynamicContext.class))) {
				continue;
			}

			AbstractInsnNode prev = instr.getPrevious();

			if (invoke.name.equals("getThis")) {

				if ((method.access & Opcodes.ACC_STATIC) != 0) {
					iList.insert(instr, new InsnNode(Opcodes.ACONST_NULL));
				} else {
					iList.insert(instr, new VarInsnNode(Opcodes.ALOAD, 0));
				}

				iList.remove(invoke);
				iList.remove(prev);
				continue;
			} else if (invoke.name.equals("getException")) {

				if (throwing) {
					iList.insert(instr, new VarInsnNode(Opcodes.ALOAD,
							exceptionslot));
				} else {
					iList.insert(instr, new InsnNode(Opcodes.ACONST_NULL));
				}

				iList.remove(invoke);
				iList.remove(prev);
				continue;
			}

			AbstractInsnNode next = instr.getNext();

			// parsing:
			// aload dynamic_info
			// iconst
			// ldc class
			// invoke (current instruction)
			// [checkcast]
			// [invoke]
			int operand = AsmHelper.getIntConstOperand(prev.getPrevious());
			Type t = AsmHelper.getTypeConstOperand(prev);

			if (invoke.name.equals("getStackValue")) {

				if (basicframe == null) {
					// TODO warn user that weaving location is unreachable.
					iList.insert(instr, AsmHelper.loadDefault(t));

					if (!AsmHelper.isReferenceType(t)) {
						iList.insert(instr, AsmHelper.boxValueOnStack(t));
					}
				} else {

					int lopcode = t.getOpcode(Opcodes.ILOAD);

					// index should be less than the stack height
					if (operand >= basicframe.getStackSize() || operand < 0) {

						throw new DynamicContextException("In snippet "
								+ snippet.getOriginClassName() + "."
								+ snippet.getOriginMethodName()
								+ " - trying to access the stack item NO."
								+ operand + ", but the size of the stack is "
								+ basicframe.getStackSize());
					}

					// Type checking
					Type targetType = FrameHelper.getStackByIndex(basicframe,
							operand).getType();

					if (t.getSort() != targetType.getSort()) {
						throw new DynamicContextException("In snippet "
								+ snippet.getOriginClassName() + "."
								+ snippet.getOriginMethodName()
								+ " - trying to access the stack item NO."
								+ operand + " with a provided type \"" + t
								+ "\", but we found \"" + targetType + "\".");
					}

					// store the stack value without changing the semantic
					int size = FrameHelper.dupStack(sourceframe, method, operand,
							t, method.maxLocals);
					// load the stack value

					// box value if applicable
					if (!AsmHelper.isReferenceType(t)) {
						iList.insert(instr, AsmHelper.boxValueOnStack(t));
					}

					iList.insert(instr, new VarInsnNode(lopcode, method.maxLocals));
					method.maxLocals += size;
				}
			}
			// TRICK: the following two situation will generate a VarInsnNode
			// with a negative local slot. And it will be updated in
			// method fixLocalIndex
			else if (invoke.name.equals("getMethodArgumentValue")) {

				if (basicframe == null) {
					// TODO warn user that weaving location is unreachable.
					basicframe = info.getRetFrame();
				}

				int slot = AsmHelper.getInternalParamIndex(method, operand);
				int args = Type.getArgumentTypes(method.desc).length;

				// index should be less than the size of local variables
				if (operand >= args || operand < 0) {

					throw new DynamicContextException("In snippet "
							+ snippet.getOriginClassName() + "."
							+ snippet.getOriginMethodName()
							+ " - trying to access the method argument NO."
							+ operand
							+ ", but the size of the method argument is "
							+ args);
				}

				// Type checking
				Type targetType = basicframe.getLocal(slot).getType();

				if (t.getSort() != targetType.getSort()) {
					
					throw new DynamicContextException("In snippet "
							+ snippet.getOriginClassName() + "."
							+ snippet.getOriginMethodName()
							+ " - trying to access the method argument NO."
							+ operand + " with a provided type \"" + t
							+ "\", but we found \"" + targetType + "\".");
				}

				// box value if applicable
				// boxing is removed by partial evaluator if not needed
				if(! AsmHelper.isReferenceType(t)) {
					iList.insert(instr, AsmHelper.boxValueOnStack(t));
				}
				iList.insert(instr, new VarInsnNode(t.getOpcode(Opcodes.ILOAD),
						slot));
			} else if (invoke.name.equals("getLocalVariableValue")) {

				if (basicframe == null) {
					// TODO warn user that weaving location is unreachable.
					basicframe = info.getRetFrame();
				}

				// index should be less than the size of local variables
				if (operand >= basicframe.getLocals() || operand < 0) {

					throw new DynamicContextException("In snippet "
							+ snippet.getOriginClassName() + "."
							+ snippet.getOriginMethodName()
							+ " - trying to access the local variable NO."
							+ operand
							+ ", but the size of the local varaibles is "
							+ basicframe.getLocals());
				}

				// Type checking
				Type targetType = basicframe.getLocal(operand).getType();

				if (t.getSort() != targetType.getSort()) {

					throw new DynamicContextException("In snippet "
							+ snippet.getOriginClassName() + "."
							+ snippet.getOriginMethodName()
							+ " - trying to access the local variable NO."
							+ operand + " with a provided type \"" + t
							+ "\", but we found \"" + targetType + "\".");
				}

				// box value if applicable
				// boxing is removed by partial evaluator if not needed
				if(! AsmHelper.isReferenceType(t)) {
					iList.insert(instr, AsmHelper.boxValueOnStack(t));
				}
				iList.insert(instr, new VarInsnNode(t.getOpcode(Opcodes.ILOAD),
						operand));
			}

			// remove aload, iconst, ldc
			iList.remove(instr.getPrevious());
			iList.remove(instr.getPrevious());
			iList.remove(instr.getPrevious());

			// remove invoke
			iList.remove(instr);

			// remove checkcast
			if (next.getOpcode() == Opcodes.CHECKCAST) {
				iList.remove(next);
			}
		}
		
		// TODO LB: iterate over a copy unless we are sure an iterator is OK
		for (AbstractInsnNode instr : iList.toArray()) {

			AbstractInsnNode prev = instr.getPrevious();

			if (prev == null || (prev.getOpcode() != Opcodes.INVOKESTATIC)
					|| (instr.getOpcode() != Opcodes.INVOKEVIRTUAL)) {
				continue;
			}

			MethodInsnNode valueOf = (MethodInsnNode) prev;
			MethodInsnNode toValue = (MethodInsnNode) instr;

			if (!(primitiveTypes.contains(valueOf.owner)
					&& valueOf.owner.equals(toValue.owner)
					&& valueOf.name.equals("valueOf")
					&& toValue.name.endsWith("Value"))) {
				continue;
			}

			if (!Type.getArgumentTypes(valueOf.desc)[0].equals(Type
					.getReturnType(toValue.desc))) {
				continue;
			}

			iList.remove(prev);
			iList.remove(instr);
		}

		if (throwing) {

			iList.insertBefore(iList.getFirst(), new VarInsnNode(
					Opcodes.ASTORE, exceptionslot));
			iList.add(new VarInsnNode(Opcodes.ALOAD, exceptionslot));
			iList.add(new InsnNode(Opcodes.ATHROW));
		}
	}

	// Fix the stack operand index of each stack-based instruction
	// according to the maximum number of locals in the target method node.
	// NOTE that the field maxLocals of the method node will be automatically
	// updated.
	public void fixLocalIndex() {
		method.maxLocals = fixLocalIndex(iList, method.maxLocals);
	}

	private int fixLocalIndex(InsnList src, int offset) {
		int max = offset;

		for (AbstractInsnNode instr : AsmHelper.allInsnsFrom (src)) {

			if (instr instanceof VarInsnNode) {

				VarInsnNode varInstr = (VarInsnNode) instr;
				varInstr.var += offset;

				switch (varInstr.getOpcode()) {
				case Opcodes.LLOAD:
				case Opcodes.DLOAD:
				case Opcodes.LSTORE:
				case Opcodes.DSTORE:
					max = Math.max(varInstr.var + 2, max);
					break;

				default:
					max = Math.max(varInstr.var + 1, max);
					break;
				}
			} else if (instr instanceof IincInsnNode) {

				IincInsnNode iinc = (IincInsnNode) instr;
				iinc.var += offset;
				max = Math.max(iinc.var + 1, max);
			}
		}

		return max;
	}

	private void fixArgumentContext(InsnList instructions, int position,
			int totalCount, Type type) {

		// TODO LB: iterate over a copy unless we are sure an iterator is OK
		for (AbstractInsnNode instr : instructions.toArray()) {

			AbstractInsnNode previous = instr.getPrevious();

			if (instr.getOpcode() != Opcodes.INVOKEINTERFACE
					|| previous == null
					|| previous.getOpcode() != Opcodes.ALOAD) {
				continue;
			}

			MethodInsnNode invoke = (MethodInsnNode) instr;

			if (!invoke.owner.equals(Type
					.getInternalName(ArgumentContext.class))) {
				continue;
			}

			if (invoke.name.equals("getPosition")) {
				instructions.insert(instr, AsmHelper.loadConst(position));
			} else if (invoke.name.equals("getTotalCount")) {
				instructions.insert(instr, AsmHelper.loadConst(totalCount));
			} else if (invoke.name.equals("getTypeDescriptor")) {
				instructions
						.insert(instr, AsmHelper.loadConst(type.toString()));
			}

			// remove the pseudo instructions
			instructions.remove(previous);
			instructions.remove(instr);
		}
	}

	// combine processors into an instruction list
	// NOTE that these processors are for the current method
	private InsnList procInMethod(ProcInstance processor) {

		InsnList ilist = new InsnList();

		for (ProcMethodInstance processorMethod : processor.getMethods()) {

			Code code = processorMethod.getCode().clone();
			InsnList instructions = code.getInstructions();

			int position = processorMethod.getArgPos();
			int totalCount = processorMethod.getArgsCount();
			Type type = processorMethod.getArgType().getASMType();

			fixArgumentContext(instructions, position, totalCount, type);

			AbstractInsnNode start = instructions.getFirst();

			VarInsnNode target = new VarInsnNode(
					type.getOpcode(Opcodes.ISTORE), 0);
			instructions.insertBefore(start, target);

			maxLocals = Math.max(fixLocalIndex(instructions, maxLocals),
					maxLocals + type.getSize());

			instructions.insertBefore(
					target,
					new VarInsnNode(type.getOpcode(Opcodes.ILOAD), AsmHelper
							.getInternalParamIndex(method,
									processorMethod.getArgPos())
							- method.maxLocals));

			ilist.add(instructions);
			method.tryCatchBlocks.addAll(code.getTryCatchBlocks());
		}

		return ilist;
	}

	// combine processors into an instruction list
	// NOTE that these processors are for the callee
	private InsnList procBeforeInvoke(ProcInstance processor) {

		Frame<SourceValue> frame = info.getSourceFrame(weavingLoc);
		InsnList ilist = new InsnList();

		for (ProcMethodInstance processorMethod : processor.getMethods()) {

			Code code = processorMethod.getCode().clone();
			InsnList instructions = code.getInstructions();

			int position = processorMethod.getArgPos();
			int totalCount = processorMethod.getArgsCount();
			Type type = processorMethod.getArgType().getASMType();

			fixArgumentContext(instructions, position, totalCount, type);

			SourceValue source = FrameHelper.getStackByIndex(frame, totalCount
					- 1 - position);
			int sopcode = type.getOpcode(Opcodes.ISTORE);

			for (AbstractInsnNode itr : source.insns) {
				method.instructions.insert(itr, new VarInsnNode(sopcode,
				// TRICK: the value has to be set properly because
				// method code will be not adjusted by fixLocalIndex
						method.maxLocals + maxLocals));
				method.instructions.insert(itr, new InsnNode(
						type.getSize() == 2 ? Opcodes.DUP2 : Opcodes.DUP));
			}

			maxLocals = Math.max(fixLocalIndex(instructions, maxLocals),
					maxLocals + type.getSize());
			ilist.add(instructions);
			method.tryCatchBlocks.addAll(code.getTryCatchBlocks());
		}

		return ilist;
	}

	// replace processor-applying pseudo invocation with processors
	public void fixProcessor(PIResolver piResolver) {

		for (int i : code.getInvokedProcessors().keySet()) {

			AbstractInsnNode instr = iArray[i];
			ProcInstance processor = piResolver.get(shadow, i);

			if (processor != null) {
				if (processor.getProcApplyType() == ArgumentProcessorMode.METHOD_ARGS) {
					iList.insert(instr, procInMethod(processor));
				} else {
					iList.insert(instr, procBeforeInvoke(processor));
				}
			}

			// remove pseudo invocation
			iList.remove(instr.getPrevious());
			iList.remove(instr.getPrevious());
			iList.remove(instr.getPrevious());
			iList.remove(instr);
		}
	}

	private InsnList createGetArgsCode(String methodDescriptor) {

		InsnList insnList = new InsnList();

		Type[] argTypes = Type.getArgumentTypes(methodDescriptor);

		// array creation code (length is the length of arguments)
		insnList.add(AsmHelper.loadConst(argTypes.length));
		insnList.add(new TypeInsnNode(Opcodes.ANEWARRAY, "java/lang/Object"));

		int argIndex = 0;
		for (int i = 0; i < argTypes.length; ++i) {

			// ** add new array store **

			// duplicate array object
			insnList.add(new InsnNode(Opcodes.DUP));

			// add index into the array where to store the value
			insnList.add(AsmHelper.loadConst(i));

			Type argType = argTypes[i];

			// load "object" that will be stored
			int loadOpcode = argType.getOpcode(Opcodes.ILOAD);
			insnList.add(new VarInsnNode(loadOpcode, argIndex));

			// box non-reference type
			if (! AsmHelper.isReferenceType(argType)) {
				insnList.add(AsmHelper.boxValueOnStack(argType));
			}

			// store the value into the array on particular index
			insnList.add(new InsnNode(Opcodes.AASTORE));

			// shift argument index according to argument size
			argIndex += argType.getSize();
		}

		return insnList;
	}

	public void fixProcessorInfo() {

		// TODO LB: iterate over a copy unless we are sure an iterator is OK
		for (AbstractInsnNode instr : iList.toArray()) {

			// it is invocation...
			if (instr.getOpcode() != Opcodes.INVOKEINTERFACE) {
				continue;
			}

			MethodInsnNode invoke = (MethodInsnNode) instr;

			// ... of ArgumentProcessorContext
			if (!invoke.owner.equals(Type
					.getInternalName(ArgumentProcessorContext.class))) {
				continue;
			}

			AbstractInsnNode prev = instr.getPrevious();

			if (prev.getOpcode() != Opcodes.GETSTATIC) {
				
				throw new DiSLFatalException("In snippet "
						+ snippet.getOriginClassName() + "."
						+ snippet.getOriginMethodName()
						+ " - unknown processor mode");
			}

			ArgumentProcessorMode procApplyType = ArgumentProcessorMode
					.valueOf(((FieldInsnNode) prev).name);

			if (invoke.name.equals("getArgs")) {

				InsnList args = null;

				if (procApplyType == ArgumentProcessorMode.METHOD_ARGS) {

					args = createGetArgsCode(method.desc);
					fixLocalIndex(args,
							((method.access & Opcodes.ACC_STATIC) != 0 ? 0 : 1)
									- method.maxLocals);
				} else {

					AbstractInsnNode callee = AsmHelper.skipVirtualInsns(
							shadow.getRegionStart(), true);
					
					if (!(callee instanceof MethodInsnNode)) {
						throw new DiSLFatalException("In snippet "
								+ snippet.getOriginClassName() + "."
								+ snippet.getOriginMethodName()
								+ " - unexpected bytecode when applying"
								+ " \"ArgumentProcessorContext.getArgs\"");
					}

					String desc = ((MethodInsnNode) callee).desc;
					Type[] argTypes = Type.getArgumentTypes(desc);

					Frame<SourceValue> frame = info.getSourceFrame(callee);

					if (frame == null) {
						throw new DiSLFatalException("In snippet "
								+ snippet.getOriginClassName() + "."
								+ snippet.getOriginMethodName()
								+ " - unexpected bytecode when applying"
								+ " \"ArgumentProcessorContext.getArgs\"");
					}
					
					int argIndex = 0;
					
					for (int i = 0; i < argTypes.length; i++) {

						SourceValue source = FrameHelper.getStackByIndex(frame,
								argTypes.length - 1 - i);
						Type type = argTypes[i];
						int sopcode = type.getOpcode(Opcodes.ISTORE);

						for (AbstractInsnNode itr : source.insns) {
							method.instructions.insert(itr, new VarInsnNode(sopcode,
							// TRICK: the value has to be set properly because
							// method code will be not adjusted by fixLocalIndex
									method.maxLocals + maxLocals + argIndex));
							method.instructions.insert(itr,
									new InsnNode(type.getSize() == 2 ? Opcodes.DUP2
													: Opcodes.DUP));
						}

						argIndex += type.getSize();
					}

					args = createGetArgsCode(desc);
					maxLocals = Math.max(fixLocalIndex(args, maxLocals),
							maxLocals + argIndex);
				}

				iList.insert(instr, args);
			} else if (invoke.name.equals("getReceiver")) {

				if (procApplyType == ArgumentProcessorMode.METHOD_ARGS) {

					if ((method.access & Opcodes.ACC_STATIC) != 0) {
						iList.insert(instr, new InsnNode(Opcodes.ACONST_NULL));
					} else {
						iList.insert(instr, new VarInsnNode(Opcodes.ALOAD,
								-method.maxLocals));
					}
				} else {

					AbstractInsnNode callee = AsmHelper.skipVirtualInsns(
							shadow.getRegionStart(), true);
					
					if (!(callee instanceof MethodInsnNode)) {
						throw new DiSLFatalException("In snippet "
								+ snippet.getOriginClassName() + "."
								+ snippet.getOriginMethodName()
								+ " - unexpected bytecode when applying"
								+ " \"ArgumentProcessorContext.getReceiver\"");
					}

					Frame<SourceValue> frame = info.getSourceFrame(callee);

					if (frame == null) {
						throw new DiSLFatalException("In snippet "
								+ snippet.getOriginClassName() + "."
								+ snippet.getOriginMethodName()
								+ " - unexpected bytecode when applying"
								+ " \"ArgumentProcessorContext.getReceiver\"");
					}

					if (callee.getOpcode() == Opcodes.INVOKESTATIC) {
						iList.insert(instr, new InsnNode(Opcodes.ACONST_NULL));
					} else {
						String desc = ((MethodInsnNode) callee).desc;
						SourceValue source = FrameHelper.getStackByIndex(frame,
								Type.getArgumentTypes(desc).length);
						
						for (AbstractInsnNode itr : source.insns) {
							method.instructions.insert(itr, new VarInsnNode(
									Opcodes.ASTORE,
							// TRICK: the value has to be set properly because
							// method code will be not adjusted by fixLocalIndex
									method.maxLocals + maxLocals));
							method.instructions.insert(itr, new InsnNode(
									Opcodes.DUP));
						}

						iList.insert(instr, new VarInsnNode(Opcodes.ALOAD,
								maxLocals));
						maxLocals++;
					}
				}
			}

			iList.remove(instr.getPrevious());
			iList.remove(instr.getPrevious());
			iList.remove(instr);
		}
	}

	public InsnList getiList() {
		return iList;
	}

	public List<TryCatchBlockNode> getTCBs() {
		return code.getTryCatchBlocks();
	}

	public void transform(SCGenerator staticInfoHolder, PIResolver piResolver, boolean throwing)
			throws DynamicContextException {
		fixProcessor(piResolver);
		fixProcessorInfo();
		fixStaticInfo(staticInfoHolder);
		fixClassInfo();
		fixLocalIndex();
		optimize();

		fixDynamicInfo(throwing);
	}

	public void optimize() {

		String prop_pe = System.getProperty(PROP_PE);

		if ((prop_pe == null) || (prop_pe.length() < 2)
				|| (prop_pe.charAt(0) != 'o' && prop_pe.charAt(0) != 'O')) {
			return;
		}

		char option = prop_pe.charAt(1);
		PartialEvaluator pe = new PartialEvaluator(iList,
				code.getTryCatchBlocks(), method.desc, method.access);

		if (option >= '1' && option <= '3') {

			for (int i = 0; i < (option - '0'); i++) {
				pe.evaluate();
			}
		} else if (option == 'x') {
			while (pe.evaluate())
				;
		}
	}
}
