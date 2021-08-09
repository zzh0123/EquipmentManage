package com.equipmentmanage.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.ResultInfo;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_result) //绑定tv_result控件
    public TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getUserList();

//        getUserList();

    }

    private void getUserList() {
        Subscribe.getUserList(new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                Log.i("result--->", result);
                //成功
                Toast.makeText(MainActivity.this, "请求成功！", Toast.LENGTH_SHORT).show();
                ResultInfo resultInfo = GsonUtils.fromJson(result,
                        ResultInfo.class);
                tvResult.setText(result);
            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toast.makeText(MainActivity.this, "请求失败：" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        }, MainActivity.this));
    }
}