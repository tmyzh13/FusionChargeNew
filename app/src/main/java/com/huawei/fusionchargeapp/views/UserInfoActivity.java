package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.corelibs.base.BaseActivity;
import com.corelibs.views.roundedimageview.CircleImageView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.ModifyUserInfoRequestBean;
import com.huawei.fusionchargeapp.model.beans.UserInfoBean;
import com.huawei.fusionchargeapp.presenter.UserInfoPresenter;
import com.huawei.fusionchargeapp.views.interfaces.UserInfoView;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserInfoActivity extends BaseActivity<UserInfoView, UserInfoPresenter> implements UserInfoView, View.OnClickListener {
    private static final String TAG = UserInfoActivity.class.getSimpleName();
    @Bind(R.id.iv_user_icon)
    CircleImageView ivUserIcon;
    @Bind(R.id.layout_portrait)
    LinearLayout layoutPortrait;
    @Bind(R.id.et_nick)
    EditText etNick;
    @Bind(R.id.layout_nick)
    LinearLayout layoutNick;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.layout_sex)
    LinearLayout layoutSex;
    @Bind(R.id.tv_email)
    EditText tvEmail;
    @Bind(R.id.layout_email)
    LinearLayout layoutEmail;
    @Bind(R.id.tv_birthday)
    EditText tvBirthday;
    @Bind(R.id.layout_birthday)
    LinearLayout layoutBirthday;
    @Bind(R.id.tv_address)
    EditText tvAddress;
    @Bind(R.id.layout_address)
    LinearLayout layoutAddress;
    @Bind(R.id.tv_car_vin)
    EditText tvCarVin;
    @Bind(R.id.layout_car_vin)
    LinearLayout layoutCarVin;
    @Bind(R.id.commit_userinfo)
    TextView commitUserinfo;

    private UserInfoPresenter presenter;

    @Bind(R.id.nav)
    NavBar navBar;
    public static Intent startActivity(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setNavTitle(getResources().getString(R.string.user_info_title));
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
            navBar.setBackground(getResources().getDrawable(R.drawable.nan_bg));
        else
            navBar.setColor(getResources().getColor(R.color.app_blue));

        presenter = getPresenter();
        presenter.doGetUserInfoRequest();
    }

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter();
    }

    @Override
    public void goLogin() {

    }


    private ModifyUserInfoRequestBean getUserInfoRequest() {
        ModifyUserInfoRequestBean bean = new ModifyUserInfoRequestBean();
        bean.name = etNick.getText().toString();
        bean.sexName = tvSex.getText().toString();
        bean.email = tvEmail.getText().toString();
        bean.birth = tvBirthday.getText().toString();
        bean.address = tvAddress.getText().toString();
        bean.vinCode = tvCarVin.getText().toString();
        return bean;
    }

    @Override
    public void onGetUserInfoSuccess(UserInfoBean userInfoBean) {
        // 更新界面

        //头像
        Glide.with(this).load(userInfoBean.photoUrl).into(ivUserIcon);

        // 姓名
        if (!TextUtils.isEmpty(userInfoBean.name)) {
            etNick.setText(userInfoBean.name);
        }
        //性别 1为男，2为女，0为未知
        tvSex.setText(userInfoBean.sexName);

        // 邮箱
        if (!TextUtils.isEmpty(userInfoBean.email)) {
            tvEmail.setText(userInfoBean.email);
        }
        // 出生日期
        if (!TextUtils.isEmpty(userInfoBean.birth)) {
            tvBirthday.setText(userInfoBean.birth);
        }
        // 地址
        if (!TextUtils.isEmpty(userInfoBean.address)) {
            tvAddress.setText(userInfoBean.address);
        }
        // 汽车VIN
        if (!TextUtils.isEmpty(userInfoBean.vinCode)) {
            tvCarVin.setText(userInfoBean.vinCode);
        }
    }

    @Override
    public void onGetUsrInfoFail() {
        Toast.makeText(this, getString(R.string.user_info_get_fail), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onModifyUserInfoFail() {
        Toast.makeText(this, getString(R.string.user_info_modify_fail), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onModifySuccess() {
        Toast.makeText(this, getString(R.string.user_info_modify_success), Toast.LENGTH_SHORT).show();
        // 提交成功再请求获取用户信息接口
        presenter.doGetUserInfoRequest();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_sex, R.id.tv_birthday, R.id.commit_userinfo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sex:
                // 性别选择单选框

                break;
            case R.id.tv_birthday:
                // 生日日期选择 上传要求格式：1993-05-20

                break;
            case R.id.commit_userinfo:
                // 修改个人信息
                ModifyUserInfoRequestBean userInfoRequest = getUserInfoRequest();
                presenter.doModifyUserInfo(userInfoRequest);
                break;
        }
    }
}
