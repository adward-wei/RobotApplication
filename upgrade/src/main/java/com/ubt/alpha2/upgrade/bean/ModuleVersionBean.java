package com.ubt.alpha2.upgrade.bean;

/**
 * @author: slive
 * @description: 获得跟新module的版本信息
 * @create: 2017/6/28
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class ModuleVersionBean {
    public String code ;
    public String msg ;
    private ServerVersionBean data;

    public ServerVersionBean getData() {
        return data;
    }

    public void setData(ServerVersionBean data) {
        this.data = data;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("code: "+code+",");
        stringBuilder.append("msg: "+msg+",");
        if(data != null){
            stringBuilder.append("[");
            stringBuilder.append("data: "+data);
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }
}
