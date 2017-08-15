package com.ubtechinc.alpha2ctrlapp.entity;

import java.io.Serializable;

/**
 * @author：tanghongyu
 * @date：2016/8/30 16:37
 * @modifier：tanghongyu
 * @modify_date：2016/8/30 16:37
 * [A brief description]
 * version
 */
public class ThirdVerify  implements Serializable {

    //第三方App图像
    private String code;
    //第三方App图像
    private String appHeadImage;
    //第三方App名称
    private String appName;
    // 作用域， 1 获取你的公开信息（昵称，头像）2 控制的机器人
    private String alpha2Authority;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppHeadImage() {
        return appHeadImage;
    }

    public void setAppHeadImage(String appHeadImage) {
        this.appHeadImage = appHeadImage;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAlpha2Authority() {
        return alpha2Authority;
    }

    public void setAlpha2Authority(String alpha2Authority) {
        this.alpha2Authority = alpha2Authority;
    }
}
