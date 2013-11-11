package com.inspur.test2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	Spinner spinner1, spinner2, spinner3;
	Button button1;
	TextView textView1;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        spinner3 = (Spinner)findViewById(R.id.spinner3);
        button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(this);
        textView1 = (TextView)findViewById(R.id.textView1);
        
    }
    
    public native int add(int x, int y);
    public native int substraction(int x, int y);
    public native float multiplication(int x, int y);
    public native float division(int x, int y);
    

    static{
    	System.loadLibrary("arithmetic");
    }

    
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