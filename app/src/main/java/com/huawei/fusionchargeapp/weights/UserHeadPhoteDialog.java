package com.huawei.fusionchargeapp.weights;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.huawei.fusionchargeapp.R;

/**
 * Created by zhangwie on 2018/5/4.
 */

public class UserHeadPhoteDialog extends Dialog{

    private TextView tvCamera;
    private TextView tvPhoto;

    public UserHeadPhoteDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_upload_user_phote);

        tvCamera = findViewById(R.id.tv_camera);
        Log.e("zw","tvcam1 : " + (tvCamera == null  ));

        tvPhoto = findViewById(R.id.tv_photo);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public void setCameraListener(View.OnClickListener listener){
        Log.e("zw","tvcam : " + (tvCamera == null  ));

        tvCamera.setOnClickListener(listener);
    }

    public void setPhotoListener(View.OnClickListener listener) {
        tvPhoto.setOnClickListener(listener);
    }
}
