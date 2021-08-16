package com.equipmentmanage.app.fragment;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.LazyFragment;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import butterknife.BindView;

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

    @BindView(R.id.tv_exit)
    TextView tvExit; //退出登录

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
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

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }


}