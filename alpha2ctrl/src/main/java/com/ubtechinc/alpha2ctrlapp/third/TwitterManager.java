package com.ubtechinc.alpha2ctrlapp.third;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.Toast;

import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.user.CompleteUserInfoFragment;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


/**
 * Class to help publish into twitter.
 * This class need twitter4j-core-[newer_version].jar
 * 
 * @author cristian palos rejano
 *
 */
public class TwitterManager {
	
	private static final String PREFERENCES_NAME = "twitter";
	private static final String PREFERENCES_ACCESS_TOKEN = "access_token";
	private static final String PREFERENCES_ACCESS_TOKEN_SECRET = "access_token_secret";
	
	static final String TWITTER_CALLBACK_URL = "oauth://twitter";
	
	private static final String TWITTER_CONSUMER_KEY = "WK1D46iGEZuZOEa6C6f9M5qkx";
	private static final String TWITTER_CONSUMER_KEY_SECRET = "1S1cHPs8Pcv7MFUKnbP7C70w6Bj095Wp04H2MHaJE6kN0ofRPX";
	

	public static boolean isNeedOnResult = false;
	private static Context mContext;

	private static AccessToken mAccessToken;
	private static RequestToken mRequestToken;
    public static ITwitterLoginListener _ltwitterListener;
	private static SharedPreferences mSharedPreferences;
	public final static int requestCode_tw = 10101;
	
	public static void InitTwitterManager(Context context) {
		mContext = context;
		
		mSharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		
		loadAccessToken();
	}
	
	
	public static  void doLogin(final Activity mContext, final ITwitterLoginListener listener) {
		InitTwitterManager(mContext);
		_ltwitterListener = listener;
		new AsyncTask<Void, Void, Boolean>() {
			ProgressDialog pDialog;
			
			protected void onPreExecute() {
				pDialog = new ProgressDialog(mContext);
				pDialog.setMessage(mContext.getString(R.string.error_message_loading));
				pDialog.setCancelable(false);
				pDialog.show();
			}
			
			@Override
			protected Boolean doInBackground(Void... params) {
				boolean result = false;
				
				try {
					twitter4j.Twitter twitter = new TwitterFactory(getConfiguration()).getInstance();
					mRequestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
					result = true;
				} catch (Exception e) {
					e.printStackTrace();
					result = false;
				}
				
				return result;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				pDialog.dismiss();
				
				if(result) {
					Intent intent = new Intent(mContext, TwitterWebView.class);
					intent.putExtra("twitter_url", Uri.parse(mRequestToken.getAuthorizationURL().toString()));
					mContext.startActivityForResult(intent, requestCode_tw);
				} else {
				}

			}
			
		}.execute();
	
	}
	public static String urlEncode(String s) {
	    try {
	        return URLEncoder.encode(s, "UTF-8");
	    }
	    catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("URLEncoder.encode() failed for " + s);
	    }
	}
	
	/**
	 * 采用android Intent的方式分享
	 * */
	public static void doShareTwitter(Activity activity, String title, String des, int id , int type){
		String url ="";
		if(type==1)
			url = Constants.SHARE_ACTION_URL+id;
		else if(type ==2)
			url= Constants.SHARE_APP_URL+id;
		else if(type ==3)
			url= des;
		String tweetUrl = String.format("https://twitter.com/intent/tweet?url=%s",urlEncode(url));
		Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

		boolean twitterAppFound = false;
		List<ResolveInfo> matches1 = activity.getApplicationContext().getPackageManager().queryIntentActivities(twitterIntent, 0);
		for (ResolveInfo info : matches1) {
			if (info.activityInfo.packageName.toLowerCase().startsWith(
					"com.twitter")) {
				twitterIntent.setPackage(info.activityInfo.packageName);
				twitterAppFound = true;
				break;
			}
		}

		if (twitterAppFound) {
			activity.startActivity(twitterIntent);
		}
		else
		{
			ToastUtils.showShortToast(activity.getResources().getString(R.string.ui_robot_share_twitter));
		}
		
	}
	/**
	 * Publish a text
	 * @param text
	 */
	public static void postTweet(String text) {
		if(text.length() > 139) {
			text = text.substring(0, 139);
		}
		
		StatusUpdate status = new StatusUpdate(text);
		new AsyncPublishPost().execute(status);
	}
	
	/**
	 * Publish a text with image
	 * @param text
	 * @param file
	 */
	public void postTweet(String text, File file) {
		if(text.length() >= 140) {
			text = text.substring(0, 117);
		}
		
		StatusUpdate status = new StatusUpdate(text);
		if(file != null && file.exists()) {
			status.setMedia(file);
		}
		
		new AsyncPublishPost().execute(status);
	}
	
	/**
	 * Check if already user sign in
	 * @return
	 */
	public static boolean isUserLogin() {
		boolean isUserLogin = false;
		loadAccessToken();
		if(mAccessToken != null) {
			String token = mAccessToken.getToken().trim();
			String tokenSecret = mAccessToken.getTokenSecret().trim();
			isUserLogin = (token.length() != 0 && tokenSecret.length() != 0);
		}
		
		return isUserLogin;
	}
	
	/**
	 * Sign in into twitter
	 */
	public static void signin() {
		if(isUserLogin()) {
			Toast.makeText(mContext, "Already sign in", Toast.LENGTH_LONG).show();
			return;
		}
		
	
	}
	public  static void onActivityResult(int requestCode, int resultCode, Intent data) {
   
        if (resultCode == Activity.RESULT_OK) {
            getAccessToken(data.getStringExtra(TWITTER_CALLBACK_URL));
        }
    
}


