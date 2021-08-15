package com.equipmentmanage.app.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.DeviceManageBean;
import com.equipmentmanage.app.bean.DeviceManageResultBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 装置管理-adapter
 * @Author: zzh
 * @CreateDate: 2021/8/10
 */
public class DeviceAdapter extends BaseQuickAdapter<DeviceManageResultBean.Records, BaseViewHolder> {
    public DeviceAdapter(@Nullable List<DeviceManageResultBean.Records> data) {
        super(R.layout.item_device_manage, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable DeviceManageResultBean.Records bean) {
        holder.setText(R.id.tv_device_name, StringUtils.nullStrToEmpty(bean.getDeviceName())); // 装置名称
//        holder.setText(R.id.tv_status, StringUtils.nullStrToEmpty(bean.getStatus())); // 状态
        holder.setText(R.id.tv_device_code, StringUtils.nullStrToEmpty(bean.getDeviceCode())); // 装置编码
//        holder.setText(R.id.tv_department_name, StringUtils.nullStrToEmpty(bean.getDepartment())); // 部门
        holder.setText(R.id.tv_device_type, StringUtils.nullStrToEmpty(bean.getDeviceType_dictText())); // 装置类型
        holder.setText(R.id.tv_test_start_date, StringUtils.nullStrToEmpty(bean.getTestSdate())); // 开始检测日期

        TextView tvStatus = holder.getView(R.id.tv_status);
//        String status = bean.getStatus();
//        if (status.equals("1")){
            tvStatus.setText(R.string.enabled_1);
            tvStatus.setTextColor(getContext().getResources().getColor(R.color.c_67C23A));
            tvStatus.setBackgroundResource(R.drawable.bg_rec_green);
//        } else {
//            tvStatus.setText(R.string.enabled_0);
//            tvStatus.setTextColor(getContext().getResources().getColor(R.color.c_FFC0C4CC));
//            tvStatus.setBackgroundResource(R.drawable.bg_rec_grey);
//        }

    }
}
