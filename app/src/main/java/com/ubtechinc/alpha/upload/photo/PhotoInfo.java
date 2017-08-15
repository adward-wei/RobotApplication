package com.ubtechinc.alpha.upload.photo;

import com.ubtechinc.alpha.provider.EntityManagerHelper;
import com.ubtechinc.framework.db.annotation.Column;
import com.ubtechinc.framework.db.annotation.GenerationType;
import com.ubtechinc.framework.db.annotation.Table;

import java.io.Serializable;

/**
 * @desc : 照片信息
 * @author: wzt
 * @time : 2017/6/9
 * @modifier:
 * @modify_time:
 */

@Table(version = EntityManagerHelper.DB_PHOTO_INFO_VERSION)
public class PhotoInfo implements Serializable {

    private static final long serialVersionUID = 1122907755312771561L;

    @com.ubtechinc.framework.db.annotation.Id(strategy = GenerationType.AUTO_INCREMENT)
    public int Id;
    @Column
    public String filePath;  // 文件本地路径
    @Column
    public String url; // 文件在服务器的url

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
