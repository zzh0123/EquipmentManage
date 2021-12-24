package com.equipmentmanage.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;

import es.dmoral.toasty.Toasty;

/**
 * @Description: 新增区域-dialog
 * @Author: zzh
 * @CreateDate: 2021/12/11
 */
public class AddFloorDialog extends Dialog implements View.OnClickListener {

    private EditText et_floor_level;
    private TextView tv_title, tvCancel, tvConfirm;
    private Context context;

    private OnConfirmListener listener;

    public AddFloorDialog(Context context) {
        this(context, R.style.tipDialog);
        this.context = context;
    }

    public AddFloorDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_floor);
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

        tv_title = findViewById(R.id.tv_title);
        et_floor_level = findViewById(R.id.et_floor_level);

        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
    }

    // 初始化监听
    private void initListener() {
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

    }

    public void setTitle(String title){
        if (!StringUtils.isNullOrEmpty(title)){
            tv_title.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_confirm) {
            if (StringUtils.isNullOrEmpty(et_floor_level.getText().toString().trim())){
                Toasty.normal(context, "楼层不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }

            if (null != listener) {
                listener.onConfirm(et_floor_level.getText().toString().trim());
//                dismiss();
            }

        } else if (v.getId() == R.id.tv_cancel) {
            dismiss();
        }
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }


    public interface OnConfirmListener {

        void onConfirm(String floorLevel); //确定

    }
}
