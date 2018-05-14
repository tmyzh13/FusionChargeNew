package com.huawei.fusionchargeapp.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.common.AppManager;
import com.huawei.fusionchargeapp.App;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.R;

/**
 * Created by issuser on 2018/4/27.
 */

public class WelcomeActivity extends BaseActivity {

    public static final String W3_ACCOUNT = "w3_account";
    public static final String W3_Phone = "w3_phone";

    private String w3Account;
    private String w3Phone;
    public static final int REQUEST_EXTERNAL_STRONGE = 0x00888;

    @Override
    public void goLogin() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent fromIntent = getIntent();
        if(fromIntent != null) {
            w3Account = fromIntent.getStringExtra(W3_ACCOUNT);
            w3Phone = fromIntent.getStringExtra(W3_Phone);
        }

        //检查有无存储权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STRONGE);
        } else {
            jump();
        }

    }

    private void jump(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.putExtra(w3Account,"");
                intent.putExtra(w3Phone,"");
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && requestCode == REQUEST_EXTERNAL_STRONGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            jump();
        } else {
            finish();
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
