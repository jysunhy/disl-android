package ch.usi.dag.disldroidreserver.shadow;

import java.util.Arrays;

import org.objectweb.asm.Type;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerFatalException;
import ch.usi.dag.disldroidreserver.shadow.ref.FieldInfo;
import ch.usi.dag.disldroidreserver.shadow.ref.MethodInfo;

public class ShadowArrayClass extends ShadowClass {

    private final Type type;

    private ShadowClass superClass;

    private ShadowClass arrayComponentClass;


	//

    ShadowArrayClass (final ShadowAddressSpace currentAddressSpace,
        final long netReference, final ShadowObject classLoader,
        final ShadowClass superClass, final ShadowClass arrayComponentClass,
        final Type type) {
        super (
            currentAddressSpace, netReference, classLoader);

        this.type = type;
        this.superClass = superClass;
        this.arrayComponentClass = arrayComponentClass;
    }


	@Override
	public boolean isArray() {
		return true;
	}

	public int getArrayDimensions() {
		return type.getDimensions();
	}

	@Override
	public ShadowClass getComponentType() {
		// return arrayComponentClass;
		throw new DiSLREServerFatalException(
				"ShadowArrayClass.getComponentType not implemented");
	}

	@Override
	public boolean isInstance(final ShadowObject obj) {
		return equals(obj.getShadowClass());
	}

	@Override
	public boolean isAssignableFrom(final ShadowClass klass) {
		return equals(klass)
				|| ((klass instanceof ShadowArrayClass) && arrayComponentClass
						.isAssignableFrom(klass.getComponentType()));
	}

	@Override
	public boolean isInterface() {
		return false;
	}

	@Override
	public boolean isPrimitive() {
		return false;
	}

	@Override
	public boolean isAnnotation() {
		return false;
	}

	@Override
	public boolean isSynthetic() {
		return false;
	}

	@Override
	public boolean isEnum() {
		return false;
	}

	@Override
	public String getName() {
		return type.getDescriptor().replace('/', '.');
	}

	@Override
	public String getCanonicalName() {
		return type.getClassName();
	}

	@Override
	public String[] getInterfaces() {
		return new String[] { "java.lang.Cloneable", "java.io.Serializable" };
	}

	@Override
	public String getPackage() {
		return null;
	}

	@Override
	public ShadowClass getSuperclass() {
		return superClass;
	}

	@Override
	public FieldInfo[] getFields() {
		return new FieldInfo[0];
	}

	@Override
	public FieldInfo getField(final String fieldName) throws NoSuchFieldException {
		throw new NoSuchFieldException(type.getClassName() + "." + fieldName);
	}

	@Override
	public MethodInfo[] getMethods() {
		return getSuperclass().getMethods();
	}

	@Override
	public MethodInfo getMethod(final String methodName, final String[] argumentNames)
			throws NoSuchMethodException {

		for (final MethodInfo methodInfo : superClass.getMethods()) {
			if (methodName.equals(methodInfo.getName())
					&& Arrays.equals(argumentNames,
							methodInfo.getParameterTypes())) {
				return methodInfo;
			}
		}

		throw new NoSuchMethodException(type.getClassName() + "." + methodName
				+ argumentNamesToString(argumentNames));
	}

	@Override
	public String[] getDeclaredClasses() {
		return new String[0];
	}

	@Override
	public FieldInfo[] getDeclaredFields() {
		return new FieldInfo[0];
	}

	@Override
	public FieldInfo getDeclaredField(final String fieldName)
			throws NoSuchFieldException {
		throw new NoSuchFieldException(type.getClassName() + "." + fieldName);
	}

	@Override
	public MethodInfo[] getDeclaredMethods() {
		return new MethodInfo[0];
	}

	@Override
	public MethodInfo getDeclaredMethod(final String methodName,
			final String[] argumentNames) throws NoSuchMethodException {
		throw new NoSuchMethodException(type.getClassName() + "." + methodName
				+ argumentNamesToString(argumentNames));
	}


    @Override
    void onFork (final ShadowAddressSpace shadowAddressSpace) {
        super.onFork (shadowAddressSpace);
        superClass = (ShadowClass) shadowAddressSpace.getClonedShadowObject (superClass);
        arrayComponentClass = (ShadowClass) shadowAddressSpace.getClonedShadowObject (arrayComponentClass);
    }

}
