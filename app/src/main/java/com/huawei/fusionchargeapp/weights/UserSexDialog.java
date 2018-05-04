package com.huawei.fusionchargeapp.weights;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.huawei.fusionchargeapp.R;
import com.huawei.fusionchargeapp.utils.Tools;

/**
 * Created by zhangwei on 2018/5/4.
 */

public class UserSexDialog extends Dialog {

    private Context context;

    public UserSexDialog(@NonNull Context context) {
        super(context, R.style.MyDialog1);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_sex_dialog);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        lp.x = 0; // 新位置X坐标
//        lp.y = -20; // 新位置Y坐标
        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels - Tools.dip2px(context,60); // 宽度
//      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//      lp.alpha = 9f; // 透明度
//        root.measure(0, 0);
//        lp.height = root.getMeasuredHeight();
//        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        TextView tvMan = findViewById(R.id.tv_man);
        TextView tvWoman = findViewById(R.id.tv_woman);
        TextView tvSecert = findViewById(R.id.tv_secret);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        tvMan.setOnClickListener(listener);
        tvWoman.setOnClickListener(listener);
        tvSecert.setOnClickListener(listener);
        tvCancel.setOnClickListener(listener);
    }


}
