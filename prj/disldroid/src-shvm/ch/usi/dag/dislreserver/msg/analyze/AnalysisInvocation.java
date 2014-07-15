package ch.usi.dag.dislreserver.msg.analyze;

import java.lang.reflect.Method;
import java.util.List;

public class AnalysisInvocation {

    private Method analysisMethod;
    private Object analysisInstance;
    private List<Object> args;

    public AnalysisInvocation (
        Method analysisMethod, Object analysisInstance,
        List<Object> args
    ) {
        super();
        this.analysisMethod = analysisMethod;
        this.analysisInstance = analysisInstance;
        this.args = args;
    }

    public void invoke () {
        try {
            analysisMethod.invoke (analysisInstance, args.toArray());

        } catch (final Exception e) {
            // report error during analysis invocation
            final Throwable cause = e.getCause ();

            System.err.format (
                "DiSL-RE: exception in analysis %s.%s(): ",
                analysisMethod.getDeclaringClass ().getName (),
                analysisMethod.getName ()
            );

            cause.printStackTrace();
        }
    }
}
