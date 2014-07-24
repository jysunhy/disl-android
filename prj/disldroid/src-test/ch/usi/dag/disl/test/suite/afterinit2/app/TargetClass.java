package ch.usi.dag.disl.test.suite.afterinit2.app;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.AfterInitBodyMarker;


/**
 * Tests that the {@link Before} and {@link After} snippets are correctly
 * ordered when instrumenting empty constructors using the
 * {@link AfterInitBodyMarker}.
 */
public class TargetClass {

    public TargetClass () {
        // empty constructor
    }


    public static class TargetSubclass extends TargetClass {
        public TargetSubclass () {
            // empty constructor
        }
    }


    public static void main (final String... args) {
        final TargetClass t = new TargetSubclass ();
        System.out.println (t.getClass ().getSimpleName ());
    }
}
