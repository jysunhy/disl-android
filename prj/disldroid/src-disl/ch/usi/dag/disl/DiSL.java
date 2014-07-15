package ch.usi.dag.disl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.cbloader.ClassByteLoader;
import ch.usi.dag.disl.cbloader.ManifestHelper;
import ch.usi.dag.disl.cbloader.ManifestHelper.ManifestInfo;
import ch.usi.dag.disl.classparser.ClassParser;
import ch.usi.dag.disl.exception.DiSLException;
import ch.usi.dag.disl.exception.DiSLIOException;
import ch.usi.dag.disl.exception.DiSLInMethodException;
import ch.usi.dag.disl.exception.InitException;
import ch.usi.dag.disl.exception.InvalidContextUsageException;
import ch.usi.dag.disl.exception.ManifestInfoException;
import ch.usi.dag.disl.exception.MarkerException;
import ch.usi.dag.disl.exception.ProcessorException;
import ch.usi.dag.disl.exception.ReflectionException;
import ch.usi.dag.disl.exception.StaticContextGenException;
import ch.usi.dag.disl.exception.TransformerException;
import ch.usi.dag.disl.exclusion.ExclusionSet;
import ch.usi.dag.disl.guard.GuardHelper;
import ch.usi.dag.disl.localvar.SyntheticLocalVar;
import ch.usi.dag.disl.localvar.ThreadLocalVar;
import ch.usi.dag.disl.processor.Proc;
import ch.usi.dag.disl.processor.generator.PIResolver;
import ch.usi.dag.disl.processor.generator.ProcGenerator;
import ch.usi.dag.disl.processor.generator.ProcInstance;
import ch.usi.dag.disl.processor.generator.ProcMethodInstance;
import ch.usi.dag.disl.scope.Scope;
import ch.usi.dag.disl.snippet.Shadow;
import ch.usi.dag.disl.snippet.Snippet;
import ch.usi.dag.disl.staticcontext.generator.SCGenerator;
import ch.usi.dag.disl.transformer.Transformer;
import ch.usi.dag.disl.weaver.Weaver;

// TODO javadoc comment all
/**
 * Main DiSL class providing interface for an instrumentation framework
 * (normally DiSL Server).
 */
public final class DiSL {

    private static final String PROP_TRACE              = "trace";
    public static final boolean trace                   = Boolean.getBoolean(PROP_TRACE);

    private static final String PROP_DEBUG              = "debug";
    public static final boolean debug                   = trace || Boolean.getBoolean(PROP_DEBUG);

    // default is that exception handler is inserted
    // this is the reason for "double" negation in assignment
    private final String        PROP_NO_EXCEPT_HANDLER  = "disl.noexcepthandler";
    private final boolean       exceptHandler           =
                                                                !Boolean.getBoolean(PROP_NO_EXCEPT_HANDLER);

    private final String        PROP_SPLIT_LONG_METHODS = "disl.splitmethods";
    private final boolean       splitLongMethods        =
                                                                Boolean.getBoolean(PROP_SPLIT_LONG_METHODS);

    private final boolean       useDynamicBypass;

    private final Transformer   transformer;

    private final boolean       transPropagateUninstr;

    private final Set<Scope>    exclusionSet;

    private final List<Snippet> snippets;

