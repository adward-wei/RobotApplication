package com.ubtechinc.alpha2ctrlapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.app.AppConfig;
import com.ubtechinc.alpha2ctrlapp.entity.business.app.AppConfigItem;
import com.ubtechinc.alpha2ctrlapp.entity.business.app.AppDatas;
import com.ubtechinc.alpha2ctrlapp.entity.business.app.AppItemGroup;

import java.util.ArrayList;
import java.util.List;


public class ConfigView extends LinearLayout {

	private Context ctx;
	private List<View> views = new ArrayList<View>();
	private float dp;
	private int left;
	private int right;

	private List<AppItemGroup> group;
	private String version;
	private List<String> idList = new ArrayList<String>();
	private ConfigListener mConfigListener;

	public ConfigView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);

	}

	public ConfigView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);

	}

	public ConfigView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context ctx) {
		if (isInEditMode())
			return;
		this.ctx = ctx;
		DisplayMetrics metric = ctx.getApplicationContext().getResources()
				.getDisplayMetrics();
		dp = metric.density;
		left = right = (int) (10 * dp);
	}

	public void setData(AppConfig config, AppDatas datas) {
		remove();
		if (datas == null) {
			noDatas(config);
		} else {
			hasDatas(config, datas);
		}
	}

	public void hasDatas(AppConfig config, AppDatas datas) {

		try {
			int index = 0;
			List<AppConfigItem> data = datas.getDatas();
			version = config.getVersion();
			if (!version.equals(datas.getVersion())) {
				return;
			}
			group = config.getModels();
			int groupSize = group.size();
			for (int j = 0; j < groupSize; j++) {
				AppItemGroup g = group.get(j);
				List<AppConfigItem> app = g.getItem();
				for (int i = 0; i < app.size(); i++, index++) {
					View layout = LayoutInflater.from(ctx).inflate(
							R.layout.card_view, null, false);
					TextView top = (TextView) layout.findViewById(R.id.top);
					TextView bottom = (TextView) layout
							.findViewById(R.id.bottom);

					if (i != 0) {
						top.setVisibility(View.INVISIBLE);
					}
					if (i == 0) {
						LayoutParams lp = new LayoutParams(
								bottom.getLayoutParams());
						int topLen = (int) (dp * Integer.parseInt(g
								.getMarginTop()));
						lp.setMargins(0, topLen, 0, 0);
						top.setLayoutParams(lp);
					}
					if (i < app.size() - 1) {
						LayoutParams lp = new LayoutParams(
								bottom.getLayoutParams());
						lp.setMargins(left, 0, right, 0);
						bottom.setLayoutParams(lp);
					}
					TextView textView1 = (TextView) layout
							.findViewById(R.id.textView1);
					EditText editText1 = (EditText) layout
							.findViewById(R.id.editText1);
					textView1.setText(app.get(i).getTextView());
					editText1.setText(app.get(i).getEditText());

					if(index<data.size()){
						AppConfigItem appData = data.get(index);
						if(appData.getId().equals(app.get(i).getId())){
							editText1.setText(appData.getEditText());
						}
					}

					views.add(layout);
					addView(layout);
				}
			}
			if(mConfigListener!=null){
				mConfigListener.isInitSuccess(true);
			}
		} catch (Exception e) {
			remove();
		}

	}

	public void noDatas(AppConfig config) {
		try {
			version = config.getVersion();
			group = config.getModels();
			int groupSize = group.size();
			for (int j = 0; j < groupSize; j++) {
				AppItemGroup g = group.get(j);
				List<AppConfigItem> app = g.getItem();
				for (int i = 0; i < app.size(); i++) {
					View layout = LayoutInflater.from(ctx).inflate(
							R.layout.card_view, null, false);
					TextView top = (TextView) layout.findViewById(R.id.top);
					TextView bottom = (TextView) layout
							.findViewById(R.id.bottom);

					if (i != 0) {
						top.setVisibility(View.INVISIBLE);

					}
					if (i == 0) {
						LayoutParams lp = new LayoutParams(
								bottom.getLayoutParams());
						int topLen = (int) (dp * Integer.parseInt(g
								.getMarginTop()));
						lp.setMargins(0, topLen, 0, 0);
						top.setLayoutParams(lp);
					}
					if (i < app.size() - 1) {
						LayoutParams lp = new LayoutParams(
								bottom.getLayoutParams());
						lp.setMargins(left, 0, right, 0);
						bottom.setLayoutParams(lp);
					}
					TextView textView1 = (TextView) layout
							.findViewById(R.id.textView1);
					EditText editText1 = (EditText) layout
							.findViewById(R.id.editText1);
					textView1.setText(app.get(i).getTextView());
					editText1.setText(app.get(i).getEditText());

					views.add(layout);
					addView(layout);
				}
			}
			if(mConfigListener!=null){
				mConfigListener.isInitSuccess(true);
			}
		} catch (Exception e) {
			remove();
		}
	}

	public AppConfig getData() {

		int groupSize = group.size();
		int viewsIndex = 0;
		for (int j = 0; j < groupSize; j++) {
			AppItemGroup g = group.get(j);
			List<AppConfigItem> items = g.getItem();
			int itemSize = items.size();
			for (int i = 0; i < itemSize; i++, viewsIndex++) {
				AppConfigItem app = items.get(i);
				View view = getChildAt(viewsIndex);
				View view2 = ((ViewGroup) view).getChildAt(1);
				TextView xx = (TextView) ((ViewGroup) view2).getChildAt(0);
				EditText xxx = (EditText) ((ViewGroup) view2).getChildAt(1);

				Log.i("ConfigView", "TextView=" + xx.getText());
				Log.i("ConfigView", "EditText=" + xxx.getText());
				app.setTextView(xx.getText().toString().trim());
				app.setEditText(xxx.getText().toString().trim());
				items.set(i, app);
			}
			group.set(j, g);
		}
		AppConfig config = new AppConfig();
		config.setVersion(version);
		config.setModels(group);
		return config;
	}

	public AppDatas getData2() {
		List<AppConfigItem> data = new ArrayList<AppConfigItem>();
		int groupSize = group.size();
		int viewsIndex = 0;
		for (int j = 0; j < groupSize; j++) {
			AppItemGroup g = group.get(j);
			List<AppConfigItem> items = g.getItem();
			int itemSize = items.size();
			for (int i = 0; i < itemSize; i++, viewsIndex++) {
				AppConfigItem app = items.get(i);
				View view = getChildAt(viewsIndex);
				View view2 = ((ViewGroup) view).getChildAt(1);
				EditText xxx = (EditText) ((ViewGroup) view2).getChildAt(1);
				app.setEditText(xxx.getText().toString().trim());
				data.add(app);
			}
		}
		AppDatas datas = new AppDatas();
		datas.setVersion(version);
		datas.setDatas(data);
		return datas;
	}

	public void remove() {
		if (views.size() != 0) {
			removeAllViews();
			views.clear();
			group.clear();
		}
	}

	public interface ConfigListener {
		public void isInitSuccess(boolean success);
	}

	public void setListener(ConfigListener mConfigListener) {
		// TODO Auto-generated method stub
		this.mConfigListener = mConfigListener;
	}
}
