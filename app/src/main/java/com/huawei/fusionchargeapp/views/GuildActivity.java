package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.corelibs.base.BaseActivity;
import com.corelibs.subscriber.RxBusSubscriber;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.ToastMgr;
import com.corelibs.utils.rxbus.RxBus;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.HomeAppointmentBean;
import com.huawei.fusionchargeapp.model.beans.HomeRefreshBean;
import com.huawei.fusionchargeapp.model.beans.MyLocationBean;
import com.huawei.fusionchargeapp.model.beans.RequestChargePileDetailBean;
import com.huawei.fusionchargeapp.presenter.GuaildPresenter;
import com.huawei.fusionchargeapp.utils.DrivingRouteOverLay;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.GuaildView;
import com.huawei.fusionchargeapp.weights.AppointmentTimeOutDialog;
import com.huawei.fusionchargeapp.weights.CommonDialog;
import com.huawei.fusionchargeapp.weights.NavBar;
import com.huawei.fusionchargeapp.weights.NearTargetDialog;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by issuser on 2018/4/23.
 */

public class GuildActivity extends BaseActivity<GuaildView, GuaildPresenter> implements GuaildView, RouteSearch.OnRouteSearchListener {

    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.map)
    MapView map;
    @Bind(R.id.ll_appointment)
    LinearLayout ll_appointment;
    @Bind(R.id.tv_appointment_address)
    TextView tv_appointment_address;
    @Bind(R.id.tv_appointment_time)
    TextView tv_appointment_time;
    @Bind(R.id.iv_hint)
    ImageView iv_hint;
    @Bind(R.id.ll_hint)
    LinearLayout ll_hint;
    @Bind(R.id.tv_pile_num)
    TextView tv_pile_num;
    @Bind(R.id.tv_pile_name)
    TextView tv_pile_name;
    @Bind(R.id.tv_gun_num)
    TextView tv_gun_num;
    @Bind(R.id.ll_time)
    LinearLayout ll_time;

    private AMap aMap;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private final int ROUTE_TYPE_DRIVE = 2;

    private LatLonPoint mStartPoint = null;//起点，116.335891,39.942295
    private LatLonPoint mEndPoint = null;//终点，116.481288,39.995576
    private Context context = GuildActivity.this;
    private DrivingRouteOverLay drivingRouteOverlay;
    private double endLatitude, endLongitude;
    private long appointmentTime;
    private HomeAppointmentBean homeAppointmentBean;
    private CommonDialog commonDialog;
    private boolean choiceNotAppointment;
    private AppointmentTimeOutDialog appointmentTimeOutDialog;
    private Timer timerAppointment;
    //桩，站id
    private long id;
    //桩 ，站类型
    private String type;
    private NearTargetDialog nearTargetDialog;

    public static Intent getLauncher(Context context, double latitude, double longitude, HomeAppointmentBean bean, boolean choiceNotAppoinment, long id, String type) {
        Intent intent = new Intent(context, GuildActivity.class);
        intent.putExtra("la", latitude);
        intent.putExtra("ln", longitude);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtra("bundle", bundle);
        intent.putExtra("choice", choiceNotAppoinment);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guaild;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

//        AMapLocationClient mlocationClient = new AMapLocationClient(context);
//        //初始化定位参数
//        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
//        //设置单次定位
//        mLocationOption.setOnceLocation(false);
//        mlocationClient.setLocationOption(mLocationOption);
//        mlocationClient.startLocation();

        navBar.setNavTitle(getString(R.string.guilding));
        navBar.setImageBackground(R.drawable.nan_bg);


        nearTargetDialog = new NearTargetDialog(context);

        timerAppointment = new Timer();
        commonDialog = new CommonDialog(context, "", 2);

        id = getIntent().getLongExtra("id", 0);
        type = getIntent().getStringExtra("type");
        endLatitude = getIntent().getDoubleExtra("la", 0);
        endLongitude = getIntent().getDoubleExtra("ln", 0);
        homeAppointmentBean = (HomeAppointmentBean) getIntent().getBundleExtra("bundle").getSerializable("bean");
        choiceNotAppointment = getIntent().getBooleanExtra("choice", false);

        appointmentTimeOutDialog = new AppointmentTimeOutDialog(context);

        if (choiceNotAppointment) {
            //提示是否继续导航
            commonDialog = new CommonDialog(context, "", 2);
            commonDialog.show();
            commonDialog.setMsg(getString(R.string.guilding_check_not_appointment));
            commonDialog.setNagitiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonDialog.dismiss();
                    finish();
                }
            });
        } else {

        }

        if (homeAppointmentBean != null) {
            navBar.showCancelAppointment(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonDialog = new CommonDialog(context, "", 2);
                    commonDialog.show();
                    commonDialog.setDialogBackground();
                    commonDialog.setMsg(getString(R.string.guilding_cancel_appointment_hint));
                    commonDialog.setPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commonDialog.dismiss();
                            //调用接口
                            presenter.cancelAppointment(homeAppointmentBean.reserveId + "", UserHelper.getSavedUser().appUserId + "", homeAppointmentBean.gunCode, homeAppointmentBean.chargingPileId + "");
                        }
                    });
                }
            });
            ll_appointment.setVisibility(View.VISIBLE);

            tv_pile_num.setText(homeAppointmentBean.runCode);
            tv_pile_name.setText(homeAppointmentBean.chargingPileName);
            tv_gun_num.setText(homeAppointmentBean.gunCode);
            tv_appointment_address.setText(homeAppointmentBean.chargingAddress);
            tv_appointment_time.setText(Tools.formatMinute(Long.parseLong(PreferencesHelper.getData(Constant.TIME_APPOINTMENT))));
            //设置时间开始计时
