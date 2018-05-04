package com.huawei.fusionchargeapp.views;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.utils.ToastMgr;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.ChargeFeeBean;
import com.huawei.fusionchargeapp.model.beans.ChargingGunBeans;
import com.huawei.fusionchargeapp.model.beans.HomeChargeOrderBean;
import com.huawei.fusionchargeapp.model.beans.OrderRequestInfo;
import com.huawei.fusionchargeapp.model.beans.PileFeeBean;
import com.huawei.fusionchargeapp.model.beans.ScanChargeInfo;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.ChargeOrderDetailsPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.ChargeOrderDetailView;
import com.huawei.fusionchargeapp.weights.ChargeFeeDialog;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChargeOrderDetailsActivity extends BaseActivity<ChargeOrderDetailView, ChargeOrderDetailsPresenter> implements RadioGroup.OnCheckedChangeListener, ChargeOrderDetailView {

    @Bind(R.id.ll_choose_gun)
    LinearLayout llChooseGun;
    private int chargePowerCount = 0;

    private final int BASE_ID = 8888;

    public static final int WITH_POWER = 1;
    public static final int WITH_TIME = 2;
    public static final int WITH_MONEY = 3;
    public static final int WITH_FULL = 4;
    public static final int WITH_CONTROL = 5;

    public static final String CHARGE_MODE_FAST = "1";
    public static final String CHARGE_MODE_SLOW = "2";

    public static final int CHARGE_GUN_SLOW = 1;
    public static final int CHARGE_GUN_FAST = 2;

    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.charge_station_name)
    TextView chargeStationName;
    @Bind(R.id.charge_zhuang_number)
    TextView chargeZhuangNumber;
    @Bind(R.id.iv_charge_cost_ask)
    ImageView ivChargeCostAsk;
    @Bind(R.id.charge_cost)
    TextView chargeCost;
    @Bind(R.id.charge_service_cost)
    TextView chargeServiceCost;
    //    @Bind(R.id.rg_choose_gun)
