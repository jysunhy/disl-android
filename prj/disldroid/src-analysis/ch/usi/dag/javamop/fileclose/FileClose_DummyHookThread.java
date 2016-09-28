package ch.usi.dag.javamop.fileclose;

public class FileClose_DummyHookThread extends Thread {



@Override
public void run(){
			FileCloseRuntimeMonitor.endProgEvent();
            //System.out.println("End Instructions");
		}

}