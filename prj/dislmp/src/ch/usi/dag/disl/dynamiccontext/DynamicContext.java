package ch.usi.dag.disl.dynamiccontext;

/**
 * <p>
 * Provides access to dynamic information available at runtime at the location
 * where the snippet is inlined.
 * 
 * <p>
 * <ul>
 * <li>{@link #getThis()}</li>
 * <li>{@link #getException()}</li>
 * <li>{@link #getStackValue(int, Class)}</li>
 * <li>{@link #getMethodArgumentValue(int, Class)}</li>
 * <li>{@link #getLocalVariableValue(int, Class)}</li>
 * </ul>
 */
public interface DynamicContext {

    /**
     * <p>
     * Returns {@code this} reference to snippets inlined in an instance method,
     * {@code null} for snippets inlined in a static method.
     */
    Object getThis();

    /**
     * <p>
     * Returns the exception reference to snippets inlined in the @After or the @AfterThrowing
     * context, {@code null} otherwise.
     */
    Throwable getException();

    /**
     * <p>
     * Returns the value of a particular item on the JVM operand stack.
     * 
     * <p>
     * <b>Note:</b> Each item index corresponds to one operand on the stack.
     * Both primitive and wide values are considered to be a single item, i.e.,
     * the index of the corresponding stack slot is determined automatically.
     * 
     * @param itemIndex
     *            <p>
     *            index of the item on the operand stack, must be positive and
     *            not exceed the number of items on the stack. Index {@code 0}
     *            refers to an item at the top of the stack.
     * @param valueType
     *            <p>
     *            the expected type of the accessed value. Primitive types are
     *            boxed into corresponding reference types.
     * @return
     *         <p>
     *         The value of the selected stack item. Primitive types are boxed
     *         into corresponding reference types.
     */
    <T> T getStackValue(int itemIndex, Class<T> valueType);

    /**
     * <p>
     * Returns the value of a particular method argument.
     * 
     * <p>
     * <b>Note:</b> Each argument index corresponds to one method argument, be
     * it primitive or wide, i.e., the index of the corresponding local variable
     * slot is determined automatically.
     * 
     * @param argumentIndex
     *            <p>
     *            index of the desired method argument, must be positive and not
     *            exceed the number of method arguments. Index {@code 0} refers
     *            to the first argument.
     * @param valueType
     *            <p>
     *            the expected type of the accessed value.
     * @return
     *         <p>
     *         The value of the selected method argument. Primitive types are
     *         boxed into corresponding reference types.
     */
    <T> T getMethodArgumentValue(int argumentIndex, Class<T> valueType);

    /**
     * <p>
     * Returns the value of a local variable occupying a particular local
     * variable slot (or two slots, in case of wide types such as long and
     * double).
     * 
     * <p>
     * <b>Note:</b> Each slot index corresponds to one local variable slot. The
     * value of wide values is obtained from two consecutive local variable
     * slots, starting with the given slot index.
     * 
     * @param slotIndex
     *            <p>
     *            index of the desired local variable slot, must be positive and
     *            not exceed the number of local variable slots. Index {@code 0}
     *            refers to the first local variable slot.
     * @param valueType
     *            <p>
     *            the expected type of the accessed value.
     * @return
     *         <p>
     *         The value of the selected local variable slot. Primitive types
     *         are boxed into corresponding reference types.
     */
    <T> T getLocalVariableValue(int slotIndex, Class<T> valueType);

    // TODO enable when properly implemented
    // <T> T getStaticFieldValue (
    // String ownerClass,
    // String fieldName, String fieldDesc, Class<T> fieldType
    // );

    // <T> T getInstanceFieldValue (
    // Object instance, String ownerClass,
    // String fieldName, String fieldDesc, Class<T> fieldType
    // );

}
