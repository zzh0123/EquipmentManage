package com.equipmentmanage.app.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.DepartmentAdapter;
import com.equipmentmanage.app.adapter.GridImageAdapter;
import com.equipmentmanage.app.adapter.SelectAreaAdapter;
import com.equipmentmanage.app.adapter.SelectChemicalAdapter;
import com.equipmentmanage.app.adapter.SelectDeviceAdapter;
import com.equipmentmanage.app.adapter.SelectDirectionAdapter;
import com.equipmentmanage.app.adapter.SelectEquipAdapter;
import com.equipmentmanage.app.adapter.SelectStreamAdapter;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseAreaBean;
import com.equipmentmanage.app.bean.BaseAreaTableBean;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.BaseChemicalBean;
import com.equipmentmanage.app.bean.BaseChemicalTableBean;
import com.equipmentmanage.app.bean.BaseComponentTypeBean;
import com.equipmentmanage.app.bean.BaseComponentTypeTableBean;
import com.equipmentmanage.app.bean.BaseDeviceBean;
import com.equipmentmanage.app.bean.BaseDeviceTableBean;
import com.equipmentmanage.app.bean.BaseDirectionBean;
import com.equipmentmanage.app.bean.BaseDirectionTableBean;
import com.equipmentmanage.app.bean.BaseEquipmentBean;
import com.equipmentmanage.app.bean.BaseEquipmentTableBean;
import com.equipmentmanage.app.bean.BaseStreamBean;
import com.equipmentmanage.app.bean.BaseStreamTableBean;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.DateUtil;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 密封点建档(组件管理)
 * @Author: zzh
 * @CreateDate: 2021/10/16
 */
public class SealPointOnRecordActivity extends BaseActivity {

