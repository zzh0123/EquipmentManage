package com.equipmentmanage.app.activity;

import static com.equipmentmanage.app.base.MyApplication.getContext;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.ImgTableBean;
import com.equipmentmanage.app.bean.ImgTableBean1;
import com.equipmentmanage.app.bean.NewRecordBean;
import com.equipmentmanage.app.bean.PointBean;
import com.equipmentmanage.app.bean.PointBean1;
import com.equipmentmanage.app.bean.TagGroupModel;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.utils.DateUtil;
import com.equipmentmanage.app.utils.DirectionUtils;
import com.equipmentmanage.app.utils.DpUtil;
import com.equipmentmanage.app.utils.GlideCacheEngine;
import com.equipmentmanage.app.utils.GlideEngine;
import com.equipmentmanage.app.utils.GsonUtils;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.view.SelectDialog;
import com.equipmentmanage.app.view.SelectDialog1;
import com.equipmentmanage.app.view.TagImageView;
import com.equipmentmanage.app.view.TipDialog;
import com.equipmentmanage.app.view.TipDialog1;
import com.google.gson.reflect.TypeToken;
import com.licrafter.tagview.DIRECTION;
import com.licrafter.tagview.TagViewGroup;
import com.licrafter.tagview.views.ITagView;
import com.licrafter.tagview.views.TagTextView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.manager.PictureCacheManager;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.yalantis.ucrop.view.OverlayView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: ???????????? -- ?????? -- ??????
 * @Author: zzh
 * @CreateDate: 2021/12/12
 */
public class TakePhotoActivity1 extends BaseActivity {

    //
    //                        et_manufacturer.getText().toString().trim(), tv_production_date.getText().toString().trim(),
    //                        tv_unreachable.getText().toString().trim(), tv_heat_preservation.getText().toString().trim(),
    //                        et_oper_temper.getText().toString().trim(), et_oper_pressure.getText().toString().trim(),
    //                        et_barcode.getText().toString().trim(), et_detection_freq.getText().toString().trim(),
    //                        et_detection_freq.getText().toString().trim(), et_leakage_threshold.getText().toString().trim());
    public static void open(Context c, boolean isNewAdd, String fileName, String localPath, String content, String deviceCode, String deviceName, String deviceType, String deviceId,
                            String areaCode, String areaName,
                            String equipCode, String equipName,
                            String mediumState, String streamTypeValue, String prodStreamName, String chemicalName, String chemicalTypeValue,
                            String directionTypeName, String directionTypeValue, String directionTypeName1, String directionTypeValue1,
                            String tagNum, String tagNumPre, String tagNumValue, String reference, String distance, String height,
                            String floorLevel,
                            String unreachable, String unreachableReason,
                            String remark) {
        Intent i = new Intent(c, TakePhotoActivity1.class);
        i.putExtra(Constant.isNewAdd, isNewAdd);
        i.putExtra(Constant.fileName, fileName);
        i.putExtra(Constant.localPath, localPath);
        i.putExtra(Constant.content, content);

        i.putExtra(Constant.deviceCode, deviceCode);
        i.putExtra(Constant.deviceName, deviceName);
        i.putExtra(Constant.deviceType, deviceType);
        i.putExtra(Constant.deviceId, deviceId);

        i.putExtra(Constant.areaCode, areaCode);
        i.putExtra(Constant.areaName, areaName);
        i.putExtra(Constant.equipCode, equipCode);
        i.putExtra(Constant.equipName, equipName);

        i.putExtra(Constant.mediumState, mediumState);
        i.putExtra(Constant.streamTypeValue, streamTypeValue);
        i.putExtra(Constant.prodStreamName, prodStreamName);

        i.putExtra(Constant.chemicalName, chemicalName);
        i.putExtra(Constant.chemicalTypeValue, chemicalTypeValue);
        i.putExtra(Constant.directionName, directionTypeName);
        i.putExtra(Constant.directionTypeValue, directionTypeValue);
        i.putExtra(Constant.directionName1, directionTypeName1);
        i.putExtra(Constant.directionTypeValue1, directionTypeValue1);

        i.putExtra(Constant.tagNum, tagNum);
        i.putExtra(Constant.tagNumPre, tagNumPre);
        i.putExtra(Constant.tagNumValue, tagNumValue);
        i.putExtra(Constant.reference, reference);
        i.putExtra(Constant.distance, distance);
        i.putExtra(Constant.height, height);
        i.putExtra(Constant.floorLevel, floorLevel);

        i.putExtra(Constant.unreachable, unreachable);
        i.putExtra(Constant.unreachableReason, unreachableReason);
        i.putExtra(Constant.remark, remark);


//        i.putExtra(Constant.detectionFreq, detectionFreq);
//        i.putExtra(Constant.leakageThreshold, leakageThreshold);
        c.startActivity(i);
    }

