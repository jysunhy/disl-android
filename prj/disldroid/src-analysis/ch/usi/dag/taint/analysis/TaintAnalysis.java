package ch.usi.dag.taint.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.usi.dag.disldroidreserver.msg.ipc.IPCEventRecord;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.NetReferenceHelper;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;
import ch.usi.dag.disldroidreserver.shadow.ShadowString;


public class TaintAnalysis extends RemoteAnalysis {

    public boolean debug = true;

    public HashMap <Integer, HashMap <Long, List <TaintObject>>> taint_map = new HashMap <> ();

    public HashMap <Long, List <TaintObject>> timed_map = new HashMap <> ();

    private TaintObject getTaintObject (final Context context, final ShadowObject obj) {
        HashMap <Long, List <TaintObject>> localmap = taint_map.get (context.pid ());
        if (localmap == null) {
            localmap = new HashMap <Long, List <TaintObject>> ();
            taint_map.put (context.pid (), localmap);
        }
        List <TaintObject> locallist = localmap.get (obj.getNetRef ());
        //if null, then put an init one
        if (locallist == null) {
            locallist = new ArrayList <TaintObject> ();
            localmap.put (obj.getNetRef (), locallist);
            locallist.add (new TaintObject (context, obj, 0, 1));
        }
        //get the latest TaintObject for obj
        return locallist.get (locallist.size () - 1);
    }


    private void addTaintObject (final Context context, final TaintObject tobj) {
        HashMap <Long, List <TaintObject>> localmap = taint_map.get (context.pid ());
        if (localmap == null) {
            localmap = new HashMap <Long, List <TaintObject>> ();
            taint_map.put (context.pid (), localmap);
        }
        final ShadowObject obj = tobj.obj;
        List <TaintObject> locallist = localmap.get (obj.getNetRef ());
        if (locallist == null) {
            locallist = new ArrayList <TaintObject> ();
            localmap.put (obj.getNetRef (), locallist);
        }
        locallist.add (tobj);
    }


    private TaintObject getTaintObjectWith (
        final int pid, final long netref, final long time) {
        final List <TaintObject> list = timed_map.get (time);
        TaintObject obj = null;
        while (obj == null) {
            if (list != null) {
                for (int i = 0; i < list.size (); i++) {
                    if (list.get (i).obj.getNetRef () == netref) {
                        if (debug) {
                            System.out.println ("Found matched version at pid "
                                + pid + " for object " + netref + " at time " + time);
                        }
                        obj = list.get (i);
                        break;
                    } else {
                        System.err.println ("NOT EXPECTED TO REACH HERE");
                    }
                }
            }
            if(obj != null) {
                break;
            }
            System.out.println ("Waiting for the client event");
            try {
                Thread.sleep (1000);
            } catch (final InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ();
            }
        }
        if(obj.context.pid() != pid) {
            System.err.println ("NOT EXPECTED TO REACH HERE 2");
        }
        return obj;
    }


    private void putTaintObjectWith (
        final Context context, final ShadowObject obj, final long time) {
        List <TaintObject> list = timed_map.get (time);
        final TaintObject tobj = getTaintObject (context, obj);
        if (list == null) {
            list = new ArrayList<TaintObject>();
            timed_map.put(time, list);
        }
        list.add (tobj);
    }

    private void propagate(final Context context, final TaintObject tfromobj, final ShadowObject toobj){
        if (tfromobj.taint == 0) {
            if (debug) {
                System.out.println ("from obj is clean, ignore this propagation");
            }
        } else {
            final TaintObject ttoobj = getTaintObject (context, toobj);
            if ((ttoobj.taint | tfromobj.taint) == ttoobj.taint) {
                if (debug) {
                    System.out.println ("no infection");
                }
            } else
            {
                if (debug) {
                    System.out.println ("\tto obj with tag "
                        + ttoobj.taint + " get infected with fromobj taint tag "
                        + tfromobj.taint + " to " + (tfromobj.taint | ttoobj.taint));
                }
                final TaintObject newversion = new TaintObject (
                    context, toobj, ttoobj.taint | tfromobj.taint, ttoobj.version + 1);
                addTaintObject (context, newversion);
            }
        }
    }

