package ch.usi.dag.disl.classparser;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.AfterThrowing;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.Monitor;
import ch.usi.dag.disl.classcontext.ClassContext;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.exception.DiSLFatalException;
import ch.usi.dag.disl.exception.GuardException;
import ch.usi.dag.disl.exception.MarkerException;
import ch.usi.dag.disl.exception.ParserException;
import ch.usi.dag.disl.exception.ReflectionException;
import ch.usi.dag.disl.exception.ScopeParserException;
import ch.usi.dag.disl.exception.SnippetParserException;
import ch.usi.dag.disl.exception.StaticContextGenException;
import ch.usi.dag.disl.guard.GuardHelper;
import ch.usi.dag.disl.marker.Marker;
import ch.usi.dag.disl.marker.Parameter;
import ch.usi.dag.disl.processorcontext.ArgumentProcessorContext;
import ch.usi.dag.disl.scope.Scope;
import ch.usi.dag.disl.scope.ScopeImpl;
import ch.usi.dag.disl.snippet.Snippet;
import ch.usi.dag.disl.snippet.SnippetUnprocessedCode;
import ch.usi.dag.disl.staticcontext.StaticContext;
import ch.usi.dag.disl.util.AsmHelper;
import ch.usi.dag.disl.util.Constants;
import ch.usi.dag.disl.util.ReflectionHelper;

/**
 * The parser takes annotated java file as input and creates Snippet classes
 */
class SnippetParser extends AbstractParser {

    private final List<Snippet> snippets = new LinkedList<Snippet>();

    public List<Snippet> getSnippets() {

        return snippets;
    }

    public void parse(final ClassNode classNode) throws ParserException,
            SnippetParserException, ReflectionException, ScopeParserException,
            StaticContextGenException, MarkerException,
            GuardException {

        // NOTE: this method can be called many times

        // process local variables
        processLocalVars(classNode);

        for (final MethodNode method : classNode.methods) {

            // skip the constructor
            if (Constants.isConstructorName (method.name)) {
                continue;
            }

            // skip static initializer
            if (Constants.isInitializerName (method.name)) {
                continue;
            }

            snippets.add(parseSnippet(classNode.name, method));
        }
    }

    // parse snippet
    private Snippet parseSnippet(final String className, final MethodNode method)
            throws SnippetParserException, ReflectionException,
            ScopeParserException, StaticContextGenException, MarkerException,
            GuardException, ParserException {

        // check annotation
        if (method.invisibleAnnotations == null) {
            throw new SnippetParserException("DiSL anottation for method "
                    + className + "." + method.name + " is missing");
        }

        // check only one annotation
        if (method.invisibleAnnotations.size() > 1) {
            throw new SnippetParserException("Method " + className + "."
                    + method.name + " can have only one DiSL anottation");
        }

        // check static
        if ((method.access & Opcodes.ACC_STATIC) == 0) {
            throw new SnippetParserException("Method " + className + "."
                    + method.name + " should be declared as static");
        }

        // check return type
        final String tmp = method.invisibleAnnotations.get (0).desc;
        if (!Type.getReturnType(method.desc).equals(Type.VOID_TYPE) && !method.invisibleAnnotations.get (0).desc.equals ("Lch/usi/dag/disl/annotation/Monitor;")) {
            throw new SnippetParserException("Method " + className + "."
                    + method.name + " cannot return value");
        }

        // no exception can be thrown
        if(! method.exceptions.isEmpty()) {
            throw new SnippetParserException("Method " + className + "."
                    + method.name + " cannot throw any exception");
        }

        final AnnotationNode annotation = method.invisibleAnnotations.get(0);

        final SnippetAnnotationData annotData =
            parseMethodAnnotation(className + "." + method.name, annotation);

        // ** marker **
        final Marker marker =
            getMarker(annotData.marker, annotData.args);

        // ** scope **
        final Scope scope = new ScopeImpl(annotData.scope);

        // ** guard **
        final Class<?> guardClass = ParserHelper.getGuard(annotData.guard);
        final Method guardMethod = GuardHelper.findAndValidateGuardMethod(guardClass,
                GuardHelper.snippetContextSet());

        // ** parse used static and dynamic context **
        Contexts context = null;
        if(!method.invisibleAnnotations.get (0).desc.equals ("Lch/usi/dag/disl/annotation/Monitor;")) {
            context = parseUsedContexts(method.desc);
        } else {
            context = parseNoContexts (method.desc);
        }

        // ** checks **

        // detect empty snippets
        if (AsmHelper.containsOnlyReturn(method.instructions)) {
            throw new SnippetParserException("Method " + className + "."
                    + method.name + " cannot be empty");
        }

        // context arguments (local variables 1, 2, ...) cannot be stored or
        // overwritten, may be used only in method calls
        if(!method.invisibleAnnotations.get (0).desc.equals ("Lch/usi/dag/disl/annotation/Monitor;")) {
            ParserHelper.usesContextProperly(className, method.name, method.desc,
                method.instructions);
        }

        // ** create unprocessed code holder class **
        // code is processed after everything is parsed
        final SnippetUnprocessedCode uscd = new SnippetUnprocessedCode(className,
                method.name, method.instructions, method.tryCatchBlocks,
                context.getStaticContexts(), context.usesDynamicContext(),
                annotData.dynamicBypass, context.usesClassContext(),
                context.usesProcessorContext());

        // return whole snippet
        return new Snippet(className, method.name, annotData.type, marker,
                scope, guardMethod, annotData.order, uscd);
    }

