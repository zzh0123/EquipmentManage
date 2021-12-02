package com.equipmentmanage.app.fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.activity.AreaManageActivity;
import com.equipmentmanage.app.activity.BaseDataActivity;
import com.equipmentmanage.app.activity.SealPointOnRecordActivity;
import com.equipmentmanage.app.activity.DeviceManageActivity;
import com.equipmentmanage.app.activity.EquipmentManageActivity;
import com.equipmentmanage.app.activity.ProductFlowActivity;
import com.equipmentmanage.app.base.LazyFragment;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.ImgTableBean;
import com.equipmentmanage.app.bean.NetImgBean;
import com.equipmentmanage.app.bean.PointBean;
import com.equipmentmanage.app.bean.PutOnRecordBean;
//import com.equipmentmanage.app.db.utils.DaoUtilsStore;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.DateUtil;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.equipmentmanage.app.view.TipDialog;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
    private int[] icons = {R.mipmap.ic_equipment_manage, R.mipmap.ic_device_manage, R.mipmap.ic_area_manage,
            R.mipmap.ic_equipment_manage, R.mipmap.ic_product_flow, R.mipmap.ic_part_manage, R.mipmap.ic_part_manage};
    // "上传"
    private String[] iconNames = {"基础数据", "装置管理", "区域管理", "设备管理", "产品流", "密封点建档", "上传"};

    private TipDialog tipDialog;

    private String currentDate;

    public PutOnRecordFragment() {
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
        currentDate = DateUtil.getCurentTime1(); // 2021-11-20
//        L.i("zzz1-currentDate-->" + currentDate);
        gridView.setAdapter(new MyGridViewAdapter(getActivity()));

        //
        tipDialog = new TipDialog(getActivity());
        tipDialog.setOnConfirmListener(new TipDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                // TODO: 2021/10/16 上传的方法
                upLoadRecord();
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
                // { "基础数据", "装置管理", "区域管理", "设备管理", "产品流", "密封点建档", "上传"};
                switch (position) {
                    case 0:
                        // 基础数据
                        BaseDataActivity.open(getActivity());
                        break;
                    case 1:
                        // 装置管理
                        DeviceManageActivity.open(getActivity());
                        break;
                    case 2:
                        // 区域管理
                        AreaManageActivity.open(getActivity());
                        break;
                    case 3:
                        // 设备管理
                        EquipmentManageActivity.open(getActivity());
                        break;
                    case 4:
                        // 产品流
                        ProductFlowActivity.open(getActivity());
                        break;
                    case 5:
                        // 密封点建档(组件管理)
                        SealPointOnRecordActivity.open(getActivity());
                        break;
                    case 6:
                        // 上传
                        showUploadDialog();
//                        DeviceManageActivity.open(getActivity());
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

    // 上传建档
    private void upLoadRecord() {
        List<ImgTableBean> list = AppDatabase.getInstance(getActivity()).imgTableDao().loadByDate(currentDate);
//        L.i("zzz1--ImgTableBean.size-->" + list.size());
//        L.i("zzz1--ImgTableBean-->" + GsonUtils.toJson(list));
        upLoadImgList(list);

//        List<PointBean> pointBeans = GsonUtils.fromJson(list.content, new TypeToken<List<PointBean>>() {
//        }.getType());
//
//        for (PointBean pointBean :
//                pointBeans) {
//            L.i("zzz1-pointBean-xy--->" + pointBean.getX() + "---" + pointBean.getY());
//        }
//        L.i("zzz1-PointBeans.list222--->" + pointBeans.size());
    }

    private void upLoadImg(String path, ImgTableBean imgTableBean) {
        L.i("--path--", "--path--" + path);
        Log.i("zzz1-path-->", path);
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Subscribe.uploadImg(part, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                L.i("zzz1-upLoadImg-->" + result);
                Log.i("zzz1-upLoadImg-->", result);

                BaseBean<String> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<String>>() {
                }.getType());
                if (null != baseBean) {
                    if (baseBean.isSuccess()) {
                        //成功
                        Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
                        long currentTimeMillis = System.currentTimeMillis();
                        String url = baseBean.getMessage();
                        List<PointBean> pointBeans = GsonUtils.fromJson(imgTableBean.content, new TypeToken<List<PointBean>>() {
                        }.getType());
                        List<PutOnRecordBean> records = new ArrayList<>();
//                        for (int j = 0; j < pointBeans.size(); j++) {
//                            PutOnRecordBean bean = new PutOnRecordBean();
//                            bean.setDeviceId(imgTableBean.deviceId);  //  所属装置
//                            bean.setAreaId(imgTableBean.areaId); // areaId  所属区域
//                            bean.setEquipmentId(imgTableBean.equipmentId); // equipmentId  所属设备
//                            bean.setPointx(pointBeans.get(j).getX());
//                            bean.setPointy(pointBeans.get(j).getY());
//                            bean.setComponentType("1"); // componentType  密封点 id 法兰
//                            bean.setMediumState(imgTableBean.mediumState); // mediumState  介质状态 id
//                            bean.setProdStream(imgTableBean.prodStream); // prodStream  产品流
//                            bean.setTagNum("" + currentTimeMillis);
//                            bean.setPictPath(StringUtils.nullStrToEmpty(url)); // pictPath 图片路径
//                            bean.setBelongCompany(ConstantValue.belongCompany1);
//                            records.add(bean);
//                        }
//
//                        putOnRecordUpload(records);
                    }
                }


            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toast.makeText(getActivity(), "上传失败：" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity()));
    }

    private void upLoadImgList(List<ImgTableBean> list) {
//        L.i("--path--", "--path--" + path);
//        Log.i("zzz1-path-->", path);
        MultipartBody.Part[] parts = new MultipartBody.Part[list.size()];
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Log.i("zzz1-upLoad-files->", list.get(i).fileName);
                File file = new File(list.get(i).localPath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", list.get(i).fileName, requestFile);
                parts[i] = filePart;
            }
        } else {
            Toasty.warning(getActivity(), "没有要上传的图片！", Toast.LENGTH_SHORT, true).show();
            return;
        }

        Subscribe.uploadImgList(parts, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                L.i("zzz1-upLoadImgList-->" + result);
                Log.i("zzz1-upLoadImgList-->", result);

                BaseBean<List<NetImgBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<NetImgBean>>>() {
                }.getType());
                if (null != baseBean) {
                    if (baseBean.isSuccess()) {
                        //成功
                        Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
                        long currentTimeMillis = System.currentTimeMillis();
//                        String url = baseBean.getMessage();
                        List<PutOnRecordBean> records = new ArrayList<>();
                        List<NetImgBean> netImgBeanList = baseBean.getResult();
                        if (netImgBeanList != null && netImgBeanList.size() > 0) {
                            for (int i = 0; i < netImgBeanList.size(); i++) {
                                ImgTableBean imgTableBean = list.get(i);
                                PutOnRecordBean bean = new PutOnRecordBean();
                                List<PointBean> pointBeans = GsonUtils.fromJson(list.get(i).content, new TypeToken<List<PointBean>>() {
                                }.getType());
                                bean.setBootBuildDetailList(pointBeans);
                                bean.setPictPath(netImgBeanList.get(i).getSavePath()); // pictPath 网络图片路径
                                bean.setBelongCompany(ConstantValue.belongCompany1);

                                bean.setDeviceId(imgTableBean.deviceId);  //  所属装置
                                bean.setAreaId(imgTableBean.areaId); // areaId  所属区域
                                bean.setEquipmentId(imgTableBean.equipmentId); // equipmentId  所属设备
                                bean.setMediumState(imgTableBean.mediumState); // mediumState  介质状态 id
                                bean.setProdStream(imgTableBean.prodStream); // prodStream  产品流
                                bean.setComponentType(imgTableBean.componentType); // componentType  密封点 id 法兰（弹框密封点）
                                bean.setMainMedium(imgTableBean.chemicalName); // 化学品，传name,可以传多个,逗号隔开
                                bean.setDirectionName(imgTableBean.directionName); // 方向

                                bean.setTagNum(imgTableBean.tagNum); // 标签号
                                bean.setRefMaterial(imgTableBean.reference); // 参考物
                                bean.setDistance(imgTableBean.distance); // 距离(米)
                                bean.setHeight(imgTableBean.height); // 高度(米)
                                bean.setFloorLevel(imgTableBean.floorLevel); // 楼层

                                bean.setComponentSize(imgTableBean.size); // 尺寸(mm
                                bean.setManufacturers(imgTableBean.manufacturer); // 生产厂家
//                                bean.setStartTime(imgTableBean.productionDate); // 投产日期
                                bean.setUnreachable(imgTableBean.unreachable); // 不可达
                                bean.setUnreachableDesc(imgTableBean.unreachableReason); // 不可达原因
                                bean.setHeatPreser(imgTableBean.heatPreservation); // 是否保温

                                bean.setOperTemper(imgTableBean.operTemper); // 操作温度(℃)
                                bean.setOperPressure(imgTableBean.operPressure); // 操作压力(MPa
                                bean.setBarCode(imgTableBean.barcode); // 条形码
                                bean.setDetectionFreq(imgTableBean.detectionFreq); // 检测频率/AOV检测频率
                                bean.setThresholdValue(imgTableBean.leakageThreshold); // 泄漏阈值/DPM

                                records.add(bean);

                            }
                        } else {
                            Toasty.error(getActivity(), R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                        }

                        putOnRecordUpload(records);
                    }
                }


            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toast.makeText(getActivity(), "上传失败：" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        }, getActivity()));
    }


    /**
     * 建档数据上传
     */
    private void putOnRecordUpload(List<PutOnRecordBean> records) {
        if (records == null || records.size() <= 0) {
            Toasty.warning(getActivity(), "没有要上传的建档数据！", Toast.LENGTH_SHORT, true).show();
            return;
        }
//        loginPostBean.captcha ="";
//        loginPostBean.checkKey ="";
//        loginPostBean.setUsername(etAccount.getText().toString().trim());
//        loginPostBean.setPassword(etPassword.getText().toString().trim());

//        kv.removeValuesForKeys(new String[]{Constant.token, Constant.userId, Constant.username,
//                Constant.realname, Constant.orgCode, Constant.orgCodeTxt, Constant.telephone,
//                Constant.post});

        Log.i("zzz1-RecordtoJson-->", GsonUtils.toJson(records));
        Subscribe.putOnRecordUpload(records, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    Log.i("zzz1-Recordresult-->", result);
                    BaseBean<String> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<String>>() {
                    }.getType());

                    if (baseBean != null) {
                        if (baseBean.isSuccess()) {
                            Toasty.success(getActivity(), "上传成功", Toast.LENGTH_SHORT, true).show();
                        } else {
                            Toasty.error(getActivity(), "上传失败", Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(getActivity(), R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(getActivity(), R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(getActivity(), R.string.request_fail, Toast.LENGTH_SHORT, true).show();

            }
        }, getActivity()));
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