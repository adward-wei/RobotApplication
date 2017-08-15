package com.ubtechinc.alpha2ctrlapp.ui.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ant.country.CountryActivity;
import com.orhanobut.logger.Logger;
import com.ubtech.utilcode.utils.SPUtils;
import com.ubtech.utilcode.utils.ToastUtils;
import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.constants.PreferenceConstants;
import com.ubtechinc.alpha2ctrlapp.data.user.IUserConfigDataSource;
import com.ubtechinc.alpha2ctrlapp.data.user.UserConfigReponsitory;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.EditUserInfoRequest;
import com.ubtechinc.alpha2ctrlapp.entity.business.user.UserInfo;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.nets.http.ThrowableWrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * 名称设置类
 */
public class EditUserInfoActivity extends BaseContactActivity implements OnClickListener {
    private static final String TAG = "EditUserInfoActivity";
    private EditText ower_nameEd; // 编辑框
    private ImageView btn_delete; // 删除按钮
    private TextView editButton;
    private boolean isEnglish = true; // 机器人的系统是否是英语
    private TextView errorTipTv;  // 编辑框输入错误提示
    private TextView inputTipsTv; // 输入提示
    private int type;//1用户名，2手机，3邮箱
    private String key;
    private TextView countryCodeTv, countryNameTv;
    private LinearLayout chooseCountryLay;
    private String countryNum;
    private int genderValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_set_name);
        this.mContext = this;
        initView();
    }

    public void initView() {
        title = (TextView) findViewById(R.id.authorize_title);
        type = getIntent().getIntExtra("type", 1);
        genderValue = getIntent().getIntExtra("gender_value", 1);
        ower_nameEd = (EditText) findViewById(R.id.user_name);
        btn_delete = (ImageView) findViewById(R.id.user_name_delete);
        btn_delete.setOnClickListener(this);
        editButton = (TextView) findViewById(R.id.btn_top_right);
        editButton.setVisibility(View.VISIBLE);
        editButton.setOnClickListener(this);
        editButton.setText(R.string.common_btn_confirm);
        errorTipTv = (TextView) findViewById(R.id.input_error_tips);
        inputTipsTv = (TextView) findViewById(R.id.input_user_name_tips);
        countryCodeTv = (TextView) findViewById(R.id.countryNumTv);
        countryNameTv = (TextView) findViewById(R.id.countryNameTv);
        chooseCountryLay = (LinearLayout) findViewById(R.id.country_code_lay);
        chooseCountryLay.setOnClickListener(this);
        /**
         * 监听输入框的输入
         */
        ower_nameEd.addTextChangedListener(new TextWatcher() {
            private String origin;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Logger.t(TAG).d("onTextChanged s = " + s + " start = " + start + " before = " + before + " count = " + count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                origin = s.toString();
                Logger.t(TAG).d("beforeTextChanged s = " + s + " start = " + start + " count = " + count + " after = " + after);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Logger.t(TAG).d("afterTextChanged s = " + s);

                String editable = ower_nameEd.getText().toString();
                String str = "";
                if (type == 1) {
                    str = stringFilter1(editable.toString());
                    if (str.length() > 20){
                        str = str.substring(0,20);
                    }
                } else if (type == 2) {
                    str = stringFilterphone(editable.toString());
                } else if (type == 3) {
                    str = stringFilteremail(editable.toString());
                }
                if (!editable.equals(str)) {
                    ower_nameEd.setText(str);
                    //设置新的光标所在位置
                    ower_nameEd.setSelection(str.length());
                }

                if (TextUtils.isEmpty(ower_nameEd.getText().toString())) {
                    errorTipTv.setText("");
                    btn_delete.setVisibility(View.GONE);
                    setEditButtonStastus(false);
                } else {

                    btn_delete.setVisibility(View.VISIBLE);
                    setEditButtonStastus(true);
                }
            }
        });
        refreshData();

    }

    public boolean stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字

        String regEx = "^[a-zA-Z][\\\\w]$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);

        return m.matches();

    }

    public String stringFilter1(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        String regEx = "[^a-zA-Z0-9_\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public String stringFilteremail(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        String regEx = "[^a-zA-Z0-9_@.]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public String stringFilterphone(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 更新界面数据
     */
    private void refreshData() {
        switch (type) {
            case 1:
                title.setText(R.string.ui_user_name);
                key = SPUtils.get().getString(PreferenceConstants.USER_NAME);
                inputTipsTv.setText(R.string.ui_user_wrong);
                break;
            case 2:
                title.setText(R.string.ui_by_phone);
                key = SPUtils.get().getString(PreferenceConstants.USER_PHONE);
                inputTipsTv.setText("");
                break;
            case 3:
                title.setText(R.string.ui_by_email);
                key = SPUtils.get().getString(PreferenceConstants.USER_EMAIL);
                inputTipsTv.setText("");
                break;

        }

        if (type == 2) {
            chooseCountryLay.setVisibility(View.VISIBLE);
            countryNum = SPUtils.get().getString(PreferenceConstants.COUNTRY_NUM);
            countryCodeTv.setText("+" + countryNum);
        } else {
            chooseCountryLay.setVisibility(View.GONE);
            ower_nameEd.setText(key);
        }

    }

    /**
     * 检查输入,有误提示，无误提交编辑
     */
    private void checkInput() {
        if (!TextUtils.isEmpty(ower_nameEd.getText().toString())) {
            btn_delete.setVisibility(View.VISIBLE);
            String inputContent = ower_nameEd.getText().toString();
            if (type == 1) {
                if (inputContent.length() < 3 || inputContent.length() > 20) {
                    errorTipTv.setText(R.string.ui_user_length);
                    setEditButtonStastus(false);
                    return;
                } else if (!Tools.isCorrectUserFormat(inputContent.toString())) {
                    errorTipTv.setText(R.string.ui_user_wrong);
                    setEditButtonStastus(false);
                    return;
                }
            } else if (type == 2) {
                if (!TextUtils.isEmpty(inputContent) && !Tools.isNumeric(inputContent)) {
                    errorTipTv.setText(R.string.ui_input_correct_phone);
                    setEditButtonStastus(false);
                    return;
                }

            } else if (type == 3) {
                if (!TextUtils.isEmpty(inputContent) && !Tools.mailAddressVerify(inputContent)) {
                    errorTipTv.setText(R.string.ui_input_correct_mial);
                    setEditButtonStastus(false);
                    return;
                }
            }
            if (inputContent.equals(key)) {// 如果输入没有改变的话同样处于不能提交的状态
                errorTipTv.setText("");
                btn_delete.setVisibility(View.GONE);
                setEditButtonStastus(false);
                return;
            } else {
                errorTipTv.setText("");
                setEditButtonStastus(true);
                doEdit();
            }

        } else {
            errorTipTv.setText("");
            btn_delete.setVisibility(View.GONE);
            setEditButtonStastus(false);
            return;
        }
    }


    /**
     * 设置编辑按钮的状态
     *
     * @param isClickable 是否可以点击
     */
    private void setEditButtonStastus(boolean isClickable) {
        if (isClickable) {
            editButton.setClickable(true);
            editButton.setTextColor(mContext.getResources().getColor(R.color.text_color_t5));
        } else {
            editButton.setClickable(false);
            editButton.setTextColor(mContext.getResources().getColor(R.color.text_color_t4));
        }
    }

    /**
     * 编辑用户资料
     */
    private void doEdit() {

        EditUserInfoRequest request = new EditUserInfoRequest();
        if (type == 1) {
            request.setUserName(ower_nameEd.getText().toString());
        } else if (type == 2) {
            request.setUserPhone(ower_nameEd.getText().toString());
            request.setCountryCode(countryNum);
        } else if (type == 3) {
            request.setUserEmail(ower_nameEd.getText().toString());
        }
        request.setUserGender(genderValue);
        UserConfigReponsitory.get().modifyUserInfo(request, new IUserConfigDataSource.ModifyUserInfoCallback() {


            @Override
            public void onSuccess(UserInfo userInfo) {
                ToastUtils.showShortToast(R.string.edit_info_success);
                switch (type) {
                    case 1:
                        SPUtils.get().put(PreferenceConstants.USER_NAME, userInfo.getUserName());
                        break;
                    case 2:
                        SPUtils.get().put(PreferenceConstants.USER_PHONE,userInfo.getUserPhone());
                        break;
                    case 3:
                        SPUtils.get().put(PreferenceConstants.USER_EMAIL, userInfo.getUserEmail());
                        break;
                    default:
                        break;
                }
                EditUserInfoActivity.this.finish();
            }

            @Override
            public void onFail(ThrowableWrapper e) {
                ToastUtils.showShortToast(e.getMessage());
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_name_delete:
                ower_nameEd.setText("");
                errorTipTv.setText("");
                setEditButtonStastus(false);
                break;
            case R.id.btn_top_right:
                doEdit();
                break;
            case R.id.country_code_lay: {
                Intent intent = new Intent();
                intent.setClass(mContext, CountryActivity.class);
                startActivityForResult(intent, Constants.GET_COUNTRY_REQUEST);
            }
            break;
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GET_COUNTRY_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            countryNum = bundle.getString("countryNumber").substring(1);
            countryCodeTv.setText(bundle.getString("countryNumber"));
        }
    }


}

