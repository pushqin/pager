package com.example.pager;

import java.util.ArrayList;
import java.util.List;

import com.example.pager.data.PackageTypeDbAdapter;
import com.example.pager.data.PackagerDbAdapter;
import com.example.pager.data.RoomTypeDbAdapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class SpinnersFragment extends BaseFragment
{
	private Spinner spin_packageType, spin_roomType, spin_packager;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.spinners_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		addItemsToSpinners();
	}
	
	private void addItemsToSpinners()
	{
		spin_packageType = (Spinner) this.getActivity().findViewById(R.id.spinner_packageType);
		spin_roomType = (Spinner) this.getActivity().findViewById(R.id.spinner_roomType);
		spin_packager = (Spinner) this.getActivity().findViewById(R.id.spinner_packager);
		
		Cursor cursor = PackagerDbAdapter.getInstance(this.getActivity().getApplicationContext()).getAllPackagers();
		List<String> packagerslist = new ArrayList<String>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			// The Cursor is now set to the right position
			packagerslist.add(cursor.getString(1));
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_layout, packagerslist);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_packager.setAdapter(dataAdapter);
		LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(display.getWidth()/7,display.getHeight()/15);
		spin_packager.setLayoutParams(p2);
		
		List<String> packTypesList = PackageTypeDbAdapter.getInstance(this.getActivity().getApplicationContext()).getAllPackageTypeNames(null);
		dataAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_layout, packTypesList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_packageType.setAdapter(dataAdapter);
		//LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(display.getWidth()/7,display.getHeight()/5);
		spin_packageType.setLayoutParams(p2);
		
		List<String> roomTypesList = RoomTypeDbAdapter.getInstance(this.getActivity().getApplicationContext()).getAllRoomTypeNames(null);
		dataAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_layout, roomTypesList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_roomType.setAdapter(dataAdapter);
		//LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(display.getWidth()/7,display.getHeight()/5);
		spin_roomType.setLayoutParams(p2);
	}
}
