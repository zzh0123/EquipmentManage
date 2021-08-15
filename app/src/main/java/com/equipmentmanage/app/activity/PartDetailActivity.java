package com.equipmentmanage.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;

/**
 * @Description: 组件详情
 * @Author: zzh
 * @CreateDate: 2021/8/15
 */
public class PartDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_detail);
    }
}