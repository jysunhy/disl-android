package ch.usi.dag.taint.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.NetReferenceHelper;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;


public class TaintAnalysis extends RemoteAnalysis {

    public void taint_propagate(final Context context, final ShadowObject from,final ShadowObject to, final ShadowString name, final ShadowString location){
        System.out.println("Proc("+context.pid()+":"+context.getPname ()+") tainted from Object ##"+from.getId()+"## to ##"+to.getId()+"## when invoking " + name + " in "+location);
    }

    public void taint_prepare(final Context context, final ShadowObject from, final ShadowString name, final ShadowString location){
        System.out.println("Proc("+context.pid()+":"+context.getPname ()+") sending Object ##"+from.getId ()+"## when invoking " + name + " in "+location);
    }
    public void taint_propagate2(final Context context, final long from, final int fromPid, final ShadowObject to, final ShadowString name, final ShadowString location){
        System.out.println("Proc("+context.pid()+":"+context.getPname ()+") tainted from Object ##"+NetReferenceHelper.get_object_id (from)+"("+fromPid+")## to ##"+to.getId()+"## when invoking " + name + " in "+location);
    }

	public void taint_sink(final Context context, final ShadowObject obj, final ShadowString name, final ShadowString location){
		System.out.println("Proc("+context.pid()+":"+context.getPname ()+") tainted Object ##"+obj.getId()+"## is leaked when invoking " + name + " in "+location);
	}
	public void taint_source(final Context context, final ShadowObject obj, final int flag, final ShadowString name, final ShadowString location){
		System.out.println("Proc("+context.pid()+":"+context.getPname ()+") Object ##"+obj.getId()+"## is tainted with flag "+flag+" when invoking " + name + " in "+location);
	}


	public void dynamic_alert(final Context context, final ShadowString name, final ShadowString location, final ShadowString args){
		System.out.println("Proc("+context.pid()+":"+context.getPname ()+") Dynamic ##"+name.toString()+"## detected in "+location.toString()+" with args "+args);
	}
	public void source_alert(final Context context, final ShadowString name, final ShadowString location){
		System.out.println("Proc("+context.pid()+":"+context.getPname ()+") Source ##"+name.toString()+"## detected in "+location.toString());
	}
	public void sink_alert(final Context context, final ShadowString name, final ShadowString location){
		System.out.println("Proc("+context.pid()+":"+context.getPname ()+") Sink ##"+name.toString()+"## detected in "+location.toString());
	}
	public void permission_alert(final Context context, final int permission, final ShadowString info){
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
		String res="";
		int cnt=0;
		for(int i = 0; i < 32; i++){
			if((permission & (1<<i)) != 0) {
				if(i==31) {
					res="Others";
				} else {
					res=permissions[i];
				}
				System.out.println("Proc("+context.pid()+"-"+context.getPname ()+")("+cnt+") Permission ##"+res+"## detected in "+info.toString());
				cnt++;
			}
		}
	}

	public void callServiceInClient (
			final ShadowString methodName, final Context context) {
		callService (methodName, context.pid ());
	}

	public void println (final ShadowString methodName) {
		System.out.println (methodName.toString ());
	}

	ConcurrentHashMap <Integer, AtomicInteger> ssCounters = new ConcurrentHashMap <Integer, AtomicInteger> ();

	ConcurrentHashMap <Integer, AtomicInteger> bsCounters = new ConcurrentHashMap <Integer, AtomicInteger> ();

	ConcurrentHashMap <Integer, AtomicInteger> saCounters = new ConcurrentHashMap <Integer, AtomicInteger> ();

	ConcurrentHashMap <Integer, AtomicInteger> scCounters = new ConcurrentHashMap <Integer, AtomicInteger> ();

	ConcurrentHashMap <Integer, AtomicInteger> btCounters = new ConcurrentHashMap <Integer, AtomicInteger> ();

	ConcurrentHashMap <Integer, AtomicInteger> otCounters = new ConcurrentHashMap <Integer, AtomicInteger> ();


	public void increase (
			final Integer caller,
			final ConcurrentHashMap <Integer, AtomicInteger> counters) {
		AtomicInteger counter;

		if ((counter = counters.get (caller)) == null) {
			final AtomicInteger temp = new AtomicInteger ();

			if ((counter = counters.putIfAbsent (caller, temp)) == null) {
				counter = temp;
			}
		}

		counter.incrementAndGet ();
	}


	public void onStartService (final int caller) {
		increase (caller, ssCounters);
	}


	public void onBindService (final int caller) {
		increase (caller, bsCounters);
	}


	public void onStartActivity (final int caller) {
		increase (caller, saCounters);
	}


	public void onServiceConstructor (final Context context) {
		increase (context.pid (), scCounters);
	}


	public void onBinderTransact (final Context context) {
		increase (context.pid (), btCounters);
	}


