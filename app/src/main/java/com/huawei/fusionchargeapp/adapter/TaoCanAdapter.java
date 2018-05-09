package com.huawei.fusionchargeapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.normal.QuickAdapter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.TaocanBean;
import com.huawei.fusionchargeapp.views.TaoCanPayActivity;

/**
 * Created by john on 2018/5/8.
 */

public class TaoCanAdapter extends QuickAdapter<TaocanBean> {

    public TaoCanAdapter(Context context){
        super(context, R.layout.item_tao_can);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, TaocanBean item, int position) {
        LinearLayout ll_pay=helper.getView(R.id.ll_pay);
        ll_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(TaoCanPayActivity.getLauncher(context));
            }
        });
    }
}
