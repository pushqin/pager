package com.example.pager;

import java.util.ArrayList;
import java.util.Map;

import com.example.pager.data.CategoryDbAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CategoryItemsFragment extends BaseFragment
{
	private Map<String, Long> categories;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.category_items_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Activity activ = this.getActivity();
		categories = CategoryDbAdapter.getInstance(activ.getApplicationContext()).getAllCategoryNames(null);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activ.getApplicationContext(), android.R.layout.simple_list_item_1,
		        new ArrayList<String>(categories.keySet()));
		ListView lv = (ListView) activ.findViewById(R.id.category_items_list);
		lv.setAdapter(adapter);
	}
}
