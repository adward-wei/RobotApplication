package com.ubtechinc.alpha.provider;

import android.content.Context;

import com.ubtechinc.alpha.app.AlphaApplication;
import com.ubtechinc.alpha.upload.photo.PhotoInfo;
import com.ubtechinc.framework.db.EntityManager;
import com.ubtechinc.framework.db.EntityManagerFactory;

import java.util.List;

/**
 * @desc : 照片信息数据库
 * @author: wzt
 * @time : 2017/6/9
 * @modifier:
 * @modify_time:
 */

public class PhotoInfoVisitor extends BaseVisitor<PhotoInfo> {

    private static PhotoInfoVisitor sInstance;

    private PhotoInfoVisitor(Context context) {
        super(context);
    }

    public static PhotoInfoVisitor getInstance() {
        if (sInstance == null) {
            synchronized (PhotoInfoVisitor.class) {
                sInstance = new PhotoInfoVisitor(AlphaApplication.getContext());
            }
        }
        return sInstance;
    }

    /**Note:要考虑版本升级的情况*/
    @Override
    protected EntityManager<PhotoInfo> entityManagerFactory() {
        dbMananger = EntityManagerFactory.getInstance(AlphaApplication.getContext(),
                EntityManagerHelper.DB_VERSION,
                EntityManagerHelper.DB_ACCOUNT,
                null, null)
                .getEntityManager(PhotoInfo.class,
                        EntityManagerHelper.DB_PHOTO_INFO_TABLE);
        return dbMananger;
    }

    public PhotoInfo query(String filePath) {
        List<PhotoInfo> photoInfoList = getAllData();
        for(PhotoInfo item: photoInfoList) {
            if(filePath.equals(item.filePath)) {
                return item;
            }
        }
        return null;
    }
}
