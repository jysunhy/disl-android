package javamop;
public class Myruntimeanalysis {
   
    static{
        
        Runtime.getRuntime().addShutdownHook(new FileClose_DummyHookThread());   
        
    }
    
        
    static Myruntimeanalysis r=null;
    
    public static   Myruntimeanalysis getInstance(){
           
           if(r!=null){
               return r;                   
           }
           else{
                r = new Myruntimeanalysis();                                
				return r;
                }
                 
           
           
       }
    
}