    /**
     * DiSL initialization.
     *
     * @param useDynamicBypass
     *            enable or disable dynamic bypass instrumentation
     */
    // this method should be called only once
    public DiSL(final boolean useDynamicBypass) throws DiSLException {

        this.useDynamicBypass = useDynamicBypass;

        // *** resolve transformer ***
        transformer = resolveTransformer();

        // transfomer output propagation
        if (transformer == null) {
            transPropagateUninstr = false;
        } else {
            transPropagateUninstr =
                    transformer.propagateUninstrumentedClasses();
        }

        // *** prepare exclusion set ***
        exclusionSet = ExclusionSet.prepare();

        // *** load disl classes ***
        final List<InputStream> dislClasses = ClassByteLoader.loadDiSLClasses();

        if (dislClasses == null) {
            throw new InitException("Cannot load DiSL classes. Please set"
                    + " the property " + ClassByteLoader.PROP_DISL_CLASSES
                    + " or supply jar with DiSL classes"
                    + " and proper manifest");
        }

        // *** parse disl classes ***
        // - create snippets
        // - create static context methods

        final ClassParser parser = new ClassParser();

        for (final InputStream classIS : dislClasses) {
            parser.parse(classIS);
        }

        // initialize processors
        final Map<Type, Proc> processors = parser.getProcessors();
        for (final Proc processor : processors.values()) {
            processor.init(parser.getAllLocalVars());
        }

        final List<Snippet> parsedSnippets = parser.getSnippets();

        // initialize snippets
        for (final Snippet snippet : parsedSnippets) {
            snippet.init(parser.getAllLocalVars(), processors, exceptHandler,
                    useDynamicBypass);
        }

        // initialize snippets variable
        // - this is set when everything is ok
        // - it serves as initialization flag
        snippets = parsedSnippets;

        // TODO put checker here
        // like After should catch normal and abnormal execution
        // but if you are using After (AfterThrowing) with BasicBlockMarker
        // or InstructionMarker that doesn't throw exception, then it is
        // probably something, you don't want - so just warn the user
        // also it can warn about unknown opcodes if you let user to
        // specify this for InstructionMarker
    }

    /**
     * Finds transformer class in configuration and allocates it.
     *
     * @return newly allocated transformer.
     */
    private Transformer resolveTransformer() throws ManifestInfoException,
            ReflectionException {

        final ManifestInfo mi = ManifestHelper.getDiSLManifestInfo();

        if (mi == null) {
            return null;
        }

        final String transformerClsName = mi.getDislTransformer();

        if (transformerClsName == null) {
            return null;
        }

        try {
            final Class<?> transformerCls = Class.forName(transformerClsName);
            return (Transformer) transformerCls.newInstance();
        } catch (final ClassNotFoundException e) {
            throw new ReflectionException("DiSL transformer "
                    + transformerClsName + " cannot be resolved", e);
        } catch (final InstantiationException e) {
            throw new ReflectionException("DiSL transformer "
                    + transformerClsName + " cannot be instantiated", e);
        } catch (final IllegalAccessException e) {
            throw new ReflectionException("DiSL transformer "
                    + transformerClsName + " cannot be instantiated", e);
        } catch (final ClassCastException e) {
            throw new ReflectionException("DiSL transformer "
                    + transformerClsName
                    + " does not implement Transformer interface", e);
        }
    }

