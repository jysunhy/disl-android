package ch.usi.dag.icc.disl;

import java.util.Stack;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.ThreadLocal;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.icc.analysis.ICCAnalysisStub;
import android.widget.Toast;

public class PermissionDiSLClass {
	//@ThreadLocal
	//	static Stack<Integer> permission_stk;

	@Before (
			marker = BodyMarker.class,
			scope = "*.check*Permission*")
		public static void api_0 (
				final CallContext msc, final ArgumentProcessorContext pc) {
			AREDispatch.NativeLog (msc.thisMethodFullName ());
			final Object [] args = pc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
			final String tabs = "\t\t";
			int api = 0;
			for (final Object obj : args) {
				if(obj == null){
					AREDispatch.NativeLog(tabs+"null");
					continue;
				}

				final String n = obj.getClass().getCanonicalName();
				if (obj instanceof Object[]){
					AREDispatch.NativeLog(tabs+n);
					//final Object[] arr = (Object[])obj;
				}else{


					if(n.equals ("java.lang.Integer")) {
						AREDispatch.NativeLog(tabs+obj.toString());
					}else if(n.equals ("java.lang.Float")) {
						AREDispatch.NativeLog(tabs+obj.toString());
					}else if(n.equals ("java.lang.Double")) {
						AREDispatch.NativeLog(tabs+obj.toString());
					}else if(n.equals ("java.lang.String")) {
						final String permissions[] =
							new String[]{
									"android.permission.WRITE_SMS"
									,"android.permission.SEND_SMS"
									,"android.permission.ACCESS_ALL_DOWNLOADS"
									,"android.permission.ACCESS_COARSE_LOCATION"
									,"android.permission.ACCESS_DOWNLOAD_MANAGER"
									,"android.permission.ACCESS_FINE_LOCATION"
									,"android.permission.ACCESS_NETWORK_STATE"
									,"android.permission.ACCESS_WIFI_STATE"
									,"android.permission.CHANGE_COMPONENT_ENABLED_STATE"
									,"android.permission.CHANGE_CONFIGURATION"
									,"android.permission.CHANGE_WIFI_STATE"
									,"android.permission.COPY_PROTECTED_DATA"
									,"android.permission.DEVICE_POWER"
									,"android.permission.GET_ACCOUNTS"
									,"android.permission.INSTALL_PACKAGES"
									,"android.permission.INTERNAL_SYSTEM_WINDOW"
									,"android.permission.INTERNET"
									,"android.permission.MANAGE_APP_TOKENS"
									,"android.permission.MODIFY_AUDIO_SETTINGS"
									,"android.permission.MODIFY_PHONE_STATE"
									,"android.permission.PACKAGE_USAGE_STATS"
									,"android.permission.READ_CALENDAR"
									,"android.permission.READ_CALL_LOG"
									,"android.permission.READ_CONTACTS"
									,"android.permission.READ_PHONE_STATE"
									,"android.permission.READ_SMS"
									,"android.permission.READ_USER_DICTIONARY"
									,"android.permission.START_ANY_ACTIVITY"
									,"android.permission.UPDATE_DEVICE_STATS"
									,"android.permission.WRITE_APN_SETTINGS"
									,"android.permission.WRITE_SECURE_SETTINGS"
									,"android.permission.WRITE_SETTINGS"

									,"android.permission.STATUS_BAR"
									,"android.permission.STATUS_BAR_SERVICE"
									,"android.permission.READ_FRAME_BUFFER"
									,"android.permission.BIND_INPUT_METHOD"
									,"android.permission.BIND_WALLPAPER"
									,"android.permission.CONNECTIVITY_INTERNAL"
									,"android.permission.BACKUP"
									,"android.permission.RECEIVE_BOOT_COMPLETED"
									,"android.permission.SET_TIME_ZONE"
									,"android.permission.SET_WALLPAPER_HINTS"
									,"android.permission.ACCESS_SURFACE_FLINGER"
									,"android.permission.VIBRATE"
									,"android.permission.WAKE_LOCK"
									,"android.permission.BROADCAST_PACKAGE_REMOVED"
									,"android.permission.BROADCAST_STICKY"
									,"android.permission.ASEC_ACCESS"
							};
						int pos = 0;
						for (final String p : permissions) {
							if(obj.toString ().equals (p)) {
								if(pos >= 31)
									api = 1<<31;
								else
									api = 1<<pos;
								break;
							}
							pos++;
						}
						AREDispatch.NativeLog(tabs+obj.toString());
					}else {
						AREDispatch.NativeLog (tabs+n);
					}
				}
			}
			AREDispatch.CallAPI (api);
		}

