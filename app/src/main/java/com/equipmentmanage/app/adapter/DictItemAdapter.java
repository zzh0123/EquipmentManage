package com.equipmentmanage.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.DictItemBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 字典adapter，  化学品明细-adapter
 * @Author: zzh
 * @CreateDate: 2021/8/11
 */
public class DictItemAdapter extends BaseQuickAdapter<DictItemBean, BaseViewHolder> {
    public DictItemAdapter(@Nullable List<DictItemBean> data) {
        super(R.layout.item_dict, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable DictItemBean bean) {
        holder.setText(R.id.tv_name, StringUtils.nullStrToEmpty(bean.getText())); // 化学品名称
    }
}
