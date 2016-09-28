package javamop;
import java.io.*;
public class FileWriterDebug{




//public static void dumpInfo(String fileName, Throwable info){

public static void dumpInfo(String fileName, Object info){
	if(info instanceof Throwable){
		 info = (Throwable) info;
	}else if(info instanceof String){
		 info = (String) info;
	}	
	

	 try {
 		File file = new File(fileName);
 		FileWriter fw = new FileWriter(file,true);
 
		BufferedWriter bufferedWriter = new BufferedWriter(fw);                             
 		bufferedWriter.write("================================="+"\n");
 		bufferedWriter.write("Dumped data is="+info+"\n");        
 		bufferedWriter.write("================================="+"\n");
 
bufferedWriter.close();
 } catch(IOException ex){ex.printStackTrace();}




}





}