//            Intent intent = new Intent(this, TimerService.class);
//            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            timerAppointment.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (homeAppointmentBean != null) {
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_appointment_time.setText(Tools.formatMinute(Long.parseLong(PreferencesHelper.getData(Constant.TIME_APPOINTMENT))));
                            }
                        });
                        if (appointmentTime <= 0) {
                            //预约超时
                            //计时器里面操作 RunOnUiThread 注意is your activity running?
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    appointmentTimeOutDialog.show();
                                    appointmentTimeOutDialog.setIvDeleteListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            appointmentTimeOutDialog.dismiss();
                                            finish();
                                        }
                                    });
                                    appointmentTimeOutDialog.setReAppointment(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            appointmentTimeOutDialog.dismiss();
                                            finish();
                                            startActivity(new Intent(GuildActivity.this,AppointmentChargeActivity.class));
                                        }
                                    });
                                }
                            });
                            timerAppointment.cancel();
                            timerAppointment = null;
                            HomeRefreshBean bean = new HomeRefreshBean();
                            bean.type = 1;
                            RxBus.getDefault().send(bean, Constant.HOME_STATUE_REFRESH);
//                        cancelTimerAppointment();
                        }
                    }
                }
            }, 1000, 1000);
        } else {

        }

        map.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = map.getMap();
            UiSettings uiSettings = aMap.getUiSettings();
            // 去掉缩放按钮==
            uiSettings.setZoomControlsEnabled(false);
            uiSettings.setRotateGesturesEnabled(false);
        }
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);

