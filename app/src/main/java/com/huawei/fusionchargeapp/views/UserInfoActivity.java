package com.huawei.fusionchargeapp.views;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.corelibs.api.ApiFactory;
import com.corelibs.base.BaseActivity;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.rxbus.RxBus;
import com.corelibs.views.roundedimageview.CircleImageView;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.apis.UploadImagesApi;
import com.huawei.fusionchargeapp.model.beans.ModifyUserInfoRequestBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.model.beans.UserInfoBean;
import com.huawei.fusionchargeapp.presenter.UserInfoPresenter;
import com.huawei.fusionchargeapp.utils.GlideImageLoader;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.UserInfoView;
import com.huawei.fusionchargeapp.weights.NavBar;
import com.huawei.fusionchargeapp.weights.UserHeadPhoteDialog;
import com.huawei.fusionchargeapp.weights.UserSexDialog;
import com.lzy.imagepicker.ImageDataSource;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.huawei.fusionchargeapp.views.ImageActivity.REQUEST_PERMISSION_ALL;


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
    TextView tvBirthday;
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

    private final int REQUEST_CODE_SELECT_CAMERA = 0x0001;

    @Bind(R.id.nav)
    NavBar navBar;
    private UserHeadPhoteDialog userHeadPhoteDialog;
    private UserSexDialog userSexDialog;

    private String uploadImageName = "";
    private long lastInputTime = 0;
    private MyHandler myHandler = new MyHandler(this);
    private boolean isGoToCarema = true;
    private String datePickerContent = "";

    public static Intent startActivity(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void init(Bundle savedInstanceState){
        navBar.setNavTitle(getResources().getString(R.string.user_info_title));
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
            navBar.setBackground(getResources().getDrawable(R.drawable.nan_bg));
        else
            navBar.setColor(getResources().getColor(R.color.app_blue));

/*
        tvEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                long currentTime = System.currentTimeMillis();
                if(lastInputTime == 0) {
                    lastInputTime = currentTime;
                    Message msg = Message.obtain();
                    msg.what = 123;
                    msg.obj = editable.toString();
                    myHandler.sendMessageDelayed(msg,2000);
                } else {
                    if(currentTime - lastInputTime < 2000) {
                        myHandler.removeCallbacksAndMessages(null);
                    }
                    lastInputTime = currentTime;
                    Message msg = Message.obtain();
                    msg.what = 123;
                    msg.obj = editable.toString();
                    myHandler.sendMessageDelayed(msg,2000);
                }

            }
        }); */

        initImagePicker();

        showLoading();
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
        imagePicker.setShowCamera(false);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setMultiMode(false); //单选
        imagePicker.setSelectLimit(1);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(400);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(400);                        //保存文件的高度。单位像素
    }


    //获取已输入的个人信息
    private ModifyUserInfoRequestBean getUserInfoRequest() {
        //姓名不能为空
        if(Tools.isNull(etNick.getText().toString())){
            showToast(getString(R.string.name_cannot_null));
            return null;
        }
        //性别不能为空
        if(Tools.isNull(tvSex.getText().toString())) {
            showToast(getString(R.string.sex_cannot_null));
            return null;
        }
        //生日不能为空
        if(Tools.isNull(tvBirthday.getText().toString())) {
            showToast("请选择出生日期");
            return null;
        }
       /* if(Tools.isNull(tvEmail.getText().toString())) {
            showToast("邮箱不能为空！");
        }*/
       //vin码校验，长度为17位
        if (!Tools.isNull(tvCarVin.getText().toString()) && tvCarVin.getText().toString().length() != 17) {
            showToast("车辆识别号码必须是17位字母和数字组合");
            return null;
        }

        ModifyUserInfoRequestBean bean = new ModifyUserInfoRequestBean();
        if(!Tools.isNull(uploadImageName)) {
            bean.photoUrl = uploadImageName;
        }
        bean.name = etNick.getText().toString();
        bean.sexName = tvSex.getText().toString();
        bean.email = tvEmail.getText().toString();
        bean.birth = tvBirthday.getText().toString();
        if (bean.birth.length() > Tools.BIRTHDAY_LENGTH) {
            bean.birth = bean.birth.substring(0,Tools.BIRTHDAY_LENGTH);
        }
        bean.address = tvAddress.getText().toString();
        bean.vinCode = tvCarVin.getText().toString();
        return bean;
    }

    @Override
    public void onGetUserInfoSuccess(UserInfoBean userInfoBean) {
        hideLoading();
        // 更新界面

        //头像
        SimpleTarget<GlideDrawable> target = new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                ivUserIcon.setImageDrawable(resource);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                String path = PreferencesHelper.getData(Tools.USER_PHOTO_PATH);
                if (!TextUtils.isEmpty(path)) {
                    File file = new File(path);
                    Glide.with(UserInfoActivity.this).load(Uri.fromFile(file))
                            .error(R.mipmap.ic_launcher_round).into(ivUserIcon);
                }
            }
        };
        if (!TextUtils.isEmpty(userInfoBean.photoUrl)) {
            Glide.with(this).load(userInfoBean.photoUrl).into(target);
        }

        //保存用户信息
        UserBean userBean = UserHelper.getSavedUser();

        userBean.photoUrl = userInfoBean.photoUrl;
        userBean.name = userInfoBean.name;
        userBean.sex = userInfoBean.sex;
        userBean.sexName = userInfoBean.sexName;
        userBean.email = userInfoBean.email;
        userBean.birthDay = userInfoBean.birth;
        userBean.address = userInfoBean.address;
        userBean.vinCode = userInfoBean.vinCode;

        UserHelper.saveUser(userBean);

        // 姓名
        if (!TextUtils.isEmpty(userBean.name)) {
            etNick.setText(userBean.name);
        }else{
            etNick.setText(userBean.phone);
        }
        //性别 1为男，2为女，0为未知
        tvSex.setText(userBean.sexName);

        // 邮箱
        if (!TextUtils.isEmpty(userBean.email)) {
            tvEmail.setText(userBean.email);
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
        hideLoading();
        Toast.makeText(this, getString(R.string.user_info_get_fail), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onModifyUserInfoFail() {
        hideLoading();
        Toast.makeText(this, getString(R.string.user_info_modify_fail), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onModifySuccess() {
        hideLoading();
        Toast.makeText(this, getString(R.string.user_info_modify_success), Toast.LENGTH_SHORT).show();
        startActivity(MainActivity.getLauncher(UserInfoActivity.this));
        // 提交成功再请求获取用户信息接口
        //presenter.doGetUserInfoRequest();
    }

    @Override
    public void onUploadPhotoSuccess(String imgUrl) {
        uploadImageName =imgUrl;
        UserBean userBean = UserHelper.getSavedUser();
        if(userBean != null) {
            userBean.photoUrl = uploadImageName;
            UserHelper.saveUser(userBean);
            //通知主页去更新头像
            //RxBus.getDefault().send(new Object(), Constant.REFRESH_MAIN_HEAD_PHOTO);
        }
    }

    @Override
    public void onUploadPhotoFail() {
        showToast(getString(R.string.upload_photo_error));
    }


    @OnClick({R.id.tv_sex, R.id.tv_birthday, R.id.commit_userinfo,R.id.iv_user_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_user_icon:
                //头像选择
                if(userHeadPhoteDialog == null ){
                    userHeadPhoteDialog = new UserHeadPhoteDialog(UserInfoActivity.this);
                }
                userHeadPhoteDialog.show();
                userHeadPhoteDialog.setCameraListener(this);
                userHeadPhoteDialog.setPhotoListener(this);
                break;
            case R.id.tv_sex:
                // 性别选择单选框
                if(userSexDialog == null ) {
                    userSexDialog = new UserSexDialog(this);
                }
                if(!userSexDialog.isShowing()) {
                    userSexDialog.show();
                    userSexDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()){
                                case R.id.tv_man:
                                    tvSex.setText(getString(R.string.man));
                                    userSexDialog.dismiss();
                                    break;
                                case R.id.tv_woman:
                                    tvSex.setText(getString(R.string.woman));
                                    userSexDialog.dismiss();
                                    break;
                                case R.id.tv_secret:
                                    tvSex.setText(getString(R.string.secret));
                                    userSexDialog.dismiss();
                                    break;
                                case R.id.tv_cancel:
                                    userSexDialog.dismiss();
                                    break;
                            }
                        }
                    });
                }
                break;
            case R.id.tv_birthday:
                // 生日日期选择 上传要求格式：1993.04.05
                showTimePickerDialog();
                break;
            case R.id.commit_userinfo:
                //验证邮箱
                if (!isEmailVailid()) {
                    return;
                }
                // 修改个人信息
                ModifyUserInfoRequestBean userInfoRequest = getUserInfoRequest();
                if(userInfoRequest != null) {
                    showLoading();
                    presenter.doModifyUserInfo(userInfoRequest);
                }
                break;
            case R.id.tv_camera:
                isGoToCarema = true;
                userHeadPhoteDialog.dismiss();
                boolean isGo = checkPermisson();
                if(!isGo) return;
                //相机拍照
                Intent intent = new Intent(this, ImageActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                startActivityForResult(intent, REQUEST_CODE_SELECT_CAMERA);
                break;
            case R.id.tv_photo:
                isGoToCarema = false;
                userHeadPhoteDialog.dismiss();
                boolean isGo1 = checkPermisson();
                if(!isGo1) return;
                //从相册选取
                Intent intent1 = new Intent(this, ImageActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_SELECT_CAMERA);

                break;
        }
    }

    private boolean isEmailVailid() {
        if (null == tvEmail.getText() || tvEmail.getText().toString().isEmpty()) {
            showToast("邮箱不能为空！");
            return false;
        }
        Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher matcher = pattern.matcher(tvEmail.getText().toString());
        boolean result = matcher.matches();
        setEmail(result);
        return result;
    }

    private boolean checkPermisson(){
        if (!(checkPermission(Manifest.permission.CAMERA)) || !(checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_ALL);
            return false;
        }
        return true;
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_ALL) {
            if(grantResults.length <2 || grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED){
                showToast(getString(R.string.permission_denied));
                return;
            } else {
                if(isGoToCarema) {
                    //相机拍照
                    Intent intent = new Intent(this, ImageActivity.class);
                    intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                    startActivityForResult(intent, REQUEST_CODE_SELECT_CAMERA);
                    userHeadPhoteDialog.dismiss();
                } else {
                    //从相册选取
                    Intent intent1 = new Intent(this, ImageActivity.class);
                    startActivityForResult(intent1, REQUEST_CODE_SELECT_CAMERA);
                    userHeadPhoteDialog.dismiss();
                }
            }

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
                    Uri uri = Uri.fromFile(file);
                    Log.e("zw","log .to string : " + uri.toString() + " .. " + file.exists());
                    //上传名称
                    String[] imgPath = uri.toString().split("/");
                    uploadImageName = "img/" + imgPath[imgPath.length - 1];
                    Log.e("zw","log .to string : " + uploadImageName);

                    Glide.with(this).load(Uri.fromFile(file))
                            .into(ivUserIcon);
                    PreferencesHelper.saveData(Tools.USER_PHOTO_PATH,images.get(0).path);
                    presenter.uploadImage(file);
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showTimePickerDialog(){
        View popupView = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.double_date_picker,null);
        DatePicker start_time,end_time;
        TextView tv_start,tv_end;
        start_time = (DatePicker) popupView.findViewById(R.id.start_time);
        end_time = (DatePicker) popupView.findViewById(R.id.end_time);
        tv_start = (TextView) popupView.findViewById(R.id.tv_start_time);
        tv_end = (TextView) popupView.findViewById(R.id.tv_end_time);

        end_time.setVisibility(View.GONE);
        tv_end.setVisibility(View.GONE);
        tv_start.setVisibility(View.GONE);

        int year,monthOfYear,dayOfMonth;
        if (TextUtils.isEmpty(tvBirthday.getText())) {
            Calendar calendar = Calendar.getInstance();
            year=calendar.get(Calendar.YEAR);
            monthOfYear=calendar.get(Calendar.MONTH);
            dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
            tvBirthday.setText(getDateFromYMD(year,monthOfYear,dayOfMonth));
        } else {
            String[] date = tvBirthday.getText().toString().split("-|\\.");
            year = Integer.parseInt(date[0]);
            monthOfYear = Integer.parseInt(date[1]) - 1;
            dayOfMonth = Integer.parseInt(date[2]);
        }

        start_time.setMaxDate(Calendar.getInstance().getTimeInMillis());
        start_time.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                datePickerContent = getDateFromYMD(i,i1,i2);
            }
        });
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(popupView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!Tools.isNull(datePickerContent)) {
                            tvBirthday.setText(datePickerContent);
                            datePickerContent = "";
                        }
                    }
                }).create();
        dialog.show();
    }

    private String getDateFromYMD(int i,int i1,int i2){
        String str = i + ".";
        if (i1 < 9){
            str = str + "0"+(i1+1)+".";
        } else {
            str = str + (i1+1) +".";
        }
        if (i2 < 10) {
            str = str + "0" + i2;
        } else {
            str = str + i2;
        }
    ;    return str;
    }


    private void setEmail(boolean isRight) {
        if(!isRight) {
            tvEmail.setText("");
            myHandler.removeCallbacksAndMessages(null);
            showToast(getString(R.string.wrong_email_address));
        }
    }

    private static class MyHandler extends Handler {

        WeakReference<UserInfoActivity> userInfoActivity;

        public MyHandler(UserInfoActivity activity){
            userInfoActivity = new WeakReference<UserInfoActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 123) {
                String mail = (String) msg.obj;
                Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
                Matcher matcher = pattern.matcher(mail);

                if(matcher.matches()) {
                    userInfoActivity.get().setEmail(true);
                }else {
                    userInfoActivity.get().setEmail(false);
                }
            }
        }
    }
}
