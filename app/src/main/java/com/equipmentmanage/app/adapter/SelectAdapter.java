package com.equipmentmanage.app.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.BaseDeviceBean;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 选择-adapter
 * @Author: zzh
 * @CreateDate: 2021/10/25
 */
public class SelectAdapter extends BaseQuickAdapter<DepartmentBean, BaseViewHolder> {
    public SelectAdapter(@Nullable List<DepartmentBean> data) {
        super(R.layout.item_common, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable DepartmentBean bean) {
        holder.setText(R.id.tv_name, StringUtils.nullStrToEmpty(bean.getName())); // 名称
    }
}
