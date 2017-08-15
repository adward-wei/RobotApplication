package com.ubtechinc.alpha2ctrlapp.ui.fragment.app;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;

public class HelpActivity extends BaseContactActivity {

    private String TAG = "HelpActivity";
    ;

    private WebView webView;
    String url = "";
    String titleText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_menu_help);
        this.mContext = this;
        titleText = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        initView();
    }

    public void initView() {
        title = (TextView) findViewById(R.id.authorize_title);
        title.setText(titleText);
        webView = (WebView) findViewById(R.id.web_view);

        webView.loadUrl(url);
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }


}
