package ch.usi.dag.disl.snippet;

import java.lang.reflect.Method;
import java.util.Map;

import org.objectweb.asm.Type;

import ch.usi.dag.disl.exception.ProcessorException;
import ch.usi.dag.disl.exception.ReflectionException;
import ch.usi.dag.disl.exception.StaticContextGenException;
import ch.usi.dag.disl.localvar.LocalVars;
import ch.usi.dag.disl.marker.Marker;
import ch.usi.dag.disl.processor.Proc;
import ch.usi.dag.disl.scope.Scope;

/**
 * Holds all the information about a snippet.
 */
public class Snippet implements Comparable<Snippet> {

	private String originClassName;
	private String originMethodName;
	
	private Class<?> annotationClass;
	private Marker marker;
	private Scope scope;
	private Method guard;
	private int order;
	private SnippetUnprocessedCode unprocessedCode;
	private SnippetCode code;

	/**
	 * Creates snippet structure.
	 */
	public Snippet(String originClassName, String originMethodName,
			Class<?> annotationClass, Marker marker, Scope scope, Method guard,
			int order, SnippetUnprocessedCode unprocessedCode) {
		super();
		this.originClassName = originClassName;
		this.originMethodName = originMethodName;
		this.annotationClass = annotationClass;
		this.marker = marker;
		this.scope = scope;
		this.guard = guard;
		this.order = order;
		this.unprocessedCode = unprocessedCode;
	}

	/**
	 * Get the class name where the snippet is defined.
	 */
	public String getOriginClassName() {
		return originClassName;
	}

	/**
	 * Get the method name where the snippet is defined.
	 */
	public String getOriginMethodName() {
		return originMethodName;
	}

	/**
	 * Get the snippet annotation class.
	 */
	public Class<?> getAnnotationClass() {
		return annotationClass;
	}

	/**
	 * Get the snippet marker.
	 */
	public Marker getMarker() {
		return marker;
	}

	/**
	 * Get the snippet scope.
	 */
	public Scope getScope() {
		return scope;
	}

	/**
	 * Get the snippet guard.
	 */
	public Method getGuard() {
		return guard;
	}
	
	/**
	 * Get the snippet order.
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Get the snippet code.
	 */
	public SnippetCode getCode() {
		return code;
	}

	/**
	 * Compares snippets according to order.
	 */
	public int compareTo(Snippet o) {
		return o.getOrder() - order;
	}

	/**
	 * Initializes snippet -- prepares the snippet code.
	 */
	public void init(LocalVars allLVs, Map<Type, Proc> processors,
			boolean exceptHandler, boolean useDynamicBypass)
			throws StaticContextGenException, ReflectionException,
			ProcessorException {

		code = unprocessedCode.process(allLVs, processors, marker,
				exceptHandler, useDynamicBypass);
		unprocessedCode = null;
	}
}
