package com.equipmentmanage.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.CommonSelectBean;
import com.equipmentmanage.app.bean.NewAreaBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 选择-adapter
 * @Author: zzh
 * @CreateDate: 2021/12/13
 */
public class CommonSelectAdapter extends BaseQuickAdapter<CommonSelectBean, BaseViewHolder> {
    public CommonSelectAdapter(@Nullable List<CommonSelectBean> data) {
        super(R.layout.item_common_select3, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable CommonSelectBean bean) {
        holder.setText(R.id.tv_name, StringUtils.nullStrToEmpty(bean.getName())); //

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