private static void getAccessToken(String callbackUrl) {
    Uri uri = Uri.parse(callbackUrl);
    String verifier = uri.getQueryParameter("oauth_verifier");

    GetAccessTokenTask getAccessTokenTask = new GetAccessTokenTask();
    getAccessTokenTask.execute(verifier);
}


private static class GetAccessTokenTask extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {
        String verifier = strings[0];
        boolean result = false;
        try {
        	Looper.prepare();
        	twitter4j.Twitter twitter = new TwitterFactory(getConfiguration()).getInstance();
        	mAccessToken = twitter.getOAuthAccessToken(mRequestToken, verifier);
			_ltwitterListener.OnTwitterLoginComplete(mAccessToken);
            System.out.println("twitter:"+mAccessToken.toString());
            result = (mAccessToken != null);
        } catch (Exception e) {
                  e.printStackTrace();
                  result = false;
        }
        return result;
    }
    	protected void onPostExecute(Boolean result) {
			if(result) {
//				Toast.makeText(mActivity, "Wellcome @" + mAccessToken.getScreenName(), Toast.LENGTH_LONG).show();
				saveAccessToken();
			} else {
//				Toast.makeText(mActivity, "Can't sign in", Toast.LENGTH_LONG).show();
			}
    }
}
	public static void doGetUserProfile(final CompleteUserInfoFragment listener)
	{
		
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						twitter4j.Twitter twitter = new TwitterFactory(getConfigurationBySecret()).getInstance();
						System.out.println(twitter.showUser(mAccessToken.getUserId()).getName()+ " "+ twitter.showUser(mAccessToken.getUserId()).getProfileImageURL());
						User user = twitter.showUser(mAccessToken.getUserId());
						listener.onTwitterProfileInfo(user);
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				}
			}).start();
		

	}
	
	private static Configuration getConfiguration() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
		  .setOAuthConsumerSecret(TWITTER_CONSUMER_KEY_SECRET)
		  .setOAuthAccessToken(null)
		  .setOAuthAccessTokenSecret(null);
		return cb.build() ;
	}
	private static Configuration getConfigurationBySecret() {
		String token = (mAccessToken.getToken().length() == 0) ? null : mAccessToken.getToken();
		String tokenSecret = (mAccessToken.getTokenSecret().length() == 0) ? null : mAccessToken.getTokenSecret();
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
		.setOAuthConsumerSecret(TWITTER_CONSUMER_KEY_SECRET)
		.setOAuthAccessToken(token)
		.setOAuthAccessTokenSecret(tokenSecret);
		return cb.build() ;
	}
	
	private static  void loadAccessToken() {
		String keyAccesToken = mSharedPreferences.getString(PREFERENCES_ACCESS_TOKEN, "");
		String keyAccesTokenSecret = mSharedPreferences.getString(PREFERENCES_ACCESS_TOKEN_SECRET, "");
		mAccessToken = new AccessToken(keyAccesToken, keyAccesTokenSecret);
	}

	private static void saveAccessToken() {
		if(mAccessToken != null) {
			SharedPreferences.Editor sharedPreferencesEditor = mSharedPreferences.edit();
			sharedPreferencesEditor.putString(PREFERENCES_ACCESS_TOKEN, mAccessToken.getToken());
			sharedPreferencesEditor.putString(PREFERENCES_ACCESS_TOKEN_SECRET, mAccessToken.getTokenSecret());
			sharedPreferencesEditor.commit();
		}
	}
	
	static class AsyncPublishPost extends AsyncTask<StatusUpdate, Void, Boolean> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			if(!isUserLogin()) {
				signin();
				cancel(true);
			}
			
		}
		
		@Override
		protected Boolean doInBackground(StatusUpdate... params) {
			StatusUpdate statusUpdate = params[0];
			boolean result = false;
			
			try {
				twitter4j.Twitter twitter = new TwitterFactory(getConfigurationBySecret()).getInstance();
			    twitter4j.Status updatedStatus = twitter.updateStatus(statusUpdate);
			    result = (updatedStatus != null);
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			if(result) {
				Toast.makeText(mContext, "Tweet published sucesfully", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(mContext, "Error while publish tweet", Toast.LENGTH_LONG).show();
			}
		}
		
	}


}