//        aMap.addMarker(new MarkerOptions()
//                .position(Tools.convertToLatLng(mStartPoint)));
////                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location)));
//        aMap.addMarker(new MarkerOptions()
//                .position(Tools.convertToLatLng(mEndPoint)));
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.home_ic_position)));


        RxBus.getDefault().toObservable(MyLocationBean.class, Constant.REFRESH_LOCATION)
                .compose(this.<MyLocationBean>bindToLifecycle())
                .subscribe(new RxBusSubscriber<MyLocationBean>() {

                    @Override
                    public void receive(MyLocationBean data) {
                        //刷新坐标 位置不一致时才更新路线
                        if (mStartPoint != null) {
                            if (mStartPoint.getLatitude() != data.latitude || mStartPoint.getLongitude() != data.longtitude) {
                                mStartPoint = new LatLonPoint(data.latitude, data.longtitude);
                                searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
                            }
                        }

                    }
                });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });

        RxBus.getDefault().toObservable(Object.class, Constant.APPOINTMENT_TIME_OUT)
                .compose(this.bindToLifecycle())
                .subscribe(new RxBusSubscriber<Object>() {
                    @Override
                    public void receive(Object data) {
                        appointmentTimeOutDialog.show();
                        appointmentTimeOutDialog.setIvDeleteListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appointmentTimeOutDialog.dismiss();
                                finish();
                            }
                        });
                        appointmentTimeOutDialog.setReAppointment(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appointmentTimeOutDialog.dismiss();
                                finish();
                            }
                        });
                    }
                });
        final RequestChargePileDetailBean bean = new RequestChargePileDetailBean();
        bean.setId(id + "");
        bean.setType(type);
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                if (!Tools.isNull(type)) {
                    if (type.equals("station")) {
                        presenter.getStationDetail(bean);
                    } else {
                        presenter.getPileDetail(bean);
                    }
                }

            }
        });

    }

    private long zoneId;
    private int zoneGisOpen;

    @Override
    public void getZoneInfo(long id, int isGisOpen) {
        //先获取园区信息再路径规划
        zoneId = id;
        zoneGisOpen = isGisOpen;
        MyLocationBean locationBean = PreferencesHelper.getData(MyLocationBean.class);
        if (locationBean != null) {
            mStartPoint = new LatLonPoint(locationBean.latitude, locationBean.longtitude);
        }
        if (mStartPoint == null) {
            mStartPoint = new LatLonPoint(39.9088600000, 116.3973900000);
        }
        mEndPoint = new LatLonPoint(endLatitude, endLongitude);
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
        aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mStartPoint.getLatitude(), mEndPoint.getLongitude())));
    }

    @OnClick(R.id.ll_time)
    public void hideOrShowInfo() {
        if (ll_hint.getVisibility() == View.VISIBLE) {
            ll_hint.setVisibility(View.GONE);
        } else {
            ll_hint.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastMgr.show(getString(R.string.guilding_get_location));
            return;
        }
        if (mEndPoint == null) {
            ToastMgr.show(getString(R.string.guilding_no_end));
            return;
        }
//        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery
                    query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }
    }


    @Override
    protected GuaildPresenter createPresenter() {
        return new GuaildPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();

        if (timerAppointment != null) {
            timerAppointment.cancel();
            timerAppointment = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    mDriveRouteResult = driveRouteResult;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverLay drivingRouteOverlay = new DrivingRouteOverLay(
                            context, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    Log.e("yzh", "dur" + "---" + dur + "---" + dis);


                    if (dis < 200) {
                        if (zoneGisOpen == 0) {
                            //没有gis地图
                        } else {
                            //提示是否切换地图
                            View dView = getWindow().getDecorView();
                            dView.setDrawingCacheEnabled(true);
                            dView.buildDrawingCache();
                            Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
                            if (!nearTargetDialog.isShowing()) {
                                nearTargetDialog.show();
                                nearTargetDialog.setImageContent(bitmap);
                                nearTargetDialog.setChangerListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        nearTargetDialog.dismiss();
                                        startActivity(ParkActivity.getLauncher(context, zoneId,endLatitude,endLongitude));
                                        finish();
                                    }
                                });
                            }

                        }
                    }
                } else if (driveRouteResult != null && driveRouteResult.getPaths() == null) {
//                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
//                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void cancelAppointmentSuccess() {
        //取消成功
        timerAppointment.cancel();
        timerAppointment = null;
//        TimeServiceManager.getInstance().getTimerService().cancelTimerAppointment();
        HomeRefreshBean bean = new HomeRefreshBean();
        bean.type = 1;
        RxBus.getDefault().send(bean, Constant.HOME_STATUE_REFRESH);

        finish();
    }

    private TimerService timerService;


    @Override
    public void goLogin() {

    }
}
