package com.equipmentmanage.app.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.activity.AreaManageActivity;
import com.equipmentmanage.app.activity.DeviceManageActivity;
import com.equipmentmanage.app.activity.EquipmentManageActivity;
import com.equipmentmanage.app.activity.ProductFlowActivity;
import com.equipmentmanage.app.base.LazyFragment;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import butterknife.BindView;

/**
 * @Description: 建档Fragment
 * @Author: zzh
 * @CreateDate: 2021/8/13 17:23
 */
public class PutOnRecordFragment extends LazyFragment {

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.gridView)
    GridView gridView; //gridView

    // 图片封装为一个数组
    private int[] icons = { R.mipmap.ic_device_manage, R.mipmap.ic_area_manage,
            R.mipmap.ic_equipment_manage, R.mipmap.ic_product_flow, R.mipmap.ic_part_manage};
    private String[] iconNames = { "装置管理", "区域管理", "设备管理", "产品流", "组件管理"};


    public PutOnRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static TaskFragment newInstance(String param1, String param2) {
//        TaskFragment fragment = new TaskFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }




    @Override
    protected int getContentViewId() {
        return R.layout.fragment_put_on_record;
    }

    @Override
    protected void initView(View view) {
        gridView.setAdapter(new MyGridViewAdapter(getActivity()));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // { "装置管理", "区域管理", "设备管理", "产品流", "组件管理"};
                switch (position){
                    case 0:
                        // 装置管理
                        DeviceManageActivity.open(getActivity());
                        break;
                    case 1:
                        // 区域管理
                        AreaManageActivity.open(getActivity());
                        break;
                    case 2:
                        // 设备管理
                        EquipmentManageActivity.open(getActivity());
                        break;
                    case 3:
                        // 产品流
                        ProductFlowActivity.open(getActivity());
                        break;
                    case 4:
                        // 组件管理
//                        DeviceManageActivity.open(getActivity());
                        break;
                }
            }
        });

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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.ivIcon = (AppCompatImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // 这里只是模拟，实际开发可能需要加载网络图片，可以使用ImageLoader这样的图片加载框架来异步加载图片
//            imageLoader.displayImage("drawable://" + mThumbIds[position], viewHolder.itemImg);
            viewHolder.ivIcon.setImageResource(icons[position]);
            viewHolder.tvName.setText(iconNames[position]);
            return convertView;
        }

        class ViewHolder {
            AppCompatImageView ivIcon;
            TextView tvName;
        }
    }



}