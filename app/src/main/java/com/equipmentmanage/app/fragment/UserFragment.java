package com.equipmentmanage.app.fragment;

import android.view.View;

import com.equipmentmanage.app.R;

/**
 * @Description: 用户Fragment
 * @Author: zzh
 * @CreateDate: 2021/8/13
 */
public class UserFragment extends LazyFragment {


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