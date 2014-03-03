package ch.usi.dag.fia.disl;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.classcontext.ClassContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.fia.observe.ImmutabilityAnalysisRE;

public class DiSLClass {

    /** CONSTRUCTORS *************************************************************/

    @Before(marker = BodyMarker.class, guard = ConstructorGuard.class)
    public static void beforeConstructor(final MethodStaticContext sc, final DynamicContext dc) {

        ImmutabilityAnalysisRE.constructorStart(dc.getThis());
    }

    @After(marker = BodyMarker.class, guard = ConstructorGuard.class)
    public static void afterConstructor(final MethodStaticContext sc) {

    	ImmutabilityAnalysisRE.constructorEnd();

    }

    public static class ConstructorGuard {

        @GuardMethod
        public static boolean isApplicable(final MethodStaticContext sc) {
            return "<init>".equals(sc.thisMethodName());
        }
    }

    /** FIELD ACCESSES ***********************************************************/

    @Before(marker=BytecodeMarker.class, args = "getfield")
    public static void beforeFieldRead(final FieldAccessStaticContext sc, final DynamicContext dc, final ClassContext cc) {

        AREDispatch.NativeLog (cc.asClass(sc.getOwner())+ sc.thisMethodFullName());
    	final Object object = dc.getStackValue(0, Object.class);

    	if(object != null) {
    		ImmutabilityAnalysisRE.onFieldRead(object, cc.asClass(sc.getOwner()), sc.getFieldId());
    	}
    }

    @Before(marker = BytecodeMarker.class, args = "putfield")
    public static void beforeFieldWrite(final FieldAccessStaticContext sc, final DynamicContext dc, final ClassContext cc) {
        AREDispatch.NativeLog (cc.asClass(sc.getOwner())+ sc.thisMethodFullName());
    	final Object object = dc.getStackValue(1, Object.class);

        if(object != null) {
        	ImmutabilityAnalysisRE.onFieldWrite(object, cc.asClass(sc.getOwner()), sc.getFieldId());
        }
    }

    /** SHADOW HEAP **************************************************************/

    /**
     * Instruments the allocation of a single object (<code>new</code>)
     *
     * Note that this snippet requires that bytecode verification is switched off (<code>-noverify</code>) as the newly
     * created instances are passed to the runtime <em>prior</em> to initialization.
     */
    @AfterReturning(marker = BytecodeMarker.class, args = "new")
    public static void objectAllocated(final ClassContext cc, final DynamicContext dc, final AllocationSiteStaticContext sc, final MethodStaticContext msc) {
        ImmutabilityAnalysisRE.onObjectAllocation(dc.getStackValue(0, Object.class), sc.getAllocationSite());
    }

    /**
     * Instruments the reflective allocation of a single object through a call to
     * {@link java.lang.reflect.Constructor#newInstance(Object...) Constructor.newInstance(Object...)}.
     *
     * Note that this method assumes that {@link Class#newInstance()} internally delegates to
     * {@code Constructor.newInstance(Object...)}; hence, it only instruments the latter. This assumption is true under
     * OpenJDK and possibly other JREs.
     */
    @AfterReturning(marker = BodyMarker.class,
            scope = "java.lang.Object java.lang.reflect.Constructor.newInstance(java.lang.Object[])")
    public static void objectAllocatedThroughReflection(final DynamicContext dc, final AllocationSiteStaticContext sc) {
        //AREDispatch.NativeLog (sc.thisMethodFullName() +"\t"+ sc.thisMethodDescriptor());
        ImmutabilityAnalysisRE.onObjectAllocation(dc.getStackValue(0, Object.class), sc.getReflectiveAllocationSite());
    }

}
