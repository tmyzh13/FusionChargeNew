package com.huawei.fusionchargeapp.views.home;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.corelibs.base.BaseFragment;
import com.corelibs.common.AppManager;
import com.corelibs.subscriber.RxBusSubscriber;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.ToastMgr;
import com.corelibs.utils.rxbus.RxBus;
import com.google.zxing.integration.android.IntentIntegrator;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.AppointTimeOutBean;
import com.huawei.fusionchargeapp.model.beans.ChargeFeeBean;
import com.huawei.fusionchargeapp.model.beans.HomeAppointmentBean;
import com.huawei.fusionchargeapp.model.beans.HomeChargeOrderBean;
import com.huawei.fusionchargeapp.model.beans.HomeOrderBean;
import com.huawei.fusionchargeapp.model.beans.HomeRefreshBean;
import com.huawei.fusionchargeapp.model.beans.MapDataBean;
import com.huawei.fusionchargeapp.model.beans.MapInfoBean;
import com.huawei.fusionchargeapp.model.beans.MyLocationBean;
import com.huawei.fusionchargeapp.model.beans.PileFeeBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.MapPresenter;
import com.huawei.fusionchargeapp.utils.ActionControl;
import com.huawei.fusionchargeapp.utils.ChoiceManager;
import com.huawei.fusionchargeapp.utils.TimeServiceManager;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.AppointmentChargeActivity;
import com.huawei.fusionchargeapp.views.ChagerStatueActivity;
import com.huawei.fusionchargeapp.views.ChargeCaptureActivity;
import com.huawei.fusionchargeapp.views.ChargeDetails2Activity;
import com.huawei.fusionchargeapp.views.GuildActivity;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.PayActivity;
import com.huawei.fusionchargeapp.views.TimerService;
import com.huawei.fusionchargeapp.views.interfaces.MapHomeView;
import com.huawei.fusionchargeapp.weights.AppointmentTimeOutDialog;
import com.huawei.fusionchargeapp.weights.ChargeFeeDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.http.Body;

/**
 * Created by issuser on 2018/4/19.
 */

public class MapFragment extends BaseFragment<MapHomeView, MapPresenter> implements MapHomeView, LocationSource, AMapLocationListener, AMap.OnMapTouchListener {

    @Bind(R.id.map)
    MapView map;
    @Bind(R.id.rl_not_pay)
    RelativeLayout rl_not_pay;
    @Bind(R.id.rl_bottom_detail)
    RelativeLayout rl_bottom_detail;
    @Bind(R.id.ll_fee_all)
    LinearLayout ll_fee_all;
    @Bind(R.id.tv_map_info_name)
    TextView tv_map_info_name;
    @Bind(R.id.tv_map_info_score)
    TextView tv_map_info_score;
    @Bind(R.id.score_unit)
    TextView scoreUnit;
    @Bind(R.id.tv_map_info_address)
    TextView tv_map_info_address;
    @Bind(R.id.tv_map_info_distance)
    TextView tv_map_info_distance;
    @Bind(R.id.tv_map_info_pile_num)
    TextView tv_map_info_pile_num;
    @Bind(R.id.tv_map_info_gun_num)
    TextView tv_map_info_gun_num;
    @Bind(R.id.tv_map_info_free_num)
    TextView tv_map_info_free_num;
    @Bind(R.id.tv_map_info_current_fee)
    TextView tv_map_info_current_fee;
    @Bind(R.id.tv_map_info_service_fee)
    TextView tv_map_info_service_fee;
    @Bind(R.id.ll_appointment)
    LinearLayout ll_appontment;
    @Bind(R.id.rl_charger_order)
    RelativeLayout rl_charger_order;
    @Bind(R.id.tv_appointment_address)
    TextView tv_appointment_address;
    @Bind(R.id.ll_appointment_guaild)
    LinearLayout ll_appointment_guaild;
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
    //预约超时提示 和计时器
    private AppointmentTimeOutDialog appointmentTimeOutDialog;
    private Timer timerAppointment;
    private long appointmentTime;
    //对应充电桩的费率信息
    private List<ChargeFeeBean> list_fee;
    //首页地图桩站信息
    private List<MapDataBean> list;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        map.onCreate(savedInstanceState);
        aMap = map.getMap();

