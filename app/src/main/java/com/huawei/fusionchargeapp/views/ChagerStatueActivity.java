package com.huawei.fusionchargeapp.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.corelibs.base.BaseActivity;
import com.corelibs.common.AppManager;
import com.corelibs.subscriber.RxBusSubscriber;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.ToastMgr;
import com.corelibs.utils.rxbus.RxBus;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.ChargerStatueBean;
import com.huawei.fusionchargeapp.model.beans.HomeChargeOrderBean;
import com.huawei.fusionchargeapp.model.beans.PayInfoBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.ChargeStatuePresenter;
import com.huawei.fusionchargeapp.utils.TimeServiceManager;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.ChargerStatueView;
import com.huawei.fusionchargeapp.weights.CheckChargeFailDialog;
import com.huawei.fusionchargeapp.weights.CheckStatueLoadingView;
import com.huawei.fusionchargeapp.weights.CircleProgressView;
import com.huawei.fusionchargeapp.weights.CommonDialog;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by issuser on 2018/4/20.
 */

public class ChagerStatueActivity extends BaseActivity<ChargerStatueView, ChargeStatuePresenter> implements ChargerStatueView {

    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.tv_charge_time)
    TextView tv_charge_time;
    @Bind(R.id.progress)
    CircleProgressView progressView;
    @Bind(R.id.tv_address_name)
    TextView tv_address_name;
    @Bind(R.id.tv_kw)
    TextView tv_kw;
    @Bind(R.id.tv_current_charger)
    TextView tv_current_charge;
    @Bind(R.id.tv_charged_enegy)
    TextView tv_charged_enegy;
    @Bind(R.id.tv_charged_money)
    TextView tv_charged_money;
    @Bind(R.id.tv_pay)
    TextView tv_pay;

    private Context context = ChagerStatueActivity.this;
    private CommonDialog commonDialog;
    private CheckStatueLoadingView checkStatueLoadingView;
    private CheckStatueLoadingView checkEndStatueLoadingView;
    private CheckChargeFailDialog dialog;
    private TimerService timerService;
    private HomeChargeOrderBean homeChargeOrderBean;
    private Timer timer;
    private Handler handler;

    public static Intent getLauncher(Context context, HomeChargeOrderBean bean) {
        Intent intent = new Intent(context, ChagerStatueActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", bean);
        intent.putExtra("bundle", bundle);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_statue;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setNavTitle(getString(R.string.charging_statue));
        navBar.setImageBackground(R.drawable.nan_bg);

        handler = new Handler();
        commonDialog = new CommonDialog(context, getString(R.string.hint), getString(R.string.charging_statue_hint), 2);
        dialog = new CheckChargeFailDialog(context);
        checkStatueLoadingView = new CheckStatueLoadingView(context, getString(R.string.charging_statue_checking));
        checkEndStatueLoadingView=new CheckStatueLoadingView(context,getString(R.string.charging_statue_ending));
        Bundle bundle = getIntent().getBundleExtra("bundle");
        homeChargeOrderBean = (HomeChargeOrderBean) bundle.getSerializable("data");
        //进度设置100  进度给对应的百分比
        progressView.setMax(100);
//        if(Tools.isNull(PreferencesHelper.getData(Constant.CHARGING_TIME))){
//            PreferencesHelper.saveData(Constant.CHARGING_TIME,"0");
//            tv_charge_time.setText("00:00");
//            progressView.setProgress(0);
//        }else{
//            progressView.setProgress(Long.parseLong(PreferencesHelper.getData(Constant.CHARGING_TIME)));
//            tv_charge_time.setText(Tools.formatHour(Long.parseLong(PreferencesHelper.getData(Constant.CHARGING_TIME))));
//        }


//        timerService.timerHour();
        RxBus.getDefault().toObservable(Object.class, Constant.CHARGING_TIME)
                .compose(this.bindToLifecycle())
                .subscribe(new RxBusSubscriber<Object>() {
                    @Override
                    public void receive(Object data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_charge_time.setText(Tools.formatHour(Long.parseLong(PreferencesHelper.getData(Constant.CHARGING_TIME))));
//                                progressView.setProgress(Long.parseLong(PreferencesHelper.getData(Constant.CHARGING_TIME)));
                            }
                        });

                    }
                });
        if (homeChargeOrderBean != null) {
            presenter.getChargeStatue(homeChargeOrderBean.virtualId, homeChargeOrderBean.chargeGunNum);
        }
        timer = new Timer();

    }

    private void enableEndButton() {
        tv_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_corner_blue));
        tv_pay.setTextColor(getResources().getColor(R.color.white));
        tv_pay.setEnabled(true);
    }

    private void disableEndButton() {
        tv_pay.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_corner_gray));
        tv_pay.setTextColor(getResources().getColor(R.color.black));
        tv_pay.setEnabled(false);
    }

    private long hourTime = 0;

    public void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (Tools.isNull(PreferencesHelper.getData(Constant.CHARGING_TIME))) {
                    hourTime = 0;
                } else {
                    hourTime = Long.parseLong(PreferencesHelper.getData(Constant.CHARGING_TIME));
                }
                hourTime += 1000;
                if (hourTime <= 0) {
                    hourTime = 0;
                }
                PreferencesHelper.saveData(Constant.CHARGING_TIME, hourTime + "");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_charge_time.setText(Tools.formatHour(Long.parseLong(PreferencesHelper.getData(Constant.CHARGING_TIME))));
                    }
                });

            }
        }, 1000, 1000);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        Intent intent = new Intent(this, TimerService.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected ChargeStatuePresenter createPresenter() {
        return new ChargeStatuePresenter();
    }


    @OnClick(R.id.tv_pay)
    public void goPay() {
        commonDialog.show();
        commonDialog.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
                if (Tools.isNull(chargerStatueBean.money) || Double.parseDouble(chargerStatueBean.money) == 0) {
                    //说明充电没上数据（用钱做判断） 不能结束充电
                    ToastMgr.show(getString(R.string.dont_end));
                    return;
                }
                checkEndStatueLoadingView.show();
                presenter.endCharging(chargerStatueBean.chargingPileId, chargerStatueBean.chargingPileName, homeChargeOrderBean.virtualId, homeChargeOrderBean.chargeGunNum, chargerStatueBean.orderRecordNum);
            }
        });
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            TimerService.ServiceBinder binder = (TimerService.ServiceBinder) service;
//            Log.e("yzh", "11111111111");
//            timerService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            Log.e("yzh", "22222222222222");
//            timerService.cancelTimerHour();
//            timerService = null;
        }
    };

    private ChargerStatueBean chargerStatueBean;
    private boolean isFirst=true;

    //-1状态
    @Override
    public void renderChargerStatueData(final ChargerStatueBean bean) {
        chargerStatueBean = bean;
        if (bean.isStart == 0) {
            //检测失败
            final CheckChargeFailDialog dialog = new CheckChargeFailDialog(context);
            dialog.show();
            dialog.setCancelOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog,
                                     int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        finish();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        } else if (bean.isStart == 1 || bean.isStart == -1) {
            if(checkStatueLoadingView.isShowing()){
                checkStatueLoadingView.dismiss();
            }



            //检测中的定时结束
            handler.postDelayed(runnable, 5000);
//            timer.cancel();
//            timer = new Timer();
//            //开始5s刷新一个数据的定时
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    presenter.getChargeStatue(homeChargeOrderBean.virtualId, homeChargeOrderBean.chargeGunNum);
//                }
//            }, 1000, 5000);

            //成功
            tv_address_name.setText(bean.chargingPileName);
            //功率
            tv_kw.setText(bean.power);
            if (!Tools.isNull(bean.money)&&Double.parseDouble(bean.money)!=0) {
                tv_charged_money.setText(bean.money + getString(R.string.yuan));
                enableEndButton();
            }

            tv_current_charge.setText(bean.soc + "%");
            //kwh 当前电量
            tv_charged_enegy.setText(bean.kwh + getString(R.string.du));
            //05:04:25
            if(isFirst){
                if (Tools.isNull(bean.alreadyTime)) {
                    tv_charge_time.setText("00:00:00");
                } else {
                    tv_charge_time.setText(bean.alreadyTime);
                }
                if (Tools.isNull(bean.alreadyTime)) {
                    PreferencesHelper.saveData(Constant.CHARGING_TIME, "0");
                } else {
                    PreferencesHelper.saveData(Constant.CHARGING_TIME, Tools.getTimeValue(bean.alreadyTime) + "");
                }
                startTimer();
                isFirst=false;
            }


//            TimeServiceManager.getInstance().getTimerService().timerHour();
//            if (Tools.isNull(bean.soc)) {
//                progressView.setProgress(0);
//            } else {
//                double d = Double.parseDouble(bean.soc);
//                long l = (long) Math.floor(d);
//                progressView.setProgress(l);
//            }
            progressView.startAnimation(0, 100, 1000);
            if (bean.isStop == 1) {
                Log.e("yzh","isStop");
//                timerService.cancelTimerHour();
                if(checkEndStatueLoadingView.isShowing()){
                    checkEndStatueLoadingView.dismiss();
                }
                stopTimer();
                progressView.stopAnimator();
                RxBus.getDefault().send(new Object(), Constant.REFRESH_HOME_STATUE);
//                TimeServiceManager.getInstance().getTimerService().cancelTimerHour();
                handler.removeCallbacks(runnable);
//                checkStatueLoadingView.dismiss();
                //弹框提示充电结束去支付
                final CommonDialog dialog = new CommonDialog(context, getString(R.string.hint), getString(R.string.charging_statue_end_go_to_pay), 2);
                dialog.show();
                dialog.setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        while (!AppManager.getAppManager().currentActivity().getClass().equals(MainActivity.class)) {
                            AppManager.getAppManager().finishActivity();
                        }
                        PayInfoBean payInfoBean = new PayInfoBean();
                        if (Tools.isNull(bean.serviceFee)) {
                            payInfoBean.serviceCharge = 0;
                        } else {
                            payInfoBean.serviceCharge = Double.parseDouble(bean.serviceFee);
                        }
                        if (Tools.isNull(bean.money)) {
                            payInfoBean.eneryCharge = 0;
                        } else {
                            payInfoBean.eneryCharge = Double.parseDouble(bean.money);
                        }
                        if (Tools.isNull(bean.kwh)) {
                            payInfoBean.chargePowerAmount = 0;
                        } else {
                            payInfoBean.chargePowerAmount = Double.parseDouble(bean.kwh);
                        }
                        payInfoBean.consumeTotalMoney = payInfoBean.serviceCharge + payInfoBean.eneryCharge;

                        startActivity(PayActivity.getLauncher(context, chargerStatueBean.orderRecordNum, "0"));
                        finish();
                    }
                });

                dialog.setNagitiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        while (!AppManager.getAppManager().currentActivity().getClass().equals(MainActivity.class)) {
                            AppManager.getAppManager().finishActivity();
                        }
                    }
                });
            }

        } else if (bean.isStart == 2) {
            //检测中
            checkStatueLoadingView.show();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    presenter.getChargeStatue(homeChargeOrderBean.virtualId, homeChargeOrderBean.chargeGunNum);
//                }
//            }, 1000, 3000);
            handler.postDelayed(runnable, 3000);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (homeChargeOrderBean != null) {
                presenter.getChargeStatue(homeChargeOrderBean.virtualId, homeChargeOrderBean.chargeGunNum);
            }

        }
    };

    @Override
    public void endChargeSuccess() {
//        checkStatueLoadingView.dismiss();
        //调了结束充电的接口之后 继续获取充电状态接口
        //检测中的定时结束

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 5000);
//        timerService.cancelTimerHour();
//        TimeServiceManager.getInstance().getTimerService().cancelTimerHour();
//        //刷新首页的界面
//        RxBus.getDefault().send(new Object(),Constant.REFRESH_HOME_STATUE);
//        commonDialog.setMsg(getString(R.string.charging_statue_end_go_to_pay));
//        commonDialog.show();
//        commonDialog.setPositiveListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                                orderNum 是在传参里面有
////                                presenter.endCharging();
//            startActivity(PayActivity.getLauncher(context,chargerStatueBean.orderRecordNum));
//            commonDialog.dismiss();
//            finish();
//            }
//        });
//        commonDialog.setNagitiveListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                commonDialog.dismiss();
//                checkStatueLoadingView.dismiss();
//                while (!AppManager.getAppManager().currentActivity().getClass().equals(MainActivity.class)) {
//                    AppManager.getAppManager().finishActivity();
//                }
//            }
//        });

    }

    @Override
    public void endChargeFail() {
        ToastMgr.show(getString(R.string.charging_end_fail));
        checkEndStatueLoadingView.dismiss();
        //调了结束充电的接口之后 继续获取充电状态接口
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
        handler.removeCallbacks(runnable);
        handler = null;
        //刷新首页的界面
        RxBus.getDefault().send(new Object(), Constant.REFRESH_HOME_STATUE);
        if (progressView != null) {
            progressView.stopAnimator();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void goLogin() {
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(context));
    }

}
