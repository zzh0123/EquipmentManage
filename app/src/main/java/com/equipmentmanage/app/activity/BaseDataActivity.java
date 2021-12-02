package com.equipmentmanage.app.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseAreaBean;
import com.equipmentmanage.app.bean.BaseAreaTableBean;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.BaseChemicalBean;
import com.equipmentmanage.app.bean.BaseChemicalTableBean;
import com.equipmentmanage.app.bean.BaseComponentTypeTableBean;
import com.equipmentmanage.app.bean.BaseDeviceBean;
import com.equipmentmanage.app.bean.BaseDeviceTableBean;
import com.equipmentmanage.app.bean.BaseDirectionBean;
import com.equipmentmanage.app.bean.BaseDirectionTableBean;
import com.equipmentmanage.app.bean.BaseEquipmentBean;
import com.equipmentmanage.app.bean.BaseEquipmentTableBean;
import com.equipmentmanage.app.bean.BaseGasBean;
import com.equipmentmanage.app.bean.BaseInstrumentBean;
import com.equipmentmanage.app.bean.BaseLeakageBean;
import com.equipmentmanage.app.bean.BaseComponentTypeBean;
import com.equipmentmanage.app.bean.DictItemBean;
import com.equipmentmanage.app.bean.GasTableBean;
import com.equipmentmanage.app.bean.InstrumentTableBean;
import com.equipmentmanage.app.bean.LeakageTableBean;
import com.equipmentmanage.app.bean.ThresholdTableBean;
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
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static com.equipmentmanage.app.netapi.ConstantValue.last_time;

/**
 * @Description: 建档Fragment
 * @Author: zzh
 * @CreateDate: 2021/10/23 17:23
 */
public class BaseDataActivity extends BaseActivity {

    public static void open(Context c) {
        Intent i = new Intent(c, BaseDataActivity.class);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.tv_dl_gas)
    TextView tv_dl_gas; //标准气

    @BindView(R.id.tv_last_date_gas)
    TextView tv_last_date_gas; //标准气

    @BindView(R.id.tv_dl_threshold)
    TextView tv_dl_threshold; //阈值

    @BindView(R.id.tv_last_date_threshold)
    TextView tv_last_date_threshold; //阈值


    @BindView(R.id.tv_dl_main_medium)
    TextView tv_dl_main_medium; //主要介质

    @BindView(R.id.tv_dl_instrument)
    TextView tv_dl_instrument; //仪器

    @BindView(R.id.tv_last_date_instrument)
    TextView tv_last_date_instrument; //仪器

    @BindView(R.id.tv_dl_leakage_source)
    TextView tv_dl_leakage_source; //泄露源

    @BindView(R.id.tv_last_date_leakage_source)
    TextView tv_last_date_leakage_source; //泄露源


    @BindView(R.id.tv_dl_repair_type)
    TextView tv_dl_repair_type; //维修类型

    @BindView(R.id.tv_dl_repair_person)
    TextView tv_dl_repair_person; //维修人员


    @BindView(R.id.tv_dl_device)
    TextView tv_dl_device; //装置

    @BindView(R.id.tv_dl_equipment)
    TextView tv_dl_equipment; //设备

    @BindView(R.id.tv_dl_area)
    TextView tv_dl_area; //区域

    @BindView(R.id.tv_last_date_area)
    TextView tv_last_date_area; //区域

    @BindView(R.id.tv_last_date_chemical)
    TextView tv_last_date_chemical; //化学品

    @BindView(R.id.tv_last_date_component_type)
    TextView tv_last_date_component_type; //组件类型

    @BindView(R.id.tv_last_date_direction)
    TextView tv_last_date_direction; //方向


    private List<DictItemBean> deviceTypeBeanList = new ArrayList<>();

    // BaseGasBean 标准气
    private List<BaseGasBean> gasBeanList = new ArrayList<>();

    private MMKV kv = MMKV.defaultMMKV();

