package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ubtechinc.alpha2ctrlapp.R;

public class CountyCodeSpinner extends Spinner{
	private Context mContext;
	public CountyCodeSpinner(Context context) {
		super(context);
		this.mContext = context;
		// TODO Auto-generated constructor stub
	}

	public CountyCodeSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		 String[] rl=mContext.getResources().getStringArray(R.array.CountryCodes);
		 ArrayAdapter<String> adapter;		
		 adapter=new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, rl);
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 this.setAdapter(adapter);
		 this.setSelection(35);
	}
	

}
