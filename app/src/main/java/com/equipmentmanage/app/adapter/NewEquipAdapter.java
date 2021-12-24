package com.equipmentmanage.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.NewAreaBean;
import com.equipmentmanage.app.bean.NewEquipBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 新增设备-adapter
 * @Author: zzh
 * @CreateDate: 2021/12/11
 */
public class NewEquipAdapter extends BaseQuickAdapter<NewEquipBean, BaseViewHolder> {
    public NewEquipAdapter(@Nullable List<NewEquipBean> data) {
        super(R.layout.item_new_common, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable NewEquipBean bean) {
        holder.setText(R.id.tv_code, StringUtils.nullStrToEmpty(bean.getCode())); // 设备编码
        holder.setText(R.id.tv_name, StringUtils.nullStrToEmpty(bean.getName())); // 设备名称

        boolean isSelected = bean.isSelected();
        if (isSelected){
            holder.setTextColor(R.id.tv_code, getContext().getResources().getColor(R.color.c_409EFF));
            holder.setTextColor(R.id.tv_name, getContext().getResources().getColor(R.color.c_409EFF));
        } else {
            holder.setTextColor(R.id.tv_code, getContext().getResources().getColor(R.color.c_303133));
            holder.setTextColor(R.id.tv_name, getContext().getResources().getColor(R.color.c_303133));
        }

    }
}
