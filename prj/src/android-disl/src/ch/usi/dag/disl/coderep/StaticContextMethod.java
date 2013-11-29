package ch.usi.dag.disl.coderep;

import java.lang.reflect.Method;

public class StaticContextMethod {

	private String id;
	private Method method;
	private Class<?> referencedClass;
	
	public StaticContextMethod(String id, Method method, 
			Class<?> referencedClass) {
		super();
		this.id = id;
		this.method = method;
		this.referencedClass = referencedClass;
	}

	public String getId() {
		return id;
	}
	
	public Method getMethod() {
		return method;
	}

	public Class<?> getReferencedClass() {
		return referencedClass;
	}
}