    /**
     * Instruments a method in a class.
     *
     * NOTE: This method changes the classNode argument
     *
     * @param classNode
     *            class that will be instrumented
     * @param methodNode
     *            method in the classNode argument, that will be instrumented
     * @return
     *         {@code true} if the methods was changed, {@code false} otherwise.
     */
    private boolean instrumentMethod(
            final ClassNode classNode, final MethodNode methodNode
            ) throws ReflectionException, StaticContextGenException,
                    ProcessorException, InvalidContextUsageException, MarkerException {

        // skip abstract methods
        if ((methodNode.access & Opcodes.ACC_ABSTRACT) != 0) {
            return false;
        }

        // skip native methods
        if ((methodNode.access & Opcodes.ACC_NATIVE) != 0) {
            return false;
        }

        final String className = classNode.name;
        final String methodName = methodNode.name;
        final String methodDesc = methodNode.desc;

        // evaluate exclusions
        for (final Scope exclScope : exclusionSet) {
            if (exclScope.matches(className, methodName, methodDesc)) {
                __debug("DiSL: excluding method: %s.%s(%s)\n",
                        className, methodName, methodDesc);
                return false;
            }
        }

        // *** match snippet scope ***

        final List<Snippet> matchedSnippets = new LinkedList<Snippet>();
        for (final Snippet snippet : snippets) {
            if (snippet.getScope().matches(className, methodName, methodDesc)) {
                matchedSnippets.add(snippet);
            }
        }

        // if there is nothing to instrument -> quit
        // just to be faster out
        if (matchedSnippets.isEmpty()) {
            __debug("DiSL: skipping unaffected method: %s.%s(%s)\n",
                    className, methodName, methodDesc);
            return false;
        }

        // *** create shadows ***

        __trace("DiSL: processing method: %s.%s(%s)\n",
                className, methodName, methodDesc);

        // shadows mapped to snippets - for weaving
        final Map<Snippet, List<Shadow>> snippetMarkings =
                new HashMap<Snippet, List<Shadow>>();

        for (final Snippet snippet : matchedSnippets) {
            __trace("DiSL:     snippet: %s.%s()\n",
                    snippet.getOriginClassName(), snippet.getOriginMethodName());

            // marking
            final List<Shadow> shadows = snippet.getMarker().mark(
                    classNode, methodNode, snippet
                    );

            // select shadows according to snippet guard
            final List<Shadow> selectedShadows = selectShadowsWithGuard(
                    snippet.getGuard(), shadows
                    );

            __trace("DiSL:         selected shadows: %d\n",
                    selectedShadows.size());

            // add to map
            if (!selectedShadows.isEmpty()) {
                snippetMarkings.put(snippet, selectedShadows);
            }
        }

        // *** compute static info ***

        // prepares SCGenerator class (computes static context)
        final SCGenerator staticInfo = new SCGenerator(snippetMarkings);

        // *** used synthetic local vars in snippets ***

        // weaver needs list of synthetic locals that are actively used in
        // selected (matched) snippets

        final Set<SyntheticLocalVar> usedSLVs = new HashSet<SyntheticLocalVar>();
        for (final Snippet snippet : snippetMarkings.keySet()) {
            usedSLVs.addAll(snippet.getCode().getReferencedSLVs());
        }

        // *** prepare processors ***

        final PIResolver piResolver = new ProcGenerator().compute(snippetMarkings);

        // *** used synthetic local vars in processors ***

        // include SLVs from processor methods into usedSLV
        for (final ProcInstance pi : piResolver.getAllProcInstances()) {
            for (final ProcMethodInstance pmi : pi.getMethods()) {
                usedSLVs.addAll(pmi.getCode().getReferencedSLVs());
            }
        }

        // *** weaving ***

        __trace("DiSL:     snippet markings: %d\n", snippetMarkings.size());
        if (snippetMarkings.size() > 0) {
            __debug("DiSL: instrumenting method: %s.%s(%s)\n",
                    className, methodName, methodDesc);
            Weaver.instrument(
                    classNode, methodNode, snippetMarkings,
                    new LinkedList<SyntheticLocalVar>(usedSLVs),
                    staticInfo, piResolver
                    );

            return true;

        } else {
            __debug("DiSL: skipping unaffected method: %s.%s(%s)\n",
                    className, methodName, methodDesc);
            return false;
        }
    }

    /**
     * Selects only shadows matching the passed guard.
     *
     * @param guard
     *            guard, on witch conditions are the shadows selected
     * @param marking
     *            the list of shadows from where the gurads selects
     * @return selected shadows
     */
    private List<Shadow> selectShadowsWithGuard(final Method guard,
            final List<Shadow> marking) {

        if (guard == null) {
            return marking;
        }

        final List<Shadow> selectedMarking = new LinkedList<Shadow>();

        // check guard for each shadow
        for (final Shadow shadow : marking) {

            if (GuardHelper.guardApplicable(guard, shadow)) {

                selectedMarking.add(shadow);
            }
        }

        return selectedMarking;
    }

    /**
     * Data holder for an instrumented class
     */
    private static class InstrumentedClass {

        private final ClassNode   classNode;
        private final Set<String> changedMethods;

        public InstrumentedClass(final ClassNode classNode,
                final Set<String> changedMethods) {
            super();
            this.classNode = classNode;
            this.changedMethods = changedMethods;
        }

        public ClassNode getClassNode() {
            return classNode;
        }

        public Set<String> getChangedMethods() {
            return changedMethods;
        }
    }

