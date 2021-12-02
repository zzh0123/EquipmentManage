package com.equipmentmanage.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.DeviceManageBean;
import com.equipmentmanage.app.bean.LoginPostBean;
import com.equipmentmanage.app.bean.LoginResultBean;
import com.equipmentmanage.app.bean.ResultInfo;
import com.equipmentmanage.app.bean.UserInfoBean;
import com.equipmentmanage.app.netapi.BaseConstant;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.GsonUtils1;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 登录页
 * @Author: zzh
 * @CreateDate: 2021/8/13
 */
public class LoginActivity extends BaseActivity {

    public static void open(Context c){
        Intent i = new Intent(c, LoginActivity.class);
        c.startActivity(i);
    }

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

    private MMKV kv = MMKV.defaultMMKV();


    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        // 20001 123456 taorui1     Aa123456!
//        etAccount.setText("ygp2021");
        etAccount.setText("wksw2021");
//        etAccount.setText("20001");
//        etAccount.setText("ygp2021");
        etPassword.setText("123456");

//        etAccount.setText("taorui1");
//        etPassword.setText("Aa123456!");
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
        LoginPostBean loginPostBean = new LoginPostBean();
//        loginPostBean.captcha ="";
//        loginPostBean.checkKey ="";
        loginPostBean.setUsername(etAccount.getText().toString().trim());
        loginPostBean.setPassword(etPassword.getText().toString().trim());

        BaseConstant.TOKEN = "";
        kv.removeValuesForKeys(new String[]{Constant.userId, Constant.username,
                Constant.realname, Constant.orgCode, Constant.orgCodeTxt, Constant.telephone,
                Constant.post});

        Subscribe.login(loginPostBean, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    BaseBean<LoginResultBean> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<LoginResultBean>>() {
                    }.getType());

                    if (baseBean != null) {
                        if (baseBean.isSuccess()) {
                            LoginResultBean loginResultBean = baseBean.getResult();
                            if (loginResultBean != null){
                                String token = loginResultBean.getToken();
                                BaseConstant.TOKEN = token;
//                                kv.encode(Constant.token, token);
//                                L.i("zzz1", "rtro token0: " + kv.decodeString(Constant.token, ""));
                                UserInfoBean userInfoBean = loginResultBean.getUserInfo();
                                if (userInfoBean != null){
                                    kv.encode(Constant.userId, userInfoBean.getId());
                                    kv.encode(Constant.username, StringUtils.nullStrToEmpty(userInfoBean.getUsername()));
                                    kv.encode(Constant.realname, StringUtils.nullStrToEmpty(userInfoBean.getRealname()));
                                    kv.encode(Constant.orgCode, StringUtils.nullStrToEmpty(userInfoBean.getOrgCode()));
                                    kv.encode(Constant.orgCodeTxt, StringUtils.nullStrToEmpty(userInfoBean.getOrgCodeTxt()));
                                    kv.encode(Constant.telephone, StringUtils.nullStrToEmpty(userInfoBean.getTelephone()));
                                    kv.encode(Constant.post, StringUtils.nullStrToEmpty(userInfoBean.getPost()));
                                }

                            }
                            Toasty.success(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT, true).show();

                            MainActivity.open(LoginActivity.this);
                            finish();
//                            DeviceManageActivity.open(LoginActivity.this);
//                            EquipmentManageActivity.open(LoginActivity.this);
                        } else {
                            Toasty.error(LoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(LoginActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(LoginActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(LoginActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();

            }
        }, LoginActivity.this));
    }

}