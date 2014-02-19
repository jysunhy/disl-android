package ch.usi.dag.disl.marker;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

import ch.usi.dag.disl.exception.MarkerException;
import ch.usi.dag.disl.snippet.Shadow;
import ch.usi.dag.disl.snippet.Shadow.WeavingRegion;
import ch.usi.dag.disl.snippet.Snippet;

/**
 * <p>
 * AbstractMarker eases the effort to implement new marker by providing mark
 * method returning MarkedRegion class instead of Shadow. The MarkedRegion class
 * itself supports automatic computation of weaving region based on simplified
 * region specification.
 */
public abstract class AbstractMarker implements Marker {

    /**
     * <p>
     * The class specifies marked region. The start and ends are mandatory
     * values where the weaving region can be precomputed by
     * computeDefaultWeavingRegion method.
     */
    public static class MarkedRegion {

        private AbstractInsnNode       start;
        private List<AbstractInsnNode> ends;

        private WeavingRegion          weavingRegion;

        /**
         * Access region start.
         */
        public AbstractInsnNode getStart() {
            return start;
        }

        /**
         * Set region start.
         */
        public void setStart(AbstractInsnNode start) {
            this.start = start;
        }

        /**
         * Access list of region ends.
         */
        public List<AbstractInsnNode> getEnds() {
            return ends;
        }

        /**
         * Add one region end to the list.
         */
        public void addEnd(AbstractInsnNode exitpoint) {
            this.ends.add(exitpoint);
        }

        /**
         * Access weaving region.
         */
        public WeavingRegion getWeavingRegion() {
            return weavingRegion;
        }

        /**
         * Set weaving region.
         */
        public void setWeavingRegion(WeavingRegion weavingRegion) {
            this.weavingRegion = weavingRegion;
        }

        /**
         * Crate marked region with start.
         */
        public MarkedRegion(AbstractInsnNode start) {
            this.start = start;
            this.ends = new LinkedList<AbstractInsnNode>();
        }

        /**
         * Create marked region with start and one end.
         */
        public MarkedRegion(AbstractInsnNode start, AbstractInsnNode end) {
            this.start = start;
            this.ends = new LinkedList<AbstractInsnNode>();
            this.ends.add(end);
        }

        /**
         * Create marked region with start and list of ends.
         */
        public MarkedRegion(AbstractInsnNode start, List<AbstractInsnNode> ends) {
            this.start = start;
            this.ends = ends;
        }

        /**
         * Create marked region with start, multiple ends and weaving region.
         */
        public MarkedRegion(AbstractInsnNode start,
                List<AbstractInsnNode> ends, WeavingRegion weavingRegion) {
            super();
            this.start = start;
            this.ends = ends;
            this.weavingRegion = weavingRegion;
        }

        /**
         * Test if all required fields are filled
         */
        public boolean valid() {
            return start != null && ends != null && weavingRegion != null;
        }

        /**
         * Computes default weaving region for this MarkedRegion. Computed
         * weaving region will NOT be automatically associated with this
         * MarkedRegion.
         */
        public WeavingRegion computeDefaultWeavingRegion(MethodNode methodNode) {

            AbstractInsnNode wstart = start;
            // wends is set to null - see WeavingRegion for details

            // compute after throwing region

            // set start
            AbstractInsnNode afterThrowStart = start;
            AbstractInsnNode afterThrowEnd = null;

            // get end that is the latest in the method instructions
            Set<AbstractInsnNode> endsSet = new HashSet<AbstractInsnNode>(ends);

            // get end that is the latest in the method instructions
            AbstractInsnNode instr = methodNode.instructions.getLast();

            while (instr != null) {

                if (endsSet.contains(instr)) {
                    afterThrowEnd = instr;
                    break;
                }

                instr = instr.getPrevious();
            }

            // skip the label nodes which are the end of try-catch blocks
            if (afterThrowEnd instanceof LabelNode) {

                Set<AbstractInsnNode> tcb_ends = new HashSet<AbstractInsnNode>();

                for (TryCatchBlockNode tcb : methodNode.tryCatchBlocks) {
                    tcb_ends.add(tcb.end);
                }

                while (tcb_ends.contains(afterThrowEnd)) {
                    afterThrowEnd = afterThrowEnd.getPrevious();
                }
            }

            return new WeavingRegion(wstart, null, afterThrowStart,
                    afterThrowEnd);
        }
    }

    @Override
    public List<Shadow> mark(ClassNode classNode, MethodNode methodNode,
            Snippet snippet) throws MarkerException {

        // use simplified interface
        List<MarkedRegion> regions = mark(methodNode);

        List<Shadow> result = new LinkedList<Shadow>();

        // convert marked regions to shadows
        for (MarkedRegion mr : regions) {

            if (!mr.valid()) {
                throw new MarkerException("Marker " + this.getClass()
                        + " produced invalid MarkedRegion (some MarkedRegion" +
                        " fields where not set)");
            }

            result.add(new Shadow(classNode, methodNode, snippet,
                    mr.getStart(), mr.getEnds(), mr.getWeavingRegion()));
        }

        return result;
    }

    /**
     * Implementation of this method should return list of MarkedRegion with
     * start, ends end weaving region filled.
     * 
     * @param methodNode
     *            method node of the marked class
     * @return returns list of MarkedRegion
     */
    public abstract List<MarkedRegion> mark(MethodNode methodNode);
}