	public void onBinderOnTransact (final Context context) {
		increase (context.pid (), otCounters);
	}


	public void onCreateService (final int callee, final boolean isIsolated) {
	}


	ConcurrentHashMap <Integer, ConcurrentHashMap <String, AtomicInteger>> allAMSCounters = new ConcurrentHashMap <Integer, ConcurrentHashMap <String, AtomicInteger>> ();


	public void callService (final ShadowString methodName, final int caller) {
		ConcurrentHashMap <String, AtomicInteger> currentAMSCounters;

		if ((currentAMSCounters = allAMSCounters.get (caller)) == null) {
			final ConcurrentHashMap <String, AtomicInteger> temp = new ConcurrentHashMap <String, AtomicInteger> ();

			if ((currentAMSCounters = allAMSCounters.putIfAbsent (caller, temp)) == null) {
				currentAMSCounters = temp;
			}
		}

		final String methodKey = methodName.toString ();
		AtomicInteger counter;

		if ((counter = currentAMSCounters.get (methodKey)) == null) {
			final AtomicInteger temp = new AtomicInteger ();

			if ((counter = currentAMSCounters.put (methodKey, temp)) == null) {
				counter = temp;
			}
		}

		counter.incrementAndGet ();
	}


	public void onCheckPermission (
			final int caller, final ShadowString permission, final Context context) {
		if (caller != context.pid ()) {
			System.out.printf (
					"PROCESS-%d %s: checkPermission %s\n", caller,
					Context.getContext (caller).getPname (),
					permission.toString ());
		}
	}


	public void onGetContentProvider (final int caller, final ShadowString name) {
		System.out.printf (
				"PROCESS-%d %s: getContentProvider %s\n", caller,
				Context.getContext (caller).getPname (),
				name.toString ());
	}


	public void onSystemReady () {
		//        final HashSet <Integer> processes = new HashSet <> ();

		//        processes.addAll (ssCounters.keySet ());
		//        processes.addAll (bsCounters.keySet ());
		//        processes.addAll (saCounters.keySet ());
		//        processes.addAll (scCounters.keySet ());
		//        processes.addAll (btCounters.keySet ());
		//        processes.addAll (otCounters.keySet ());
		//
		//        for (final Integer pid : processes) {
		//            final AtomicInteger ssCounter = ssCounters.get (pid);
		//            final AtomicInteger bsCounter = bsCounters.get (pid);
		//            final AtomicInteger saCounter = saCounters.get (pid);
		//            final AtomicInteger scCounter = scCounters.get (pid);
		//            final AtomicInteger btCounter = btCounters.get (pid);
		//            final AtomicInteger otCounter = otCounters.get (pid);
		//
		//            System.out.printf (
		//                "PROCESS-%d %s: %d %d %d %d %d %d\n", pid,
		//                Context.getContext (pid).getPname (),
		//                ssCounter == null ? 0 : ssCounter.get (),
		//                bsCounter == null ? 0 : bsCounter.get (),
		//                saCounter == null ? 0 : saCounter.get (),
		//                scCounter == null ? 0 : scCounter.get (),
		//                btCounter == null ? 0 : btCounter.get (),
		//                otCounter == null ? 0 : otCounter.get ());
		//        }


		System.out.println ("+++++++++++++++++++++++++++++");

		final ArrayList <Integer> pids = new ArrayList <Integer> (allAMSCounters.keySet ());
		Collections.sort (pids);

		for (final Integer pid : pids) {
			final String name = Context.getContext (pid).getPname ();

			//if (!("dalvikvm".equals (name))) {
			//    continue;
			//}

			final ConcurrentHashMap <String, AtomicInteger> currentAMSCounters = allAMSCounters.get (pid);
			final ArrayList <String> methodNames = new ArrayList<String> (currentAMSCounters.keySet ());
			Collections.sort (methodNames);

			final HashMap <String, Integer> classCounters = new HashMap<String, Integer> ();

			for (final String methodName : methodNames) {
				final int methodCounter = currentAMSCounters.get (methodName).get ();
				System.out.printf (
						"PROCESS-%05d-%s-METHODS: %s %d\n", pid, name, methodName,
						methodCounter);

				final String className = methodName.substring (
						0, methodName.indexOf ('.'));

				final Integer classCounter = classCounters.get (className);
				classCounters.put (className, classCounter == null
						? methodCounter : classCounter + methodCounter);
			}

			for (final String className : classCounters.keySet ()) {
				System.out.printf (
						"PROCESS-%05d-%s-CLASSES: %s %d\n", pid, name, className,
						classCounters.get (className));
			}
		}
	}


	@Override
		public void atExit (final Context context) {
		}


	@Override
		public void objectFree (
				final Context context, final ShadowObject netRef) {
		}

}
