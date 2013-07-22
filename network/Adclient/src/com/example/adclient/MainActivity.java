package com.example.adclient;

import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void sendMsg(View view){
		StringBuffer strbuf = new StringBuffer();
		for (int i = 0; i < 1000; i++){
			strbuf.append("test");
		}
		String str = strbuf.toString();
		long startTime = new Date().getTime();
		for (int n = 0; n < 100; n++){
			new TCPClient("10.61.96.147", 51706).send(str);			
//			new TCPClient("192.168.1.135", 51706).send(str);			
		}
		long endTime = new Date().getTime();
        Log.d("TCP", "SendTime:"+(endTime-startTime));
	}

}
