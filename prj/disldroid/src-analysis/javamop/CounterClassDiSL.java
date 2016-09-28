package javamop;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import ch.usi.dag.dislre.AREDispatch;

public class CounterClassDiSL{

    static ConcurrentMap<String,AtomicLong> joinPointMap = new ConcurrentHashMap<String,AtomicLong>();
    static ConcurrentMap<String,AtomicLong> sortedJoinPointMap;

    public static void countJoinPoints(final String mName){
    	joinPointMap.putIfAbsent(mName,new AtomicLong(0));
    	joinPointMap.get(mName).incrementAndGet();
        //System.out.println(mName +" = "+joinPointMap.get(mName));
    	AREDispatch.NativeLog (mName +" = "+joinPointMap.get(mName));
    }
/*
    static {
    	Runtime.getRuntime().addShutdownHook(new Thread(CounterClassDiSL::getStatistics));
    }

	public static void getStatistics(){
		sortedJoinPointMap = new ConcurrentSkipListMap<String,AtomicLong>(joinPointMap);
   		for (ConcurrentMap.Entry<String,AtomicLong> entry :sortedJoinPointMap.entrySet())
   		{
       		String key = entry.getKey();
       		AtomicLong value = (AtomicLong) entry.getValue();

			try {
				File file = new File("/Users/omarjaved/Results/JavaMOP_5052016/benchmarks/DiSLStatistics.txt");
				FileWriter fileWriter = new FileWriter(file,true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(key+","+value+"\n");
				bufferedWriter.close();
			}
			catch(IOException ex){ex.printStackTrace();}

		}

	}


*/






}
