package com.example.pager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.pager.data.CategoryDbAdapter;

public class CategoryButtonsFragment extends BaseFragment
{
	private GridView gridView;
	private Map<String, Long> categoriesList;
	private List<String> categoryNames;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.categories_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		gridView = (GridView) this.getActivity().findViewById(R.id.categories_grid);
		categoriesList = CategoryDbAdapter.getInstance(this.getActivity().getApplicationContext()).getAllCategoryNames(null);
		categoryNames = new ArrayList<String>(categoriesList.keySet());
		
		gridView.setAdapter(new ButtonAdapter(this.getActivity().getApplicationContext()));
		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				Intent intent = new Intent(v.getContext(), ItemsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("packageNumber", v.getId() + 1);
				v.getContext().startActivity(intent);
				Toast.makeText(getActivity().getApplicationContext() , "pic" + (position + 1) + " selected", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public class ButtonAdapter extends BaseAdapter
	{
		private Context context;
		
		public ButtonAdapter(Context c)
		{
			context = c;
		}
		
		public int getCount()
		{
			return categoriesList.size();
		}
		
		public Object getItem(int position)
		{
			return position;
		}
		
		public long getItemId(int position)
		{
			return position;
		}
		
		@SuppressLint("ResourceAsColor")
		public View getView(int position, View convertView, ViewGroup parent)
		{
	       Button btn;  
           if (convertView == null) 
           {   
   
	            btn = new Button(context);  
	            //width, height.
	            btn.setLayoutParams(new GridView.LayoutParams(display.getWidth()*5/7/4-4,(int) (display.getHeight()/4.4)));  
	            //btn.setLayoutParams(new GridView.LayoutParams(235,165));  
	            btn.setPadding(8, 8, 8, 8);  
	            btn.setBackgroundColor(R.color.Red);
	            btn.setFocusable(false);
	            btn.setClickable(false);
           }
           else 
           {  
        	   btn = (Button) convertView;  
           }               
           btn.setText(categoryNames.get(position));    
           btn.setTextColor(Color.WHITE);  
           btn.setId(position);       
           return btn; 
		}
	}
}
