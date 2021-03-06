package com.equipmentmanage.app.activity;

import static com.equipmentmanage.app.base.MyApplication.getContext;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.CommonSelectAdapter;
import com.equipmentmanage.app.adapter.DepartmentAdapter;
import com.equipmentmanage.app.adapter.FloorSelectAdapter;
import com.equipmentmanage.app.adapter.GridImageAdapter;
import com.equipmentmanage.app.adapter.SelectChemicalAdapter;
import com.equipmentmanage.app.adapter.SelectDirectionAdapter;
import com.equipmentmanage.app.adapter.SelectStreamAdapter;
import com.equipmentmanage.app.adapter.SelectUnreachReasonAdapter;
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
import com.equipmentmanage.app.bean.BaseStreamBean;
import com.equipmentmanage.app.bean.BaseStreamTableBean;
import com.equipmentmanage.app.bean.CommonSelectBean;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.bean.ImgTableBean1;
import com.equipmentmanage.app.bean.NewAreaBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.DateUtil;
import com.equipmentmanage.app.utils.GlideCacheEngine;
import com.equipmentmanage.app.utils.GlideEngine;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.equipmentmanage.app.view.AddFloorDialog;
import com.equipmentmanage.app.view.AddProductDialog;
import com.equipmentmanage.app.view.TipDialog;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.tencent.mmkv.MMKV;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.zhouyou.view.seekbar.SignSeekBar;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: ???????????????(????????????)
 * @Author: zzh
 * @CreateDate: 2021/10/16
 */
public class SealPointOnRecordActivity1 extends BaseActivity {

