package ch.usi.dag.demo.nativecounter.disl;

import java.util.HashMap;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.disldroidserver.Worker;

public class NativeStaticContext extends MethodStaticContext {
    static HashMap<String, Integer> nativeHashMap = new HashMap <String, Integer> ();

    public boolean isNative () {

        final AbstractInsnNode instruction = staticContextData.getRegionStart ();

        if (instruction instanceof MethodInsnNode) {
            final MethodInsnNode min = (MethodInsnNode) instruction;
            final String className = min.owner;
            final String key = className+min.name+min.desc;
            if(nativeHashMap.containsKey (key)){
                final Integer v = nativeHashMap.get (key);
                if(v == null) {
                    return false;
                }
                nativeHashMap.put(key, v+1);
                return true;
            }
//            if(className.startsWith ("L")){
//                className = className.substring (1, className.length () - 1);
//            }
            if(Worker.classNodeMap.containsKey(className)){
//                if(!Worker.classNodeDexName.get (className).equals(Worker.classNodeDexName.get(thisClassName ()))){
//                    //calling native method in another package
//                    System.out.println ("NOT COUNTING EXTERNAL INVOCATION from "+thisClassName()+" to "+className);
//                }
                //calling native method in current package
                final ClassNode cn = Worker.classNodeMap.get(className);
                for(final MethodNode mth : cn.methods){
                    if(mth.name.equals (min.name) && min.desc.equals (mth.desc)){
                        System.out.println ("FOUND NATIVE "+ className+" "+min.name + " "+ min.desc);
                        if((mth.access & Opcodes.ACC_NATIVE) != 0){
                            nativeHashMap.put(key, 1);
                            return true;
                        }else{
                            nativeHashMap.put(key, null);
                        }
                    }
                }
            }else{
                //TODO explain why
                System.out.println ("NOT FOUND "+ className );
                nativeHashMap.put(key, null);
            }
        }
        return false;
    }

    public String getNativeInfoFrom () {
        return getPackge (thisClassName ());
    }
    public String getNativeInfoTo () {
        final AbstractInsnNode instruction = staticContextData.getRegionStart ();
        if (instruction instanceof MethodInsnNode) {
            final MethodInsnNode min = (MethodInsnNode) instruction;
            final String className = min.owner;
            return getPackge(className);
        }
        return null;
    }
    private static String getPackge (final String className) {
        if(!className.contains ("/")) {
            return className;
        }
        return className.substring (0, className.lastIndexOf ("/"));
    }
}
