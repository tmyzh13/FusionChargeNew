package com.huawei.fusionchargeapp.adapter;

import android.content.Context;

import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.normal.QuickAdapter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.TaocanBean;

/**
 * Created by john on 2018/5/8.
 */

public class TaoCanAdapter extends QuickAdapter<TaocanBean> {

    public TaoCanAdapter(Context context){
        super(context, R.layout.item_tao_can);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, TaocanBean item, int position) {

    }
}
