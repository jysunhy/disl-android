package ch.usi.dag.disl.localvar;

import java.util.HashMap;
import java.util.Map;


public final class LocalVars {

    private final Map <String, SyntheticLocalVar> syntheticLocals;

    private final Map <String, ThreadLocalVar> threadLocals;


    //

    public LocalVars () {
        this (
            new HashMap <String, SyntheticLocalVar> (),
            new HashMap <String, ThreadLocalVar> ()
        );
    }


    public LocalVars (
        final Map <String, SyntheticLocalVar> syntheticLocals,
        final Map <String, ThreadLocalVar> threadLocals
    ) {
        this.syntheticLocals = syntheticLocals;
        this.threadLocals = threadLocals;
    }


    //

    public Map <String, SyntheticLocalVar> getSyntheticLocals () {
        return syntheticLocals;
    }


    public Map <String, ThreadLocalVar> getThreadLocals () {
        return threadLocals;
    }


    //

    public void putAll (final LocalVars other) {
        syntheticLocals.putAll (other.getSyntheticLocals ());
        threadLocals.putAll (other.getThreadLocals ());
    }

}