    @Override
    protected int initLayout() {
        return R.layout.activity_base_data;
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

        String dateGas = kv.getString(Constant.date_gas, "");
        tv_last_date_gas.setText(last_time + dateGas);

        String dateThreshold= kv.getString(Constant.thres_hold, "");
        tv_last_date_threshold.setText(last_time + dateThreshold);

        String dateInstrument = kv.getString(Constant.instrument, "");
        tv_last_date_instrument.setText(last_time + dateInstrument);

        String dateLeakage = kv.getString(Constant.leakage, "");
        tv_last_date_leakage_source.setText(last_time + dateLeakage);

        String dateBaseArea = kv.getString(Constant.base_area, "");
        tv_last_date_area.setText(last_time + dateBaseArea);

        String dateBaseChemical = kv.getString(Constant.date_base_chemical, "");
        tv_last_date_chemical.setText(last_time + dateBaseChemical);

        String dateBaseComponentType = kv.getString(Constant.date_base_component_type, "");
        tv_last_date_component_type.setText(last_time + dateBaseComponentType);

        String dateBaseDirection = kv.getString(Constant.date_base_direction, "");
        tv_last_date_direction.setText(last_time + dateBaseDirection);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }


    @SingleClick
    @OnClick({R.id.tv_dl_gas, R.id.tv_dl_threshold, R.id.tv_dl_main_medium,
            R.id.tv_dl_instrument, R.id.tv_dl_leakage_source, R.id.tv_dl_repair_type,
            R.id.tv_dl_repair_person,
            R.id.tv_dl_device, R.id.tv_dl_equipment, R.id.tv_dl_area,
            R.id.tv_dl_chemical, R.id.tv_dl_component_type, R.id.tv_dl_direction})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dl_gas: // 标准气
//                getDictList();
                getGasList();
//                BaseAreaTableBean list3 = AppDatabase.getInstance(BaseDataActivity.this).baseAreaTableDao().loadById("1");
//                L.i("zzz1-Area->" + GsonUtils.toJson(list3));
                break;
            case R.id.tv_dl_threshold: // 阈值
                getThresholdList();
                break;
            case R.id.tv_dl_main_medium: // 主要介质

                break;
            case R.id.tv_dl_instrument: // 仪器
                getInstrumentList();
//                ThresholdTableBean list = AppDatabase.getInstance(BaseDataActivity.this).baseThresholdTableDao().loadById("1");
//                L.i("zzz1-getthreshold->" + GsonUtils.toJson(list));
                break;
            case R.id.tv_dl_leakage_source: // 泄露源
                getLeakageList();
//                InstrumentTableBean list1 = AppDatabase.getInstance(BaseDataActivity.this).baseInstrumentTableDao().loadById("1");
//                L.i("zzz1-getInstrument->" + GsonUtils.toJson(list1));
                break;
            case R.id.tv_dl_repair_type: // 维修类型

                break;
            case R.id.tv_dl_repair_person: // 维修人员

                break;

            case R.id.tv_dl_device: // 装置
                getBaseDeviceList();
//                LeakageTableBean list1 = AppDatabase.getInstance(BaseDataActivity.this).baseLeakageTableDao().loadById("1");
//                L.i("zzz1-Leakage->" + GsonUtils.toJson(list1));
                break;

            case R.id.tv_dl_equipment: // 设备
                getBaseEquipmentList();
//                BaseDeviceTableBean list3 = AppDatabase.getInstance(BaseDataActivity.this).baseDeviceTableDao().loadById("1");
//                L.i("zzz1-baseDevice->" + GsonUtils.toJson(list3));
                break;

            case R.id.tv_dl_area: // 区域
                getBaseAreaList();
//                BaseEquipmentTableBean list3 = AppDatabase.getInstance(BaseDataActivity.this).baseEquipmentTableDao().loadById("1");
//                L.i("zzz1-Equipment->" + GsonUtils.toJson(list3));
//                DeviceManageResultBean.Records records = GsonUtils.fromJson(list3.content, Basee.Records.class);
//
//                GsonUtils.fromJson(list3.content, List<BaseEquipmentBean>)
//                List<BaseEquipmentBean> baseEquipmentBeans = GsonUtils.fromJson(list3.content, new TypeToken<List<BaseEquipmentBean>>() {
//                }.getType());
//                L.i("zzz1-baseEquipmentBeans.list222--->" + baseEquipmentBeans.size());
                break;

            case R.id.tv_dl_chemical: // 化学品
                getBaseChemicalList();
//                BaseDirectionTableBean list3 = AppDatabase.getInstance(BaseDataActivity.this).baseDirectionTableDao().loadById("1");
//                L.i("zzz1-Direction->" + GsonUtils.toJson(list3));
                break;

            case R.id.tv_dl_component_type: // 组件类型
                getBaseComponentTypeList();
                break;

