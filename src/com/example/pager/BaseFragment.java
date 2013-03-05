package com.example.pager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.WindowManager;


public class BaseFragment extends Fragment
{	
	public Display display;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Context cntx = this.getActivity().getBaseContext();
		WindowManager wm = (WindowManager) cntx.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		super.onCreate(savedInstanceState);
	}

}