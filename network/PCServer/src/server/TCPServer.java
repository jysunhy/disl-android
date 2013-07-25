package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


public class TCPServer implements Runnable{
	public final String SERVERIP;
    public final int SERVERPORT;
    
    private int order;
    private LinkedList<String> dataList = new LinkedList<String>();
    
    public TCPServer(String serverIP, int serverPort){
    	SERVERIP = serverIP;
    	SERVERPORT = serverPort;
    	order = 0;
    }
    
    //Check the order and hashcode
    //Return the msg string
    private String check(String str){
    	String strs[] = str.split(":");
    	if (Integer.parseInt(strs[0])!=order){
    		System.out.println("TCPServer: Order vilated! "+strs[0]+"(expect "+order+").");
    		return null;
    	}
    	order++;
    	if (strs[1].hashCode()!=Integer.parseInt(strs[2])){
    		System.out.println("TCPServer: HashCode check failed!");
    		return null;
    	}
    	return strs[1];
    	
    }
    @Override
    public void run() {
       try{
           System.out.println("TCPServer: Waiting...");
           ServerSocket serverSocket = new ServerSocket(SERVERPORT);
           while(true){
              Socket client = serverSocket.accept();
//              System.out.println("TCPServer: Receiving...");
              try{
                  BufferedReader in = new BufferedReader(new
                		  InputStreamReader(client.getInputStream()));
                  String str = in.readLine();
                  
//                  System.out.println("TCPServer: Received:'" + str + "'");
                  String msg = check(str);
                  if (msg != null)
                	  saveData(msg);
                 
              }catch(Exception e){
                  System.out.println("TCPServer: Wrong!");
                  e.printStackTrace();
              }
              finally{
                  client.close();
//                  System.out.println("TCPServer: Closed.");
              }
           }
       }catch(Exception e){
           System.out.println("TCPServer: Wrong!");
           e.printStackTrace();
       }
       
    }
    
    //Save data to dataList
    private void saveData(String data){
    	synchronized(dataList){
    		dataList.addLast(data);
    	}
    }
    
    public String receive(){
    	synchronized(dataList){
    		if (!dataList.isEmpty()){
    			String tmp = dataList.getFirst();
    			dataList.removeFirst();
    			return tmp;    		
    		}
    		else{
    			return null;
    		}
    	}
    		
    }
    
    //return the number of msg received
    public int getRecNum(){
    	return dataList.size();
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TCPServer server = new TCPServer("10.61.96.147", 51706);
		Thread serverThread = new Thread(server);
	    serverThread.start();
	    while (true){
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	int num = server.getRecNum();
	    	for (; num>0; num--){
	    		System.out.println(server.receive());	    		
	    	}
	    }
	}

}
