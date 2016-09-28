package ch.usi.dag.javamop.hasnext;
public class Myruntimeanalysis {

 	static long countingPointcut;


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

	public static void setCountNum(final int ptNum){
		countingPointcut+=ptNum;
	}


	public static long getCountNum(){
		return countingPointcut;
	}
}



