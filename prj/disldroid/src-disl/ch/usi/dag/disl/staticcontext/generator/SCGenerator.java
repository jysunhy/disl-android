package ch.usi.dag.disl.staticcontext.generator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.usi.dag.disl.coderep.StaticContextMethod;
import ch.usi.dag.disl.exception.ReflectionException;
import ch.usi.dag.disl.exception.StaticContextGenException;
import ch.usi.dag.disl.processor.ProcMethod;
import ch.usi.dag.disl.resolver.SCResolver;
import ch.usi.dag.disl.snippet.ProcInvocation;
import ch.usi.dag.disl.snippet.Shadow;
import ch.usi.dag.disl.snippet.Snippet;
import ch.usi.dag.disl.snippet.SnippetCode;
import ch.usi.dag.disl.staticcontext.StaticContext;
import ch.usi.dag.disl.util.Constants;

public class SCGenerator {

    private static final class StaticContextKey {
        private final Shadow shadow;
        private final String methodId;


        public StaticContextKey (Shadow shadow, String methodId) {
            this.shadow = shadow;
            this.methodId = methodId;
        }

        public StaticContextKey (
            Shadow shadow, String className, String methodName
        ) {
            this (shadow, className + Constants.STATIC_CONTEXT_METHOD_DELIM + methodName);
        }

        //

        private static final int __PRIME__ = 31;

        @Override
        public int hashCode() {
            int result = __PRIME__;
            result += (shadow == null) ? 0 : shadow.hashCode ();

            result *= __PRIME__;
            result += (methodId == null) ? 0 : methodId.hashCode ();

            return result;
        }


        @Override
        public boolean equals (final Object object) {
            if (this == object) {
                return true;
            }

            if (object instanceof StaticContextKey) {
                final StaticContextKey that = (StaticContextKey) object;

                //
                // Shadows and methods must either be null in both
                // objects, or equal.
                //
                boolean shadowsEqual = __nullOrEqual (this.shadow, that.shadow);
                if (shadowsEqual) {
                    return __nullOrEqual (this.methodId, that.methodId);
                }
            }

            return false;
        }
    }

    private static boolean __nullOrEqual (final Object obj1, final Object obj2) {
        if (obj1 == null) {
            return obj2 == null;
        } else {
            return obj1.equals (obj2);
        }
    }

    //

    private final Map <StaticContextKey, Object>
        staticInfoData = new HashMap <StaticContextKey, Object> ();


    public SCGenerator (
        final Map <Snippet, List <Shadow>> snippetMarkings
    ) throws ReflectionException, StaticContextGenException {
        computeStaticInfo (snippetMarkings);
    }


    // Call static context for each snippet and each marked region and create
    // a static info values
    private void computeStaticInfo (
        Map <Snippet, List <Shadow>> snippetMarkings
    ) throws ReflectionException, StaticContextGenException {

        for (final Snippet snippet : snippetMarkings.keySet ()) {
            for (final Shadow shadow : snippetMarkings.get (snippet)) {
                // compute static data for snippet and all processors
                // static data for snippets and processors can be evaluated
                // and stored together

                final SnippetCode snippetCode = snippet.getCode ();
                Set <StaticContextMethod>
                    scMethods = snippetCode.getStaticContexts ();

                // add static contexts from all processors
                for (ProcInvocation pi : snippetCode.getInvokedProcessors ().values ()) {

                    // add static contexts from all processor methods
                    for (ProcMethod pm : pi.getProcessor ().getMethods ()) {

                        // add to the pool
                        scMethods.addAll (pm.getCode ().getStaticContexts ());
                    }
                }

                //
                // For all static context methods, get a static context
                // instance, resolve static context data for the method,
                // and store the result.
                //
                for (final StaticContextMethod scm : scMethods) {
                    final StaticContext staticContext =
                        SCResolver.getInstance().getStaticContextInstance (
                            scm.getReferencedClass (), shadow
                        );

                    Object result = getStaticContextData (
                        staticContext, scm.getMethod ()
                    );

                    // store the result
                    put (shadow, scm.getId (), result);
                }
            }
        }
    }

    // resolves static context data - uses static context data caching
    private Object getStaticContextData (
        StaticContext staticContext, final Method method
    ) throws StaticContextGenException, ReflectionException {

        try {
            // get static data by invoking static context method
            method.setAccessible (true);
            return method.invoke (staticContext);

        } catch (final Exception e) {
            String message = String.format (
                "Invocation of static context method %s failed",
                method.getName ()
            );
            throw new StaticContextGenException (message, e);
        }
    }

    //

    public boolean contains (Shadow shadow, String infoClass, String infoMethod) {
        return staticInfoData.containsKey (new StaticContextKey (
            shadow, infoClass, infoMethod
        ));
    }


    public Object get (Shadow shadow, String infoClass, String infoMethod) {
        return staticInfoData.get (new StaticContextKey (
            shadow, infoClass, infoMethod
        ));
    }


    private void put (Shadow shadow, String methodId, Object value) {
        staticInfoData.put (new StaticContextKey (shadow, methodId), value);
    }

}
