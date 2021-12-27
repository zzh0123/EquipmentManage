package com.equipmentmanage.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.NewAreaBean;
import com.equipmentmanage.app.bean.NewTagBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 新增标签-adapter
 * @Author: zzh
 * @CreateDate: 2021/12/15
 */
public class NewTagAdapter extends BaseQuickAdapter<NewTagBean, BaseViewHolder> {
    public NewTagAdapter(@Nullable List<NewTagBean> data) {
        super(R.layout.item_new_tag, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable NewTagBean bean) {
        holder.setText(R.id.tv_tag_num, "标签号：" + StringUtils.nullStrToEmpty(bean.getTagNum())); //
        holder.setText(R.id.tv_count, "图片数量：" + StringUtils.nullStrToEmpty(bean.getCount())); //
        holder.setText(R.id.tv_point_count, "标点数量：" + StringUtils.nullStrToEmpty(bean.getPointCount())); //

        boolean isSelected = bean.isSelected();
        if (isSelected) {
            holder.setTextColor(R.id.tv_tag_num, getContext().getResources().getColor(R.color.c_409EFF));
            holder.setTextColor(R.id.tv_count, getContext().getResources().getColor(R.color.c_409EFF));
            holder.setTextColor(R.id.tv_point_count, getContext().getResources().getColor(R.color.c_409EFF));
        } else {
            holder.setTextColor(R.id.tv_tag_num, getContext().getResources().getColor(R.color.c_303133));
            holder.setTextColor(R.id.tv_count, getContext().getResources().getColor(R.color.c_303133));
            holder.setTextColor(R.id.tv_point_count, getContext().getResources().getColor(R.color.c_303133));
        }

    }
}
