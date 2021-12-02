package com.equipmentmanage.app.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.AreaManageBean;
import com.equipmentmanage.app.bean.AreaManageResultBean;
import com.equipmentmanage.app.bean.BaseAreaBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 区域管理-adapter
 * @Author: zzh
 * @CreateDate: 2021/8/11
 */
public class AreaAdapter extends BaseQuickAdapter<BaseAreaBean, BaseViewHolder> {
    public AreaAdapter(@Nullable List<BaseAreaBean> data) {
        super(R.layout.item_area_manage, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable BaseAreaBean bean) {
        holder.setText(R.id.tv_area_name, StringUtils.nullStrToEmpty(bean.getAreaName())); // 区域名称
//        holder.setText(R.id.tv_status, StringUtils.nullStrToEmpty(bean.getStatus())); // 状态
        holder.setText(R.id.tv_device_code, StringUtils.nullStrToEmpty(bean.getAreaCode())); // 区域编码
        holder.setText(R.id.tv_belong_device, StringUtils.nullStrToEmpty(bean.getDeviceName())); // 所属装置

        TextView tvStatus = holder.getView(R.id.tv_status);
//        String status = bean.getStatus();
//        if (status.equals("1")){
            tvStatus.setText(R.string.enabled_1);
            tvStatus.setTextColor(getContext().getResources().getColor(R.color.c_67C23A));
            tvStatus.setBackgroundResource(R.drawable.bg_rec_green);
//        } else {
//            tvStatus.setText(R.string.enabled_0);
//            tvStatus.setTextColor(getContext().getResources().getColor(R.color.c_FFC0C4CC));
//            tvStatus.setBackgroundResource(R.drawable.bg_rec_grey);
//        }

    }
}
