package com.ubtechinc.alpha2ctrlapp.ui.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ant.country.CountryActivity;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.ubtech.utilcode.utils.ActivityUtils;
import com.ubtech.utilcode.utils.SizeUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.base.ActivitySupport;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.data.Injection;
import com.ubtechinc.alpha2ctrlapp.entity.request.GetWeixinLoginRequest;
import com.ubtechinc.alpha2ctrlapp.third.WeiXinManager;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainActivity;
import com.ubtechinc.alpha2ctrlapp.ui.fragment.base.BaseFragment;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.LoadImage;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.EditPortDialog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.ForceOfflineDialog;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;

/*************************
 * @date 2016/6/30
 * @author
 * @Description 登录页面，依赖MainActivity
 * @modify
 * @modify_time
 **************************/
public class LoginFragement extends BaseFragment implements OnClickListener, LoginContract.View, IUiListener {
    private static final String TAG = "LoginFragement";
    private ImageButton btn_qq_login, btn_weixin_login, btn_weibo_login, btn_facebook_login, btn_twitter_login;
    private TextView btn_Login, btn_email, btn_phone, btn_regist;
    private EditText nameEdt, pswEdt;
    private TextView findPswTv;

    private LinearLayout edt_countryCode;

    private TextView countryNameTv, countryNumTv, tipsTV;

    private ImageView img_head;
    private ImageView showPsw;
    private ImageView btn_delete;

    private LinearLayout edt_phone_num_Lay;
    private RelativeLayout psw_lay;
    private EditPortDialog editPortDialog;
    private TextView btnTest; //环境切换入口
    LoginContract.Presenter mPresenter;

    public LoginFragement() {
        // Requires empty public constructor
    }

    public static LoginFragement newInstance() {
        return new LoginFragement();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(false, getString(R.string.ui_login));
        ((BaseActivity) getActivity()).setTopGone();
        activity.btn_ignore.setVisibility(View.GONE);
        initView();
        initControlListener();
        initData();
    }


