package com.ubt.alpha2.upgrade;

import com.ubt.alpha2.upgrade.bean.UpgradeModuleManager;
import com.ubt.alpha2.upgrade.impl.ITaskCompleteListener;
import com.ubt.alpha2.upgrade.impl.IUpgradeTask;
import com.ubt.alpha2.upgrade.utils.ApkUtils;
import com.ubt.alpha2.upgrade.utils.Constants;
import com.ubt.alpha2.upgrade.utils.LogUtils;
import com.ubt.alpha2.upgrade.utils.PropertyUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: slive
 * @description: 升级调度器
 * @create: 2017/6/28
 * @email: slive.shu@ubtrobot.com
 * @modified: slive
 */

public class ScheduleManager implements ITaskCompleteListener {
    private Deque<IUpgradeTask>  upgradeTaskDeque;
    private ExecutorService singleThreadExecutor;

    private ScheduleManager(){
        upgradeTaskDeque = new ArrayDeque<>();
        singleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    public static ScheduleManager getInstance(){
        return ScheduleManagerHolder._instance;
    }

    private static class ScheduleManagerHolder{
        private  static ScheduleManager _instance = new ScheduleManager();
    }

    public void addTask(IUpgradeTask upgradeTask){
        if(!upgradeTaskDeque.contains(upgradeTask))
            upgradeTaskDeque.add(upgradeTask);
    }

    public void addFirstTask(IUpgradeTask upgradeTask){
        upgradeTaskDeque.add(upgradeTask);
    }

    public void removeTask(IUpgradeTask upgradeTask){
        upgradeTaskDeque.remove(upgradeTask);
    }

    public void startTask(){
        LogUtils.d("startTask");
        getTaskAndExecute();
    }

    public boolean isAllTaskCompleted(){
        if(upgradeTaskDeque == null || upgradeTaskDeque.size() ==0)
            return true;
        return false;
    }

    @Override
    public void onTaskCompleted(IUpgradeTask task) {
        LogUtils.d("onTaskCompleted");
        getTaskAndExecute();
    }

    private void getTaskAndExecute(){
        LogUtils.d("upgradeTaskDeque: "+upgradeTaskDeque);
        if(upgradeTaskDeque!= null && upgradeTaskDeque.size()>0){
            PropertyUtils.getSystemProperty(UpgradeApplication.getContext(), Constants.IS_LYNX_INSTALLING).equals("false");
            LogUtils.d("upgradeTaskDeque.size: "+upgradeTaskDeque.size());
            IUpgradeTask upgradeTask = upgradeTaskDeque.removeFirst();
            upgradeTask.setTaskFinishListener(this);
            singleThreadExecutor.execute(upgradeTask);
        }else {
            if (UpgradeModuleManager.getInstance().isNeededReboot()) {
                ApkUtils.rebootRobot();
            }
        }
        UpgradeModuleManager.getInstance().upgradeFinish();
    }
}

