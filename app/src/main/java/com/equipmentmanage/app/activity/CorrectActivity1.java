package com.equipmentmanage.app.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.AreaAdapter;
import com.equipmentmanage.app.adapter.CorrectAdapter;
import com.equipmentmanage.app.adapter.DepartmentAdapter;
import com.equipmentmanage.app.adapter.SelectChemicalAdapter;
import com.equipmentmanage.app.adapter.SelectGasAdapter;
import com.equipmentmanage.app.adapter.SelectInstrumentAdapter;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.AreaManageResultBean;
import com.equipmentmanage.app.bean.BaseAreaBean;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.BaseChemicalBean;
import com.equipmentmanage.app.bean.BaseDeviceBean;
import com.equipmentmanage.app.bean.BaseDeviceTableBean;
import com.equipmentmanage.app.bean.BaseGasBean;
import com.equipmentmanage.app.bean.BaseInstrumentBean;
import com.equipmentmanage.app.bean.CorrectCheckBean;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.bean.GasTableBean;
import com.equipmentmanage.app.bean.InstrumentTableBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.DateUtil;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.equipmentmanage.app.view.BluetoothDialog;
import com.equipmentmanage.app.view.ReadValueDialog;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 漂移校准/校准设备
 * @Author: zzh
 * @CreateDate: 2021/8/18
 */
public class CorrectActivity1 extends BaseActivity {

    public static void open(Context c, String type, String id) {
        Intent i = new Intent(c, CorrectActivity1.class);
        i.putExtra(ConstantValue.correct_type, type);
        i.putExtra(Constant.id, id);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.rv_list)
    RecyclerView rvList;

    private CorrectAdapter adapter;
    private List<BaseGasBean> baseGasBeanList = new ArrayList<>();

    private String correctType;

    private BluetoothDialog checkDialog2;
    private boolean mIsCon = false;

    private ReadValueDialog readValueDialog;
    private BaseGasBean bean;
    private int pos;

    private String currentDate;

    private long startTime, endTime;

    private String id;
    private String actualId;

    @BindView(R.id.ll_instrument_type)
    LinearLayout ll_instrument_type; //仪器

    @BindView(R.id.tv_instrument_name)
    TextView tv_instrument_name; //仪器
    //选择仪器
    private ListPopupWindow instrumentPopupWindow;
    private String instrumentName = "";
    private String instrumentTypeValue = "";
    private SelectInstrumentAdapter selectInstrumentAdapter;
    private List<BaseInstrumentBean> baseInstrumentBeanList = new ArrayList<>();

    private MMKV kv = MMKV.defaultMMKV();
    private String eventStr;

    @Override
    protected int initLayout() {
        return R.layout.activity_drift_correct1;
    }

    @Override
    protected void initView() {


//        bean = (AreaManageResultBean.Records) getIntent().getSerializableExtra(Constant.areaBean);
        id = getIntent().getStringExtra(Constant.id);
        correctType = getIntent().getStringExtra(ConstantValue.correct_type);
        // 0 校准设备(日常校准), 1 漂移校准
        if (!StringUtils.isNullString(correctType) && correctType.equals(ConstantValue.correct_type_0)) {
            titleBar.setTitle(R.string.daily_correct);
            actualId = id + "2";
            eventStr = ConstantValue.event_daily_save;
        } else {
            titleBar.setTitle(R.string.drift_correct);
            actualId = id + "3";
            eventStr = ConstantValue.event_drift_save;
        }

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                saveData();
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
                return R.mipmap.ic_bluetooth;
            }

