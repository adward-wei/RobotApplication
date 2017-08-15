package com.ubtechinc.nets;

import com.ubtech.utilcode.utils.LogUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.Semaphore;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/6/19
 * @modifier:
 * @modify_time:
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DownloadManagerTest {

    @Test
    public void test_download(){
        final Semaphore semaphore = new Semaphore(0);
        String url = "http://files.jb51.net/file_images/article/201702/201702031108228.png";
        DownloadManager.getInstance().download(url, new DownloadManager.DownloadListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(DownloadInfo info) {

            }

            @Override
            public void onError(Throwable e) {
                LogUtils.D("error = %s",e.getMessage());
                semaphore.release();
            }

            @Override
            public void onProcess(int progress) {

            }
        });
    }
}
