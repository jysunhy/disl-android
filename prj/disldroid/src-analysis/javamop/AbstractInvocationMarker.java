package javamop;
import java.util.LinkedList;
import java.util.List;
import java.io.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ClassNode;
import ch.usi.dag.disl.marker.AbstractMarker;
import ch.usi.dag.disl.marker.AbstractDWRMarker;
import ch.usi.dag.disl.marker.Marker;
import ch.usi.dag.disl.snippet.Snippet;
import ch.usi.dag.disl.snippet.Shadow;
import ch.usi.dag.disl.snippet.Shadow.WeavingRegion;
import ch.usi.dag.disl.exception.MarkerException;
import ch.usi.dag.disl.marker.AbstractMarker.MarkedRegion;
import java.util.*;
import org.objectweb.asm.tree.LabelNode;

import ch.usi.dag.disl.scope.Scope;
import ch.usi.dag.disl.scope.ScopeImpl;
import ch.usi.dag.disl.exception.ScopeParserException;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public abstract class AbstractInvocationMarker implements Marker {

 private static String appropriateMethodDescription;
 private static String className;
 private static String methodName;
		// Methods for testing the condition
		public abstract String getMethodDescription();

		@Override
	    public List <Shadow> mark (
	        final ClassNode classNode, final MethodNode methodNode,
	        final Snippet snippet
	    ) throws MarkerException {
	        // use simplified interface
			className = classNode.name;
			methodName = methodNode.name;
	       final List<MarkedRegion> regions = mark(methodNode);
	        final List<Shadow> result = new LinkedList<Shadow>();

	        // convert marked regions to shadows
	        for (final MarkedRegion mr : regions) {
	            if (!mr.valid()) {
	                throw new MarkerException("Marker " + this.getClass()
	                        + " produced invalid MarkedRegion (some MarkedRegion" +
	                        " fields where not set)");
	            }

	            result.add (new Shadow (
	                classNode, methodNode, snippet,
	                mr.getStart (), mr.getEnds (), mr.getWeavingRegion ()
	            ));
	        }
			//To get the class name


			return result;

	    }


	public final List<MarkedRegion> mark(MethodNode methodNode) {


		      List<MarkedRegion> mrs = markWithDefaultWeavingReg(methodNode);

		        // automatically compute default weaving region
		        for (MarkedRegion mr : mrs) {
		            mr.setWeavingRegion(mr.computeDefaultWeavingRegion(methodNode));
		        }

		        return mrs;
		    }

		public List<MarkedRegion> markWithDefaultWeavingReg(MethodNode method) {
			List<MarkedRegion> regions = new LinkedList<MarkedRegion>();

			// traverse all instructions
			if((method.access & Opcodes.ACC_BRIDGE)<1){
				InsnList instructions = method.instructions;

					for (AbstractInsnNode instruction : instructions.toArray()) {

							// check for method invocation instructions
						if(instruction.getOpcode()!=Opcodes.INVOKESPECIAL){
							if (instruction instanceof MethodInsnNode) {
									MethodInsnNode inst = (MethodInsnNode)instruction;
								//if(IdentifyClass.allowSubInterfaces(getMethodDescription(),instruction,inst)){
											appropriateMethodDescription = IdentifyClass.ClassFromInterface(inst.owner, inst.name, getMethodDescription(),className,methodName);
										//}
										try{
											Scope s = new ScopeImpl(appropriateMethodDescription);
											//if(s.matches(inst.owner, inst.name, inst.desc)){
												//System.out.println("Status of isInterfaceMethod="+IdentifyClass.isInterfaceMethod());
												if(s.matches(inst.owner, inst.name, inst.desc) && IdentifyClass.isInterfaceMethod()){
												regions.add(new MarkedRegion(instruction, instruction));
											}
										} catch(Exception ex){}

							//	}
							}
						}
					}

			}
			return regions;


		}


}
