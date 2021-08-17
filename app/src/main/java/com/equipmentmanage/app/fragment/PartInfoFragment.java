package com.equipmentmanage.app.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.LazyFragment;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import butterknife.BindView;

/**
 * @Description: 组件信息Fragment
 * @Author: zzh
 * @CreateDate: 2021/8/17
 */
public class PartInfoFragment extends LazyFragment {

    @BindView(R.id.tv_device_name)
    TextView tvDeviceName; //装置名称

    @BindView(R.id.tv_area_name)
    TextView tvAreaName; //区域名称

    @BindView(R.id.tv_equipment_name)
    TextView tvQquipmentName; //装置名称


    public PartInfoFragment() {
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
        return R.layout.fragment_part_info;
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