    private String deviceCode, deviceName, deviceType, deviceId,
            areaCode, areaName, equipCode, equipName,
            mediumState, streamTypeValue, prodStreamName, chemicalName, chemicalTypeValue,
            directionTypeName, directionTypeValue, directionTypeName1, directionTypeValue1;

    private String tagNum, tagNumPre, tagNumValue, reference, distance, height, floorLevel,
            unreachable, unreachableReason, remark;

    @BindView(R.id.titleBar)
    TitleBar titleBar; //?????????

    @BindView(R.id.sb_recall)
    SuperButton sb_recall; //??????

    @BindView(R.id.sb_save)
    SuperButton sb_save; //??????

    @BindView(R.id.sb_remake)
    SuperButton sb_remake; //??????


    @BindView(R.id.tagImageView)
    TagImageView tagImageView; //????????????

    List<TagGroupModel> tagGroupList = new ArrayList<>();

    // compress path
    private String compressPath, fileName;

    private String componentType;

    private SelectDialog1 checkDialog2;
    private int index = 1;

    private DisplayMetrics dm;
    int screenWidth, screenHeight;

    List<PointBean1> pointBeanList = new ArrayList<>();

    private ActivityResultLauncher<Intent> launcherResult;

    // ????????????
    private PictureWindowAnimationStyle mWindowAnimationStyle = PictureWindowAnimationStyle.ofDefaultWindowAnimationStyle();
    private int maxSelectNum = 1;
    private int aspect_ratio_x = 16, aspect_ratio_y = 9;

    private final static String TAG = "zzz1-->";

    private String currentDate;

    // ??????
    private GestureDetector detector;

    // ?????????
    private TipDialog1 tipDialog;
    private int deletePos;

    private String content, localPath;
    // ????????????
    private Boolean isNewAdd = true;

    @Override
    protected int initLayout() {
        return R.layout.activity_take_photo;
    }

