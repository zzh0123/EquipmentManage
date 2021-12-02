package com.equipmentmanage.app.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.BaseAreaBean;
import com.equipmentmanage.app.bean.BaseGasBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 校准-adapter
 * @Author: zzh
 * @CreateDate: 2021/11/06
 */
public class CorrectAdapter extends BaseQuickAdapter<BaseGasBean, BaseViewHolder> {
    public CorrectAdapter(@Nullable List<BaseGasBean> data) {
        super(R.layout.item_correct, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable BaseGasBean bean) {
        holder.setText(R.id.tv_date, StringUtils.nullStrToEmpty(bean.getDate())); // 时间
        holder.setText(R.id.tv_response_time, StringUtils.nullStrToEmpty(bean.getResponseTime())); // 响应时间
        holder.setText(R.id.tv_status, StringUtils.nullStrToEmpty(bean.getStatus())); // 状态
        holder.setText(R.id.tv_hold, StringUtils.nullStrToEmpty(bean.getCalibThreshold())); //
        holder.setText(R.id.tv_gas_name, StringUtils.nullStrToEmpty(bean.getGasName())); // 标准气名称
        holder.setText(R.id.tv_aim_value, StringUtils.nullStrToEmpty(bean.getPpm())); // 目标值
        holder.setText(R.id.tv_actual_value, StringUtils.nullStrToEmpty(bean.getActualValue())); // 实际检测值

        TextView tvStatus = holder.getView(R.id.tv_status);
        String status = bean.getStatus();
        if (!StringUtils.isNullOrEmpty(status)) {
            if (status.equals("1")) {
                tvStatus.setText(R.string.pass_1);
                tvStatus.setTextColor(getContext().getResources().getColor(R.color.c_67C23A));
                tvStatus.setBackgroundResource(R.drawable.bg_rec_green);
            } else {
                tvStatus.setText(R.string.pass_0);
                tvStatus.setTextColor(getContext().getResources().getColor(R.color.c_FFC0C4CC));
                tvStatus.setBackgroundResource(R.drawable.bg_rec_grey);
            }
        } else {
            tvStatus.setText(R.string.pass_0);
            tvStatus.setTextColor(getContext().getResources().getColor(R.color.c_FFC0C4CC));
            tvStatus.setBackgroundResource(R.drawable.bg_rec_grey);
        }

    }
}
