package com.ubtechinc.alpha2ctrlapp.third;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.user.CompleteUserInfoFragment;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import twitter4j.auth.AccessToken;

public class FaceBookManager {
	private static CallbackManager callbackManager;
	private static AccessToken accessToken;
	public static boolean isNeedOnResualt = false;
	public static String url =null;
	
	public static void initMyFaceBook(Context context)
	{
		FacebookSdk.sdkInitialize(context);
		if(callbackManager==null)
		 callbackManager = CallbackManager.Factory.create();
	}
	public static void doLogin(Activity activity, final IFaceBookLoginListener listener) {
	
		
//		initMyFaceBook(activity.getApplicationContext());
//		LoginManager.getInstance().registerCallback(callbackManager,
//				new FacebookCallback<LoginResult>() {
//					@Override
//					public void onSuccess(LoginResult loginResult) {
//						 accessToken = loginResult.getAccessToken();
//						GraphRequest request = GraphRequest.newMeRequest(
//								accessToken,
//								new GraphRequest.GraphJSONObjectCallback() {
//									// 當RESPONSE回來的時候
//									@Override
//									public void onCompleted(JSONObject object,
//											GraphResponse response) {
//										// 讀出姓名 ID FB個人頁面連結
//										Log.d("FB", "complete");
//										System.out.println(object.toString());
//										try {
//											url = response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");
//										} catch (JSONException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										}
//										listener.onLoginComplete(object);
//									}
//								});
//						Bundle parameters = new Bundle();
//						parameters.putString("fields", "id,name,link,gender,picture.type(large)");
//						request.setParameters(parameters);
//						request.executeAsync();
//					}
//
//					@Override
//					public void onCancel() {
//						System.out.println("Login onCancel");
//					}
//
//					@Override
//					public void onError(FacebookException e) {
//						System.out.println("Login onError:"+e.getMessage());
//					}
//				});
//		LoginManager.getInstance().logInWithReadPermissions(activity,
//				Arrays.asList("public_profile"));
	}
	public static void onGetUserProfile(CompleteUserInfoFragment listener)
	{
//		 Profile profile = Profile.getCurrentProfile();
//	     listener.onFaceBookProfileInfo(profile,url);
		
	}
	public static void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
	
//	public static boolean canShare() {
//        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        if (accessToken == null) {
//            return false;
//        }
//        final Set<String> permissions = accessToken.getPermissions();
//        if (permissions == null) {
//            return false;
//        }
//        return (permissions.contains("publish_actions"));
//
//
//    }
	
	public static String urlEncode(String s) {
	    try {
	        return URLEncoder.encode(s, "UTF-8");
	    }
	    catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("URLEncoder.encode() failed for " + s);
	    }
	}
/***
 * facebook分享
 * @param activity 上下文环境
 * @param tittle 标题
 * @param des 分享内容描述
 * @param id 分享的id
 * @param type 
 */
	public static void doShareFaceBook(Activity activity, String tittle, String des , int id, int type)
	{
		
		/**
		 * Intent的方式分享链接
		 * */
//		String facebookUrl = 
//			    String.format("https://www.facebook.com/sharer/sharer.php?u=",urlEncode(url+action.actionId));
//		Intent facebookIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(facebookUrl));
//		//"https://www.facebook.com/sharer/sharer.php?u=" 
//		boolean facebookAppFound = false;
//		List<ResolveInfo> matches = activity.getApplicationContext().getPackageManager()
//				.queryIntentActivities(facebookIntent, 0);
//
//		for (ResolveInfo info : matches) {
//			if (info.activityInfo.packageName.toLowerCase().startsWith(
//					"com.facebook")) {
//				facebookIntent.setPackage(info.activityInfo.packageName);
//				facebookAppFound = true;
//				break;
//			}
//		}
//		if (facebookAppFound) {
//			activity.startActivity(facebookIntent);
//		}
//		else
//		{
//			Toast.makeText(mMainPageActivity,mMainPageActivity.getString(R.string.ui_robot_share_facebook), 4).show();
//		}

//		System.out.println("action:"+url+" "+action.actionTitle+" "+action.actionDesciber);
		String url =""; // 分享的URL
		if(type==1)
			url = Constants.SHARE_ACTION_URL+id;
		else if(type ==2)
			url= Constants.SHARE_APP_URL+id;
		else{
			url= des;
		}
		initMyFaceBook(activity.getApplicationContext());
		ShareDialog shareDialog = new ShareDialog(activity);
		Uri uri = Uri.parse(url);
		ShareLinkContent content = new ShareLinkContent.Builder().setContentTitle(tittle)
	            .setContentDescription(
	            		des)
        .setContentUrl(uri)
        .build();
	   shareDialog.registerCallback(callbackManager,  new FacebookCallback<Sharer.Result>() {
	        @Override
	        public void onCancel() {
	            System.out.println("HelloFacebook onCancel!");
	        }
	        @Override
	        public void onError(FacebookException error) {
	            System.out.println("HelloFacebook onerror："+ String.format("Error: %s", error.toString()));
	        }
	        @Override
	        public void onSuccess(Sharer.Result result) {
	            System.out.println("HelloFacebook Success!");
	            if (result.getPostId() != null) {
	            }
	        }
	    });
//	     shareDialog.show(content, Mode.AUTOMATIC);

     	}


	public interface IFaceBookLoginListener {

		public void onLoginComplete(JSONObject object);

	}
	}
