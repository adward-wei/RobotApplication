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

public class ConnectRobotDialog extends Dialog {
    private Activity mContext;
    private Button btn_confirm;

    private OnPositiveClick positiveClick;
    private OnNegsitiveClick negsitiveClick;
    public static ConnectRobotDialog mDia;


    public ConnectRobotDialog(Activity context) {
        super(context, R.style.connctdialog);
        this.mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
        }

        this.setContentView(R.layout.dialog_connect_robot);
        btn_confirm = (Button) findViewById(R.id.btn_reconnect);

        initClick(context);
    }

    public static ConnectRobotDialog getInstance(Activity _context) {
        if (mDia != null && mDia.isShowing()) {
            /*在dimiss一个dialog之前，必须确保所在的Activity没有被销毁，否则会出现崩溃异常*/
            if (mDia.mContext != null && mDia.mContext instanceof Activity) {
                if (!((Activity) mDia.mContext).isFinishing()) {
                    mDia.cancel();
                    mDia.mContext = null;
                }
            }
        }

        mDia = new ConnectRobotDialog(_context);
        mDia.mContext = _context;
        return mDia;
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

                ConnectRobotDialog.this.dismiss();
            }
        });

    }


    public OnPositiveClick getPositiveClick() {
        return positiveClick;
    }

    public void setPositiveClick(OnPositiveClick positiveClick) {
        this.positiveClick = positiveClick;
    }

    public OnNegsitiveClick getNegsitiveClick() {
        return negsitiveClick;
    }

    public void setNegsitiveClick(OnNegsitiveClick negsitiveClick) {
        this.negsitiveClick = negsitiveClick;
    }


    public interface OnPositiveClick {
        public void OnPositiveClick();
    }

    public interface OnNegsitiveClick {
        public void OnNegsitiveClick();
    }
}
