package javamop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import ch.usi.dag.dislre.AREDispatch;

public class CounterClassDiSL{

    static ConcurrentMap<String,AtomicLong> joinPointMap = new ConcurrentHashMap<String,AtomicLong>();
    static ConcurrentMap<String,AtomicLong> sortedJoinPointMap;

    public static void countJoinPoints(final String mName){

    	joinPointMap.putIfAbsent(mName,new AtomicLong(0));
    	joinPointMap.get(mName).incrementAndGet();
    	AREDispatch.NativeLog ("JAVAMOP: "+mName+" "+joinPointMap.get(mName).get());
    }

	public static void getStatistics(){
		sortedJoinPointMap = new ConcurrentSkipListMap<String,AtomicLong>(joinPointMap);
   		for (final ConcurrentMap.Entry<String,AtomicLong> entry :sortedJoinPointMap.entrySet())
   		{
       		final String key = entry.getKey();
       		final AtomicLong value = entry.getValue();

			try {
				final File file = new File("DiSLStatistics.txt");
				final FileWriter fileWriter = new FileWriter(file,true);
				final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(key+","+value+"\n");
				bufferedWriter.close();
			}
			catch(final IOException ex){ex.printStackTrace();}

		}

	}









}