            @Override
            public void performAction(View view) {
                L.i("zzz1--->bluetooth");
                // 蓝牙连接
                showCheckDialog();
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

        // 选择仪器
        instrumentPopupWindow = new ListPopupWindow(this);
        instrumentPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        instrumentPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        instrumentPopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        instrumentPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        instrumentPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        instrumentPopupWindow.setAnchorView(ll_instrument_type);
        instrumentPopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        instrumentPopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        instrumentPopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        selectInstrumentAdapter = new SelectInstrumentAdapter(this, baseInstrumentBeanList);
        instrumentPopupWindow.setAdapter(selectInstrumentAdapter);

        instrumentTypeValue = kv.getString(actualId + Constant.instrumentTypeValue, "");
        instrumentName = kv.getString(actualId + Constant.instrumentName, "");
        if (!StringUtils.isNullOrEmpty(instrumentTypeValue)) {
            tv_instrument_name.setText(StringUtils.nullStrToEmpty(instrumentName));
        }

//        if (bean != null) {
//            tvAreaName.setText(StringUtils.nullStrToEmpty(bean.getAreaName())); //名称
//            tvAreaCode.setText(StringUtils.nullStrToEmpty(bean.getAreaCode())); //编号
//            tvBelongDevice.setText(StringUtils.nullStrToEmpty(bean.getBelongDevice_dictText())); //所属装置
////            tvEnabled.setText(StringUtils.nullStrToEmpty(bean.getDeviceName())); //是否可用
//        }


        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(manager);
        adapter = new CorrectAdapter(baseGasBeanList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (!StringUtils.isNullOrEmpty(instrumentTypeValue)) {
                    pos = position;
                    bean = baseGasBeanList.get(position);
                    if (bean != null) {
                        if (mIsCon) {
                            showReadValueDialog();
                        } else {
                            Toasty.warning(CorrectActivity1.this, "请先连接设备！", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                } else {
                    Toasty.warning(CorrectActivity1.this, "请先选择仪器！", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        adapter.setEmptyView(getEmptyDataView());
        rvList.setAdapter(adapter);

    }

    private View getEmptyDataView() {
        View notDataView = getLayoutInflater().inflate(R.layout.layout_no_data, rvList, false);
        LinearLayout llNoData = notDataView.findViewById(R.id.ll_no_data);
        llNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readCache();
            }
        });
        return notDataView;
    }

    @Override
    protected void initEvent() {
        // 选择仪器
        instrumentPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseInstrumentBean bean = baseInstrumentBeanList.get(position);
                if (bean != null) {
                    instrumentTypeValue = bean.getId();
                    instrumentName = bean.getInstrumentName();
                    tv_instrument_name.setText(StringUtils.nullStrToEmpty(bean.getInstrumentName()));
                    kv.encode(actualId + Constant.instrumentTypeValue, instrumentTypeValue);
                    kv.encode(actualId + Constant.instrumentName, instrumentName);
                }
                instrumentPopupWindow.dismiss();
            }
        });
    }

    @Override
    protected void initData() {
        readInstrumentCache(false);
        readCache();
    }

    private void showCheckDialog() {
        checkDialog2 = new BluetoothDialog(this);
        checkDialog2.setOnSelectClickListener(new BluetoothDialog.OnSelectClickListener() {
            @Override
            public void onSelectClick(String s) {
                if (!StringUtils.isNullOrEmpty(s)) {
                    if (s.equals(ConstantValue.log_error)) {
                        Toasty.warning(CorrectActivity1.this, "无效的指令！", Toast.LENGTH_SHORT, true).show();
                        return;
                    }

                    readValueDialog.setReadValue(s);
                    checkDialog2.dismiss();
//                    Toasty.normal(PartImgsActivity.this, s, Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.warning(CorrectActivity1.this, "读取的数据为空！", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void isConnnect(boolean isCon) {
                mIsCon = isCon;
                checkDialog2.dismiss();
            }
        });

        checkDialog2.show();
    }

    private void showReadValueDialog() {
        readValueDialog = new ReadValueDialog(this);
        readValueDialog.setOnConfirmListener(new ReadValueDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String s) {
                endTime = System.currentTimeMillis();
                long diff = (endTime - startTime) / 1000;//转换为秒数
                bean.setResponseTime("" + diff);
                // s已经非空判断
                bean.setActualValue(s);
                String currentDate = DateUtil.getCurentTime();
                L.i("zzz1---currentDate->" + currentDate);
                bean.setDate(currentDate);
                if (Double.parseDouble(s) >= (Double.parseDouble(bean.getPpm()) * 0.9)
                        && Double.parseDouble(s) <= (Double.parseDouble(bean.getPpm()) * 1.1)) {
                    bean.setStatus(ConstantValue.correct_pass_1);
                } else {
                    bean.setStatus(ConstantValue.correct_pass_0);
                }
                adapter.notifyDataSetChanged();
//                bean.setResponseTime();

            }

            @Override
            public void onRead() {
                startTime = System.currentTimeMillis();//获取开始时间，以自1970年1月1日经历的毫秒数值表示
                checkDialog2.sendMsg(ConstantValue.log_start);
            }
        });

