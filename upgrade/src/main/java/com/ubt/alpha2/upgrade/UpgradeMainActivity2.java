package com.ubt.alpha2.upgrade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ubt.alpha2.statistics.StatisticsWrapper;
import com.ubt.alpha2.upgrade.action.ActionUpgradeTask;
import com.ubt.alpha2.upgrade.bean.UpgradeModel;
import com.ubt.alpha2.upgrade.bean.UpgradeModuleManager;
import com.ubt.alpha2.upgrade.utils.UpgradeFeedbackConfig;

public class UpgradeMainActivity2 extends AppCompatActivity implements View.OnClickListener{

    Button tvUpgradeAll;
    Button tvUpgradeSelf;
    Button tvUpgradeMainService;
    Button tvUpgradeChest;
    Button tvUpgradeBattery;
    Button tvUpgradeAndroidOs;
    Button tvTest;
    Button tvAction;
    Button actionReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_main2);
        initViews();
        bindListener();
    }

    private void initViews(){
        tvUpgradeAll = (Button)findViewById(R.id.upgrade_all);
        tvUpgradeSelf = (Button)findViewById(R.id.upgrade_self);
        tvUpgradeMainService = (Button)findViewById(R.id.upgrade_mainservice);
        tvUpgradeChest = (Button)findViewById(R.id.upgrade_chest);
        tvUpgradeBattery = (Button)findViewById(R.id.upgrade_battery);
        tvUpgradeAndroidOs = (Button)findViewById(R.id.upgrade_androidos);
        tvTest = (Button)findViewById(R.id.test);
        tvAction = (Button)findViewById(R.id.action);
        actionReport = (Button)findViewById(R.id.action_report);
    }

    private void bindListener(){
        tvUpgradeAll.setOnClickListener(this);
        tvUpgradeSelf.setOnClickListener(this);
        tvUpgradeMainService.setOnClickListener(this);
        tvUpgradeChest.setOnClickListener(this);
        tvUpgradeBattery.setOnClickListener(this);
        tvUpgradeAndroidOs.setOnClickListener(this);
        tvTest.setOnClickListener(this);
        tvAction.setOnClickListener(this);
        actionReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.upgrade_all:
                invokeUpgradeAll();
                break;
            case R.id.upgrade_self:
                invokeUpgradeSelf();
                break;
            case R.id.upgrade_mainservice:
                invokeUpgradeMainService();
                break;
            case R.id.upgrade_chest:
                invokeUpgradeChestService();
                break;
            case R.id.upgrade_battery:
                invokeUpgradeBatteryService();
                break;
            case R.id.upgrade_androidos:
                invokeUpgradeAndroidOsService();
                break;
            case R.id.test:
                Intent intent = new Intent();
                intent.setClass(this,UpgradeMainActivity.class);
                startActivity(intent);
                break;
            case R.id.action:
                invokeActionUpgrade();
                break;
            case R.id.action_report:
                invokeActionReport();
                break;
        }
    }

    private void invokeUpgradeAll(){
        UpgradeManager upgradeManager = UpgradeManager.getInstance();
        upgradeManager.startTask();
    }

    private void invokeUpgradeSelf(){
        UpgradeManager upgradeManager = UpgradeManager.getInstance();
        upgradeManager.initUpgradeTest(UpgradeModel.SELF_MODLE_NAME);
        upgradeManager.startTask();
    }

    private void invokeUpgradeMainService(){
        UpgradeManager upgradeManager = UpgradeManager.getInstance();
        UpgradeModuleManager.getInstance().resetUpgradeModule();
        upgradeManager.initUpgradeTest(UpgradeModel.MAINSERVICE_MODLE_NAME);
        upgradeManager.startTask();
    }

    private void invokeUpgradeChestService(){
        UpgradeManager upgradeManager = UpgradeManager.getInstance();
        UpgradeModuleManager.getInstance().resetUpgradeModule();
        upgradeManager.initUpgradeTest(UpgradeModel.MODEL_EMBEDDED_CHEST);
        upgradeManager.startTask();
    }

    private void invokeUpgradeBatteryService(){
        UpgradeManager upgradeManager = UpgradeManager.getInstance();
        UpgradeModuleManager.getInstance().resetUpgradeModule();
        upgradeManager.initUpgradeTest(UpgradeModel.MODEL_EMBEDDED_BATTERY);
        upgradeManager.startTask();
    }

    private void invokeUpgradeAndroidOsService(){
        UpgradeManager upgradeManager = UpgradeManager.getInstance();
        UpgradeModuleManager.getInstance().resetUpgradeModule();
        upgradeManager.initUpgradeTest(UpgradeModel.ANDROID_OS);
        upgradeManager.startTask();
    }

    @Override
    public void onResume(){
        super.onResume();
        StatisticsWrapper.getInstance().onResume(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        StatisticsWrapper.getInstance().onPause(this);
    }

    private void invokeActionUpgrade(){
        ActionUpgradeTask actionUpgradeTask = new ActionUpgradeTask();
        new Thread(actionUpgradeTask).start();
    }

    private void invokeActionReport(){
        UpgradeFeedbackConfig.getInstance().saveActionErrorReport(getString(R.string.md5_error),"001");
        UpgradeFeedbackConfig.getInstance().saveActionErrorReport(getString(R.string.unzip_error),"002");
    }
}


