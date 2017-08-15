package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.ui.adapter.robot.Alpha2WifiAdapter;
import com.ubtechinc.alpha2ctrlapp.util.DialogUtil;

import java.util.Iterator;
import java.util.List;

public class WifiActivity extends Activity implements  OnItemClickListener  {  
  
    private WifiManager wifiManager;  
    private  List<ScanResult> list;  
    private Context context ;
    private   ListView listView;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_wifi);
        context = this;
        DialogUtil.setDialogHalfSize(this);
        init();  
    }  
  
    private void init() {  
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
        openWifi(); 
        if(wifiManager.startScan()){
        	 list = wifiManager.getScanResults();  
              listView = (ListView) findViewById(R.id.wifilistView);
             if (list == null) {  
//                 Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();  
                 ToastUtils.showLongToast( R.string.wifi_closed);
                 this.finish();
             }else { 
             	Logger.i("nxy", "搜到的wifi 列表长度"+list.size());
                 if(list.size()==0){
                     ToastUtils.showLongToast( R.string.wifi_open_location_permission);
                	 this.finish();
                 }
                 if(wifiManager.getConnectionInfo().getBSSID()==null){
                     ToastUtils.showLongToast(R.string.error_message_network_unavailable);
                	this.finish();
                 }
                 cleanSSID();
               
             }  
             listView.setOnItemClickListener(this);
        }else{
        	Toast.makeText(this, "还没有开始扫描到", Toast.LENGTH_LONG).show();
        }
        
       
          
    }  
    
    private void cleanSSID(){
    	List<ScanResult> newList  = list;

        //去重
    	for(int i=0;i<list.size();i++){
    		for(int y=list.size()-1;y>i;y--){
    			if(i!=y){
    				if(list.get(i).SSID.equals(list.get(y).SSID)){
        				list.remove(y);
        			}
    			}
    			
    		}
    	}

        //去空
        if(newList != null) {
            Iterator<ScanResult> iterator = newList.iterator();
            while(iterator.hasNext()) {
                if(TextUtils.isEmpty(iterator.next().SSID)) {
                    iterator.remove();
                }
            }
        }
  
        listView.setAdapter(new Alpha2WifiAdapter(context,newList,wifiManager.getConnectionInfo().getBSSID()));
    }
    public void onBack(View v){
    	this.finish();
    }
      
    /** 
     *  打开WIFI 
     */  
    private void openWifi() {  
       if (!wifiManager.isWifiEnabled()) {  
        wifiManager.setWifiEnabled(true);  
       }  
        
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		Constants.ssID = list.get(position).SSID;
		Constants.WIFI_CAPA = list.get(position).capabilities;
        Intent intent = new Intent();
        intent.putExtra("SSID", list.get(position).SSID);
        intent.putExtra("capabilities", list.get(position).capabilities);
        setResult(RESULT_OK, intent);
	    WifiActivity.this.finish();
	}  
	
  
}  
