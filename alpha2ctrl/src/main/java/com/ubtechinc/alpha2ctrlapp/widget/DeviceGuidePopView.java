package com.ubtechinc.alpha2ctrlapp.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ubtech.utilcode.utils.SPUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;

public class DeviceGuidePopView extends Dialog implements
        View.OnClickListener {

    private Activity context;

    private OnConfirmListener confirmListener;
    private boolean isOwner =false;
    private RelativeLayout  appLay,bottomLay;
    private Button btn_app,btn_bottom;
    private boolean hasShowBind =false;

    public DeviceGuidePopView(Activity context,boolean hasConnect) {
        super(context, R.style.deleteDialog);
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
        }
        this.setContentView(R.layout.device_guide);
        this.isOwner = hasConnect;
        appLay =(RelativeLayout)findViewById(R.id.pop_app);
        btn_app= (Button)findViewById(R.id.guide_app_next);
        bottomLay=(RelativeLayout)findViewById(R.id.pop_bottom);
        btn_bottom = (Button)findViewById(R.id.guide_bottom_next);
        btn_app.setOnClickListener(this);
        btn_bottom.setOnClickListener(this);
        this.setCancelable(false);
    }
    public void refresh(boolean isOwner){
        this.isOwner = isOwner;
        if(!isOwner){
            appLay.setVisibility(View.VISIBLE);
            bottomLay.setVisibility(View.GONE);
        }else{
            bottomLay.setVisibility(View.VISIBLE);
            appLay.setVisibility(View.GONE);
            hasShowBind = SPUtils.get().getBoolean(Constants.VERSION_CODE+Constants.device_add_guide, false);
            if(hasShowBind){
                btn_bottom.setText(R.string.guide_button1);;
            }
            btn_bottom.setText(R.string.guide_button2);
        }
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.guide_app_next:
                DeviceGuidePopView.this.dismiss();
                break;
            case R.id.guide_bottom_next:
                if(!hasShowBind){
                    appLay.setVisibility(View.VISIBLE);
                    bottomLay.setVisibility(View.GONE);
                }else{
                    DeviceGuidePopView.this.dismiss();
                }

                break;
            default:
                break;
        }

    }
    public interface OnConfirmListener {
        public void onConfirm();
    }
    public OnConfirmListener getConfirmListener() {
        return confirmListener;
    }

    public void setConfirmListener(OnConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }


}