    /**
     * Instruments class node.
     *
     * Note: This method is thread safe. Parameter classNode is changed during
     * the invocation.
     *
     * @param classNode
     *            class node to instrument
     * @return instrumented class
     */
    private InstrumentedClass instrumentClass(ClassNode classNode)
            throws DiSLException {

        // NOTE that class can be changed without changing any method
        // - adding thread local fields
        boolean classChanged = false;

        // track changed methods for code merging
        final Set<String> changedMethods = new HashSet<String>();

        // instrument all methods in a class
        for (final MethodNode methodNode : classNode.methods) {

            boolean methodChanged = false;

            // intercept all exceptions and add a method name
            try {
                methodChanged = instrumentMethod(classNode, methodNode);
            } catch (final DiSLException e) {

                throw new DiSLInMethodException(
                        classNode.name + "." + methodNode.name, e);
            }

            // add method to the set of changed methods
            if (methodChanged) {
                changedMethods.add(methodNode.name + methodNode.desc);
                classChanged = true;
            }
        }

        // instrument thread local fields
        if (Type.getInternalName(Thread.class).equals(classNode.name)) {

            final Set<ThreadLocalVar> insertTLVs = new HashSet<ThreadLocalVar>();

            // dynamic bypass
            if (useDynamicBypass) {

                // prepare dynamic bypass thread local variable
                final ThreadLocalVar tlv = new ThreadLocalVar(null, "bypass",
                        Type.getType(boolean.class), false);
                tlv.setDefaultValue(0);
                insertTLVs.add(tlv);
            }

            // get all thread locals in snippets
            for (final Snippet snippet : snippets) {
                insertTLVs.addAll(snippet.getCode().getReferencedTLVs());
            }

            if (!insertTLVs.isEmpty()) {

                // instrument fields
                final ClassNode cnWithFields = new ClassNode(Opcodes.ASM4);
                classNode.accept(new TLVInserter(cnWithFields, insertTLVs));

                // replace original code with instrumented one
                classNode = cnWithFields;
                classChanged = true;
            }
        }

        // we have changed some methods
        if (classChanged) {
            return new InstrumentedClass(classNode, changedMethods);
        }

        return null;
    }

    /**
     * Instruments array of bytes representing a class.
     *
     * @param classAsBytes
     *            class as array of bytes
     * @return instrumented class as array of bytes
     */
    // TODO ! current static context interface does not allow to have nice
    // synchronization - it should be redesigned such as the staticContextData
    // also invokes the required method and returns result - if this method
    // (and static context class itself) will be synchronized, it should work
    public synchronized byte[] instrument(
            byte[] classAsBytes
            ) throws DiSLException {
        // keep the currently processed class around in case of errors
        if (debug) {
            __dumpClassToFile(classAsBytes, "err.class");
        }

        // apply transformer first
        if (transformer != null) {
            try {
                classAsBytes = transformer.transform(classAsBytes);
            } catch (final Exception e) {
                throw new TransformerException("Transformer error", e);
            }
        }

        // create class reader
        final ClassReader classReader = new ClassReader(classAsBytes);

        // AfterInitBodyMarker uses AdviceAdapter
        // - classNode with API param is required by ASM 4.0 guidelines
        final ClassNode classNode = new ClassNode(Opcodes.ASM4);

        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

        final InstrumentedClass instrClass = instrumentClass(classNode);

        if (instrClass == null) {

            // propagate uninstrumented classes
            // useful, when transformer is doing some job on all classes
            if (transPropagateUninstr) {
                return classAsBytes;
            }

            return null;
        }

        ClassNode instrCN = instrClass.getClassNode();

        // if dynamic bypass is enabled use code merger
        if (useDynamicBypass) {
            final ClassReader origCR = new ClassReader(classAsBytes);
            final ClassNode origCN = new ClassNode();

            origCR.accept(origCN, ClassReader.EXPAND_FRAMES);

            // origCN and instrCN are destroyed during the merging
            instrCN = CodeMerger.mergeClasses(origCN, instrCN,
                    instrClass.getChangedMethods(), splitLongMethods);
        }

        // DiSL uses some instructions available only in higher versions
        final int REQUIRED_VERSION = Opcodes.V1_5;
        final int MAJOR_V_MASK = 0xFFFF;

        final int requiredMajorVersion = REQUIRED_VERSION & MAJOR_V_MASK;
        final int classMajorVersion = instrCN.version & MAJOR_V_MASK;

        if (classMajorVersion < requiredMajorVersion) {
            instrCN.version = REQUIRED_VERSION;
        }

        // Use compute frames for newer classes
        // It is required for >= 1.7
        final int COMPUTEFRAMES_VERSION = Opcodes.V1_7;
        final int cwFlags = (classMajorVersion >= COMPUTEFRAMES_VERSION) ?
            ClassWriter.COMPUTE_FRAMES : ClassWriter.COMPUTE_MAXS;

        final ClassWriter cw = new ClassWriter (cwFlags) {
            @Override
            protected String getCommonSuperClass (
                final String type1, final String type2
            ) {
                // same string as in super class
                return "java/lang/Object";
            }
        };

        // return as bytes
        instrCN.accept(cw);
        return cw.toByteArray();
    }