//    RadioGroup rgChooseGun;
    @Bind(R.id.rb_with_power)
    RadioButton rbWithPower;
    @Bind(R.id.rb_with_money)
    RadioButton rbWithMoney;
    @Bind(R.id.rb_with_time)
    RadioButton rbWithTime;
    @Bind(R.id.rb_with_full)
    RadioButton rbWithFull;
    @Bind(R.id.et_charge_count)
    EditText etChargeCount;
    @Bind(R.id.btn_start_charge)
    Button btnStartCharge;
    @Bind(R.id.rg_choose_power_style)
    RadioGroup rgChoosePowerStyle;
    private ScanChargeInfo scanChargeInfo;
    private ChargingGunBeans chooseGun;
    private int chooseStyle = WITH_POWER;

    private List<ImageView> selectGunImageViews = new ArrayList<>();
    private boolean hasAppointment=false;

    private View.OnClickListener chooseGunsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            int index = id - BASE_ID;
            chooseGun = scanChargeInfo.getChargingGunBeans().get(index);
            for (int i = 0; i < selectGunImageViews.size(); i++) {
                ImageView iv = selectGunImageViews.get(i);
                if(id == iv.getId()) {
                    iv.setImageResource(R.drawable.list_btn_on);
                } else {
                    iv.setImageResource(R.drawable.list_btn);
                }
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_order_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            navBar.setBackground(getResources().getDrawable(R.drawable.nan_bg));
        } else {
            navBar.setColor(getResources().getColor(R.color.app_blue));
        }
        navBar.setNavTitle(this.getString(R.string.charge_detail));

        initData();
        initView();

        rgChoosePowerStyle.setOnCheckedChangeListener(this);
        rbWithPower.setChecked(true);
        setRadioButtonDrawableSize();
    }

    private void initData() {
        String data = getIntent().getStringExtra("data");
        Gson gson = new Gson();
        scanChargeInfo = gson.fromJson(data, new TypeToken<ScanChargeInfo>() {
        }.getType());
        presenter.getFeeInfo(scanChargeInfo.getChargingPileId());
    }

    private void initView() {
        chargeStationName.setText(scanChargeInfo.getStationName());
        chargeZhuangNumber.setText(scanChargeInfo.getRunCode());
        String chargeCostStr = "<font color='#FF0000'>" + scanChargeInfo.getFee() + "</font>" + getString(R.string.yuan_du);
        chargeCost.setText(Html.fromHtml(chargeCostStr));
        chargeServiceCost.setText(scanChargeInfo.getServiceFee() + getString(R.string.yuan_du));

        List<ChargingGunBeans> gunBeans = scanChargeInfo.getChargingGunBeans();
        if (gunBeans != null && !gunBeans.isEmpty()) {

//            for (int i = 0; i < scanChargeInfo.getChargingGunBeans().size(); i++) {
//                LayoutInflater.from(this).inflate(R.layout.merge_charge_gun_radiobutton,rgChooseGun,true);
//                RadioButton rb = (RadioButton) rgChooseGun.getChildAt(i);
//                rb.setText("充电枪 ： " + scanChargeInfo.getChargingGunBeans().get(i).getGunNumber());
//                rb.setId(BASE_ID + i);
//                if(i == 0) {
//                    rb.setChecked(true);
//                    chooseGun = scanChargeInfo.getChargingGunBeans().get(i);
//                }
//            }
//            rgChooseGun.setOnCheckedChangeListener(this);
            for (int i = 0; i < scanChargeInfo.getChargingGunBeans().size(); i++) {
                ChargingGunBeans chargingGunBeans = scanChargeInfo.getChargingGunBeans().get(i);


                LinearLayout ll = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_choose_gun, llChooseGun,false);

                ImageView ivSelectGun = ll.findViewById(R.id.iv_select_gun);
                ivSelectGun.setId(BASE_ID + i);
                selectGunImageViews.add(ivSelectGun);
                if(chargingGunBeans.getReserve()==1){
                    hasAppointment=true;
                    chooseGun = chargingGunBeans;
                    ivSelectGun.setImageResource(R.drawable.list_btn_on);
                }else{
                    ivSelectGun.setImageResource(R.drawable.list_btn);
                }
                TextView tvGunName = ll.findViewById(R.id.tv_gun_name);
                tvGunName.setText(getString(R.string.charge_gun_ )+ chargingGunBeans.getGunNumber());

                TextView tvGunStatus = ll.findViewById(R.id.tv_gun_status);
                if(chargingGunBeans.getGunStatus() == 1 ){
                    //空闲
                    tvGunStatus.setText(R.string.gun_status_free);
                    tvGunStatus.setTextColor(getResources().getColor(R.color.gun_status_free));
                    tvGunStatus.setBackgroundResource(R.drawable.shape_choose_gun_free);
                    ivSelectGun.setOnClickListener(chooseGunsClickListener);
                }
                else if(chargingGunBeans.getGunStatus() == 2) {
                    //使用中（插标未充电）
                    tvGunStatus.setText(R.string.gun_not_using);
                    tvGunStatus.setTextColor(getResources().getColor(R.color.gun_status_free));
                    tvGunStatus.setBackgroundResource(R.drawable.shape_choose_gun_free);
                    ivSelectGun.setOnClickListener(chooseGunsClickListener);
                }
                else if(chargingGunBeans.getGunStatus() == 3) {
                    //使用中（已充电）
                    tvGunStatus.setText(R.string.gun_status_using);
                    tvGunStatus.setTextColor(getResources().getColor(R.color.gun_status_using));
                    tvGunStatus.setBackgroundResource(R.drawable.shape_choose_gun_using);
                } else if(chargingGunBeans.getGunStatus() == 4) {
                    //预约中
                    tvGunStatus.setText(R.string.gun_status_appoint);
                    tvGunStatus.setTextColor(getResources().getColor(R.color.gun_status_appoint));
                    tvGunStatus.setBackgroundResource(R.drawable.shape_choose_gun_appoint);
                } else if(chargingGunBeans.getGunStatus() == 5) {
                    //停止服务
                    tvGunStatus.setText(R.string.gun_status_stop_service);
                    tvGunStatus.setTextColor(getResources().getColor(R.color.gun_status_stop_service));
                    tvGunStatus.setBackgroundResource(R.drawable.shape_choose_gun_stop_service);
                } else if(chargingGunBeans.getGunStatus() == 6) {
                    //故障
                    tvGunStatus.setText(R.string.gun_status_bad);
                    tvGunStatus.setTextColor(getResources().getColor(R.color.gun_status_bad));
                    tvGunStatus.setBackgroundResource(R.drawable.shape_choose_gun_stop_bad);
                }
                llChooseGun.addView(ll);
            }
        }

    }

    private void setRadioButtonDrawableSize() {
        Drawable[] drawables = null;
       /* for (int i = 0; i < rgChooseGun.getChildCount(); i++) {
            RadioButton rb = (RadioButton) rgChooseGun.getChildAt(i);
            drawables = rb.getCompoundDrawables();
            drawables[2].setBounds(0,0,Tools.dip2px(getApplicationContext(),20),Tools.dip2px(getApplicationContext(),20));
            rb.setCompoundDrawables(null,null,drawables[2],null);
        }*/

        for (int i = 0; i < rgChoosePowerStyle.getChildCount(); i++) {
            RadioButton rb = (RadioButton) rgChoosePowerStyle.getChildAt(i);
            drawables = rb.getCompoundDrawables();
            drawables[0].setBounds(0, 0, Tools.dip2px(getApplicationContext(), 20), Tools.dip2px(getApplicationContext(), 20));
            drawables[2].setBounds(0, 0, Tools.dip2px(getApplicationContext(), 20), Tools.dip2px(getApplicationContext(), 20));
            rb.setCompoundDrawables(drawables[0], null, drawables[2], null);
        }

    }

    @Override
    protected ChargeOrderDetailsPresenter createPresenter() {
        return new ChargeOrderDetailsPresenter();
    }

    @OnClick({R.id.iv_charge_cost_ask, R.id.btn_start_charge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_charge_cost_ask:
                ChargeFeeDialog dialog = new ChargeFeeDialog(this);

                if (list_fee != null && list_fee.size() != 0) {
                    dialog.show();
                    dialog.setFeeDatas(list_fee);
                }
                break;
            case R.id.btn_start_charge:
                if (UserHelper.getSavedUser() == null || Tools.isNull(UserHelper.getSavedUser().token)) {
                    startActivity(LoginActivity.getLauncher(this));
                    return;
                }
                if (chooseGun == null) {
                    ToastMgr.show(getString(R.string.no_charge_gun_cannot_charge));
                    return;
                }
                if (chooseStyle == WITH_FULL) {
                    getData();
                    return;
                }
                String count = etChargeCount.getText().toString();
                if (!TextUtils.isEmpty(count)) {
                    chargePowerCount = Integer.parseInt(count);
                    if (chargePowerCount > 999 || chargePowerCount < 1) {
                        ToastMgr.show(getString(R.string.please_enter_1_999_int));
                        return;
                    } else {
                        getData();
                    }
                } else {
                    ToastMgr.show(getString(R.string.please_input_charge_count));
                }

                break;
        }
    }

    //开始充电
    private void getData() {
        btnStartCharge.setEnabled(false);

        showLoading();

        OrderRequestInfo info = new OrderRequestInfo();
        info.setChargingPileName(scanChargeInfo.getChargingPileName());
        //TODO 后续替换
//        info.setVirtualId("0004d2");
        info.setVirtualId(scanChargeInfo.getVirtualId());
//        info.setAppUserId(71 + "");
        info.setAppUserId(scanChargeInfo.getAppUserId());
//        info.setGunCode(2 + "");
        info.setGunCode(chooseGun.getGunCode());
//        info.setControlInfo(2 + "");
        info.setControlInfo(chooseStyle + "");
        //TODO 后续替换
//        info.setChargingPileId(14);
        info.setChargingPileId(scanChargeInfo.getChargingPileId());
        if (chooseStyle != WITH_FULL) {
//            info.setControlData(320 + "");
            info.setControlData(chargePowerCount + "");
        } else {
            info.setControlData("0");
        }
        //设置充电模式，交流为慢，直流为快
        if (chooseGun.getGunType() == CHARGE_GUN_FAST) {
            info.setChargingMode(CHARGE_MODE_FAST);
        } else {
            info.setChargingMode(CHARGE_MODE_SLOW);
        }

        presenter.startCharge(info);

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "扫码已取消", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getId()) {
            //充电方式选择
            case R.id.rg_choose_power_style:
                if (i == rbWithFull.getId()) {
                    etChargeCount.setEnabled(false);
                    etChargeCount.setClickable(false);
                    etChargeCount.setHint(R.string.choose_charge_full);
                    chooseStyle = WITH_FULL;
                } else {
                    etChargeCount.setEnabled(true);
                    etChargeCount.setClickable(true);
//                    etChargeCount.setHint(R.string.please_enter_charge_count);
                    if (i == rbWithPower.getId()) {
                        chooseStyle = WITH_POWER;
                        etChargeCount.setHint(R.string.please_enter_charge_count);
                    } else if (i == rbWithMoney.getId()) {
                        chooseStyle = WITH_MONEY;
                        etChargeCount.setHint(R.string.please_input_charge_money);
                    } else if (i == rbWithTime.getId()) {
                        chooseStyle = WITH_TIME; //2
                        etChargeCount.setHint(R.string.please_input_charge_time);
                    }
                }
                break;
           /* //充电枪选择
            case R.id.rg_choose_gun:
                int index = i - BASE_ID;
                chooseGun = scanChargeInfo.getChargingGunBeans().get(index);
                break;*/
        }
    }

    @Override
    public void goLogin() {
        ToastMgr.show(getString(R.string.login_fail));
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(ChargeOrderDetailsActivity.this));
    }

    @Override
    public void onSuccess() {
        hideLoading();
        HomeChargeOrderBean homeChargeOrderBean = new HomeChargeOrderBean();
        homeChargeOrderBean.virtualId = scanChargeInfo.getVirtualId();
        homeChargeOrderBean.chargeGunNum = chooseGun.getGunCode();
        startActivity(ChagerStatueActivity.getLauncher(ChargeOrderDetailsActivity.this, homeChargeOrderBean));
        finish();
    }

    @Override
    public void onFail() {
        btnStartCharge.setEnabled(true);
    }

    //对应充电桩的费率信息
    private List<ChargeFeeBean> list_fee;
    @Override
    public void showPileFeeInfo(PileFeeBean bean) {
        list_fee = bean.feeList;
    }


}
