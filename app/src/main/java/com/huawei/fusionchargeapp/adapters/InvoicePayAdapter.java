package com.huawei.fusionchargeapp.adapters;

import android.content.Context;
import android.widget.CheckBox;

import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.normal.QuickAdapter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.PayStyleBean;

/**
 * Created by john on 2018/5/9.
 */

public class InvoicePayAdapter extends QuickAdapter<PayStyleBean> {

    private int currentPosition=0;

    public InvoicePayAdapter(Context context){
        super(context, R.layout.item_invoice_pay);
    }

    public void setCurrentPosition(int currentPosition){
        this.currentPosition=currentPosition;
        notifyDataSetChanged();
    }
    public int getCurrentPosition(){
        return currentPosition;
    }
    @Override
    protected void convert(BaseAdapterHelper helper, PayStyleBean item, int position) {
        helper.setImageResource(R.id.img,item.imgRes)
                .setText(R.id.tv_pay,item.name)
                .setText(R.id.tv_pay_hint,item.hint);
        CheckBox cb_select=helper.getView(R.id.cb_select);
        if(currentPosition==position){
            cb_select.setChecked(true);
        }else{
            cb_select.setChecked(false);
        }
    }
}
