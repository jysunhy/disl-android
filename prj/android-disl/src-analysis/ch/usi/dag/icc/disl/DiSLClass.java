package ch.usi.dag.icc.disl;

import android.os.Binder;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;
import ch.usi.dag.dislre.AREDispatch;
import ch.usi.dag.icc.analysis.ICCAnalysisStub;

public class DiSLClass {





/*
    @Before (
        marker = InvokeMarker.class, args = "transact",
        scope = "android.hardware.input.IInputManager$Stub$Proxy.injectInputEvent")
    public static void createService (final DynamicContext dc) {
        ICCAnalysisStub.callServiceInClient ("android.hardware.input.IInputManager.l355");
    }
*/

    @Before (
        marker = BodyMarker.class,
        scope = "android.hardware.input.IInputManager$Stub$Proxy.*")
    public static void callIMProxy (final MethodStaticContext msc) {
        AREDispatch.NativeLog (msc.thisMethodFullName ());
        ICCAnalysisStub.callServiceInClient (msc.thisMethodFullName ());
    }

    @Before (
        marker = BodyMarker.class,
        scope = "android.hardware.input.IInputManager$Stub.*")
    public static void callIMStub (final MethodStaticContext msc) {
        AREDispatch.NativeLog (msc.thisMethodFullName ());
        ICCAnalysisStub.callServiceInClient (msc.thisMethodFullName ());
    }


    @Before (
        marker = BodyMarker.class,
        scope = "android.app.ActivityManagerProxy.*")
    public static void callAMProxy (final MethodStaticContext msc) {
        AREDispatch.NativeLog (msc.thisMethodFullName ());
        ICCAnalysisStub.callServiceInClient (msc.thisMethodFullName ());
    }


    @Before (
        marker = BodyMarker.class,
        scope = "android.view.IWindowManager$Stub$Proxy.*")
    public static void callWMProxy (final MethodStaticContext msc) {
        AREDispatch.NativeLog (msc.thisMethodFullName ());
        ICCAnalysisStub.callServiceInClient (msc.thisMethodFullName ());
    }


    @Before (
        marker = BodyMarker.class,
        scope = "android.content.pm.IPackageManager$Stub$Proxy.*")
    public static void callPMProxy (final MethodStaticContext msc) {
        AREDispatch.NativeLog (msc.thisMethodFullName ());
        ICCAnalysisStub.callServiceInClient (msc.thisMethodFullName ());
    }



/*
    @Befere k
	marke  = BodyMarker.class,
        scope = "android.hardware.input.InputManager.injectInputEvent")
    public static void injectEventInputManager (final MethodStaticContext msc) {
        ICCAnalysisStub.callServiceInClient (msc.thisMethodFullName ());
    }
*/

    /*@Before (
        marker = BodyMarker.class,
        scope = "java.lang.System.exit")
    public static void exit () {
 //       AREDispatch.manuallyClose();
    }*/


/*
    @Before (
        marker = BodyMarker.class,
        scope = "android.hardware.input.IInputManager$Stub.onTransact")
    public static void onTransact (final MethodStaticContext msc) {
        ICCAnalysisStub.callService (
            msc.thisMethodFullName (), Binder.getCallingPid ());
    }
/*
    @After (
        marker = BodyMarker.class,
        scope = "com.android.commands.monkey.*.injectEvent")
    public static void injectEvent (
        final MethodStaticContext msc, final DynamicContext dc) {
        ICCAnalysisStub.println (msc.thisMethodFullName ());
        System.out.println (msc.thisMethodFullName ());
    }
*/
    @Before (
        marker = BodyMarker.class,
        scope = "com.android.calculator2.Calculator.onCreate")
    public static void onSystemReady () {

        ICCAnalysisStub.onSystemReady ();
    }


    @Before (
        marker = BodyMarker.class,
        scope = "com.android.server.am.ActivityManagerService.*",
        guard = ActivityManagerServiceGuard.class)
    public static void callActivityManagerService (final MethodStaticContext msc) {

        AREDispatch.NativeLog (msc.thisMethodFullName ());
        ICCAnalysisStub.callService (

            msc.thisMethodFullName (), Binder.getCallingPid ());
    }


