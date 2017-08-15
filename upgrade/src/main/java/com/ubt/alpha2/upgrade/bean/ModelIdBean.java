package com.ubt.alpha2.upgrade.bean;

import java.util.ArrayList;

/**
 * @author: slive
 * @description: 服务器端 model Id 信息
 * @create: 2017/6/28
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 * @json: {"data":[{"id":1,"name":"android"}],"code":"0","msg":"success"}
 */
public class ModelIdBean {
    private ArrayList<IdInfo> data = new ArrayList<>();
    public  String code="" ;
    public  String msg="" ;

    public ArrayList<IdInfo> getData() {
        return data;
    }

    public void setData(ArrayList<IdInfo> data) {
        this.data = data;
    }

    public static class IdInfo {
        public String id;
        public String name;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("code: "+code+",");
        sb.append("msg: "+msg+",");
        if(data != null) {
            sb.append("[");
            for(IdInfo info: data) {
                sb.append("{id: "+info.id+",name: "+info.name+"} ,");
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
