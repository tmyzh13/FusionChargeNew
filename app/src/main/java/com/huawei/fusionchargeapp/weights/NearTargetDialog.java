package com.huawei.fusionchargeapp.weights;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.fusionchargeapp.R;

/**
 * Created by issuser on 2018/4/24.
 */

public class NearTargetDialog extends Dialog{

    private ImageView imageView;
    private TextView tv_change;

    public NearTargetDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_near_tag);

        imageView=findViewById(R.id.iv);
        tv_change=findViewById(R.id.tv_change);

        setCanceledOnTouchOutside(false);
    }

    public void setChangerListener(View.OnClickListener listener){
        tv_change.setOnClickListener(listener);
    }

    public void setImageContent(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }
}
