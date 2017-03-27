package ch.usi.dag.disl.staticcontext;

import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;

import ch.usi.dag.disldroidserver.FolderWorker;


public class DexStaticContext extends MethodStaticContext {

    public static final ConcurrentHashMap <String, ClassNode> classNodeMap = new ConcurrentHashMap <String, ClassNode> ();

	public String getDexName(){
		return FolderWorker.curDex;
	}

	public String getStaticInfo(){
        return getDexShortName ()+":"+thisMethodFullName ();
    }

	public String getDexShortName(){
		final String fullName = getDexName();
		final String shortName = fullName.substring(fullName.lastIndexOf('/')+1);
		return shortName;
	}

	public String getInvocationSignature () {
		final StringBuilder builder = new StringBuilder ();
		final AbstractInsnNode instruction = staticContextData.getRegionStart ();

		if (instruction instanceof MethodInsnNode) {
			final MethodInsnNode min = (MethodInsnNode) instruction;
			builder.append (min.owner);
			builder.append ('.');
			builder.append (min.name);
			builder.append (min.desc);
		}
		return builder.toString ();
	}

	public String getInvocationClass () {
        final StringBuilder builder = new StringBuilder ();
        final AbstractInsnNode instruction = staticContextData.getRegionStart ();

        if (instruction instanceof MethodInsnNode) {
            final MethodInsnNode min = (MethodInsnNode) instruction;
            builder.append (min.owner);
        }
        return builder.toString ();
    }
//
//	public boolean isSelfOrChildOf (final String class1, String class2) {
//	    class2 = plainClassName (class2);
//        if (class1.equals (class2)) {
//            return true;
//        }
//        final ClassNode cn1 = classNodeMap.get (class1);
////        final ClassNode cn2 = classNodeMap.get (class2);
//
//        ClassNode cur = cn1;
//        String superName = "";
//        while (cur != null) {
//            if (cur.superName == null) {
//                return false;
//            }
//            superName = plainClassName (cur.superName);
//            if (superName.equals (class2)) {
//                return true;
//            }
//            cur = classNodeMap.get (superName);
//        }
//        return false;
//    }

	static String plainClassName(final String name){
	    if(name.contains (".")) {
            return name.replace (".", "/");
        }
	    return name;
	}

    public boolean isInterfaceOrChildOf (String class1, String class2) {
        class1 = plainClassName (class1);
        class2 = plainClassName (class2);
        if (class1.equals (class2)) {
            return true;
        }
        final ClassNode cn1 = classNodeMap.get (class1);
        if(cn1 != null) {
            for (final String it : cn1.interfaces) {
                if (it.equals (class2)) {
                    return true;
                }
            }
            if (cn1.superName == null || cn1.superName == "") {
                return false;
            }
            if(cn1.name.equals (cn1.superName)){
                return false;
            }
            return isInterfaceOrChildOf(cn1.superName, class2);

        }else {
            return false;
        }
    }
}
