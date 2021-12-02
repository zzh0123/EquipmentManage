package com.equipmentmanage.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.PointBean;
import com.equipmentmanage.app.bean.TagGroupModel;
import com.equipmentmanage.app.bean.TaskRecordsTableBean;
import com.equipmentmanage.app.bean.TaskResultBean;
import com.equipmentmanage.app.bean.TaskTableBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.utils.DirectionUtils;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.view.BluetoothDialog;
import com.equipmentmanage.app.view.SelectDialog;
import com.equipmentmanage.app.view.TagImageView;
import com.equipmentmanage.app.view.TipDialog;
import com.google.gson.reflect.TypeToken;
import com.licrafter.tagview.DIRECTION;
import com.licrafter.tagview.TagViewGroup;
import com.licrafter.tagview.views.ITagView;
import com.tencent.mmkv.MMKV;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.CountDownButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 组件检测-密封点检测，（原 PartImgsFragment）
 * @Author: zzh
 * @CreateDate: 2021/11/20
 */
public class PartImgsActivity extends BaseActivity {

    public static void open(Context c, TaskResultBean.Records bean) {
        Intent i = new Intent(c, PartImgsActivity.class);
        i.putExtra(Constant.taskBean, bean);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.tagImageView)
    TagImageView tagImageView; //组件图片

    @BindView(R.id.tv_read_value)
    TextView tvReadValue; //读数

    @BindView(R.id.tv_read)
    CountDownButton tvReread; //读数

    @BindView(R.id.tv_last)
    TextView tvLast; //上一个

    @BindView(R.id.tv_next)
    TextView tvNext; //下一个

    @BindView(R.id.progress_bar)
    ProgressBar progressBar; //进度

    @BindView(R.id.tv_progress)
    TextView tv_progress; //进度

    @BindView(R.id.tv_tag_num)
    TextView tv_tag_num; //下一个

    // 任务
    private TaskResultBean.Records bean;
    // 全部任务
    private List<TaskResultBean.Records> mList = new ArrayList<>();
    // 要缓存的任务
    private List<TaskResultBean.Records> records = new ArrayList<>();
    // 图片集合（1个任务对应多个图片）
    private List<TaskResultBean.LiveTaskAppPicPageList> liveTaskAppPicPageList;
    // 当前检测的图片
    private TaskResultBean.LiveTaskAppPicPageList currentPic;
    private int picSize, picCurrentPos = 0;
    private List<TaskResultBean.LiveTaskAppAssignedPageList> currentPicDots;
    private int currentPicDotsSize, dotCurrentPos = 0;

    private String readValue, bgValue;
    private boolean isNext = false;

    private int checkedCount = 0;
    private int totalCount = 0;
    private int progress = 0;
    private String progressStr = "";

    private String id = "";

    private MMKV kv = MMKV.defaultMMKV();

    private BluetoothDialog checkDialog2;

    private boolean mIsCon = false;

    private String currentAreaId, lastAreaId;
    private TaskResultBean.LiveTaskAppPicPageList lastPic;
    private List<TaskResultBean.LiveTaskAppAssignedPageList> lastPicDots;

    private TipDialog tipDialog;

    private boolean isGetBgValue = false;

    @Override
    protected int initLayout() {
        return R.layout.activity_part_imgs;
    }

