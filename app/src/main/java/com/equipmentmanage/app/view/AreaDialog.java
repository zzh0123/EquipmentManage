package com.equipmentmanage.app.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.RecyclerBlueToothAdapter;
import com.equipmentmanage.app.bean.BlueTooth;
import com.equipmentmanage.app.receiver.BlueToothReceiver;
import com.equipmentmanage.app.service.BluetoothChatService;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.vinterface.BlueToothInterface;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

import static com.equipmentmanage.app.netapi.ConstantValue.BLUE_TOOTH_DIALOG;
import static com.equipmentmanage.app.netapi.ConstantValue.BLUE_TOOTH_READ;
import static com.equipmentmanage.app.netapi.ConstantValue.BLUE_TOOTH_SUCCESS;
import static com.equipmentmanage.app.netapi.ConstantValue.BLUE_TOOTH_TOAST;


/**
 * @Description: 蓝牙连接-弹框
 * @Author: zzh
 * @CreateDate: 2021/11/22
 */
public class AreaDialog extends Dialog implements BlueToothInterface, RecyclerBlueToothAdapter.OnItemClickListener {

    private Context context;
    private LinearLayout ll_cancel;
    private ImageView iv_cancel;
    private OnSelectClickListener onSelectClickListener;

    // 扫码数据列表
    private SmartRefreshLayout srl;
    private RecyclerView rv_list;

    private TextView tv_switch_name;
    private SwitchButton switchBtn;

    private BluetoothAdapter mBluetoothAdapter;
    private Timer timer;
    private WifiTask task;
    private RecyclerBlueToothAdapter recyclerAdapter;
    private List<BlueTooth> list = new ArrayList<>();
    private BlueToothReceiver mReceiver;
    private ProgressDialog progressDialog;

    private BluetoothChatService mBluetoothChatService;

    private static final String TAG = "zzz1--blued->";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BluetoothAdapter.STATE_ON:
                case BluetoothAdapter.STATE_OFF: {
                    if (msg.what == BluetoothAdapter.STATE_ON) {
                        recyclerAdapter.setWifiData(list);
                        tv_switch_name.setText("蓝牙已开启");
                        L.i(TAG, "onCheckedChanged: startIntent");
                        //自动刷新
                        srl.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                srl.setEnableRefresh(true);

                                refresh();
                            }
                        }, 300);

                        //开启socket监听
                        mBluetoothChatService = BluetoothChatService.getInstance(handler);
                        mBluetoothChatService.start();
                    } else if (msg.what == BluetoothAdapter.STATE_OFF) {
                        tv_switch_name.setText("蓝牙已关闭");
                        recyclerAdapter.setWifiData(null);
                        recyclerAdapter.notifyDataSetChanged();
                        mBluetoothChatService.stop();
                    }
                    timer.cancel();
                    timer = null;
                    task = null;
                    switchBtn.setClickable(true);
                }
                break;
                case BLUE_TOOTH_DIALOG: {
                    showProgressDialog((String) msg.obj);
                }
                break;
                case BLUE_TOOTH_TOAST: {
                    dismissProgressDialog();
                    Toasty.normal(context, (String) msg.obj, Toast.LENGTH_SHORT).show();

                }
                break;
                case BLUE_TOOTH_SUCCESS: {
                    dismissProgressDialog();
                    Toasty.success(context, "连接设备" + (String) msg.obj + "成功", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(context , ChatActivity.class);
//                    intent.putExtra(ChatActivity.DEVICE_NAME_INTENT , (String) msg.obj);
//                    startActivity(intent);

//                    postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
////                            mBluetoothChatService.sendData("1111111".getBytes());
//                            sendMsg(ConstantValue.log_start);
//                        }
//                    }, 300);
                    if (onSelectClickListener != null){
                        onSelectClickListener.isConnnect(true);
                    }

                    //关闭其他资源
                    close();

                }
                break;

                case BLUE_TOOTH_READ: {
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
//                    L.i("zzz1---readmsg-->" + readMessage);

                    if (onSelectClickListener != null){
                        onSelectClickListener.onSelectClick(readMessage);
                    }

                }
                break;
            }
        }
    };

    public void sendMsg(String msg){
        mBluetoothChatService.sendData(msg.getBytes());
    }

    public AreaDialog(Context context) {
        this(context, R.style.tipDialog);
        this.context = context;
    }

    public AreaDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bluetooth);
        initView();
    }

    private void initView() {
        setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER; // BOTTOM紧贴底部
        lp.alpha = 1;
        lp.dimAmount = 0.6f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        ll_cancel = findViewById(R.id.ll_cancel);
        iv_cancel = findViewById(R.id.iv_cancel);
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_switch_name = findViewById(R.id.tv_switch_name);
        switchBtn = findViewById(R.id.switchBtn);
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    if (mBluetoothAdapter.getState() != BluetoothAdapter.STATE_ON) {
                        mBluetoothAdapter.enable();  //打开蓝牙
                        tv_switch_name.setText("正在开启蓝牙");
//                        ToastUtil.showText(this, "正在开启蓝牙");
                    }
                } else {
                    if (mBluetoothAdapter.getState() != BluetoothAdapter.STATE_OFF) {
                        mBluetoothAdapter.disable();  //打开蓝牙
                        tv_switch_name.setText("正在关闭Wifi");
//                        ToastUtil.showText(this, "正在关闭蓝牙");
                    }
                }
                switchBtn.setClickable(false);
                if (timer == null || task == null) {
                    timer = new Timer();
                    task = new WifiTask();
                    task.setChecked(isChecked);
                    timer.schedule(task, 0, 1000);
                }
            }
        });
