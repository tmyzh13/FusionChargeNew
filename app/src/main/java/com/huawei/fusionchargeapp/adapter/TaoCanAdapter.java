package com.huawei.fusionchargeapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.normal.QuickAdapter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.TaocanBean;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.TaoCanPayActivity;

/**
 * Created by john on 2018/5/8.
 */

public class TaoCanAdapter extends QuickAdapter<TaocanBean> {

    public TaoCanAdapter(Context context){
        super(context, R.layout.item_tao_can);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final TaocanBean item, int position) {
        LinearLayout ll_pay=helper.getView(R.id.ll_pay);
        ll_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(TaoCanPayActivity.getLauncher(context,item.id,item.fee,item.limitType,item.name,item.limitCondition,item.startTime,item.endTime));
            }
        });
        helper.setText(R.id.tv_taocan_name,item.name)
                .setText(R.id.tv_fee,item.fee+"");
        TextView tv_validate_date =helper.getView(R.id.tv_validate_date);
        TextView tv_time=helper.getView(R.id.tv_time);
        TextView tv_limit_1=helper.getView(R.id.tv_limit_1);
        TextView tv_limit_2=helper.getView(R.id.tv_limit_2);
        TextView tv_limit_3=helper.getView(R.id.tv_limit_3);
        tv_time.setText(item.startTime.substring(0,Tools.BIRTHDAY_LENGTH)+"-"+item.endTime.substring(0,Tools.BIRTHDAY_LENGTH));
        if(item.packageType==0){
            if(item.limitType==0){
                //包电量
                tv_limit_1.setText(context.getString(R.string.my_tao_can_limit));
                tv_limit_2.setText(context.getString(R.string.my_tao_can_electronic));
                tv_limit_3.setText(item.limitCondition+context.getString(R.string.du));
            }else{
                tv_limit_1.setText(context.getString(R.string.my_tao_can_unlimit));
                tv_limit_2.setText(context.getString(R.string.my_tao_can_electronic));
                tv_limit_3.setText(context.getString(R.string.my_tao_can_month));
            }
            tv_validate_date.setVisibility(View.VISIBLE);
            tv_validate_date.setText(context.getString(R.string.my_tao_can_hint1));
        }else if(item.packageType==2){
            if(item.limitType==0){
                //包电量
                tv_limit_1.setText(context.getString(R.string.my_tao_can_limit));
                tv_limit_2.setText(context.getString(R.string.my_tao_can_electronic));
                tv_limit_3.setText(item.limitCondition+context.getString(R.string.du));
            }else{
                tv_limit_1.setText(context.getString(R.string.my_tao_can_unlimit));
                tv_limit_2.setText(context.getString(R.string.my_tao_can_electronic));
                tv_limit_3.setText(context.getString(R.string.my_tao_can_year));
            }
            tv_validate_date.setVisibility(View.VISIBLE);
            tv_validate_date.setText(context.getString(R.string.my_tao_can_hint2));
        }else if(item.packageType==1){
            tv_validate_date.setVisibility(View.GONE);
            tv_limit_1.setVisibility(View.GONE);
            tv_limit_2.setText(item.limitCondition+context.getString(R.string.du));
            tv_limit_3.setText(context.getString(R.string.my_tao_can_charge));
        }
    }
}
