package com.equipmentmanage.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.SelectAdapter;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description: 推送箱校验-弹框
 * @Author: zzh
 * @CreateDate: 2021/09/17
 */
public class SelectDialog extends Dialog {

    private Context context;
    private LinearLayout ll_cancel;
    private ImageView iv_cancel;
    private OnSelectClickListener onSelectClickListener;

    // 扫码数据列表
    private RecyclerView rv_list;
    private SelectAdapter adapter;
    private List<DepartmentBean> orderCheckBeanList;
    private Context mContext;

    public SelectDialog(Context context) {
        this(context, R.style.tipDialog);
        this.context = context;
    }

    public SelectDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_dh_dialog_push_box_check);
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

        orderCheckBeanList = new ArrayList<>();

        ll_cancel = findViewById(R.id.ll_cancel);
        iv_cancel = findViewById(R.id.iv_cancel);
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

//        iv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onButtonOnclickListener.onCancelClick();
//            }
//        });
        setData();

        rv_list = findViewById(R.id.rv_list);

        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(manager);
        adapter = new SelectAdapter(orderCheckBeanList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                DepartmentBean bean = orderCheckBeanList.get(position);
                if (bean != null) {
                    if (!StringUtils.isNullOrEmpty(bean.getValue()) && onSelectClickListener != null) {
                        onSelectClickListener.onSelectClick(bean.getValue());
                    }
                }
            }
        });
        rv_list.setAdapter(adapter);

    }

    private void setData(){
        DepartmentBean bean = new DepartmentBean();
        bean.setName("泄压设备");
        bean.setValue("R");
        DepartmentBean bean1 = new DepartmentBean();
        bean1.setName("开口管线");
        bean1.setValue("O");
        DepartmentBean bean2 = new DepartmentBean();
        bean2.setName("泵");
        bean2.setValue("P");
        DepartmentBean bean3 = new DepartmentBean();
        bean3.setName("法兰");
        bean3.setValue("F");
        DepartmentBean bean4 = new DepartmentBean();
        bean4.setName("连接件");
        bean4.setValue("C");
        DepartmentBean bean5 = new DepartmentBean();
        bean5.setName("阀门");
        bean5.setValue("V");
        DepartmentBean bean6 = new DepartmentBean();
        bean6.setName("搅拌器");
        bean6.setValue("A");
        DepartmentBean bean7 = new DepartmentBean();
        bean7.setName("压缩机");
        bean7.setValue("Y");
        DepartmentBean bean8 = new DepartmentBean();
        bean8.setName("其他");
        bean8.setValue("Q");
        DepartmentBean bean9 = new DepartmentBean();
        bean9.setName("取样连接系统");
        bean9.setValue("S");
        orderCheckBeanList.add(bean);
        orderCheckBeanList.add(bean1);
        orderCheckBeanList.add(bean2);
        orderCheckBeanList.add(bean3);
        orderCheckBeanList.add(bean4);
        orderCheckBeanList.add(bean5);
        orderCheckBeanList.add(bean6);
        orderCheckBeanList.add(bean7);
        orderCheckBeanList.add(bean8);
        orderCheckBeanList.add(bean9);
    }

    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener) {
        this.onSelectClickListener = onSelectClickListener;
    }

    public interface OnSelectClickListener {
        void onSelectClick(String s);
    }
}
