package ch.usi.dag.disl.annotation;

import ch.usi.dag.disl.marker.Marker;

/**
 * <p>
 * The AfterReturning annotation instructs DiSL to insert the snippet body after
 * the marked region. The snippet will be invoked after a normal exit of the
 * region.
 * 
 * <p>
 * <b>note:</b> This is only general contract. It depends on particular marker
 * how the contract will be implemented.
 * 
 * <p>
 * <b>usage:</b>
 * 
 * <p>
 * There are multiple optional parameters the annotation takes. Browse them for
 * more details on usage.
 * <ul>
 * <li>{@link #marker}</li>
 * <li>{@link #args}</li>
 * <li>{@link #guard}</li>
 * <li>{@link #scope}</li>
 * <li>{@link #order}</li>
 * <li>{@link #dynamicBypass}</li>
 * </ul>
 * 
 * <p>
 * This annotation should be used only with static methods that does not return
 * any value or throw any exception.
 * 
 * <p>
 * The method might be specified with arguments of following types.
 * {@link ch.usi.dag.disl.staticcontext.StaticContext StaticContext (or
 * derived)}, {@link ch.usi.dag.disl.dynamiccontext.DynamicContext
 * DynamicContext}, {@link ch.usi.dag.disl.classcontext.ClassContext
 * ClassContext} and
 * {@link ch.usi.dag.disl.processorcontext.ArgumentProcessorContext
 * ArgumentProcessContext}. There's no restriction on order or number of these
 * arguments.
 */
public @interface AfterReturning {

    // NOTE if you want to change names, you need to change
    // SnippetParser.SnippetAnnotationData class

    // NOTE because of implementation of annotations in java the defaults
    // are not retrieved from here but from class mentioned above

    /**
     * <p>
     * Marker class defines a region where the snippet is applied.
     * 
     * @see ch.usi.dag.disl.marker.Marker Implementation details
     */
    Class<? extends Marker> marker();

    /**
     * <p>
     * Argument for the marker (as string).
     * 
     * <p>
     * Default value means none.
     */
    String args() default ""; // cannot be null :(

    /**
     * <p>
     * Scope of the methods where the snippet is applied.
     * 
     * <p>
     * Default value means everything.
     * 
     * @see ch.usi.dag.disl.scope.ScopeImpl Implementation details
     */
    String scope() default "*";

    /**
     * <p>
     * The guard class defining if the snippet will be inlined in particular
     * region or not.
     * 
     * <p>
     * Default value means none.
     */
    Class<? extends Object> guard() default Object.class; // cannot be null :(

    /**
     * <p>
     * Defines ordering of the snippets. Smaller number indicates that snippet
     * will be inlined closer to the instrumented code.
     * 
     * <p>
     * Default is 100.
     */
    int order() default 100;

    /**
     * <p>
     * You can in general disable dynamic bypass on snippets that are not using
     * any other class. (Advanced option)
     * 
     * <p>
     * <b>note:</b> Usage of dynamic bypass is determined by the underlying
     * instrumentation framework.
     */
    boolean dynamicBypass() default true;
}
