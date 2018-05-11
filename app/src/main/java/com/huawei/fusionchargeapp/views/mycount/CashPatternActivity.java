package com.huawei.fusionchargeapp.views.mycount;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.CashPatternAdapter;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.OnClick;

/**
 * Created by admin on 2018/5/11.
 */

public class CashPatternActivity extends BaseActivity {
    @Bind(R.id.nav_bar)
    NavBar bar;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.cash_num)
    EditText cashNum;
    @Bind(R.id.submit)
    Button submit;
    @Bind(R.id.cach_pattern)
    TextView cashPattern;

    @BindString(R.string.my_acount_recharge)
    String recharge;
    @BindString(R.string.my_acount_put_forward)
    String recover;

    public static final String KEY_CASH_OPERATION_PATTER = "key_cash_operation_pattern";
    private boolean isRechargeType = false;
    private CashPatternAdapter adapter;


    @Override
    public void goLogin() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash_pattern;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bar.setColorRes(R.color.blue);
        isRechargeType = getIntent().getBooleanExtra(KEY_CASH_OPERATION_PATTER, false);
        bar.setNavTitle(isRechargeType ? recharge : recover);
        cashPattern.setText(getString(R.string.cash_select_pattern, isRechargeType ? recharge : recover));
        cashNum.setHint(getString(R.string.cash_input_with_pattern, isRechargeType ? recharge : recover));
        submit.setText(isRechargeType ? recharge : recover);

        adapter = new CashPatternAdapter(this);
        listView.setAdapter(adapter);
    }

    @OnClick(R.id.cach_pattern)
    void submit(){
        //提交
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
