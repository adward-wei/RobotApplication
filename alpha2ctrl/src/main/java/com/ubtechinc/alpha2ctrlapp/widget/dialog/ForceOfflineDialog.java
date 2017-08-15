package com.ubtechinc.alpha2ctrlapp.widget.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.widget.SystemBarTintManager;

public class ForceOfflineDialog extends Dialog {
    private Button btn_confirm;
    private OnPositiveClickLitstener positiveClick;

    public ForceOfflineDialog(Activity context) {
        super(context, R.style.deleteDialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
        }

        this.setContentView(R.layout.force_offline_dialog);
        btn_confirm = (Button) findViewById(R.id.btn_comfirm);
        initClick(context);
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

    public void initClick(Activity context) {

        btn_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (positiveClick != null)
                    positiveClick.OnPositiveClick();

                ForceOfflineDialog.this.dismiss();
            }
        });
    }


    public OnPositiveClickLitstener getPositiveClick() {
        return positiveClick;
    }

    public void setPositiveClick(OnPositiveClickLitstener positiveClick) {
        this.positiveClick = positiveClick;
    }

    public interface OnPositiveClickLitstener {
        public void OnPositiveClick();
    }

}
