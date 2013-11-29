package ch.usi.dag.disl.processorcontext;

/**
 * <p>
 * Allows accessing method arguments and apply argument processors.
 * 
 * <ul>
 * <li>{@link #apply(Class, ArgumentProcessorMode)}</li>
 * <li>{@link #getReceiver(ArgumentProcessorMode)}</li>
 * <li>{@link #getArgs(ArgumentProcessorMode)}</li>
 * </ul>
 */
public interface ArgumentProcessorContext {

    /**
     * <p>
     * Applies mentioned processor for method or call-site arguments.
     * 
     * @param argumentProcessor
     *            processor class to apply
     * @param mode
     *            in which should be processor applied
     */
    void apply(Class<?> argumentProcessor, ArgumentProcessorMode mode);

    /**
     * <p>
     * Returns the object on which is the processed method (arguments of that
     * method) called. Returns null for static methods.
     * 
     * @param mode
     *            for which should be the object retrieved
     */
    Object getReceiver(ArgumentProcessorMode mode);

    /**
     * <p>
     * Returns the object array composed from the method arguments. Note that
     * primitive types will be boxed.
     * 
     * @param mode
     *            for which should be the argument array retrieved
     */
    Object[] getArgs(ArgumentProcessorMode mode);
}
