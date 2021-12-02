package com.equipmentmanage.app.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.activity.AreaManageActivity;
import com.equipmentmanage.app.activity.DeviceManageActivity;
import com.equipmentmanage.app.base.LazyFragment;
import com.equipmentmanage.app.view.TipDialog;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import butterknife.BindView;

//import com.equipmentmanage.app.db.utils.DaoUtilsStore;

/**
 * @Description: 上传下载Fragment
 * @Author: zzh
 * @CreateDate: 2021/10/22
 */
public class UpDownLoadFragment extends LazyFragment {

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.gridView)
    GridView gridView; //gridView

    // 图片封装为一个数组
    private int[] icons = {R.mipmap.ic_equipment_manage, R.mipmap.ic_part_manage};
    private String[] iconNames = {"上传", "下载"};

    private TipDialog tipDialog;

    public UpDownLoadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
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

        //
        tipDialog = new TipDialog(getActivity());
        tipDialog.setOnConfirmListener(new TipDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                // TODO: 2021/10/16 上传的方法
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // { "上传", "下载"};
                switch (position) {
                    case 0:
                        // 上传
                        DeviceManageActivity.open(getActivity());
                        break;
                    case 1:
                        // 下载
                        AreaManageActivity.open(getActivity());
                        break;
                }
            }
        });

    }

    private void showUploadDialog() {
        if (tipDialog == null) {
            tipDialog = new TipDialog(getActivity());
        }
        tipDialog.show();
        tipDialog.setTitleAndTip(null, getString(R.string.upload_tip));
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