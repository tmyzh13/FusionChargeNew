package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.weights.NavBar;



import butterknife.Bind;

/**
 * Created by john on 2018/6/3.
 */

public class WebActivity extends BaseActivity {

    @Bind(R.id.nav)
    NavBar nav;
    @Bind(R.id.web)
    WebView web;
    @Bind(R.id.progress)
    ProgressBar progress;

    private Context context=WebActivity.this;
    private int type;

    public static Intent getLauncher(Context context, int type){
        Intent intent=new Intent(context,WebActivity.class);
        intent.putExtra("type",type);
        return intent;
    }
    private WebViewClient mClient=new WebViewClient(){

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    };

    private WebChromeClient mChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            setNewProgress(newProgress);
            super.onProgressChanged(view, newProgress);

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {

            super.onReceivedTitle(view, title);
        }
    };

    private void setNewProgress(int newProgress){
        progress.setVisibility(View.VISIBLE);
        progress.setProgress(newProgress);
        if(newProgress >= 100)
            progress.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        //web.goBack();
        super.onBackPressed();
    }
    @Override
    public void goLogin() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        type=getIntent().getIntExtra("type",0);
        if(type==0){
            nav.setNavTitle(getString(R.string.copy_right));
        }
        nav.setImageBackground(R.drawable.nan_bg);
        web.setWebViewClient(mClient);
        web.setWebChromeClient(mChromeClient);

        // webview必须设置支持Javascript才可打开
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setSupportZoom(true);
        // 设置此属性,可任意比例缩放
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        web.getSettings().setLoadWithOverviewMode(true);
        //通过在线预览office文档的地址加载
        web.loadUrl("file:///android_asset/test.html");

    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

}
