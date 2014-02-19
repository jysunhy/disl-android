package ch.usi.dag.disl.staticcontext;

import ch.usi.dag.disl.snippet.Shadow;

/**
 * <p>
 * The static context provides information derived from code's static analysis.
 * 
 * <p>
 * There is a list of already prepared static contexts.
 * <ul>
 * <li>
 * {@link ch.usi.dag.disl.staticcontext.BasicBlockStaticContext
 * BasicBLockStaticContext - experimental}</li>
 * <li>
 * {@link ch.usi.dag.disl.staticcontext.BytecodeStaticContext
 * BytecodeStaticContext}</li>
 * <li>
 * {@link ch.usi.dag.disl.staticcontext.FieldAccessStaticContext
 * FieldAccessStaticContext}</li>
 * <li>
 * {@link ch.usi.dag.disl.staticcontext.MethodStaticContext
 * MethodStaticContext}</li>
 * <li>
 * {@link ch.usi.dag.disl.staticcontext.LoopStaticContext
 * LoopStaticContext - experimental}</li>
 * </ul>
 * 
 * <p>
 * To implement custom static context all methods should follow convention:
 * <ul>
 * <li>static context methods does not have parameters</li>
 * <li>return value can be only basic type or String</li>
 * <li>implementation has to be thread-safe</li>
 * </ul>
 */
public interface StaticContext {

    /**
     * <p>
     * Receives static context data. Call to this method precedes a static
     * context method invocation.
     */
    void staticContextData(Shadow sa);
}
