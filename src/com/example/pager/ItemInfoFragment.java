package com.example.pager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.pager.data.CategoryDbAdapter;
import com.example.pager.data.ConditionDbAdapter;
import com.example.pager.data.PackagerDbAdapter;

public class ItemInfoFragment extends BaseFragment
{
	private Map<String, Long> conditions;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.items_info_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Activity activ = this.getActivity();
		conditions = CategoryDbAdapter.getInstance(activ.getApplicationContext()).getAllCategoryNames(null);
		
		Spinner spinner = (Spinner) this.getActivity().findViewById(R.id.spinner_item_condition);
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_layout);
		dataAdapter.add("");
		dataAdapter.addAll(new ArrayList<String>(conditions.keySet()));
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}
}
