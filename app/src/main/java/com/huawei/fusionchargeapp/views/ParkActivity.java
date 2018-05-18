package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.RxBusSubscriber;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.rxbus.RxBus;
import com.google.zxing.integration.android.IntentIntegrator;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.ChargeFeeBean;
import com.huawei.fusionchargeapp.model.beans.MapDataBean;
import com.huawei.fusionchargeapp.model.beans.MapInfoBean;
import com.huawei.fusionchargeapp.model.beans.MyLocationBean;
import com.huawei.fusionchargeapp.model.beans.PileFeeBean;
import com.huawei.fusionchargeapp.model.beans.ZoneStationBean;
import com.huawei.fusionchargeapp.presenter.ParkPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.ParkView;
import com.huawei.fusionchargeapp.weights.ChargeFeeDialog;
import com.huawei.fusionchargeapp.weights.NavBar;
import com.huawei.map.mapapi.CameraUpdate;
import com.huawei.map.mapapi.CameraUpdateFactory;
import com.huawei.map.mapapi.HWMap;
import com.huawei.map.mapapi.InnerMap;
import com.huawei.map.mapapi.MapView;
import com.huawei.map.mapapi.model.CameraPosition;
import com.huawei.map.mapapi.model.LatLng;
import com.huawei.map.mapapi.model.Marker;
import com.huawei.map.mapapi.model.MarkerOptions;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by issuser on 2018/4/24.
 */

public class ParkActivity extends BaseActivity<ParkView, ParkPresenter> implements ParkView {

    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.map)
    MapView mapView;
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


    private HWMap mIMapImpl;
    private Context context = ParkActivity.this;
    //园区id
    private long zoneId;
    private static final String OUTDOOR_URL = "http://api-beta.hwmap.cn/NaviModePullMapServer/V1.1.0?tileid={z},{x},{y}&apikey=5ca3029553574cbb83a170bece7a5daa&c=1.0.1";
    private static final String INDOOR_URL = "http://api-beta.hwmap.cn/pullmap/indoor/drive/V1?indoorid={x}&c=1.0.1";
    private static final String LAYER_URl = "http://114.115.148.209:8080/geowebcache/service/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=MapServerB&MAPNAME=MapServer&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&STYLES=&BBOX={MnX},{MnY},{MxX},{MxY}";


    public static Intent getLauncher(Context context, long id) {
        Intent intent = new Intent(context, ParkActivity.class);
        intent.putExtra("id", id);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_park;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setNavTitle(getString(R.string.guilding_park_map));
        navBar.setImageBackground(R.drawable.nan_bg);

        zoneId = getIntent().getLongExtra("id", 0);
        mapView.onCreate(savedInstanceState);
        mIMapImpl = mapView.getMap();
        mIMapImpl.getUiSettings().setZoomControlsEnabled(false);
        mIMapImpl.getUiSettings().setRotateGesturesEnabled(false);
        mIMapImpl.setOnMapLoadedListener(new HWMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                //重设数据源
//                mIMapImpl.getInnerMap().resetDataSource(InnerMap.LAYER_TYPE_DEFAULT,OUTDOOR_URL);
//                mIMapImpl.getInnerMap().resetDataSource(InnerMap.LAYER_TYPE_INDOOR,INDOOR_URL);
                mIMapImpl.getInnerMap().resetDataSource(InnerMap.LAYER_TYPE_3857G, LAYER_URl);
                mIMapImpl.getInnerMap().addTileLayer(0, InnerMap.LAYER_TYPE_3857G, 1, 10);
                mIMapImpl.runOnDrawFrame();
//                LatLng latlng=new LatLng(22.65827,114.057425);
//                final Marker marker= mIMapImpl.addMarker(new MarkerOptions()
//                        .position(latlng).title("南京").snippet("DefaultMarker"));
                presenter.getZoneDatas(zoneId);
            }
        });