    public static void open(Context c) {
        Intent i = new Intent(c, SealPointOnRecordActivity.class);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏


    @BindView(R.id.ll_device_type)
    LinearLayout ll_device_type; //装置类型

    @BindView(R.id.tv_device_type_name)
    TextView tv_device_type_name; //装置类型

    @BindView(R.id.ll_area_type)
    LinearLayout ll_area_type; //区域

    @BindView(R.id.tv_area_type_name)
    TextView tv_area_type_name; //区域


    @BindView(R.id.ll_equipment_type)
    LinearLayout ll_equipment_type; //设备

    @BindView(R.id.tv_equipment_type_name)
    TextView tv_equipment_type_name; //设备

    @BindView(R.id.ll_stream_type)
    LinearLayout ll_stream_type; //产品流

    @BindView(R.id.tv_stream_type_name)
    TextView tv_stream_type_name; //产品流

    @BindView(R.id.ll_chemical_type)
    LinearLayout ll_chemical_type; //化学品

    @BindView(R.id.tv_chemical_type_name)
    TextView tv_chemical_type_name; //化学品

    @BindView(R.id.ll_direction_type)
    LinearLayout ll_direction_type; //方向

    @BindView(R.id.tv_direction_type_name)
    TextView tv_direction_type_name; //方向

    @BindView(R.id.et_tag_num)
    EditText et_tag_num; //标签号

    @BindView(R.id.et_reference)
    EditText et_reference; //参考物

    @BindView(R.id.et_distance)
    EditText et_distance; //距离(米)

    @BindView(R.id.et_height)
    EditText et_height; //高度(米)

    @BindView(R.id.et_floor_level)
    EditText et_floor_level; //楼层

    @BindView(R.id.et_size)
    EditText et_size; //尺寸(mm)

    @BindView(R.id.et_manufacturer)
    EditText et_manufacturer; //生产厂家

    @BindView(R.id.ll_production_date)
    LinearLayout ll_production_date; //投产日期

    @BindView(R.id.tv_production_date)
    TextView tv_production_date; //投产日期

    @BindView(R.id.ll_unreachable)
    LinearLayout ll_unreachable; //不可达

//    @BindView(R.id.tv_unreachable)
//    TextView tv_unreachable; //不可达

    @BindView(R.id.et_unreachable_reason)
    EditText et_unreachable_reason; //不可达原因

    @BindView(R.id.ll_heat_preservation)
    LinearLayout ll_heat_preservation; //是否保温

//    @BindView(R.id.tv_heat_preservation)
//    TextView tv_heat_preservation; //是否保温

    @BindView(R.id.et_oper_temper)
    EditText et_oper_temper; //操作温度(℃)

    @BindView(R.id.et_oper_pressure)
    EditText et_oper_pressure; //操作压力(MPa)

    @BindView(R.id.et_barcode)
    EditText et_barcode; //条形码

    @BindView(R.id.et_detection_freq)
    EditText et_detection_freq; //检测频率/AOV检测频率

    @BindView(R.id.et_leakage_threshold)
    EditText et_leakage_threshold; //泄漏阈值/DPM

    @BindView(R.id.bt_sure)
    ButtonView bt_sure; //确定


    @BindView(R.id.test)
    TextView test; //点

    // 不可达 是否
    @BindView(R.id.ll_select)
    LinearLayout ll_select;

    @BindView(R.id.iv_selected)
    ImageView iv_selected;

    @BindView(R.id.ll_unselect)
    LinearLayout ll_unselect;

    @BindView(R.id.iv_unselect)
    ImageView iv_unselect;

    // 是否保温 是否
    @BindView(R.id.ll_select1)
    LinearLayout ll_select1;

    @BindView(R.id.iv_selected1)
    ImageView iv_selected1;

    @BindView(R.id.ll_unselect1)
    LinearLayout ll_unselect1;

    @BindView(R.id.iv_unselect1)
    ImageView iv_unselect1;



//    @BindView(R.id.iv_photo)
//    ImageView ivPhoto; //类型

    //选择装置类型
    private ListPopupWindow devicetTypePopupWindow;
    private String devicetTypeName = "";
    private String deviceTypeValue = "";
    private SelectDeviceAdapter selectDeviceAdapter;
    private List<BaseDeviceBean> baseDeviceBeanList = new ArrayList<>();

    //选择区域
    private ListPopupWindow areaTypePopupWindow;
    private String areaTypeName = "";
    private String areaTypeValue = "";
    private SelectAreaAdapter selectAreaAdapter;
    private List<BaseAreaBean> baseAreaBeanList = new ArrayList<>();
    private List<BaseAreaBean> baseAreaBeanListAll = new ArrayList<>();

    //选择设备
    private ListPopupWindow equipmentTypePopupWindow;
    private String equipmentTypeName = "";
    private String equipmentTypeValue = "";
    private SelectEquipAdapter selectEquipAdapter;
    private List<BaseEquipmentBean> baseEquipmentBeanList = new ArrayList<>();
    private List<BaseEquipmentBean> baseEquipmentBeanListAll = new ArrayList<>();

    //选择产品流
    private ListPopupWindow streamTypePopupWindow;
    private String streamTypeName = "";
    private String streamTypeValue = "";
    private String mediumState = "";
    private SelectStreamAdapter selectStreamAdapter;
    private List<BaseStreamBean> baseStreamBeanList = new ArrayList<>();
    private List<BaseStreamBean> baseStreamBeanListAll = new ArrayList<>();

    //选择化学品
    private ListPopupWindow chemicalTypePopupWindow;
    private String chemicalName = "";
    private String chemicalTypeValue = "";
    private SelectChemicalAdapter selectChemicalAdapter;
    private List<BaseChemicalBean> baseChemicalBeanList = new ArrayList<>();

    //选择方向
    private ListPopupWindow directionTypePopupWindow;
    private String directionTypeName = "";
    private String directionTypeValue = "";
    private SelectDirectionAdapter selectDirectionAdapter;
    private List<BaseDirectionBean> baseDirectionBeanList = new ArrayList<>();

    //选择不可达
    private ListPopupWindow unreachableTypePopupWindow;
    private String unreachableTypeName = "";
    private String unreachableTypeValue = "";
    private DepartmentAdapter selectUnreachableAdapter;
    private List<DepartmentBean> unreachableBeanList = new ArrayList<>();

    //选择是否保温
    private ListPopupWindow heatPreTypePopupWindow;
    private String heatPreTypeName = "";
    private String heatPreTypeValue = "";
    private DepartmentAdapter selectHeatPreAdapter;
    private List<DepartmentBean> heatPreBeanList = new ArrayList<>();


    // 组件类型
    private List<BaseComponentTypeBean> baseComponentTypeBeanList = new ArrayList<>();

    /**
     * 装置 区域 设备联动
     * 先选装置 LDAR_BASE_DEVICE
     * 区域  LDAR_BASE_AREA
     * 设备 LDAR_BASE_EQUIPMENT
     */
//    private String code = "LDAR_BASE_DEVICE";
    private String code = "LDAR_BASE_AREA";
    //    private String code = "LDAR_BASE_AREA,AREA_NAME,ID,BELONG_DEVICE='"+deviceId+"'";
    // 装置  备注：展示名称，传值传id

    private String componentType;

    // et_tag_num


    @Override
    protected int initLayout() {
        return R.layout.activity_component_manage;
    }

    @Override
    protected void initView() {
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBar.addAction(new TitleBar.Action() {
            @Override
            public String getText() {
                return null;
            }

            @Override
            public int getDrawable() {
                return R.mipmap.ic_download;
            }

            @Override
            public void performAction(View view) {
                L.i("zzz1--->download");
//                refresh();
//                readCache();
            }

            @Override
            public int leftPadding() {
                return 0;
            }

            @Override
            public int rightPadding() {
                return 0;
            }
        });

//        llType = findViewById(R.id.ll_type);
        // 选择装置类型
        devicetTypePopupWindow = new ListPopupWindow(this);
        devicetTypePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        devicetTypePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        devicetTypePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        devicetTypePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        devicetTypePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        devicetTypePopupWindow.setAnchorView(ll_device_type);
        devicetTypePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        devicetTypePopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        devicetTypePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        selectDeviceAdapter = new SelectDeviceAdapter(this, baseDeviceBeanList);
        devicetTypePopupWindow.setAdapter(selectDeviceAdapter);

        // 选择区域
        areaTypePopupWindow = new ListPopupWindow(this);
        areaTypePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        areaTypePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        areaTypePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        areaTypePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        areaTypePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        areaTypePopupWindow.setAnchorView(ll_area_type);
        areaTypePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        areaTypePopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        areaTypePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        selectAreaAdapter = new SelectAreaAdapter(this, baseAreaBeanList);
        areaTypePopupWindow.setAdapter(selectAreaAdapter);

        // 选择设备类型
        equipmentTypePopupWindow = new ListPopupWindow(this);
        equipmentTypePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        equipmentTypePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        equipmentTypePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        equipmentTypePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        equipmentTypePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        equipmentTypePopupWindow.setAnchorView(ll_equipment_type);
        equipmentTypePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        equipmentTypePopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        equipmentTypePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        selectEquipAdapter = new SelectEquipAdapter(this, baseEquipmentBeanList);
        equipmentTypePopupWindow.setAdapter(selectEquipAdapter);

        // 选择产品流
        streamTypePopupWindow = new ListPopupWindow(this);
        streamTypePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        streamTypePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        streamTypePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        streamTypePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        streamTypePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        streamTypePopupWindow.setAnchorView(ll_stream_type);
        streamTypePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        streamTypePopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        streamTypePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        selectStreamAdapter = new SelectStreamAdapter(this, baseStreamBeanList);
        streamTypePopupWindow.setAdapter(selectStreamAdapter);

        // 选择化学品
        chemicalTypePopupWindow = new ListPopupWindow(this);
        chemicalTypePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        chemicalTypePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        chemicalTypePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        chemicalTypePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        chemicalTypePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        chemicalTypePopupWindow.setAnchorView(ll_chemical_type);
        chemicalTypePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        chemicalTypePopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        chemicalTypePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        selectChemicalAdapter = new SelectChemicalAdapter(this, baseChemicalBeanList);
        chemicalTypePopupWindow.setAdapter(selectChemicalAdapter);

        // 选择方向
        directionTypePopupWindow = new ListPopupWindow(this);
        directionTypePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        directionTypePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        directionTypePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        directionTypePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        directionTypePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        directionTypePopupWindow.setAnchorView(ll_direction_type);
        directionTypePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        directionTypePopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        directionTypePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        selectDirectionAdapter = new SelectDirectionAdapter(this, baseDirectionBeanList);
        directionTypePopupWindow.setAdapter(selectDirectionAdapter);

        // 选择不可达
        unreachableTypePopupWindow = new ListPopupWindow(this);
        unreachableTypePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        unreachableTypePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        unreachableTypePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        unreachableTypePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        unreachableTypePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        unreachableTypePopupWindow.setAnchorView(ll_unreachable);
        unreachableTypePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        unreachableTypePopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        unreachableTypePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        selectUnreachableAdapter = new DepartmentAdapter(this, unreachableBeanList);
        unreachableTypePopupWindow.setAdapter(selectUnreachableAdapter);

        // 选择是否保温
        heatPreTypePopupWindow = new ListPopupWindow(this);
        heatPreTypePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        heatPreTypePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        heatPreTypePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        heatPreTypePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        heatPreTypePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        heatPreTypePopupWindow.setAnchorView(ll_heat_preservation);
        heatPreTypePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        heatPreTypePopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        heatPreTypePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        selectHeatPreAdapter = new DepartmentAdapter(this, heatPreBeanList);
        heatPreTypePopupWindow.setAdapter(selectHeatPreAdapter);

        unreachableTypeValue = ConstantValue.unreachable_type_1;
        heatPreTypeValue = ConstantValue.heatPre_type_1;

        // 图片
//        FullyGridLayoutManager manager = new FullyGridLayoutManager(this,
//                4, GridLayoutManager.VERTICAL, false);
//        rvList.setLayoutManager(manager);
//
//        rvList.addItemDecoration(new GridSpacingItemDecoration(4,
//                ScreenUtils.dip2px(this, 8), false));
//        mAdapter = new GridImageAdapter(this, onAddPicClickListener);
//        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
//            mAdapter.setList(savedInstanceState.getParcelableArrayList("selectorList"));
//        }
    }

    @Override
    protected void initEvent() {

        devicetTypePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseDeviceBean bean = baseDeviceBeanList.get(position);
                if (bean != null) {
                    deviceTypeValue = bean.getId();
                    tv_device_type_name.setText(StringUtils.nullStrToEmpty(bean.getDeviceName()));
                    setAreaData(deviceTypeValue);
                    setStreamData(deviceTypeValue);
                }
                devicetTypePopupWindow.dismiss();
            }
        });

        areaTypePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseAreaBean bean = baseAreaBeanList.get(position);
                if (bean != null) {
                    areaTypeValue = bean.getId();
                    tv_area_type_name.setText(StringUtils.nullStrToEmpty(bean.getAreaName()));
                    setEquipData(deviceTypeValue, areaTypeValue);
                }
                areaTypePopupWindow.dismiss();
            }
        });

        equipmentTypePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseEquipmentBean bean = baseEquipmentBeanList.get(position);
                if (bean != null) {
                    equipmentTypeValue = bean.getId();
                    tv_equipment_type_name.setText(StringUtils.nullStrToEmpty(bean.getEquipmentName()));
                }
                equipmentTypePopupWindow.dismiss();
            }
        });

        streamTypePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseStreamBean bean = baseStreamBeanList.get(position);
                if (bean != null) {
                    streamTypeValue = bean.getId();
                    mediumState = bean.getMediumState();
                    tv_stream_type_name.setText(StringUtils.nullStrToEmpty(bean.getProdStreamName()));
                }
                streamTypePopupWindow.dismiss();
            }
        });

        // 选择化学品
        chemicalTypePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseChemicalBean bean = baseChemicalBeanList.get(position);
                if (bean != null) {
                    chemicalTypeValue = bean.getId();
                    chemicalName = bean.getChemicalsName();
                    tv_chemical_type_name.setText(StringUtils.nullStrToEmpty(bean.getChemicalsName()));
                }
                chemicalTypePopupWindow.dismiss();
            }
        });

        // 方向
        directionTypePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseDirectionBean bean = baseDirectionBeanList.get(position);
                if (bean != null) {
                    directionTypeValue = bean.getDictCode();
                    directionTypeName = bean.getDictName();
                    tv_direction_type_name.setText(StringUtils.nullStrToEmpty(bean.getDictName()));
                }
                directionTypePopupWindow.dismiss();
            }
        });

        // 选择不可达
        unreachableTypePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DepartmentBean bean = unreachableBeanList.get(position);
                if (bean != null) {
                    unreachableTypeValue = bean.getValue();
//                    tv_unreachable.setText(StringUtils.nullStrToEmpty(bean.getName()));
                }
                unreachableTypePopupWindow.dismiss();
            }
        });

        // 选择是否保温
        heatPreTypePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DepartmentBean bean = heatPreBeanList.get(position);
                if (bean != null) {
                    heatPreTypeValue = bean.getValue();
//                    tv_heat_preservation.setText(StringUtils.nullStrToEmpty(bean.getName()));
                }
                heatPreTypePopupWindow.dismiss();
            }
        });

