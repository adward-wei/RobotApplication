package com.ubtechinc.alpha2ctrlapp.ui.fragment.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.data.user.CollectReponsitory;
import com.ubtechinc.alpha2ctrlapp.data.user.ICollectDataSource;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.MyFavoriteInfo;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.MyFavoriteActionAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.shop.MyFavoriteAppAdpter;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.widget.SwtichChangeListButton;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MyFavorite
 * @date 6/6/2017
 * @author tanghongyu
 * @Description 我的收藏页
 * @modifier
 * @modify_time
 */
public class MyFavorite extends BaseContactFragememt implements SwtichChangeListButton.OnSwitchChangedActiveListener {

	private String TAG = "MyFavorite";
	public SwtichChangeListButton btn_swich_active;
	private MyFavoriteActionAdpter adpter;
	private MyFavoriteAppAdpter appadper;
	private List<MyFavoriteInfo> infoList = new ArrayList<MyFavoriteInfo>();
	private List<MyFavoriteInfo> actionList = new ArrayList<MyFavoriteInfo>();
	private List<MyFavoriteInfo> appList = new ArrayList<MyFavoriteInfo>();
	private int type =0;
	private ListView  listView ;
	private LinearLayout no_collect_tip;
	@Override
	public View onCreateFragmentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.my_favo, container, false);
	}

	@Override
	public void initView()  {
		btn_swich_active = (SwtichChangeListButton) mMainPageActivity.findViewById(R.id.btn_swich_list);
		btn_swich_active.setChangedLanListener(this);
		listView = (ListView)mContentView.findViewById(R.id.lst_actions);
		appadper = new MyFavoriteAppAdpter(mActivity, appList);
		adpter = new MyFavoriteActionAdpter(mActivity, actionList);
		title.setText(getString(R.string.left_menu_my_like));
		no_collect_tip = (LinearLayout)mContentView.findViewById(R.id.no_collect_tip);
		mMainPageActivity.bottomLay.setVisibility(View.GONE);
	}


	  private void checkCollector(){


		  CollectReponsitory.get().getCollectionList(BusinessConstants.APP_TYPE, new ICollectDataSource.LoadCollectionCallback() {
              @Override
              public void onSuccess(List<MyFavoriteInfo> response) {
                  infoList.addAll(response);
                  refreshList ();
              }

              @Override
              public void onFail(ThrowableWrapper e) {

              }
          });


	    }
	  
	  private void refreshList (){
		  if(infoList.size()>0){
			  no_collect_tip.setVisibility(View.GONE);
			  listView.setVisibility(View.VISIBLE);
			  actionList.clear();
			  appList.clear();
			  for(int i=0;i<infoList.size();i++){
				  if(infoList.get(i).getCollectType()==1){
					  actionList.add(infoList.get(i));
				  }else{
					  appList.add(infoList.get(i));
				  }
			  }
		  }else{
			  no_collect_tip.setVisibility(View.VISIBLE);
			  listView.setVisibility(View.GONE);
		  }
		 refreshAdper(type);
		  
		  
	  }
	  private void refreshAdper(int type){
		  if(type ==0){
			  listView.setAdapter(appadper);
			  listView.setOnItemClickListener(appadper);
			  appadper.onNotifyDataSetChanged(appList);
		  }else{
			  listView.setAdapter(adpter);
			  listView.setOnItemClickListener(adpter);
			  appadper.onNotifyDataSetChanged(actionList);
		  }
	  }
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	public void onResume() {
		super.onResume();
		mMainPageActivity.currentFragment = this;
		mMainPageActivity.mainTopView.setVisibility(View.GONE);
//		mMainPageActivity.btn_swich_active.setVisibility(View.GONE);
		type = 0;
		checkCollector();
	}

	@Override
	public void onChangeActive(Integer index) {

		switch (index) {
		case 0:
			type =0;
			 refreshAdper(type);
			break;
		case 1:
			type = 1;
			refreshAdper(type);
			break;
		default:
			break;
		}
	}
	
}
