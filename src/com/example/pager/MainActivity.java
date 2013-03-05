package com.example.pager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends BaseActivity implements OnClickListener
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button b = (Button) findViewById(R.id.mainButton);
		b.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		
		Intent myIntent = new Intent(v.getContext(), Clicktestactivity.class);
		startActivityForResult(myIntent, v.getId());
		
	}
	
}
