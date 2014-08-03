package ch.usi.dag.icc.disl;

import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorMode;
import ch.usi.dag.dislre.AREDispatch;

public class DiSLClass {


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
			AREDispatch.printStack ();
		}

	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.TransactionContext.getUpdatedSyncStates")
	// public static void api_1 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (256);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ContactsProvider2.query")
	// public static void api_2 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactLocaleUtils.getNameLookupKeys")
	// public static void api_3 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ProfileDatabaseHelper.getInstance")
	// public static void api_4 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ProfileDatabaseHelper.getNewInstanceForTest")
	// public static void api_5 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.PhotoProcessor.getThumbnailPhotoBytes")
	// public static void api_6 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ReorderingCursorWrapper.getInt")
	// public static void api_7 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.VoicemailContentProvider.getUriType")
	// public static void api_8 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.PhotoProcessor.getDisplayPhotoBytes")
	// public static void api_9 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.aggregation.util.ContactMatcher.getScore")
	// public static void api_10 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.AbstractContactsProvider.insert")
	// public static void api_11 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.aggregation.util.ContactMatcher.getContactId")
	// public static void api_12 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.util.DbQueryUtils.getEqualityClause")
	// public static void api_13 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactDirectoryManager.getDbHelper")
	// public static void api_14 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getMaxDisplayPhotoDim")
	// public static void api_15 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getTransactionHolder")
	// public static void api_16 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.DefaultCallLogInsertionHelper.getGeocodedLocationFor")
	// public static void api_17 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.GlobalSearchSupport.handleSearchSuggestionsQuery")
	// public static void api_18 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getContactDirectoryManagerForTest")
	// public static void api_19 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.util.Clock.getInstance")
	// public static void api_20 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactLocaleUtils.getNameLookupKeys")
	// public static void api_21 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactLocaleUtils.getNameLookupKeys")
	// public static void api_22 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getDatabaseHelper")
	// public static void api_23 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.aggregation.ContactAggregator.getSelection")
	// public static void api_24 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.PhotoStore.getTotalSize")
	// public static void api_25 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ReorderingCursorWrapper.getDouble")
	// public static void api_26 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ContactsProvider2.query")
	// public static void api_27 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.AbstractContactsProvider.update")
	// public static void api_28 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.LegacyApiSupport.query")
	// public static void api_29 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ProfileProvider.getTransactionHolder")
	// public static void api_30 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.VoicemailStatusTable.getType")
	// public static void api_31 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.buildSingleRowResult")
	// public static void api_32 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.aggregation.ContactAggregator.queryAggregationSuggestions")
	// public static void api_33 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.createPhotoPriorityResolver")
	// public static void api_34 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.aggregation.util.CommonNicknameCache.getCommonNicknameClusters")
	// public static void api_35 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ProfileProvider.query")
	// public static void api_36 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getQueryParameter")
	// public static void api_37 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.AccountWithDataSet.getAccountType")
	// public static void api_38 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.DataRowHandler.getAugmentedValues")
	// public static void api_39 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.PhotoPriorityResolver.getPhotoPriority")
	// public static void api_40 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.DataRowHandlerForPhoneNumber.getTypeRank")
	// public static void api_41 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.VoicemailContentProvider.getSourcePackage")
	// public static void api_42 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.AbstractContactsProvider.getCurrentTransaction")
	// public static void api_43 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.SearchIndexManager.getName")
	// public static void api_44 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.NameSplitter.getAdjustedFullNameStyle")
	// public static void api_45 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.VoicemailContentProvider.getUri")
	// public static void api_46 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.CallLogProvider.getDatabaseHelper")
	// public static void api_47 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.DataRowHandlerForOrganization.getTypeRank")
	// public static void api_48 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.MemoryCursor.getCount")
	// public static void api_49 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.util.MemoryUtils.getTotalMemorySize")
	// public static void api_50 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.VoicemailStatusTable.query")
	// public static void api_51 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ReorderingCursorWrapper.getCount")
	// public static void api_52 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.FastScrollingIndexCache.get")
	// public static void api_53 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.VoicemailContentProvider.getType")
	// public static void api_54 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.util.DbQueryUtils.getInequalityClause")
	// public static void api_55 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.SearchIndexManager.getFtsMatchQuery")
	// public static void api_56 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.TransactionContext.getDirtyRawContactIds")
	// public static void api_57 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.VoicemailContentProvider.query")
	// public static void api_58 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ProfileProvider.getDatabaseHelper")
	// public static void api_59 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.DataRowHandler.getTypeRank")
	// public static void api_60 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ProfileProvider.getType")
	// public static void api_61 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.TransactionContext.getAccountIdOrNullForRawContact")
	// public static void api_62 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.CallLogProvider.getType")
	// public static void api_63 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.TransactionContext.getStaleSearchIndexRawContactIds")
	// public static void api_64 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.PhotoStore.get")
	// public static void api_65 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getDataRowHandlerForProfile")
	// public static void api_66 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ContactLocaleUtils.getSortKey")
	// public static void api_67 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ReorderingCursorWrapper.getShort")
	// public static void api_68 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ContactsProvider2.queryLocal")
	// public static void api_69 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.NameLookupBuilder.normalizeName")
	// public static void api_70 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.SearchIndexManager.getString")
	// public static void api_71 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ReorderingCursorWrapper.getString")
	// public static void api_72 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.VoicemailContentProvider.getId")
	// public static void api_73 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.AbstractContactsProvider.getDatabaseHelper")
	// public static void api_74 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.TransactionContext.getUpdatedRawContactIds")
	// public static void api_75 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.util.DbQueryUtils.getEqualityClause")
	// public static void api_76 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.TransactionContext.getStaleSearchIndexContactIds")
	// public static void api_77 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ReorderingCursorWrapper.getLong")
	// public static void api_78 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.CountryMonitor.getCountryIso")
	// public static void api_79 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.SearchIndexManager.splitIntoFtsTokens")
	// public static void api_80 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ReorderingCursorWrapper.getColumnNames")
	// public static void api_81 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getCommonNicknameClusters")
	// public static void api_82 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactDirectoryManager.scanAllPackages")
	// public static void api_83 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.PhotoProcessor.getMaxThumbnailPhotoDim")
	// public static void api_84 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.AccountWithDataSet.getAccountName")
	// public static void api_85 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.NameNormalizer.getCompressingCollator")
	// public static void api_86 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ContactsTransaction.getDbForTag")
	// public static void api_87 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ReorderingCursorWrapper.getType")
	// public static void api_88 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ReorderingCursorWrapper.getFloat")
	// public static void api_89 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.NameSplitter.getGivenNames")
	// public static void api_90 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.NameSplitter.getSuffix")
	// public static void api_91 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.VoicemailContentProvider.getWhereClause")
	// public static void api_92 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.aggregation.util.NameDistance.getDistance")
	// public static void api_93 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.HanziToPinyin.getInstance")
	// public static void api_94 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ContactLocaleUtils.getSortKey")
	// public static void api_95 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ContactsProvider2.getType")
	// public static void api_96 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.HanziToPinyin.get")
	// public static void api_97 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.NameSplitter.getMiddleName")
	// public static void api_98 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.DataRowHandlerForEmail.getTypeRank")
	// public static void api_99 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactDirectoryManager.getDirectoryProviderPackages")
	// public static void api_100 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ContactLocaleUtils.getSortKey")
	// public static void api_101 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.aggregation.ContactAggregator.getCommonNicknameClusters")
	// public static void api_102 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.SearchIndexManager.getContent")
	// public static void api_103 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.TransactionContext.getInsertedRawContactIds")
	// public static void api_104 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.DataRowHandler.insert")
	// public static void api_105 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.CallLogProvider.query")
	// public static void api_106 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ContactLocaleUtils.getIntance")
	// public static void api_107 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.VoicemailContentProvider.createUriData")
	// public static void api_108 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getDefaultProjection")
	// public static void api_109 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.SearchIndexManager.getInt")
	// public static void api_110 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.NameSplitter.getAdjustedNameStyleBasedOnPhoneticNameStyle")
	// public static void api_111 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.AccountWithDataSet.getDataSet")
	// public static void api_112 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.NameNormalizer.getComplexityCollator")
	// public static void api_113 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.VoicemailContentProvider.getCallingPackage")
	// public static void api_114 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.DataRowHandler.getMimeTypeId")
	// public static void api_115 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getDefaultAccount")
	// public static void api_116 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.AccountWithDataSet.get")
	// public static void api_117 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.VoicemailContentTable.getType")
	// public static void api_118 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ContactsProvider2.getLocale")
	// public static void api_119 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.GlobalSearchSupport.handleSearchShortcutRefresh")
	// public static void api_120 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getThreadActiveDatabaseHelperForTest")
	// public static void api_121 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.PhotoProcessor.getDisplayPhoto")
	// public static void api_122 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.DefaultCallLogInsertionHelper.getInstance")
	// public static void api_123 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.SearchIndexManager.getTokens")
	// public static void api_124 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getMaxThumbnailDim")
	// public static void api_125 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getProfilePhotoStore")
	// public static void api_126 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ProfileProvider.query")
	// public static void api_127 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.ContactsProvider2.getPhotoStore")
	// public static void api_128 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.SearchIndexManager.getDigitsQueryBuilder")
	// public static void api_129 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.VoicemailContentProvider.getDatabaseHelper")
	// public static void api_130 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.MemoryCursor.getColumnNames")
	// public static void api_131 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactsProvider2.getDataRowHandler")
	// public static void api_132 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.NameSplitter.getPrefix")
	// public static void api_133 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.LegacyApiSupport.getType")
	// public static void api_134 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.PhotoProcessor.getNormalizedBitmap")
	// public static void api_135 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope =
	// "com.android.providers.contacts.ContactDirectoryManager.providerDescription")
	// public static void api_136 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.VoicemailContentTable.query")
	// public static void api_137 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "com.android.providers.contacts.NameSplitter.getFamilyName")
	// public static void api_138 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.Contacts.tryGetMyContactsGroupId")
	// public static void api_139 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.getLookupUri")
	// public static void api_140 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.getWithUri")
	// public static void api_141 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.Contacts.getDisplayLabel")
	// public static void api_142 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.Contacts.getDisplayLabel")
	// public static void api_143 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.getWithUri")
	// public static void api_144 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.Contacts.queryGroups")
	// public static void api_145 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.getLookupUri")
	// public static void api_146 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.getDisplayLabel")
	// public static void api_147 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.getEntityAndIncrementCursor")
	// public static void api_148 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.openContactPhotoInputStream")
	// public static void api_149 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.getContactLookupUri")
	// public static void api_150 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.getDisplayLabel")
	// public static void api_151 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.getEntityAndIncrementCursor")
	// public static void api_152 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.Contacts.addToGroup")
	// public static void api_153 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.lookupContact")
	// public static void api_154 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.Contacts.getDisplayLabel")
	// public static void api_155 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.Contacts.getDisplayLabel")
	// public static void api_156 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.getContactLookupUri")
	// public static void api_157 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.get")
	// public static void api_158 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.Contacts.getSetting")
	// public static void api_159 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.provider.ContactsContract.get")
	// public static void api_160 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1024);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderOperation.getUri")
	// public static void api_161 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (16);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.createCancellationSignal")
	// public static void api_162 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderOperation.getType")
	// public static void api_163 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderProxy.getType")
	// public static void api_164 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.getContext")
	// public static void api_165 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.getPathPermissions")
	// public static void api_166 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderClient.getType")
	// public static void api_167 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderClient.query")
	// public static void api_168 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.query")
	// public static void api_169 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.getStreamTypes")
	// public static void api_170 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderClient.getStreamTypes")
	// public static void api_171 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.getReadPermission")
	// public static void api_172 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.getIContentProvider")
	// public static void api_173 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.getType")
	// public static void api_174 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.getStreamTypes")
	// public static void api_175 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.openPipeHelper")
	// public static void api_176 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderProxy.insert")
	// public static void api_177 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.query")
	// public static void api_178 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderProxy.query")
	// public static void api_179 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.getContentProvider")
	// public static void api_180 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderClient.getLocalContentProvider")
	// public static void api_181 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderClient.query")
	// public static void api_182 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderNative.asBinder")
	// public static void api_183 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.bulkInsert")
	// public static void api_184 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.getWritePermission")
	// public static void api_185 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.getProviderName")
	// public static void api_186 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderClient.insert")
	// public static void api_187 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderProxy.getStreamTypes")
	// public static void api_188 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProviderOperation.apply")
	// public static void api_189 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.openTypedAssetFile")
	// public static void api_190 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.content.ContentProvider.openFileHelper")
	// public static void api_191 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	// @Before (
	// marker = BodyMarker.class,
	// scope = "android.providers.contacts.ContactsDatabaseHelper.*")
	// public static void api_192 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (65536);
	// }
	//
	// @Before (
	// marker = BodyMarker.class,
	// scope = "TelephonyProvider.*")
	// public static void api_193(final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (1);
	// }
	//
	// @Before (
	// marker = BodyMarker.class,
	// scope = "*ContentResolver.query")
	// public static void api_194 (final MethodStaticContext msc) {
	// AREDispatch.NativeLog (msc.thisMethodFullName ());
	// AREDispatch.CallAPI (131072);
	// }

}
