package com.equipmentmanage.app.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.RecyclerBlueToothAdapter;
import com.equipmentmanage.app.adapter.SelectAdapter;
import com.equipmentmanage.app.bean.BlueTooth;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.receiver.BlueToothReceiver;
import com.equipmentmanage.app.service.BluetoothChatService;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
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
 * @Description: ????????????-??????
 * @Author: zzh
 * @CreateDate: 2021/11/21
 */
public class BluetoothDialog extends Dialog implements BlueToothInterface, RecyclerBlueToothAdapter.OnItemClickListener {

    private Context context;
    private LinearLayout ll_cancel;
    private ImageView iv_cancel;
    private OnSelectClickListener onSelectClickListener;

    // ??????????????????
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
                        tv_switch_name.setText("???????????????");
                        L.i(TAG, "onCheckedChanged: startIntent");
                        //????????????
                        srl.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                srl.setEnableRefresh(true);

                                refresh();
                            }
                        }, 300);

                        //??????socket??????
                        mBluetoothChatService = BluetoothChatService.getInstance(handler);
                        mBluetoothChatService.start();
                    } else if (msg.what == BluetoothAdapter.STATE_OFF) {
                        tv_switch_name.setText("???????????????");
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
                    Toasty.success(context, "????????????" + (String) msg.obj + "??????", Toast.LENGTH_SHORT).show();
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

                    //??????????????????
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

    public BluetoothDialog(Context context) {
        this(context, R.style.tipDialog);
        this.context = context;
    }

    public BluetoothDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // ??????Content?????????
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bluetooth);
        initView();
    }

    private void initView() {
        setCanceledOnTouchOutside(true); // ??????????????????

        // ?????????????????????, ?????????????????????
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER; // BOTTOM????????????
        lp.alpha = 1;
        lp.dimAmount = 0.6f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // ????????????
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
                        mBluetoothAdapter.enable();  //????????????
                        tv_switch_name.setText("??????????????????");
//                        ToastUtil.showText(this, "??????????????????");
                    }
                } else {
                    if (mBluetoothAdapter.getState() != BluetoothAdapter.STATE_OFF) {
                        mBluetoothAdapter.disable();  //????????????
                        tv_switch_name.setText("????????????Wifi");
//                        ToastUtil.showText(this, "??????????????????");
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

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //????????????????????????

        initReceiverService();
    }

    private void initReceiverService() {
        mReceiver = new BlueToothReceiver(this);
        //????????????????????????
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
            //????????????????????????
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                list.add(new BlueTooth("??????????????????", BlueTooth.TAG_TOAST));
                for (BluetoothDevice device : pairedDevices) {
                    L.i(TAG, device.getName() + "\n" + device.getAddress());
                    list.add(new BlueTooth(device.getName(), device.getAddress(), ""));
                }
                list.add(new BlueTooth("??????????????????", BlueTooth.TAG_TOAST));
            } else {
                Toasty.warning(context, "?????????????????????????????????", Toast.LENGTH_SHORT, false).show();
                list.add(new BlueTooth("??????????????????", BlueTooth.TAG_TOAST));
            }
            L.i(TAG, "list.size--->" + list.size());
            recyclerAdapter.notifyDataSetChanged();
            //??????????????????
            mBluetoothAdapter.startDiscovery();
            Toasty.normal(context, "??????????????????", Toast.LENGTH_SHORT).show();

        } else {
            Toasty.normal(context, "???????????????", Toast.LENGTH_SHORT).show();

        }
        srl.finishRefresh();
    }

    private void close() {
        if (timer != null)
            timer.cancel();
        //????????????
        mBluetoothAdapter.cancelDiscovery();
        srl.finishRefresh();
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver);
        }
    }

    /**
     * RecyclerView Item ????????????
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        showProgressDialog("??????????????????");
        BlueTooth blueTooth = list.get(position);
        connectDevice(blueTooth.getMac());
    }

    private void connectDevice(String mac) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mac);
        mBluetoothChatService.connectDevice(device);
    }

    /**
     * ???????????????
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
     * ?????????????????????
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
     * ????????????????????????
     *
     * @param device
     * @param rssi
     */
    @Override
    public void getBlutToothDevices(BluetoothDevice device, int rssi) {
        list.add(new BlueTooth(device.getName(), device.getAddress(), rssi + ""));
        L.i(TAG, "getBlutToothDevices--" + list.size());
        //??????UI
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void searchFinish() {
        srl.finishRefresh();
        L.i(TAG, "searchFinish--" + list.size());
        Toasty.normal(context, "????????????", Toast.LENGTH_SHORT).show();
    }
}
