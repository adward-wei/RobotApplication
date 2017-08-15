package com.ubtechinc.alpha2ctrlapp.widget.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.widget.SystemBarTintManager;

public class NormalLoadingDialog extends Dialog {
    private Activity mContext;
    private ImageView spaceshipImage;
    private TextView tvTipContent;
    Animation hyperspaceJumpAnimation;

    public NormalLoadingDialog(Activity context) {
        super(context, R.style.connctdialog);
        this.mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.bg_color_b3);//通知栏所需颜色
        }

        this.setContentView(R.layout.dialog_normal_loading_layout);

        // main.xml中的ImageView
        spaceshipImage = (ImageView) findViewById(R.id.img_loading_logo);
        tvTipContent = (TextView) findViewById(R.id.tipTextView);
        this.setCanceledOnTouchOutside(false);
    }

    public void setImageView(int id) {
        spaceshipImage.setImageResource(id);

    }

    public void setTipContent(String str) {
        tvTipContent.setText(str);

    }

    public void stopImageAnim(){
        spaceshipImage.clearAnimation();
    }
    public void startImageAnim() {
        // 加载动画
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                mContext, R.anim.loading_animation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
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


}
