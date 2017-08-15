package com.ubt.alpha2.download.impl;

import com.ubt.alpha2.download.core.DownloadStatus;

/**
 * @author: liwushu
 * @description: handler for download status
 * @created: 2017/6/20
 * @version: 1.0
 * @modify: liwushu
*/
public interface IDownloadStatusDelivery {

    void post(DownloadStatus status);

}
