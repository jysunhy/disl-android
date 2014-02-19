package ch.usi.dag.disl.staticcontext.uid;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.util.Constants;

public class UniqueMethodId extends AbstractUniqueId {

	private static AbstractUniqueId instance = null;
	
	// constructor for static context
	public UniqueMethodId() {
		
	}
	
	// constructor for singleton
	private UniqueMethodId(AbstractIdCalculator idCalc,
			String outputFileName) {
		super(idCalc, outputFileName);
	}
	
	protected String idFor() {
		
		ClassNode classNode = staticContextData.getClassNode();
		MethodNode methodNode = staticContextData.getMethodNode();
		
		return classNode.name
				+ Constants.CLASS_DELIM
				+ methodNode.name
				+ "("
				+ methodNode.desc
				+ ")";
	}
	
	protected synchronized AbstractUniqueId getSingleton() {
		
		if (instance == null) {
			instance = new UniqueMethodId(new RandomId(), "methodid.txt");
		}
		return instance;
	}
}