        readValueDialog.show();
    }

    @OnClick({R.id.ll_instrument_type})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_instrument_type: // 仪器
                readInstrumentCache(true);
                break;
        }
    }

    /**
     * 仪器缓存
     */
    private void readInstrumentCache(boolean isClick) {
        InstrumentTableBean list = AppDatabase.getInstance(CorrectActivity1.this).baseInstrumentTableDao().loadById("1");
        if (list != null) {
            List<BaseInstrumentBean> baseInstrumentBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseInstrumentBean>>() {
            }.getType());
            L.i("zzz1-baseInstrumentBeans.list222--->" + baseInstrumentBeans.size());

            baseInstrumentBeanList.clear();
            if (baseInstrumentBeans != null && baseInstrumentBeans.size() > 0) {
                baseInstrumentBeanList.addAll(baseInstrumentBeans);
                selectInstrumentAdapter.notifyDataSetChanged();
                if (isClick) {
                    instrumentPopupWindow.show();
                }
            } else {
                Toasty.warning(CorrectActivity1.this, "请先下载仪器的基础数据", Toast.LENGTH_SHORT, true).show();
            }

        } else {
            Toasty.warning(CorrectActivity1.this, "请先下载仪器的基础数据", Toast.LENGTH_SHORT, true).show();
        }
    }

    /**
     * 标准气查询
     */
    private void readCache() {
        GasTableBean list = AppDatabase.getInstance(CorrectActivity1.this).baseGasTableDao().loadById(actualId);
//                L.i("zzz1-baseDevice->" + GsonUtils.toJson(list3));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)
        if (list != null) {
            List<BaseGasBean> baseGasBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseGasBean>>() {
            }.getType());
            L.i("zzz1-baseGasBeans.list222--->" + baseGasBeans.size());

            baseGasBeanList.clear();
            if (baseGasBeans != null && baseGasBeans.size() > 0) {
                baseGasBeanList.addAll(baseGasBeans);
                adapter.notifyDataSetChanged();
            } else {
                readInitCache();
            }
        } else {
            readInitCache();

        }

    }

    private void readInitCache() {
        GasTableBean list_init = AppDatabase.getInstance(CorrectActivity1.this).baseGasTableDao().loadById("1");
        if (list_init != null) {
            List<BaseGasBean> baseGasBeans = GsonUtils.fromJson(list_init.content, new TypeToken<List<BaseGasBean>>() {
            }.getType());
            L.i("zzz1-baseGasBeans.list333--->" + baseGasBeans.size());

            baseGasBeanList.clear();
            if (baseGasBeans != null && baseGasBeans.size() > 0) {
                baseGasBeanList.addAll(baseGasBeans);
                baseGasBeanList.addAll(baseGasBeans);
                adapter.notifyDataSetChanged();
            } else {
                Toasty.warning(CorrectActivity1.this, "请先下载标准气的基础数据", Toast.LENGTH_SHORT, true).show();
            }

        } else {
            Toasty.warning(CorrectActivity1.this, "请先下载标准气的基础数据", Toast.LENGTH_SHORT, true).show();
        }
    }

    private void saveData() {
        if (baseGasBeanList != null && baseGasBeanList.size() > 0) {
            // 0 校准设备(日常校准), 1 漂移校准
            GasTableBean gasTableBean = new GasTableBean();
            gasTableBean.id = actualId;
            gasTableBean.content = GsonUtils.toJson(baseGasBeanList);

            Long rowId = AppDatabase.getInstance(CorrectActivity1.this).baseGasTableDao().insert(gasTableBean);
            L.i("zzz1--rowId->" + rowId);
            if (rowId != null) {
                if (isAllChecked()){
                    kv.encode(Constant.is_correct_checked + actualId, "1");
                }
                Toasty.success(CorrectActivity1.this, "保存成功！", Toast.LENGTH_SHORT, true).show();
            } else {
                Toasty.error(CorrectActivity1.this, "保存失败！", Toast.LENGTH_SHORT, true).show();
            }

            EventBus.getDefault().post(eventStr);
        }
    }

    private boolean isAllChecked() {
        boolean allChecked = true;
        for (int i = 0; i < baseGasBeanList.size(); i++) {
            if (StringUtils.isNullOrEmpty(baseGasBeanList.get(i).getActualValue())) {
                allChecked = false;
                break;
            }
        }

        return allChecked;
    }

    @Override
    public void onBackPressed() {
        saveData();
        finish();
//        super.onBackPressed();
    }
}