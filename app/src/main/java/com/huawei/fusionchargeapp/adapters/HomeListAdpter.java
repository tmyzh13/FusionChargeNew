package com.huawei.fusionchargeapp.adapters;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.normal.QuickAdapter;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.MapDataBean;
import com.huawei.fusionchargeapp.views.GuildActivity;

/**
 * Created by issuser on 2018/4/19.
 */

public class HomeListAdpter extends QuickAdapter<MapDataBean> {


    public HomeListAdpter(Context context){
        super(context, R.layout.item_home_list);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final MapDataBean item, int position) {
        helper.setText(R.id.tv_name,item.title)
                .setText(R.id.tv_address,item.address)
                .setText(R.id.tv_direct,item.directNum+"")
                .setText(R.id.tv_alter,item.alterNum+"");
        TextView tv_distance=helper.getView(R.id.tv_distance);

        tv_distance.setText(item.distance+"KM");
        LinearLayout ll_guild=helper.getView(R.id.ll_guild);
        ll_guild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(GuildActivity.getLauncher(context, item.latitude, item.longitude, null, false));
            }
        });
    }
}
