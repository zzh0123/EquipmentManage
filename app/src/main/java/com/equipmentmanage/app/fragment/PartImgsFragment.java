package com.equipmentmanage.app.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.LazyFragment;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import butterknife.BindView;

/**
 * @Description: 组件图片Fragment
 * @Author: zzh
 * @CreateDate: 2021/8/17
 */
public class PartImgsFragment extends LazyFragment {

    @BindView(R.id.iv_part)
    ImageView ivPart; //组件图片

    @BindView(R.id.read_value)
    TextView readValue; //读数

    @BindView(R.id.tv_reread)
    TextView tvReread; //重新读数

    @BindView(R.id.tv_last)
    TextView tvLast; //上一个

    @BindView(R.id.tv_next)
    TextView tvNext; //下一个

    @BindView(R.id.progress_bar)
    ProgressBar progressBar; //下一个

    public PartImgsFragment() {
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
        return R.layout.fragment_part_imgs;
    }

    @Override
    protected void initView(View view) {
        progressBar.setProgress(40);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }


}