    public static void open(Context c, boolean isNewAdd, ImgTableBean1 imgTableBean1, String deviceCode, String deviceName, String deviceType, String deviceId,
                            String areaCode, String areaName,
                            String equipCode, String equipName) {
        Intent i = new Intent(c, SealPointOnRecordActivity1.class);
        i.putExtra(Constant.isNewAdd, isNewAdd);
        i.putExtra(Constant.imgTableBean1, (Serializable) imgTableBean1);
        i.putExtra(Constant.deviceCode, deviceCode);
        i.putExtra(Constant.deviceName, deviceName);
        i.putExtra(Constant.deviceType, deviceType);
        i.putExtra(Constant.deviceId, deviceId);

        i.putExtra(Constant.areaCode, areaCode);
        i.putExtra(Constant.areaName, areaName);
        i.putExtra(Constant.equipCode, equipCode);
        i.putExtra(Constant.equipName, equipName);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //?????????


    @BindView(R.id.ll_device_type)
    LinearLayout ll_device_type; //????????????

    @BindView(R.id.tv_device_type_name)
    TextView tv_device_type_name; //????????????

    @BindView(R.id.ll_area_type)
    LinearLayout ll_area_type; //??????

    @BindView(R.id.tv_area_type_name)
    TextView tv_area_type_name; //??????


    @BindView(R.id.ll_equipment_type)
    LinearLayout ll_equipment_type; //??????

    @BindView(R.id.tv_equipment_type_name)
    TextView tv_equipment_type_name; //??????

    @BindView(R.id.ll_stream_type)
    LinearLayout ll_stream_type; //?????????

    @BindView(R.id.tv_stream_type_name)
    TextView tv_stream_type_name; //?????????

    @BindView(R.id.ll_chemical_type)
    LinearLayout ll_chemical_type; //?????????

    @BindView(R.id.tv_chemical_type_name)
    TextView tv_chemical_type_name; //?????????

    @BindView(R.id.ll_direction_type)
    LinearLayout ll_direction_type; //??????

    @BindView(R.id.tv_direction_type_name)
    TextView tv_direction_type_name; //??????

    @BindView(R.id.tv_tag_num_pre)
    TextView tv_tag_num_pre; //???????????????

    @BindView(R.id.et_tag_num)
    EditText et_tag_num; //?????????

    @BindView(R.id.et_floor_level)
    TextView et_floor_level; //??????

    @BindView(R.id.et_height)
    TextView et_height; //??????(???)

    @BindView(R.id.et_distance)
    TextView et_distance; //??????(???)

    @BindView(R.id.et_reference)
    EditText et_reference; //?????????

    @BindView(R.id.et_size)
    EditText et_size; //??????(mm)

    @BindView(R.id.et_manufacturer)
    EditText et_manufacturer; //????????????

    @BindView(R.id.ll_production_date)
    LinearLayout ll_production_date; //????????????

    @BindView(R.id.tv_production_date)
    TextView tv_production_date; //????????????

    @BindView(R.id.ll_unreachable)
    LinearLayout ll_unreachable; //?????????

//    @BindView(R.id.tv_unreachable)
//    TextView tv_unreachable; //?????????

    @BindView(R.id.ll_unreachable_reason)
    LinearLayout ll_unreachable_reason; //?????????

    @BindView(R.id.tv_unreachable_reason)
    TextView tv_unreachable_reason; //???????????????

    @BindView(R.id.ll_heat_preservation)
    LinearLayout ll_heat_preservation; //????????????

//    @BindView(R.id.tv_heat_preservation)
//    TextView tv_heat_preservation; //????????????

    @BindView(R.id.et_oper_temper)
    EditText et_oper_temper; //????????????(???)

    @BindView(R.id.et_oper_pressure)
    EditText et_oper_pressure; //????????????(MPa)

    @BindView(R.id.et_barcode)
    EditText et_barcode; //?????????

    @BindView(R.id.et_detection_freq)
    EditText et_detection_freq; //????????????/AOV????????????

    @BindView(R.id.et_leakage_threshold)
    EditText et_leakage_threshold; //????????????/DPM


    @BindView(R.id.bt_continue_position)
    ButtonView bt_continue_position; //????????????

    @BindView(R.id.bt_save)
    ButtonView bt_save; //??????

    @BindView(R.id.bt_take_photo)
    ButtonView bt_take_photo; //??????


    @BindView(R.id.test)
    TextView test; //???

    // ????????? ??????
    @BindView(R.id.ll_select)
    LinearLayout ll_select;

    @BindView(R.id.iv_selected)
    ImageView iv_selected;

    @BindView(R.id.ll_unselect)
    LinearLayout ll_unselect;

    @BindView(R.id.iv_unselect)
    ImageView iv_unselect;

    // ???????????? ??????
    @BindView(R.id.ll_select1)
    LinearLayout ll_select1;

    @BindView(R.id.iv_selected1)
    ImageView iv_selected1;

    @BindView(R.id.ll_unselect1)
    LinearLayout ll_unselect1;

    @BindView(R.id.iv_unselect1)
    ImageView iv_unselect1;


//    @BindView(R.id.iv_photo)
//    ImageView ivPhoto; //??????

    //???????????????
    private ListPopupWindow streamTypePopupWindow;
    private String streamTypeName = "";
    private String streamTypeValue = "";
    private String prodStreamName = "";
    private String mediumState = "";
    private SelectStreamAdapter selectStreamAdapter;
    private List<BaseStreamBean> baseStreamBeanList = new ArrayList<>();
    private List<BaseStreamBean> baseStreamBeanListAll = new ArrayList<>();

    //???????????????
    private ListPopupWindow chemicalTypePopupWindow;
    private String chemicalName = "";
    private String chemicalTypeValue = "";
    private SelectChemicalAdapter selectChemicalAdapter;
    private List<BaseChemicalBean> baseChemicalBeanList = new ArrayList<>();

    //????????????
    private ListPopupWindow directionTypePopupWindow;
    private String directionTypeName = "";
    private String directionTypeValue = "";
    private SelectDirectionAdapter selectDirectionAdapter;
    private List<BaseDirectionBean> baseDirectionBeanList = new ArrayList<>();

    @BindView(R.id.rv_direction)
    RecyclerView rv_direction;
    private CommonSelectAdapter adapterDirection;
    private List<CommonSelectBean> directionList = new ArrayList<>();

    @BindView(R.id.rv_direction1)
    RecyclerView rv_direction1;
    private CommonSelectAdapter adapterDirection1;
    private List<CommonSelectBean> directionList1 = new ArrayList<>();

    @BindView(R.id.tv_direction_type_name1)
    TextView tv_direction_type_name1;

    private String directionTypeName1 = "";
    private String directionTypeValue1 = "";

    //???????????????
    private ListPopupWindow unreachableTypePopupWindow;
    private String unreachableTypeName = "";
    private String unreachableTypeValue = "";
    private DepartmentAdapter selectUnreachableAdapter;
    private List<DepartmentBean> unreachableBeanList = new ArrayList<>();

    //?????????????????????
    private ListPopupWindow unreachReasonPopupWindow;
    private String unreachReasonName = "";
    private SelectUnreachReasonAdapter selectUnreachReasonAdapter;
    private List<String> unreachReasonList = new ArrayList<>();

    //??????????????????
    private ListPopupWindow heatPreTypePopupWindow;
    private String heatPreTypeName = "";
    private String heatPreTypeValue = "";
    private DepartmentAdapter selectHeatPreAdapter;
    private List<DepartmentBean> heatPreBeanList = new ArrayList<>();

    // ??????
    @BindView(R.id.rv_floor)
    RecyclerView rv_floor;
    private FloorSelectAdapter adapterFloor;
    private List<CommonSelectBean> floorList = new ArrayList<>();
    private String floorName = "";
    private String floorValue = "";
    // ?????? ??????
    @BindView(R.id.ll_select_half)
    LinearLayout ll_select_half;

    @BindView(R.id.iv_select_half)
    ImageView iv_select_half;

    private boolean isHalfSelected = false;

    // ?????? ??????
    @BindView(R.id.tv_floor_level_other)
    TextView tv_floor_level_other;

    private AddFloorDialog addFloorDialog;

    // ??????
    @BindView(R.id.seek_bar_height)
    SignSeekBar seek_bar_height;

    // ??????
    @BindView(R.id.seek_bar_distance)
    SignSeekBar seek_bar_distance;
    // ?????? ??????
    @BindView(R.id.tv_distance_change)
    TextView tv_distance_change;

    // ????????????
    @BindView(R.id.et_remark)
    EditText et_remark;


    // ????????????
    private List<BaseComponentTypeBean> baseComponentTypeBeanList = new ArrayList<>();

    /**
     * ?????? ?????? ????????????
     * ???????????? LDAR_BASE_DEVICE
     * ??????  LDAR_BASE_AREA
     * ?????? LDAR_BASE_EQUIPMENT
     */
//    private String code = "LDAR_BASE_DEVICE";
    private String code = "LDAR_BASE_AREA";
    //    private String code = "LDAR_BASE_AREA,AREA_NAME,ID,BELONG_DEVICE='"+deviceId+"'";
    // ??????  ?????????????????????????????????id

    private String componentType;

    // et_tag_num
    private String deviceCode, deviceName, deviceType, deviceId;
    private String areaCode, areaName;
    private String equipCode, equipName;
    // ????????????
    private Boolean isNewAdd = true;
    // ??????????????????
    private String localPath = "";
    private String content = "";
    private ImgTableBean1 imgTableBean1;
    // ??????
    private String mFileName;
    private String currentDate;

    // ????????????
    private ActivityResultLauncher<Intent> launcherResult;
    private PictureWindowAnimationStyle mWindowAnimationStyle = PictureWindowAnimationStyle.ofDefaultWindowAnimationStyle();
    private int maxSelectNum = 1;
    private String compressPath;

    // ???????????????
    @BindView(R.id.iv_add_product)
    RadiusImageView iv_add_product;

    private AddProductDialog addProductDialog;

    private MMKV kv = MMKV.defaultMMKV();

    @Override
    protected int initLayout() {
        return R.layout.activity_component_manage1;
    }

    @Override
    protected void initView() {
        // ??????????????????onCreate???Fragment onAttach??????????????????java.lang.IllegalStateException??????
        launcherResult = createActivityResultLauncher();
        currentDate = DateUtil.getCurentTime1(); // 2021-11-20
        // ????????????
        isNewAdd = getIntent().getBooleanExtra(Constant.isNewAdd, true);
        imgTableBean1 = (ImgTableBean1) getIntent().getSerializableExtra(Constant.imgTableBean1);

        deviceCode = getIntent().getStringExtra(Constant.deviceCode);
        deviceName = getIntent().getStringExtra(Constant.deviceName);
        deviceType = getIntent().getStringExtra(Constant.deviceType);
        deviceId = getIntent().getStringExtra(Constant.deviceId);

        areaCode = getIntent().getStringExtra(Constant.areaCode);
        areaName = getIntent().getStringExtra(Constant.areaName);
        equipCode = getIntent().getStringExtra(Constant.equipCode);
        equipName = getIntent().getStringExtra(Constant.equipName);

        int tagNum = kv.getInt(Constant.tag_num, 0);
        et_tag_num.setText("" + (tagNum + 1));
        tv_tag_num_pre.setText(getTagPre(et_tag_num.getText().toString().trim()));

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        titleBar.addAction(new TitleBar.Action() {
//            @Override
//            public String getText() {
//                return null;
//            }
//
//            @Override
//            public int getDrawable() {
//                return R.mipmap.ic_download;
//            }
//
//            @Override
//            public void performAction(View view) {
//                L.i("zzz1--->download");
////                refresh();
////                readCache();
//            }
//
//            @Override
//            public int leftPadding() {
//                return 0;
//            }
//
//            @Override
//            public int rightPadding() {
//                return 0;
//            }
//        });

        addProductDialog = new AddProductDialog(this);
        addProductDialog.setOnConfirmListener(new AddProductDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String code, String name) {

            }
        });

//        llType = findViewById(R.id.ll_type);
        // ??????,??????,??????
        tv_device_type_name.setText(deviceName);
        tv_area_type_name.setText(areaName);
        tv_equipment_type_name.setText(equipName);

        // ???????????????
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

        // ???????????????
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

        // ????????????
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

        // ??????
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rv_floor.setLayoutManager(staggeredGridLayoutManager);
        adapterFloor = new FloorSelectAdapter(floorList);
        adapterFloor.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                CommonSelectBean bean = floorList.get(position);
                if (bean != null) {
                    clearSelectFloor();
                    bean.setSelected(true);
                    adapterFloor.notifyDataSetChanged();


                    if (isHalfSelected) {
                        floorValue = "" + (Integer.parseInt(bean.getValue()) + 0.5);
                    } else {
                        floorValue = bean.getValue();
                    }

//                    floorName = bean.getName();
                    et_floor_level.setText(StringUtils.nullStrToEmpty(floorValue));
                }

            }
        });
        rv_floor.setAdapter(adapterFloor);
        initFloorData();

        addFloorDialog = new AddFloorDialog(this);
        addFloorDialog.setOnConfirmListener(new AddFloorDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String floorLevel) {
//                List<TaskBean> list = DaoUtilsStore.getInstance().getUserDaoUtils().queryAll();
//                L.i("zzz1--->size--" + list.size());
                L.i("zzz1--->floorLevel--" + floorLevel);
                et_floor_level.setText(floorLevel);
                floorValue = floorLevel;
                addFloorDialog.dismiss();
            }
        });

        // ??????
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rv_direction.setLayoutManager(gridLayoutManager);
        adapterDirection = new CommonSelectAdapter(directionList);
        adapterDirection.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                CommonSelectBean bean = directionList.get(position);
                if (bean != null) {
                    clearSelectDirection();
                    bean.setSelected(true);
                    adapterDirection.notifyDataSetChanged();

                    directionTypeValue = bean.getValue();
                    directionTypeName = bean.getName();
                    tv_direction_type_name.setText(StringUtils.nullStrToEmpty(bean.getName()));
                }

            }
        });
        rv_direction.setAdapter(adapterDirection);
        initDirectionData();

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 2);
        rv_direction1.setLayoutManager(gridLayoutManager1);
        adapterDirection1 = new CommonSelectAdapter(directionList1);
        adapterDirection1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                CommonSelectBean bean = directionList1.get(position);
                if (bean != null) {
                    clearSelectDirection1();
                    bean.setSelected(true);
                    adapterDirection1.notifyDataSetChanged();

                    directionTypeValue1 = bean.getValue();
                    directionTypeName1 = bean.getName();
                    tv_direction_type_name1.setText(StringUtils.nullStrToEmpty(bean.getName()));
                }

            }
        });
        rv_direction1.setAdapter(adapterDirection1);
        initDirectionData1();

        // ???????????????
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

        // ?????????????????????
        unreachReasonPopupWindow = new ListPopupWindow(this);
        unreachReasonPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        unreachReasonPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        unreachReasonPopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        unreachReasonPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        unreachReasonPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        unreachReasonPopupWindow.setAnchorView(ll_unreachable_reason);
        unreachReasonPopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        unreachReasonPopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        unreachReasonPopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        selectUnreachReasonAdapter = new SelectUnreachReasonAdapter(this, unreachReasonList);
        unreachReasonPopupWindow.setAdapter(selectUnreachReasonAdapter);
        initReasonData();

        // ??????????????????
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

        unreachableTypeValue = ConstantValue.unreachable_type_0;
        heatPreTypeValue = ConstantValue.heatPre_type_1;

        // ??????
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

    private void showAddProductDialog() {
        if (addProductDialog == null) {
            addProductDialog = new AddProductDialog(this);
        }
        addProductDialog.show();
    }

    private void setView() {
//        String currentDate = DateUtil.getCurentTime1();
//        List<ImgTableBean1> list = AppDatabase.getInstance(SealPointOnRecordActivity1.this)
//                .imgTableDao1()
//                .loadByDateAndEquip(currentDate, deviceId, areaCode, equipCode);
        if (imgTableBean1 != null) {
            tv_device_type_name.setText(StringUtils.nullStrToEmpty(imgTableBean1.deviceName));
            tv_area_type_name.setText(StringUtils.nullStrToEmpty(imgTableBean1.areaName));
            tv_equipment_type_name.setText(StringUtils.nullStrToEmpty(imgTableBean1.equipName));

            streamTypeValue = imgTableBean1.prodStream;
            prodStreamName = imgTableBean1.prodStreamName;
            mediumState = imgTableBean1.mediumState;
            tv_stream_type_name.setText(StringUtils.nullStrToEmpty(imgTableBean1.prodStreamName));

            chemicalTypeValue = imgTableBean1.mainMedium;
            chemicalName = imgTableBean1.chemicalName;
            tv_chemical_type_name.setText(StringUtils.nullStrToEmpty(imgTableBean1.chemicalName));

            String tagNumPre = imgTableBean1.tagNumPre;
            tv_tag_num_pre.setText(StringUtils.nullStrToEmpty(tagNumPre));
            String tagNumValue = imgTableBean1.tagNumValue;
            et_tag_num.setText(StringUtils.nullStrToEmpty(tagNumValue));

            // ??????
            floorValue = imgTableBean1.floorLevel;
            String floorValueInt = floorValue;
            if (!StringUtils.isNullOrEmpty(floorValue)) {
                if (floorValue.contains(".5")) {
                    isHalfSelected = true;
                    iv_select_half.setImageResource(R.mipmap.selected);
                    floorValueInt = floorValueInt.substring(0, 1);
                } else {
                    isHalfSelected = false;
                    iv_select_half.setImageResource(R.mipmap.unselect);
                }

                for (int i = 0; i < floorList.size(); i++) {
                    if (floorValueInt.equals(floorList.get(i).getValue())) {
                        floorList.get(i).setSelected(true);
                        adapterFloor.notifyDataSetChanged();
                        break;
                    }

                }
            }
            et_floor_level.setText(StringUtils.nullStrToEmpty(imgTableBean1.floorLevel));

            // ??????
            et_height.setText(StringUtils.nullStrToEmpty(imgTableBean1.height));
            seek_bar_height.setProgress(Float.parseFloat(imgTableBean1.height));

            // ??????
            et_distance.setText(StringUtils.nullStrToEmpty(imgTableBean1.distance));
            seek_bar_distance.setProgress(Float.parseFloat(imgTableBean1.distance));

            et_reference.setText(StringUtils.nullStrToEmpty(imgTableBean1.refMaterial));

            directionTypeName = imgTableBean1.directionName;
            directionTypeValue = imgTableBean1.directionValue;
            tv_direction_type_name.setText(StringUtils.nullStrToEmpty(directionTypeName));
            if (!StringUtils.isNullOrEmpty(directionTypeValue)) {
                clearSelectDirection();
                for (int i = 0; i < directionList.size(); i++) {
                    if (directionTypeValue.equals(directionList.get(i).getValue())) {
                        directionList.get(i).setSelected(true);
                        adapterDirection.notifyDataSetChanged();
                        break;
                    }

                }
            }

            directionTypeName1 = imgTableBean1.directionPosName;
            directionTypeValue1 = imgTableBean1.directionPosValue;
            tv_direction_type_name1.setText(directionTypeName1);
            if (!StringUtils.isNullOrEmpty(directionTypeValue1)) {
                clearSelectDirection1();
                for (int i = 0; i < directionList1.size(); i++) {
                    if (directionTypeValue1.equals(directionList1.get(i).getValue())) {
                        directionList1.get(i).setSelected(true);
                        adapterDirection1.notifyDataSetChanged();
                        break;
                    }
                }
            }


            unreachableTypeValue = imgTableBean1.unreachable;
            if (unreachableTypeValue.equals(ConstantValue.unreachable_type_1)) {
                iv_selected.setImageResource(R.mipmap.selected);
                iv_unselect.setImageResource(R.mipmap.unselect);
                unreachReasonName = imgTableBean1.unreachableDesc;
            } else {
                iv_selected.setImageResource(R.mipmap.unselect);
                iv_unselect.setImageResource(R.mipmap.selected);
            }
            tv_unreachable_reason.setText(StringUtils.nullStrToEmpty(imgTableBean1.unreachableDesc));
            et_remark.setText(StringUtils.nullStrToEmpty(imgTableBean1.remark));
        }
    }

    private void initReasonData() {
        unreachReasonList.add("????????????2???");
        unreachReasonList.add("??????????????????????????????????????????");
        unreachReasonList.add("?????????????????????");
        unreachReasonList.add("???????????????????????????");
        unreachReasonList.add("??????");
        selectUnreachReasonAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initEvent() {

        et_tag_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.isNullString(s.toString().trim())) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (s.length() > 5) {
                                Toasty.warning(SealPointOnRecordActivity1.this, "????????????5??????", Toast.LENGTH_SHORT, false).show();
                                return;
                            }
                            tv_tag_num_pre.setText(getTagPre(s.toString().trim()));
                        }
                    }, 600);

                }
            }
        });

        streamTypePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseStreamBean bean = baseStreamBeanList.get(position);
                if (bean != null) {
//                    streamTypeValue = bean.getId();
                    streamTypeValue = bean.getProdStreamCode();
                    prodStreamName = bean.getProdStreamName();
                    mediumState = bean.getMediumState();
                    tv_stream_type_name.setText(StringUtils.nullStrToEmpty(bean.getProdStreamName()));
                }
                streamTypePopupWindow.dismiss();
            }
        });

        // ???????????????
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

        // ??????
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

        // ???????????????
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

        // ?????????????????????
        unreachReasonPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bean = unreachReasonList.get(position);
                if (bean != null) {
                    unreachReasonName = bean;
                    tv_unreachable_reason.setText(StringUtils.nullStrToEmpty(unreachReasonName));
                }
                unreachReasonPopupWindow.dismiss();
            }
        });

        // ??????????????????
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

        // ??????
        seek_bar_height.getConfigBuilder()
                .min(0)
                .max(5)
                .progress(0)
                .sectionCount(4)
                .trackColor(ContextCompat.getColor(this, R.color.color_gray))
                .secondTrackColor(ContextCompat.getColor(this, R.color.color_blue))
                .thumbColor(ContextCompat.getColor(this, R.color.color_blue))
                .sectionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .sectionTextSize(16)
                .thumbTextColor(ContextCompat.getColor(this, R.color.color_red))
                .thumbTextSize(18)
                .signColor(ContextCompat.getColor(this, R.color.color_green))
                .signTextSize(18)
