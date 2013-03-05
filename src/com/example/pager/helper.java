package com.example.pager;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class helper implements OnClickListener
{
	
	public static int buttonsNumber = 400;
	public static int buttonCounter = 0;
	
	private int numOfbuttonsInsideOneColumn = 9;
	private int numOfbuttonsInsideOneRow = 13;
	private int numOfPackagesToCreate = 100;
	public Button[] btns = new Button[400];
//	private boolean fCreated = false;
	
	public TableLayout createPageButtons(View rootView, Context context)
	{
		
		int counterOfButtons = 0;
		TableLayout tableLayout = (TableLayout) rootView;// new
														 // TableLayout(this.getActivity().getApplicationContext());
		// tableLayout.setLayoutParams(new
		// LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		int counter = 0;
		for (int i = 0; i < numOfbuttonsInsideOneColumn; i++)
		{
			// Create a TableRow and give it an ID
			TableRow tableRow = new TableRow(context);
			tableRow.setId(i);
			tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			for (int j = 0; j < numOfbuttonsInsideOneRow && counterOfButtons < numOfPackagesToCreate && counterOfButtons < buttonsNumber; j++)
			{
				
				// MainActivity.orderDetails.getPackagesList().add(pack);
				
				counterOfButtons++;
				Button btn = new Button(context);
				btns[counter] = btn;
				counter++;
				btn.setOnClickListener(this);
				
				btn.setLayoutParams(new TableRow.LayoutParams());
				ViewGroup.LayoutParams params = btn.getLayoutParams();
				params.height = 70;
				params.width = 98;
				btn.setLayoutParams(params);
				// btn.setLayoutParams()
				btn.setId(buttonCounter);
				btn.setText(String.valueOf(buttonCounter + 1));
				btn.setTextSize(35f);
				buttonCounter++;
				// btn.setBackgroundResource(drawableStateID);
				// btn.getBackground().setColorFilter(unpressedColor,
				// Mode.SRC_ATOP);
				// buttonsList.add(buttonControl);
				// Button b = new Button(context);
				tableRow.addView(btn);
			}
			tableLayout.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
		}
		return tableLayout;
	}
	
	@Override
	public void onClick(View v)
	{
		// Intent intent = new Intent();
		// intent.setClass(v.getContext(), Clicktestactivity.class);
		
		Intent intent = new Intent(v.getContext(), Clicktestactivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("packageNumber", v.getId() + 1);
		v.getContext().startActivity(intent);
		
		// Button a = (Button)v.findViewById(70);
//		Button b = btns[1];
		// b.setText("aa");
		v.setBackgroundResource(color.black);
		// b.setBackgroundResource(color.black);;
		
	}
	
}
