package com.ubtechinc.alpha2ctrlapp.widget.popWindow;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ubtech.utilcode.utils.SPUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.entity.business.robot.ImageModel;
import com.ubtechinc.alpha2ctrlapp.widget.SystemBarTintManager;

import java.util.List;

public class GuidePopView extends Dialog implements
        View.OnClickListener {

    private TextView deleteTips;
    private TextView btn_delete;
    private LinearLayout btn_cancel;
    private Activity context;
    private List<ImageModel>list;
    private View view;
    private OnConfirmListener confirmListener;
    private boolean hasConnect =false;
    private RelativeLayout  menuLay,appLay,bottomLay,actionLay;
    private Button btn_menu,btn_app,btn_bottom,btn_action;
    private boolean hasShowMenu;

    public GuidePopView(Activity context, boolean hasConnect) {
        super(context, R.style.deleteDialog);
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
        }
        this.setContentView(R.layout.main_page_guide);
        this.hasConnect = hasConnect;
        menuLay = (RelativeLayout)findViewById(R.id.pop_menu);
        btn_menu = (Button)findViewById(R.id.guide_menu_know);
        appLay =(RelativeLayout)findViewById(R.id.pop_app);
        btn_app= (Button)findViewById(R.id.guide_app_next);
        bottomLay=(RelativeLayout)findViewById(R.id.pop_bottom);
        btn_bottom = (Button)findViewById(R.id.guide_bottom_next);
        btn_menu.setOnClickListener(this);
        btn_app.setOnClickListener(this);
        btn_bottom.setOnClickListener(this);
        actionLay=(RelativeLayout)findViewById(R.id.action_menu);
        btn_action = (Button)findViewById(R.id.guide_action_know);
        btn_action.setOnClickListener(this);
        setCancelable(false);
    }
    public void refresh(boolean hasConnect){
        this.hasConnect = hasConnect;
        if(hasConnect){
            appLay.setVisibility(View.VISIBLE);
            menuLay.setVisibility(View.GONE);
            hasShowMenu = SPUtils.get().getBoolean(Constants.VERSION_CODE+Constants.main_menu_guide, false);
            if(hasShowMenu){
                btn_action.setText(R.string.guide_button1);;
            }
        }else{
            menuLay.setVisibility(View.VISIBLE);
            appLay.setVisibility(View.GONE);
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
    public void refresh(List<ImageModel>list,View view) {
        this.list = list;
        deleteTips.setText(context.getString(R.string.image_delete_phototips).replace("%", list.size()+""));
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.guide_action_know:
                if(!hasShowMenu){
                    actionLay.setVisibility(View.GONE);
                    menuLay.setVisibility(View.VISIBLE);
                }else{
                    GuidePopView.this.dismiss();
                }
                break;
            case R.id.guide_menu_know:
                GuidePopView.this.dismiss();
                break;
            case R.id.guide_app_next:
                appLay.setVisibility(View.GONE);
                actionLay.setVisibility(View.VISIBLE);
                break;
            case R.id.guide_bottom_next:
                bottomLay.setVisibility(View.GONE);
                appLay.setVisibility(View.VISIBLE);

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