    @Before (
        marker = BodyMarker.class,
        scope = "com.android.server.wm.WindowManagerService.*",
        guard = WindowManagerServiceGuard.class)
    public static void callWindowManagerService (final MethodStaticContext msc) {
        AREDispatch.NativeLog (msc.thisMethodFullName ());
        ICCAnalysisStub.callService (
            msc.thisMethodFullName (), Binder.getCallingPid ());
    }


    @Before (
        marker = BodyMarker.class,
        scope = "com.android.server.input.InputManagerService.*",
        guard = InputManagerServiceGuard.class)
    public static void callInputManagerService (final MethodStaticContext msc) {
        AREDispatch.NativeLog (msc.thisMethodFullName ());
        ICCAnalysisStub.callService (
            msc.thisMethodFullName (), Binder.getCallingPid ());
    }


    @Before (
        marker = BodyMarker.class,
        scope = "com.android.server.pm.PackageManagerService.*",
        guard = PackageManagerServiceGuard.class)
    public static void callPackageManagerService (final MethodStaticContext msc) {
        AREDispatch.NativeLog (msc.thisMethodFullName ());
        ICCAnalysisStub.callService (
            msc.thisMethodFullName (), Binder.getCallingPid ());
    }

//    @Before (
//        marker = BodyMarker.class,
//        scope = "com.android.server.*.*",
//        guard = PublicNonStaticGuard.class)
//    public static void callSystemServer (final MethodStaticContext msc) {
//        ICCAnalysisStub.callService (
//            msc.thisMethodFullName (), Binder.getCallingPid ());
//    }

//    @Before (
//        marker = BodyMarker.class,
//        scope = "com.android.server.am.ActivityManagerService.checkPermission")
//    public static void checkPermission (final DynamicContext dc) {
//        ICCAnalysisStub.onCheckPermission (
//            Binder.getCallingPid (), dc.getMethodArgumentValue (0, String.class));
//    }
//
//
//    @Before (
//        marker = BodyMarker.class,
//        scope = "com.android.server.am.ActivityManagerService.getContentProvider")
//    public static void onGetContentProvider (final DynamicContext dc) {
//        ICCAnalysisStub.onGetContentProvider (
//            Binder.getCallingPid (), dc.getMethodArgumentValue (1, String.class));
//    }

//    @AfterReturning (marker = BodyMarker.class, scope = "android.app.Service.<init>")
//    public static void onServiceConstructor () {
//        ICCAnalysisStub.onServiceConstructor ();
//    }
//
//
//    @Before (
//        marker = BodyMarker.class,
//        scope = "android.os.Binder.transact")
//    public static void onBinderTransact () {
//        ICCAnalysisStub.onBinderTransact ();
//    }
//
//
//    @Before (
//        marker = BodyMarker.class,
//        scope = "android.os.Binder.onTransact")
//    public static void onBinderOnTransact () {
//        ICCAnalysisStub.onBinderOnTransact ();
//    }

//    @Before (
//        marker = InvokeMarker.class, args = "realStartServiceLocked",
//        scope = "com.android.server.am.ActivityManagerService.bringUpServiceLocked")
//    public static void createService (final DynamicContext dc) {
//        ICCAnalysisStub.onCreateService (
//            dc.getStackValue (0, ProcessRecord.class).pid, false);
//    }
//
//
//    @Before (
//        marker = LastReturnMarker.class,
//        scope = "com.android.server.am.ActivityManagerService.bringUpServiceLocked")
//    public static void createServiceIsolated (final DynamicContext dc) {
//        final ProcessRecord record = dc.getMethodArgumentValue (
//            0, ServiceRecord.class).isolatedProc;
//
//        if (record != null) {
//            ICCAnalysisStub.onCreateService (record.pid, true);
//        }
//    }

//  @Before (
//      marker = BodyMarker.class,
//      scope = "com.android.server.am.ActivityManagerService.startService")
//  public static void onStartService () {
//      ICCAnalysisStub.onStartService (Binder.getCallingPid ());
//  }
//
//
//  @Before (
//      marker = BodyMarker.class,
//      scope = "com.android.server.am.ActivityManagerService.bindService")
//  public static void onBindService () {
//      ICCAnalysisStub.onBindService (Binder.getCallingPid ());
//  }
//
//
//  @Before (
//      marker = BodyMarker.class,
//      scope = "com.android.server.am.ActivityManagerService.startActivity")
//  public static void onStartActivity () {
//      ICCAnalysisStub.onStartActivity (Binder.getCallingPid ());
//  }

}
