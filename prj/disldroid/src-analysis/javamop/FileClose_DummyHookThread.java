package javamop;
public class FileClose_DummyHookThread extends Thread {
   
 static{
 	
 	Runtime.getRuntime().addShutdownHook(new FileClose_DummyHookThread());
  	
  }
       
public void run(){
			FileCloseRuntimeMonitorDiSL.endProgEvent();
           
		}
    
}