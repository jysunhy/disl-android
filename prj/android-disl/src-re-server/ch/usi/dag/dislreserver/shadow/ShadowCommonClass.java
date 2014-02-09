package ch.usi.dag.dislreserver.shadow;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.dislreserver.exception.DiSLREServerFatalException;

class ShadowCommonClass extends ShadowClass {

    // TODO ! is this implementation of methods really working ??

    private final ShadowClass superClass;
    private ClassNode   classNode;

    private int         access;
    private String      name;

    ShadowCommonClass(final long net_ref, final String classSignature,
            final ShadowObject classLoader, final ShadowClass superClass, byte[] classCode) {
        super(net_ref, classLoader);

        this.superClass = superClass;
        name = classSignature.replace('/', '.');
        if(name.startsWith("L")) {
            name = name.substring(1);
        }
        if(name.endsWith(";")) {
            name = name.substring(0, name.length()-1);
        }

        //HAIYANG current empty
        System.out.println ("Getting class code:" +name);
        if(false) {
            return;
        }
        if (classCode == null || classCode.length == 0) {
            try {
                final Socket socket = new Socket(InetAddress.getByName (null), 6666);
                DataOutputStream os;
                DataInputStream is;

                os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

                os.writeInt(name.length ());
                os.writeInt(0);

                os.write(name.getBytes ());
                //os.write(nm.getClassCode());
                os.flush();
                final int controlLength = is.readInt();
                final int classCodeLength = is.readInt();

                // allocate buffer for class reading
                final byte[] control = new byte[controlLength];
                classCode = new byte[classCodeLength];

                // read class
                is.readFully(control);
                is.readFully(classCode);
            } catch (final UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //throw new DiSLREServerFatalException("Creating class info for "
            //        + classSignature + " with no code provided");
        }
        if(classCode==null) {
            System.out.println ("Getting class code of" +name+ ": NULL");
        }
        initializeClassInfo(classCode);
    }

    private List<MethodInfo> methods;
    private List<MethodInfo> public_methods;
    private List<FieldInfo>  fields;
    private List<FieldInfo>  public_fields;
    private List<String>     innerclasses;

    private void initializeClassInfo(final byte[] classCode) {

        final ClassReader classReader = new ClassReader(classCode);
        classNode = new ClassNode(Opcodes.ASM4);
        classReader.accept(classNode, ClassReader.SKIP_DEBUG
                | ClassReader.EXPAND_FRAMES);

        access = classNode.access;
        name = classNode.name.replace('/', '.');

        methods = new ArrayList<MethodInfo>(classNode.methods.size());
        public_methods = new LinkedList<MethodInfo>();

        for (final MethodNode methodNode : classNode.methods) {

            final MethodInfo methodInfo = new MethodInfo(methodNode);
            methods.add(methodInfo);

            if (methodInfo.isPublic()) {
                public_methods.add(methodInfo);
            }
        }

        fields = new ArrayList<FieldInfo>(classNode.fields.size());
        public_fields = new LinkedList<FieldInfo>();

        for (final FieldNode fieldNode : classNode.fields) {

            final FieldInfo fieldInfo = new FieldInfo(fieldNode);
            fields.add(fieldInfo);

            if (fieldInfo.isPublic()) {
                public_fields.add(fieldInfo);
            }
        }

        if (getSuperclass() != null) {

            for (final MethodInfo methodInfo : getSuperclass().getMethods()) {
                public_methods.add(methodInfo);
            }

            for (final FieldInfo fieldInfo : getSuperclass().getFields()) {
                public_fields.add(fieldInfo);
            }
        }

        innerclasses = new ArrayList<String>(classNode.innerClasses.size());

        for (final InnerClassNode innerClassNode : classNode.innerClasses) {
            innerclasses.add(innerClassNode.name);
        }
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public ShadowClass getComponentType() {
        return null;
    }

    @Override
    public boolean isInstance(final ShadowObject obj) {
        // return equals(obj.getSClass());
        throw new DiSLREServerFatalException(
                "ShadowCommonClass.isInstance not implemented");
    }

    @Override
    public boolean isAssignableFrom(final ShadowClass klass) {
        // while (klass != null) {
        //
        // if (klass.equals(this)) {
        // return true;
        // }
        //
        // klass = klass.getSuperclass();
        // }
        //
        // return false;
        throw new DiSLREServerFatalException(
                "ShadowCommonClass.isAssignableFrom not implemented");
    }

    @Override
    public boolean isInterface() {
        return (access & Opcodes.ACC_INTERFACE) != 0;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isAnnotation() {
        return (access & Opcodes.ACC_ANNOTATION) != 0;
    }

    @Override
    public boolean isSynthetic() {
        return (access & Opcodes.ACC_SYNTHETIC) != 0;
    }

    @Override
    public boolean isEnum() {
        return (access & Opcodes.ACC_ENUM) != 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCanonicalName() {
        throw new DiSLREServerFatalException(
                "ShadowCommonClass.getCanonicalName not implemented");
    }

    @Override
    public String[] getInterfaces() {
        return classNode.interfaces.toArray(new String[0]);
    }

    @Override
    public String getPackage() {

        final int i = name.lastIndexOf('.');

        if (i != -1) {
            return name.substring(0, i);
        } else {
            return null;
        }
    }

    @Override
    public ShadowClass getSuperclass() {
        return superClass;
    }

    @Override
    public FieldInfo[] getFields() {

        // to have "checked" array :(
        return public_fields.toArray(new FieldInfo[0]);
    }

    @Override
    public FieldInfo getField(final String fieldName) throws NoSuchFieldException {

        for (final FieldInfo fieldInfo : fields) {
            if (fieldInfo.isPublic() && fieldInfo.getName().equals(fieldName)) {
                return fieldInfo;
            }
        }

        if (getSuperclass() == null) {
            throw new NoSuchFieldException(name + "." + fieldName);
        }

        return getSuperclass().getField(fieldName);
    }

    @Override
    public MethodInfo[] getMethods() {

        // to have "checked" array :(
        return public_methods.toArray(new MethodInfo[0]);
    }

    @Override
    public MethodInfo getMethod(final String methodName, final String[] argumentNames)
            throws NoSuchMethodException {

        for (final MethodInfo methodInfo : public_methods) {
            if (methodName.equals(methodInfo.getName())
                    && Arrays.equals(argumentNames,
                            methodInfo.getParameterTypes())) {
                return methodInfo;
            }
        }

        throw new NoSuchMethodException(name + "." + methodName
                + argumentNamesToString(argumentNames));
    }

    @Override
    public FieldInfo[] getDeclaredFields() {
        return fields.toArray(new FieldInfo[0]);
    }

    @Override
    public FieldInfo getDeclaredField(final String fieldName)
            throws NoSuchFieldException {

        for (final FieldInfo fieldInfo : fields) {
            if (fieldInfo.getName().equals(fieldName)) {
                return fieldInfo;
            }
        }

        throw new NoSuchFieldException(name + "." + fieldName);
    }

    @Override
    public MethodInfo[] getDeclaredMethods() {
        return methods.toArray(new MethodInfo[methods.size()]);
    }

    @Override
    public String[] getDeclaredClasses() {
        return innerclasses.toArray(new String[innerclasses.size()]);
    }

    @Override
    public MethodInfo getDeclaredMethod(final String methodName,
            final String[] argumentNames) throws NoSuchMethodException {

        for (final MethodInfo methodInfo : methods) {
            if (methodName.equals(methodInfo.getName())
                    && Arrays.equals(argumentNames,
                            methodInfo.getParameterTypes())) {
                return methodInfo;
            }
        }

        throw new NoSuchMethodException(name + "." + methodName
                + argumentNamesToString(argumentNames));
    }

}
