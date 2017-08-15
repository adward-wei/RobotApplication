package com.ubtechinc.alpha2ctrlapp.ui.activity.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.BusinessConstants;
import com.ubtechinc.alpha2ctrlapp.data.robot.IRobotInitDataSource;
import com.ubtechinc.alpha2ctrlapp.data.robot.RobotInitRepository;
import com.ubtechinc.alpha2ctrlapp.ui.activity.base.BaseContactActivity;
import com.ubtechinc.alpha2ctrlapp.ui.activity.main.MainPageActivity;
import com.ubtechinc.alpha2ctrlapp.util.Tools;
import com.ubtechinc.alpha2ctrlapp.widget.dialog.LoadingDialog;
import com.ubtechinc.nets.http.ThrowableWrapper;

import static com.tencent.qalsdk.service.QalService.context;


/**
 *
 * 名称设置类
 */
public  class SetAlphaOwnerNameActivity extends BaseContactActivity implements OnClickListener  {

	public static final String OWNER_NAME = "owner_name";

	private EditText ower_nameEd; // 编辑框
	private ImageView btn_delete; // 删除按钮
	private TextView editButton;
	private boolean isEnglish=true; // 机器人的系统是否是英语
	private TextView errorTipTv;  // 编辑框输入错误提示
	private TextView inputTipsTv; // 输入提示

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alpha_set_owoner_name);
		this.mContext = this;
		initView();
	}

	


	public void initView() {
		TextView tittleTv = (TextView) findViewById(R.id.authorize_title);
		tittleTv.setText(R.string.robot_address_setting);
		ower_nameEd = (EditText)findViewById(R.id.ower_name);
		btn_delete = (ImageView)findViewById(R.id.ower_name_image);
		btn_delete.setOnClickListener(this);
		editButton = (TextView)findViewById(R.id.btn_top_right);
		editButton.setVisibility(View.VISIBLE);
		editButton.setOnClickListener(this);
		editButton.setText(R.string.common_btn_confirm);
		errorTipTv = (TextView)findViewById(R.id.input_error_tips);
		inputTipsTv = (TextView)findViewById(R.id.input_owner_name_tips);
		/**
		 * 监听输入框的输入
		 */
		ower_nameEd.addTextChangedListener(new TextWatcher() {

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
				if(!TextUtils.isEmpty(ower_nameEd.getText().toString())){
					btn_delete.setVisibility(View.VISIBLE);
					if(ower_nameEd.getText().toString().length()>10){ // 最多10个字符
						errorTipTv.setText(R.string.address_setting_error_character);
						setEditButtonStastus(false);
					}else{
						if(isEnglish){// 英语状态下只能包含数字和字母
							if(Tools.isContainChinese(ower_nameEd.getText().toString())){
								errorTipTv.setText(R.string.address_setting_error_sinogram);
								setEditButtonStastus(false);
							}else{
								if(!Tools.isLetterOrDigital(ower_nameEd.getText().toString())){
									errorTipTv.setText(R.string.address_setting_error_symbol);
									setEditButtonStastus(false);
								}else{
									errorTipTv.setText("");
									setEditButtonStastus(true);
								}
							}
						}else{// 中文状态下只能包含汉字，字母，数字
							if(Tools.isLetterOrDigitalChinese(ower_nameEd.getText().toString())){
								errorTipTv.setText("");
								setEditButtonStastus(true);
							}else{
								errorTipTv.setText(R.string.address_setting_error_symbol);
								setEditButtonStastus(false);
							}
						}

					}


				}else{
					errorTipTv.setText("");
					btn_delete.setVisibility(View.GONE);
					setEditButtonStastus(false);
				}


			}
		});


		if(MainPageActivity.alphaParam!=null){
			if(MainPageActivity.alphaParam.getServiceLanguage().equalsIgnoreCase(BusinessConstants.ROBOT_SERVICE_LANGUAGE_CHINESE)){
				isEnglish= false;
			}else{
				isEnglish = true;
			}
			if(!TextUtils.isEmpty(MainPageActivity.alphaParam.getMasterName())){
				ower_nameEd.setText(MainPageActivity.alphaParam.getMasterName());
			}
		}
		if(isEnglish){
			inputTipsTv.setText(R.string.address_setting_note_en);
		}else{
			inputTipsTv.setText(R.string.address_setting_note_cn);
		}
	}

	/**
	 * 设置编辑按钮的状态
	 * @param isClickable 是否可以点击
     */
	private void setEditButtonStastus(boolean isClickable){
		if(isClickable){
			editButton.setClickable(true);
			editButton.setTextColor(mContext.getResources().getColor(R.color.text_color_t5));
		}else{
			editButton.setClickable(false);
			editButton.setTextColor(mContext.getResources().getColor(R.color.text_color_t4));
		}
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ower_name_image:
				ower_nameEd.setText("");
				errorTipTv.setText("");
				setEditButtonStastus(false);
			break;
			case R.id.btn_top_right:
				setAlphaParam();
				break;
		default:
			break;
		}
		
	}



	/**
	 * 设置Alpha 的名称
	 */
	private void setAlphaParam(){
		LoadingDialog.getInstance(mContext).show();

		RobotInitRepository.getInstance().setMasterName(ower_nameEd.getText().toString(), new IRobotInitDataSource.SetInitDataCallback() {
			@Override
			public void onSuccess() {
				LoadingDialog.dissMiss();
				Intent resultIntent = new Intent();
				resultIntent.putExtra(OWNER_NAME,ower_nameEd.getText().toString());
				setResult(RESULT_OK,resultIntent);
				SetAlphaOwnerNameActivity.this.finish();
			}

			@Override
			public void onFail(ThrowableWrapper e) {

			}
		});


	}
	
}

