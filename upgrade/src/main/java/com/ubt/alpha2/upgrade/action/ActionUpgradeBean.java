package com.ubt.alpha2.upgrade.action;

import java.util.List;

/**
 * @author: slive
 * @description: 动作升级后台数据
 * @create: 2017/7/26
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
//{
//        -"data": {
//        -"result": [
//        -{
//        "actionCreateTime": 1498629746000,
//        "actionId": "1466392694020",
//        "actionMD5": "8B34A5FE949F73ACEBF752A1604C4555",
//        "actionNameCN": "胜利之歌",
//        "actionNameEN": "yankee_doodle_dandy",
//        "actionPackageUrl": "http://video.ubtrobot.com/1466392694020.zip",
//        "actionType": "dance",
//        "id": 822
//        }
//        ]
//        },
//        "msg": "success",
//        "resultCode": 200,
//        "success": true
//        }

public class ActionUpgradeBean{

    public ActionBean data;
    public String msg;
    public int resultCode;
    public boolean success;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("data: [");
        if(data != null){
            for(ActionFileBean bean:data.result){
                sb.append(bean+",");
            }
        }
        sb.append("]");
        sb.append("msg: "+msg);
        sb.append("resultCode: "+resultCode);
        sb.append("success: "+success);
        return sb.toString();
    }

    public static class ActionBean{
        List<ActionFileBean> result;
    }

    /**
     * @author: slive
     * @description: 动作升级列表动作信息
     * @create: 2017/7/26
     * @email: slive.shu@ubtrobot.com
     * @modified: slive
     */
    static class ActionFileBean {
        public int id;
        public String actionId;
        public String actionNameCN;
        public String actionNameEN;
        public String actionType;
        public String actionPackageUrl;
        public String actionMD5;
        public String actionCreateTime;

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("id: "+id+",");
            sb.append("actionId: "+actionId+",");
            sb.append("actionType: "+actionType+",");
            sb.append("actionNameCN: "+actionNameCN+",");
            sb.append("actionNameEN: "+actionNameEN+",");
            sb.append("actionPackageUrl: "+actionPackageUrl+",");
            sb.append("actionMD5: "+actionMD5+",");
            sb.append("actionCreateTime: "+actionCreateTime);
            return sb.toString();
        }
    }
}



