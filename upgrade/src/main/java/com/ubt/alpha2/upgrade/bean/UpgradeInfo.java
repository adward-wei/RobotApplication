package com.ubt.alpha2.upgrade.bean;

import com.ubt.alpha2.upgrade.db.UpgradeTable;

/**
 * @author: slive
 * @description: upgrade info
 * @create: 2017/6/29
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class UpgradeInfo {
    public boolean fileAvailable;
    public String filePath;

    public UpgradeInfo(){

    }

    public UpgradeInfo(boolean fileAvailable){
        this.fileAvailable = fileAvailable;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("fileAvailable: "+fileAvailable);
        builder.append(",");
        builder.append("filePath: "+filePath);
        return builder.toString();
    }
}
