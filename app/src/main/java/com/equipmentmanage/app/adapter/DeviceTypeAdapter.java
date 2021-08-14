package com.equipmentmanage.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.bean.DeviceTypeBean;
import com.equipmentmanage.app.utils.StringUtils;
import com.xuexiang.xui.widget.spinner.editspinner.BaseEditSpinnerAdapter;
import com.xuexiang.xui.widget.spinner.editspinner.EditSpinnerFilter;

import java.util.List;

/**
 * @Description: 装置类型-adapter
 * @Author: zzh
 * @CreateDate: 2021/8/14
 */
public class DeviceTypeAdapter extends BaseEditSpinnerAdapter implements EditSpinnerFilter {

    private Context mContext;
    private List<DeviceTypeBean> mList;

    public DeviceTypeAdapter(Context context, List<DeviceTypeBean> list) {
        super(list);
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public EditSpinnerFilter getEditSpinnerFilter() {
        return this;
    }

    @Override
    public String getItemString(int position) {
        return mList.get(position).getText();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList == null ? "" : mList.get(position) == null ? "" : mList.get(position).getText();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_common, parent, false);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        textView.setText(Html.fromHtml(getItem(position)));
        viewHolder.tv_name.setText(StringUtils.nullStrToEmpty(mList.get(position).getText()));
        return convertView;
    }

    @Override
    public boolean onFilter(String keyword) {
        return false;
    }

    private static class ViewHolder {
        private TextView tv_name;

        private ViewHolder() {

        }

        private ViewHolder(TextView tv_name) {
            this.tv_name = tv_name;
        }
    }
}
