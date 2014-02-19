import java.util.LinkedList;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BasicBlockMarker;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.dislre.REDispatch;

public class DiSLClass {

    @Before(marker = BodyMarker.class, scope = "Main.main")
    public static void fork() {
        try {
            REDispatch.fork ();
            Thread.sleep (1000);
        } catch (final InterruptedException e) {
        }
    }

	@After(marker = BasicBlockMarker.class, scope = "Main.*")
	public static void invokedInstr(final CodeLengthSC clsc) {

		CodeExecutedRE.bytecodesExecuted(clsc.codeSize());
	}

	@After(marker = BodyMarker.class, scope = "Main.main")
	public static void testing() {

        CodeExecutedRE.testingBasic(true, (byte) 125, 's', (short) 50000, 100000, 10000000000L);

		CodeExecutedRE.testingAdvanced("Corect transfer of String", "test", Object.class, Thread.currentThread());

		CodeExecutedRE.testingAdvanced2(new LinkedList<String>(),
				new LinkedList<Integer>(), new LinkedList[0], new int[0],
				int[].class, int.class, LinkedList.class,
				LinkedList.class.getClass());

		CodeExecutedRE.testingNull(null, null, null);
	}
}
