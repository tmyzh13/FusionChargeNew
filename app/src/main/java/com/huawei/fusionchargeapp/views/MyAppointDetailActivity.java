package com.huawei.fusionchargeapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.utils.ToastMgr;
import com.corelibs.views.cube.ptr.PtrFrameLayout;
import com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout;
import com.corelibs.views.ptr.loadmore.widget.AutoLoadMoreListView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.AppointmentListAdapter;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.AllAppointmentResultBean;
import com.huawei.fusionchargeapp.model.beans.HomeAppointmentBean;
import com.huawei.fusionchargeapp.model.beans.HomeChargeOrderBean;
import com.huawei.fusionchargeapp.model.beans.HomeOrderBean;
import com.huawei.fusionchargeapp.model.beans.MapDataBean;
import com.huawei.fusionchargeapp.model.beans.MapInfoBean;
import com.huawei.fusionchargeapp.model.beans.PileFeeBean;
import com.huawei.fusionchargeapp.model.beans.RawRecordBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.AllAppointmentPresenter;
import com.huawei.fusionchargeapp.presenter.MapPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.AllAppointmentView;
import com.huawei.fusionchargeapp.views.interfaces.MapHomeView;
import com.huawei.fusionchargeapp.views.interfaces.MyOrderActivity;
import com.huawei.fusionchargeapp.weights.NavBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2018/5/3.
 */

public class MyAppointDetailActivity extends BaseActivity<AllAppointmentView,AllAppointmentPresenter> implements AllAppointmentView, DatePicker.OnDateChangedListener{
    @Bind(R.id.lv_order)
    AutoLoadMoreListView lvOrder;
    @Bind(R.id.ptrLayout)
    PtrAutoLoadMoreLayout ptrLayout;
    @Bind(R.id.tv_appointment)
    TextView tv_appointment;
    @Bind(R.id.nav)
    NavBar navBar;


    private AppointmentListAdapter appointmentListAdapter;
    private List<AllAppointmentResultBean> data = new ArrayList<>();
    private int page = 0;
    private static final int PAGE_LIMIT_NUM = 10;
    private String startTime,endTime;

    @Override
    public void goLogin() {
        ToastMgr.show(getString(R.string.login_fail));
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(MyAppointDetailActivity.this));
    }
    public static Intent getLauncher(Context context) {
        Intent intent = new Intent(context, MyAppointDetailActivity.class);
        return intent;
    }
    @Override
    public void getAppointmentInfo(List<AllAppointmentResultBean> bean) {
        if (!noData(bean)){
            if (page == 0) {
                data = bean;
            } else {
                data.addAll(bean);
            }
            appointmentListAdapter.setData(data);
            appointmentListAdapter.notifyDataSetChanged();
        } else if (null != startTime && page == 0){
            data = null;
            appointmentListAdapter.setData(data);
            appointmentListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
        if (datePicker.getId() == R.id.start_time) {
            startTime = getTimeFromYMD(i,i1,i2);
        }
        if (datePicker.getId() == R.id.end_time) {
            endTime = getTimeFromYMD(i,i1,i2);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_appoint_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setColorRes(R.color.app_blue);
        navBar.setNavTitle("我的预约");
        presenter.getAppointmentInfo(page,PAGE_LIMIT_NUM);
        tv_appointment.setVisibility(View.VISIBLE);
        tv_appointment.setText("筛选");

        appointmentListAdapter = new AppointmentListAdapter(this,data);
        lvOrder.setAdapter(appointmentListAdapter);

        ptrLayout.setRefreshLoadCallback(new PtrAutoLoadMoreLayout.RefreshLoadCallback() {
            //上拉
            @Override
            public void onLoading(PtrFrameLayout frame) {
                page ++;
                getInfo();
            }

            //下拉
            @Override
            public void onRefreshing(PtrFrameLayout frame) {
                page = 0;
                ptrLayout.enableLoading();
                if (!frame.isAutoRefresh()) {
                    getInfo();
                }
            }
        });
        if(UserHelper.getSavedUser()==null|| Tools.isNull(UserHelper.getSavedUser().token)){
            startActivity(LoginActivity.getLauncher(this));
            return;
        }
        showLoading();
        presenter.getAppointmentInfo(page,PAGE_LIMIT_NUM);
    }

    private void getInfo() {
        if (null == startTime || startTime.length() <= 0) {
            presenter.getAppointmentInfo(page,PAGE_LIMIT_NUM);
        } else {
            presenter.getAppointmentInfoWithTimeCondition(page,PAGE_LIMIT_NUM,startTime,endTime);
        }
    }

    @OnClick(R.id.tv_appointment)
    void doAppointmentFind(){
        View popupView = LayoutInflater.from(MyAppointDetailActivity.this).inflate(R.layout.double_date_picker,null);
        DatePicker start_time,end_time;
        TextView tv_ok;
        start_time = (DatePicker) popupView.findViewById(R.id.start_time);
        end_time = (DatePicker) popupView.findViewById(R.id.end_time);

        Calendar calendar = Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        startTime = endTime = getTimeFromYMD(year,monthOfYear,dayOfMonth);

        start_time.init(year,monthOfYear,dayOfMonth,this);
        end_time.init(year,monthOfYear,dayOfMonth,this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(popupView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        page = 0;
                        presenter.getAppointmentInfoWithTimeCondition(page,PAGE_LIMIT_NUM,startTime,endTime);
                    }
                }).create();
        dialog.show();
    }

    private String getTimeFromYMD(int year,int month,int day){
        String str = year + "-";
        if (month<9) {
            str = str +"0"+(month+1)+"-";
        } else {
            str = str +(month+1)+"-";
        }
        if (day < 10) {
            str = str +"0"+day;
        }else {
            str = str + day;
        }
        return str;
    }

    @Override
    protected AllAppointmentPresenter createPresenter() {
        return new AllAppointmentPresenter();
    }

    @Override
    public void getInfoFail(boolean state) {
        if (null != startTime && page == 0) {
            data = null;
            appointmentListAdapter.setData(data);
            appointmentListAdapter.notifyDataSetChanged();
        }
        if (page > 0) {
            page--;
        }
    }

    public boolean noData(List<AllAppointmentResultBean> bean) {
        if (null == bean || bean.isEmpty()){
            String str;
            if (page == 0) {
                str = null == startTime ? "没有预约信息" : "没有满足条件的预约信息";
            } else {
                str = null==startTime ? "没有更多预约信息" : "没有更多满足条件的预约信息";
            }
            showToast(str);
            return true;
        }
        return false;
    }

    @Override
    public void showLoading() {
        ptrLayout.setRefreshing();
    }

    @Override
    public void hideLoading() {
        ptrLayout.complete();
    }

}
