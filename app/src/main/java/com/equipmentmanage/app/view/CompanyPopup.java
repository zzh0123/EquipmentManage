package com.equipmentmanage.app.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.CompanyAdapter;
import com.equipmentmanage.app.bean.BarcodeTypeBean;
import com.equipmentmanage.app.utils.L;
import com.xuexiang.xui.widget.popupwindow.easypopup.BaseCustomPopup;

import java.util.ArrayList;
import java.util.List;

public class CompanyPopup extends BaseCustomPopup {

    private EditText popup_keshi_et;
    private RecyclerView popup_keshi_recyclerview;
    private CompanyAdapter adapter;
    private List<BarcodeTypeBean> mList;
    private Context context;
    private List<BarcodeTypeBean> mListSearch;
    private int pageIndex = 1;

    public CompanyPopup(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.popup_keshi1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置为false点击之外的地方不会消失，但是会响应返回按钮事件
        setFocusAndOutsideEnable(true);
        //允许背景变暗
//        .setBackgroundDimEnable(true)
//        .setDimValue(0.1f);
    }


    @Override
    protected void initViews(View view) {
        popup_keshi_et = (EditText) view.findViewById(R.id.popup_keshi_et);
        popup_keshi_recyclerview = (RecyclerView) view.findViewById(R.id.popup_keshi_recyclerview);
        mList = new ArrayList<>();
        mListSearch = new ArrayList<>();
        adapter = new CompanyAdapter(mListSearch);
        popup_keshi_recyclerview.setLayoutManager(new LinearLayoutManager(context));
        popup_keshi_recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                L.i("zzz1-->" + position);
                if (onKeshiItemClick != null) {
                    onKeshiItemClick.onItemClick(adapter, view, position, mListSearch);
                }
                dismiss();
            }
        });

        popup_keshi_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mListSearch.clear();
                if (!TextUtils.isEmpty(popup_keshi_et.getText().toString())) {
                    for (int i = 0; i < mList.size(); i++) {
                        BarcodeTypeBean data = mList.get(i);
                        if (data.getName().contains(popup_keshi_et.getText().toString())) {
                            mListSearch.add(data);
                        } else if (data.getName().startsWith(popup_keshi_et.getText().toString().toUpperCase())) {
                            mListSearch.add(data);
                        }
                    }
                    if (mListSearch.size() == 0) {
                        mListSearch.addAll(mList);
                    }
                } else {
                    mListSearch.addAll(mList);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    //设置数据
    public void setData(List<BarcodeTypeBean> list) {
        mList.clear();
        mListSearch.clear();
        mList.addAll(list);
        mListSearch.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void setEmpty() {
        popup_keshi_et.setText("");
    }

    private OnKeshiItemClick onKeshiItemClick;

    public void setOnKeshiItemClick(OnKeshiItemClick onKeshiItemClick) {
        this.onKeshiItemClick = onKeshiItemClick;
    }

    public interface OnKeshiItemClick {
        void onItemClick(BaseQuickAdapter adapter, View view, int position, List<BarcodeTypeBean> list);

    }
}
