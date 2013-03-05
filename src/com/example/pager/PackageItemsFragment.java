package com.example.pager;

import java.util.ArrayList;
import java.util.List;

import com.example.pager.data.PackageTypeDbAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
public class PackageItemsFragment extends BaseFragment
{	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	  return inflater.inflate(R.layout.package_items_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Intent intent = this.getActivity().getIntent();
		int packageNumber = intent.getIntExtra("packageNumber", 0);
		TextView tv = (TextView) this.getActivity().findViewById(R.id.text_packageName);
		tv.setText(packageNumber == 0 ? "" : Integer.toString(packageNumber));
		
		
		  super.onActivityCreated(savedInstanceState);

	        //this.getListView().setTextFilterEnabled(true);
	        //String[] items = new String[] {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};
	        //this.setListAdapter((ListAdapter) new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items)); // your list adapter
	  

	}
}