package com.ubtechinc.alpha2ctrlapp.presenter;

import android.content.Intent;

import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;


/**
 * just doing something about spotify login
 * @author Administrator
 *
 */
public interface ISpotifyLoginPresenter {
	public static final int ALPHA_MSG_GET_APPCONFIG = 41;
	public static final int ALPHA_MSG_SAVE_APPCONFIG = 42;
	public static final String COM_UBTECHINC_ALPHAENGLISHCHAT= BusinessConstants.PACKAGENAME_EN_CHAT;
	public static final String COM_SPOTIFY_LOGIN_ACTIVITY="com.spotify.sdk.android.authentication.LoginActivity";
	public static final int CMD_GET=1;
	public static final int CMD_SAVE=2;
	public static final int HAVE_LOGIN=1;
	
	
	public static final int REQUEST_CODE = 1337;
	/**spotify login info*/
	@SuppressWarnings("SpellCheckingInspection")
	public static final String CLIENT_ID = "d46e20165d8847238cd10833e816fc47";
	@SuppressWarnings("SpellCheckingInspection")
	public static final String REDIRECT_URI = "http://120.76.77.90:8080/examples/callback.jsp";
	
	public void onStart();
	public void onEnd();
    public void checkLoginState(String pckName);/**check login state*/
    public void doLogin();/**spotify login*/
    public void sendLoginInfo(String token, int expireIn);/**send the token info after loginning*/
    public void onActivityResult(int requestCode, int resultCode, Intent intent);
    public void setmFragment(BaseContactFragememt mFragment);
}
