package com.huawei.fusionchargeapp.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.corelibs.utils.ToastMgr;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.ScanApi;
import com.huawei.fusionchargeapp.model.beans.BaseData;
import com.huawei.fusionchargeapp.model.beans.RequestScanBean;
import com.huawei.fusionchargeapp.model.beans.ScanChargeInfo;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.ChargeCapturePresenter;
import com.huawei.fusionchargeapp.views.interfaces.ChargeCaptureView;
import com.huawei.fusionchargeapp.weights.CommonDialog;
import com.huawei.fusionchargeapp.weights.NavBar;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.trello.rxlifecycle.ActivityEvent;

import butterknife.Bind;
import butterknife.OnClick;

public class ChargeCaptureActivity extends BaseActivity<ChargeCaptureView,ChargeCapturePresenter> implements ChargeCaptureView {

    private ScanApi api;

    @Bind(R.id.nav)
    NavBar navBar;
//    @Bind(R.id.iv_light)
    ImageView ivLight;
//    @Bind(R.id.ll_change_erweima_style)
    LinearLayout llChangeErweimaStyle;
    private CaptureManager capture;

    @Bind(R.id.dbv)
    DecoratedBarcodeView dbv;

    private CommonDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_capture;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
            navBar.setBackground(getResources().getDrawable(R.drawable.nan_bg));
        } else {
            navBar.setColor(getResources().getColor(R.color.app_blue));
        }
        navBar.setNavTitle(this.getString(R.string.charge));

        ivLight = dbv.findViewById(R.id.iv_light);
        llChangeErweimaStyle = dbv.findViewById(R.id.ll_change_erweima_style);

        api = ApiFactory.getFactory().create(ScanApi.class);

        capture = new CaptureManager(this, dbv);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.setResultCallBack(new CaptureManager.ResultCallBack() {
            @Override
            public void callBack(int requestCode, int resultCode, Intent intent) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                if (null != result && null != result.getContents()) {
                    showLoadingDialog(result.getContents());
                }
            }
        });
        capture.decode();

    }

    private void showLoadingDialog(String contents){
        showLoading();
        presenter.getData(contents);

        /*RequestScanBean bean = new RequestScanBean();
        bean.setAppUserId(UserHelper.getSavedUser().appUserId + "");
        Log.e("zw","UserHelper.getSavedUser().userId : " + UserHelper.getSavedUser().appUserId);
//        bean.setQrCode("1300000001");
        bean.setQrCode(contents);
        api.getScanChargeInfo(UserHelper.getSavedUser().token,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<ScanChargeInfo>>bindUntilEvent(ActivityEvent.DESTROY)))
                .subscribe(new ResponseSubscriber<BaseData<ScanChargeInfo>>() {
                               @Override
                               public void success(BaseData<ScanChargeInfo> baseData) {
                                   hideLoading();
                                   if(baseData.data != null) {
                                       Gson gson = new Gson();
                                       String data = gson.toJson(baseData.data);
                                       Intent intent = new Intent(ChargeCaptureActivity.this,ChargeOrderDetailsActivity.class);
                                       intent.putExtra("data",data);
                                       startActivity(intent);
                                   } else {
                                       showToast(getString(R.string.no_user_info));
                                   }

                                   finish();

                               }

                               @Override
                               public boolean operationError(BaseData<ScanChargeInfo> scanChargeInfoBaseData, int status, String message) {
                                     hideLoading();

                                     if(status==403){
                                         UserHelper.clearUserInfo(UserBean.class);
                                         startActivity(LoginActivity.getLauncher(ChargeCaptureActivity.this));
                                         finish();
                                     }else{
                                         String content = TextUtils.isEmpty(scanChargeInfoBaseData.msg) ? getString(R.string.server_wrong) : scanChargeInfoBaseData.msg;
                                         dialog = new CommonDialog(ChargeCaptureActivity.this,getString(R.string.hint),content,1);
                                         dialog.show();
                                         dialog.setPositiveListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 dialog.dismiss();
                                                 finish();
                                             }
                                         });
                                     }

                                   return super.operationError(scanChargeInfoBaseData, status, message);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   super.onError(e);
                                   hideLoading();
                                   dialog = new CommonDialog(ChargeCaptureActivity.this,getString(R.string.hint),getString(R.string.scan_time_out),1);
                                   dialog.show();
                                   dialog.setPositiveListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           dialog.dismiss();
                                           finish();
                                       }
                                   });

                               }
                           }
                );*/
    }

    @Override
    protected ChargeCapturePresenter createPresenter() {
        return new ChargeCapturePresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕长久亮屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isTorchOpen){
            dbv.setTorchOff();
        }
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        capture.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_DENIED) {
            capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(getLoadingDialog().isShowing()){
            return true;
        }
        return dbv.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    private boolean isTorchOpen = false;
    @OnClick({R.id.iv_light, R.id.ll_change_erweima_style})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_light:
                if(isTorchOpen){
                    dbv.setTorchOff();
                    isTorchOpen = false;
                } else {
                    dbv.setTorchOn();
                    isTorchOpen = true;
                }
                break;
            case R.id.ll_change_erweima_style:
                startActivity(new Intent(this,ChargeInputNumberActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void goLogin() {
        ToastMgr.show(getString(R.string.login_fail));
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(ChargeCaptureActivity.this));
    }

    @Override
    public void onSuccess(ScanChargeInfo info) {
        hideLoading();
        if(info != null) {
            Gson gson = new Gson();
            String data = gson.toJson(info);
            Intent intent = new Intent(ChargeCaptureActivity.this,ChargeOrderDetailsActivity.class);
            intent.putExtra("data",data);
            startActivity(intent);
        } else {
            showToast(getString(R.string.no_user_info));
        }
        finish();
    }

    @Override
    public void onOperationError(String msg) {
        hideLoading();

        /*if(status==403){
            UserHelper.clearUserInfo(UserBean.class);
            startActivity(LoginActivity.getLauncher(ChargeCaptureActivity.this));
            finish();
        }else{
            String content = TextUtils.isEmpty(scanChargeInfoBaseData.msg) ? getString(R.string.server_wrong) : scanChargeInfoBaseData.msg;
            dialog = new CommonDialog(ChargeCaptureActivity.this,getString(R.string.hint),content,1);
            dialog.show();
            dialog.setPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    finish();
                }
            });
        }*/
        String content = TextUtils.isEmpty(msg) ? getString(R.string.server_wrong) : msg;
        dialog = new CommonDialog(ChargeCaptureActivity.this,getString(R.string.hint),content,1);
        dialog.show();
        dialog.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

    }

    @Override
    public void onError() {
        hideLoading();
        dialog = new CommonDialog(ChargeCaptureActivity.this,getString(R.string.hint),getString(R.string.scan_time_out),1);
        dialog.show();
        dialog.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
    }
}
