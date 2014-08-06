package ch.usi.dag.taint.analysis;

import ch.usi.dag.disldroidreserver.shadow.Context;
import ch.usi.dag.disldroidreserver.shadow.ShadowObject;

public class TaintObject {

    TaintObject (
        final Context _context, final ShadowObject _obj, final int _taint,
        final int _version) {
        context = _context;
        obj = _obj;
        taint = _taint;
        version = _version;
    }


    public Context context = null;

    public ShadowObject obj = null;

    public int taint = 0;

    public int version = 0;

    public long time = -1;
}