//        readCache(); 报错
    }

    private void initUnreachableData(){
        DepartmentBean bean = new DepartmentBean();
        bean.setName(ConstantValue.unreachable_type_0_name);
        bean.setValue(ConstantValue.unreachable_type_0);
        DepartmentBean bean1 = new DepartmentBean();
        bean1.setName(ConstantValue.unreachable_type_1_name);
        bean1.setValue(ConstantValue.unreachable_type_1);

        unreachableBeanList.add(bean);
        unreachableBeanList.add(bean1);
        selectUnreachableAdapter.notifyDataSetChanged();
    }

    private void initHeatPreData(){
        DepartmentBean bean = new DepartmentBean();
        bean.setName(ConstantValue.heatPre_type_0_name);
        bean.setValue(ConstantValue.heatPre_type_0);
        DepartmentBean bean1 = new DepartmentBean();
        bean1.setName(ConstantValue.heatPre_type_1_name);
        bean1.setValue(ConstantValue.heatPre_type_1);

        heatPreBeanList.add(bean);
        heatPreBeanList.add(bean1);
        selectHeatPreAdapter.notifyDataSetChanged();
    }

    // @BindView(R.id.ll_select1)
    //    LinearLayout ll_select1;
    //
    //    @BindView(R.id.iv_selected1)
    //    ImageView iv_selected1;
    //
    //    @BindView(R.id.ll_unselect1)
    //    LinearLayout ll_unselect1;
    //
    //    @BindView(R.id.iv_unselect1)
    //    ImageView iv_unselect1;
    // R.id.sb_sure
    @SingleClick
    @OnClick({R.id.ll_device_type, R.id.ll_area_type, R.id.ll_equipment_type, R.id.ll_stream_type, R.id.test,
            R.id.ll_chemical_type, R.id.ll_direction_type,
            R.id.ll_production_date, R.id.ll_heat_preservation,
            R.id.bt_sure,
            R.id.ll_select, R.id.ll_unselect,
            R.id.ll_select1, R.id.ll_unselect1})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_select: //不可达  是
                iv_selected.setImageResource(R.mipmap.selected);
                iv_unselect.setImageResource(R.mipmap.unselect);
                unreachableTypeValue = ConstantValue.unreachable_type_1;
                break;
            case R.id.ll_unselect: //不可达  否
                iv_selected.setImageResource(R.mipmap.unselect);
                iv_unselect.setImageResource(R.mipmap.selected);
                unreachableTypeValue = ConstantValue.unreachable_type_0;
                break;
            case R.id.ll_select1: //是否保温  是
                iv_selected1.setImageResource(R.mipmap.selected);
                iv_unselect1.setImageResource(R.mipmap.unselect);
                heatPreTypeValue = ConstantValue.heatPre_type_1;
                break;
            case R.id.ll_unselect1: //是否保温  否
                iv_selected1.setImageResource(R.mipmap.unselect);
                iv_unselect1.setImageResource(R.mipmap.selected);
                heatPreTypeValue = ConstantValue.heatPre_type_0;
                break;
            case R.id.ll_device_type: //装置类型
                readCache();


