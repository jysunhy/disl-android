package ch.usi.dag.disl.staticcontext;

import ch.usi.dag.disl.snippet.Shadow;

/**
 * Provides simple implementation of StaticContext that holds the static context
 * data in a protected field.
 */
public abstract class AbstractStaticContext implements StaticContext {

	protected Shadow staticContextData;

	public void staticContextData(Shadow sa) {

		staticContextData = sa;
	}

}
