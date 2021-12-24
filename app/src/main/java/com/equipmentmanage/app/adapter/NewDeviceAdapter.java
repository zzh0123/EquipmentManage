package com.equipmentmanage.app.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.BaseDeviceBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 新建装置-adapter
 * @Author: zzh
 * @CreateDate: 2021/12/11
 */
public class NewDeviceAdapter extends BaseQuickAdapter<BaseDeviceBean, BaseViewHolder> {
    public NewDeviceAdapter(@Nullable List<BaseDeviceBean> data) {
        super(R.layout.item_new_common, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable BaseDeviceBean bean) {
        holder.setText(R.id.tv_code, StringUtils.nullStrToEmpty(bean.getDeviceCode())); // 装置编码
        holder.setText(R.id.tv_name, StringUtils.nullStrToEmpty(bean.getDeviceName())); // 装置名称

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
