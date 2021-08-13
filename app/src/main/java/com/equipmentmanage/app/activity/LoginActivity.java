package com.equipmentmanage.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.utils.StringUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 登录页
 * @Author: zzh
 * @CreateDate: 2021/8/13
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_account)
    EditText etAccount; //账号

    @BindView(R.id.imb_clear_account)
    ImageButton imbClearAccount; //清除账号

    @BindView(R.id.et_password)
    EditText etPassword; //密码

    @BindView(R.id.imb_clear_password)
    ImageButton imbClearPassword; //清除密码

    @BindView(R.id.tv_login)
    TextView tvLogin; //登录


    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.toString().length() > 0) {
                    imbClearAccount.setVisibility(View.VISIBLE);
                } else {
                    imbClearAccount.setVisibility(View.INVISIBLE);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.toString().length() > 0) {
                    imbClearPassword.setVisibility(View.VISIBLE);
                } else {
                    imbClearPassword.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.tv_login, R.id.imb_clear_account, R.id.imb_clear_password})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                if (valid()){
                    login();
                }
                break;
            case R.id.imb_clear_account:
                etAccount.getText().clear();
                break;
            case R.id.imb_clear_password:
                etPassword.getText().clear();
                break;
        }
    }

    @Override
    protected void initData() {

    }

    private boolean valid() {

        if (StringUtils.isNullString(etAccount.getText().toString().trim())) {
            Toasty.warning(LoginActivity.this, R.string.account_tip, Toast.LENGTH_SHORT, true).show();
            return false;
        }

        if (StringUtils.isNullString(etPassword.getText().toString().trim())) {
            Toasty.warning(LoginActivity.this, R.string.password_tip, Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    private void login(){

    }
}