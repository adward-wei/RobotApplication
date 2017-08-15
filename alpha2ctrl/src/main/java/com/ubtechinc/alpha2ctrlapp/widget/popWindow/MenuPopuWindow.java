package com.ubtechinc.alpha2ctrlapp.widget.popWindow;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ButtonConfig;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ButtonConfigItem;
import com.ubtechinc.alpha2ctrlapp.presenter.ISpotifyLoginPresenter;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.MenuAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;

import java.util.ArrayList;
import java.util.List;

public class MenuPopuWindow extends Dialog implements
		View.OnClickListener {

	private List<ButtonConfigItem> buttonList = new ArrayList<ButtonConfigItem>();
	private GridView gridView;
	private MenuAdpter adpter;
	private BaseContactFragememt mframent;
	private TextView menuTitle, menuTips;
	private ImageView btn_setting;
	private Button btnLoginSpotify;
	private Activity context;
	private ImageView btn_close;

	public MenuPopuWindow(Activity context, BaseContactFragememt frament) {
		super(context, R.style.menutyle);
		this.context = context;
		this.setContentView(R.layout.menu_grid_view);
		this.mframent = frament;
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.BOTTOM);
		int screenWidth = context.getWindowManager().getDefaultDisplay()
				.getWidth();
		int screenHeight = context.getWindowManager().getDefaultDisplay()
				.getHeight();
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			screenHeight = screenHeight
					- resources.getDimensionPixelSize(resourceId);// 减去底部操作栏的高度
		}
		if (screenWidth < screenHeight) {
			lp.width = screenWidth;
			lp.height = (int) (screenHeight * (5.0 / 10.0));
		} else {
			lp.width = (int) (screenWidth * (4.0 / 5.0));
			lp.height = (int) (screenHeight * (3.0 / 4.0));
		}
		dialogWindow.setAttributes(lp);
		gridView = (GridView) this.findViewById(R.id.menu_grid);
		adpter = new MenuAdpter(context, buttonList, mframent);
		gridView.setAdapter(adpter);
		menuTips = (TextView) findViewById(R.id.menu_tips);
		menuTitle = (TextView) findViewById(R.id.menu_title);
		btn_setting = (ImageView) findViewById(R.id.btn_setting);
		btn_close = (ImageView)findViewById(R.id.btn_close);
		this.btnLoginSpotify = (Button)findViewById(R.id.btn_spotify_login);
//		this.btnLoginSpotify.setOnClickListener(this);
		this.setCanceledOnTouchOutside(true);
		setEvent();

	}

	public void setEvent() {
//		btn_setting.setOnClickListener(this);
	}

	public List<ButtonConfigItem> getButtonList() {
		return buttonList;
	}

	public void setButtonList(List<ButtonConfigItem> buttonList) {
		this.buttonList = buttonList;
	}

	public BaseContactFragememt getMframent() {
		return mframent;
	}

	public void setMframent(BaseContactFragememt mframent) {
		this.mframent = mframent;
	}

	public void refresh(ButtonConfig ss, String packgeName,
						BaseContactFragememt fragment, boolean hasSetting) {
		// this.buttonList.clear();

		if(packgeName.equals(ISpotifyLoginPresenter.COM_UBTECHINC_ALPHAENGLISHCHAT)){
			btnLoginSpotify.setVisibility(View.VISIBLE);
			this.buttonList.clear();
			adpter.packgeName = packgeName;
			adpter.onNotifyDataSetChanged(buttonList, fragment);
			menuTitle.setText("");
			menuTips.setText("");
			btn_setting.setVisibility(View.GONE);
			btn_close.setVisibility(View.GONE);
		}else{
			btnLoginSpotify.setVisibility(View.GONE);

			if (ss != null && ss.getModels().size()>0) {

				this.buttonList = ss.getModels();
				if (buttonList.size() <= 3)
					gridView.setNumColumns(buttonList.size());
				else
					gridView.setNumColumns(3);
				adpter.packgeName = packgeName;
				adpter.onNotifyDataSetChanged(buttonList, fragment);
				menuTitle.setText(ss.getTitle());
				menuTips.setText(ss.getDetails());
			} else {
				this.buttonList.clear();
				adpter.packgeName = packgeName;
				adpter.onNotifyDataSetChanged(buttonList, fragment);
				menuTitle.setText("");
				menuTips.setText("");
			}

			if (hasSetting)
				btn_setting.setVisibility(View.VISIBLE);
			else
				btn_setting.setVisibility(View.GONE);
			if(packgeName.equals("com.example.robottest")){ //趣声有关闭按钮
				btn_close.setVisibility(View.VISIBLE);
			}else{
				btn_close.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
			case R.id.btn_spotify_login:
//				LoadingDialog.getInstance(context).show();
//				SpotifyLoginPresenterImpl.getInstance().checkLoginState(ISpotifyLoginPresenter.COM_UBTECHINC_ALPHAENGLISHCHAT);
				break;
		default:

			break;
		}

	}

}
