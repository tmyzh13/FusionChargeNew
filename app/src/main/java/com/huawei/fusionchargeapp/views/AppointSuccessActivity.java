package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.common.AppManager;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.rxbus.RxBus;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.model.beans.HomeAppointmentBean;
import com.huawei.fusionchargeapp.model.beans.HomeRefreshBean;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

public class AppointSuccessActivity extends BaseActivity {
    private static final String TAG = AppointSuccessActivity.class.getSimpleName();
    private Context context = AppointSuccessActivity.this;
    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.arrive_on_time_tv)
    TextView arriveOnTimeTv;
    @Bind(R.id.action_go_tv)
    TextView actionGoTv;
    @Bind(R.id.action_guild_tv)
    TextView actionGuildTv;

    private int time;
    private String gunCode;
    private int chargingPileId;
    private String chargingPileName;
    private double latitude;
    private double longitude;
    private String runCode;
    private String address;
    private String reserveId;
    private Timer timerAppointment;
    private long appointmentTime;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_appoint_success;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        
        navBar.setColorRes(R.color.app_blue);
        navBar.setNavTitle(context.getString(R.string.appoint_success));
        time = getIntent().getIntExtra("time", 0);
        if (time == 1) {
            arriveOnTimeTv.setText(R.string.appoint_15m);
        } else if (time == 2) {
            arriveOnTimeTv.setText(R.string.appoint_30m);
        } else if (time == 3) {
            arriveOnTimeTv.setText(R.string.appoint_1h);
        } else if (time == 4) {
            arriveOnTimeTv.setText(R.string.appoint_2h);
        }

        gunCode = getIntent().getStringExtra("gunCode");
        chargingPileId = getIntent().getIntExtra("chargingPileId",0);
        chargingPileName = getIntent().getStringExtra("chargingPileName");
        runCode = getIntent().getStringExtra("runCode");
        address = getIntent().getStringExtra("address");
        reserveId = getIntent().getStringExtra("reserveId");


        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);

        timerAppointment = new Timer();
        timerAppointment.schedule(new TimerTask() {
            @Override
            public void run() {

                    if (Tools.isNull(PreferencesHelper.getData(Constant.TIME_APPOINTMENT))) {
                        appointmentTime = 0;
                    } else {
                        appointmentTime = Long.parseLong(PreferencesHelper.getData(Constant.TIME_APPOINTMENT));
                    }
                    appointmentTime -= 1000;
                    if (appointmentTime <= 0) {
                        appointmentTime = 0;
                    }
//                        PreferencesHelper.saveData(Constant.TIME_APPOINTMENT,appointmentTime + "");
                    PreferencesHelper.saveData(Constant.TIME_APPOINTMENT, appointmentTime + "");
                    if (appointmentTime <= 0) {
                        //预约超时
                        timerAppointment.cancel();
                        timerAppointment = null;
                        HomeRefreshBean bean = new HomeRefreshBean();
                        bean.type = 1;
                        RxBus.getDefault().send(bean, Constant.HOME_STATUE_REFRESH);
//                        cancelTimerAppointment();
                    }
            }
        }, 1000, 1000);
    }


    @OnClick(R.id.action_go_tv)
    public void actionGo() {
        while (!AppManager.getAppManager().currentActivity().getClass().equals(MainActivity.class)){
            AppManager.getAppManager().finishActivity();
        }
    }

    @OnClick(R.id.action_guild_tv)
    public void actionGuild() {
        while (!AppManager.getAppManager().currentActivity().getClass().equals(MainActivity.class)){
            AppManager.getAppManager().finishActivity();
        }

        HomeAppointmentBean bean = new HomeAppointmentBean();
        bean.chargingAddress = address;
        bean.chargingPileId = chargingPileId;
        bean.chargingPileName = chargingPileName;
        bean.gunCode = gunCode;
        bean.latitude = latitude;
        bean.longitude = longitude;
        bean.runCode = runCode;
        bean.reserveId = Long.parseLong(reserveId);

        Log.e("zw","address : " + address + "  id : " + chargingPileId
                 + " name : " + chargingPileName + " gunCode : " + gunCode + ",lat : " + latitude
                + " ,lon : " + longitude + " ,runCode : " + runCode);

        startActivity(GuildActivity.getLauncher(this,latitude,longitude,bean,false,bean.chargingPileId,"pile"));
        this.finish();

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void goLogin() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timerAppointment != null) {
            timerAppointment.cancel();
            timerAppointment = null;
        }
    }
}
