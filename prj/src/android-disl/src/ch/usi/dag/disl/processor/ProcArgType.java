package ch.usi.dag.disl.processor;

import org.objectweb.asm.Type;

import ch.usi.dag.disl.annotation.ProcessAlso;
import ch.usi.dag.disl.exception.DiSLFatalException;

public enum ProcArgType {

	BOOLEAN(Type.BOOLEAN_TYPE),
	BYTE(Type.BYTE_TYPE),
	CHAR(Type.CHAR_TYPE),
	DOUBLE(Type.DOUBLE_TYPE),
	FLOAT(Type.FLOAT_TYPE),
	INT(Type.INT_TYPE),
	LONG(Type.LONG_TYPE),
	SHORT(Type.SHORT_TYPE),
	OBJECT(Type.getType(Object.class));
	
	private Type asmType;
	
	private ProcArgType(Type asmType) {
		this.asmType = asmType;
	}
	
	public Type getASMType() {
		return asmType;
	}
	
	public static ProcArgType valueOf(Type type) {
		
		if(type == null) {
			throw new DiSLFatalException("Conversion from null not defined");
		}
		
		if(Type.BOOLEAN_TYPE.equals(type)) {
			return BOOLEAN;
		}
		
		if(Type.BYTE_TYPE.equals(type)) {
			return BYTE;
		}
		
		if(Type.CHAR_TYPE.equals(type)) {
			return CHAR;
		}
		
		if(Type.DOUBLE_TYPE.equals(type)) {
			return DOUBLE;
		}
		
		if(Type.FLOAT_TYPE.equals(type)) {
			return FLOAT;
		}
		
		if(Type.INT_TYPE.equals(type)) {
			return INT;
		}
		
		if(Type.LONG_TYPE.equals(type)) {
			return LONG;
		}
		
		if(Type.SHORT_TYPE.equals(type)) {
			return SHORT;
		}
		
		if(Type.OBJECT == type.getSort()) {
			return OBJECT;
		}

		// process arrays as objects
		if(Type.ARRAY == type.getSort()) {
			return OBJECT;
		}
		
		throw new DiSLFatalException("Conversion from " + type.getClassName()
				+ " not defined");
	}
	
	public static ProcArgType valueOf(ProcessAlso.Type type) {
		
		if(type == null) {
			throw new DiSLFatalException("Conversion from null not defined");
		}
		
		switch(type) {
		case BOOLEAN:
			return BOOLEAN;
		case BYTE:
			return BYTE;
		case SHORT:
			return SHORT;
		default:
			throw new DiSLFatalException("Conversion from " + type.toString()
					+ " not defined");
		}
	}
}
