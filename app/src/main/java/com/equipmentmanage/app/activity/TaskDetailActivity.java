package com.equipmentmanage.app.activity;

import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.TaskBean;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description: 任务详情
 * @Author: zzh
 * @CreateDate: 2021/8/17
 */
public class TaskDetailActivity extends BaseActivity {

    public static void open(Context c, TaskBean bean){
        Intent i = new Intent(c, TaskDetailActivity.class);
        i.putExtra(Constant.taskBean, bean);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.tv_progress)
    TextView tvProgress; //检测进度

    @BindView(R.id.tv_part_amount_total)
    TextView tvPartAmountTotal; //组件数

    @BindView(R.id.tv_checked_amount)
    TextView tvCheckedAmount; //已检测

    @BindView(R.id.tv_leak_amount)
    TextView tvLeakAmount; //泄漏数

    @BindView(R.id.tv_task_name)
    TextView tvTaskName; //任务名称

    @BindView(R.id.gridView)
    GridView gridView; //gridView

    @BindView(R.id.tv_start_check)
    TextView tvStartCheck; //开始检测

    // 图片封装为一个数组
    private int[] icons = { R.mipmap.ic_correct, R.mipmap.ic_weather_params,
            R.mipmap.ic_part_search, R.mipmap.ic_correct};
    private String[] iconNames = { "校准设备", "气象参数", "组件查询", "漂移校准"};
    private String[] statusArr = { "已校准", "未录入", "未录入", "未录入"};

    private MyGridViewAdapter adapter;

    private TaskBean bean;

    @Override
    protected int initLayout() {
        return R.layout.activity_task_detail;
    }

    @Override
    protected void initView() {

        bean = (TaskBean) getIntent().getSerializableExtra(Constant.taskBean);

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (bean != null) {
//            tvAreaName.setText(StringUtils.nullStrToEmpty(bean.getAreaName())); //名称
//            tvAreaCode.setText(StringUtils.nullStrToEmpty(bean.getAreaCode())); //编号
//            tvBelongDevice.setText(StringUtils.nullStrToEmpty(bean.getBelongDevice_dictText())); //所属装置
//            tvEnabled.setText(StringUtils.nullStrToEmpty(bean.getDeviceName())); //是否可用
        }

        adapter = new MyGridViewAdapter(this);
        gridView.setAdapter(adapter);

    }

    @Override
    protected void initEvent() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // { "校准设备", "气象参数", "组件查询", "漂移校准"};
                switch (position){
                    case 0:
                        // 校准设备
//                        statusArr[0] = "未校准";
//                        statusArr[1] = "已录入";
//                        adapter.notifyDataSetChanged();
                        // 0 校准设备(日常校准), 1 漂移校准
                        CorrectActivity.open(TaskDetailActivity.this, ConstantValue.correct_type_0);
                        break;
                    case 1:
                        // 气象参数
//                        AreaManageActivity.open(getActivity());
                        WeatherParamsActivity.open(TaskDetailActivity.this);
                        break;
                    case 2:
                        // 组件查询
                        PartCheckActivity.open(TaskDetailActivity.this, bean);
                        break;
                    case 3:
                        // 漂移校准
                        CorrectActivity.open(TaskDetailActivity.this, ConstantValue.correct_type_1);
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
//        refresh();
    }

    @OnClick({R.id.tv_start_check})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_check:
//                if (valid()){
//                    login();
//                }
                break;
        }
    }

    private class MyGridViewAdapter extends BaseAdapter {

        private Context mContext;

        public MyGridViewAdapter(Context context) {
            this.mContext = context;
        }


        @Override
        public int getCount() {
            return icons.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid1, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.ivIcon = (AppCompatImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.ivStatus = (AppCompatImageView) convertView.findViewById(R.id.iv_status);
                viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // 这里只是模拟，实际开发可能需要加载网络图片，可以使用ImageLoader这样的图片加载框架来异步加载图片
//            imageLoader.displayImage("drawable://" + mThumbIds[position], viewHolder.itemImg);
            viewHolder.ivIcon.setImageResource(icons[position]);
            viewHolder.tvName.setText(iconNames[position]);
            viewHolder.tvStatus.setText(statusArr[position]);
            String status = statusArr[position];
            if (status.equals("已校准") || status.equals("已录入")){
                viewHolder.tvStatus.setTextColor(getResources().getColor(R.color.c_67C23A));
                viewHolder.ivStatus.setVisibility(View.VISIBLE);
                viewHolder.ivStatus.setImageResource(R.mipmap.ic_success);
            } else {
                viewHolder.tvStatus.setTextColor(getResources().getColor(R.color.c_909399));
                viewHolder.ivStatus.setVisibility(View.GONE);
//                viewHolder.ivStatus.setImageResource(R.mipmap.ic_success);
            }
            return convertView;
        }

        class ViewHolder {
            AppCompatImageView ivIcon, ivStatus;
            TextView tvName, tvStatus;
        }
    }
}