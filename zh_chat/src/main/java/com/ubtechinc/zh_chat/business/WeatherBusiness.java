package com.ubtechinc.zh_chat.business;

import android.content.Context;

import com.ubtech.iflytekmix.R;
import com.ubtechinc.zh_chat.nets.business.AddRecord;
import com.ubtechinc.zh_chat.robot.UBTSemanticRootProxy;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <pre>
 *   @author: Logic
 *   @email : 2091843903@qq.com
 *   @time  : 2017/3/24
 *   @desc  :
 * </pre>
 */

public class WeatherBusiness extends BaseBusiness {

    private String date;
    private String city;
    private String wind;
    private String weather;
    private String tempRange;


    private void setDate(String date) {
        this.date = date;
    }

    public WeatherBusiness(Context cxt) {
        super(cxt);
    }

    /**
     * @param date 当前时间
     */
    public void setDate2(String date) {
		String nowData = getNowTime();// 当前时间
		if (date.equals("CURRENT_DAY") || nowData.compareTo(date) == 0) {//今天
			this.setDate(getContext().getString(R.string.day_today));
		} else if (nowData.compareTo(date) == 1) {//昨天
			this.setDate(getContext().getString(R.string.day_yesterday));
		} else if (nowData.compareTo(date) == -1) {//明天
			this.setDate(getContext().getString(R.string.day_tomorrow));
		} else if (nowData.compareTo(date) == -2) {//后天
			this.setDate(getContext().getString(R.string.day_after_tomorrow));
		} else {
			this.setDate(date);
		}
        this.setDate(date);
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setTempRange(String tempRange) {
        this.tempRange = tempRange;
    }

    @Override
    public String getContent() {
        String str;
        if(city==null){
            str= mContext.getString(R.string.weather_no_data);
        }else{
            str= city + "" + date + mContext.getString(R.string.weather_temperature) + tempRange + "," + weather + wind;
        }
        return str;
    }

    public String getNowTime() {
        Date now = new Date();
        String format = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);// 可以方便地修改日期格式
        return dateFormat.format(now);
    }

    @Override
    public void start(UBTSemanticRootProxy handle) {
        handle.start_TTS(getContent(),false);
        AddRecord.instance().requestAddRecord(Type.WEATHER.getValue(), getContent(), null, null, getSpeechResult());
    }

    @Override
    public void clean(UBTSemanticRootProxy handle) {

    }
}