	@Before (
			marker = BodyMarker.class,
			//scope = "com.android.browser.*.*",
			scope = "*.*",
			guard = ScopeGuard.class)
		public static void before_enter (final CallContext msc){
	//		if(permission_stk==null)
	//			permission_stk = new Stack <Integer> ();
	//		permission_stk.push (0);
			if(msc.thisClassSuperName ().contains ("Activity")) {
				AREDispatch.methodEnter ();
			}
		}

	@After (
			marker = BodyMarker.class,
			//		scope = "com.android.browser.*.*",
			scope = "*.*",
			guard = ScopeGuard.class)
		public static void after_enter (final CallContext msc){
			final int permission = AREDispatch.checkThreadPermission ();
			if(permission != 0) {
				final String permissions[] = 
					new String[]{
									"android.permission.WRITE_SMS"
									,"android.permission.SEND_SMS"
									,"android.permission.ACCESS_ALL_DOWNLOADS"
									,"android.permission.ACCESS_COARSE_LOCATION"
									,"android.permission.ACCESS_DOWNLOAD_MANAGER"
									,"android.permission.ACCESS_FINE_LOCATION"
									,"android.permission.ACCESS_NETWORK_STATE"
									,"android.permission.ACCESS_WIFI_STATE"
									,"android.permission.CHANGE_COMPONENT_ENABLED_STATE"
									,"android.permission.CHANGE_CONFIGURATION"
									,"android.permission.CHANGE_WIFI_STATE"
									,"android.permission.COPY_PROTECTED_DATA"
									,"android.permission.DEVICE_POWER"
									,"android.permission.GET_ACCOUNTS"
									,"android.permission.INSTALL_PACKAGES"
									,"android.permission.INTERNAL_SYSTEM_WINDOW"
									,"android.permission.INTERNET"
									,"android.permission.MANAGE_APP_TOKENS"
									,"android.permission.MODIFY_AUDIO_SETTINGS"
									,"android.permission.MODIFY_PHONE_STATE"
									,"android.permission.PACKAGE_USAGE_STATS"
									,"android.permission.READ_CALENDAR"
									,"android.permission.READ_CALL_LOG"
									,"android.permission.READ_CONTACTS"
									,"android.permission.READ_PHONE_STATE"
									,"android.permission.READ_SMS"
									,"android.permission.READ_USER_DICTIONARY"
									,"android.permission.START_ANY_ACTIVITY"
									,"android.permission.UPDATE_DEVICE_STATS"
									,"android.permission.WRITE_APN_SETTINGS"
									,"android.permission.WRITE_SECURE_SETTINGS"
									,"android.permission.WRITE_SETTINGS"

									,"android.permission.STATUS_BAR"
									,"android.permission.STATUS_BAR_SERVICE"
									,"android.permission.READ_FRAME_BUFFER"
									,"android.permission.BIND_INPUT_METHOD"
									,"android.permission.BIND_WALLPAPER"
									,"android.permission.CONNECTIVITY_INTERNAL"
									,"android.permission.BACKUP"
									,"android.permission.RECEIVE_BOOT_COMPLETED"
									,"android.permission.SET_TIME_ZONE"
									,"android.permission.SET_WALLPAPER_HINTS"
									,"android.permission.ACCESS_SURFACE_FLINGER"
									,"android.permission.VIBRATE"
									,"android.permission.WAKE_LOCK"
									,"android.permission.BROADCAST_PACKAGE_REMOVED"
									,"android.permission.BROADCAST_STICKY"
									,"android.permission.ASEC_ACCESS"
					};
				String res = "";
				for(int i = 0; i < 32; i++){
					if((permission & (1<<i)) != 0) {
						if(i==31)
							res+="Others;";
						else
							res+=permissions[i]+";";
					}
				}
				AREDispatch.NativeLog("ALERT: "+msc.thisMethodFullName()+" "+res);
				//Toast.makeText(null, msc.thisMethodFullName()+" "+res , 1).show();
				//System.out.println(msc.thisMethodFullName()+" "+res);
				ICCAnalysisStub.permission_alert (msc.thisMethodFullName ()+" "+res);
			}
			AREDispatch.methodExit ();

	//		if(permission_stk==null)
	//			permission_stk = new Stack <Integer> ();
	//		final int temp = permission_stk.pop ();
	//		if(temp!=permission){
	//			AREDispatch.NativeLog ("NOT CONSISTENT AT "+ msc.thisMethodFullName ());
	//		}
		}
}