//                .autoAdjustSectionMark()
                .sectionTextPosition(SignSeekBar.TextPosition.BELOW_SECTION_MARK)
                .build();

        seek_bar_height.setOnProgressChangedListener(new SignSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {
                //fromUser ??????????????????????????? ???????????????touch????????????
//                String s = String.format(Locale.CHINA, "onChanged int:%d, float:%.1f", progress, progressFloat);
                et_height.setText("" + progressFloat);
            }

            @Override
            public void getProgressOnActionUp(SignSeekBar signSeekBar, int progress, float progressFloat) {
//                String s = String.format(Locale.CHINA, "onActionUp int:%d, float:%.1f", progress, progressFloat);
//                et_height.setText("U-" + s);
            }

            @Override
            public void getProgressOnFinally(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {
//                String s = String.format(Locale.CHINA, "onFinally int:%d, float:%.1f", progress, progressFloat);
//                et_height.setText("Finally-" +s);
            }
        });

        // ??????
        seek_bar_distance.getConfigBuilder()
                .min(0)
                .max(2)
                .progress(0)
                .sectionCount(5)
                .trackColor(ContextCompat.getColor(this, R.color.color_gray))
                .secondTrackColor(ContextCompat.getColor(this, R.color.color_blue))
                .thumbColor(ContextCompat.getColor(this, R.color.color_blue))
                .sectionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .sectionTextSize(16)
                .thumbTextColor(ContextCompat.getColor(this, R.color.color_red))
                .thumbTextSize(18)
                .signColor(ContextCompat.getColor(this, R.color.color_green))
                .signTextSize(18)
