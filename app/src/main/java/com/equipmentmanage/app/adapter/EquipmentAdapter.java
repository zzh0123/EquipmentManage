package com.equipmentmanage.app.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.DeviceManageBean;
import com.equipmentmanage.app.bean.EquipmentManageBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 设备管理-adapter
 * @Author: zzh
 * @CreateDate: 2021/8/12
 */
public class EquipmentAdapter extends BaseQuickAdapter<EquipmentManageBean, BaseViewHolder> {
    public EquipmentAdapter(@Nullable List<EquipmentManageBean> data) {
        super(R.layout.item_equipment_manage, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable EquipmentManageBean bean) {
        holder.setText(R.id.tv_equipment_name, StringUtils.nullStrToEmpty(bean.getName())); // 装置名称
        holder.setText(R.id.tv_status, StringUtils.nullStrToEmpty(bean.getStatus())); // 状态
        holder.setText(R.id.tv_device_code, StringUtils.nullStrToEmpty(bean.getCode())); // 装置编码
        holder.setText(R.id.tv_belong_device, StringUtils.nullStrToEmpty(bean.getBelongEquipment())); // 所属装置
        holder.setText(R.id.tv_belong_area, StringUtils.nullStrToEmpty(bean.getBelongArea())); // 所属区域

        TextView tvStatus = holder.getView(R.id.tv_status);
        String status = bean.getStatus();
        if (status.equals("1")){
            tvStatus.setText(R.string.enabled_1);
            tvStatus.setTextColor(getContext().getResources().getColor(R.color.c_67C23A));
            tvStatus.setBackgroundResource(R.drawable.bg_rec_green);
        } else {
            tvStatus.setText(R.string.enabled_0);
            tvStatus.setTextColor(getContext().getResources().getColor(R.color.c_FFC0C4CC));
            tvStatus.setBackgroundResource(R.drawable.bg_rec_grey);
        }

    }
}