        UiSettings uiSettings = aMap.getUiSettings();
        // 去掉缩放按钮==
        uiSettings.setZoomControlsEnabled(false);
        //去掉地图手势
        uiSettings.setRotateGesturesEnabled(false);
//        uiSettings.setMyLocationButtonEnabled(true);
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                rl_bottom_detail.setVisibility(View.GONE);
            }
        });

        timerAppointment = new Timer();
//        Intent intent = new Intent(getContext(), TimerService.class);
//        getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        location();
        initMapData();
        initRxBus();

        //控制预约信息的显示隐藏
        ll_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_hint.getVisibility() == View.VISIBLE) {
                    ll_hint.setVisibility(View.GONE);
                    tv_appointment_address.setText(getString(R.string.home_appointment_hint));
                } else {
                    ll_hint.setVisibility(View.VISIBLE);
                    if (homeAppointmentBean != null) {
                        tv_appointment_address.setText(homeAppointmentBean.chargingAddress);
                    }

                }
            }
        });
        appointmentTimeOutDialog = new AppointmentTimeOutDialog(getContext());

        if (UserHelper.getSavedUser() != null && !Tools.isNull(UserHelper.getSavedUser().token)) {
            getHomeStatue();
        }

        timerAppointment.schedule(new TimerTask() {
            @Override
            public void run() {

                if (homeAppointmentBean != null) {

                    if (!AppManager.getAppManager().currentActivity().getClass().equals(MainActivity.class)) {
                        return;
                    }

                    if(appointmentTime==0){
                        if (Tools.isNull(PreferencesHelper.getData(Constant.TIME_APPOINTMENT))) {
                            appointmentTime = 0;
                        } else {
                            appointmentTime = Long.parseLong(PreferencesHelper.getData(Constant.TIME_APPOINTMENT));
                        }
                    }
                    appointmentTime -= 1000;
                    if (appointmentTime <= 0) {
                        appointmentTime = 0;
                    }
                    //优化计时器操作 6-13
//                    PreferencesHelper.saveData(Constant.TIME_APPOINTMENT, appointmentTime + "");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_appointment_time.setText(Tools.formatMinute(appointmentTime));
                        }
                    });
                    if (appointmentTime <= 0) {
                        //预约超时
                        if (AppManager.getAppManager().currentActivity().getClass().equals(MainActivity.class)) {
                            Log.e("yzh", (homeAppointmentBean == null) + "--");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //结束时间小于=当前时间
                                    if (homeAppointmentBean != null) {
                                        ActionControl.getInstance(getContext()).setHasAppointment(false,null);
                                        if (!appointmentTimeOutDialog.isShowing()) {
                                            appointmentTimeOutDialog.show();
                                        }
                                        appointmentTimeOutDialog.setIvDeleteListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                appointmentTimeOutDialog.dismiss();
                                            }
                                        });
                                        appointmentTimeOutDialog.setReAppointment(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                appointmentTimeOutDialog.dismiss();
                                                startActivity(new Intent(getActivity(), AppointmentChargeActivity.class));
                                            }
                                        });
                                        homeAppointmentBean = null;
                                    }
