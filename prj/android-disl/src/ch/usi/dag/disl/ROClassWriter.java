package ch.usi.dag.disl;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ROClassWriter extends ClassWriter {

    public ROClassWriter(ClassReader classReader, int flags) {
        super(classReader, flags);
    }

    public ROClassWriter(int flags) {
        super(flags);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        // same string as in super class
        return "java/lang/Object";
    }
}
