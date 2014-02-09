package spec.benchmarks._202_jess;
import spec.harness.*;

public class Main implements SpecBenchmark {

    static long runBenchmark( String[] args ) {
    
        int speed =  spec.harness.Context.getSpeed();   

        if( speed == 100 ) {
	    args = new String[1];
	    args[0] = spec.Constants.DIR_INPUT+"input.jess/wordgames.clp";
	}
        if( speed == 10 ) {
	    args = new String[1];
	    args[0] = spec.Constants.DIR_INPUT+"input.jess/zebra.clp";
	}
        if( speed == 1  ) {
	    args = new String[1];
	    args[0] = spec.Constants.DIR_INPUT+"input.jess/fullmab.clp";
	}
	
	return new Jess().inst_main( args );
    }


    public static void main( String[] args ) {  	 
        runBenchmark( args );
    }

    
    public long harnessMain( String[] args ) {
        return runBenchmark( args );
    }

  
}
