package ch.usi.dag.icc.disl;

import android.app.Activity;
import android.content.Intent;
import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.marker.BytecodeMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.icc.analysis.ICCAnalysisStub;

public class PermissionDiSLClass {
	@Before (
			marker = BodyMarker.class,
			scope = "*.*",
			guard = ScopeGuard.class)
		public static void before_enter (final CallContext msc){
			AREDispatch.methodEnter ();
		}

	@After (
			marker = BodyMarker.class,
			scope = "*.*",
			guard = ScopeGuard.class)
		public static void after_enter (final CallContext msc){
			final int permission = AREDispatch.checkThreadPermission ();
			if(permission != 0) {
				ICCAnalysisStub.permission_alert (permission, msc.thisMethodFullName());
			}
			AREDispatch.methodExit ();
		}

	@Before (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = StartActivityForResultGuard.class)
    public static void startActivityForResult (final CallContext ac, final ArgumentProcessorContext pc) {
        final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        AREDispatch.NativeLog ("in start activity for result test");
        AREDispatch.NativeLog (Integer.toString (args.length));
        final android.content.Intent intent = (Intent)args[0];
        intent.putExtra ("specialtag", "123");
    }

	@Before (
        marker = BytecodeMarker.class,
        args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
        guard = SetActivityResultGuard.class)
    public static void setActivityResult (final CallContext ac, final ArgumentProcessorContext pc) {
        final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
        AREDispatch.NativeLog ("in set activity result");
        AREDispatch.NativeLog (Integer.toString (args.length));
        final android.content.Intent intent = (Intent)args[1];
        intent.putExtra ("specialtag", "456");
    }

	@After (
        marker = BodyMarker.class,
        scope = "*.onCreate")
    public static void onActivityCreate (final DynamicContext dc, final CallContext ac) {
        AREDispatch.NativeLog ("in activity on create");
        final Intent intent = dc.getLocalVariableValue (0, Activity.class).getIntent ();
        if(intent.hasExtra("specialtag")) {
            final String tag = intent.getExtras().getString("specialtag");
            if(tag !=null){
                AREDispatch.NativeLog ("HAHA get the special tag "+tag);
            }
        }
    }

	@Before (
        marker = BodyMarker.class,
        scope = "*.onActivityResult")
    public static void onActivityResult (final CallContext ac, final ArgumentProcessorContext pc) {
        AREDispatch.NativeLog ("in on activity result");
        final Object [] args = pc.getArgs (ArgumentProcessorMode.METHOD_ARGS);
        if(args.length==3){
            final Intent intent = (Intent)args[2];
            if(intent.hasExtra("specialtag")) {
                final String tag = intent.getExtras().getString("specialtag");
                if(tag !=null){
                    AREDispatch.NativeLog ("HAHA get the special tag "+tag);
                }
            }
        }
    }

	@Before (
			marker = BytecodeMarker.class,
			args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual")
		public static void beforeInvoke (final CallContext ac) {
			final String methodName = ac.getCallee ();
			AREDispatch.methodEnter ();
	//		AREDispatch.NativeLog ("Before method call"+methodName);
		}

	@AfterReturning (
			marker = BytecodeMarker.class,
			args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual")
		public static void afterInvoke (final CallContext ac) {
			final String methodName = ac.getCallee ();
			final int permission = AREDispatch.checkThreadPermission ();
			if(permission != 0) {
				ICCAnalysisStub.permission_alert (permission, methodName);
			}
			AREDispatch.methodExit ();
		//	AREDispatch.NativeLog ("After method call"+methodName);
		}

	@AfterReturning (
			marker = BytecodeMarker.class,
			args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
			guard = DynamicGuard.class)
		public static void afterDynamicInvoke (final CallContext ac,final ArgumentProcessorContext pc) {
	        final Object [] args = pc.getArgs (ArgumentProcessorMode.CALLSITE_ARGS);
	        String arg="";
	        for (final Object obj : args) {
	            if(obj == null){
                    continue;
                }
                final String n = obj.getClass().getCanonicalName();
                if (obj instanceof Object[]){
                    continue;
                }else{
                    if(n.equals ("java.lang.Integer")) {
                    }else if(n.equals ("java.lang.Float")) {
                    }else if(n.equals ("java.lang.Double")) {
                    }else if(n.equals ("java.lang.String")) {
                        arg = obj.toString ();
                        break;
                    }
                }
	        }
			final String methodName = ac.getCallee ();
			ICCAnalysisStub.dynamic_alert (methodName, ac.thisMethodFullName(), arg);
		}

	@AfterReturning (
			marker = BytecodeMarker.class,
			args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
			guard = SourceGuard.class)
		public static void afterSourceInvoke (final CallContext ac) {
			final String methodName = ac.getCallee ();
			ICCAnalysisStub.source_alert (methodName, ac.thisMethodFullName());
		}

	@AfterReturning (
			marker = BytecodeMarker.class,
			args = "invokestatic, invokespecial, invokestatic, invokeinterface, invokevirtual",
			guard = SinkGuard.class)
		public static void afterSinkInvoke (final CallContext ac) {
			final String methodName = ac.getCallee ();
			ICCAnalysisStub.sink_alert (methodName, ac.thisMethodFullName());
		}

	@Before (
			marker = BodyMarker.class,
			scope = "*.check*Permission*")
		public static void detectPermission (
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
								if(pos >= 31) {
                                    api = 1<<31;
                                } else {
                                    api = 1<<pos;
                                }
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

}
