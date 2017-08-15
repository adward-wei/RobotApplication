package com.ubtechinc.alpha2ctrlapp.util.ImageLoad;

import android.content.Context;
import android.widget.ImageView;

/**
 * @author：liuhai
 * @date：2017/5/10 11:23
 * @modifier：ubt
 * @modify_date：2017/5/10 11:23
 * [A brief description]
 * version
 */

public interface BaseImageLoaderStrategy {
    //无占位图
    void loadImage(String url, ImageView imageView);

    //这里的context指定为ApplicationContext
    void loadImageWithAppCxt(String url, ImageView imageView);

    void loadImage(String url, int placeholder, ImageView imageView);

    void loadImage(Context context, String url, int placeholder, ImageView imageView);

    void loadCircleImage(String url, int placeholder, ImageView imageView);

    void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor);

    void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor, int heightPx, int widthPx);

    void loadGifImage(String url, int placeholder, ImageView imageView);


    //清除硬盘缓存
    void clearImageDiskCache(final Context context);

    //清除内存缓存
    void clearImageMemoryCache(Context context);

}
