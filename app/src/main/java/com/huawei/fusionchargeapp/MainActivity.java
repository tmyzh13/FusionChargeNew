package com.huawei.fusionchargeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.common.AppManager;
import com.corelibs.subscriber.ResponseSubscriber;
import com.corelibs.utils.IMEUtil;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.rxbus.RxBus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ScanApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RequestIadminLoginBean;
import com.huawei.fusionchargeapp.model.beans.ScanChargeInfo;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.model.beans.W3User;
import com.huawei.fusionchargeapp.utils.ChoiceManager;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.MyAppointDetailActivity;
import com.huawei.fusionchargeapp.views.MyTcActivity;
import com.huawei.fusionchargeapp.views.SearchStationTitleActivity;
import com.huawei.fusionchargeapp.views.SettingActivity;
import com.huawei.fusionchargeapp.views.UserInfoActivity;
import com.huawei.fusionchargeapp.views.W3AccountBindPhoneActivity;
import com.huawei.fusionchargeapp.views.WelcomeActivity;
import com.huawei.fusionchargeapp.views.home.HomeListFragment;
import com.huawei.fusionchargeapp.views.home.MapFragment;
import com.huawei.fusionchargeapp.views.interfaces.MyOrderActivity;
import com.huawei.fusionchargeapp.views.mycount.MyAcountActivity;
import com.huawei.hae.mcloud.bundle.base.Lark;
import com.huawei.hae.mcloud.bundle.base.login.LoginCallback;
import com.huawei.hae.mcloud.bundle.base.login.model.User;
import com.huawei.hae.mcloud.bundle.base.network.Network;
import com.huawei.hae.mcloud.bundle.base.network.callback.NetworkCallback;
import com.huawei.hae.mcloud.bundle.base.util.AppUtils;
import com.trello.rxlifecycle.ActivityEvent;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity {

    @Bind(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.main_left_drawer_layout)
    LinearLayout main_left_drawer_layout;
    @Bind(R.id.main_right_drawer_layout)
    LinearLayout main_right_drawer_layout;
    @Bind(R.id.view_main_statue)
    View view_main_statue;
    @Bind(R.id.view_statue)
    View view_statue;
    @Bind(R.id.tv_map)
    TextView tv_map;
    @Bind(R.id.tv_list)
    TextView tv_list;
    @Bind(R.id.iv_user_icon)
    ImageView iv_user_icon;
    @Bind(R.id.cb_charge_direct)
    CheckBox cb_charge_direct;
    @Bind(R.id.cb_charge_alternating)
    CheckBox cb_charge_alternating;
    @Bind(R.id.cb_free)
    CheckBox cb_free;
    @Bind(R.id.cb_busy)
    CheckBox cb_busy;
    @Bind(R.id.et_distance)
    EditText et_distance;
    @Bind(R.id.tv_user_name)
    TextView tv_user_name;
    @Bind(R.id.tv_favourite)
    TextView tv_favourite;
    @Bind(R.id.ll_user_icon)
    LinearLayout ll_user_icon;
    @Bind(R.id.iv_search)
    ImageView search;
    @Bind(R.id.ll_setting)
    LinearLayout ll_setting;
    private Context context = MainActivity.this;

    private MapFragment mapFragment;
    private HomeListFragment homeListFragment;

    public static final String ACTION = "action";
    public static final String LOGINT_OUT = "loginout";
    public static final String EXIT = "exit";

    public static final String W3_ACCOUNT = "w3_account";
    public static final String W3_PHONE = "w3_phone";
    public static final String IS_REGISTER_SUCCESS = "is_register_success";
    public static final int W3_REGISTER_REQUEST_CODE = 0x00123;




    private String w3Account;
    private String w3Phone;
    private boolean notNeedRegister = false;

    public static Intent getLauncher(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent == null || intent.getExtras() == null)
            return;
        if (EXIT.equals(intent.getExtras().get(ACTION))) {
            PreferencesHelper.clearData();
            AppManager.getAppManager().appExit();
        } else if (LOGINT_OUT.equals(intent.getExtras().get(ACTION))) {
            PreferencesHelper.clearData();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {


        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ViewGroup.LayoutParams lp = view_main_statue.getLayoutParams();
        lp.height = Tools.getStatueHeight(context);
        view_main_statue.setLayoutParams(lp);
        view_statue.setLayoutParams(lp);
        view_main_statue.setBackgroundResource(R.drawable.nan_bg);
        view_statue.setBackgroundColor(context.getResources().getColor(R.color.transparent_black));

        mapFragment = new MapFragment();
        homeListFragment = new HomeListFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, mapFragment).commit();

        cb_charge_alternating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_charge_direct.setChecked(false);
                }
            }
        });

        cb_charge_direct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_charge_alternating.setChecked(false);
                }
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (drawerLayout.isDrawerOpen(main_left_drawer_layout)) {
                    ChoiceManager.getInstance().setDrawerStatus(true);
                    return;
                }
                if (ChoiceManager.getInstance().getType() == 3) {
                    cb_charge_alternating.setChecked(true);
                    cb_charge_direct.setChecked(true);
                } else if (ChoiceManager.getInstance().getType() == 2) {
                    cb_charge_alternating.setChecked(true);
                    cb_charge_direct.setChecked(false);
                } else if (ChoiceManager.getInstance().getType() == 1) {
                    cb_charge_alternating.setChecked(false);
                    cb_charge_direct.setChecked(true);
                } else if (ChoiceManager.getInstance().getType() == 0) {
                    cb_charge_alternating.setChecked(false);
                    cb_charge_direct.setChecked(false);
                }
                if (ChoiceManager.getInstance().getStatue() == 3) {
                    cb_free.setChecked(true);
                    cb_busy.setChecked(true);
                } else if (ChoiceManager.getInstance().getStatue() == 2) {
                    cb_busy.setChecked(true);
                    cb_free.setChecked(false);
                } else if (ChoiceManager.getInstance().getStatue() == 1) {
                    cb_busy.setChecked(false);
                    cb_free.setChecked(true);
                } else if (ChoiceManager.getInstance().getStatue() == 0) {
                    cb_busy.setChecked(false);
                    cb_free.setChecked(false);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (!drawerLayout.isDrawerOpen(main_left_drawer_layout)) {
                    ChoiceManager.getInstance().setDrawerStatus(false);
                }
                IMEUtil.closeIME(et_distance, context);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        /*
        RxBus.getDefault().toObservable(Object.class,Constant.REFRESH_MAIN_HEAD_PHOTO)
                .compose(this.bindToLifecycle())
                .subscribe(new RxBusSubscriber<Object>() {

                    @Override
                    public void receive(Object data) {
                        if(UserHelper.getSavedUser() != null){
                            Glide.with(MainActivity.this).load(UserHelper.getSavedUser().photoUrl)
                                    .override(320,320).error(R.mipmap.ic_launcher_round).into(iv_user_icon);
                        }
                    }
                });*/
        cb_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.e("yzh","1111111111111111");
                    cb_busy.setChecked(false);
                }
            }
        });

        cb_busy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_free.setChecked(false);
                }
            }
        });
    }

    @OnClick(R.id.iv_user)
    public void openLeft() {
        if (drawerLayout.isDrawerOpen(main_right_drawer_layout)) {
            drawerLayout.closeDrawer(main_right_drawer_layout);
        }
//        if (TextUtils.isEmpty(PreferencesHelper.getData(Constant.LOGIN_STATUE))) {
//            startActivity(LoginActivity.getLauncher(context));
//        }
        if (UserHelper.getSavedUser() == null) {
            startActivity(LoginActivity.getLauncher(context));
        } else {
            if (!Tools.isNull(UserHelper.getSavedUser().photoUrl)) {
                //头像
                SimpleTarget<GlideDrawable> target = new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv_user_icon.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        String path = PreferencesHelper.getData(Tools.USER_PHOTO_PATH);
                        if (!TextUtils.isEmpty(path)) {
                            File file = new File(path);
                            Glide.with(context).load(Uri.fromFile(file))
                                    .error(R.mipmap.ic_launcher_round).into(iv_user_icon);
                        }
                    }
                };
                Glide.with(context).load(UserHelper.getSavedUser().photoUrl).error(R.mipmap.ic_launcher)
                        .override(320, 320).into(target);
            }

            if(Tools.isNull(UserHelper.getSavedUser().name)){
                tv_user_name.setText(UserHelper.getSavedUser().phone);
            }else{
                tv_user_name.setText(UserHelper.getSavedUser().name);
            }

            tv_favourite.setText(UserHelper.getSavedUser().score+"");
            drawerLayout.openDrawer(main_left_drawer_layout);
        }

    }

    @OnClick(R.id.ll_user_icon)
    public void gotoUserinfo() {
        startActivity(UserInfoActivity.startActivity(context));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (drawerLayout.isDrawerOpen(main_left_drawer_layout)) {
                    drawerLayout.closeDrawer(main_left_drawer_layout);
                }
            }
        }, 500);
    }

    @OnClick(R.id.ll_setting)
    public void gotoSetting() {
        startActivity(SettingActivity.startActivity(context));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (drawerLayout.isDrawerOpen(main_left_drawer_layout)) {
                    drawerLayout.closeDrawer(main_left_drawer_layout);
                }
            }
        }, 500);
    }

    @OnClick(R.id.iv_choice)
    public void openRight() {
        if (drawerLayout.isDrawerOpen(main_left_drawer_layout)) {
            drawerLayout.closeDrawer(main_left_drawer_layout);
        }
        drawerLayout.openDrawer(main_right_drawer_layout);
    }

    @OnClick(R.id.tv_map)
    public void choiceMap() {
        RxBus.getDefault().send(new Object(), Constant.REFRESH_HOME_STATUE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        tv_map.setTextColor(getResources().getColor(R.color.app_blue));
        tv_list.setTextColor(getResources().getColor(R.color.white));
        ViewGroup.LayoutParams lp = tv_map.getLayoutParams();
        lp.width = Tools.dip2px(context, 75);
        tv_map.setLayoutParams(lp);
        ViewGroup.LayoutParams lp1 = tv_list.getLayoutParams();
        lp1.width = Tools.dip2px(context, 65);
        tv_list.setLayoutParams(lp1);
        tv_map.setBackgroundResource(R.drawable.tv_corner_white);
        tv_list.setBackgroundColor(getResources().getColor(R.color.transparent));

        ft.hide(homeListFragment);
        if (!mapFragment.isAdded()) {
            ft.add(R.id.content, mapFragment).show(mapFragment);
        } else {
            ft.show(mapFragment);
        }
        ft.commit();

    }

    @OnClick(R.id.tv_list)
    public void choiceList() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        tv_map.setTextColor(getResources().getColor(R.color.white));
        tv_list.setTextColor(getResources().getColor(R.color.app_blue));
        ViewGroup.LayoutParams lp = tv_map.getLayoutParams();
        lp.width = Tools.dip2px(context, 65);
        tv_map.setLayoutParams(lp);
        ViewGroup.LayoutParams lp1 = tv_list.getLayoutParams();
        lp1.width = Tools.dip2px(context, 75);
        tv_list.setLayoutParams(lp1);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        tv_list.setBackgroundResource(R.drawable.tv_corner_white);
        tv_map.setBackgroundColor(getResources().getColor(R.color.transparent));
//            }
//        },500);


        ft.hide(mapFragment);
        if (!homeListFragment.isAdded()) {
            ft.add(R.id.content, homeListFragment).show(homeListFragment);
        } else {
            ft.show(homeListFragment);
        }
        ft.commit();
    }

    @OnClick(R.id.tv_confirm)
    public void choiceCondition() {
        if (TextUtils.isEmpty(et_distance.getText().toString().trim())) {
            ChoiceManager.getInstance().setDistance(Constant.DEFAULT_DISTANCE);
        } else {
            ChoiceManager.getInstance().setDistance(Double.parseDouble(et_distance.getText().toString()));
        }
        int type = 0;
        int statue = 0;
        if (cb_charge_direct.isChecked()) {
            type = 1;
        }
        if (cb_charge_alternating.isChecked()) {
            type = 2;
        }
        if (cb_charge_direct.isChecked() && cb_charge_alternating.isChecked()) {
            type = 3;
        }
        if (!cb_charge_direct.isChecked() && !cb_charge_alternating.isChecked()) {
            type = 0;
        }

        if (cb_free.isChecked()) {
            statue = 1;
        }
        if (cb_busy.isChecked()) {
            statue = 2;
        }

        ChoiceManager.getInstance().setStatue(statue);
        ChoiceManager.getInstance().setType(type);
        //发送设置
        drawerLayout.closeDrawer(main_right_drawer_layout);
        RxBus.getDefault().send(new Object(), Constant.REFRESH_MAP_OR_LIST_DATA);
    }

    @OnClick(R.id.tv_reset)
    public void resetCondition() {
        cb_busy.setChecked(true);
        cb_free.setChecked(false);
        cb_charge_direct.setChecked(false);
        cb_charge_alternating.setChecked(false);
        et_distance.setText("");
        ChoiceManager.getInstance().resetChoice();
        //发送设置
        drawerLayout.closeDrawer(main_right_drawer_layout);
        RxBus.getDefault().send(new Object(), Constant.REFRESH_MAP_OR_LIST_DATA);
    }

    /**
     * 进入我的订单
     */
    @OnClick(R.id.my_order_tv)
    public void toMyOrder() {
//        UserHelper.clearUserInfo(UserBean.class);
        startActivity(MyOrderActivity.getLauncher(MainActivity.this));
    }

    @OnClick(R.id.tv_reserve)
    public void goAllAppointActivity(){
        startActivity(MyAppointDetailActivity.getLauncher(MainActivity.this));
    }

    @OnClick(R.id.iv_search)
    public void goSearch() {
        startActivity(SearchStationTitleActivity.getLauncher(MainActivity.this));
    }

    @OnClick(R.id.ll_home_setmeal)
    public void goSetMeal(){
        startActivity(MyTcActivity.getLauncher(context));
    }

    @OnClick(R.id.ll_my_acount)
    public void goMyAcount(){
        startActivity(MyAcountActivity.getLuancher(context));
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    /**
     * 物理返回键拦截
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doublePressBackToast();
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    /**
     * 双击两次返回键退出应用
     */
    private boolean isBackPressed = false;//判断是否已经点击过一次回退键

    private void doublePressBackToast() {
        if (!isBackPressed) {
            isBackPressed = true;
            showToast(getString(R.string.double_press_for_exit));
        } else {
            AppManager.getAppManager().finishAllActivity();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        }, 2000);
    }

    @Override
    public void goLogin() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果从其他应用过来，去登录
        initOtherLogin();
    }

    private void initOtherLogin() {

        //如果没有登录，就去验证
        if(UserHelper.getSavedUser() != null ) {
            //如果已经登录，根据状态是否显示左侧抽屉
            if (ChoiceManager.getInstance().isDrawerStatus()) {
                openLeft();
            }
            return;
        }
        //如果需要登录，则登录后不需要打开左侧状态栏
        ChoiceManager.getInstance().setDrawerStatus(false);

        Intent intent = getIntent();
        if(intent == null) return;

        boolean isFromOtherApp = intent.getBooleanExtra(WelcomeActivity.IS_FROM_OTHER_APP,false);
        if(isFromOtherApp == false) return;

        if(notNeedRegister) return;

        if(!getLoadingDialog().isShowing()) {
            showLoading();
        }

        Lark.autoLogin(new LoginCallback() {
           @Override
           public void onSuccess(User user) {
               Log.e("zw","mainactivity :" + user.toString());
               if( TextUtils.isEmpty(user.getUid()) ){
                   hideLoading();
                   notNeedRegister = true;
                   return;
               }
               w3Account = user.getUid();
               getW3PhoneNumber();
           }

           @Override
           public void onFailure(int i, String s) {
               hideLoading();
               notNeedRegister = true;
               Log.e("zw","mainactivity : " + s);
           }
       });
    }

    /**
     * 去获取W3手机号码
     */
    public void getW3PhoneNumber(){
        String httpUrl = AppUtils.getHostUrl() + Urls.HUAWEI_GET_INFO;
        Map<String,String> map = new HashMap<>();
        map.put("lang","zh");
        map.put("w3Account",w3Account);

        Network.get(httpUrl, null, map, new NetworkCallback() {
            @Override
            public void onSuccess(Map map, Object o) {
                String userStr = o.toString();
                Log.e("zw","phone number : " + userStr);
                Gson gson = new Gson();
                W3User user = gson.fromJson(userStr, new TypeToken<W3User>() {
                }.getType());
                w3Phone = user.getPerson_Mobile_Code();
                if(!TextUtils.isEmpty(w3Phone) && w3Phone.length() > 11) {
                    w3Phone = w3Phone.substring(w3Phone.length() - 11);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        w3Login();
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e("zw","phone number : " + s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        w3Login();
                    }
                });
            }

            @Override
            public boolean isMainThread() {
                return false;
            }
        });

    }


    /**
     * 登录W3
     */
    private void w3Login(){
        if(!getLoadingDialog().isShowing()) {
            showLoading();
        }

        if(TextUtils.isEmpty(w3Account)) {
            hideLoading();
            notNeedRegister = true;
            return;
        }
        RequestIadminLoginBean bean = new RequestIadminLoginBean();
        if(!TextUtils.isEmpty(w3Phone)) bean.setPhone(w3Phone);
        bean.setJobNumber(w3Account);

        ScanApi api = ApiFactory.getFactory().create(ScanApi.class);
        api.iAdminLogin(bean)
                .compose(new ResponseTransformer<>(this.<BaseData<UserBean>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<UserBean>>() {
                    @Override
                    public void success(BaseData<UserBean> baseData) {
                        hideLoading();
                        if(baseData.data == null) {
                            goToBindW3Account();
                        } else {
                            UserHelper.saveUser(baseData.data);
                            RxBus.getDefault().send(new Object(), Constant.REFRESH_HOME_STATUE);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        notNeedRegister = true;
                        hideLoading();
                    }

                    @Override
                    public boolean operationError(BaseData<UserBean> baseData, int status, String message) {
                        hideLoading();
                        if(status == 210) {
                            goToBindW3Account();
                        } else {
                            notNeedRegister = true;
                        }
                        return super.operationError(baseData, status, message);
                    }
                });
    }

    private void goToBindW3Account(){
        //去注册
        Log.e("zw", " start bind"  );
        Intent bindIntent = new Intent(this, W3AccountBindPhoneActivity.class);
        bindIntent.putExtra(W3_ACCOUNT,w3Account);
        bindIntent.putExtra(W3_PHONE,w3Phone);
        startActivityForResult(bindIntent,W3_REGISTER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == W3_REGISTER_REQUEST_CODE) {
            notNeedRegister = true;
            boolean goLogin = data == null ? false : data.getBooleanExtra(IS_REGISTER_SUCCESS,false);
            if(goLogin) {
                w3Login();
            }
        }
    }
}