    private SnippetAnnotationData parseMethodAnnotation(final String fullMethodName,
            final AnnotationNode annotation) throws SnippetParserException {

        final Type annotationType = Type.getType(annotation.desc);

        // after annotation
        if (annotationType.equals(Type.getType(After.class))) {
            return parseMethodAnnotFields(After.class, annotation);
        }

        // after normal execution annotation
        if (annotationType.equals(Type.getType(AfterReturning.class))) {
            return parseMethodAnnotFields(AfterReturning.class, annotation);
        }

        // after abnormal execution annotation
        if (annotationType.equals(Type.getType(AfterThrowing.class))) {
            return parseMethodAnnotFields(AfterThrowing.class, annotation);
        }

        // before annotation
        if (annotationType.equals(Type.getType(Before.class))) {
            return parseMethodAnnotFields(Before.class, annotation);
        }

        if (annotationType.equals(Type.getType(Monitor.class))) {
            return parseMethodAnnotFields(Monitor.class, annotation);
        }

        // unknown annotation
        throw new SnippetParserException("Method " + fullMethodName
                + " has unsupported DiSL annotation");
    }

    // data holder for AnnotationParser
    private static class SnippetAnnotationData {

        public Class<?> type;

        // annotation values
        public Type marker = null;
        public String args = null; // default
        public String scope = "*"; // default
        public Type guard = null; // default
        public int order = 100; // default
        public boolean dynamicBypass = true; // default

        public SnippetAnnotationData(final Class<?> type) {
            this.type = type;
        }
    }

    private SnippetAnnotationData parseMethodAnnotFields(final Class<?> type,
            final AnnotationNode annotation) {

        final SnippetAnnotationData sad = new SnippetAnnotationData(type);
        ParserHelper.parseAnnotation(sad, annotation);

        if (sad.marker == null) {

            throw new DiSLFatalException("Missing attribute in annotation "
                    + type.toString()
                    + ". This may happen if annotation class is changed but"
                    + " data holder class is not.");
        }

        return sad;
    }

    private Marker getMarker(final Type markerType, final String markerParam)
            throws ReflectionException, MarkerException {

        // get marker class - as generic class
        final Class<?> genMarkerClass = ReflectionHelper.resolveClass(markerType);

        // get marker class - as subclass
        final Class<? extends Marker> markerClass =
                genMarkerClass.asSubclass(Marker.class);

        // instantiate marker WITHOUT Parameter as an argument
        if(markerParam == null) {
            try {
                return ReflectionHelper.createInstance(markerClass);
            }
            catch(final ReflectionException e) {

                if(e.getCause() instanceof NoSuchMethodException) {
                    throw new MarkerException("Marker " + markerClass.getName()
                            + " requires \"param\" annotation attribute"
                            + " declared",
                            e);
                }

                throw e;
            }
        }

        // try to instantiate marker WITH Parameter as an argument
        try {
            return ReflectionHelper.createInstance(markerClass, new Parameter(
                    markerParam));
        }
        catch(final ReflectionException e) {

            if(e.getCause() instanceof NoSuchMethodException) {
                throw new MarkerException("Marker " + markerClass.getName()
                        + " does not support \"param\" attribute", e);
            }

            throw e;
        }
    }

    private static class Contexts {

        private final Set<String> staticContexts;
        private final boolean usesDynamicContext;
        private final boolean usesClassContext;
        private final boolean usesProcessorContext;

        public Contexts(final Set<String> staticContexts, final boolean usesDynamicContext,
                final boolean usesClassContext, final boolean usesProcessorContext) {
            super();
            this.staticContexts = staticContexts;
            this.usesDynamicContext = usesDynamicContext;
            this.usesClassContext = usesClassContext;
            this.usesProcessorContext = usesProcessorContext;
        }

        public Set<String> getStaticContexts() {
            return staticContexts;
        }

        public boolean usesDynamicContext() {
            return usesDynamicContext;
        }

        public boolean usesClassContext() {
            return usesClassContext;
        }

        public boolean usesProcessorContext() {
            return usesProcessorContext;
        }
    }


    private Contexts parseNoContexts (final String methodDesc)
    throws ReflectionException, StaticContextGenException {

        final Set <String> knownStCo = new HashSet <String> ();
        final boolean usesDynamicContext = false;
        final boolean usesClassContext = false;
        final boolean usesArgProcContext = false;

        return new Contexts (knownStCo, usesDynamicContext, usesClassContext,
            usesArgProcContext);
    }
    private Contexts parseUsedContexts(final String methodDesc)
            throws ReflectionException, StaticContextGenException {

        final Set<String> knownStCo = new HashSet<String>();
        boolean usesDynamicContext = false;
        boolean usesClassContext = false;
        boolean usesArgProcContext = false;

        for (final Type argType : Type.getArgumentTypes(methodDesc)) {

            // skip dynamic context class - don't check anything
            if (argType.equals(Type.getType(DynamicContext.class))) {
                usesDynamicContext = true;
                continue;
            }

            // skip class context class - don't check anything
            if (argType.equals(Type.getType(ClassContext.class))) {
                usesClassContext = true;
                continue;
            }

            // skip processor context class - don't check anything
            if (argType.equals(Type.getType(ArgumentProcessorContext.class))) {
                usesArgProcContext = true;
                continue;
            }

            final Class<?> argClass = ReflectionHelper.resolveClass(argType);

            // static context should implement static context interface
            if (!ReflectionHelper.implementsInterface(argClass,
                    StaticContext.class)) {

                throw new StaticContextGenException(argClass.getName()
                        + " does not implement StaticContext interface and"
                        + " cannot be used as snippet method parameter");
            }

            knownStCo.add(argType.getInternalName());
        }

        return new Contexts(knownStCo, usesDynamicContext, usesClassContext,
                usesArgProcContext);
    }
}
