package com.huawei.fusionchargeapp.views.mycount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.utils.ToastMgr;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by john on 2018/5/9.
 */

public class InputContentActivity extends BaseActivity {

    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.edit_suggestion)
    EditText edit_suggestion;

    private String data;

    public static Intent getLauncher(Context context,String data){
        Intent intent=new Intent(context,InputContentActivity.class);
        intent.putExtra("data",data);
        return intent;
    }

    @Override
    public void goLogin() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_input_content;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setImageBackground(R.drawable.nan_bg);

        data=getIntent().getStringExtra("data");
        if(!Tools.isNull(data)){
            edit_suggestion.setText(data);
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick(R.id.publish)
    public void submitContent(){
        if(Tools.isNull(edit_suggestion.getText().toString())){
            ToastMgr.show(R.string.hint_input_content);
            return;
        }
        Intent intent =new Intent();
        intent.putExtra("content",edit_suggestion.getText().toString().trim());
        setResult(100,intent);
        finish();
    }
}
