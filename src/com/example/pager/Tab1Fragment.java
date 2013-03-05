package com.example.pager;

import java.util.ArrayList;
import java.util.List;

import com.example.pager.CategoryButtonsFragment.ButtonAdapter;
import com.example.pager.data.CategoryDbAdapter;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author mwho
 * 
 */
public class Tab1Fragment extends BaseFragment implements OnClickListener
{
	private GridView gridView;
	private List<String> categoriesList;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.tab_frag1_layout, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		gridView = (GridView) this.getActivity().findViewById(R.id.grdTab1);
		categoriesList = new  ArrayList<String>();
		for (int i = 1; i < 101; i++) {
			categoriesList.add(Integer.toString(i));
		}
		//categoriesList = CategoryDbAdapter.getInstance(this.getActivity().getApplicationContext()).getAllCategoryNames(null);
		
		gridView.setAdapter(new ButtonAdapter(this.getActivity().getApplicationContext()));
		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				
				Toast.makeText(getActivity().getApplicationContext() , "pic" + (position + 1) + " selected", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(v.getContext(), Clicktestactivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("packageNumber", v.getId() + 1);
				v.getContext().startActivity(intent);
				v.setBackgroundResource(color.black);
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
	            btn.setLayoutParams(new GridView.LayoutParams(display.getWidth()*5/7/11,(int) (display.getHeight()/9)+7));  
	           // btn.setLayoutParams(new GridView.LayoutParams(235,165));  
	            btn.setPadding(8, 8, 8, 8);  
	            btn.setBackgroundColor(R.color.Red);
	            btn.setFocusable(false);
	            btn.setClickable(false);
           }
           else 
           {  
        	   btn = (Button) convertView;  
           }               
           btn.setText(categoriesList.get(position));    
           btn.setTextColor(Color.WHITE);  
           btn.setId(position);       
           return btn; 
		}
	}

	@Override
	public void onClick(View v) {

		
	}
}

//	
////	private static final Activity TabsViewPagerFragmentActivity = null;
//	
//	/**
//	 * (non-Javadoc)
//	 * 
//	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
//	 *      android.view.ViewGroup, android.os.Bundle)
//	 */
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		helper hlp = new helper();
//		TableLayout layout = (TableLayout) inflater.inflate(R.layout.tab_frag1_layout, container, false);
//		hlp.createPageButtons(layout, this.getActivity().getApplicationContext());
//		
//		// TableLayout layout =
//		// (TableLayout)inflater.inflate(R.layout.tab_frag1_layout, container,
//		// false);
//		Button btn = new Button(this.getActivity().getApplicationContext());
//		btn.setText("text");
//		
//		btn.setOnClickListener(this);
//		btn.setId(1);
//		layout.addView(btn);
//		if (container == null)
//		{
//			// We have different layouts, and in one of them this
//			// fragment's containing frame doesn't exist. The fragment
//			// may still be created from its saved state, but there is
//			// no reason to try to create its view hierarchy because it
//			// won't be displayed. Note this is not needed -- we could
//			// just run the code below, where we would create and return
//			// the view hierarchy; it would just never be used.
//			return null;
//		}
//		
//		return layout;
//		// return (TableLayout)inflater.inflate(R.layout.tab_frag1_layout,
//		// container, false);
//		
//	}
//	
//	@Override
//	public void onClick(View v)
//	{
//		Intent myIntent = new Intent(v.getContext(), Clicktestactivity.class);
//		
//		startActivityForResult(myIntent, v.getId());
//		
//	}

