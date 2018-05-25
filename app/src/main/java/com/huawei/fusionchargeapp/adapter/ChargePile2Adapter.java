package com.huawei.fusionchargeapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.ChargeDetailFeeBean;
import com.huawei.fusionchargeapp.model.beans.ChargeDetailFeeListBean;
import com.huawei.fusionchargeapp.model.beans.ChargeMultipleBean;
import com.huawei.fusionchargeapp.model.beans.ChargeStationDetailBean;
import com.huawei.fusionchargeapp.model.beans.GunList;
import com.huawei.fusionchargeapp.model.beans.PileList;

import java.util.List;

/**
 * Created by zhangwei on 2018/5/24.
 */

public class ChargePile2Adapter extends BaseMultiItemQuickAdapter<ChargeMultipleBean,BaseViewHolder>{

    private Context mContext;

    public ChargePile2Adapter(Context context,List<ChargeMultipleBean> data) {
        super(data);
        this.mContext = context;
        addItemType(ChargeMultipleBean.CHARGE_DETAIL_HEAD,R.layout.item_charge_detail_header);
        addItemType(ChargeMultipleBean.CHARGE_DETAIL_BODY,R.layout.item_charge_detail_body);
    }


    @Override
    protected void convert(BaseViewHolder helper, ChargeMultipleBean item) {
        switch (helper.getItemViewType()) {
            case ChargeMultipleBean.CHARGE_DETAIL_HEAD:
                if(helper.getLayoutPosition() == 0) {
                    helper.setGone(R.id.header_view,false);
                }

                PileList pileList = item.getmPileList();
                ChargeDetailFeeBean feeBean = item.getmFeeBean();
                int statusRes = pileList.getRunStatus() == 1 ? R.mipmap.charge_on : R.mipmap.charge_off;
                helper.setImageResource(R.id.pile_status_iv,statusRes);

                //获取充电价格和服务费
                helper.setText(R.id.service_fee,feeBean.getServiceFee() + mContext.getString(R.string.yuan_du));
                List<ChargeDetailFeeListBean> feeListBean = feeBean.getFeeList();
                if( feeListBean != null) {
                    double max = 0;
                    double min = 0;
                    for (int i = 0; i < feeListBean.size(); i++) {
                        double temp = feeListBean.get(i).getMultiFee();
                        if(temp < min) {
                            min = temp;
                        }
                        if(temp > max) {
                            max = temp;
                        }
                    }
                    helper.setText(R.id.charging_fee,min + mContext.getString(R.string.yuan_du) + "~" + max + mContext.getString(R.string.yuan_du));
                }

                helper.setText(R.id.pile_num_tv,pileList.getRunCode());
                helper.setText(R.id.max_power_tv,pileList.getMaxPower() + "KM");
                helper.setText(R.id.max_electronic_tv,pileList.getMaxCurrent() + "A");
                helper.setText(R.id.max_voltage_tv,pileList.getMaxVoltage() + "V");
                helper.setText(R.id.pile_status_tv,pileList.getRunStatus() == 1 ?
                        mContext.getString(R.string.statue_offline) : mContext.getString(R.string.statue_online));

                break;
            case ChargeMultipleBean.CHARGE_DETAIL_BODY:
                GunList gunList = item.getmGunList();

                //type=1  交流
                if (gunList.getGunType() == 1) {
                    helper.setImageResource(R.id.gun_type_iv,R.drawable.dots_green);
                    helper.setText(R.id.gun_type_tv,mContext.getString(R.string.statue_alternating_electronic));
                } else if (gunList.getGunType() == 2) {
                    helper.setImageResource(R.id.gun_type_iv,R.drawable.dots_yellow);
                    helper.setText(R.id.gun_type_tv,mContext.getString(R.string.statue_direct_electronic));
                }
//                helper.addOnClickListener(R.id.gun_appointment_tv);
                // 充电枪状态 1：空闲 2：使用中（插枪未充电） 3：使用中（已充电） 4：预约中 5：停止服务 6：故障
                if (gunList.getGunStatus() == 1) {
                    helper.setImageResource(R.id.gun_status_iv,R.drawable.dots_green);
                    helper.setText(R.id.gun_status_tv,mContext.getString(R.string.statue_free));

                    //判断剩余时间 有remindtime说明轮冲 也不能预约 并且显示剩余时间
                    if(gunList.getRemainingTime()>0){
                        helper.setTextColor(R.id.gun_appointment_tv,mContext.getResources().getColor(R.color.text_gray));
                        helper.setBackgroundRes(R.id.gun_appointment_tv,R.drawable.appoint_gray_bg_shape);
                        helper.setGone(R.id.tv_remindtime_hint,true);
                        helper.setText(R.id.tv_remindtime_hint,mContext.getString(R.string.tv_remindtime_hint_1)
                                + gunList.getRemainingTime()+mContext.getString(R.string.tv_remindtime_hint_2));
                    }else{
                        helper.setGone(R.id.tv_remindtime_hint,false);
                        helper.addOnClickListener(R.id.gun_appointment_tv);
                        helper.setTextColor(R.id.gun_appointment_tv,mContext.getResources().getColor(R.color.app_blue));
                        helper.setBackgroundRes(R.id.gun_appointment_tv,R.drawable.blue_stroke_90angle_bg);
                    }
                } else if (gunList.getGunStatus() == 2) {
                    helper.setGone(R.id.tv_remindtime_hint,false);
                    helper.setImageResource(R.id.gun_status_iv,R.drawable.dots_fault_red);
                    helper.setText(R.id.gun_status_tv,mContext.getString(R.string.gun_not_using));
                    helper.setTextColor(R.id.gun_appointment_tv,mContext.getResources().getColor(R.color.text_gray));
                    helper.setBackgroundRes(R.id.gun_appointment_tv,R.drawable.appoint_gray_bg_shape);
                } else if (gunList.getGunStatus() == 3) {
                    helper.setGone(R.id.tv_remindtime_hint,false);
                    helper.setImageResource(R.id.gun_status_iv,R.drawable.dots_yellow);
                    helper.setText(R.id.gun_status_tv,mContext.getString(R.string.useing));
                    helper.setTextColor(R.id.gun_appointment_tv,mContext.getResources().getColor(R.color.text_gray));
                    helper.setBackgroundRes(R.id.gun_appointment_tv,R.drawable.appoint_gray_bg_shape);
                } else if (gunList.getGunStatus() == 4 ) {
                    helper.setGone(R.id.tv_remindtime_hint,false);
                    helper.setImageResource(R.id.gun_status_iv,R.drawable.dots_fault_red);
                    helper.setText(R.id.gun_status_tv,mContext.getString(R.string.appoingment_ing));
                    helper.setTextColor(R.id.gun_appointment_tv,mContext.getResources().getColor(R.color.text_gray));
                    helper.setBackgroundRes(R.id.gun_appointment_tv,R.drawable.appoint_gray_bg_shape);
                } else if (gunList.getGunStatus() == 5 ) {
                    helper.setGone(R.id.tv_remindtime_hint,false);
                    helper.setImageResource(R.id.gun_status_iv,R.drawable.dots_fault_red);
                    helper.setText(R.id.gun_status_tv,mContext.getString(R.string.stop_server));
                    helper.setTextColor(R.id.gun_appointment_tv,mContext.getResources().getColor(R.color.text_gray));
                    helper.setBackgroundRes(R.id.gun_appointment_tv,R.drawable.appoint_gray_bg_shape);
                } else if (gunList.getGunStatus() == 6 ) {
                    helper.setGone(R.id.tv_remindtime_hint,false);
                    helper.setImageResource(R.id.gun_status_iv,R.drawable.dots_fault_red);
                    helper.setText(R.id.gun_status_tv,mContext.getString(R.string.status_fault));
                    helper.setTextColor(R.id.gun_appointment_tv,mContext.getResources().getColor(R.color.text_gray));
                    helper.setBackgroundRes(R.id.gun_appointment_tv,R.drawable.appoint_gray_bg_shape);
                }
                helper.setText(R.id.electric_gun_num,gunList.getGunNumber());

                break;
        }
    }
}
