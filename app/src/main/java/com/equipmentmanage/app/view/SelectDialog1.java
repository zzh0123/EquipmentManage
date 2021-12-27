package com.equipmentmanage.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.activity.SealPointOnRecordActivity1;
import com.equipmentmanage.app.adapter.CommonSelectAdapter;
import com.equipmentmanage.app.adapter.SelectAdapter;
import com.equipmentmanage.app.adapter.SelectAdapter1;
import com.equipmentmanage.app.adapter.SelectBean;
import com.equipmentmanage.app.bean.CommonSelectBean;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.utils.StringUtils;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;


/**
 * @Description: 推送箱校验-弹框
 * @Author: zzh
 * @CreateDate: 2021/09/17
 */
public class SelectDialog1 extends Dialog {

    private Context context;
    private LinearLayout ll_cancel;
    private ImageView iv_cancel;
    private TextView tv_continue_last, tv_cancel, tv_confirm;

    private OnSelectClickListener onSelectClickListener;

    // 类型
    private RecyclerView rv_type;
    private SelectAdapter1 adapterType;
    private List<SelectBean> typeList = new ArrayList<>();

    // 是否保温 是否
    LinearLayout ll_select1;
    ImageView iv_selected1;

    LinearLayout ll_unselect1;
    ImageView iv_unselect1;

    // 尺寸
    private RecyclerView rv_size;
    private SelectAdapter1 adapterSize;
    private List<SelectBean> sizeList = new ArrayList<>();

    private EditText et_count;
    private String type, heatPre, size, count;

    private MMKV kv = MMKV.defaultMMKV();

    public SelectDialog1(Context context) {
        this(context, R.style.tipDialog);
        this.context = context;
    }

    public SelectDialog1(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select);
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