//                                    }
                                }
                            });
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ll_appontment.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
        }, 1000, 1000);
    }



    //初始化通知接收
    private void initRxBus() {
        RxBus.getDefault().toObservable(HomeRefreshBean.class, Constant.HOME_STATUE_REFRESH)
                .compose(this.<HomeRefreshBean>bindToLifecycle())
                .subscribe(new RxBusSubscriber<HomeRefreshBean>() {

                    @Override
                    public void receive(HomeRefreshBean data) {
                        //支付成功了 屏蔽未支付提示
                        if (data.type == 0) {
                            rl_not_pay.setVisibility(View.GONE);
                            ActionControl.getInstance(getViewContext()).setHasNoPayOrder(false, null);
                        } else if (data.type == 1) {
                            //预约结束 屏蔽预约提示
                            homeAppointmentBean = null;
                            ActionControl.getInstance(getViewContext()).setHasAppointment(false, null);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ll_appontment.setVisibility(View.GONE);
                                    if (appointmentTimeOutDialog.isShowing()) {
                                        appointmentTimeOutDialog.dismiss();
                                    }
                                }
                            });

                        }

                    }
                });
        //刷新界面地图或者列表站点数据
        RxBus.getDefault().toObservable(Object.class, Constant.REFRESH_MAP_OR_LIST_DATA)
                .compose(this.<Object>bindToLifecycle())
                .subscribe(new RxBusSubscriber<Object>() {

                    @Override
                    public void receive(Object data) {
                        if (ChoiceManager.getInstance().getType() == 0) {
                            presenter.getDataType0();
                        } else {
                            presenter.getData();
                        }

                    }
                });
        //刷新首页获取未支付订单，充电中订单， 预约中订单
        RxBus.getDefault().toObservable(Object.class, Constant.REFRESH_HOME_STATUE)
                .compose(this.bindToLifecycle())
                .subscribe(new RxBusSubscriber<Object>() {

                    @Override
                    public void receive(Object data) {
                        if (UserHelper.getSavedUser() != null && !Tools.isNull(UserHelper.getSavedUser().token)) {
                            getHomeStatue();
                        }
                    }
                });

        //从设置返回，让预约中的订单隐藏
        RxBus.getDefault().toObservable(Object.class, Constant.LOGIN_OUT_SET_APPOINT_VIEW_GONE)
                .compose(this.bindToLifecycle())
                .subscribe(new RxBusSubscriber<Object>() {
                    @Override
                    public void receive(Object data) {
                        homeAppointmentBean = null;
                        homeOrderBean = null;
                        homeChargeOrderBean = null;
                        ll_appontment.setVisibility(View.GONE);
                        rl_charger_order.setVisibility(View.GONE);
                        rl_not_pay.setVisibility(View.GONE);
                    }
                });
        RxBus.getDefault().toObservable(Boolean.class,Constant.START_OR_STOP_LOCATION)
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new RxBusSubscriber<Boolean>() {
                    @Override
                    public void receive(Boolean data) {
                        controlLocation(data);
                    }
                });
    }

    //获取未支付 充电  预约情况
    private void getHomeStatue() {
        Log.e("zw", "getHomeStatue");
        presenter.getUserOrderStatue();
        presenter.getUserChargeStatue();
        presenter.getUserAppointment();
    }

    //获取地图数据
    private void initMapData() {
        if (ChoiceManager.getInstance().getType() == 0) {
            presenter.getDataType0();
        } else {
            presenter.getData();
        }

    }


    OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;

    private MapDataBean currentMapDataBean;

    private void location() {
        //控制手势
//        aMap.getUiSettings().setRotateGesturesEnabled(false);
//        aMap.getUiSettings().setTiltGesturesEnabled(false);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.mipmap.location);
        BitmapDescriptor markerIcon = BitmapDescriptorFactory
                .fromView(imageView);
        myLocationStyle.myLocationIcon(markerIcon);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
        //让定位点周围的圈显示不出来
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);
        //移动时 不让地图到定位点
        aMap.setOnMapTouchListener(this);
        // 定义 Marker 点击事件监听
        AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                MapDataBean bean = list.get(Integer.parseInt(marker.getTitle()));
                currentMapDataBean = bean;
                presenter.getInfo(bean.id, bean.type);
                return true;
            }
        };
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(markerClickListener);
    }

    @Override
    protected MapPresenter createPresenter() {
        return new MapPresenter();
    }

    @OnClick(R.id.ll_fee)
    public void showFee() {
        ChargeFeeDialog dialog = new ChargeFeeDialog(getContext());
        dialog.show();
        if (list_fee != null && list_fee.size() != 0) {
            dialog.setFeeDatas(list_fee);
        }
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 10:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    if (ChoiceManager.getInstance().getType() == 0) {
                        presenter.getDataType0();
                    } else {
                        presenter.getData();
                    }
                } else {
                    // 没有获取到权限，做特殊处理
                    ToastMgr.show(getString(R.string.get_location_permission_fail));
                }
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.tv_pay)
    public void goPay() {
        startActivity(PayActivity.getLauncher(getContext(), homeOrderBean.orderRecordNum, "0"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != map) {
            map.onDestroy();
        }

        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        if (null != timerAppointment) {
            timerAppointment.cancel();
            timerAppointment = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();

        //动态请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), getString(R.string.no_permission_location), Toast.LENGTH_SHORT).show();
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 10);
            }
        }
        if (UserHelper.getSavedUser() != null) {
            presenter.getUserAppointment();
            presenter.getUserScore();
        }

        //优化地图功耗 6-14
       controlLocation(true);
    }

    private void controlLocation(boolean b){
        if(mlocationClient!=null){
            if(b){
                //设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //设置定位参数
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.startLocation();

            }else{
                //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.stopLocation();

            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
        //优化计时器操作 6-13
        PreferencesHelper.saveData(Constant.TIME_APPOINTMENT, appointmentTime + "");
        //优化地图功耗 6-14
       controlLocation(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(getContext());
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            mLocationOption.setInterval(10000);//设置间隔时间
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    private boolean followMove = true;
    private boolean isReportUserLocation = false;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {

                Log.e("yzh","-----------");
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                MyLocationBean bean = new MyLocationBean();
                bean.latitude = aMapLocation.getLatitude();
                bean.longtitude = aMapLocation.getLongitude();
                bean.province=aMapLocation.getProvince();
                bean.city=aMapLocation.getCity();
                //设置区
                bean.county=aMapLocation.getDistrict();

                //todo
//                bean.latitude = 22.6552090201;
//                bean.longtitude = 114.0643603795;
                PreferencesHelper.saveData(bean);
                RxBus.getDefault().send(bean, Constant.REFRESH_LOCATION);
                if (followMove) {
                    aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                }

                //在登陆的情况下上报一次用户坐标信息
                if (UserHelper.getSavedUser() != null) {
                    //登陆过了
                    if (!isReportUserLocation) {
                        //还没上报
                        presenter.reportUserLocation(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    }
                }

            } else {
                String errText = "location_fail," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("yzh", errText);
            }
        }
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        if (followMove) {
            //用户拖动地图后，不再跟随移动，直到用户点击定位按钮
            followMove = false;
        }
    }

    @OnClick(R.id.ll_go_to_detail)
    public void goDetail() {
        //进入对应的充电桩详情

    }

    private List<Marker> listMark = new ArrayList<>();

    @Override
    public void renderMapData(List<MapDataBean> list) {
        this.list = list;

        MyLocationBean bean = PreferencesHelper.getData(MyLocationBean.class);
        clearMark();
        listMark = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //距离筛选
            if (bean != null) {
                if (Tools.GetDistance(list.get(i).latitude, list.get(i).longitude, bean.latitude, bean.longtitude) > ChoiceManager.getInstance().getDistance()) {
                    continue;
                }
            }
            MarkerOptions markerOption = new MarkerOptions();
            LatLng latLng = new LatLng(list.get(i).latitude, list.get(i).longitude);
            markerOption.position(latLng);
            //点标记的标题和内容；
            markerOption.title(i + "");
            markerOption.draggable(true);//设置Marker可拖动
            if (list.get(i).type.equals("pile")) {
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.home_ic_position)));
            } else {
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(Tools.getNumberBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.home_ic_position1).copy(Bitmap.Config.ARGB_8888, true), list.get(i).pileNum + "")));

            }