//                ImgTableBean list = AppDatabase.getInstance(ComponentManageActivity.this).imgTableDao().loadById(compressPath);
////                L.i("zzz1-Area->" + GsonUtils.toJson(list3));
//                List<PointBean> pointBeans = GsonUtils.fromJson(list.content, new TypeToken<List<PointBean>>() {
//                }.getType());
//
//                for (PointBean pointBean:
//                pointBeans) {
//                    L.i("zzz1-pointBean-xy--->" + pointBean.getX()+ "---"+ pointBean.getY());
//                }
//                L.i("zzz1-PointBeans.list222--->" + pointBeans.size());
                break;

            case R.id.ll_area_type: //区域

                break;

            case R.id.ll_equipment_type: //设备

                break;

            case R.id.ll_stream_type: //产品流
                if (baseStreamBeanList != null && baseStreamBeanList.size() > 0) {
                    selectStreamAdapter.notifyDataSetChanged();
                    streamTypePopupWindow.show();
                } else {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先下载产品流的基础数据！", Toast.LENGTH_SHORT, true).show();
                }
                break;

            case R.id.ll_chemical_type: //化学品
                readChemicalCache();
                break;
            case R.id.ll_direction_type: //方向
                readDirectionCache();
                break;

            case R.id.ll_production_date: //投产日期
                Dialog dialog = DateUtil.showDateDialog2(this, tv_production_date);
