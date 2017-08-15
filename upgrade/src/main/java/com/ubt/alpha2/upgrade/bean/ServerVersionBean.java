package com.ubt.alpha2.upgrade.bean;

import java.util.ArrayList;
/**
 * @author: slive
 * @description: 更新module的版本信息
 * @create: 2017/6/28
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 *         {
                "upgradeType": "0",
                "md5Main": "A43E1777E3AA494F93CA6E72FDD8A81D",
                "module_id": "1",
                "remark": "????",
                "fromVersion": "v1.0",
                "md5": "A43E1777E3AA494F93CA6E72FDD8A81D",
                "updateType": "0",
                "toVersion": "v1.2",
                "urlMain": "http://ol1o6u36w.bkt.clouddn.com//server_30/main/82/1/1491568865543/FallingRecognizer.apk",
                "url": "http://ol1o6u36w.bkt.clouddn.com//server_30/main/82/1/1491568865543/FallingRecognizer.apk"
}
 */
public class ServerVersionBean {

    private ArrayList<ModuleInfo>  version = new ArrayList<>();
    private ArrayList<TimeInfo> times = new ArrayList<>();

    public ArrayList<ModuleInfo> getVersion() {
        return version;
    }

    public void setVersion(ArrayList<ModuleInfo> moduleInfoList) {
        this.version = moduleInfoList;
    }

    public class ModuleInfo {
        public String upgradeType;
        public String MD5Main;
        public String module_id;
        public String remark;
        public String fromVersion;
        public String md5;
        public String updateType;
        public String toVersion;
        public String urlMain;
        public String url;

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("upgradeType: "+upgradeType);
            sb.append(",module_id: "+module_id);
            sb.append(",fromVersion: "+fromVersion);
            sb.append(",toVersion: "+toVersion);
            sb.append(",updateType: "+upgradeType);
            sb.append(",url: "+url);
            sb.append(",md5: "+md5);
            sb.append(",urlMain: "+urlMain);
            sb.append(",MD5Main: "+MD5Main);
            sb.append(",remark: "+remark);
            return sb.toString();
        }
    }

    public class TimeInfo{
        String time;
        String module_id;
    }
}