//                .autoAdjustSectionMark()
                .sectionTextPosition(SignSeekBar.TextPosition.BELOW_SECTION_MARK)
                .build();

        seek_bar_distance.setOnProgressChangedListener(new SignSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {
                //fromUser ??????????????????????????? ???????????????touch????????????
//                String s = String.format(Locale.CHINA, "onChanged int:%d, float:%.1f", progress, progressFloat);
                et_distance.setText("" + progressFloat);
            }

            @Override
            public void getProgressOnActionUp(SignSeekBar signSeekBar, int progress, float progressFloat) {
//                String s = String.format(Locale.CHINA, "onActionUp int:%d, float:%.1f", progress, progressFloat);
//                et_height.setText("U-" + s);
            }

            @Override
            public void getProgressOnFinally(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {
//                String s = String.format(Locale.CHINA, "onFinally int:%d, float:%.1f", progress, progressFloat);
//                et_height.setText("Finally-" +s);
            }
        });

        // ??????
        if (!isNewAdd) {
            setView();
            //                String fileName = "";
            localPath = imgTableBean1.localPath;
            // ??????
            mFileName = imgTableBean1.fileName;
            content = imgTableBean1.content;

        } else {
            mFileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        }
//        L.i("zzz1--mFileName->" + isNewAdd + "---" + mFileName);
//        readCache(); ??????
    }

    private String getTagPre(String s) {
        String preStr = ConstantValue.tag_num;

        if (!StringUtils.isNullOrEmpty(s)) {
            int preInt = Integer.parseInt(s);
            if (preInt >= 0 && preInt < 10) {
                preStr = ConstantValue.tag_num;
            } else if (preInt >= 10 && preInt < 100) {
                preStr = ConstantValue.tag_num3;
            } else if (preInt >= 100 && preInt < 1000) {
                preStr = ConstantValue.tag_num2;
            } else if (preInt >= 1000 && preInt < 10000) {
                preStr = ConstantValue.tag_num1;
            } else if (preInt >= 10000) {
                preStr = ConstantValue.tag_num0;
            }
        }

        return preStr;
    }


    /**
     * ??????
     * 1, 2...8
     */
    private void initFloorData() {
        CommonSelectBean bean1 = new CommonSelectBean();
        bean1.setName("1");
        bean1.setValue("1");
        CommonSelectBean bean2 = new CommonSelectBean();
        bean2.setName("2");
        bean2.setValue("2");
        CommonSelectBean bean3 = new CommonSelectBean();
        bean3.setName("3");
        bean3.setValue("3");
        CommonSelectBean bean4 = new CommonSelectBean();
        bean4.setName("4");
        bean4.setValue("4");

        CommonSelectBean bean5 = new CommonSelectBean();
        bean5.setName("5");
        bean5.setValue("5");
        CommonSelectBean bean6 = new CommonSelectBean();
        bean6.setName("6");
        bean6.setValue("6");
        CommonSelectBean bean7 = new CommonSelectBean();
        bean7.setName("7");
        bean7.setValue("7");
        CommonSelectBean bean8 = new CommonSelectBean();
        bean8.setName("8");
        bean8.setValue("8");

        floorList.add(bean1);
        floorList.add(bean2);
        floorList.add(bean3);
        floorList.add(bean4);

        floorList.add(bean5);
        floorList.add(bean6);
        floorList.add(bean7);
        floorList.add(bean8);

        adapterFloor.notifyDataSetChanged();
    }

    private void clearSelectFloor() {
        if (floorList != null && floorList.size() > 0) {
            for (int i = 0; i < floorList.size(); i++) {
                floorList.get(i).setSelected(false);
            }
        }
    }

    private void showAddFloorDialog() {
        if (addFloorDialog == null) {
            addFloorDialog = new AddFloorDialog(this);
        }
        addFloorDialog.show();
    }

    /**
     * ???E ???S ???W ???N ??????EN ??????ES ??????WN ??????WS
     * <p>
     * ???U ???D ??????T ??????B
     */
    private void initDirectionData() {
        CommonSelectBean bean = new CommonSelectBean();
        bean.setName("??????");
        bean.setValue("WN");
        CommonSelectBean bean1 = new CommonSelectBean();
        bean1.setName("???");
        bean1.setValue("N");
        CommonSelectBean bean2 = new CommonSelectBean();
        bean2.setName("??????");
        bean2.setValue("EN");

        CommonSelectBean bean3 = new CommonSelectBean();
        bean3.setName("???");
        bean3.setValue("W");
        CommonSelectBean bean4 = new CommonSelectBean();
        bean4.setName("");
        bean4.setValue("");
        CommonSelectBean bean5 = new CommonSelectBean();
        bean5.setName("???");
        bean5.setValue("E");

        CommonSelectBean bean6 = new CommonSelectBean();
        bean6.setName("??????");
        bean6.setValue("WS");
        CommonSelectBean bean7 = new CommonSelectBean();
        bean7.setName("???");
        bean7.setValue("S");
        CommonSelectBean bean8 = new CommonSelectBean();
        bean8.setName("??????");
        bean8.setValue("ES");

        directionList.add(bean);
        directionList.add(bean1);
        directionList.add(bean2);
        directionList.add(bean3);
        directionList.add(bean4);
        directionList.add(bean5);
        directionList.add(bean6);
        directionList.add(bean7);
        directionList.add(bean8);
        adapterDirection.notifyDataSetChanged();
    }

    /**
     * ???U ???D ??????T ??????B
     */
    private void initDirectionData1() {
        CommonSelectBean bean = new CommonSelectBean();
        bean.setName("???");
        bean.setValue("U");
        CommonSelectBean bean1 = new CommonSelectBean();
        bean1.setName("??????");
        bean1.setValue("T");

        CommonSelectBean bean2 = new CommonSelectBean();
        bean2.setName("???");
        bean2.setValue("D");
        CommonSelectBean bean3 = new CommonSelectBean();
        bean3.setName("??????");
        bean3.setValue("B");

        directionList1.add(bean);
        directionList1.add(bean1);
        directionList1.add(bean2);
        directionList1.add(bean3);
        adapterDirection1.notifyDataSetChanged();
    }

    private void clearSelectDirection() {
        if (directionList != null && directionList.size() > 0) {
            for (int i = 0; i < directionList.size(); i++) {
                directionList.get(i).setSelected(false);
            }
        }
    }

    private void clearSelectDirection1() {
        if (directionList1 != null && directionList1.size() > 0) {
            for (int i = 0; i < directionList1.size(); i++) {
                directionList1.get(i).setSelected(false);
            }
        }
    }

    private void initUnreachableData() {
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

    private void initHeatPreData() {
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
    @OnClick({R.id.iv_add_product, R.id.ll_unreachable_reason, R.id.ll_select_half, R.id.tv_distance_change, R.id.ll_device_type, R.id.ll_area_type, R.id.ll_equipment_type, R.id.ll_stream_type, R.id.test,
            R.id.ll_chemical_type, R.id.ll_direction_type,
            R.id.ll_production_date, R.id.ll_heat_preservation,
            R.id.bt_continue_position, R.id.bt_save, R.id.bt_take_photo,
            R.id.ll_select, R.id.ll_unselect,
            R.id.ll_select1, R.id.ll_unselect1})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_product:  //???????????????
                showAddProductDialog();
                break;

            case R.id.ll_unreachable_reason:  //???????????????
                unreachReasonPopupWindow.show();
                break;

            case R.id.ll_select_half:  //?????? ??????
                if (isHalfSelected) {
                    isHalfSelected = false;
                    iv_select_half.setImageResource(R.mipmap.unselect);

                    floorValue = "" + (int) (Float.parseFloat(floorValue) - 0.5);
                    et_floor_level.setText(StringUtils.nullStrToEmpty(floorValue));
                } else {
                    isHalfSelected = true;
                    iv_select_half.setImageResource(R.mipmap.selected);
                    if (!StringUtils.isNullOrEmpty(floorValue)) {
                        floorValue = "" + (Integer.parseInt(floorValue) + 0.5);
                    } else {
                        floorValue = "0.5";
                    }
                    et_floor_level.setText(StringUtils.nullStrToEmpty(floorValue));
                }

                break;

            case R.id.tv_floor_level_other: //?????? ??????
                showAddFloorDialog();
                break;

            case R.id.tv_distance_change: // ????????????
                // 4, 2, 10, 100
                seek_bar_distance.getConfigBuilder()
                        .min(0)
                        .max(1000)
                        .progress(0)
                        .sectionCount(4)
                        .trackColor(ContextCompat.getColor(this, R.color.color_gray))
                        .secondTrackColor(ContextCompat.getColor(this, R.color.color_blue))
                        .thumbColor(ContextCompat.getColor(this, R.color.color_blue))
                        .sectionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .sectionTextSize(16)
                        .thumbTextColor(ContextCompat.getColor(this, R.color.color_red))
                        .thumbTextSize(18)
                        .signColor(ContextCompat.getColor(this, R.color.color_green))
                        .signTextSize(18)
//                .autoAdjustSectionMark()
                        .sectionTextPosition(SignSeekBar.TextPosition.BELOW_SECTION_MARK)
                        .build();
                break;
            case R.id.ll_select: //?????????  ???
                iv_selected.setImageResource(R.mipmap.selected);
                iv_unselect.setImageResource(R.mipmap.unselect);
                unreachableTypeValue = ConstantValue.unreachable_type_1;
                break;
            case R.id.ll_unselect: //?????????  ???
                iv_selected.setImageResource(R.mipmap.unselect);
                iv_unselect.setImageResource(R.mipmap.selected);
                unreachableTypeValue = ConstantValue.unreachable_type_0;
                break;
            case R.id.ll_select1: //????????????  ???
                iv_selected1.setImageResource(R.mipmap.selected);
                iv_unselect1.setImageResource(R.mipmap.unselect);
                heatPreTypeValue = ConstantValue.heatPre_type_1;
                break;
            case R.id.ll_unselect1: //????????????  ???
                iv_selected1.setImageResource(R.mipmap.unselect);
                iv_unselect1.setImageResource(R.mipmap.selected);
                heatPreTypeValue = ConstantValue.heatPre_type_0;
                break;
            case R.id.ll_device_type: //????????????
//                readCache();


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

            case R.id.ll_area_type: //??????

                break;

            case R.id.ll_equipment_type: //??????

                break;

            case R.id.ll_stream_type: //?????????
//                if (baseStreamBeanList != null && baseStreamBeanList.size() > 0) {
//                    selectStreamAdapter.notifyDataSetChanged();
//                    streamTypePopupWindow.show();
//                } else {
//                    Toasty.warning(SealPointOnRecordActivity1.this, "???????????????????????????????????????", Toast.LENGTH_SHORT, true).show();
//                }
                readStreamCache();
                break;

            case R.id.ll_chemical_type: //?????????
                readChemicalCache();
                break;
            case R.id.ll_direction_type: //??????
//                readDirectionCache();

                break;

            case R.id.ll_production_date: //????????????
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
//            case R.id.ll_unreachable: //?????????
//                unreachableTypePopupWindow.show();
//                break;
//            case R.id.ll_heat_preservation: //????????????
//                heatPreTypePopupWindow.show();
//                break;
            case R.id.bt_continue_position: //????????????
                readCurrentTemp();
                break;

            case R.id.bt_save: //??????
                if (isValid()) {
                    save();
                }
                break;

            case R.id.bt_take_photo: //??????
//                TakePhotoActivity1.open(this, deviceCode, deviceName, deviceType, deviceId,
//                        areaCode, areaName, equipCode, equipName,
//                        mediumState, streamTypeValue, chemicalName, chemicalTypeValue,
//                        "directionTypeNameAll", "directionTypeValueAll",
//                        et_tag_num.getText().toString().trim(), et_reference.getText().toString().trim(),
//                        et_distance.getText().toString().trim(), et_height.getText().toString().trim(),
//                        floorValue,
//                        unreachableTypeValue, tv_unreachable_reason.getText().toString().trim(),
//                        et_remark.getText().toString().trim());


                // ????????????-->??????
                if (isValid()) {
                    // ??????
                    if (!isNewAdd) {
                        if (StringUtils.isNullOrEmpty(localPath)) {
                            takePhoto();
                        } else {
                            openTakePhotoActivity();
                        }

                    } else {
                        takePhoto();
                    }

                }


//                if (isValid()) {
////                    L.i("zzz1--floorValue->" + floorValue);
//                    kv.encode(Constant.tag_num, Integer.parseInt(et_tag_num.getText().toString().trim()));
//
//                    String tagNum = ConstantValue.tag_num + et_tag_num.getText().toString().trim();
//
//                    TakePhotoActivity1.open(this, isNewAdd, mFileName, localPath, content, deviceCode, deviceName, deviceType, deviceId,
//                            areaCode, areaName, equipCode, equipName,
//                            mediumState, streamTypeValue, prodStreamName, chemicalName, chemicalTypeValue,
//                            directionTypeName, directionTypeValue, directionTypeName1, directionTypeValue1,
//                            tagNum, et_reference.getText().toString().trim(),
//                            et_distance.getText().toString().trim(), et_height.getText().toString().trim(),
//                            floorValue,
//                            unreachableTypeValue, unreachReasonName,
//                            et_remark.getText().toString().trim());
//                    finish();
//                }
                break;

            case R.id.test:
//                if (StringUtils.isNullOrEmpty(deviceTypeValue) || StringUtils.isNullOrEmpty(areaTypeValue)
//                        || StringUtils.isNullOrEmpty(equipmentTypeValue)){
//                    Toasty.warning(ComponentManageActivity.this, "???????????????????????????????????????", Toast.LENGTH_SHORT, true).show();
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
//                        Toasty.success(ComponentManageActivity.this, "???????????????", Toast.LENGTH_SHORT, true).show();
//                    } else {
//                        Toasty.error(ComponentManageActivity.this, "???????????????", Toast.LENGTH_SHORT, true).show();
//                    }
//                } else {
//                    Toasty.warning(ComponentManageActivity.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
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


    private boolean isValid() {
        boolean isRight = true;
        if (StringUtils.isNullOrEmpty(deviceCode) || StringUtils.isNullOrEmpty(areaCode)
                || StringUtils.isNullOrEmpty(equipCode)) {
            Toasty.warning(SealPointOnRecordActivity1.this, "???????????????????????????????????????", Toast.LENGTH_SHORT, true).show();
            isRight = false;
            return isRight;
        }

        if (StringUtils.isNullOrEmpty(streamTypeValue) || StringUtils.isNullOrEmpty(prodStreamName)) {
            Toasty.warning(SealPointOnRecordActivity1.this, "????????????????????????", Toast.LENGTH_SHORT, true).show();
            isRight = false;
            return isRight;
        }

        if (StringUtils.isNullOrEmpty(chemicalTypeValue) || StringUtils.isNullOrEmpty(chemicalName)) {
            Toasty.warning(SealPointOnRecordActivity1.this, "????????????????????????", Toast.LENGTH_SHORT, true).show();
            isRight = false;
            return isRight;
        }

        if (StringUtils.isNullOrEmpty(directionTypeName)) {
            Toasty.warning(SealPointOnRecordActivity1.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
            isRight = false;
            return isRight;
        }

        if (StringUtils.isNullOrEmpty(directionTypeName1)) {
            Toasty.warning(SealPointOnRecordActivity1.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
            isRight = false;
            return isRight;
        }

        if (StringUtils.isNullOrEmpty(et_tag_num.getText().toString().trim())) {
            Toasty.warning(SealPointOnRecordActivity1.this, "????????????????????????", Toast.LENGTH_SHORT, true).show();
            isRight = false;
            return isRight;
        }
//        if (StringUtils.isNullOrEmpty(et_reference.getText().toString().trim())) {
//            Toasty.warning(SealPointOnRecordActivity1.this, "????????????????????????", Toast.LENGTH_SHORT, true).show();
//            isRight = false;
//            return isRight;
//        }
        if (StringUtils.isNullOrEmpty(et_distance.getText().toString().trim())) {
            Toasty.warning(SealPointOnRecordActivity1.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
            isRight = false;
            return isRight;
        }
        if (StringUtils.isNullOrEmpty(et_height.getText().toString().trim())) {
            Toasty.warning(SealPointOnRecordActivity1.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
            isRight = false;
            return isRight;
        }
        if (StringUtils.isNullOrEmpty(floorValue)) {
            Toasty.warning(SealPointOnRecordActivity1.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
            isRight = false;
            return isRight;
        }

        if (StringUtils.isNullOrEmpty(unreachableTypeValue)) {
            Toasty.warning(SealPointOnRecordActivity1.this, "??????????????????????????????", Toast.LENGTH_SHORT, true).show();
            isRight = false;
            return isRight;
        }

        // ?????????  ???  ????????????
        if (unreachableTypeValue.equals(ConstantValue.unreachable_type_1)) {
            if (StringUtils.isNullOrEmpty(unreachReasonName)) {
                Toasty.warning(SealPointOnRecordActivity1.this, "???????????????????????????", Toast.LENGTH_SHORT, true).show();
                isRight = false;
                return isRight;
            }
        }

        return isRight;
    }


    // ?????????
    private void readStreamCache() {
        BaseStreamTableBean list = AppDatabase.getInstance(SealPointOnRecordActivity1.this).baseStreamTableDao().loadById("1");
        if (list != null) {
            List<BaseStreamBean> baseStreamBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseStreamBean>>() {
            }.getType());

            L.i("zzz1-baseStreamBeans.list222--->" + baseStreamBeans.size());


            baseStreamBeanListAll.clear();
            if (baseStreamBeans != null && baseStreamBeans.size() > 0) {
                baseStreamBeanListAll.addAll(baseStreamBeans);
                setStreamData(deviceId);
            } else {
                Toasty.warning(SealPointOnRecordActivity1.this, "????????????????????????????????????", Toast.LENGTH_SHORT, true).show();
                return;
            }
        } else {
            Toasty.warning(SealPointOnRecordActivity1.this, "????????????????????????????????????", Toast.LENGTH_SHORT, true).show();
            return;
        }

    }

    private void setStreamData(String deviceId) {
        baseStreamBeanList.clear();
        if (baseStreamBeanListAll != null && baseStreamBeanListAll.size() > 0) {
            for (int i = 0; i < baseStreamBeanListAll.size(); i++) {
                if (deviceId.equals(baseStreamBeanListAll.get(i).getDeviceId())) {
                    baseStreamBeanList.add(baseStreamBeanListAll.get(i));
                }
            }

            if (baseStreamBeanList != null && baseStreamBeanList.size() > 0) {
                selectStreamAdapter.notifyDataSetChanged();
                streamTypePopupWindow.show();
            } else {
                Toasty.warning(SealPointOnRecordActivity1.this, "????????????????????????", Toast.LENGTH_SHORT, true).show();
            }

        } else {
            Toasty.warning(SealPointOnRecordActivity1.this, "????????????????????????", Toast.LENGTH_SHORT, true).show();

        }
    }

    // ?????????-??????
    private void readChemicalCache() {
        BaseChemicalTableBean list = AppDatabase.getInstance(SealPointOnRecordActivity1.this).baseChemicalTableDao().loadById("1");
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
                Toasty.warning(SealPointOnRecordActivity1.this, "????????????????????????????????????", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            Toasty.warning(SealPointOnRecordActivity1.this, "????????????????????????????????????", Toast.LENGTH_SHORT, true).show();
        }

    }

    // ????????????-??????
    private void readComponentTypeCache() {
        BaseComponentTypeTableBean list = AppDatabase.getInstance(SealPointOnRecordActivity1.this).baseComponentTypeTableDao().loadById("1");
        if (list != null) {
            List<BaseComponentTypeBean> baseComponentTypeBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseComponentTypeBean>>() {
            }.getType());

            L.i("zzz1-baseComponentTypeBeans.list222--->" + baseComponentTypeBeans.size());


            baseComponentTypeBeanList.clear();
            if (baseComponentTypeBeans != null && baseComponentTypeBeans.size() > 0) {
                baseComponentTypeBeanList.addAll(baseComponentTypeBeans);
            } else {
                Toasty.warning(SealPointOnRecordActivity1.this, "???????????????????????????????????????", Toast.LENGTH_SHORT, true).show();
                return;
            }
        } else {
            Toasty.warning(SealPointOnRecordActivity1.this, "???????????????????????????????????????", Toast.LENGTH_SHORT, true).show();
            return;
        }

    }

    // ??????-??????
    private void readDirectionCache() {
        BaseDirectionTableBean list = AppDatabase.getInstance(SealPointOnRecordActivity1.this).baseDirectionTableDao().loadById("1");
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
                Toasty.warning(SealPointOnRecordActivity1.this, "?????????????????????????????????", Toast.LENGTH_SHORT, true).show();
                return;
            }
        } else {
            Toasty.warning(SealPointOnRecordActivity1.this, "?????????????????????????????????", Toast.LENGTH_SHORT, true).show();
            return;
        }

    }

    /**
     * ??????
     * ?????? ?????? ????????????
     */
    private void getBaseLinkList() {
        Subscribe.getBaseLinkList(code, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //??????
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
                                AppDatabase.getInstance(SealPointOnRecordActivity1.this).baseAreaTableDao().insert(baseAreaTableBean);

                            } else {
                                Toasty.error(SealPointOnRecordActivity1.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(SealPointOnRecordActivity1.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(SealPointOnRecordActivity1.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(SealPointOnRecordActivity1.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(SealPointOnRecordActivity1.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, SealPointOnRecordActivity1.this));
    }


    private final GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
//            takePhoto();
        }
    };


    public void hideKeyboard() {
//        InputMethodManager imm = (InputMethodManager) view.getContext()
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null) {
//            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
//        }


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.RESULT_UNCHANGED_HIDDEN);

    }

    private boolean isKeyboardOpen() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen?????????true???????????????????????????
        L.i("zzz1-isOpen->", "" + isOpen);
        return isOpen;
    }

    private void save() {
        ImgTableBean1 imgTableBean = new ImgTableBean1();
        imgTableBean.fileName = mFileName;
        imgTableBean.localPath = localPath;
        imgTableBean.content = content;
        imgTableBean.currentDate = currentDate;

        imgTableBean.deviceCode = deviceCode;
        imgTableBean.deviceName = deviceName;
        imgTableBean.deviceType = deviceType;
        imgTableBean.deviceId = deviceId;

        imgTableBean.areaCode = areaCode;
        imgTableBean.areaName = areaName;
        imgTableBean.equipmentCode = equipCode;
        imgTableBean.equipName = equipName;

        // ConstantValue.tag_num
        String tagNum = tv_tag_num_pre.getText().toString() + et_tag_num.getText().toString().trim();

        imgTableBean.tagNum = tagNum;
        imgTableBean.tagNumPre = tv_tag_num_pre.getText().toString();
        imgTableBean.tagNumValue = et_tag_num.getText().toString().trim();

        imgTableBean.refMaterial = et_reference.getText().toString().trim();

        imgTableBean.directionValue = directionTypeValue;
        imgTableBean.directionName = directionTypeName;
        imgTableBean.directionPosValue = directionTypeValue1;
        imgTableBean.directionPosName = directionTypeName1;

        imgTableBean.distance = et_distance.getText().toString().trim();
        imgTableBean.height = et_height.getText().toString().trim();
//        if (isHalfSelected) {
//            floorValue = "" + (Integer.parseInt(floorValue) + 0.5);
//        }
        imgTableBean.floorLevel = floorValue;

        imgTableBean.remark = et_remark.getText().toString().trim();
//            imgTableBean.componentType = componentType;
//            imgTableBean.heatPreser = heatPreservation;
//            imgTableBean.componentSize = size;
        imgTableBean.mediumState = mediumState;
        imgTableBean.prodStream = streamTypeValue;
        imgTableBean.prodStreamName = prodStreamName;

        imgTableBean.unreachable = unreachableTypeValue;
        imgTableBean.unreachableDesc = unreachReasonName;
        imgTableBean.mainMedium = chemicalName;
//            imgTableBean.pictPath = chemicalTypeValue;
        imgTableBean.chemicalName = chemicalName;


        Long rowId = AppDatabase.getInstance(SealPointOnRecordActivity1.this).imgTableDao1().insert(imgTableBean);
        if (rowId != null) {
            Toasty.success(SealPointOnRecordActivity1.this, "???????????????", Toast.LENGTH_SHORT, true).show();
            saveCurrentTemp();
            EventBus.getDefault().post(ConstantValue.event_photo_save);
        } else {
            Toasty.error(SealPointOnRecordActivity1.this, "???????????????", Toast.LENGTH_SHORT, true).show();
        }

    }

    private void saveCurrentTemp() {

        kv.encode(Constant.tag_num, Integer.parseInt(et_tag_num.getText().toString().trim()));

//        kv.encode(Constant.imgtabbean1_tag_num, et_tag_num.getText().toString().trim());
        kv.encode(Constant.imgtabbean1_ref, et_reference.getText().toString().trim());
        kv.encode(Constant.imgtabbean1_direction_value, directionTypeValue);
        kv.encode(Constant.imgtabbean1_direction_name, directionTypeName);
        kv.encode(Constant.imgtabbean1_direction_pos_value, directionTypeValue1);
        kv.encode(Constant.imgtabbean1_direction_pos_name, directionTypeName1);

        kv.encode(Constant.imgtabbean1_distance, et_distance.getText().toString().trim());
        L.i("zzz1--save-distance->" + et_distance.getText().toString().trim());
        kv.encode(Constant.imgtabbean1_height, et_height.getText().toString().trim());

        kv.encode(Constant.imgtabbean1_floor_level, floorValue);
        kv.encode(Constant.imgtabbean1_remark, et_remark.getText().toString().trim());

        kv.encode(Constant.imgtabbean1_medium_state, mediumState);
        kv.encode(Constant.imgtabbean1_prod_stream, streamTypeValue);
        kv.encode(Constant.imgtabbean1_prod_stream_name, prodStreamName);

        kv.encode(Constant.imgtabbean1_unreachable, unreachableTypeValue);
        kv.encode(Constant.imgtabbean1_unreachable_desc, unreachReasonName);
        kv.encode(Constant.imgtabbean1_main_medium, chemicalTypeValue);
        kv.encode(Constant.imgtabbean1_chemicalName, chemicalName);
    }

    // String directionTypeNameAll = directionTypeName + directionTypeName1;
    //        String directionTypeValueAll = directionTypeValue + directionTypeValue1;
    private void readCurrentTemp() {
        // ????????????
        int tagNum = kv.getInt(Constant.tag_num, 0);
        if (isNewAdd) {
            et_tag_num.setText("" + (tagNum + 1));
        } else {
            et_tag_num.setText("" + tagNum);
        }
        tv_tag_num_pre.setText(getTagPre(et_tag_num.getText().toString().trim()));

        directionTypeValue = kv.getString(Constant.imgtabbean1_direction_value, "");
        if (StringUtils.isNullOrEmpty(directionTypeValue)) {
            Toasty.warning(context, "???????????????????????????", Toast.LENGTH_SHORT, true).show();
            return;
        }
        directionTypeName = kv.getString(Constant.imgtabbean1_direction_name, "");
        tv_direction_type_name.setText(directionTypeName);
        if (!StringUtils.isNullOrEmpty(directionTypeValue)) {
            clearSelectDirection();
            for (int i = 0; i < directionList.size(); i++) {
                if (directionTypeValue.equals(directionList.get(i).getValue())) {
                    directionList.get(i).setSelected(true);
                    adapterDirection.notifyDataSetChanged();
                    break;
                }

            }
        }

        directionTypeValue1 = kv.getString(Constant.imgtabbean1_direction_pos_value, "");
        directionTypeName1 = kv.getString(Constant.imgtabbean1_direction_pos_name, "");
        tv_direction_type_name1.setText(directionTypeName1);
        if (!StringUtils.isNullOrEmpty(directionTypeValue1)) {
            clearSelectDirection1();
            for (int i = 0; i < directionList1.size(); i++) {
                if (directionTypeValue1.equals(directionList1.get(i).getValue())) {
                    directionList1.get(i).setSelected(true);
                    adapterDirection1.notifyDataSetChanged();
                    break;
                }
            }
        }

        String ref = kv.getString(Constant.imgtabbean1_ref, "");
        et_reference.setText(ref);


        String distance = kv.getString(Constant.imgtabbean1_distance, "");
        L.i("zzz1--distance->" + distance);
        et_distance.setText(distance);
        if (!StringUtils.isNullOrEmpty(distance)) {
            seek_bar_distance.setProgress(Float.parseFloat(distance));
        }

        String height = kv.getString(Constant.imgtabbean1_height, "");
        et_height.setText(height);
        if (!StringUtils.isNullOrEmpty(height)) {
            seek_bar_height.setProgress(Float.parseFloat(height));
        }

        // ??????
        floorValue = kv.getString(Constant.imgtabbean1_floor_level, "");
        et_floor_level.setText(floorValue);

        String floorValueInt = floorValue;
        if (!StringUtils.isNullOrEmpty(floorValue)) {
            if (floorValue.contains(".5")) {
                isHalfSelected = true;
                iv_select_half.setImageResource(R.mipmap.selected);
                floorValueInt = floorValueInt.substring(0, 1);
            } else {
                isHalfSelected = false;
                iv_select_half.setImageResource(R.mipmap.unselect);
            }

            for (int i = 0; i < floorList.size(); i++) {
                if (floorValueInt.equals(floorList.get(i).getValue())) {
                    floorList.get(i).setSelected(true);
                    adapterFloor.notifyDataSetChanged();
                    break;
                }

            }
        }

        String remark = kv.getString(Constant.imgtabbean1_remark, "");
        et_remark.setText(remark);

        mediumState = kv.getString(Constant.imgtabbean1_medium_state, "");
        streamTypeValue = kv.getString(Constant.imgtabbean1_prod_stream, "");
        prodStreamName = kv.getString(Constant.imgtabbean1_prod_stream_name, "");
        tv_stream_type_name.setText(prodStreamName);

        unreachableTypeValue = kv.getString(Constant.imgtabbean1_unreachable, "");
        unreachReasonName = kv.getString(Constant.imgtabbean1_unreachable_desc, "");
        tv_unreachable_reason.setText(StringUtils.nullStrToEmpty(unreachReasonName));

        chemicalTypeValue = kv.getString(Constant.imgtabbean1_main_medium, "");
        chemicalName = kv.getString(Constant.imgtabbean1_chemicalName, "");
        tv_chemical_type_name.setText(StringUtils.nullStrToEmpty(chemicalName));
    }

    private void takePhoto() {
        // ????????????
        PictureSelector.create(SealPointOnRecordActivity1.this)
                .openCamera(PictureMimeType.ofImage())// ?????????????????????????????????????????? ??????????????????????????????or??????
                .theme(R.style.picture_default_style)// ?????????????????? ???????????? values/styles
                .imageEngine(GlideEngine.createGlideEngine())// ??????????????????????????????????????????
//                .setPictureStyle(mPictureParameterStyle)// ???????????????????????????
//                .setPictureCropStyle(mCropParameterStyle)// ???????????????????????????
                .setPictureWindowAnimationStyle(mWindowAnimationStyle)// ?????????????????????????????????
                .maxSelectNum(maxSelectNum)// ????????????????????????
                .isUseCustomCamera(false)// ???????????????????????????
                //.setOutputCameraPath()// ???????????????????????????
                .minSelectNum(1)// ??????????????????
                //.querySpecifiedFormatSuffix(PictureMimeType.ofPNG())// ??????????????????????????????
                .closeAndroidQChangeWH(true)//??????????????????????????????????????????????????????true
                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// ???????????????????????????????????????????????????false
                .selectionMode(PictureConfig.SINGLE)// ?????? or ??????
                //.cameraFileName(System.currentTimeMillis() + ".jpg")// ?????????????????????????????????????????????,???????????????????????????????????????
                .renameCompressFile(mFileName)// ??????????????????????????? ????????????????????????????????????????????????????????????
                //.renameCropFileName(System.currentTimeMillis() + ".jpg")// ??????????????????????????? ????????????????????????????????????????????????????????????
                .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// ????????????????????????????????????????????????10??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                .isPreviewImage(true)// ?????????????????????
                .isPreviewVideo(false)// ?????????????????????
                .isEnablePreviewAudio(false) // ?????????????????????
                .isCamera(true)// ????????????????????????
                .isAndroidQTransform(true)// ??????????????????Android Q ??????????????????????????????????????????compress(false); && .isEnableCrop(false);??????,????????????
                .isEnableCrop(false)// ????????????
                //.basicUCropConfig()//??????????????????UCropOptions????????????????????????PictureSelector???????????????????????????????????????????????????
                .isCompress(true)// ????????????
                .compressQuality(60)// ???????????????????????????
                .glideOverride(160, 160)// glide ???????????????????????????????????????????????????????????????????????????????????????
//                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// ???????????? ???16:9 3:2 3:4 1:1 ????????????
                .hideBottomControls(false)// ????????????uCrop???????????????????????????
                .isGif(false)// ????????????gif??????
                .freeStyleCropEnabled(true)// ????????????????????????
                .freeStyleCropMode(OverlayView.DEFAULT_FREESTYLE_CROP_MODE)// ?????????????????????
                .circleDimmedLayer(false)// ??????????????????
                //.setCircleDimmedColor(ContextCompat.getColor(this, R.color.app_color_white))// ??????????????????????????????
                //.setCircleDimmedBorderColor(ContextCompat.getColor(this, R.color.app_color_white))// ??????????????????????????????
                //.setCircleStrokeWidth(3)// ??????????????????????????????
//                .showCropFrame(true)// ?????????????????????????????? ???????????????????????????false
//                .showCropGrid(true)// ?????????????????????????????? ???????????????????????????false
                .isOpenClickSound(true)// ????????????????????????
//                .selectionData(mAdapter.getData())// ????????????????????????
                .isAutoScalePreviewImage(true)// ??????????????????????????????????????????????????????????????????
                //.isPreviewEggs(true)// ??????????????? ????????????????????????????????????(???????????????????????????????????????????????????)
                //.cropCompressQuality(90)// ?????? ??????cutOutQuality()
                .cutOutQuality(90)// ?????????????????? ??????100
                .minimumCompressSize(100)// ??????100kb??????????????????
                //.cropWH()// ???????????????????????????????????????????????????????????????
                //.cropImageWideHigh()// ???????????????????????????????????????????????????????????????
                //.rotateEnabled() // ???????????????????????????
                //.scaleEnabled()// ?????????????????????????????????
                //.videoQuality()// ?????????????????? 0 or 1
//                .forResult(PictureConfig.CHOOSE_REQUEST);//????????????onActivityResult code
                .forResult(new SealPointOnRecordActivity1.MyResultCallback());
    }

    /**
     * ????????????ActivityResultLauncher
     *
     * @return
     */
    private ActivityResultLauncher<Intent> createActivityResultLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        if (resultCode == RESULT_OK) {
                            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(result.getData());
                            // ?????? LocalMedia ??????????????????path
                            // 1.media.getPath(); ??????path
                            // 2.media.getCutPath();?????????path????????????media.isCut();??????????????????
                            // 3.media.getCompressPath();?????????path????????????media.isCompressed();??????????????????
                            // 4.media.getOriginalPath()); media.isOriginal());???true?????????????????????
                            // 5.media.getAndroidQToPath();Android Q?????????????????????????????????????????????????????????????????????????????????????????????????????????.isAndroidQTransform ???false ?????????????????????
                            // ???????????????????????????????????????????????????????????????????????????????????????
                            for (LocalMedia media : selectList) {
                                if (media.getWidth() == 0 || media.getHeight() == 0) {
                                    if (PictureMimeType.isHasImage(media.getMimeType())) {
                                        MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(media.getPath());
                                        media.setWidth(imageExtraInfo.getWidth());
                                        media.setHeight(imageExtraInfo.getHeight());
                                    } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                                        MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(getContext(), media.getPath());
                                        media.setWidth(videoExtraInfo.getWidth());
                                        media.setHeight(videoExtraInfo.getHeight());
                                    }
                                }
                                Log.i(TAG, "????????????:" + media.isCompressed());
                                Log.i(TAG, "??????:" + media.getCompressPath());
                                Log.i(TAG, "??????:" + media.getPath());
                                Log.i(TAG, "????????????:" + media.getRealPath());
                                Log.i(TAG, "????????????:" + media.isCut());
                                Log.i(TAG, "??????:" + media.getCutPath());
                                Log.i(TAG, "??????????????????:" + media.isOriginal());
                                Log.i(TAG, "????????????:" + media.getOriginalPath());
                                Log.i(TAG, "Android Q ??????Path:" + media.getAndroidQToPath());
                                Log.i(TAG, "??????: " + media.getWidth() + "x" + media.getHeight());
                                Log.i(TAG, "Size: " + media.getSize());

                                // TODO ????????????PictureSelectorExternalUtils.getExifInterface();??????????????????????????????????????????????????????????????????????????????
                            }
//                            mAdapter.setList(selectList);
//                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * ??????????????????
     */
    private class MyResultCallback implements OnResultCallbackListener<LocalMedia> {

        public MyResultCallback() {
            super();
        }

        @Override
        public void onResult(List<LocalMedia> result) {
            for (LocalMedia media : result) {
                if (media.getWidth() == 0 || media.getHeight() == 0) {
                    if (PictureMimeType.isHasImage(media.getMimeType())) {
                        MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(media.getPath());
                        media.setWidth(imageExtraInfo.getWidth());
                        media.setHeight(imageExtraInfo.getHeight());
                    } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                        MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(PictureAppMaster.getInstance().getAppContext(), media.getPath());
                        media.setWidth(videoExtraInfo.getWidth());
                        media.setHeight(videoExtraInfo.getHeight());
                    }
                }
                Log.i(TAG, "?????????: " + media.getFileName());
                Log.i(TAG, "????????????:" + media.isCompressed());
                Log.i(TAG, "??????:" + media.getCompressPath());
                Log.i(TAG, "??????:" + media.getPath());
                Log.i(TAG, "????????????:" + media.getRealPath());
                Log.i(TAG, "????????????:" + media.isCut());
                Log.i(TAG, "??????:" + media.getCutPath());
                Log.i(TAG, "??????????????????:" + media.isOriginal());
                Log.i(TAG, "????????????:" + media.getOriginalPath());
                Log.i(TAG, "Android Q ??????Path:" + media.getAndroidQToPath());
                Log.i(TAG, "??????: " + media.getWidth() + "x" + media.getHeight());
                Log.i(TAG, "Size: " + media.getSize());

                Log.i(TAG, "onResult: " + media.toString());

                // TODO ????????????PictureSelectorExternalUtils.getExifInterface();??????????????????????????????????????????????????????????????????????????????

            }

            LocalMedia media = result.get(0);
            if (media != null) {
//                fileName = media.getFileName();
                compressPath = media.getCompressPath();
                L.i("zzz1--compressPath->" + compressPath);
                if (!StringUtils.isNullOrEmpty(compressPath)) {
                    localPath = compressPath;
                    L.i("zzz1--localPath-->" + localPath);
                    openTakePhotoActivity();
                } else {
                    Toasty.warning(SealPointOnRecordActivity1.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();

                }
            }

//            if (mAdapterWeakReference.get() != null) {
//                mAdapterWeakReference.get().setList(result);
//                mAdapterWeakReference.get().notifyDataSetChanged();
//            }
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "PictureSelector Cancel");
        }
    }

    private void openTakePhotoActivity() {
//        if (isValid()) {
//                    L.i("zzz1--floorValue->" + floorValue);
        kv.encode(Constant.tag_num, Integer.parseInt(et_tag_num.getText().toString().trim()));

        String tagNum = tv_tag_num_pre.getText().toString() + et_tag_num.getText().toString().trim();
        String tagNumPre = tv_tag_num_pre.getText().toString();
        String tagNumValue = et_tag_num.getText().toString().trim();

        TakePhotoActivity1.open(this, isNewAdd, mFileName, localPath, content, deviceCode, deviceName, deviceType, deviceId,
                areaCode, areaName, equipCode, equipName,
                mediumState, streamTypeValue, prodStreamName, chemicalName, chemicalTypeValue,
                directionTypeName, directionTypeValue, directionTypeName1, directionTypeValue1,
                tagNum, tagNumPre, tagNumValue, et_reference.getText().toString().trim(),
                et_distance.getText().toString().trim(), et_height.getText().toString().trim(),
                floorValue,
                unreachableTypeValue, unreachReasonName,
                et_remark.getText().toString().trim());
        finish();
//        }
    }


}