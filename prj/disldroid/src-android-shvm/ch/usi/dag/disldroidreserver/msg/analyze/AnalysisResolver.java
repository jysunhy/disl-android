package ch.usi.dag.disldroidreserver.msg.analyze;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ch.usi.dag.disldroidreserver.exception.DiSLREServerException;
import ch.usi.dag.disldroidreserver.exception.DiSLREServerFatalException;
import ch.usi.dag.disldroidreserver.remoteanalysis.RemoteAnalysis;
import ch.usi.dag.disldroidreserver.shadow.ShadowAddressSpace;

public final class AnalysisResolver {
	private static final String METHOD_DELIM = ".";

	private static class MethodMap extends ConcurrentHashMap <Short, AnalysisMethodHolder>{

	}

	//private static final ConcurrentHashMap <Short, AnalysisMethodHolder>
	//	methodMap = new ConcurrentHashMap <Short, AnalysisMethodHolder> ();
	private static ConcurrentHashMap<Integer, MethodMap> methodMaps = new ConcurrentHashMap <Integer, AnalysisResolver.MethodMap> ();


	private static final ConcurrentHashMap <String, RemoteAnalysis>
		analysisMap = new ConcurrentHashMap <String, RemoteAnalysis> ();

	// for fast set access - contains all values from analysisMap
	private static final Set <RemoteAnalysis>
		analysisSet = new HashSet <RemoteAnalysis> ();

	//

	public static final class AnalysisMethodHolder {
		private final RemoteAnalysis analysisInstance;
		private final Method analysisMethod;

		public AnalysisMethodHolder(
			final RemoteAnalysis analysisInstance, final Method analysisMethod
		) {
			this.analysisInstance = analysisInstance;
			this.analysisMethod = analysisMethod;
		}

		public RemoteAnalysis getAnalysisInstance() {
			return analysisInstance;
		}

		public Method getAnalysisMethod() {
			return analysisMethod;
		}
	}

	//

	private static AnalysisMethodHolder resolveMethod (final String methodStr
	) throws DiSLREServerException {
		try {
			final int classNameEnd = methodStr.lastIndexOf (METHOD_DELIM);

			// without METHOD_DELIM
			final String className = methodStr.substring (0, classNameEnd);
			final String methodName = methodStr.substring (classNameEnd + 1);

			// resolve analysis instance
			RemoteAnalysis raInst = analysisMap.get (className);
			if (raInst == null) {
				// resolve class
				final Class <?> raClass = Class.forName (className);

				// create instance
				raInst = (RemoteAnalysis) raClass.newInstance ();
				RemoteAnalysis tmp;

                if ((tmp = analysisMap.putIfAbsent (className, raInst)) == null) {
                    analysisSet.add (raInst);
                } else {
                    raInst = tmp;
                }
			}

			// resolve analysis method
			final Method raMethod = __getAnalysisMethod (raInst, methodName);

//            if (Forkable.class.isAssignableFrom (raInst.getClass ())) {
//                final Class <?> [] argTypes = raMethod.getParameterTypes ();
//
//                if (!argTypes [argTypes.length - 1].equals (Context.class)) {
//                    throw new DiSLREServerFatalException ("Analysis method "
//                        + methodStr + " does not accept Context as an argument");
//                }
//            }

			return new AnalysisMethodHolder(raInst, raMethod);
		}

		catch (final ClassNotFoundException e) {
			throw new DiSLREServerException(e);
		} catch (final InstantiationException e) {
			throw new DiSLREServerException(e);
		} catch (final IllegalAccessException e) {
			throw new DiSLREServerException(e);
		}
	}

	private static Method __getAnalysisMethod (
		final RemoteAnalysis analysis, final String methodName
	) throws DiSLREServerException {
		final Class <?> analysisClass = analysis.getClass ();

		final List <Method> methods = new ArrayList <Method> ();
		for (final Method analysisMethod : analysisClass.getMethods ()) {
			if (analysisMethod.getName ().equals (methodName)) {
				methods.add (analysisMethod);
			}
		}

		//
		// Throw an exception if there are multiple methods
		//
		final int methodCount = methods.size ();
		if (methodCount == 1) {
			return methods.get (0);

		} else if (methodCount > 1) {
			throw new DiSLREServerException (String.format (
				"Multiple methods matching \"%s\" found in %s",
				methodName, analysisClass.getName ()
			));
		} else {
			throw new DiSLREServerException (String.format (
				"No method matching \"%s\" found in %s",
				methodName, analysisClass.getName ()
			));
		}
	}


	static AnalysisMethodHolder getMethod (int pid, final short methodId)
	throws DiSLREServerException {

	    if(!methodMaps.containsKey (pid)) {
	        final ShadowAddressSpace parent = ShadowAddressSpace.getShadowAddressSpaceNoCreate (pid).getParent ();
	        if(parent == null) {
                throw new DiSLREServerFatalException ("Unknown method id: "+ methodId + " in "+pid);
            }else {
                pid = parent.getContext ().pid ();
            }
	    }

	    final MethodMap methodMap = methodMaps.get (pid);

		final AnalysisMethodHolder result = methodMap.get (methodId);
		if (result == null) {
			throw new DiSLREServerFatalException ("Unknown method id: "+ methodId);
		}

		return result;
	}


	public static void registerMethodId (
		final int pid, final short methodId, final String methodString
	) throws DiSLREServerException {
		//methodMap.putIfAbsent (methodId, resolveMethod(methodString));
	    final MethodMap temp = new MethodMap ();
	    MethodMap res = methodMaps.putIfAbsent (pid, temp);
	    if(res == null) {
            res = temp;
        }
	    res.putIfAbsent (methodId, resolveMethod(methodString));
	}


	public static Set <RemoteAnalysis> getAllAnalyses () {
		return analysisSet;
	}
}
