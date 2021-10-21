package com.equipmentmanage.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.xuexiang.xaop.annotation.SingleClick;

/**
 * @Description: 上传
 * @Author: zzh
 * @CreateDate: 2021/10/16
 */
public class UploadActivity extends BaseActivity {

    public static void open(Context c) {
        Intent i = new Intent(c, UploadActivity.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_area_manage;
    }

    @Override
    protected void initView() {

//        titleBar.setLeftClickListener(new View.OnClickListener() {
//            @SingleClick
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initData() {
    }

}