//        CameraPosition cameraPosition = new CameraPosition(
//                new LatLng(22.65827,114.057425), 17, 0, 0);
//        CameraUpdate cameraUpdate = CameraUpdateFactory
//                .newCameraPosition(cameraPosition);
//        mIMapImpl.animateCamera(cameraUpdate);
        mIMapImpl.setOnMarkerClickListener(new HWMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().equals("location")) {
                    //点击了定位点 不做操作
                } else {
                    //点击了其他mark
                    ZoneStationBean bean = list.get(Integer.parseInt(marker.getTitle()));
                    currentMapDataBean = bean;
                    presenter.getInfo(bean.id, bean.type);
                }
                return true;
            }
        });
        mIMapImpl.setOnMapClickListener(new HWMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                rl_bottom_detail.setVisibility(View.GONE);
            }
        });
        RxBus.getDefault().toObservable(MyLocationBean.class, Constant.REFRESH_LOCATION)
                .compose(this.<MyLocationBean>bindToLifecycle())
                .subscribe(new RxBusSubscriber<MyLocationBean>() {

                    @Override
                    public void receive(MyLocationBean data) {
                        //刷新坐标 位置不一致时才更新路线
                        if (locationMark != null) {

                            if (locationMark.getPosition().latitude != data.latitude || locationMark.getPosition().longitude != data.longtitude) {
                                locationMark.remove();
                                locationMark = mIMapImpl.addMarker(new MarkerOptions()
                                        .position(new LatLng(data.latitude, data.longtitude)).title("location").icon(com.huawei.map.mapapi.model.BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                                .decodeResource(getResources(), R.mipmap.location))));
                            }
                        } else {
                            locationMark = mIMapImpl.addMarker(new MarkerOptions()
                                    .position(new LatLng(data.latitude, data.longtitude)).title("location").icon(com.huawei.map.mapapi.model.BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                            .decodeResource(getResources(), R.mipmap.location))));
                        }

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected ParkPresenter createPresenter() {
        return new ParkPresenter();
    }

    @OnClick(R.id.iv_sao)
    public void actionScan() {
        if (UserHelper.getSavedUser() == null || Tools.isNull(UserHelper.getSavedUser().token)) {
            startActivity(LoginActivity.getLauncher(context));
        } else {
            //进入扫一扫界面
            new IntentIntegrator(ParkActivity.this)
                    .setCaptureActivity(ChargeCaptureActivity.class)
                    .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                    .setPrompt(getString(R.string.please_take_qrcode))// 设置提示语
                    .setCameraId(0)// 选择摄像头,可使用前置或者后置
                    .setBeepEnabled(true)// 是否开启声音,扫完码之后会"哔"的一声
                    .setBarcodeImageEnabled(false)// 扫完码之后生成二维码的图片
                    .initiateScan();// 初始化扫码
        }
    }

    @Override
    public void goLogin() {

    }

    private Marker locationMark;
    private List<ZoneStationBean> list;
    private ZoneStationBean currentMapDataBean;

    @Override
    public void renderDatas(List<ZoneStationBean> list) {
        if (list != null) {
            this.list = list;
            for (int i = 0; i < list.size(); i++) {
                LatLng latlng = new LatLng(list.get(i).latitude, list.get(i).longitude);
                mIMapImpl.addMarker(new MarkerOptions()
                        .position(latlng).title(i + "").icon(com.huawei.map.mapapi.model.BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(), R.mipmap.home_ic_position))));
            }
            if (list.size() != 0) {
                CameraPosition cameraPosition = new CameraPosition(
                        new LatLng(list.get(0).latitude, list.get(0).longitude), 17, 0, 0);
                CameraUpdate cameraUpdate = CameraUpdateFactory
                        .newCameraPosition(cameraPosition);
                mIMapImpl.animateCamera(cameraUpdate);
            }

        }

        MyLocationBean locationBean = PreferencesHelper.getData(MyLocationBean.class);
        if (locationBean != null) {

            locationMark = mIMapImpl.addMarker(new MarkerOptions()
                    .position(new LatLng(locationBean.latitude, locationBean.longtitude)).title("location").icon(com.huawei.map.mapapi.model.BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.mipmap.location))));
        }


    }

    @Override
    public void getMarkInfo(long id, MapInfoBean mapInfoBean) {
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
            scoreUnit.setText("暂无评分");
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

    //对应充电桩的费率信息
    private List<ChargeFeeBean> list_fee;

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

    @OnClick(R.id.ll_fee)
    public void showFee() {
        ChargeFeeDialog dialog = new ChargeFeeDialog(context);
        dialog.show();
        if (list_fee != null && list_fee.size() != 0) {
            dialog.setFeeDatas(list_fee);
        }
    }

    @OnClick(R.id.iv_guaild)
    public void goGuaild() {
        if (currentMapDataBean != null) {
            boolean choiceNotAppointment = false;
            Log.e("yzh", "guild--" + currentMapDataBean.toString());
            if (UserHelper.getSavedUser() == null || Tools.isNull(UserHelper.getSavedUser().token)) {
                startActivity(LoginActivity.getLauncher(context));
            } else {
                startActivity(GuildActivity.getLauncher(context, currentMapDataBean.latitude, currentMapDataBean.longitude, null, choiceNotAppointment, currentMapDataBean.id, currentMapDataBean.type));
                finish();
            }
        }
    }

    @OnClick(R.id.enter_charge_station_iv)
    public void enterChargeStation() {
        //获取详情要token 所以判断
        if (UserHelper.getSavedUser() == null || Tools.isNull(UserHelper.getSavedUser().token)) {
            startActivity(LoginActivity.getLauncher(context));
        } else {
            startActivity(ChargeDetailsActivity.getLauncher(context, currentMapDataBean.id + "", currentMapDataBean.type));
        }
    }
}