    @Override
    protected void initView() {

        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener());
        detector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.i("zzz1--->", "onDoubleTap--getX--" + e.getX() + "--getY--" + e.getY());
                float getX = (float) e.getX();
                float getY = (float) e.getY();
//                L.i("zzz1--????????????->" + getX + "--getY->" + getY);
                if (!StringUtils.isNullOrEmpty(compressPath)) {
                    showCheckDialog(getX, getY, false);
                } else {
                    Toasty.warning(TakePhotoActivity1.this, "???????????????", Toast.LENGTH_SHORT, true).show();
                }
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });

        currentDate = DateUtil.getCurentTime1(); // 2021-11-20
        isNewAdd = getIntent().getBooleanExtra(Constant.isNewAdd, true);

        deviceCode = getIntent().getStringExtra(Constant.deviceCode);
        deviceName = getIntent().getStringExtra(Constant.deviceName);
        deviceType = getIntent().getStringExtra(Constant.deviceType);
        deviceId = getIntent().getStringExtra(Constant.deviceId);

        areaCode = getIntent().getStringExtra(Constant.areaCode);
        areaName = getIntent().getStringExtra(Constant.areaName);
        equipCode = getIntent().getStringExtra(Constant.equipCode);
        equipName = getIntent().getStringExtra(Constant.equipName);

        mediumState = getIntent().getStringExtra(Constant.mediumState);
        streamTypeValue = getIntent().getStringExtra(Constant.streamTypeValue);
        prodStreamName = getIntent().getStringExtra(Constant.prodStreamName);

        chemicalName = getIntent().getStringExtra(Constant.chemicalName);
        chemicalTypeValue = getIntent().getStringExtra(Constant.chemicalTypeValue);
        directionTypeName = getIntent().getStringExtra(Constant.directionName);
        directionTypeValue = getIntent().getStringExtra(Constant.directionTypeValue);
        directionTypeName1 = getIntent().getStringExtra(Constant.directionName1);
        directionTypeValue1 = getIntent().getStringExtra(Constant.directionTypeValue1);

        tagNum = getIntent().getStringExtra(Constant.tagNum);
        tagNumPre = getIntent().getStringExtra(Constant.tagNumPre);
        tagNumValue = getIntent().getStringExtra(Constant.tagNumValue);
        reference = getIntent().getStringExtra(Constant.reference);
        distance = getIntent().getStringExtra(Constant.distance);
        height = getIntent().getStringExtra(Constant.height);
        floorLevel = getIntent().getStringExtra(Constant.floorLevel);

        unreachable = getIntent().getStringExtra(Constant.unreachable);
        unreachableReason = getIntent().getStringExtra(Constant.unreachableReason);
        remark = getIntent().getStringExtra(Constant.remark);

        screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // ???????????????????????????480px???
        screenHeight = getWindowManager().getDefaultDisplay().getHeight() - DpUtil.dip2px(this, 45); // ???????????????????????????800p???
//        screenHeight = screenHeight/2;
//        screenHeight = (int) (screenHeight - titleBar.getY()
//                        - llType.getY()- llType.getY() -rvList.getY() -test.getY());
        L.i(" zzz1-wh->", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);