//            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                    .decodeResource(getResources(), R.mipmap.home_ic_position)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(false);//设置marker平贴地图效果
            Marker marker = aMap.addMarker(markerOption);
            listMark.add(marker);
        }
    }

    private void clearMark() {
        for (int i = 0; i < listMark.size(); i++) {
            if (listMark.get(i) != null) {
                listMark.get(i).remove();
            }
        }
    }

    @Override
    public void getMarkInfo(long id, MapInfoBean mapInfoBean) {
        if (mapInfoBean == null) {
            ToastMgr.show(getString(R.string.no_data));
            return;
        }
        //充电桩获取费率数据
        if (mapInfoBean.objType.equals("pile")) {
            presenter.getFeeInfo(id);
        }
        rl_bottom_detail.setVisibility(View.VISIBLE);
        if (mapInfoBean.objType.equals("station")) {
            //充电站
            ll_fee_all.setVisibility(View.GONE);
        } else if (mapInfoBean.objType.equals("pile")) {
            //充电桩
            ll_fee_all.setVisibility(View.VISIBLE);
        }
        tv_map_info_name.setText(mapInfoBean.name);
        // 加粗
        TextPaint paintName = tv_map_info_name.getPaint();
        paintName.setFakeBoldText(true);
        tv_map_info_address.setText(mapInfoBean.address);
        // 加粗
        TextPaint paintAddress = tv_map_info_address.getPaint();
        paintAddress.setFakeBoldText(true);
        if (mapInfoBean.averageScore.equals("-1.0")) {
            tv_map_info_score.setText("");
            scoreUnit.setText(getString(R.string.no_score));
        } else {
            tv_map_info_score.setText(mapInfoBean.averageScore);
            scoreUnit.setText(R.string.score);
        }
        //要单算距离
        MyLocationBean bean = PreferencesHelper.getData(MyLocationBean.class);
        if (bean != null) {
            tv_map_info_distance.setText(Tools.GetDistance(currentMapDataBean.latitude, currentMapDataBean.longitude, bean.latitude, bean.longtitude) + "KM");
        }
        tv_map_info_pile_num.setText(mapInfoBean.pileNum + "");
        tv_map_info_gun_num.setText(mapInfoBean.gunNum + "");
        tv_map_info_free_num.setText(mapInfoBean.freeNum + "");
    }

    @Override
    public void showPileFeeInfo(PileFeeBean bean) {
        list_fee = bean.feeList;
        tv_map_info_service_fee.setText(bean.serviceFee + getString(R.string.yuan_du));
        if (list_fee != null && list_fee.size() != 0) {
            double min = list_fee.get(0).multiFee;
            double max = list_fee.get(0).multiFee;
            for (int i = 1; i < list_fee.size(); i++) {
                if (list_fee.get(i).multiFee > max) {
                    max = list_fee.get(i).multiFee;
                }
                if (list_fee.get(i).multiFee < min) {
                    min = list_fee.get(i).multiFee;
                }
            }
            tv_map_info_current_fee.setText(min + getString(R.string.yuan_du) + "~" + max + getString(R.string.yuan_du));
        } else {
            tv_map_info_current_fee.setText("0.0" + getString(R.string.yuan_du) + "~" + "0.0" + getString(R.string.yuan_du));
        }

    }

    private HomeOrderBean homeOrderBean;

    @Override
    public void hasNoPayOrder(boolean has, HomeOrderBean bean) {
        ActionControl.getInstance(getContext()).setHasNoPayOrder(has, bean);
        if (has) {
            rl_not_pay.setVisibility(View.VISIBLE);
            homeOrderBean = bean;
        } else {
            rl_not_pay.setVisibility(View.GONE);
        }
    }

    private HomeAppointmentBean homeAppointmentBean;

    @Override
    public void renderAppoinmentInfo(boolean has, HomeAppointmentBean bean) {
        ActionControl.getInstance(getContext()).setHasAppointment(has, bean);
        homeAppointmentBean = bean;
        if (has) {
            tv_pile_num.setText(bean.runCode);
            tv_pile_name.setText(bean.chargingPileName);
            tv_gun_num.setText(bean.gunCode);
            tv_appointment_address.setText(getString(R.string.home_appointment_hint));

            if (bean.reserveEndTime <= bean.nowTime) {
                ll_appontment.setVisibility(View.GONE);
                homeAppointmentBean = null;
                ActionControl.getInstance(getContext()).setHasAppointment(false, null);
            } else {
                ll_appontment.setVisibility(View.VISIBLE);
                long surplusTime = bean.reserveEndTime - bean.nowTime;
                //优化计时器操作 6-13
                appointmentTime=surplusTime;
//                PreferencesHelper.saveData(Constant.TIME_APPOINTMENT, surplusTime + "");
                PreferencesHelper.saveData(Constant.APPOINTMENT_DURING, bean.reserveDuration);
                tv_appointment_time.setText(Tools.formatMinute(surplusTime));
            }
//            ll_appontment.setVisibility(View.VISIBLE);
            //防止从首页超时到再次预约时 预约桩数据丢失
            AppointTimeOutBean appointTimeOutBean = new AppointTimeOutBean();
            appointTimeOutBean.setGunCode(bean.runCode);
            appointTimeOutBean.setAddress(bean.chargingAddress);
            appointTimeOutBean.setChargingPileName(bean.chargingPileName);
            appointTimeOutBean.setChargingPileId(Integer.parseInt(bean.chargingPileId+""));
            appointTimeOutBean.setLatitude(bean.latitude);
            appointTimeOutBean.setLongitude(bean.longitude);
            appointTimeOutBean.setRunCode(bean.runCode);
            PreferencesHelper.saveData(appointTimeOutBean);
        } else {
            ll_appontment.setVisibility(View.GONE);
        }

    }

    private HomeChargeOrderBean homeChargeOrderBean;

    @Override
    public void renderHomeChargerOrder(boolean has, HomeChargeOrderBean bean) {
        ActionControl.getInstance(getContext()).setHasCharging(has, bean);
        if (has) {
            rl_charger_order.setVisibility(View.VISIBLE);
            homeChargeOrderBean = bean;
//            TimeServiceManager.getInstance().getTimerService().timerHour();
        } else {
            rl_charger_order.setVisibility(View.GONE);
        }

    }

    @Override
    public void reportUserLocationSuccess() {
        isReportUserLocation = true;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimerService.ServiceBinder binder = (TimerService.ServiceBinder) service;
            TimeServiceManager.getInstance().setTimerService(binder.getService());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            TimeServiceManager.getInstance().getTimerService().cancelTimerHour();
            TimeServiceManager.getInstance().setTimerService(null);
        }
    };

    @OnClick(R.id.ll_appointment_guaild)
    public void goAppointmentGuaild() {
        startActivity(GuildActivity.getLauncher(getContext(), homeAppointmentBean.latitude, homeAppointmentBean.longitude, homeAppointmentBean, false, homeAppointmentBean.chargingPileId, "pile"));
    }

    @OnClick(R.id.iv_guaild)
    public void goGuaild() {
        if (currentMapDataBean != null) {
            boolean choiceNotAppointment = false;
            if (homeAppointmentBean != null) {
                Log.e("yzh", homeAppointmentBean.latitude + "--" + currentMapDataBean.latitude);
                if (homeAppointmentBean.latitude != currentMapDataBean.latitude || homeAppointmentBean.longitude != currentMapDataBean.longitude) {
                    choiceNotAppointment = true;
                } else {
                    choiceNotAppointment = false;
                }
            } else {
                choiceNotAppointment = false;
            }
//            if (UserHelper.getSavedUser() == null || Tools.isNull(UserHelper.getSavedUser().token)) {
//                startActivity(LoginActivity.getLauncher(getContext()));
//            } else {
                startActivity(GuildActivity.getLauncher(getContext(), currentMapDataBean.latitude, currentMapDataBean.longitude, null, choiceNotAppointment, currentMapDataBean.id, currentMapDataBean.type));
//            }
        }
    }

    @OnClick(R.id.iv_my_location)
    public void backLcation() {
        MyLocationBean bean = PreferencesHelper.getData(MyLocationBean.class);
        if (bean != null) {
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bean.latitude, bean.longtitude), 14));
        }
    }

    @OnClick(R.id.enter_charge_station_iv)
    public void enterChargeStation() {
        //获取详情要token 所以判断
        if (UserHelper.getSavedUser() == null || Tools.isNull(UserHelper.getSavedUser().token)) {
            startActivity(LoginActivity.getLauncher(getContext()));
        } else {
            startActivity(ChargeDetails2Activity.getLauncher(getActivity(), currentMapDataBean.id + "", currentMapDataBean.type));
        }
    }

    @OnClick(R.id.iv_scan)
    public void goScan() {


        if (UserHelper.getSavedUser() == null || Tools.isNull(UserHelper.getSavedUser().token)) {
            startActivity(LoginActivity.getLauncher(getContext()));
        } else {
            //进入扫一扫界面
            new IntentIntegrator(getActivity())
                    .setCaptureActivity(ChargeCaptureActivity.class)
                    .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                    .setPrompt(getString(R.string.please_take_qrcode))// 设置提示语
                    .setCameraId(0)// 选择摄像头,可使用前置或者后置
                    .setBeepEnabled(true)// 是否开启声音,扫完码之后会"哔"的一声
                    .setBarcodeImageEnabled(false)// 扫完码之后生成二维码的图片
                    .initiateScan();// 初始化扫码
        }
    }

    @OnClick(R.id.rl_charger_order)
    public void goCharger() {
        startActivity(ChagerStatueActivity.getLauncher(getContext(), homeChargeOrderBean));
    }

    @Override
    public void goLogin() {

        UserHelper.clearUserInfo(UserBean.class);
        ll_appontment.setVisibility(View.GONE);
        rl_charger_order.setVisibility(View.GONE);
        rl_not_pay.setVisibility(View.GONE);
        startActivity(LoginActivity.getLauncher(getContext()));
    }
}
