package com.ubtechinc.alpha2ctrlapp.presenter;

import android.content.Intent;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha.CmrAppConfigData;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotAppDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotAppRepository;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseContactFragememt;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;
import com.ubtechinc.nets.phonerobotcommunite.ICallback;

public class SpotifyLoginPresenterImpl implements ISpotifyLoginPresenter {
	private static final String TAG = SpotifyLoginPresenterImpl.class
			.getSimpleName();
	private BaseContactFragememt mFragment;

	public void setmFragment(BaseContactFragememt mFragment) {
		this.mFragment = mFragment;
	}

	private static class SpotifyLoginPresenterImplInstance {
		private static final SpotifyLoginPresenterImpl INSTANCE = new SpotifyLoginPresenterImpl();
	}

	public static SpotifyLoginPresenterImpl getInstance() {
		return SpotifyLoginPresenterImplInstance.INSTANCE;
	}

	private SpotifyLoginPresenterImpl() {
	}

	@Override
	public void onStart() {
//		Alpha2Application.getInstance().registerHandler(mRecDataHandler);
	}

	@Override
	public void checkLoginState(String pckName) {
		Log.i("ken", "checkLoginState()===>>>");
		if(pckName.equals(ISpotifyLoginPresenter.COM_UBTECHINC_ALPHAENGLISHCHAT)&&!SpotifyLoginUtil.isExist(ISpotifyLoginPresenter.COM_SPOTIFY_LOGIN_ACTIVITY, mFragment.getActivity()));
		{
			RobotAppRepository.getInstance().getThirdAppConfig(CMD_GET, COM_UBTECHINC_ALPHAENGLISHCHAT, "en".getBytes(), new ICallback<CmrAppConfigData.CmrAppConfigDataResponse>() {
				@Override
				public void onSuccess(CmrAppConfigData.CmrAppConfigDataResponse data) {
					LoadingDialog.dissMiss();

					String json = new String(data.getDatas().toByteArray());
					Log.i(TAG, "ALPHA_MSG_RSP_SAVE_APPCONFIG+json1==" + json);
					int isLogin = SpotifyLoginUtil.getLoginresult(json);
					if (isLogin != HAVE_LOGIN) {
						doLogin();
						ToastUtils.showShortToast( R.string.tip_spotify_not_login);//提示用户去登陆
					}else {
						ToastUtils.showShortToast( R.string.tip_spotify_have_login);//提示用户已经登录
					}
				}

				@Override
				public void onError(ThrowableWrapper e) {

				}
			});

		}
	}

	@Override
	public void doLogin() {
		final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
		.setScopes(new String[] { "user-read-private", "streaming" })
		.build();
		Log.i("ken", "request+uri==>" + request.toUri().toString());
		Intent intent = AuthenticationClient.createLoginActivityIntent(this.mFragment.getActivity(), request);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		this.mFragment.startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	public void sendLoginInfo(String token, int expireIn) {
		byte[] datas = SpotifyLoginUtil.packetTokenStrToBytes(token, expireIn);
		RobotAppRepository.getInstance().saveThirdAppConfig(CMD_SAVE,COM_UBTECHINC_ALPHAENGLISHCHAT,datas, new IRobotAppDataSource.ControlAppCallback(){

			@Override
			public void onSuccess() {
				LoadingDialog.dissMiss();

			}

			@Override
			public void onFail(ThrowableWrapper e) {

			}
		});

	}

	@Override
	public void onEnd() {
//		Alpha2Application.getInstance().unregisterHandler(mRecDataHandler);

	}


	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE) {
			AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
			Log.i("ken", "response.getType()==" + response.getType());
			switch (response.getType()) {
			case TOKEN:
				String token = response.getAccessToken();
				int expireIn = response.getExpiresIn();
				Log.d(TAG, "!!! Got token: " + token + ">>>" + expireIn + ">>>"+ response.getState());
				sendLoginInfo(token, expireIn);
				break;

			case ERROR:
				Log.d(TAG, "!!! Auth error: " + response.getError());
				break;

			default:
				Log.d(TAG, "!!! Auth result: " + response.getType());
			}
		}
	}

}
