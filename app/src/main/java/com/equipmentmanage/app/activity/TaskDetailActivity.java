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
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.TaskBean;
import com.equipmentmanage.app.bean.TaskResultBean;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.tencent.mmkv.MMKV;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 任务详情
 * @Author: zzh
 * @CreateDate: 2021/8/17
 */
public class TaskDetailActivity extends BaseActivity {

    public static void open(Context c, TaskResultBean.Records bean){
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
            R.mipmap.ic_correct};
    private String[] iconNames = { "校准设备", "气象参数", "漂移校准"};
    private String[] statusArr = { "未校准", "未录入", "未录入"};

    private MyGridViewAdapter adapter;

    private TaskResultBean.Records bean;

    private MMKV kv = MMKV.defaultMMKV();

    private int totalCount = 0;
    private int checkedCount = 0;
    private int progress;
    private String id = "";

    private String is_daily_checked, is_drift_checked, is_weather_checked;

    @Override
    protected int initLayout() {
        return R.layout.activity_task_detail;
    }

    @Override
    protected void initView() {
        // 注册订阅者
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }

        bean = (TaskResultBean.Records) getIntent().getSerializableExtra(Constant.taskBean);
        id = bean.getId();

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

        setDailyView();
        setWeatherView();
        setDriftView();

        setView();
    }

    private void setDailyView(){
        // 日常校准是否已检测  1 是 其他否
        is_daily_checked = kv.getString(Constant.is_correct_checked + id + "2", "");
//        L.i("zzz1--is_daily_checked-->" + is_daily_checked);
        if (!StringUtils.isNullOrEmpty(is_daily_checked) && is_daily_checked.equals(ConstantValue.correct_1)){
            statusArr[0] = ConstantValue.correct_1_name;
        }
        adapter.notifyDataSetChanged();
    }

    private void setDriftView(){
        // 漂移校准是否已检测  1 是 其他否
        is_drift_checked = kv.getString(Constant.is_correct_checked + id + "3", "");
        L.i("zzz1--is_drift_checked-->" + is_drift_checked);
        if (!StringUtils.isNullOrEmpty(is_drift_checked) && is_drift_checked.equals(ConstantValue.correct_1)){
            statusArr[2] = ConstantValue.correct_1_name;
        }
        adapter.notifyDataSetChanged();
    }

    private void setWeatherView(){
        // 气象参数是否已检测  1 是 其他否
        is_weather_checked = kv.getString(Constant.is_weather_checked + id, "");
//        L.i("zzz1--is_weather_checked-->" + is_weather_checked);
        if (!StringUtils.isNullOrEmpty(is_weather_checked) && is_weather_checked.equals(ConstantValue.entered_1)){
            statusArr[1] = ConstantValue.entered_1_name;
        }
        adapter.notifyDataSetChanged();
    }

    private void setView(){
        totalCount = Integer.parseInt(bean.getUserTaskCompCount());
//        totalCount = kv.getInt(Constant.totalCount+ id, 0);
        checkedCount = kv.getInt(Constant.checkedCount+ id, 0);
        tvPartAmountTotal.setText("" + totalCount);

        if ((checkedCount) > 0){
            tvCheckedAmount.setText("" + (checkedCount));
            if (totalCount != 0){
                progress = ((checkedCount) * 100)/totalCount;
            } else {
                progress = 0;
            }

        } else {
            tvCheckedAmount.setText("0");
            progress = 0;
        }
        tvProgress.setText(progress + "%");

        if (progress == 100){
            kv.encode(Constant.is_seal_point_checked + id, "1");
        }
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
                        CorrectActivity1.open(TaskDetailActivity.this, ConstantValue.correct_type_0, bean.getId());
                        break;
                    case 1:
                        // 气象参数
//                        AreaManageActivity.open(getActivity());
                        if (!StringUtils.isNullOrEmpty(bean.getBelongCompany())) {
                            WeatherParamsActivity.open(TaskDetailActivity.this, bean.getBelongCompany(), bean.getId());
                        } else {
                            Toasty.error(TaskDetailActivity.this, "所属机构为空！", Toast.LENGTH_SHORT, true).show();
                        }
                        break;
                    case 2:
                        // 漂移校准
                        CorrectActivity1.open(TaskDetailActivity.this, ConstantValue.correct_type_1, bean.getId());
                        break;
                        // 组件查询
//                        PartCheckActivity.open(TaskDetailActivity.this, bean);
//                        break;
//                    case 3:
//                        // 漂移校准
//                        CorrectActivity1.open(TaskDetailActivity.this, ConstantValue.correct_type_1);
//                        break;
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
                if (StringUtils.isNullOrEmpty(is_daily_checked) || (!is_daily_checked.equals(ConstantValue.correct_1))){
                    Toasty.warning(TaskDetailActivity.this, "请先校准设备！", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                if (StringUtils.isNullOrEmpty(is_weather_checked) || (!is_weather_checked.equals(ConstantValue.entered_1))){
                    Toasty.warning(TaskDetailActivity.this, "请先录入气象参数！", Toast.LENGTH_SHORT, true).show();
                    return;
                }

//                if (StringUtils.isNullOrEmpty(is_drift_checked) || (!is_drift_checked.equals(ConstantValue.correct_1))){
//                    Toasty.warning(TaskDetailActivity.this, "请先漂移校准！", Toast.LENGTH_SHORT, true).show();
//                    return;
//                }


                L.i("zzz1--bean-1->" + bean.getTaskName());
//                // 组件查询
//                PartCheckActivity.open(TaskDetailActivity.this, bean);
                // 组件查询, 密封点检测
                if (bean != null) {
                    PartImgsActivity.open(TaskDetailActivity.this, bean);
                }
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

    //订阅事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(String event) {
//        Log.i("zzz1", "" + event);
//        //接收以及处理数据
        if (!StringUtils.isNullOrEmpty(event)) {
            if (ConstantValue.event_1.equals(event)) {
                setView();
            } else if (ConstantValue.event_weather_save.equals(event)) {
                setWeatherView();
            } else if (ConstantValue.event_daily_save.equals(event)) {
                setDailyView();
            }else if (ConstantValue.event_drift_save.equals(event)) {
                setDriftView();
            }
        }
    }
}