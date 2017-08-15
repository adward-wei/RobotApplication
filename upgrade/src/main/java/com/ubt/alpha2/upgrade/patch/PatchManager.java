package com.ubt.alpha2.upgrade.patch;

import android.util.Log;

import com.cundong.utils.PatchUtils;
import com.ubt.alpha2.upgrade.utils.MD5FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * [A brief description]
 *
 * @author：zengdengyi
 * @date：2016/6/2 9:50
 * @modifier：zengdengyi
 * @modify_date：2016/6/2 9:50
 * [A brief description]
 * version
 */
public class PatchManager {
    String TAG = "Client";
    public boolean startPatch(String old, String newPath, String patch, String md5) {
        boolean ret = false;
        int patchResult = PatchUtils.patch(old, newPath, patch);
        if (patchResult == 0) {
            File big = new File(newPath);
            String newMd5 = MD5FileUtil.getFileMD5String(big).toUpperCase();
            if (newMd5.equals(md5)) {
                ret = true;
            } else {
                ret = false;
            }
            Log.i(TAG,"patch md5 "+ret);
            return ret;
        }else{
            Log.i(TAG,"patch failed");
        }
        return ret;
    }
}