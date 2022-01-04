package com.equipmentmanage.app.fragment;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.activity.AreaManageActivity;
import com.equipmentmanage.app.activity.DeviceManageActivity;
import com.equipmentmanage.app.activity.EquipmentManageActivity;
import com.equipmentmanage.app.activity.LoginActivity;
import com.equipmentmanage.app.activity.MainActivity;
import com.equipmentmanage.app.activity.ProductFlowActivity;
import com.equipmentmanage.app.base.LazyFragment;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.LoginPostBean;
import com.equipmentmanage.app.bean.LoginResultBean;
import com.equipmentmanage.app.bean.UserInfoBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.ActivityCollector;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.equipmentmanage.app.view.TipDialog;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 用户Fragment
 * @Author: zzh
 * @CreateDate: 2021/8/13
 */
public class UserFragment extends LazyFragment {

    @BindView(R.id.iv_header)
    RadiusImageView ivHeader; //头像

    @BindView(R.id.tv_user_name)
    TextView tvUserName; //用户名

    @BindView(R.id.tv_user_role)
    TextView tvUserRole; //岗位角色

    @BindView(R.id.cstl_change_password)
    ConstraintLayout cstlChangePassword; //修改密码

    @BindView(R.id.cstl_iv_clear_cache)
    ConstraintLayout cstl_iv_clear_cache; //清空缓存

    @BindView(R.id.tv_exit)
    TextView tvExit; //退出登录

    private MMKV kv = MMKV.defaultMMKV();
    private String userName, role;

    private TipDialog tipDialog;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static TaskFragment newInstance(String param1, String param2) {
//        TaskFragment fragment = new TaskFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    @Override
    protected int getContentViewId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initView(View view) {
        userName = kv.decodeString(Constant.username, "");
        role = kv.decodeString(Constant.post, "");
        tvUserName.setText(StringUtils.nullStrToEmpty(userName));
        tvUserRole.setText(StringUtils.nullStrToEmpty(role));

        tipDialog = new TipDialog(getActivity());
        tipDialog.setOnConfirmListener(new TipDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                // 清空缓存
                clearCahce();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void showUploadDialog() {
        if (tipDialog == null) {
            tipDialog = new TipDialog(getActivity());
        }
        tipDialog.show();
        tipDialog.setTitleAndTip(null, getString(R.string.clear_cache_tip));
    }

    @OnClick({R.id.tv_exit, R.id.cstl_iv_clear_cache})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exit:
                logout();
                break;

            case R.id.cstl_iv_clear_cache:
                showUploadDialog();
                break;

        }
    }

    private void clearCahce() {
        // 任务缓存
        int row0 = AppDatabase.getInstance(getActivity()).taskTableDao().deleteAllByUser("1" + userName);
        // 装置
        int row1 = AppDatabase.getInstance(getActivity()).baseDeviceTableDao().deleteAll();
        // 区域
        int row2 = AppDatabase.getInstance(getActivity()).baseAreaTableDao().deleteAll();
        // 设备
        int row3 = AppDatabase.getInstance(getActivity()).baseEquipmentTableDao().deleteAll();
        // 产品流
        int row4 = AppDatabase.getInstance(getActivity()).baseStreamTableDao().deleteAll();
//        L.i("zzz1-del-row0-->" + row0);
//        L.i("zzz1-del-row1-->" + row1);
//        L.i("zzz1-del-row2-->" + row2);
//        L.i("zzz1-del-row3-->" + row3);
//        L.i("zzz1-del-row4-->" + row4);
        EventBus.getDefault().post(ConstantValue.event_clear_cache);
        if ((row0 < 0) || (row1 < 0) || (row2 < 0) || (row3 < 0) || (row4 < 0)){
            Toasty.error(getActivity(), "清空缓存失败！", Toast.LENGTH_SHORT, true).show();
        } else {
            Toasty.success(getActivity(), "清空缓存成功！", Toast.LENGTH_SHORT, true).show();
        }
    }

    /**
     * 登出
     */
    private void logout() {
        LoginPostBean loginPostBean = new LoginPostBean();
//        loginPostBean.captcha ="";
//        loginPostBean.checkKey ="";
//        loginPostBean.setUsername(etAccount.getText().toString().trim());
//        loginPostBean.setPassword(etPassword.getText().toString().trim());

//        kv.removeValuesForKeys(new String[]{Constant.token, Constant.userId, Constant.username,
//                Constant.realname, Constant.orgCode, Constant.orgCodeTxt, Constant.telephone,
//                Constant.post});

        Subscribe.logout(loginPostBean, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    BaseBean<LoginResultBean> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<LoginResultBean>>() {
                    }.getType());

                    if (baseBean != null) {
                        if (baseBean.isSuccess()) {
                            kv.removeValuesForKeys(new String[]{Constant.isLogin});

                            Toasty.success(getActivity(), R.string.logout_success, Toast.LENGTH_SHORT, true).show();
                            ActivityCollector.finishAll();
                            LoginActivity.open(getActivity());
                        } else {
                            Toasty.error(getActivity(), R.string.logout_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(getActivity(), R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(getActivity(), R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(getActivity(), R.string.request_fail, Toast.LENGTH_SHORT, true).show();

            }
        }, getActivity()));
    }

}