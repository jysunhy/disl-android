package com.example.adclient;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

public class TCPClient extends AsyncTask<String, String, String>{

	public final String SERVERIP;
	public final int SERVERPORT;
	
	private static int order = 0;
	
	public TCPClient(String serverIP, int serverPort){
		SERVERIP = serverIP;
		SERVERPORT = serverPort;
	}
	
	public void send(String msg){
		this.execute(msg);
	}
	
	private synchronized String packet(String msg){
		return (order++)+":"+msg+":"+msg.hashCode();
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		try{
		    InetAddress serverAddr = InetAddress.getByName(SERVERIP);
//		       Log.d("TCP", "TCPClient: Connecting...");
		       Socket socket = new Socket(serverAddr, SERVERPORT);
		       String message = packet(arg0[0]);
		       try {  
//		    	   Log.d("TCP", "TCPClient: Sending '"+message+"'");
		    	   PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
		           out.println(message);
		        } catch (Exception e) {
		        Log.e("TCP", "TCPClient: Wrong! ",e);
		    }finally{
		        socket.close();
		    }
		    }catch(Exception e){
		       Log.e("TCP", "TCPClient: Wrong! ",e);
		    }
		return null;
	}

}
