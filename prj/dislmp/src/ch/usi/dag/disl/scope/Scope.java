package ch.usi.dag.disl.scope;

/**
 * Interface for matching snippet scope.
 * 
 * Scope implementation has to be thread-safe.
 */
public interface Scope {

    /**
     * The implementation should return true if the className, methodName and
     * methodDesc matches the contract of the scope. False otherwise.
     */
    boolean matches(String className, String methodName, String methodDesc);
}