//        dm = new DisplayMetrics();
//        dm = getResources().getDisplayMetrics();
//        float density = dm.density; // ??????????????????????????????0.75/1.0/1.5/2.0???
//        int densityDPI = dm.densityDpi; // ??????????????????????????????120/160/240/320???
//        float xdpi = dm.xdpi;
//        float ydpi = dm.ydpi;
//        Log.e(TAG + " DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);
//        Log.e(TAG + " DisplayMetrics", "density=" + density + "; densityDPI=" + densityDPI);
//        screenWidth = dm.widthPixels; // ???????????????????????????480px???
//        screenHeight = dm.heightPixels; // ???????????????????????????800px???
//        L.i(" zzz1-wh->", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);

        // ??????????????????onCreate???Fragment onAttach??????????????????java.lang.IllegalStateException??????
        launcherResult = createActivityResultLauncher();

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                save();
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
//                return R.mipmap.ic_photo;
//            }
//
//            @Override
//            public void performAction(View view) {
//                L.i("zzz1--->takephoto");
////                refresh();
//                takePhoto();
//            }
//
//            @Override
//            public int leftPadding() {
//                return 0;
//            }
//
//            @Override
//            public int rightPadding() {
//                return 20;
//            }
//        });

        titleBar.addAction(new TitleBar.Action() {
            @Override
            public String getText() {
                return "??????";
            }

            @Override
            public int getDrawable() {
                return 0;
            }

            @Override
            public void performAction(View view) {
                L.i("zzz1--->recall--" + index);

                if (tagGroupList != null && tagGroupList.size() > 0) {
                    index--;
                    tagGroupList.remove((index - 1));
                    tagImageView.setTagList(tagGroupList);
                    pointBeanList.remove((index - 1));
                } else {
                    Toasty.warning(TakePhotoActivity1.this, "???????????????????????????", Toast.LENGTH_SHORT, true).show();
                }
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


        tipDialog = new TipDialog1(this);
        tipDialog.setOnConfirmListener(new TipDialog1.OnConfirmListener() {
            @Override
            public void onConfirm() {
                // ?????????
                tagGroupList.remove(deletePos);
                tagImageView.setTagList(tagGroupList);
                pointBeanList.remove(deletePos);
            }

            @Override
            public void onEdit() {
                // String type, String heatPre, String size, String count)
                PointBean1 pointBean = pointBeanList.get(deletePos);

                showCheckDialog(0, 0, true);
                checkDialog2.setEdit(pointBean.getComponentType(), pointBean.getHeatPreser(), "" + pointBean.getComponentSize(), "");
            }
        });
    }

    private void showDeletePointDialog() {
        if (tipDialog == null) {
            tipDialog = new TipDialog1(this);
        }
        tipDialog.show();
        tipDialog.setTitleAndTip(null, "???????????????????????????");
    }

    @Override
    protected void initEvent() {
        tagImageView.setEditMode(true);
//        tagImageView.setOnTouchListener(this);
        tagImageView.setTagGroupClickListener(mTagGroupClickListener);
        tagImageView.setTagGroupScrollListener(mTagGroupDragListener);

        tagImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // ??????????????? ?????????detector???onTouchEvent()?????????????????????true??????????????????????????????
                detector.onTouchEvent(event);
                return true;
            }
        });

        fileName = getIntent().getStringExtra(Constant.fileName);
        compressPath = getIntent().getStringExtra(Constant.localPath);
        L.i("zzz1--compressPath222-->" + compressPath);
        tagImageView.setImageUrl(compressPath);
        // ??????
        if (!isNewAdd) {
            content = getIntent().getStringExtra(Constant.content);

            List<PointBean1> pointBeans = com.equipmentmanage.app.utils.gson.GsonUtils.fromJson(content, new TypeToken<List<PointBean1>>() {
            }.getType());

            pointBeanList.clear();
            if (pointBeans != null && pointBeans.size() > 0) {
                index = pointBeans.size() + 1;

                pointBeanList.addAll(pointBeans);

                for (int i = 0; i < pointBeanList.size(); i++) {
                    TagGroupModel tagGroupModel = new TagGroupModel();
                    List<TagGroupModel.Tag> tagList = new ArrayList<>();
                    TagGroupModel.Tag tag = new TagGroupModel.Tag();
                    tag.setName(pointBeanList.get(i).getComponentType() + "00" + pointBeanList.get(i).getExtNum());
                    tag.setDirection(DirectionUtils.getValue(DIRECTION.RIGHT_TOP_STRAIGHT));
                    tagList.add(tag);
                    tagGroupModel.setTags(tagList);
                    tagGroupModel.setPercentX(pointBeanList.get(i).getPointx());
                    tagGroupModel.setPercentY(pointBeanList.get(i).getPointy());
                    tagGroupModel.setPercentX1(pointBeanList.get(i).getPointx1());
                    tagGroupModel.setPercentY1(pointBeanList.get(i).getPointy1());

                    //                tagImageView.setTag(tagGroupModel);
                    tagGroupList.add(tagGroupModel);
                }

                tagImageView.setTagList(tagGroupList);
            }
        }
    }

    @Override
    protected void initData() {
//        refresh();
    }

    @OnClick({R.id.sb_save, R.id.sb_recall, R.id.sb_remake})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sb_remake:
                fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
                deleteTag();
                break;

            case R.id.sb_save:
                save();
                finish();
                break;

            case R.id.sb_recall:
                if (tagGroupList != null && tagGroupList.size() > 0) {
                    index--;
                    tagGroupList.remove((index - 1));
                    tagImageView.setTagList(tagGroupList);
                    pointBeanList.remove((index - 1));
                } else {
                    Toasty.warning(TakePhotoActivity1.this, "???????????????????????????", Toast.LENGTH_SHORT, true).show();
                }
                break;

        }
    }

    private void deleteTag() {
        int rowCount = AppDatabase.getInstance(TakePhotoActivity1.this)
                .imgTableDao1()
                .deleteByTagNum(currentDate, deviceId, areaCode, equipCode, tagNum);
        L.i("zzz1--rowCount->" + rowCount);
        if (rowCount >= 0) {
//            Toasty.success(TakePhotoActivity1.this, "???????????????", Toast.LENGTH_SHORT, true).show();
            takePhoto();
        } else {
//            Toasty.error(TakePhotoActivity1.this, "???????????????", Toast.LENGTH_SHORT, true).show();
        }
    }

    private void save() {
        if (StringUtils.isNullOrEmpty(deviceCode) || StringUtils.isNullOrEmpty(areaCode)
                || StringUtils.isNullOrEmpty(equipCode)) {
            Toasty.warning(TakePhotoActivity1.this, "???????????????????????????????????????", Toast.LENGTH_SHORT, true).show();
            return;
        }

        if (!StringUtils.isNullOrEmpty(compressPath)) {
            if (tagGroupList != null && tagGroupList.size() > 0) {
                for (int i = 0; i < tagGroupList.size(); i++) {
                    pointBeanList.get(i).setPointx(tagGroupList.get(i).getPercentX());
                    pointBeanList.get(i).setPointy(tagGroupList.get(i).getPercentY());
                    pointBeanList.get(i).setPointx1(tagGroupList.get(i).getPercentX1());
                    pointBeanList.get(i).setPointy1(tagGroupList.get(i).getPercentY1());
                }
            }

//            L.i("zzz1--pointBeanListJson->" + GsonUtils.toJson(pointBeanList));

            ImgTableBean1 imgTableBean = new ImgTableBean1();
            imgTableBean.fileName = fileName;
            imgTableBean.localPath = compressPath;
            imgTableBean.content = GsonUtils.toJson(pointBeanList);
            imgTableBean.pointCount = "" + pointBeanList.size();
            imgTableBean.currentDate = currentDate;

            imgTableBean.deviceCode = deviceCode;
            imgTableBean.deviceName = deviceName;
            imgTableBean.deviceType = deviceType;
            imgTableBean.deviceId = deviceId;

            imgTableBean.areaCode = areaCode;
            imgTableBean.areaName = areaName;
            imgTableBean.equipmentCode = equipCode;
            imgTableBean.equipName = equipName;

            imgTableBean.tagNum = tagNum;
            imgTableBean.tagNumPre = tagNumPre;
            imgTableBean.tagNumValue = tagNumValue;
            imgTableBean.refMaterial = reference;

            imgTableBean.directionValue = directionTypeValue;
            imgTableBean.directionName = directionTypeName;
            imgTableBean.directionPosValue = directionTypeValue1;
            imgTableBean.directionPosName = directionTypeName1;

            imgTableBean.distance = distance;
            imgTableBean.height = height;
            imgTableBean.floorLevel = floorLevel;

            imgTableBean.remark = remark;
//            imgTableBean.componentType = componentType;
//            imgTableBean.heatPreser = heatPreservation;
//            imgTableBean.componentSize = size;
            imgTableBean.mediumState = mediumState;
            imgTableBean.prodStream = streamTypeValue;
            imgTableBean.prodStreamName = prodStreamName;

            imgTableBean.unreachable = unreachable;
            imgTableBean.unreachableDesc = unreachableReason;
            imgTableBean.mainMedium = chemicalName;
//            imgTableBean.pictPath = chemicalTypeValue;
            imgTableBean.chemicalName = chemicalName;

            Long rowId = AppDatabase.getInstance(TakePhotoActivity1.this).imgTableDao1().insert(imgTableBean);
            if (rowId != null) {
                Toasty.success(TakePhotoActivity1.this, "???????????????", Toast.LENGTH_SHORT, true).show();
                EventBus.getDefault().post(ConstantValue.event_photo_save);
            } else {
                Toasty.error(TakePhotoActivity1.this, "???????????????", Toast.LENGTH_SHORT, true).show();
            }

        } else {
            Toasty.warning(TakePhotoActivity1.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
        }
    }

    private TagViewGroup.OnTagGroupClickListener mTagGroupClickListener = new TagViewGroup.OnTagGroupClickListener() {
        @Override
        public void onCircleClick(TagViewGroup container) {
//            Toast.makeText(ComponentManageActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTagClick(TagViewGroup container, ITagView tag, int position) {
//            Toast.makeText(TakePhotoActivity1.this, "??????Tag->" + ((TagTextView) tag).getText().toString(), Toast.LENGTH_SHORT).show();
//            deletePos = position;
            String tagStr = ((TagTextView) tag).getText().toString();
            if (tagGroupList != null && tagGroupList.size() > 0) {
                for (int i = 0; i < tagGroupList.size(); i++) {
                    if (tagStr.equals(tagGroupList.get(i).getTags().get(0).name)) {
                        deletePos = i;
                        break;
                    }
                }
            }
            L.i("zzz1--deletePos->" + deletePos);
            showDeletePointDialog();
        }

        @Override
        public void onLongPress(final TagViewGroup group) {
//            Toast.makeText(ComponentManageActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
        }
    };

    private TagViewGroup.OnTagGroupDragListener mTagGroupDragListener = new TagViewGroup.OnTagGroupDragListener() {
        @Override
        public void onDrag(TagViewGroup container, float percentX, float percentY, float percentX1, float percentY1) {
//            L.i("zzz1--Drag>" + "-percentX-" + percentX + "-percentY-" + percentY +"-percentX1-" + percentX1 + "-percentY1-" + percentY1);
            tagImageView.onDrag(container, percentX, percentY, percentX1, percentY1);

//            L.i("zzz1--Drag111>" + "--" + tagImageView.getTagGroupModels().get(0).getPercentX() + "--" + tagImageView.getTagGroupModels().get(0).getPercentY());
        }
    };


//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()) {
//            /**
//             * ?????????????????????
//             */
//            case MotionEvent.ACTION_DOWN:
////                test.setText("???????????????(" + event.getX() + "," + event.getY());
//                L.i("zzz1--????????????->" + event.getX() + "--getY->" + event.getY());
//                break;
//            /**
//             * ??????????????????
//             */
//            case MotionEvent.ACTION_MOVE:
//                L.i("zzz1--????????????->" + event.getX() + "--getY->" + event.getY());
////                test.setText("???????????????(" + event.getX() + "," + event.getY());
//                break;
//            /**
//             * ?????????????????????
//             */
//            case MotionEvent.ACTION_UP:
//                float getX = (float) event.getX();
//                float getY = (float) event.getY();
//                L.i("zzz1--????????????->" + getX + "--getY->" + getY);
//                if (!StringUtils.isNullOrEmpty(compressPath)) {
//                    showCheckDialog(getX, getY);
//                } else {
//                    Toasty.warning(TakePhotoActivity1.this, "???????????????", Toast.LENGTH_SHORT, true).show();
//                }
//                break;
//            default:
//                break;
//        }
//        /**
//         * ???????????????
//         * true???view????????????Touch?????????
//         * false???view????????????Touch????????????????????????false?????????????????????????????????????????????????????????????????????
//         */
//        return true;
//    }


    private void showCheckDialog(float getX, float getY, boolean isEdit) {
        checkDialog2 = new SelectDialog1(this);
        checkDialog2.setOnSelectClickListener(new SelectDialog1.OnSelectClickListener() {
            @Override
            public void onSelectClick(String type, String heatPre, String size, String count) {
                componentType = type;
//                L.i("zzz1--type->" + type + "--heatPre--" + heatPre + "--size->" + size);
//                L.i("zzz1--getX->" + getX + "--getY->" + getY);
//                L.i("zzz1--getscreenWidth->" + screenWidth + "--screenHeight->" + screenHeight);
                if (isEdit) {
                    PointBean1 pointBean = pointBeanList.get(deletePos);
                    pointBean.setComponentType(type);
                    pointBean.setHeatPreser(heatPre);
                    pointBean.setComponentSize(Integer.parseInt(size));
                    clearLastest();
                    tagGroupList.get(deletePos).setLastest(true);
                    tagImageView.setTagList(tagGroupList);
                } else {
                    float x = getX / ((float) screenWidth);
                    float y = getY / ((float) screenHeight);
//                L.i("zzz1--x->" + x + "--y->" + y);
                    PointBean1 pointBean = new PointBean1();
//                pointBean.setX(x);
//                pointBean.setY(y);
                    pointBean.setExtNum("" + index);
                    pointBean.setComponentType(type);
                    pointBean.setHeatPreser(heatPre);
                    pointBean.setComponentSize(Integer.parseInt(size));
                    pointBeanList.add(pointBean);
                    // ?????????

//                    test.setText("???????????????(" + event.getX() + "," + event.getY());
                    TagGroupModel tagGroupModel = new TagGroupModel();
                    List<TagGroupModel.Tag> tagList = new ArrayList<>();
                    TagGroupModel.Tag tag = new TagGroupModel.Tag();
                    tag.setName(type + "00" + index);
                    tag.setDirection(DirectionUtils.getValue(DIRECTION.RIGHT_TOP_STRAIGHT));
                    tagList.add(tag);
                    tagGroupModel.setTags(tagList);
                    tagGroupModel.setPercentX(x);
                    tagGroupModel.setPercentY(y);
                    tagGroupModel.setPercentX1(x);
                    tagGroupModel.setPercentY1(y);
                    clearLastest();
                    tagGroupModel.setLastest(true);

//                tagImageView.setTag(tagGroupModel);
                    tagGroupList.add(tagGroupModel);
                    L.i("zzz1--tagGroupList.size->" + tagGroupList.size());

                    tagImageView.setTagList(tagGroupList);
                    index++;
                }
                checkDialog2.dismiss();

            }
        });

        checkDialog2.show();
    }

    private void clearLastest() {
        if (tagGroupList != null && tagGroupList.size() > 0) {
            for (int i = 0; i < tagGroupList.size(); i++) {
                tagGroupList.get(i).setLastest(false);
            }
        }
    }


    private void takePhoto() {
        // ????????????
        PictureSelector.create(TakePhotoActivity1.this)
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
                .renameCompressFile(fileName)// ??????????????????????????? ????????????????????????????????????????????????????????????
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
                .forResult(new TakePhotoActivity1.MyResultCallback());
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
                setImg(compressPath);
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

    private void setImg(String compressPath) {
        if (!StringUtils.isNullOrEmpty(compressPath)) {
            tagImageView.setImageUrl(compressPath);

//        tagImageView.setImageRes(R.mipmap.ic_test1);
        }

        pointBeanList.clear();
        tagGroupList.clear();
        tagImageView.setTagList(tagGroupList);
    }

    /**
     * ????????????????????????????????????AndroidQToPath??????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private void clearCache() {
        // ?????????????????????????????????????????????????????? ??????:????????????????????????????????? ?????????????????????
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //PictureCacheManager.deleteCacheDirFile(this, PictureMimeType.ofImage());
            PictureCacheManager.deleteAllCacheDirRefreshFile(getContext());
        } else {
            PermissionChecker.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        save();
        finish();
//        super.onBackPressed();
    }

}