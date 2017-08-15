package com.ubtechinc.alpha2ctrlapp.events;


import com.ubtechinc.alpha2ctrlapp.entity.business.app.ApkDownLoad;

/**
 * @author：tanghongyu
 * @date：2016/10/13 13:51
 * @modifier：tanghongyu
 * @modify_date：2016/10/13 13:51
 * [A brief description]
 * version
 */

public class DownloadFileEvent {

    ApkDownLoad downLoad;
    public DownloadFileEvent(ApkDownLoad apkDownLoad) {
        this.downLoad = apkDownLoad;
    }

    public ApkDownLoad getDownLoad() {
        return downLoad;
    }


}
