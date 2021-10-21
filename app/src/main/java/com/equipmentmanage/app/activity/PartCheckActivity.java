package com.equipmentmanage.app.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.TaskBean;
import com.equipmentmanage.app.fragment.PartImgsFragment;
import com.equipmentmanage.app.fragment.PartInfoFragment;
import com.equipmentmanage.app.netapi.Constant;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description: 组件检测
 * @Author: zzh
 * @CreateDate: 2021/8/17
 */
public class PartCheckActivity extends BaseActivity {

    public static void open(Context c, TaskBean bean) {
        Intent i = new Intent(c, PartCheckActivity.class);
        i.putExtra(Constant.taskBean, bean);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.ll_part_info) //组件信息
    public LinearLayout llPartInfo;

    @BindView(R.id.tv_part_info) //组件信息
    public TextView tvPartInfo;

    @BindView(R.id.tv_part_info_indicator) //组件信息指示器
    public TextView tvPartInfoIndicator;

    @BindView(R.id.ll_part_imgs) //组件图片
    public LinearLayout llPartImgs;

    @BindView(R.id.tv_part_imgs) //组件图片
    public TextView tvPartImgs;

    @BindView(R.id.tv_part_imgs_indicator) //组件图片指示器
    public TextView tvPartImgsIndicator;

    @BindView(R.id.viewPager2) //viewPager2
    public ViewPager2 viewPager2;
    private FragmentStateAdapter fragmentStateAdapter;

    private String[] tabTitles = {"组件信息", "组件图片"};
    private List<Fragment> fragmentList = new ArrayList<>();

    private TaskBean bean;

    @Override
    protected int initLayout() {
        return R.layout.activity_part_check;
    }

    @Override
    protected void initView() {
        bean = (TaskBean) getIntent().getSerializableExtra(Constant.taskBean);

//        bean = (AreaManageResultBean.Records) getIntent().getSerializableExtra(Constant.areaBean);

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        if (bean != null) {
//            tvAreaName.setText(StringUtils.nullStrToEmpty(bean.getAreaName())); //名称
//            tvAreaCode.setText(StringUtils.nullStrToEmpty(bean.getAreaCode())); //编号
//            tvBelongDevice.setText(StringUtils.nullStrToEmpty(bean.getBelongDevice_dictText())); //所属装置
////            tvEnabled.setText(StringUtils.nullStrToEmpty(bean.getDeviceName())); //是否可用
//        }


    }

    @Override
    protected void initEvent() {
        fragmentList.add(new PartInfoFragment());
        fragmentList.add(new PartImgsFragment());

        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                change(position);
            }
        });

        fragmentStateAdapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Fragment fragment = fragmentList.get(position);
                Bundle bundle=new Bundle();
                bundle.putSerializable(Constant.taskBean, bean);
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        };

        viewPager2.setAdapter(fragmentStateAdapter);
        viewPager2.setOffscreenPageLimit(1);

    }

    /**
     * 改变背景颜色,背景图片
     *
     * @param position 选中的tab项的位置
     */
    private void change(int position) {
        switch (position) {
            case 0:
                tvPartInfoIndicator.setVisibility(View.VISIBLE);
                tvPartImgsIndicator.setVisibility(View.INVISIBLE);
                tvPartInfo.setTextColor(getResources().getColor(R.color.c_303133));
                tvPartImgs.setTextColor(getResources().getColor(R.color.c_909399));
                break;

            case 1:
                tvPartInfoIndicator.setVisibility(View.INVISIBLE);
                tvPartImgsIndicator.setVisibility(View.VISIBLE);
                tvPartInfo.setTextColor(getResources().getColor(R.color.c_909399));
                tvPartImgs.setTextColor(getResources().getColor(R.color.c_303133));
                break;

        }
    }


    @Override
    protected void initData() {
//        refresh();
    }

    @OnClick({R.id.ll_part_info, R.id.ll_part_imgs})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_part_info:
                change(0);
                viewPager2.setCurrentItem(0);
                break;
            case R.id.ll_part_imgs:
                change(1);
                viewPager2.setCurrentItem(1);
                break;
        }
    }

}