    @Override
    protected void initView() {
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                saveData();
                saveLastPos();
                EventBus.getDefault().post(ConstantValue.event_1);
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

        tipDialog = new TipDialog(this);
        tipDialog.setOnConfirmListener(new TipDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                // 直接让他获取当前区域的背景值赋值给切换新区域的图片背景值
                checkDialog2.sendMsg(ConstantValue.log_start);
                isGetBgValue = true;
                tipDialog.dismiss();
            }
        });

        bean = (TaskResultBean.Records) getIntent().getSerializableExtra(Constant.taskBean);
        L.i("zzz1--bean0->" + bean.getTaskName());
        id = bean.getId();
        readCache();



    }

    @Override
    protected void initEvent() {
//        tvReread.setCountDownTime();
    }

    @Override
    protected void initData() {
        tagImageView.setTagGroupClickListener(mTagGroupClickListener);
//        bean = (TaskResultBean.Records) getIntent().getSerializableExtra(Constant.taskBean);

//        if (bean != null){ // 上个界面已经判断

        // 每个任务多个图片，1个图片多个点，把点检测完，进行下一张图片
        // 检测是蓝牙读数
        // BigDecimal.valueOf(i)  setDetectionVal  读取的
        //"检测组件")


        //  先检测背景纸  弹框检测背景纸---》接着检测  所有点默认第一次 areaid

        // areaid 变化 ，重新检测背景纸  ，
        // 图片集合
        liveTaskAppPicPageList = bean.getLiveTaskAppPicPageList();
        if (liveTaskAppPicPageList != null && liveTaskAppPicPageList.size() > 0) {
            picSize = liveTaskAppPicPageList.size();
            L.i("zzz1--picSize->" + picSize);
            refreshData();
//            dotCurrentPos;

            for (int i = 0; i < picSize; i++) {
                TaskResultBean.LiveTaskAppPicPageList pic = liveTaskAppPicPageList.get(i);
                List<TaskResultBean.LiveTaskAppAssignedPageList> picDotList = pic.getLiveTaskAppAssignedPageList();
                totalCount += picDotList.size();
            }

        } else {
            Toasty.warning(this, "没有要检测的点！", Toast.LENGTH_SHORT, false).show();
        }

        // 进度
        refreshProgress();
        setAreaId();




        // 测试数据
//        if (liveTaskAppPicPageList != null && liveTaskAppPicPageList.size() > 0) {
//            for (int i = 0; i < liveTaskAppPicPageList.size(); i++) {
//                TaskResultBean.LiveTaskAppPicPageList pic = liveTaskAppPicPageList.get(i);
//                List<TaskResultBean.LiveTaskAppAssignedPageList> picDotList = pic.getLiveTaskAppAssignedPageList();
//                for (int j = 0; j < picDotList.size(); j++) {
//                    picDotList.get(j).setDetectionVal(String.valueOf(j + 100));
////                    picDotList.get(j).setBackgrVal();
//                }
//            }
//        }


//        records.add(bean);


        // liveTaskAppPicPageList
//        tagImageView.setImageUrl(bean.getLiveTaskAppPicPageList().get(0).getPictPath());


        // 测试数据
//        initTestData();
        // 测试数据
//        TaskRecordsTableBean taskRecordsTableBean = new TaskRecordsTableBean();
//        taskRecordsTableBean.id = "1";
//        taskRecordsTableBean.content = GsonUtils.toJson(records);
//        AppDatabase.getInstance(this).taskRecordsTableDao().insert(taskRecordsTableBean);
//        Toasty.success(BaseDataActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();

        // 正式数据

    }

    private void saveData() {
        records.add(bean);
        TaskRecordsTableBean taskRecordsTableBean = new TaskRecordsTableBean();
        taskRecordsTableBean.id = bean.getId();
        taskRecordsTableBean.content = GsonUtils.toJson(records);
        Long rowId = AppDatabase.getInstance(this).taskRecordsTableDao().insert(taskRecordsTableBean);
        if (rowId != null) {
            // 最后一图图片的最后一个点  代表检测完成
//            if (picCurrentPos == (picSize - 1) && ) {
//
//            }
            Toasty.success(PartImgsActivity.this, "保存成功！", Toast.LENGTH_SHORT, true).show();
        } else {
            Toasty.error(PartImgsActivity.this, "保存失败！", Toast.LENGTH_SHORT, true).show();
        }
    }

    private void refreshData() {
        currentPic = liveTaskAppPicPageList.get(picCurrentPos);
        tagImageView.setImageUrl(StringUtils.nullStrToEmpty(currentPic.getPictPath()));
        tv_tag_num.setText(StringUtils.nullStrToEmpty(currentPic.getTagNum()));
        // 当前图片点集合
        currentPicDots = currentPic.getLiveTaskAppAssignedPageList();
        currentPicDotsSize = currentPicDots.size();
    }

    private void refreshProgress() {
        if (totalCount != 0) {
            progress = (checkedCount * 100) / totalCount;
            progressStr = checkedCount + "/" + totalCount;
        } else {
            progress = 0;
            progressStr = "0/0";
        }

        tv_progress.setText(progressStr);
        progressBar.setProgress(progress);
    }

    /**
     * 任务-缓存
     */
    private void readCache() {
        L.i("zzz1-bean.getId()-->" + bean.getId());
        TaskRecordsTableBean list = AppDatabase.getInstance(this).taskRecordsTableDao().loadById(id);
//        L.i("zzz1-taskBeanList.content--->" + list.content);
        if (list != null) {
            List<TaskResultBean.Records> taskBeanList = GsonUtils.fromJson(list.content, new TypeToken<List<TaskResultBean.Records>>() {
            }.getType());

            L.i("zzz1-taskBeanList.size--->" + taskBeanList.size());

            mList.clear();
            if (taskBeanList != null && taskBeanList.size() > 0) {
                mList.addAll(taskBeanList);
                bean = mList.get(0);
                L.i("zzz1--bean01->" + bean.getTaskName());

                picCurrentPos = kv.getInt(Constant.picCurrentPos + id, 0);
                dotCurrentPos = kv.getInt(Constant.dotCurrentPos+ id, 0);
                checkedCount = kv.getInt(Constant.checkedCount+ id, 0);

            }

        }

    }

    private void setAreaId() {
        currentAreaId = currentPicDots.get(dotCurrentPos).getAreaId();
        if (picCurrentPos > 0) {
            if (dotCurrentPos > 0) {
                lastAreaId = currentPicDots.get(dotCurrentPos - 1).getAreaId();
            } else {

                lastPic = liveTaskAppPicPageList.get(picCurrentPos - 1);
                lastPicDots = lastPic.getLiveTaskAppAssignedPageList();

                lastAreaId = lastPicDots.get((lastPicDots.size() - 1)).getAreaId();
            }
        } else {
            // 第一张图片的判断
            //
            if (dotCurrentPos > 0) {
                lastAreaId = currentPicDots.get(dotCurrentPos - 1).getAreaId();
            } else {

                lastAreaId = currentAreaId;
            }
        }

        if (!(lastAreaId.equals(currentAreaId))){
            showBgDialog();
        }

    }

    private void showBgDialog(){
        if (tipDialog == null) {
            tipDialog = new TipDialog(this);
        }
        tipDialog.show();
        tipDialog.setTitleAndTip(null, "请获取背景值！");
    }

    @OnClick({R.id.tv_read, R.id.tv_next, R.id.tv_last, R.id.tagImageView})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_read:
                if (mIsCon) {
                    checkDialog2.sendMsg(ConstantValue.log_start);
                } else {
                    Toasty.warning(this, "请先连接设备！", Toast.LENGTH_SHORT, true).show();
                }

                break;

            case R.id.tv_next: // 下一个
                if (!isNext) {
                    Toasty.warning(this, "请先完成当前点的检测！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                isNext = false;

                if (dotCurrentPos < (currentPicDotsSize - 1)) {
                    dotCurrentPos++;

                    checkedCount++;
                    refreshProgress();
                } else {
                    // >= 下一个图片
                    if (picCurrentPos < (picSize - 1)) {
                        L.i("zzz1--nPos->" + picCurrentPos + "---" + dotCurrentPos);
                        dotCurrentPos = 0;
                        checkedCount++;
                        refreshProgress();

                        picCurrentPos++;
                        refreshData();
                    } else {
                        Toasty.warning(this, "已经是最后一张图片了！", Toast.LENGTH_SHORT, true).show();
                    }

                }
                L.i("zzz1--Pos->" + picCurrentPos + "---" + dotCurrentPos);

                setAreaId();
                break;

            case R.id.tv_last: // 上一个
                if (dotCurrentPos > 0) {
                    dotCurrentPos--;
                    checkedCount--;
                    refreshProgress();
                } else {
                    // >= 下一个图片
                    if (picCurrentPos > 0) {
                        picCurrentPos--;
                        refreshData();
                        dotCurrentPos = currentPicDotsSize - 1;
                        checkedCount--;
                        refreshProgress();
                    } else {
                        Toasty.warning(this, "已经是第一张图片了！", Toast.LENGTH_SHORT, true).show();
                    }

                }
                L.i("zzz1--Pos->" + picCurrentPos + "---" + dotCurrentPos);
                setAreaId();
                break;

            case R.id.tagImageView:
//                if (valid()){
//                    login();
//                }
//                accessResultList();
                break;
        }
    }

    private void showCheckDialog() {
        checkDialog2 = new BluetoothDialog(this);
        checkDialog2.setOnSelectClickListener(new BluetoothDialog.OnSelectClickListener() {
            @Override
            public void onSelectClick(String s) {
                if (!StringUtils.isNullOrEmpty(s)) {
                    if (s.equals(ConstantValue.log_error)) {
                        Toasty.warning(PartImgsActivity.this, "无效的指令！", Toast.LENGTH_SHORT, true).show();
                        return;
                    }

                    if (isGetBgValue){
                        isGetBgValue = false;
                        bgValue = s;
                        setCurrentDotBgValue(bgValue);
                    } else {
                        readValue = s;
                        tvReadValue.setText(readValue);
                        // 校验点
                        isNext = true;
                        setCurrentDotDetectValue(dotCurrentPos, readValue);

                        // 净检测值 与 泄露阈值进行比较
                        // 净检测值 >= 泄露阈值 是否泄露 是
                        // 净检测值 < 泄露阈值 是否泄露 否
                        // 1是0否  如果泄露，提示下泄露，继续检测下一个点
//                        L.i("zzz1--readValue->" + readValue);
                        if (Double.parseDouble(readValue) >= Double.parseDouble(currentPicDots.get(dotCurrentPos).getThresholdValue())){
                            Toasty.error(PartImgsActivity.this, "当前检测点泄露！", Toast.LENGTH_LONG, true).show();
                            currentPicDots.get(dotCurrentPos).setIsleakage("1");
                        } else {
                            currentPicDots.get(dotCurrentPos).setIsleakage("0");
                        }
                    }
                    checkDialog2.dismiss();
//                    Toasty.normal(PartImgsActivity.this, s, Toast.LENGTH_SHORT).show();
                } else {
                    isNext = false;
                    Toasty.warning(PartImgsActivity.this, "读取的数据为空！", Toast.LENGTH_SHORT, true).show();
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

    private void setCurrentDotBgValue(String bgValue){
        for (int i = 0; i < currentPicDots.size(); i++){
            // setBackgrVal
            currentPicDots.get(i).setBackgrVal(bgValue);
        }
    }

    private void setCurrentDotDetectValue(int dotCurrentPos, String detectionVal){
        currentPicDots.get(dotCurrentPos).setDetectionVal(detectionVal);
    }


    private void saveLastPos() {
        kv.encode(Constant.picCurrentPos + id, picCurrentPos);
        kv.encode(Constant.dotCurrentPos + id, dotCurrentPos);
        kv.encode(Constant.checkedCount + id, checkedCount);
        kv.encode(Constant.totalCount + id, totalCount);
    }

    private TagViewGroup.OnTagGroupClickListener mTagGroupClickListener = new TagViewGroup.OnTagGroupClickListener() {
        @Override
        public void onCircleClick(TagViewGroup container) {
//            Toast.makeText(MainActivity.this, "点击中心圆", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTagClick(TagViewGroup container, ITagView tag, int position) {
//            Toast.makeText(MainActivity.this, "点击Tag->" + ((TagTextView) tag).getText().toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLongPress(final TagViewGroup group) {
//            Toast.makeText(MainActivity.this, "点击中心圆", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        saveData();
        saveLastPos();
        EventBus.getDefault().post(ConstantValue.event_1);
        finish();
//        super.onBackPressed();
    }

    private void initTestData() {
        tagImageView.setImageRes(R.mipmap.ic_test1);
        List<TagGroupModel> tagGroupList = new ArrayList<>();
        TagGroupModel tagGroupModel = new TagGroupModel();
        List<TagGroupModel.Tag> tagList = new ArrayList<>();
        TagGroupModel.Tag tag = new TagGroupModel.Tag();
        tag.setName("V0001");
        tag.setDirection(DirectionUtils.getValue(DIRECTION.RIGHT_TOP_STRAIGHT));
        tagList.add(tag);
        tagGroupModel.setTags(tagList);
        tagGroupModel.setPercentX(0.4f);
        tagGroupModel.setPercentY(0.8f);

        TagGroupModel tagGroupModel1 = new TagGroupModel();
        List<TagGroupModel.Tag> tagList1 = new ArrayList<>();
        TagGroupModel.Tag tag1 = new TagGroupModel.Tag();
        tag1.setName("V0002");
        tag1.setDirection(DirectionUtils.getValue(DIRECTION.RIGHT_TOP_STRAIGHT));
        tagList1.add(tag1);
        tagGroupModel1.setTags(tagList1);
        tagGroupModel1.setPercentX(0.5f);
        tagGroupModel1.setPercentY(0.5f);

        TagGroupModel tagGroupModel2 = new TagGroupModel();
        List<TagGroupModel.Tag> tagList2 = new ArrayList<>();
        TagGroupModel.Tag tag2 = new TagGroupModel.Tag();
        tag2.setName("V0003");
        tag2.setDirection(DirectionUtils.getValue(DIRECTION.RIGHT_TOP_STRAIGHT));
        tagList2.add(tag2);
        tagGroupModel2.setTags(tagList2);
        tagGroupModel2.setPercentX(0.2f);
        tagGroupModel2.setPercentY(0.6f);

        tagGroupList.add(tagGroupModel);
        tagGroupList.add(tagGroupModel1);
        tagGroupList.add(tagGroupModel2);
//        tagImageView.setTag(tagGroupModel);
        tagImageView.setTagList(tagGroupList);


        liveTaskAppPicPageList = bean.getLiveTaskAppPicPageList();
        if (liveTaskAppPicPageList != null && liveTaskAppPicPageList.size() > 0) {
            for (int i = 0; i < liveTaskAppPicPageList.size(); i++) {
                TaskResultBean.LiveTaskAppPicPageList pic = liveTaskAppPicPageList.get(i);
                List<TaskResultBean.LiveTaskAppAssignedPageList> picDotList = pic.getLiveTaskAppAssignedPageList();
                for (int j = 0; j < picDotList.size(); j++) {
                    picDotList.get(j).setDetectionVal(String.valueOf(j));
//                    picDotList.get(j).setBackgrVal();
                }
            }
        }
    }
}