        tv_continue_last = findViewById(R.id.tv_continue_last);
        tv_continue_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 读取缓存
                type = kv.getString(Constant.select_type, "");
                if (StringUtils.isNullOrEmpty(type)){
                    Toasty.warning(context, "没有可延续的数据！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                heatPre = kv.getString(Constant.select_heatpre, "");
                size = kv.getString(Constant.select_size, "");
                count = kv.getString(Constant.select_count, "");

                setLast();

            }
        });

        tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_confirm = findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNullOrEmpty(type)){
                    Toasty.warning(context, "请选择类型！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(heatPre)){
                    Toasty.warning(context, "请选择是否保温！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(size)){
                    Toasty.warning(context, "请选择尺寸！", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                String count = et_count.getText().toString().trim();
                if (onSelectClickListener != null) {
                    kv.encode(Constant.select_type, type);
                    kv.encode(Constant.select_heatpre, heatPre);
                    kv.encode(Constant.select_size, size);
                    kv.encode(Constant.select_count, count);
                    onSelectClickListener.onSelectClick(type, heatPre, size, count);
                    dismiss();
                }
            }
        });

        et_count = findViewById(R.id.et_count);
        // 类型
        rv_type = findViewById(R.id.rv_type);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 5);
        rv_type.setLayoutManager(gridLayoutManager);
        adapterType = new SelectAdapter1(typeList);
        adapterType.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                SelectBean bean = typeList.get(position);
                if (bean != null) {
                    clearSelectType();
                    bean.setSelected(true);
                    adapterType.notifyDataSetChanged();

                    type = bean.getName();
                }

            }
        });
        rv_type.setAdapter(adapterType);
        initTypeData();

        // 是否保温 是否
        ll_select1 = findViewById(R.id.ll_select1);
        iv_selected1 = findViewById(R.id.iv_selected1);

        ll_unselect1 = findViewById(R.id.ll_unselect1);
        iv_unselect1 = findViewById(R.id.iv_unselect1);

        // 是
        ll_select1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_selected1.setImageResource(R.mipmap.selected);
                iv_unselect1.setImageResource(R.mipmap.unselect);
                heatPre = ConstantValue.heatPre_type_1;
            }
        });

        ll_unselect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_selected1.setImageResource(R.mipmap.unselect);
                iv_unselect1.setImageResource(R.mipmap.selected);
                heatPre = ConstantValue.heatPre_type_0;
            }
        });

        // 尺寸
        rv_size = findViewById(R.id.rv_size);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(context, 5);
        rv_size.setLayoutManager(gridLayoutManager1);
        adapterSize = new SelectAdapter1(sizeList);
        adapterSize.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                SelectBean bean = sizeList.get(position);
                if (bean != null) {
                    clearSizeType();
                    bean.setSelected(true);
                    adapterSize.notifyDataSetChanged();

                    size = bean.getName();
                }

            }
        });
        rv_size.setAdapter(adapterSize);
        initSizeData();

    }

    private void setLast(){
        clearSelectType();
        if (!StringUtils.isNullOrEmpty(type)){
            for (int i = 0; i < typeList.size(); i++) {
                if (type.equals(typeList.get(i).getName())){
                    typeList.get(i).setSelected(true);
                    adapterType.notifyDataSetChanged();
                    break;
                }

            }
        }

        if (heatPre.equals(ConstantValue.heatPre_type_1)){
            iv_selected1.setImageResource(R.mipmap.selected);
            iv_unselect1.setImageResource(R.mipmap.unselect);
        } else {
            iv_selected1.setImageResource(R.mipmap.unselect);
            iv_unselect1.setImageResource(R.mipmap.selected);
        }

        clearSizeType();
        if (!StringUtils.isNullOrEmpty(size)){
            for (int i = 0; i < sizeList.size(); i++) {
                if (size.equals(sizeList.get(i).getName())){
                    sizeList.get(i).setSelected(true);
                    adapterSize.notifyDataSetChanged();
                    break;
                }

            }
        }

        et_count.setText(count);
    }

    public void setEdit(String type, String heatPre, String size, String count){
        this.type = type;
        this.heatPre = heatPre;
        this.size = size;
        this.count = count;

        setLast();
    }

    private void clearSelectType() {
        if (typeList != null && typeList.size() > 0) {
            for (int i = 0; i < typeList.size(); i++) {
                typeList.get(i).setSelected(false);
            }
        }
    }

    private void clearSizeType() {
        if (sizeList != null && sizeList.size() > 0) {
            for (int i = 0; i < sizeList.size(); i++) {
                sizeList.get(i).setSelected(false);
            }
        }
    }

    private void initTypeData(){
        SelectBean bean0 = new SelectBean();
        bean0.setName("V");
        SelectBean bean1 = new SelectBean();
        bean1.setName("F");
        SelectBean bean2 = new SelectBean();
        bean2.setName("C");
        SelectBean bean3 = new SelectBean();
        bean3.setName("P");
        SelectBean bean4 = new SelectBean();
        bean4.setName("O");
        SelectBean bean5 = new SelectBean();
        bean5.setName("A");
        SelectBean bean6 = new SelectBean();
        bean6.setName("Y");
        SelectBean bean7 = new SelectBean();
        bean7.setName("R");
        SelectBean bean8 = new SelectBean();
        bean8.setName("S");
        SelectBean bean9 = new SelectBean();
        bean9.setName("Q");

        typeList.add(bean0);
        typeList.add(bean1);
        typeList.add(bean2);
        typeList.add(bean3);
        typeList.add(bean4);
        typeList.add(bean5);
        typeList.add(bean6);
        typeList.add(bean7);
        typeList.add(bean8);
        typeList.add(bean9);
        adapterType.notifyDataSetChanged();
    }

    private void initSizeData(){
        SelectBean bean0 = new SelectBean();
        bean0.setName("0");
        SelectBean bean1 = new SelectBean();
        bean1.setName("10");
        SelectBean bean2 = new SelectBean();
        bean2.setName("15");
        SelectBean bean3 = new SelectBean();
        bean3.setName("20");
        SelectBean bean4 = new SelectBean();
        bean4.setName("25");
        SelectBean bean5 = new SelectBean();
        bean5.setName("32");

        SelectBean bean6 = new SelectBean();
        bean6.setName("40");
        SelectBean bean7 = new SelectBean();
        bean7.setName("50");
        SelectBean bean8 = new SelectBean();
        bean8.setName("65");
        SelectBean bean9 = new SelectBean();
        bean9.setName("80");
        SelectBean bean10 = new SelectBean();
        bean10.setName("100");
        SelectBean bean11 = new SelectBean();
        bean11.setName("125");

        SelectBean bean12 = new SelectBean();
        bean12.setName("150");
        SelectBean bean13 = new SelectBean();
        bean13.setName("200");
        SelectBean bean14 = new SelectBean();
        bean14.setName("250");
        SelectBean bean15 = new SelectBean();
        bean15.setName("300");
        SelectBean bean16 = new SelectBean();
        bean16.setName("350");
        SelectBean bean17 = new SelectBean();
        bean17.setName("400");

        SelectBean bean18 = new SelectBean();
        bean18.setName("500");
        SelectBean bean19 = new SelectBean();
        bean19.setName("600");
        SelectBean bean20 = new SelectBean();
        bean20.setName("800");
        SelectBean bean21 = new SelectBean();
        bean21.setName("其他");

        sizeList.add(bean0);
        sizeList.add(bean1);
        sizeList.add(bean2);
        sizeList.add(bean3);
        sizeList.add(bean4);
        sizeList.add(bean5);

        sizeList.add(bean6);
        sizeList.add(bean7);
        sizeList.add(bean8);
        sizeList.add(bean9);
        sizeList.add(bean10);
        sizeList.add(bean11);

        sizeList.add(bean12);
        sizeList.add(bean13);
        sizeList.add(bean14);
        sizeList.add(bean15);
        sizeList.add(bean16);
        sizeList.add(bean17);

        sizeList.add(bean18);
        sizeList.add(bean19);
        sizeList.add(bean20);
        sizeList.add(bean21);
        adapterSize.notifyDataSetChanged();
    }

    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener) {
        this.onSelectClickListener = onSelectClickListener;
    }

    public interface OnSelectClickListener {
        void onSelectClick(String type, String heatPre, String size, String count);
    }
}