    @Override
    public View onCreateFragmentView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.activity_login, container, false);
    }

    @Override
    public void initView() {


        btn_Login = (TextView) mContentView.findViewById(R.id.btn_login);
        btn_qq_login = (ImageButton) mContentView
                .findViewById(R.id.btn_qq_login);

        pswEdt = (EditText) mContentView.findViewById(R.id.input_psw);
        findPswTv = (TextView) mContentView.findViewById(R.id.find_psw_tv);
        edt_countryCode = (LinearLayout) mContentView
                .findViewById(R.id.edt_phone_1);
        btn_weixin_login = (ImageButton) mContentView.findViewById(R.id.btn_wx_login);


        btn_weibo_login = (ImageButton) mContentView.findViewById(R.id.btn_weibo_login);
        btn_facebook_login = (ImageButton) mContentView.findViewById(R.id.btn_facebook_login);
        btn_twitter_login = (ImageButton) mContentView.findViewById(R.id.btn_twitter_login);
        btn_delete = (ImageView) mContentView.findViewById(R.id.btn_delete);
        btn_phone = (TextView) mContentView.findViewById(
                R.id.btn_login_by_phone);
        btn_email = (TextView) mContentView.findViewById(
                R.id.btn_login_by_email);
        countryNameTv = (TextView) mContentView.findViewById(R.id.countryNameTv);
        countryNumTv = (TextView) mContentView.findViewById(R.id.countryNumTv);


        img_head = (ImageView) mContentView.findViewById(R.id.img_head);


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Constants.deviceHeight / 6, Constants.deviceHeight / 6);
        lp.setMargins(0, SizeUtils.dp2px(15), 0, 0);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        img_head.setLayoutParams(lp);


        nameEdt = (EditText) mContentView.findViewById(R.id.input_name);
        btn_weibo_login.setOnClickListener(this);
        nameEdt.setHorizontallyScrolling(true);
        tipsTV = (TextView) mContentView.findViewById(R.id.tips);
        edt_phone_num_Lay = (LinearLayout) mContentView.findViewById(R.id.edt_phone_num_Lay);


        if (!TextUtils.isEmpty(nameEdt.getText().toString())) {
            btn_delete.setVisibility(View.VISIBLE);
        }
        showPsw = (ImageView) mContentView.findViewById(R.id.btn_show_psw);
        showPsw.setOnClickListener(this);

        btn_regist = (TextView) getActivity().findViewById(R.id.btn_register);
        btn_regist.setOnClickListener(this);
        nameEdt.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                edt_phone_num_Lay.setBackgroundResource(R.drawable.input_nomal);
            }
        });
        btnTest = (TextView) mContentView.findViewById(R.id.btn_Test);
        btnTest.setOnClickListener(this);
        nameEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                edt_phone_num_Lay.setBackgroundResource(R.drawable.input_nomal);
                if (!TextUtils.isEmpty(nameEdt.getText().toString())) {
                    btn_delete.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(pswEdt.getText().toString())) {
                        btn_Login.setBackgroundResource(R.drawable.btn_button_able);
                        tipsTV.setText("");
                    } else {
                        btn_Login.setBackgroundResource(R.drawable.button_disable);
                    }
                } else {
                    btn_delete.setVisibility(View.GONE);
                    btn_Login.setBackgroundResource(R.drawable.button_disable);
                }


            }
        });
        pswEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                psw_lay.setBackgroundResource(R.drawable.input_nomal);
                if (!TextUtils.isEmpty(pswEdt.getText().toString()) && !TextUtils.isEmpty(nameEdt.getText().toString())) {
                    btn_Login.setBackgroundResource(R.drawable.btn_button_able);
                    tipsTV.setText("");
                } else {
                    btn_Login.setBackgroundResource(R.drawable.button_disable);
                }

            }
        });
        pswEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub

                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    slidingKeyBroad();
                    doLogin(nameEdt.getText().toString(), pswEdt.getText().toString());
                }
                return false;

            }
        });
        psw_lay = (RelativeLayout) mContentView.findViewById(R.id.psw_lay);
        Bundle arguments = getArguments();
        if (arguments != null) {
            boolean isForceOffline = arguments.getBoolean(MainActivity.FORCE_OFFLINE, false);
            if (isForceOffline) {
                ForceOfflineDialog forceOfflineDialog = new ForceOfflineDialog(getActivity());
                forceOfflineDialog.show();
            }
        }

    }

    private void initData() {
        mPresenter = new LoginPresenter(mActivity, this, Injection.provideMessageRepository(mApplication), Injection.provideLoginRepository(mApplication));
        mPresenter.initData();
    }

    public void getWeixinLoginInfo(SendAuth.Resp rsp) {
        Logger.i("微信登录: 获取登录信息");
        GetWeixinLoginRequest request = new GetWeixinLoginRequest();
        request.setAppid(WeiXinManager.WEIXIN_APP_ID);
        request.setSecret(WeiXinManager.WEIXIN_APP_SECRET);
        request.setCode(rsp.code);
        request.setGrant_type(WeiXinManager.GRANTTYPE);
//		UserAction action = UserAction.getInstance(mActivity, this);
//		action.setThirdPrama(request);
//		LoadingDialog.getInstance(mActivity).show();
//		action.doRequest(NetWorkConstant.REQUEST_GET_WEIXIN_LOGIN_INFO);

    }

    @Override
    public void refreshUI(String loginKey, String countryNum, String countryName, String imgUrl) {

        //加载头像
        LoadImage.LoadHeader(this.getActivity(), 0, img_head, imgUrl);
        nameEdt.setText(loginKey);
        refreshCountry(countryNum, countryName);


    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoadingDialog() {
        LoadingDialog.getInstance(mActivity).show();
    }

    @Override
    public void dismissLoadingDialog() {
        LoadingDialog.getInstance(mActivity).dismiss();
    }

    @Override
    public void initControlListener() {
        btn_Login.setOnClickListener(this);
        findPswTv.setOnClickListener(this);
        edt_countryCode.setOnClickListener(this);
        btn_qq_login.setOnClickListener(this);
        btn_weixin_login.setOnClickListener(this);
        btn_phone.setOnClickListener(this);
        btn_email.setOnClickListener(this);
        btn_facebook_login.setOnClickListener(this);
        btn_twitter_login.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void setCountry(String countryNum, String countryName) {
        countryNameTv.setText(countryName);
        countryNumTv.setText("+" + countryNum);
    }

    @Override
    public void skipActivity(Intent intent) {
        LoadingDialog.dissMiss();
        startActivity(intent);
    }


    @Override
    public void refreshRegisterByPhone() {
        mPresenter.changeLoginModel(true);
        nameEdt.setHint(R.string.ui_phone_input);
        btn_phone.setTextColor(getResources().getColor(R.color.text_color_t5));
        btn_email.setTextColor(getResources().getColor(R.color.text_color_t4));
        edt_countryCode.setVisibility(View.VISIBLE);
        edt_phone_num_Lay.setBackgroundResource(R.drawable.input_nomal);
    }

    @Override
    public void refreshRegisterByEmail() {
        mPresenter.changeLoginModel(false);
        nameEdt.setText("");
        nameEdt.setHint(R.string.ui_email_input);
        edt_countryCode.setVisibility(View.GONE);
        btn_phone.setTextColor(getResources().getColor(R.color.text_color_t4));
        btn_email.setTextColor(getResources().getColor(R.color.text_color_t5));
        edt_phone_num_Lay.setBackgroundResource(R.drawable.input_nomal);
    }

    @Override
    public void refreshCountry(String countryNum, String countryName) {
        countryNameTv.setText(countryName);
        countryNumTv.setText(countryNum);
    }

    @Override
    public void showLoginUserNamePrompt(int promptId) {
        tipsTV.setText(promptId);
        edt_phone_num_Lay.setBackgroundResource(R.drawable.input_error_shape);
        ToastUtils.showShortToast(promptId);
    }

    @Override
    public void showLoginPwdPrompt(int promptId) {
        tipsTV.setText(promptId);
        ToastUtils.showShortToast(promptId);
        psw_lay.setBackgroundResource(R.drawable.input_error_shape);
    }

    @Override
    public void replaceFragment(String fragmentName, Bundle bundle) {
        ActivityUtils.replaceFragment(getActivity(), getFragmentManager(), fragmentName, R.id.layout_fragment_contanier, bundle);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_by_phone:

                refreshRegisterByPhone();
                break;
            case R.id.btn_login_by_email:
                refreshRegisterByEmail();
                break;
            case R.id.btn_login: {
                doLogin(nameEdt.getText().toString(), pswEdt.getText().toString());
            }

            break;
            case R.id.find_psw_tv:
                replaceFragment(FindPwdFragment.class.getName(), bundle);
                break;
            case R.id.edt_phone_1:
                Intent intent = new Intent();
                intent.setClass(mActivity, CountryActivity.class);
                activity.startActivityForResult(intent,
                        Constants.GET_COUNTRY_REQUEST);
                break;
            case R.id.btn_qq_login:
                Logger.i("第三方登录btn_qq_login");
                mPresenter.doQQLogin(getActivity());
                break;
            case R.id.btn_wx_login:
                mPresenter.doWeiXinLogin();
                break;
            case R.id.btn_show_psw:
                if (pswEdt.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    pswEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPsw.setImageResource(R.drawable.icon_eye_nomal);
                } else {
                    pswEdt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPsw.setImageResource(R.drawable.icon_eye_press);
                }
                break;
            case R.id.btn_facebook_login:
                mPresenter.doFackbookLogin(getActivity());
                break;
            case R.id.btn_twitter_login:
                mPresenter.doTwitterLogin();
                break;
            case R.id.btn_delete:
                nameEdt.setText("");
                btn_delete.setVisibility(View.GONE);
                break;
            case R.id.btn_register:
                replaceFragment(RegistFragement.class.getName(), bundle);
                break;
            case R.id.btn_Test:
                if (editPortDialog == null)
                    editPortDialog = new EditPortDialog((ActivitySupport) mActivity);
                editPortDialog.show();
                editPortDialog.refresh();

                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GET_COUNTRY_REQUEST) {
            mPresenter.handleCountryRequest(data.getExtras());
        }

    }


    /**
     * 发起登录
     *
     * @param name 用户名
     * @param psw  密码
     */
    private void doLogin(String name, String psw) {
        if (mPresenter.checkLoginParam(name, psw)) {
            showLoadingDialog();
            mPresenter.doLoginRequest(name, psw);

        }
    }


    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}
