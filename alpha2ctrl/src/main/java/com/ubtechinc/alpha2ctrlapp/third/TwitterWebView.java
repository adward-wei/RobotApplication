package com.ubtechinc.alpha2ctrlapp.third;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;
import com.ubtechinc.alpha2ctrlapp.R;

public class TwitterWebView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_webview_login  );
		WebView twitterWebView = (WebView) findViewById(R.id.webview_twitter);
		twitterWebView.getSettings().setJavaScriptEnabled(true);
		twitterWebView.setWebViewClient(new LoginToTwitterWebViewClient());
		twitterWebView.loadUrl(getIntent().getExtras().get("twitter_url").toString());
	}
	
	 private class LoginToTwitterWebViewClient extends WebViewClient {

	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            if (url.startsWith(TwitterManager.TWITTER_CALLBACK_URL)) {
	                Intent intent = new Intent();
	                intent.putExtra(TwitterManager.TWITTER_CALLBACK_URL, url);
	                setResult(Activity.RESULT_OK, intent);
	                finish();
	            }
	            return false;
	        }
	    }

	@Override
	protected void onStop() {
		super.onStop();
		Logger.d("TwitterWebView onStop", "onStop");
		this.finish();
	}
}
