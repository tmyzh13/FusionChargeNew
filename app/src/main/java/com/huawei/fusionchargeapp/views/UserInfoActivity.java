package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import com.huawei.fusionchargeapp.utils.GlideImageLoader;
import com.huawei.fusionchargeapp.views.interfaces.UserInfoView;
import com.huawei.fusionchargeapp.weights.NavBar;
import com.huawei.fusionchargeapp.weights.UserHeadPhoteDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final int REQUEST_CODE_SELECT_CAMERA = 0x0001;

    @Bind(R.id.nav)
    NavBar navBar;
    private UserHeadPhoteDialog dialog;

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


        tvEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
                Matcher matcher = pattern.matcher(tvEmail.getText().toString());

                if(matcher.matches()) {

                }else {
                    tvEmail.setText("");
                    showToast(getString(R.string.wrong_email_address));
                }
                return false;
            }
        });

        initImagePicker();

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

    private void initImagePicker(){
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());//设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(3);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(400);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(400);                        //保存文件的高度。单位像素
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
        Glide.with(this).load(userInfoBean.photoUrl).error(R.mipmap.ic_launcher_round).into(ivUserIcon);

        // 姓名
        if (!TextUtils.isEmpty(userInfoBean.name)) {
            etNick.setText(userInfoBean.name);
        }
        //性别 1为男，2为女，0为未知
        Log.e("zw","sex ； " + userInfoBean.sexName);
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


    @OnClick({R.id.tv_sex, R.id.tv_birthday, R.id.commit_userinfo,R.id.iv_user_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_user_icon:
                //头像选择
                if(dialog == null ){
                    dialog = new UserHeadPhoteDialog(UserInfoActivity.this);
                }
                dialog.show();
                dialog.setCameraListener(this);
                break;
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
            case R.id.tv_camera:
                //相机拍照
                Intent intent = new Intent(this, ImageActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                startActivityForResult(intent, REQUEST_CODE_SELECT_CAMERA);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_SELECT_CAMERA) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if(images != null && images.size() != 0) {
                    File file = new File(images.get(0).path);
                    Glide.with(this).load(file).placeholder(R.mipmap.ic_launcher_round)
                            .into(ivUserIcon);
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