//        iv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onButtonOnclickListener.onCancelClick();
//            }
//        });
//        setData();

        srl = findViewById(R.id.srl);
        srl.setEnableRefresh(true);
        srl.setEnableLoadMore(false);
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });

        rv_list = findViewById(R.id.rv_list);

        recyclerAdapter = new RecyclerBlueToothAdapter(context);
        recyclerAdapter.setWifiData(list);
        recyclerAdapter.setOnItemClickListener(this);

        rv_list.setLayoutManager(new LinearLayoutManager(context));
        rv_list.setAdapter(recyclerAdapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //获取本地蓝牙实例

        initReceiverService();
    }

    private void initReceiverService() {
        mReceiver = new BlueToothReceiver(this);
        //注册扫描设备广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mReceiver, filter);

        if (mBluetoothAdapter.isEnabled()) {
            L.i(TAG, "onResume: resumeStart");
            mBluetoothChatService = BluetoothChatService.getInstance(handler);
            mBluetoothChatService.start();
        }
    }

    private void refresh() {
//        pageNo = 1;
        if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            list.clear();
            //扫描的是已配对的
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                list.add(new BlueTooth("已配对的设备", BlueTooth.TAG_TOAST));
                for (BluetoothDevice device : pairedDevices) {
                    L.i(TAG, device.getName() + "\n" + device.getAddress());
                    list.add(new BlueTooth(device.getName(), device.getAddress(), ""));
                }
                list.add(new BlueTooth("已扫描的设备", BlueTooth.TAG_TOAST));
            } else {
                Toasty.warning(context, "没有找到已匹对的设备！", Toast.LENGTH_SHORT, false).show();
                list.add(new BlueTooth("已扫描的设备", BlueTooth.TAG_TOAST));
            }
            L.i(TAG, "list.size--->" + list.size());
            recyclerAdapter.notifyDataSetChanged();
            //开始扫描设备
            mBluetoothAdapter.startDiscovery();
            Toasty.normal(context, "开始扫描设备", Toast.LENGTH_SHORT).show();

        } else {
            Toasty.normal(context, "请开启蓝牙", Toast.LENGTH_SHORT).show();

        }
        srl.finishRefresh();
    }

    private void close() {
        if (timer != null)
            timer.cancel();
        //取消扫描
        mBluetoothAdapter.cancelDiscovery();
        srl.finishRefresh();
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver);
        }
    }

    /**
     * RecyclerView Item 点击处理
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        showProgressDialog("正在进行连接");
        BlueTooth blueTooth = list.get(position);
        connectDevice(blueTooth.getMac());
    }

    private void connectDevice(String mac) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mac);
        mBluetoothChatService.connectDevice(device);
    }

    /**
     * 进度对话框
     *
     * @param msg
     */
    public void showProgressDialog(String msg) {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private class WifiTask extends TimerTask {
        private boolean isChecked;

        public void setChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }

        @Override
        public void run() {
            if (isChecked) {
                if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
                    handler.sendEmptyMessage(BluetoothAdapter.STATE_ON);
            } else {
                if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF)
                    handler.sendEmptyMessage(BluetoothAdapter.STATE_OFF);
            }
        }
    }


    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener) {
        this.onSelectClickListener = onSelectClickListener;
    }

    public interface OnSelectClickListener {
        void onSelectClick(String s);
        void isConnnect(boolean isCon);
    }


    /**
     * 扫描设备回调监听
     *
     * @param device
     * @param rssi
     */
    @Override
    public void getBlutToothDevices(BluetoothDevice device, int rssi) {
        list.add(new BlueTooth(device.getName(), device.getAddress(), rssi + ""));
        L.i(TAG, "getBlutToothDevices--" + list.size());
        //更新UI
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void searchFinish() {
        srl.finishRefresh();
        L.i(TAG, "searchFinish--" + list.size());
        Toasty.normal(context, "扫描完成", Toast.LENGTH_SHORT).show();
    }
}
