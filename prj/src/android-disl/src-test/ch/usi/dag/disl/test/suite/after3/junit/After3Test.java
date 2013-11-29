package ch.usi.dag.disl.test.suite.after3.junit;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.usi.dag.disl.test.utils.*;

@RunWith(JUnit4.class)
public class After3Test {

	ClientServerRunner r = new ClientServerRunner(this.getClass());
	
	@Test
	public void test() 
			throws Exception {		
		r.start();			
		r.waitFor();
		r.assertIsFinished();
		if(Boolean.parseBoolean(System.getProperty("disl.test.verbose"))) { r.destroyIfRunningAndFlushOutputs(); }
		r.assertIsSuccessfull();
		r.assertClientOut("client.out.resource");
		r.assertClientErrNull();
		r.assertServerOutNull();
		r.assertServerErrNull();
	}	
	
	@After
	public void cleanup() {
		r.destroy();
	}
}

