package com.huawei.fusionchargeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.model.beans.CommentsBean;
import com.huawei.fusionchargeapp.utils.Tools;

import java.util.List;

import retrofit2.http.Url;

/**
 * Created by issuser on 2018/4/23.
 */

public class CommentsAdapter extends BaseAdapter {
    private List<CommentsBean> datas;
    private LayoutInflater mInflater;
    private Context context;

    public CommentsAdapter(Context context, List<CommentsBean> datas) {
        this.context = context;
        this.datas = datas;
        mInflater = LayoutInflater.from(context);
    }
    public void  setData(List<CommentsBean> datas){
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentsBean bean = datas.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_comments_list, null);
            holder.headIv = convertView.findViewById(R.id.commenter_head_iv);
            holder.name = convertView.findViewById(R.id.commenter_name_tv);
            holder.time = convertView.findViewById(R.id.commenter_time_tv);
            holder.content = convertView.findViewById(R.id.commenter_content_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(bean.getUserName());
        holder.time.setText(bean.getCreateTime().substring(0,bean.getCreateTime().length()-2));
        holder.content.setText(bean.getEvaluateContent());
        if (!Tools.isNull(bean.photoUrl)) {
            Glide.with(context).load(bean.photoUrl).into(holder.headIv);
        }

        return convertView;
    }

    public class ViewHolder {
        private ImageView headIv;
        private TextView name;
        private TextView time;
        private TextView content;
    }
}
