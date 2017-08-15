package com.ubtechinc.alpha2ctrlapp.util.ImageLoad;

import android.content.Context;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ubtechinc.alpha2ctrlapp.util.ImageLoad.transformation.GlideCircleTransform;

/**
 * @author：liuhai
 * @date：2017/5/10 13:53
 * @modifier：ubt
 * @modify_date：2017/5/10 13:53
 * [A brief description]
 * version
 */

public class GlideImageLoadStrategy implements BaseImageLoaderStrategy {


    /**
     * 无Context加载图片
     *
     * @param url
     * @param imageView
     */
    @Override
    public void loadImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url)
                .placeholder(imageView.getDrawable())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * 通过ApplicationContext加载
     *
     * @param url
     * @param imageView
     */
    @Override
    public void loadImageWithAppCxt(String url, ImageView imageView) {
        Glide.with(imageView.getContext().getApplicationContext()).load(url)
                .placeholder(imageView.getDrawable())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * 自定义占位图
     *
     * @param url
     * @param placeholder
     * @param imageView
     */
    @Override
    public void loadImage(String url, int placeholder, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).placeholder(placeholder).dontAnimate()
                .transform(new GlideCircleTransform(imageView.getContext()))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    /**
     * 自定义占位图加载
     *
     * @param context
     * @param url
     * @param placeholder 占位图id
     * @param imageView
     */
    @Override
    public void loadImage(Context context, String url, int placeholder, ImageView imageView) {
        loadNormal(context, url, placeholder, imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param url
     * @param placeholder
     * @param imageView
     */
    @Override
    public void loadCircleImage(String url, int placeholder, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).placeholder(placeholder).dontAnimate()
                .transform(new GlideCircleTransform(imageView.getContext()))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    /**
     * 自定义圆形图的宽度和颜色
     *
     * @param url
     * @param placeholder
     * @param imageView
     * @param borderWidth
     * @param borderColor
     */
    @Override
    public void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor) {
        Glide.with(imageView.getContext()).load(url).placeholder(placeholder).dontAnimate()
                .transform(new GlideCircleTransform(imageView.getContext(), borderWidth, borderColor))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    /**
     * 自定义圆形图片的宽高和边框颜色
     *
     * @param url
     * @param placeholder
     * @param imageView
     * @param borderWidth
     * @param borderColor
     * @param heightPx
     * @param widthPx
     */
    @Override
    public void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor, int heightPx, int widthPx) {
        Glide.with(imageView.getContext()).load(url).placeholder(placeholder).dontAnimate()
                .transform(new GlideCircleTransform(imageView.getContext(), borderWidth, borderColor, heightPx, widthPx))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    /**
     * 加载gif图片
     *
     * @param url
     * @param placeholder
     * @param imageView
     */
    @Override
    public void loadGifImage(String url, int placeholder, ImageView imageView) {
        loadGif(imageView.getContext(), url, placeholder, imageView);
    }


    /**
     * 清除磁盘缓存
     *
     * @param context
     */
    @Override
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context.getApplicationContext()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context.getApplicationContext()).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除内存缓存
     *
     * @param context
     */
    @Override
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context.getApplicationContext()).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * load image with Glide
     */
    private void loadGif(final Context ctx, String url, int placeholder, ImageView imageView) {
        final long startTime = System.currentTimeMillis();
        Glide.with(ctx).load(url).asGif()
                .placeholder(placeholder).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GifDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        })
                .into(imageView);
    }

    /**
     * load image
     */
    private void loadNormal(final Context ctx, final String url, int placeholder, ImageView imageView) {
        /**
         *  为其添加缓存策略,其中缓存策略可以为:Source及None,None及为不缓存,Source缓存原型.如果为ALL和Result就不行.然后几个issue的连接:
         https://github.com/bumptech/glide/issues/513
         https://github.com/bumptech/glide/issues/281
         https://github.com/bumptech/glide/issues/600
         modified by xuqiang
         */

        //去掉动画 解决与CircleImageView冲突的问题 这个只是其中的一个解决方案
        //使用SOURCE 图片load结束再显示而不是先显示缩略图再显示最终的图片（导致图片大小不一致变化）
        final long startTime = System.currentTimeMillis();
        Glide.with(ctx).load(url)
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        })
                .into(imageView);
    }
}
