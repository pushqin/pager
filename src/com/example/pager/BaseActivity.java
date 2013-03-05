package com.example.pager;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends FragmentActivity
{
	Display display;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
	    display = getWindowManager().getDefaultDisplay();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clicktestactivity, menu);
		// getActionBar().setHomeButtonEnabled(true);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		ColorDrawable Blue = new ColorDrawable(R.color.Blue);
		//Drawable myIcon = getResources().getDrawable( R.drawable.Blue );
		actionBar.setBackgroundDrawable(Blue);
		//int a = 2;
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		
		switch (item.getItemId())
		{
			case android.R.id.home:
				this.finish();
				// return true;
				// case R.id.help:
				// showHelp();
				// return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
}
