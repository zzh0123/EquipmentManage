package com.equipmentmanage.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
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
public class SelectAdapter1 extends BaseQuickAdapter<SelectBean, BaseViewHolder> {
    public SelectAdapter1(@Nullable List<SelectBean> data) {
        super(R.layout.item_common_select2, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable SelectBean bean) {
        holder.setText(R.id.tv_name, StringUtils.nullStrToEmpty(bean.getName())); // 名称

        boolean isSelected = bean.isSelected();
        if (isSelected){
            holder.setTextColor(R.id.tv_name, getContext().getResources().getColor(R.color.white));
            holder.setBackgroundColor(R.id.card_view, getContext().getResources().getColor(R.color.c_409EFF));
        } else {
            holder.setTextColor(R.id.tv_name, getContext().getResources().getColor(R.color.c_303133));
            holder.setBackgroundColor(R.id.card_view, getContext().getResources().getColor(R.color.c_C0C4CC));
        }
    }
}
