package com.equipmentmanage.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;

/**
 * @Description: 提示信息-dialog
 * @Author: zzh
 * @CreateDate: 2021/8/17
 */
public class TipDialog1 extends Dialog implements View.OnClickListener {

    private TextView tvTitle, tvTipContent;
    private TextView tvCancel, tvConfirm, tvEdit;
    private Context context;

    private OnConfirmListener listener;

    public TipDialog1(Context context) {
        this(context, R.style.tipDialog);
        this.context = context;
    }

    public TipDialog1(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tip1);
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
        tvTipContent = findViewById(R.id.tv_tip_content);

        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvEdit = findViewById(R.id.tv_edit);

    }

    // 初始化监听
    private void initListener() {
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
    }

    public void setTitleAndTip(String title, String tip) {
        if (!StringUtils.isNullOrEmpty(title)){
            tvTitle.setText(title);
        }

        if (!StringUtils.isNullOrEmpty(tip)){
            tvTipContent.setText(tip);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_confirm) {
            if (null != listener) {
                listener.onConfirm();
                dismiss();
            }

        } else if (v.getId() == R.id.tv_cancel) {
            dismiss();
        } else if (v.getId() == R.id.tv_edit) {
            if (null != listener) {
                listener.onEdit();
                dismiss();
            }
        }
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }


    public interface OnConfirmListener {

        void onConfirm(); //确定
        void onEdit(); //修改

    }
}
