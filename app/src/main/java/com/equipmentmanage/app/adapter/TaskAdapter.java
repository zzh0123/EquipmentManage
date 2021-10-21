package com.equipmentmanage.app.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.DeviceManageResultBean;
import com.equipmentmanage.app.bean.TaskBean;
import com.equipmentmanage.app.bean.TaskResultBean;
import com.equipmentmanage.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 任务-adapter
 * @Author: zzh
 * @CreateDate: 2021/8/16
 */
public class TaskAdapter extends BaseQuickAdapter<TaskBean, BaseViewHolder> {
    public TaskAdapter(@Nullable List<TaskBean> data) {
        super(R.layout.item_task, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable TaskBean bean) {
        holder.setText(R.id.tv_task_name, StringUtils.nullStrToEmpty(bean.getTaskName())); // 任务名称
//        holder.setText(R.id.tv_status, StringUtils.nullStrToEmpty(bean.getStatus())); // 状态
        holder.setText(R.id.tv_task_num, StringUtils.nullStrToEmpty(bean.getTaskNum())); // 任务编码
        holder.setText(R.id.tv_complete_progress,
                "共有" + StringUtils.nullStrToEmpty(bean.getUserTaskCompCount()) + "个密封点，已检测"
                        + StringUtils.nullStrToEmpty(bean.getUserDetectionCount()) + "个，"
                        + StringUtils.nullStrToEmpty(bean.getLeakageCount()) + "个泄露" ); // 任务完成进度
//        holder.setText(R.id.tv_label_info, StringUtils.nullStrToEmpty(bean.getDeviceType_dictText())); // 标签信息
        holder.setText(R.id.tv_upload_date, StringUtils.nullStrToEmpty(bean.getUploadTime())); // 上传时间

//        TextView tvStatus = holder.getView(R.id.tv_status);
//        String status = bean.getTaskEnd();
//        if (!StringUtils.isNullString(status) && status.equals("0")){
//            tvStatus.setText(R.string.progress_0);
//            tvStatus.setTextColor(getContext().getResources().getColor(R.color.c_FFC0C4CC));
//            tvStatus.setBackgroundResource(R.drawable.bg_rec_grey);
//        } else {
//            tvStatus.setText(R.string.progress_1);
//            tvStatus.setTextColor(getContext().getResources().getColor(R.color.c_67C23A));
//            tvStatus.setBackgroundResource(R.drawable.bg_rec_green);
//        }

    }
}