            case R.id.tv_dl_direction: // 方向
                getBaseDirectionList();
//                BaseChemicalTableBean list3 = AppDatabase.getInstance(BaseDataActivity.this).baseChemicalTableDao().loadById("1");
//                L.i("zzz1-baseChemical->" + GsonUtils.toJson(list3));
                break;
        }
    }

    /**
     * 获取装置类型列表
     */
    private void getDictList() {
        String code = "ldar_deviceType";
        //部门也是字典接口，传这个： 这样： sys_depart,depart_name,id
//        String code = "sys_depart";
        Subscribe.getDictList(code, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<DictItemBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<DictItemBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<DictItemBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                deviceTypeBeanList.addAll(dataList);
                                GasTableBean gasTableBean = new GasTableBean();
                                gasTableBean.id = "1";
                                gasTableBean.content = GsonUtils.toJson(deviceTypeBeanList);
                                AppDatabase.getInstance(BaseDataActivity.this).baseGasTableDao().insert(gasTableBean);

                            } else {
                                Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(BaseDataActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(BaseDataActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(BaseDataActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, BaseDataActivity.this));
    }


    /**
     * 标准气查询
     */
    private void getGasList() {
        Subscribe.getGasList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<BaseGasBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseGasBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseGasBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                GasTableBean gasTableBean = new GasTableBean();
                                gasTableBean.id = "1";
                                gasTableBean.content = GsonUtils.toJson(dataList);
                                AppDatabase.getInstance(BaseDataActivity.this).baseGasTableDao().insert(gasTableBean);
                                Toasty.success(BaseDataActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();

//                                String currentDate = DateUtil.getCurentTime();
//                                kv.encode(Constant.date_gas, StringUtils.nullStrToEmpty(currentDate));
//                                tv_last_date_gas.setText(last_time + currentDate);
                                setLastTime(Constant.date_gas, tv_last_date_gas);
//                                L.i("zzz1---currentDate->" + currentDate);
                            } else {
                                Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(BaseDataActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(BaseDataActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(BaseDataActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, BaseDataActivity.this));
    }

    private void setLastTime(String dateType, TextView tv){
        String currentDate = DateUtil.getCurentTime();
        kv.encode(dateType, StringUtils.nullStrToEmpty(currentDate));
        tv.setText(last_time + currentDate);
    }

    /**
     * 阈值查询
     */
    private void getThresholdList() {
        Subscribe.getThresholdList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<BaseGasBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseGasBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseGasBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                ThresholdTableBean thresholdTableBean = new ThresholdTableBean();
                                thresholdTableBean.id = "1";
                                thresholdTableBean.content = GsonUtils.toJson(dataList);
                                AppDatabase.getInstance(BaseDataActivity.this).baseThresholdTableDao().insert(thresholdTableBean);
                                Toasty.success(BaseDataActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();

                                setLastTime(Constant.thres_hold, tv_last_date_threshold);
                            } else {
                                Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(BaseDataActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(BaseDataActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(BaseDataActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, BaseDataActivity.this));
    }


    /**
     * 仪器查询
     */
    private void getInstrumentList() {
        Subscribe.getInstrumentList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<BaseInstrumentBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseInstrumentBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseInstrumentBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                InstrumentTableBean instrumentTableBean = new InstrumentTableBean();
                                instrumentTableBean.id = "1";
                                instrumentTableBean.content = GsonUtils.toJson(dataList);
                                AppDatabase.getInstance(BaseDataActivity.this).baseInstrumentTableDao().insert(instrumentTableBean);
                                Toasty.success(BaseDataActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();

                                setLastTime(Constant.instrument, tv_last_date_instrument);
                            } else {
                                Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(BaseDataActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(BaseDataActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(BaseDataActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, BaseDataActivity.this));
    }

    /**
     * 泄漏源查询
     */
    private void getLeakageList() {
        Subscribe.getLeakageList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<BaseLeakageBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseLeakageBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseLeakageBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                LeakageTableBean leakageTableBean = new LeakageTableBean();
                                leakageTableBean.id = "1";
                                leakageTableBean.content = GsonUtils.toJson(dataList);
                                AppDatabase.getInstance(BaseDataActivity.this).baseLeakageTableDao().insert(leakageTableBean);
                                Toasty.success(BaseDataActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();

                                setLastTime(Constant.leakage, tv_last_date_leakage_source);
                            } else {
                                Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(BaseDataActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(BaseDataActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(BaseDataActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, BaseDataActivity.this));
    }


    /**
     * 装置
     */
    private void getBaseDeviceList() {
        Subscribe.getBaseDeviceList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<BaseDeviceBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseDeviceBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseDeviceBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                BaseDeviceTableBean baseDeviceTableBean = new BaseDeviceTableBean();
                                baseDeviceTableBean.id = "1";
                                baseDeviceTableBean.content = GsonUtils.toJson(dataList);
                                AppDatabase.getInstance(BaseDataActivity.this).baseDeviceTableDao().insert(baseDeviceTableBean);
                                Toasty.success(BaseDataActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();

                            } else {
                                Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(BaseDataActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(BaseDataActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(BaseDataActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, BaseDataActivity.this));
    }

    /**
     * 设备
     */
    private void getBaseEquipmentList() {
        Subscribe.getBaseEquipmentList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<BaseEquipmentBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseEquipmentBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseEquipmentBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                L.i("zzz1-baseEquipmentBeans.list1111--->" + dataList.size());
                                BaseEquipmentTableBean baseEquipmentTableBean = new BaseEquipmentTableBean();
                                baseEquipmentTableBean.id = "1";
                                baseEquipmentTableBean.content = GsonUtils.toJson(dataList);
                                AppDatabase.getInstance(BaseDataActivity.this).baseEquipmentTableDao().insert(baseEquipmentTableBean);
                                Toasty.success(BaseDataActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();

                            } else {
                                Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(BaseDataActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(BaseDataActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(BaseDataActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, BaseDataActivity.this));
    }

    /**
     * 区域
     */
    private void getBaseAreaList() {
        Subscribe.getBaseAreaList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
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
                                AppDatabase.getInstance(BaseDataActivity.this).baseAreaTableDao().insert(baseAreaTableBean);
                                Toasty.success(BaseDataActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();

                                setLastTime(Constant.base_area, tv_last_date_area);
                            } else {
                                Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(BaseDataActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(BaseDataActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(BaseDataActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, BaseDataActivity.this));
    }

    /**
     * 化学品
     */
    private void getBaseChemicalList() {
        Subscribe.getBaseChemicalList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<BaseChemicalBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseChemicalBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseChemicalBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                BaseChemicalTableBean baseChemicalTableBean = new BaseChemicalTableBean();
                                baseChemicalTableBean.id = "1";
                                baseChemicalTableBean.content = GsonUtils.toJson(dataList);
                                AppDatabase.getInstance(BaseDataActivity.this).baseChemicalTableDao().insert(baseChemicalTableBean);
                                Toasty.success(BaseDataActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();

                                setLastTime(Constant.date_base_chemical, tv_last_date_chemical);
                            } else {
                                Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(BaseDataActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(BaseDataActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(BaseDataActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, BaseDataActivity.this));
    }

    /**
     * 组件类型
     */
    private void getBaseComponentTypeList() {
        Subscribe.getBaseComponentTypeList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<BaseComponentTypeBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseComponentTypeBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseComponentTypeBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                BaseComponentTypeTableBean baseComponentTypeTableBean = new BaseComponentTypeTableBean();
                                baseComponentTypeTableBean.id = "1";
                                baseComponentTypeTableBean.content = GsonUtils.toJson(dataList);
                                AppDatabase.getInstance(BaseDataActivity.this).baseComponentTypeTableDao().insert(baseComponentTypeTableBean);
                                Toasty.success(BaseDataActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();

                                setLastTime(Constant.date_base_component_type, tv_last_date_component_type);
                            } else {
                                Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(BaseDataActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(BaseDataActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(BaseDataActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, BaseDataActivity.this));
    }


    /**
     * 方向
     */
    private void getBaseDirectionList() {
        Subscribe.getBaseDirectionList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<BaseDirectionBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseDirectionBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseDirectionBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                BaseDirectionTableBean baseDirectionTableBean = new BaseDirectionTableBean();
                                baseDirectionTableBean.id = "1";
                                baseDirectionTableBean.content = GsonUtils.toJson(dataList);
                                AppDatabase.getInstance(BaseDataActivity.this).baseDirectionTableDao().insert(baseDirectionTableBean);
                                Toasty.success(BaseDataActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();

                                setLastTime(Constant.date_base_direction, tv_last_date_direction);
                            } else {
                                Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(BaseDataActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(BaseDataActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(BaseDataActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(BaseDataActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, BaseDataActivity.this));
    }


}