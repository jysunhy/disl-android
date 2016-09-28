package ch.usi.dag.javamop.unsafeiterator;

import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.exception.MarkerException;
import ch.usi.dag.disl.marker.AbstractMarker.MarkedRegion;
import ch.usi.dag.disl.marker.Marker;
import ch.usi.dag.disl.snippet.Shadow;
import ch.usi.dag.disl.snippet.Snippet;

/**
 * Sets the region on every method invocation instruction.
 *
 */
public abstract class AbstractInvocationMarker implements Marker {

 private static String name;

 	//Getter and Setter Methods for Getting the class name from mark() method
	public void setClassName(final String n){

		name = n;


	}

	public String getClassName(){

		return name;


	}


		@Override
	    public List <Shadow> mark (
	        final ClassNode classNode, final MethodNode methodNode,
	        final Snippet snippet
	    ) throws MarkerException {
	        // use simplified interface

				setClassName(classNode.name);

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


	public final List<MarkedRegion> mark(final MethodNode methodNode) {


		      final List<MarkedRegion> mrs = markWithDefaultWeavingReg(methodNode);

		        // automatically compute default weaving region
		        for (final MarkedRegion mr : mrs) {
		            mr.setWeavingRegion(mr.computeDefaultWeavingRegion(methodNode));
		        }

		        return mrs;
		    }

		// Methods for testing the condition
		public abstract String getMethodName();
		public abstract String testClassName();
		public abstract String getSignature();
		public abstract String getReceiver();


		public List<MarkedRegion> markWithDefaultWeavingReg(final MethodNode method) {
			final List<MarkedRegion> regions = new LinkedList<MarkedRegion>();

			// traverse all instructions

			final InsnList instructions = method.instructions;

				for (final AbstractInsnNode instruction : instructions.toArray()) {

						// check for method invocation instructions

						if (instruction instanceof MethodInsnNode) {
								final MethodInsnNode inst = (MethodInsnNode)instruction;
//System.out.println("getMethodName()=" + getMethodName()+ " & "+ "inst.name=" + inst.name + " ... "+"getClassName()=" + getClassName() + " & " +"testClassName()=" + " & " +testClassName() +" ... "+ "getSignature()=" + getSignature() + " & " +"inst.desc=" + " & " +inst.desc +" ... "+ inst.owner);


									if((getMethodName().equals(inst.name))&&(getSignature().equals(inst.desc))&&(getReceiver().equals(inst.owner))){


											regions.add(new MarkedRegion(instruction, instruction));

								}
						}
				}


			return regions;


		}

}
