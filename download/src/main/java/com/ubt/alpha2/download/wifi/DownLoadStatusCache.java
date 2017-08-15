package com.ubt.alpha2.download.wifi;


import com.ubt.alpha2.download.CallBack;
import com.ubt.alpha2.download.DownloadRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: liwushu
 * @description:
 * @created: 2017/6/21
 * @version: 1.0
 * @modify: liwushu
*/
public class DownLoadStatusCache {

    static HashMap<String, DownloadRequest> mapRequset = new HashMap<>();
    static HashMap<String, CallBack> mapCallBack = new HashMap<>();
    static List<String> listUrl = new ArrayList<>();

    static public HashMap<String, DownloadRequest> getQuestmap() {
        return mapRequset;
    }

    static public HashMap<String, CallBack> getCallBack() {
        return mapCallBack;
    }

    static public List<String> getUrl() {
        return listUrl;
    }

    static public void setQuestList(DownloadRequest request, CallBack call) {

        mapRequset.put(request.getUri(), request);
        mapCallBack.put(request.getUri(), call);
        listUrl.add(request.getUri());
    }

    static public void remove(String url) {
        mapRequset.remove(url);
        mapCallBack.remove(url);
        listUrl.remove(url);
    }

    static public void clear() {
        mapRequset.clear();
        mapCallBack.clear();
        listUrl.clear();
    }
}
