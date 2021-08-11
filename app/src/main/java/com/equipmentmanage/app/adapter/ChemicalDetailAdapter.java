package com.equipmentmanage.app.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.ChemicalDetailBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 化学品明细-adapter
 * @Author: zzh
 * @CreateDate: 2021/8/11
 */
public class ChemicalDetailAdapter extends BaseQuickAdapter<ChemicalDetailBean, BaseViewHolder> {
    public ChemicalDetailAdapter(@Nullable List<ChemicalDetailBean> data) {
        super(R.layout.item_chemical_detail, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable ChemicalDetailBean bean) {
        holder.setText(R.id.tv_name, StringUtils.nullStrToEmpty(bean.getName())); // 化学品名称
    }
}
