package com.huawei.fusionchargeapp.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.ToastMgr;
import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.adapter.SearchHistoryOrResultAdapter;
import com.huawei.fusionchargeapp.model.UserHelper;
import com.huawei.fusionchargeapp.model.beans.MapDataBean;
import com.huawei.fusionchargeapp.presenter.HomeListPresenter;
import com.huawei.fusionchargeapp.utils.Tools;
import com.huawei.fusionchargeapp.utils.alipay.CachedSearchTitleUtils;
import com.huawei.fusionchargeapp.views.interfaces.HomeListView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.StringUtils;

/**
 * Created by issuser on 2018/4/27.
 */

public class SearchStationTitleActivity extends BaseActivity<HomeListView,HomeListPresenter> implements HomeListView{

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.tv_search_content)
    EditText tvSearchContent;
    @Bind(R.id.iv_clear)
    ImageView ivClear;
    @Bind(R.id.tv_search)
    TextView tvSearch;
    @Bind(R.id.list_search)
    ListView listSearch;
    @Bind(R.id.tv_clear_history)
    TextView clearHistory;

    public static final String KEY_ID = "id";
    public static final String  KEY_TITLE = "title";
    public static final String KEY_TYPE = "type";

    private SearchHistoryOrResultAdapter adapter;


    @OnClick(R.id.iv_back)
    public void goBack(){
       finish();
    }

    @OnClick(R.id.iv_clear)
    public void doClear(){
        tvSearchContent.setText("");
        tvSearchContent.setHint(R.string.please_input_key_value);
        ivClear.setVisibility(View.GONE);
        clearHistory.setVisibility(View.VISIBLE);
        listSearch.setDivider(null);
        adapter.resetShowHistoryData();
    }

    @OnClick(R.id.tv_search)
    public void goSearch(){
        String key=tvSearchContent.getText().toString();
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(key.trim())) {
            showToast(getString(R.string.input_can_not_be_null));
        } else {
            //信息不为空保存数据
            if (!StringUtils.isEmpty(key)) {
                CachedSearchTitleUtils.addHistoryData(new CachedSearchTitleUtils.CachedData(key, key, key));
                CachedSearchTitleUtils.saveHistoryData();
            }
            presenter.getDatas(key);
        }
    }
    @Override
    public void rendData(List<MapDataBean> list) {
        if (null == list || list.size() == 0) {
            ToastMgr.show(getString(R.string.no_search_station));
            adapter.resetShowHistoryData();
            return;
        }

        adapter.setResultData(list);
        adapter.notifyDataSetChanged();
        clearHistory.setVisibility(View.GONE);
        listSearch.setDivider(new ColorDrawable(Color.GRAY));
        listSearch.setDividerHeight(1);
    }

    @Override
    public void goLogin() {

    }

    public static Intent getLauncher(Context context) {
        return new Intent(context,SearchStationTitleActivity.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_station;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        CachedSearchTitleUtils.initHistoryData();
        tvSearchContent.setText("");
        clearHistory.setVisibility(View.VISIBLE);
        listSearch.setDivider(null);
        adapter = new SearchHistoryOrResultAdapter(SearchStationTitleActivity.this);
        adapter.resetShowHistoryData();
        listSearch.setAdapter(adapter);
        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==0){
                    return;
                }
                if (adapter.isHistoryData()){
                    tvSearchContent.setText(adapter.getIntent(position).getString(SearchStationTitleActivity.KEY_TITLE));
                    goSearch();
                }else{
                    Bundle data = adapter.getIntent(position);
                    goDetailActivity(data);
                }

            }
        });
        tvSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ivClear.getVisibility() != View.VISIBLE) {
                    ivClear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (null == s || s.toString().isEmpty()) {
                    ivClear.setVisibility(View.GONE);
                    listSearch.setDivider(null);
                    listSearch.setDividerHeight(0);
                    adapter.resetShowHistoryData();
                }
            }
        });
    }
    private void goDetailActivity(Bundle bundle){
        //go detail
        if (UserHelper.getSavedUser() == null || Tools.isNull(UserHelper.getSavedUser().token)){
            startActivity(LoginActivity.getLauncher(SearchStationTitleActivity.this));
        }else{
            Intent intent =new Intent(SearchStationTitleActivity.this,ChargeDetailsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @OnClick(R.id.tv_clear_history)
    public void clearHistory(){
        PreferencesHelper.remove(CachedSearchTitleUtils.HistoryDataBean.class);

        CachedSearchTitleUtils.resetHistoryData();
        listSearch.setDivider(null);
        listSearch.setDividerHeight(0);
        adapter.resetShowHistoryData();
    }

    @Override
    protected HomeListPresenter createPresenter() {
        return new HomeListPresenter();
    }
}
