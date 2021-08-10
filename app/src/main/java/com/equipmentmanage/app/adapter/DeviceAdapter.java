package com.equipmentmanage.app.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.DeviceBean;
import com.equipmentmanage.app.utils.StringUtils;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 装置管理-adapter
 * @Author: zzh
 * @CreateDate: 2021/8/10
 */
public class DeviceAdapter extends BaseQuickAdapter<DeviceBean, BaseViewHolder> {
    public DeviceAdapter(@Nullable List<DeviceBean> data) {
        super(R.layout.item_device, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable DeviceBean bean) {
        holder.setText(R.id.tv_device_name, StringUtils.nullStrToEmpty(bean.getName())); // 装置名称
        holder.setText(R.id.tv_status, StringUtils.nullStrToEmpty(bean.getStatus())); // 状态
        holder.setText(R.id.tv_device_code, StringUtils.nullStrToEmpty(bean.getCode())); // 装置编码
        holder.setText(R.id.tv_department_name, StringUtils.nullStrToEmpty(bean.getDepartment())); // 部门
        holder.setText(R.id.tv_device_type, StringUtils.nullStrToEmpty(bean.getType())); // 装置类型
        holder.setText(R.id.tv_test_start_date, StringUtils.nullStrToEmpty(bean.getDate())); // 开始检测日期

        TextView tvStatus = holder.getView(R.id.tv_status);
//        if (status1.equals("待拣货")){
////            tv_status.setTextColor(getContext().getResources().getColor(R.color.text_color_red));
//        } else {
////            tv_status.setTextColor(getContext().getResources().getColor(R.color.green_00B38B));
//        }
//
//        SuperButton sb_pick_complete = holder.getView(R.id.sb_pick_complete);
//        // 是否是当前人，后台已判断 & displayButton 展示拣货完成按钮
//        boolean displayButton = bean.isDisplayButton();
//        if (displayButton){
//            sb_pick_complete.setVisibility(View.VISIBLE);
//        } else {
//            sb_pick_complete.setVisibility(View.GONE);
//        }

    }
}
