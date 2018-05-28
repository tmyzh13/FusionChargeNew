package com.huawei.fusionchargeapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.huawei.fusionchargeapp.MainActivity;
import com.huawei.fusionchargeapp.adapter.CommentSortAdapter;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.CommentSortBean;
import com.huawei.fusionchargeapp.model.beans.CommentsBean;
import com.huawei.fusionchargeapp.model.beans.PayInfoBean;
import com.huawei.fusionchargeapp.model.beans.PublishCommentsBean;
import com.huawei.fusionchargeapp.presenter.CommentPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.views.interfaces.CommentView;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.weights.NavBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.OnClick;

/**
 * Created by issuser on 2018/4/25.
 */

public class PublishCommentActivity extends BaseActivity<CommentView,CommentPresenter> implements CommentView {
    @Bind(R.id.charge_location_name)
    TextView locationName;
    @Bind(R.id.charge_location_detail)
    TextView locationDetail;
    @Bind(R.id.charge_time_start)
    TextView startTime;
    @Bind(R.id.charge_time_end)
    TextView endTime;
    @Bind(R.id.charge_info_kw)
    TextView power;
    @Bind(R.id.charge_info_money)
    TextView money;
    @Bind(R.id.charge_info_tips)
    TextView tips;
    @Bind(R.id.charge_info_total)
    TextView total;
    @Bind(R.id.comment_detail_edit)
    EditText edit;
    @Bind(R.id.comment_detail_show)
    TextView show;
    @Bind(R.id.favor_lever)
    RatingBar favor;
    @Bind(R.id.publish_comment)
    Button publish;

    @Bind(R.id.flow_layout)
    GridView flowGrid;
    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @BindString(R.string.charger_info_fee_unit)
    String fee_unit;
    @BindString(R.string.charge_info_unit_kw)
    String unit_kw;
    private PublishCommentsBean bean = new PublishCommentsBean();
    private Context mContext = PublishCommentActivity.this;
    private int sort = 0;
    private List<CommentSortBean> list= new ArrayList<>();
    private CommentSortAdapter adapter;
    private long pileId =1;

    @Override
    public void commentPublished() {
        show.setText(edit.getText());
        edit.setVisibility(View.GONE);
        show.setVisibility(View.VISIBLE);
        publish.setVisibility(View.GONE);
        sort = 0;
        favor.setIsIndicator(true);
        flowGrid.setOnItemClickListener(null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_comment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setNavTitle(getString(R.string.activity_publish));
        navBar.setImageBackground(R.drawable.nan_bg);
        sort = 0;
        presenter.queryCommentSortType();
        presenter.getPayDetailInfo(orderRecordNum);
        adapter = new CommentSortAdapter(mContext,list,true);
        flowGrid.setAdapter(adapter);
        flowGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                makeContentSort(view,position);
            }
        });
        //init the TextView content
    }

    private static String orderRecordNum ="1524387088804002";
    public static Intent getLauncher(Context context, String orderRecord) {
        Intent intent = new Intent(context, PublishCommentActivity.class);
        orderRecordNum = orderRecord;
        return intent;
    }

    @Override
    protected CommentPresenter createPresenter() {
        return new CommentPresenter();
    }

    @OnClick(R.id.publish_comment)
    public void publish() {
        //get data
        if (Tools.isNull(edit.getText().toString())) {
            showToast(R.string.no_comment_content);
            return;
        }
        if (favor.getRating() < 0.5) {
            showToast(R.string.no_comment_score);
            return;
        }
        bean.setEvaluateScore(favor.getRating());
        bean.setEvaluateContent(edit.getText().toString());
        bean.setEvaluateTypeId(getSortType());
        bean.setOrderRecordNum(orderRecordNum);
        bean.setPileId(pileId);
        bean.setUserId(UserHelper.getSavedUser().appUserId);

        presenter.publish(bean);
    }

    @Override
    public void getCommentSortAndTimes(List<CommentSortBean> times) {
    }

    @Override
    public void queryCommentInfo(List<CommentsBean> infos) {
    }

    @Override
    public void queryCommentSortType(List<CommentSortBean> sorts) {
        list = sorts;
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    public void makeContentSort(View view, int position){
        int num =(int) Math.pow(2,position);
        if ((sort & num) > 0) {
            sort =sort - num;
            setTextAndCornerStokeColor((TextView)view, false);
        } else {
            sort =sort +num;
            setTextAndCornerStokeColor((TextView)view, true);
        }

    }

    private String getSortType() {
        if(list.size()==0 || sort == 0){
            return "0";
        }
        StringBuilder result = new StringBuilder();
        int num = (int) Math.pow(2,list.size()-1);
        int i=1;
        int step =1;
        while(step < num ) {
            if ((sort & step) >0 ) {
                result.append(i).append(",");
            }
            i++;
            step = step *2;
        }
        if ((sort & num) >0 ) {
            result.append(i);
        } else {
            result.deleteCharAt(result.length()-1);
        }
        return result.toString();
    }
    private void setTextAndCornerStokeColor(TextView view,boolean singleClick) {
        if (singleClick) {
            view.setTextColor(getResources().getColor(R.color.blue));
            view.setBackgroundResource(R.drawable.blue_stroke_corner_fill_white);
        } else {
            view.setTextColor(getResources().getColor(R.color.text_gray));
            view.setBackgroundResource(R.drawable.grey_stroke_corner_fill_white);
        }
    }

    @Override
    @OnClick(R.id.iv_back)
    public void onBackPressed() {
        if (edit.getVisibility() != View.VISIBLE) {
            mContext.startActivity(MainActivity.getLauncher(mContext));
            finish();
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(PublishCommentActivity.this)
                .setMessage(getString(R.string.cancel_publish_toast))
                .setNegativeButton(getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton(getString(R.string.action_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            mContext.startActivity(MainActivity.getLauncher(mContext));
            finish();
        }}).create();
        dialog.show();
    }

    @Override
    public void goLogin() {
    }

    //private PayInfoBean payInfoBean = new PayInfoBean();
    @Override
    public void renderData(PayInfoBean bean) {
        //payInfoBean = bean;
        refreshView(bean);
    }

    private void refreshView(PayInfoBean bean){
        if (bean == null) {
            return;
        }
        locationName.setText(bean.parkAddress);
        locationDetail.setText(bean.address);
        startTime.setText(bean.chargeStartTime);
        endTime.setText(bean.chargeEndTime);
        power.setText(Double.toString(bean.chargePowerAmount)+unit_kw);
        money.setText(fee_unit+Double.toString(bean.eneryCharge));
        tips.setText(fee_unit+Double.toString(bean.serviceCharge));
        total.setText(fee_unit+Double.toString(bean.consumeTotalMoney));
        pileId = bean.chargeId;
    }
}