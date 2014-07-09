package ch.usi.dag.icc.disl;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;


public class CallContext extends MethodStaticContext {

	//    String permissions[] = new String[]{"android.permission.READ_CONTACTS","android.permission.READ_PHONE_STATE"};
	//
	//    public int getPermissionBit(final String permission){
	//        int pos = 0;
	//        for (final String p : permissions) {
	//            if(permission.equals (p)) {
	//                return 1<<pos;
	//            }
	//            pos++;
	//        }
	//        return 0;
	//    }
	//
	//    public String getPermissionString(final int permission){
	//        String res = "";
	//        for(int i = 0; i < 32; i++){
	//            if((permission & (1>>i)) == 1) {
	//                res+=permissions[i]+";";
	//            }
	//        }
	//        return res;
	//    }

	public String getType () {
		final AbstractInsnNode instruction = staticContextData.getRegionStart ();

		if (instruction instanceof TypeInsnNode) {
			return ((TypeInsnNode) instruction).desc;
		}

		return "java/lang/Object";
	}


	public String getCallee () {
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

}
