package com.huawei.fusionchargeapp.views;

import android.os.Bundle;
import android.widget.EditText;

import com.corelibs.base.BaseActivity;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.SuggestionBean;
import com.huawei.fusionchargeapp.presenter.SuggestionPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.SuggestionView;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2018/5/8.
 */

public class SuggestionActivity extends BaseActivity<SuggestionView,SuggestionPresenter> implements SuggestionView {
    @Bind(R.id.nav_bar)
    NavBar bar;
    @Bind(R.id.edit_suggestion)
    EditText edit_suggestion;

    @Override
    public void CommitSucess() {
        startActivity(MainActivity.getLauncher(this));
    }

    @Override
    public void goLogin() {
        startActivity(LoginActivity.getLauncher(this));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_suggestion;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bar.setColorRes(R.color.blue);
        bar.setNavTitle(getString(R.string.setting_advice));
    }

    @OnClick(R.id.publish)
    void commitSuggestion(){
        if (Tools.isNull(edit_suggestion.getText().toString())) {
            showToast(getString(R.string.advice_is_null));
            return;
        }
        SuggestionBean bean = new SuggestionBean();
        bean.appUserId = UserHelper.getSavedUser().appUserId;
        bean.feedbackContent = edit_suggestion.getText().toString();
        presenter.commit(bean);
    }

    @Override
    protected SuggestionPresenter createPresenter() {
        return new SuggestionPresenter();
    }
}