    private void __dumpClassToFile(
            final byte[] classBytes, final String fileName
            ) throws DiSLIOException {
        try {
            final FileOutputStream fos = new FileOutputStream(fileName);
            try {
                fos.write(classBytes);
            } finally {
                fos.close();
            }
        } catch (final IOException ioe) {
            throw new DiSLIOException(ioe);
        }
    }

    /**
     * Termination handler - should be invoked by the instrumentation framework.
     */
    public void terminate() {

    }

    //

    /**
     * Options for code transformations performed by DiSL.
     */
    public enum CodeOption {

        /**
         * Create a copy of the original method code and check whether to
         * execute the instrumented or the uninstrumented version of the code
         * upon method entry.
         */
        CREATE_BYPASS (Flag.CREATE_BYPASS),

        /**
         * Insert code for dynamic bypass control. Enable bypass when entering
         * instrumentation code and disable it when leaving it.
         */
        DYNAMIC_BYPASS (Flag.DYNAMIC_BYPASS),

        /**
         * Split methods exceeding the limit imposed by the class file format.
         */
        SPLIT_METHODS (Flag.SPLIT_METHODS),

        /**
         * Wrap snippets in exception handlers to catch exceptions. This is
         * mainly useful for debugging instrumentation code, because the
         * handlers terminate the program execution.
         */
        CATCH_EXCEPTIONS (Flag.CATCH_EXCEPTIONS);


        /**
         * Flags corresponding to individual code options. The flags are
         * used when communicating with DiSL agent.
         */
        public interface Flag {
            static final int CREATE_BYPASS = 1 << 0;
            static final int DYNAMIC_BYPASS = 1 << 1;
            static final int SPLIT_METHODS = 1 << 2;
            static final int CATCH_EXCEPTIONS = 1 << 3;
        }

        //

        private final int __flag;

        private CodeOption (final int flag) {
            __flag = flag;
        }

        //

        /**
         * Creates a set of code options from an array of options.
         */
        public static Set <CodeOption> setOf (final CodeOption... options) {
            final EnumSet <CodeOption> result = EnumSet.noneOf (CodeOption.class);
            for (final CodeOption option : options) {
                result.add (option);
            }

            return result;
        }


        /**
         * Creates a set of code options from flags in an integer.
         */
        public static Set <CodeOption> setOf (final int flags) {
            final EnumSet <CodeOption> result = EnumSet.noneOf (CodeOption.class);
            for (final CodeOption option : CodeOption.values ()) {
                if ((flags & option.__flag) != 0) {
                    result.add (option);
                }
            }

            return result;
        }
    }

    //

    private void __debug(final String format, final Object... args) {
        if (debug) {
            System.out.printf(format, args);
        }
    }

    private void __trace(final String format, final Object... args) {
        if (trace) {
            System.out.printf(format, args);
        }
    }
}
