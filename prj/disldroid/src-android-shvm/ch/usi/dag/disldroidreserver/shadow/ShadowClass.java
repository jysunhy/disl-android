package ch.usi.dag.disldroidreserver.shadow;

import ch.usi.dag.disldroidreserver.shadow.ref.FieldInfo;
import ch.usi.dag.disldroidreserver.shadow.ref.MethodInfo;

public abstract class ShadowClass extends ShadowObject {

    private final int          classId;
    private ShadowObject classLoader;

    //

    protected ShadowClass (final ShadowAddressSpace currentAddressSpace,
        final long netReference, final ShadowObject classLoader) {
        super (
            currentAddressSpace, netReference, null);

        this.classId = NetReferenceHelper.get_class_id (netReference);
        this.classLoader = classLoader;
    }

    //

    // No need to expose the interface to user
    // getId() should be sufficient
    protected final int getClassId() {
        return classId;
    }

    public final ShadowObject getShadowClassLoader() {
        return classLoader;
    }

    public abstract boolean isArray();

    public abstract ShadowClass getComponentType();

    public abstract boolean isInstance(ShadowObject obj);

    public abstract boolean isAssignableFrom(ShadowClass klass);

    public abstract boolean isInterface();

    public abstract boolean isPrimitive();

    public abstract boolean isAnnotation();

    public abstract boolean isSynthetic();

    public abstract boolean isEnum();

    public abstract String getName();

    public abstract String getCanonicalName();

    public abstract String[] getInterfaces();

    public abstract String getPackage();

    public abstract ShadowClass getSuperclass();

    @Override
    public boolean equals(final Object obj) {

        if (!(obj instanceof ShadowClass)) {
            return false;
        }

        final ShadowClass sClass = (ShadowClass) obj;

        if (getName().equals(sClass.getName())
                && getShadowClassLoader().equals(sClass.getShadowClassLoader())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("overriden equals, not overriden hashCode");
    }

    public abstract FieldInfo[] getFields();

    public abstract FieldInfo getField(String fieldName)
            throws NoSuchFieldException;

    public abstract MethodInfo[] getMethods();

    public abstract MethodInfo getMethod(String methodName,
            String[] argumentNames) throws NoSuchMethodException;

    public abstract String[] getDeclaredClasses();

    public abstract FieldInfo[] getDeclaredFields();

    public abstract FieldInfo getDeclaredField(String fieldName)
            throws NoSuchFieldException;

    public abstract MethodInfo[] getDeclaredMethods();

    public abstract MethodInfo getDeclaredMethod(String methodName,
            String[] argumentNames) throws NoSuchMethodException;

    public MethodInfo getMethod(final String methodName, final ShadowClass[] arguments)
            throws NoSuchMethodException {
        return getMethod(methodName, classesToStrings(arguments));
    }

    public MethodInfo getDeclaredMethod(final String methodName,
            final ShadowClass[] arguments) throws NoSuchMethodException {
        return getDeclaredMethod(methodName, classesToStrings(arguments));
    }

    protected static String[] classesToStrings(final ShadowClass[] arguments) {

        if (arguments == null) {
            return new String[0];
        }

        final int size = arguments.length;
        final String[] argumentNames = new String[size];

        for (int i = 0; i < size; i++) {
            argumentNames[i] = arguments[i].getName();
        }

        return argumentNames;
    }

    protected static String argumentNamesToString(final String[] argumentNames) {
        final StringBuilder buf = new StringBuilder();
        buf.append("(");

        if (argumentNames != null) {

            for (int i = 0; i < argumentNames.length; i++) {

                if (i > 0) {
                    buf.append(", ");
                }

                buf.append(argumentNames[i]);
            }
        }

        buf.append(")");
        return buf.toString();
    }

    @Override
    void onFork (final ShadowAddressSpace shadowAddressSpace) {
        super.onFork (shadowAddressSpace);
        classLoader = shadowAddressSpace.getClonedShadowObject (classLoader);
    }

}
