package com.ubt.alpha2.upgrade.bean;

import android.text.TextUtils;

import com.ubt.alpha2.upgrade.impl.IGetLocalModuleInfo;
import com.ubt.alpha2.upgrade.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: slive
 * @description:  模块更新信息的管理类
 * @create: 2017/7/15
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */
public class UpgradeModuleManager {
    private List<IGetLocalModuleInfo> localModuleInfoList;
    private List<String> localModuleList;
    private ServerVersionBean serverVersionBean;
    private AccessTokenInfo accessTokenInfo;
    private ModelIdBean modelIdBean;
    // upgrade module info
    private ModuleVersionBean moduleVersionBean;
    private boolean isNeededReboot = false;

    private UpgradeModuleManager(){
        localModuleInfoList = new ArrayList<>();
        localModuleList = new ArrayList<>();
    }

    public static UpgradeModuleManager getInstance(){
        return ModuleManagerHolder._install;
    }

    private static class ModuleManagerHolder{
        private static UpgradeModuleManager _install = new UpgradeModuleManager();
    }

    public void addModule(IGetLocalModuleInfo localModuleInfo){
        if(!localModuleInfoList.contains(localModuleInfo))
           localModuleInfoList.add(localModuleInfo);
    }


    public void addModule(String moduleName){
        if(!localModuleList.contains(moduleName))
            localModuleList.add(moduleName);
    }

    public void clearModule(){
        if(localModuleList != null)
            localModuleList.clear();
    }

    /**
     * 获取更新模块的moduleName (moduleId-moduleVersion)
     * @return
     */
    public String getModuleNameList(){
        if(localModuleList == null || localModuleList.isEmpty())
            return null;
        if(modelIdBean == null || modelIdBean.getData() == null || modelIdBean.getData().isEmpty())
            return null;
        StringBuilder moduleNameBuilder = new StringBuilder();
        for(int i=0;i<localModuleList.size();i++){
            String moduleName = localModuleList.get(i);
            if(TextUtils.isEmpty(moduleName))
               continue;
            String moduleId = getModuleId(moduleName);
            String localVersion = UpgradeModel.getModuleVersion(moduleName);
            if(TextUtils.isEmpty(moduleId) ||TextUtils.isEmpty(localVersion))
                continue;
            moduleNameBuilder.append(moduleId);
            moduleNameBuilder.append("-");
            if(!localVersion.startsWith("v"))
                localVersion = "v"+localVersion;
            moduleNameBuilder.append(localVersion);

            if(i <localModuleList.size()-1){
                moduleNameBuilder.append(";");
            }
        }
        return moduleNameBuilder.toString();
    }

    public String getModuleId(String moduleName){
        if(modelIdBean == null || modelIdBean.getData() == null || modelIdBean.getData().isEmpty())
            return null;
        LogUtils.d("size: "+modelIdBean.getData().size());
        for(int i = 0; i< modelIdBean.getData().size(); i++){
            ModelIdBean.IdInfo idInfo = modelIdBean.getData().get(i);
            String name= idInfo.name;
            LogUtils.d("name: "+name+"  moduleName: "+moduleName);
            if(moduleName.equals(name))
                return idInfo.id;
            continue;
        }
        return null;
    }

    public String getAccessToken(){
        if(accessTokenInfo == null)
            return "";
        return accessTokenInfo.access_token;
    }

    public ModuleVersionBean getModuleVersionBean() {
        return moduleVersionBean;
    }

    public void setModuleVersionBean(ModuleVersionBean moduleVersionBean) {
        this.moduleVersionBean = moduleVersionBean;
        if(moduleVersionBean != null)
            this.serverVersionBean = moduleVersionBean.getData();
        else
            this.serverVersionBean = null;
    }

    public ModelIdBean getModelIdBean() {
        return modelIdBean;
    }

    public void setModelIdBean(ModelIdBean modelIdBean) {
        this.modelIdBean = modelIdBean;
    }

    public AccessTokenInfo getAccessTokenInfo() {
        return accessTokenInfo;
    }

    public void setAccessTokenInfo(AccessTokenInfo accessTokenInfo) {
        this.accessTokenInfo = accessTokenInfo;
    }

    public ServerVersionBean getServerVersionBean() {
        return serverVersionBean;
    }

    public ServerVersionBean.ModuleInfo getUpgradeModuleInfo(String moduleName){
        String moduleId = getModuleId(moduleName);
        if(TextUtils.isEmpty(moduleId))
            return null;
        serverVersionBean = moduleVersionBean.getData();
        if(serverVersionBean == null || serverVersionBean.getVersion() == null || serverVersionBean.getVersion().isEmpty()) {
            LogUtils.d("serverVersionBean == null || version.isEmpty");
            return null;
        }
        List<ServerVersionBean.ModuleInfo> moduleInfoList = serverVersionBean.getVersion();
        for(ServerVersionBean.ModuleInfo moduleInfo:moduleInfoList){
            if(moduleId.equals(moduleInfo.module_id))
                return moduleInfo;
        }
        return null;
    }

    public void upgradeFinish(){
        serverVersionBean = null;
        accessTokenInfo = null;
        modelIdBean = null;
        moduleVersionBean = null;
        isNeededReboot = false;
    }

    public String getModuleName(String moduleId){
        if(TextUtils.isEmpty(moduleId))
            return null;
        LogUtils.d("modelIdBean: "+modelIdBean);
        if(modelIdBean == null)
            return null;
        ArrayList<ModelIdBean.IdInfo> idInfoList = modelIdBean.getData();
        LogUtils.d("idInfoList: "+idInfoList);
        if(idInfoList == null || idInfoList.isEmpty())
            return null;
        for(ModelIdBean.IdInfo info:idInfoList){
            if(TextUtils.isEmpty(info.id))
                continue;
            if(info.id.equals(moduleId))
                return info.name;
        }
        return null;
    }

    public boolean isNeededReboot() {
        return isNeededReboot;
    }

    public void setNeededReboot(boolean neededReboot) {
        isNeededReboot = neededReboot;
    }

    public void resetUpgradeModule(){
        isNeededReboot = false;
        accessTokenInfo = null;
        modelIdBean = null;
        moduleVersionBean = null;
        serverVersionBean = null;
    }
}
