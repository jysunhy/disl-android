package com.inspur.test2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import ch.usi.dag.dislre.*;

import java.util.LinkedList;

public class MainActivity extends Activity implements OnClickListener{

	Spinner spinner1, spinner2, spinner3;
	Button button1;
	TextView textView1;
	short methodId;
	short methodId2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//methodId = REDispatch.registerMethod("ch.usi.dag.disl.test.dispatch.CodeExecuted.testingBasic");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		spinner1 = (Spinner)findViewById(R.id.spinner1);
		spinner2 = (Spinner)findViewById(R.id.spinner2);
		spinner3 = (Spinner)findViewById(R.id.spinner3);
		button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(this);
		textView1 = (TextView)findViewById(R.id.textView1);
	}

	public int add(int x, int y){
		REDispatch.manuallyOpen();

		CodeExecutedRE.testingBasic(true, (byte) 125, 's', (short) 50000,
				100000, 10000000000L, 1.5F, 2.5);

		CodeExecutedRE.testingAdvanced("Corect transfer of String", "test", Object.class, Thread.currentThread());

		CodeExecutedRE.testingAdvanced2(new LinkedList<String>(),
				new LinkedList<Integer>(), new LinkedList[0], new int[0],
				int[].class, int.class, LinkedList.class,
				LinkedList.class.getClass());

		CodeExecutedRE.testingNull(null, null, null);
		//REDispatch.test();
		/*byte b = 125;
		  char c = 's';
		  short s = (short)50000;
		  long l = 10000000000L;
		  float f = 1.5F;
		  double d = 2.5;
		  for(int i = 0; i < 100; i++){
		  REDispatch.analysisStart(methodId);

		  REDispatch.sendBoolean(true);
		  REDispatch.sendByte(b);
		  REDispatch.sendChar(c);
		  REDispatch.sendShort(s);
		  REDispatch.sendInt(100000);
		  REDispatch.sendLong(l);
		  REDispatch.sendFloat(f);
		  REDispatch.sendDouble(d);

		  REDispatch.analysisEnd();
		  }
		//REDispatch.manuallyClose();*/
		return REDispatch.add(x,y);
	}
	public int substraction(int x, int y){
		REDispatch.manuallyClose();
		/*methodId2 = REDispatch.registerMethod("ch.usi.dag.disl.test.dispatch.CodeExecuted.testingAdvanced");
		  REDispatch.analysisStart(methodId2);
		//"Corect transfer of String", "test", Object.class, Thread.currentThread()
		REDispatch.sendObjectPlusData("Corect transfer of String");
		REDispatch.sendObject("test");
		REDispatch.sendObject(Object.class);
		REDispatch.sendObjectPlusData(Thread.currentThread());

		REDispatch.analysisEnd();*/
		return x-y;
	}
	public float multiplication(int x, int y){
		return x*y;
	}
	public float division(int x, int y){
		return x/y;
	}

	//   static{
	//   	System.loadLibrary("shadowvm");
	//   }

	private int getInt(Spinner s)
	{
		return Integer.parseInt(s.getSelectedItem().toString());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.button1:
				String str = "";
				System.out.println("Spinner1:"+spinner1.getSelectedItem().toString()+"   Spinner2:"+spinner2.getSelectedItem().toString()+"   Spinner3:"+spinner3.getSelectedItem().toString());
				if (spinner3.getSelectedItem().toString().equals("+"))
				{
					str = String.valueOf(add(getInt(spinner1) , getInt(spinner2)));
				}else if (spinner3.getSelectedItem().toString().equals("-"))
				{
					str = String.valueOf(substraction(getInt(spinner1) , getInt(spinner2)));
				}else if (spinner3.getSelectedItem().toString().equals("*"))
				{
					str = String.valueOf(multiplication(getInt(spinner1) , getInt(spinner2)));
				}else if (spinner3.getSelectedItem().toString().equals("/"))
				{
					str = String.valueOf(division(getInt(spinner1) , getInt(spinner2)));
				}
				System.out.println(str);
				textView1.setText(str+"HAIYANG");
				break;
		}
	}
}
