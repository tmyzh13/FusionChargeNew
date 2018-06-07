package com.huawei.fusionchargeapp.views.mycount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.utils.ToastMgr;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.constants.Constant;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.MyTaocanBean;
import com.huawei.fusionchargeapp.model.beans.UserBean;
import com.huawei.fusionchargeapp.presenter.MyTaocanPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.LoginActivity;
import com.huawei.fusionchargeapp.views.interfaces.MyTaocanView;
import com.huawei.fusionchargeapp.weights.NavBar;

import butterknife.Bind;

/**
 * Created by issuser on 2018/5/17.
 */

public class MyTaoCanActivity extends BaseActivity<MyTaocanView,MyTaocanPresenter> implements MyTaocanView {

    @Bind(R.id.nav)
    NavBar nav;
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Bind(R.id.ll_surplus)
    LinearLayout ll_surplus;
    @Bind(R.id.tv_surpluse)
    TextView tv_surpluse;
    @Bind(R.id.tv_buy_time)
    TextView tv_buy_time;
    @Bind(R.id.tv_end_time)
    TextView tv_end_time;
    @Bind(R.id.tv_hint_1)
    TextView tv_limit_1;
    @Bind(R.id.tv_hint_2)
    TextView tv_limit_2;
    @Bind(R.id.tv_hint_3)
    TextView tv_limit_3;
    @Bind(R.id.taocan_linear)
    LinearLayout taocan_linear;


    private Context context=MyTaoCanActivity.this;

    public static Intent getLauncher(Context context){
        Intent intent =new Intent(context,MyTaoCanActivity.class);
        return intent;
    }

    @Override
    public void goLogin() {
        UserHelper.clearUserInfo(UserBean.class);
        startActivity(LoginActivity.getLauncher(context));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_tao_can;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        nav.setNavTitle(getString(R.string.my_tao_can));
        nav.setImageBackground(R.drawable.nan_bg);
        presenter.getMyTaoCan();
    }

    @Override
    protected MyTaocanPresenter createPresenter() {
        return new MyTaocanPresenter();
    }

    @Override
    public void renderMyTaoCan(MyTaocanBean bean) {
        if(bean==null){
            ToastMgr.show(R.string.no_tao_can);
            //taocan_linear.setVisibility(View.GONE);
        }else{
            taocan_linear.setVisibility(View.VISIBLE);
            tv_name.setText(bean.businessName);
            if(!Tools.isNull(bean.startTime)){
                tv_buy_time.setText(bean.startTime.replace("-","/"));
            }
           if(!Tools.isNull(bean.endTime)){
                tv_end_time.setText(bean.endTime.replace("-","/").substring(0,Tools.DATE_LENGTH_FROM_SERVER));
           }
            if(bean.packageType==0){
                if(bean.limitType==0){
                    //包电量
                    tv_limit_1.setText(context.getString(R.string.my_tao_can_limit));
                    tv_limit_2.setText(context.getString(R.string.my_tao_can_electronic));
                    tv_limit_3.setText(bean.limitCondition+context.getString(R.string.du));
                    ll_surplus.setVisibility(View.VISIBLE);
                    tv_surpluse.setText(bean.businessRemain+context.getString(R.string.du));
                }else{
                    tv_limit_1.setText(context.getString(R.string.my_tao_can_unlimit));
                    tv_limit_2.setText(context.getString(R.string.my_tao_can_electronic));
                    tv_limit_3.setText(context.getString(R.string.my_tao_can_month));
                    ll_surplus.setVisibility(View.GONE);
                }
            }else if(bean.packageType==2){
                if(bean.limitType==0){
                    //包电量
                    tv_limit_1.setText(context.getString(R.string.my_tao_can_limit));
                    tv_limit_2.setText(context.getString(R.string.my_tao_can_electronic));
                    tv_limit_3.setText(bean.limitCondition+context.getString(R.string.du));
                    ll_surplus.setVisibility(View.VISIBLE);
                    tv_surpluse.setText(bean.businessRemain+context.getString(R.string.du));
                }else{
                    tv_limit_1.setText(context.getString(R.string.my_tao_can_unlimit));
                    tv_limit_2.setText(context.getString(R.string.my_tao_can_electronic));
                    tv_limit_3.setText(context.getString(R.string.my_tao_can_year));
                    ll_surplus.setVisibility(View.GONE);
                }
            }else if(bean.packageType==1){
                tv_limit_1.setVisibility(View.GONE);
                tv_limit_2.setText(bean.limitCondition+context.getString(R.string.du));
                tv_limit_3.setText(context.getString(R.string.my_tao_can_charge));
                ll_surplus.setVisibility(View.VISIBLE);
                tv_surpluse.setText(bean.businessRemain+context.getString(R.string.du));
            }
        }
    }
}
