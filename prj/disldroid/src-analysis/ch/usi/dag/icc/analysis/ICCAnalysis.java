package ch.usi.dag.icc.analysis;

import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;


public class ICCAnalysis extends RemoteAnalysis {

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

	@Override
		public void atExit (final Context context) {
		}


	@Override
		public void objectFree (
				final Context context, final ShadowObject netRef) {
		}

}