    public void taint_propagate (
        final Context context, final ShadowObject from, final ShadowObject to,
        final ShadowString name, final ShadowString location) {
        if(to == null)
        {
            System.err.println ("shouldn't be null here");
            return;
        }
        if (debug) {
            System.out.println ("Proc("
                + context.pid () + ":" + context.getPname ()
                + ") tainted from Object ##" + from.getId () + "## to ##"
                + to.getId ()
                + "## when invoking " + name
                + " in " + location);
        }

        final TaintObject tfromobj = getTaintObject (context, from);
        propagate (context, tfromobj, to);
    }


    public void taint_prepare (
        final Context context, final ShadowObject from, final long time,
        final ShadowString name, final ShadowString location) {
        final TaintObject tobj = getTaintObject (context, from);
        System.out.println ("Proc("
            + context.pid () + ":" + context.getPname () + ") sending Object ##"
            + from.getId () + "## when invoking " + name + " in " + location);
        putTaintObjectWith (context, from, time);
    }


    public void taint_propagate2 (
        final Context context, final long from, final int fromPid, final long time,
        final ShadowObject to, final ShadowString name, final ShadowString location) {
        final TaintObject tobj = getTaintObjectWith (fromPid, from, time);
        System.out.println ("Proc("
            + context.pid () + ":" + context.getPname ()
            + ") tainted from Object ##" + NetReferenceHelper.get_object_id (from)
            + "(" + fromPid + ")## to ##" + to.getId () + "## when invoking " + name
            + " in " + location);
        final TaintObject tfromobj = getTaintObjectWith (fromPid, from, time);
        propagate (context, tfromobj, to);
    }


    public void taint_sink (
        final Context context, final ShadowObject obj, final ShadowString name,
        final ShadowString location) {
        final TaintObject tobj = getTaintObject (context, obj);
        if(tobj.taint != 0){
            System.out.println ("Proc("
                + context.pid () + ":" + context.getPname () + ") tainted Object ##"
                + obj.getId () + "## is leaked when invoking " + name + " in "
                + location);
        }
    }


    public void taint_source (
        final Context context, final ShadowObject obj, final int flag,
        final ShadowString name, final ShadowString location) {

        final TaintObject tobj = getTaintObject (context, obj);
        if((tobj.taint | flag) == tobj.taint) {
            System.out.println ("already infected obj");
        }else{
            System.out.println ("Proc("
            + context.pid () + ":" + context.getPname () + ") Object ##"
            + obj.getId () + "## is tainted with flag " + flag + " when invoking "
            + name + " in " + location);
            final TaintObject newversion = new TaintObject (context, obj, tobj.taint|flag, tobj.version+1);
            addTaintObject (context, newversion);
        }
    }


    public void dynamic_alert (
        final Context context, final ShadowString name, final ShadowString location,
        final ShadowString args) {
//        System.out.println ("Proc("
//            + context.pid () + ":" + context.getPname () + ") Dynamic ##"
//            + name.toString () + "## detected in " + location.toString ()
//            + " with args " + args);
    }


    public void source_alert (
        final Context context, final ShadowString name, final ShadowString location) {
//        System.out.println ("Proc("
//            + context.pid () + ":" + context.getPname () + ") Source ##"
//            + name.toString () + "## detected in " + location.toString ());
    }


    public void sink_alert (
        final Context context, final ShadowString name, final ShadowString location) {
//        System.out.println ("Proc("
//            + context.pid () + ":" + context.getPname () + ") Sink ##"
//            + name.toString () + "## detected in " + location.toString ());
    }


    @Override
    public void atExit (final Context context) {
    }


    @Override
    public void objectFree (
        final Context context, final ShadowObject netRef) {
    }


    @Override
    public void ipcEventProcessed (final IPCEventRecord event) {
        // TODO Auto-generated method stub

    }

}
