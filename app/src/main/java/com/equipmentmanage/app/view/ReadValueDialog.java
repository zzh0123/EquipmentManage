package com.equipmentmanage.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.activity.PartImgsActivity;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.xuexiang.xui.widget.button.CountDownButton;

import es.dmoral.toasty.Toasty;

/**
 * @Description: 提示信息-dialog
 * @Author: zzh
 * @CreateDate: 2021/11/22
 */
public class ReadValueDialog extends Dialog implements View.OnClickListener {

    private TextView tvTitle, tv_read_value;
    private CountDownButton tv_read; //读数
    private TextView tvCancel, tvConfirm;
    private Context context;

    private OnConfirmListener listener;

    public ReadValueDialog(Context context) {
        this(context, R.style.tipDialog);
        this.context = context;
    }

    public ReadValueDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_read_value);
        initView();
        initListener();
    }

    // 初始化view
    private void initView() {
        L.i("zzz1---->initView");
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

        tvTitle = findViewById(R.id.tv_title);
        tv_read_value = findViewById(R.id.tv_read_value);
        tv_read = findViewById(R.id.tv_read);

        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
    }

    // 初始化监听
    private void initListener() {
        tv_read.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

    }

    public void setReadValue(String readValue){
        tv_read_value.setText(readValue);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_confirm) {
            if (StringUtils.isNullOrEmpty(tv_read_value.getText().toString().trim())){
                Toasty.normal(context, "请先读取数据！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (null != listener) {
                listener.onConfirm(tv_read_value.getText().toString().trim());
                dismiss();
            }

        } else if (v.getId() == R.id.tv_cancel) {
            dismiss();
        } else if (v.getId() == R.id.tv_read) {
            if (null != listener) {
                listener.onRead();
//                dismiss();
            }
        }
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }


    public interface OnConfirmListener {

        void onConfirm(String s); //确定
        void onRead(); //读数
    }
}