//                new Handler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        View v = dialog.getWindow().getCurrentFocus();
//                        if (null != v && null != v.getWindowToken()) {
//                            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                        }
//                    }
//                });

                break;
//            case R.id.ll_unreachable: //不可达
//                unreachableTypePopupWindow.show();
//                break;
//            case R.id.ll_heat_preservation: //是否保温
//                heatPreTypePopupWindow.show();
//                break;


            case R.id.bt_sure: //确定
//                TakePhotoActivity.open(this, deviceTypeValue, areaTypeValue, equipmentTypeValue,
//                        mediumState, streamTypeValue, chemicalName, chemicalTypeValue, directionTypeName,
//                        et_tag_num.getText().toString().trim(), et_reference.getText().toString().trim(),
//                        et_distance.getText().toString().trim(), et_height.getText().toString().trim(),
//                        et_floor_level.getText().toString().trim(), et_size.getText().toString().trim(),
//                        et_manufacturer.getText().toString().trim(), tv_production_date.getText().toString().trim(),
//                        unreachableTypeValue, et_unreachable_reason.getText().toString().trim(), heatPreTypeValue,
//                        et_oper_temper.getText().toString().trim(), et_oper_pressure.getText().toString().trim(),
//                        et_barcode.getText().toString().trim());
                if (StringUtils.isNullOrEmpty(deviceTypeValue) || StringUtils.isNullOrEmpty(areaTypeValue)
                        || StringUtils.isNullOrEmpty(equipmentTypeValue)) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先选择装置、区域、设备！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(chemicalTypeValue) || StringUtils.isNullOrEmpty(chemicalName)) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先选择化学品！", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                if (StringUtils.isNullOrEmpty(directionTypeName)) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先选择方向！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(et_tag_num.getText().toString().trim())) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先输入标签号！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(et_reference.getText().toString().trim())) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先输入参考物！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(et_distance.getText().toString().trim())) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先输入距离！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(et_height.getText().toString().trim())) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先输入高度！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(et_floor_level.getText().toString().trim())) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先输入楼层！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(et_size.getText().toString().trim())) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先输入尺寸！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(et_manufacturer.getText().toString().trim())) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先输入生产厂家！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(tv_production_date.getText().toString().trim())) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先选择投产日期！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(unreachableTypeValue)) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先选择是否不可达！", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                // 不可达  是  填写原因
                if (unreachableTypeValue.equals(ConstantValue.unreachable_type_1)){
                    if (StringUtils.isNullOrEmpty(et_unreachable_reason.getText().toString().trim())) {
                        Toasty.warning(SealPointOnRecordActivity.this, "请填写不可达原因！", Toast.LENGTH_SHORT, true).show();
                        return;
                    }
                }

                if (StringUtils.isNullOrEmpty(heatPreTypeValue)) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先选择是否保温！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(et_oper_temper.getText().toString().trim())) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先输入操作温度！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(et_oper_pressure.getText().toString().trim())) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先输入操作压力！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(et_barcode.getText().toString().trim())) {
                    Toasty.warning(SealPointOnRecordActivity.this, "请先输入条形码！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
//                if (StringUtils.isNullOrEmpty(et_detection_freq.getText().toString().trim())) {
//                    Toasty.warning(ComponentManageActivity.this, "请先输入检测频率/AOV检测频率！", Toast.LENGTH_SHORT, true).show();
//                    return;
//                }
//                if (StringUtils.isNullOrEmpty(et_leakage_threshold.getText().toString().trim())) {
//                    Toasty.warning(ComponentManageActivity.this, "请先输入泄漏阈值/DPM！", Toast.LENGTH_SHORT, true).show();
//                    return;
//                }

                TakePhotoActivity.open(this, deviceTypeValue, areaTypeValue, equipmentTypeValue,
                        mediumState, streamTypeValue, chemicalName, chemicalTypeValue, directionTypeName,
                        et_tag_num.getText().toString().trim(), et_reference.getText().toString().trim(),
                        et_distance.getText().toString().trim(), et_height.getText().toString().trim(),
                        et_floor_level.getText().toString().trim(), et_size.getText().toString().trim(),
                        et_manufacturer.getText().toString().trim(), tv_production_date.getText().toString().trim(),
                        unreachableTypeValue, et_unreachable_reason.getText().toString().trim(), heatPreTypeValue,
                        et_oper_temper.getText().toString().trim(), et_oper_pressure.getText().toString().trim(),
                        et_barcode.getText().toString().trim());
                break;

            case R.id.test:
//                if (StringUtils.isNullOrEmpty(deviceTypeValue) || StringUtils.isNullOrEmpty(areaTypeValue)
//                        || StringUtils.isNullOrEmpty(equipmentTypeValue)){
//                    Toasty.warning(ComponentManageActivity.this, "请先选择装置、区域、设备！", Toast.LENGTH_SHORT, true).show();
//                    return;
//                }
//
//                if (!StringUtils.isNullOrEmpty(compressPath)) {
//                    ImgTableBean imgTableBean = new ImgTableBean();
//                    imgTableBean.fileName = fileName;
//                    imgTableBean.localPath = compressPath;
//                    imgTableBean.content = GsonUtils.toJson(pointBeanList);
//                    imgTableBean.deviceId = deviceTypeValue;
//                    imgTableBean.areaId = areaTypeValue;
//                    imgTableBean.equipmentId = equipmentTypeValue;
//                    imgTableBean.mediumState = mediumState;
//                    imgTableBean.prodStream = streamTypeValue;
//                    imgTableBean.componentType = componentType;
//
//                    Long rowId = AppDatabase.getInstance(ComponentManageActivity.this).imgTableDao().insert(imgTableBean);
//                    if (rowId != null){
//                        Toasty.success(ComponentManageActivity.this, "保存成功！", Toast.LENGTH_SHORT, true).show();
//                    } else {
//                        Toasty.error(ComponentManageActivity.this, "保存失败！", Toast.LENGTH_SHORT, true).show();
//                    }
//                } else {
//                    Toasty.warning(ComponentManageActivity.this, "图片路径为空！", Toast.LENGTH_SHORT, true).show();
//                }
                break;
        }
    }

    @Override
    protected void initData() {

        initUnreachableData();
        initHeatPreData();
//        getBaseLinkList();
//        clearCache();

    }

    private void readCache() {
        // 装置
        BaseDeviceTableBean list = AppDatabase.getInstance(SealPointOnRecordActivity.this).baseDeviceTableDao().loadById("1");
//                L.i("zzz1-baseDevice->" + GsonUtils.toJson(list3));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)
        if (list != null) {
            List<BaseDeviceBean> baseDeviceBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseDeviceBean>>() {
            }.getType());
            L.i("zzz1-baseDeviceBeans.list222--->" + baseDeviceBeans.size());

            baseDeviceBeanList.clear();
            if (baseDeviceBeans != null && baseDeviceBeans.size() > 0) {
                baseDeviceBeanList.addAll(baseDeviceBeans);
                selectDeviceAdapter.notifyDataSetChanged();
                devicetTypePopupWindow.show();
            } else {
                Toasty.warning(SealPointOnRecordActivity.this, "请先下载装置的基础数据", Toast.LENGTH_SHORT, true).show();
            }

        } else {
            Toasty.warning(SealPointOnRecordActivity.this, "请先下载装置的基础数据", Toast.LENGTH_SHORT, true).show();
        }

        // 区域
        readAreaCache();
        // 设备
        readEquipCache();
        // 产品流
        readStreamCache();
    }

    private void readAreaCache() {
//        BaseDeviceTableBean list = AppDatabase.getInstance(AreaManageActivity.this).baseDeviceTableDao().loadById("1");
//                L.i("zzz1-baseDevice->" + GsonUtils.toJson(list3));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)

        BaseAreaTableBean list = AppDatabase.getInstance(SealPointOnRecordActivity.this).baseAreaTableDao().loadById("1");
//                L.i("zzz1-Area->" + GsonUtils.toJson(list3));
        if (list != null) {
            List<BaseAreaBean> baseAreaBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseAreaBean>>() {
            }.getType());

            L.i("zzz1-baseAreaBeans.list222--->" + baseAreaBeans.size());

            baseAreaBeanList.clear();
            baseAreaBeanListAll.clear();
            if (baseAreaBeans != null && baseAreaBeans.size() > 0) {
                baseAreaBeanListAll.addAll(baseAreaBeans);
//                selectAreaAdapter.notifyDataSetChanged();
//                areaTypePopupWindow.show();
            } else {
                Toasty.warning(SealPointOnRecordActivity.this, "请先下载区域的基础数据", Toast.LENGTH_SHORT, true).show();
                return;
            }
        } else {
            Toasty.warning(SealPointOnRecordActivity.this, "请先下载区域的基础数据", Toast.LENGTH_SHORT, true).show();
            return;
        }

    }

    private void readEquipCache() {
        BaseEquipmentTableBean list = AppDatabase.getInstance(SealPointOnRecordActivity.this).baseEquipmentTableDao().loadById("1");
        if (list != null) {
            List<BaseEquipmentBean> baseEquipmentBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseEquipmentBean>>() {
            }.getType());

            L.i("zzz1-baseEquipmentBeans.list222--->" + baseEquipmentBeans.size());
            baseEquipmentBeanListAll.clear();
            if (baseEquipmentBeans != null && baseEquipmentBeans.size() > 0) {
                baseEquipmentBeanListAll.addAll(baseEquipmentBeans);
            } else {
                Toasty.warning(SealPointOnRecordActivity.this, "请先下载设备的基础数据", Toast.LENGTH_SHORT, true).show();
                return;
            }
        } else {
            Toasty.warning(SealPointOnRecordActivity.this, "请先下载设备的基础数据", Toast.LENGTH_SHORT, true).show();
            return;
        }

    }

    private void readStreamCache() {
        BaseStreamTableBean list = AppDatabase.getInstance(SealPointOnRecordActivity.this).baseStreamTableDao().loadById("1");
        if (list != null) {
            List<BaseStreamBean> baseStreamBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseStreamBean>>() {
            }.getType());

            L.i("zzz1-baseStreamBeans.list222--->" + baseStreamBeans.size());


            baseStreamBeanListAll.clear();
            if (baseStreamBeans != null && baseStreamBeans.size() > 0) {
                baseStreamBeanListAll.addAll(baseStreamBeans);
            } else {
                Toasty.warning(SealPointOnRecordActivity.this, "请先下载产品流的基础数据", Toast.LENGTH_SHORT, true).show();
                return;
            }
        } else {
            Toasty.warning(SealPointOnRecordActivity.this, "请先下载产品流的基础数据", Toast.LENGTH_SHORT, true).show();
            return;
        }

    }

    private void setStreamData(String deviceId){
        baseStreamBeanList.clear();
        if (baseStreamBeanListAll != null && baseStreamBeanListAll.size() > 0) {
            for (int i = 0; i < baseStreamBeanListAll.size(); i++){
                if (deviceId.equals(baseStreamBeanListAll.get(i).getDeviceId())){
                    baseStreamBeanList.add(baseStreamBeanListAll.get(i));
                }
            }

//            if (baseStreamBeanList != null && baseStreamBeanList.size() > 0) {
//                selectStreamAdapter.notifyDataSetChanged();
//                streamTypePopupWindow.show();
//            } else {
//                Toasty.warning(ComponentManageActivity.this, "产品流数据为空！", Toast.LENGTH_SHORT, true).show();
//            }

        }else {
            Toasty.warning(SealPointOnRecordActivity.this, "产品流数据为空！", Toast.LENGTH_SHORT, true).show();

        }
    }

    private void setAreaData(String deviceId){
        baseAreaBeanList.clear();
        if (baseAreaBeanListAll != null && baseAreaBeanListAll.size() > 0) {
            for (int i = 0; i < baseAreaBeanListAll.size(); i++){
                if (deviceId.equals(baseAreaBeanListAll.get(i).getBelongDevice())){
                    baseAreaBeanList.add(baseAreaBeanListAll.get(i));
                }
            }

            if (baseAreaBeanList != null && baseAreaBeanList.size() > 0) {
                selectAreaAdapter.notifyDataSetChanged();
                areaTypePopupWindow.show();
            } else {
                Toasty.warning(SealPointOnRecordActivity.this, "区域数据为空！", Toast.LENGTH_SHORT, true).show();
            }

        }else {
            Toasty.warning(SealPointOnRecordActivity.this, "区域数据为空！", Toast.LENGTH_SHORT, true).show();

        }
    }

    private void setEquipData(String deviceId, String areaId){
        baseEquipmentBeanList.clear();
        if (baseEquipmentBeanListAll != null && baseEquipmentBeanListAll.size() > 0) {
            for (int i = 0; i < baseEquipmentBeanListAll.size(); i++){
                if (deviceId.equals(baseEquipmentBeanListAll.get(i).getDeviceId())
                    && areaId.equals(baseEquipmentBeanListAll.get(i).getAreaId())){
                    baseEquipmentBeanList.add(baseEquipmentBeanListAll.get(i));
                }
            }

            if (baseEquipmentBeanList != null && baseEquipmentBeanList.size() > 0) {
                selectEquipAdapter.notifyDataSetChanged();
                equipmentTypePopupWindow.show();
            } else {
                Toasty.warning(SealPointOnRecordActivity.this, "设备数据为空！", Toast.LENGTH_SHORT, true).show();
            }

        }else {
            Toasty.warning(SealPointOnRecordActivity.this, "设备数据为空！", Toast.LENGTH_SHORT, true).show();

        }
    }

    // 化学品-缓存
    private void readChemicalCache() {
        BaseChemicalTableBean list = AppDatabase.getInstance(SealPointOnRecordActivity.this).baseChemicalTableDao().loadById("1");
        if (list != null) {
            List<BaseChemicalBean> baseChemicalBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseChemicalBean>>() {
            }.getType());

            L.i("zzz1-baseChemicalBeans.list222--->" + baseChemicalBeans.size());


            baseChemicalBeanList.clear();
            if (baseChemicalBeans != null && baseChemicalBeans.size() > 0) {
                baseChemicalBeanList.addAll(baseChemicalBeans);
                selectChemicalAdapter.notifyDataSetChanged();
                chemicalTypePopupWindow.show();
            } else {
                Toasty.warning(SealPointOnRecordActivity.this, "请先下载化学品的基础数据", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            Toasty.warning(SealPointOnRecordActivity.this, "请先下载化学品的基础数据", Toast.LENGTH_SHORT, true).show();
        }

    }

    // 组件类型-缓存
    private void readComponentTypeCache() {
        BaseComponentTypeTableBean list = AppDatabase.getInstance(SealPointOnRecordActivity.this).baseComponentTypeTableDao().loadById("1");
        if (list != null) {
            List<BaseComponentTypeBean> baseComponentTypeBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseComponentTypeBean>>() {
            }.getType());

            L.i("zzz1-baseComponentTypeBeans.list222--->" + baseComponentTypeBeans.size());


            baseComponentTypeBeanList.clear();
            if (baseComponentTypeBeans != null && baseComponentTypeBeans.size() > 0) {
                baseComponentTypeBeanList.addAll(baseComponentTypeBeans);
            } else {
                Toasty.warning(SealPointOnRecordActivity.this, "请先下载组件类型的基础数据", Toast.LENGTH_SHORT, true).show();
                return;
            }
        } else {
            Toasty.warning(SealPointOnRecordActivity.this, "请先下载组件类型的基础数据", Toast.LENGTH_SHORT, true).show();
            return;
        }

    }

    // 方向-缓存
    private void readDirectionCache() {
        BaseDirectionTableBean list = AppDatabase.getInstance(SealPointOnRecordActivity.this).baseDirectionTableDao().loadById("1");
        if (list != null) {
            List<BaseDirectionBean> baseDirectionBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseDirectionBean>>() {
            }.getType());

            L.i("zzz1-baseDirectionBeans.list222--->" + baseDirectionBeans.size());


            baseDirectionBeanList.clear();
            if (baseDirectionBeans != null && baseDirectionBeans.size() > 0) {
                baseDirectionBeanList.addAll(baseDirectionBeans);
                selectDirectionAdapter.notifyDataSetChanged();
                directionTypePopupWindow.show();
            } else {
                Toasty.warning(SealPointOnRecordActivity.this, "请先下载方向的基础数据", Toast.LENGTH_SHORT, true).show();
                return;
            }
        } else {
            Toasty.warning(SealPointOnRecordActivity.this, "请先下载方向的基础数据", Toast.LENGTH_SHORT, true).show();
            return;
        }

    }

    /**
     * 装置
     * 装置 区域 设备联动
     */
    private void getBaseLinkList() {
        Subscribe.getBaseLinkList(code, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<BaseAreaBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseAreaBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseAreaBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                BaseAreaTableBean baseAreaTableBean = new BaseAreaTableBean();
                                baseAreaTableBean.id = "1";
                                baseAreaTableBean.content = GsonUtils.toJson(dataList);
                                AppDatabase.getInstance(SealPointOnRecordActivity.this).baseAreaTableDao().insert(baseAreaTableBean);

                            } else {
                                Toasty.error(SealPointOnRecordActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(SealPointOnRecordActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(SealPointOnRecordActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(SealPointOnRecordActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(SealPointOnRecordActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, SealPointOnRecordActivity.this));
    }



    private final GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
//            takePhoto();
        }
    };


    public void hideKeyboard(){
//        InputMethodManager imm = (InputMethodManager) view.getContext()
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null) {
//            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
//        }


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.RESULT_UNCHANGED_HIDDEN);

    }

    private boolean isKeyboardOpen(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        L.i("zzz1-isOpen->", "" + isOpen);
        return isOpen;
    }

}