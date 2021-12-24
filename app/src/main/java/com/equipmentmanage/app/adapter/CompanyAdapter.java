package com.equipmentmanage.app.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.BarcodeTypeBean;
import com.equipmentmanage.app.bean.BaseAreaBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 所属公司-adapter
 * @Author: zzh
 * @CreateDate: 2021/12/5
 */
public class CompanyAdapter extends BaseQuickAdapter<BarcodeTypeBean, BaseViewHolder> {
    public CompanyAdapter(@Nullable List<BarcodeTypeBean> data) {
        super(R.layout.item_common, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable BarcodeTypeBean bean) {
        holder.setText(R.id.tv_name, StringUtils.nullStrToEmpty(bean.getName())); // 名称
    }
}
