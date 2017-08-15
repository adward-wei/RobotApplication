package com.ubtechinc.alpha2ctrlapp.util.ImageLoad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.ubtechinc.alpha2ctrlapp.R;
import com.ubtechinc.alpha2ctrlapp.constants.Constants;
import com.ubtechinc.alpha2ctrlapp.widget.DetailImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Handler handler = new Handler();//handler to display images in UI thread
    public static ImageLoader imageLoader;
    public static Context mContext;
    private int mType;
    private Map<DetailImageView, String> detailimageViews = Collections.synchronizedMap(new WeakHashMap<DetailImageView, String>());

    public ImageLoader(Context context) {
        mContext = context;
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    public static ImageLoader getInstance(Context context) {
        mContext = context;
        if (imageLoader == null) {
            imageLoader = new ImageLoader(mContext);
        }
        return imageLoader;
    }

    public void displayImage(String url, ImageView imageView, int type) {
        mType = type;
        if (TextUtils.isEmpty(url)) {
            loadTempImage(imageView);
        } else {


            imageViews.put(imageView, url);
            Bitmap bitmap = memoryCache.get(url + mType);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                queuePhoto(url, imageView);
            }

        }

    }

    public void DisplayAlumImage(String orgUrl, DetailImageView imageView, int type) {
        mType = type;
        if (TextUtils.isEmpty(orgUrl)) {
            loadTempImage(imageView.getImageView());
        } else {
            detailimageViews.put(imageView, orgUrl);
            Bitmap bitmap = memoryCache.get(orgUrl + mType);
            if (bitmap != null)
                imageView.getImageView().setImageBitmap(bitmap);

            else {

                imageView.getProgressLay().setVisibility(View.VISIBLE);
                queueAlumPhoto(orgUrl, imageView);
            }

        }

    }



    public void DisplayRounderImage(String url, ImageView imageView, int type) {
        mType = type;
        if (TextUtils.isEmpty(url)) {

            loadTempImage(imageView);
        } else {
            imageViews.put(imageView, url);
            Bitmap bitmap = memoryCache.get(url + mType);
            if (bitmap != null) {
                imageView.setImageBitmap(PictureHandle.getRoundedCorner(bitmap, type, 10.0f));
            } else {
                queuePhoto(url, imageView);

            }
        }

    }

    private void queueAlumPhoto(String url, DetailImageView imageView) {
        AlumPhotoToLoad p = new AlumPhotoToLoad(url, imageView);
        executorService.submit(new AlumPhotosLoader(p));
    }

    //Task for the queue
    private class AlumPhotoToLoad {
        public String url;
        public DetailImageView imageView;

        public AlumPhotoToLoad(String u, DetailImageView i) {
            url = u;
            imageView = i;
        }
    }

    class AlumPhotosLoader implements Runnable {
        AlumPhotoToLoad photoToLoad;

        AlumPhotosLoader(AlumPhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if (DetailImageViewReused(photoToLoad))
                    return;
                Bitmap bmp = getBitmap(photoToLoad.url, mType);
                memoryCache.put(photoToLoad.url + mType, bmp);// 以图片url和图片类型生成唯一标识
                if (DetailImageViewReused(photoToLoad))
                    return;
                AlumBitmapDisplayer bd = new AlumBitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    //Used to display bitmap in the UI thread
    class AlumBitmapDisplayer implements Runnable {
        Bitmap bitmap;
        AlumPhotoToLoad photoToLoad;

        public AlumBitmapDisplayer(Bitmap b, AlumPhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (DetailImageViewReused(photoToLoad))
                return;
            if (bitmap != null) {
                photoToLoad.imageView.getProgressLay().setVisibility(View.GONE);

                photoToLoad.imageView.getImageView().setImageBitmap(bitmap);
            } else {
                photoToLoad.imageView.getProgressLay().setVisibility(View.GONE);
                loadTempImage(photoToLoad.imageView.getImageView());
            }


        }
    }


    boolean DetailImageViewReused(AlumPhotoToLoad photoToLoad) {
        String tag = detailimageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url)) {
            photoToLoad.imageView.getProgressLay().setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    public String encodeUrl(String url) {
        return Uri.encode(url, "-![.:/,%?&=]");
    }

    public Bitmap getBitmap(String url, int type) {
        if (TextUtils.isEmpty(url)) {
            return null;
        } else {
            Bitmap b = memoryCache.get(url + type); //from memory cache
            if (b == null) {
                File f = fileCache.getFile(url);
                //from SD cache
                b = decodeFile(f);
                if (b != null)
                    return b;
                //from web
                try {

                    Bitmap bitmap = null;
                    url = encodeUrl(url);//解决中文不能显示图片的问题

                    URL imageUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    conn.setRequestProperty("Accept-Encoding", "identity");
                    conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                    conn.setRequestProperty("Accept-Language", "zh-CN");
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
                    conn.setRequestProperty("Connection", "Keep-Alive");

                    conn.setInstanceFollowRedirects(true);
                    conn.connect();
                    conn.getResponseCode();

                    InputStream is = conn.getInputStream();
                    OutputStream os = new FileOutputStream(f);
                    CopyStream(is, os);
                    os.close();
                    conn.disconnect();
                    bitmap = decodeFile(f);
                    return bitmap;
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    if (ex instanceof OutOfMemoryError)
                        memoryCache.clear();
                    return null;
                }
            } else {
                return b;
            }
        }
    }
    public  void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();
            int REQUIRED_SIZE = 70;
            //Find the correct scale value. It should be the power of 2.
            if (mType == 6) {
                REQUIRED_SIZE = 200;
            }
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            if (mType == 4 || mType == 8) {
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = (height_tmp / Constants.appDetailImageHeight + width_tmp / Constants.appImageWidth) / 2;
                FileInputStream stream2 = new FileInputStream(f);
                Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
                stream2.close();
                return bitmap;
            }
            if (mType == 3) {
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = (height_tmp / Constants.appRecomlImageHeight + width_tmp / Constants.appImageWidth) / 2;
                FileInputStream stream2 = new FileInputStream(f);
                Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
                stream2.close();
                return bitmap;
            } else if (mType == 7) {
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = 1;
                FileInputStream stream2 = new FileInputStream(f);
                Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
                stream2.close();
                return bitmap;
            } else if (mType == 6) {
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                if (height_tmp > Constants.appRecomlImageHeight || width_tmp > Constants.appImageWidth) {
                    o2.inSampleSize = (height_tmp / Constants.appRecomlImageHeight + width_tmp / Constants.appImageWidth) / 2;
                } else
                    o2.inSampleSize = 1;
                FileInputStream stream2 = new FileInputStream(f);
                Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
                stream2.close();
                return bitmap;
            } else {
                while (true) {
                    if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                        break;
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }
                //decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                FileInputStream stream2 = new FileInputStream(f);
                Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
                stream2.close();
                return bitmap;

            }


        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadTempImage(ImageView imageView) {
        switch (mType) {
            case 0:
                imageView.setImageResource(R.drawable.no_head);
                break;
            case 1:
                imageView.setImageResource(R.drawable.no_app);
                break;
            case 2:
                imageView.setImageResource(R.drawable.no_action);
                break;
            case 3:
                imageView.setImageResource(R.drawable.action_lib_introduction_2);
                break;
            case 5:
                imageView.setImageResource(R.drawable.header_item_icon);
                break;
            case 4:
                imageView.setImageResource(R.drawable.no_photo);
            case 6:
                imageView.setImageResource(R.drawable.no_photo);
                break;
            case 7:
                imageView.setImageResource(R.drawable.no_photo);//相册加载缩略图
                break;
            case 8:
                imageView.setImageResource(R.drawable.no_photo);//相册加载图
                break;
            default:
                break;
        }
    }

    //Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if (imageViewReused(photoToLoad))
                    return;
                Bitmap bmp = getBitmap(photoToLoad.url, mType);
                memoryCache.put(photoToLoad.url + mType, bmp);// 以图片url和图片类型生成唯一标识
                if (imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                loadTempImage(photoToLoad.imageView);

        }
    }


    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

    public Bitmap grey(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap
                .createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }


}
