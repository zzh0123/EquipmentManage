package com.equipmentmanage.app.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.GridImageAdapter;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseAreaBean;
import com.equipmentmanage.app.bean.ImgTableBean;
import com.equipmentmanage.app.bean.PointBean;
import com.equipmentmanage.app.bean.TagGroupModel;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.utils.DateUtil;
import com.equipmentmanage.app.utils.DirectionUtils;
import com.equipmentmanage.app.utils.DpUtil;
import com.equipmentmanage.app.utils.GlideCacheEngine;
import com.equipmentmanage.app.utils.GlideEngine;
import com.equipmentmanage.app.utils.GsonUtils;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.view.SelectDialog;
import com.equipmentmanage.app.view.TagImageView;
import com.licrafter.tagview.DIRECTION;
import com.licrafter.tagview.TagViewGroup;
import com.licrafter.tagview.views.ITagView;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static com.equipmentmanage.app.base.MyApplication.getContext;

/**
 * @Description: 组件管理 -- 建档 -- 拍照
 * @Author: zzh
 * @CreateDate: 2021/10/30
 */
public class TakePhotoActivity extends BaseActivity implements View.OnTouchListener {

    //
    //                        et_manufacturer.getText().toString().trim(), tv_production_date.getText().toString().trim(),
    //                        tv_unreachable.getText().toString().trim(), tv_heat_preservation.getText().toString().trim(),
    //                        et_oper_temper.getText().toString().trim(), et_oper_pressure.getText().toString().trim(),
    //                        et_barcode.getText().toString().trim(), et_detection_freq.getText().toString().trim(),
    //                        et_detection_freq.getText().toString().trim(), et_leakage_threshold.getText().toString().trim());
    public static void open(Context c, String deviceTypeValue, String areaTypeValue, String equipmentTypeValue,
                            String mediumState, String streamTypeValue, String chemicalName, String chemicalTypeValue,String directionName,
                            String tagNum, String reference, String distance, String height,
                            String floorLevel, String size,
                            String manufacturer, String productionDate,
                            String unreachable, String unreachableReason, String heatPreservation,
                            String operTemper, String operPressure,
                            String barcode) {
        Intent i = new Intent(c, TakePhotoActivity.class);
        i.putExtra(Constant.deviceTypeValue, deviceTypeValue);
        i.putExtra(Constant.areaTypeValue, areaTypeValue);
        i.putExtra(Constant.equipmentTypeValue, equipmentTypeValue);
        i.putExtra(Constant.mediumState, mediumState);
        i.putExtra(Constant.streamTypeValue, streamTypeValue);
        i.putExtra(Constant.chemicalName, chemicalName);
        i.putExtra(Constant.chemicalTypeValue, chemicalTypeValue);
        i.putExtra(Constant.directionName, directionName);

        i.putExtra(Constant.tagNum, tagNum);
        i.putExtra(Constant.reference, reference);
        i.putExtra(Constant.distance, distance);
        i.putExtra(Constant.height, height);
        i.putExtra(Constant.floorLevel, floorLevel);

        i.putExtra(Constant.size, size);
        i.putExtra(Constant.manufacturer, manufacturer);
        i.putExtra(Constant.productionDate, productionDate);
        i.putExtra(Constant.unreachable, unreachable);
        i.putExtra(Constant.unreachableReason, unreachableReason);
        i.putExtra(Constant.heatPreservation, heatPreservation);

        i.putExtra(Constant.operTemper, operTemper);
        i.putExtra(Constant.operPressure, operPressure);
        i.putExtra(Constant.barcode, barcode);
//        i.putExtra(Constant.detectionFreq, detectionFreq);
//        i.putExtra(Constant.leakageThreshold, leakageThreshold);
        c.startActivity(i);
    }

    private String deviceTypeValue, areaTypeValue, equipmentTypeValue,
            mediumState, streamTypeValue, chemicalName, chemicalTypeValue, directionName;

    private String tagNum, reference, distance, height, floorLevel,
            size, manufacturer, productionDate, unreachable, unreachableReason, heatPreservation,
            operTemper, operPressure, barcode, detectionFreq,leakageThreshold;

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.sb_recall)
    SuperButton sb_recall; //撤回

    @BindView(R.id.sb_save)
    SuperButton sb_save; //保存


    @BindView(R.id.tagImageView)
    TagImageView tagImageView; //组件图片

    List<TagGroupModel> tagGroupList = new ArrayList<>();

    // compress path
    private String compressPath, fileName;

    private String componentType;

    private SelectDialog checkDialog2;
    private int index = 0;

    private DisplayMetrics dm;
    int screenWidth, screenHeight;

    List<PointBean> pointBeanList = new ArrayList<>();

    private ActivityResultLauncher<Intent> launcherResult;

    // 拍照配置
    private PictureWindowAnimationStyle mWindowAnimationStyle = PictureWindowAnimationStyle.ofDefaultWindowAnimationStyle();
    private int maxSelectNum = 1;
    private int aspect_ratio_x = 16, aspect_ratio_y = 9;

    private final static String TAG = "zzz1-->";

    private String currentDate;

    @Override
    protected int initLayout() {
        return R.layout.activity_take_photo;
    }

    @Override
    protected void initView() {
        currentDate = DateUtil.getCurentTime1(); // 2021-11-20
        deviceTypeValue = getIntent().getStringExtra(Constant.deviceTypeValue);
        areaTypeValue = getIntent().getStringExtra(Constant.areaTypeValue);
        equipmentTypeValue = getIntent().getStringExtra(Constant.equipmentTypeValue);
        mediumState = getIntent().getStringExtra(Constant.mediumState);
        streamTypeValue = getIntent().getStringExtra(Constant.streamTypeValue);
        chemicalName = getIntent().getStringExtra(Constant.chemicalName);
        chemicalTypeValue = getIntent().getStringExtra(Constant.chemicalTypeValue);
        directionName = getIntent().getStringExtra(Constant.directionName);

        tagNum = getIntent().getStringExtra(Constant.tagNum);
        reference = getIntent().getStringExtra(Constant.reference);
        distance = getIntent().getStringExtra(Constant.distance);
        height = getIntent().getStringExtra(Constant.height);
        floorLevel = getIntent().getStringExtra(Constant.floorLevel);

        size = getIntent().getStringExtra(Constant.size);
        manufacturer = getIntent().getStringExtra(Constant.manufacturer);
        productionDate = getIntent().getStringExtra(Constant.productionDate);
        unreachable = getIntent().getStringExtra(Constant.unreachable);
        unreachableReason = getIntent().getStringExtra(Constant.unreachableReason);
        heatPreservation = getIntent().getStringExtra(Constant.heatPreservation);

        operTemper = getIntent().getStringExtra(Constant.operTemper);
        operPressure = getIntent().getStringExtra(Constant.operPressure);
        barcode = getIntent().getStringExtra(Constant.barcode);
//        detectionFreq = getIntent().getStringExtra(Constant.detectionFreq);
//        leakageThreshold = getIntent().getStringExtra(Constant.leakageThreshold);
        detectionFreq = "";
        leakageThreshold = "";

        screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
        screenHeight = getWindowManager().getDefaultDisplay().getHeight() - DpUtil.dip2px(this, 45); // 屏幕高（像素，如：800p）
//        screenHeight = screenHeight/2;
//        screenHeight = (int) (screenHeight - titleBar.getY()
//                        - llType.getY()- llType.getY() -rvList.getY() -test.getY());
        L.i(" zzz1-wh->", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);

//        dm = new DisplayMetrics();
//        dm = getResources().getDisplayMetrics();
//        float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
//        int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
//        float xdpi = dm.xdpi;
//        float ydpi = dm.ydpi;
//        Log.e(TAG + " DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);
//        Log.e(TAG + " DisplayMetrics", "density=" + density + "; densityDPI=" + densityDPI);
//        screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
//        screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）
//        L.i(" zzz1-wh->", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);

        // 注册需要写在onCreate或Fragment onAttach里，否则会报java.lang.IllegalStateException异常
        launcherResult = createActivityResultLauncher();

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
                return R.mipmap.ic_photo;
            }

            @Override
            public void performAction(View view) {
                L.i("zzz1--->takephoto");
//                refresh();
                takePhoto();
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
    }

    @Override
    protected void initEvent() {
        tagImageView.setOnTouchListener(this);
        tagImageView.setTagGroupClickListener(mTagGroupClickListener);
    }

    @Override
    protected void initData() {
//        refresh();
    }

    @OnClick({R.id.sb_save, R.id.sb_recall})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sb_save:
                save();
                break;

            case R.id.sb_recall:
                if (tagGroupList != null && tagGroupList.size() > 0){
                    index--;
                    tagGroupList.remove(index);
                    tagImageView.setTagList(tagGroupList);
                } else {
                    Toasty.warning(TakePhotoActivity.this, "没有可撤回的点了！", Toast.LENGTH_SHORT, true).show();
                }
                break;

        }
    }

    private void save() {
        if (StringUtils.isNullOrEmpty(deviceTypeValue) || StringUtils.isNullOrEmpty(areaTypeValue)
                || StringUtils.isNullOrEmpty(equipmentTypeValue)) {
            Toasty.warning(TakePhotoActivity.this, "请先选择装置、区域、设备！", Toast.LENGTH_SHORT, true).show();
            return;
        }

        if (!StringUtils.isNullOrEmpty(compressPath)) {
            ImgTableBean imgTableBean = new ImgTableBean();
            imgTableBean.fileName = fileName;
            imgTableBean.localPath = compressPath;
            imgTableBean.content = GsonUtils.toJson(pointBeanList);
            imgTableBean.deviceId = deviceTypeValue;
            imgTableBean.areaId = areaTypeValue;
            imgTableBean.equipmentId = equipmentTypeValue;
            imgTableBean.mediumState = mediumState;
            imgTableBean.prodStream = streamTypeValue;
            imgTableBean.componentType = componentType;
            imgTableBean.chemicalName = chemicalName;
            imgTableBean.chemicalTypeValue = chemicalTypeValue;
            imgTableBean.directionName = directionName;

            imgTableBean.tagNum = tagNum;
            imgTableBean.reference = reference;
            imgTableBean.distance = distance;
            imgTableBean.height = height;
            imgTableBean.floorLevel = floorLevel;

            imgTableBean.size = size;
            imgTableBean.manufacturer = manufacturer;
            imgTableBean.productionDate = productionDate;
            imgTableBean.unreachable = unreachable;
            imgTableBean.unreachableReason = unreachableReason;
            imgTableBean.heatPreservation = heatPreservation;

            imgTableBean.operTemper = operTemper;
            imgTableBean.operPressure = operPressure;
            imgTableBean.barcode = barcode;
            imgTableBean.detectionFreq = detectionFreq;
            imgTableBean.leakageThreshold = leakageThreshold;
            imgTableBean.currentDate = currentDate;
            Long rowId = AppDatabase.getInstance(TakePhotoActivity.this).imgTableDao().insert(imgTableBean);
            if (rowId != null) {
                Toasty.success(TakePhotoActivity.this, "保存成功！", Toast.LENGTH_SHORT, true).show();
                finish();
            } else {
                Toasty.error(TakePhotoActivity.this, "保存失败！", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            Toasty.warning(TakePhotoActivity.this, "图片路径为空！", Toast.LENGTH_SHORT, true).show();
        }
    }

    private TagViewGroup.OnTagGroupClickListener mTagGroupClickListener = new TagViewGroup.OnTagGroupClickListener() {
        @Override
        public void onCircleClick(TagViewGroup container) {
//            Toast.makeText(ComponentManageActivity.this, "点击中心圆", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTagClick(TagViewGroup container, ITagView tag, int position) {
//            Toast.makeText(ComponentManageActivity.this, "点击Tag->" + ((TagTextView) tag).getText().toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLongPress(final TagViewGroup group) {
//            Toast.makeText(ComponentManageActivity.this, "点击中心圆", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            /**
             * 点击的开始位置
             */
            case MotionEvent.ACTION_DOWN:
//                test.setText("起始位置：(" + event.getX() + "," + event.getY());
                L.i("zzz1--起始位置->" + event.getX() + "--getY->" + event.getY());
                break;
            /**
             * 触屏实时位置
             */
            case MotionEvent.ACTION_MOVE:
                L.i("zzz1--实时位置->" + event.getX() + "--getY->" + event.getY());
//                test.setText("实时位置：(" + event.getX() + "," + event.getY());
                break;
            /**
             * 离开屏幕的位置
             */
            case MotionEvent.ACTION_UP:
                float getX = (float) event.getX();
                float getY = (float) event.getY();
                L.i("zzz1--离开位置->" + getX + "--getY->" + getY);
                if (!StringUtils.isNullOrEmpty(compressPath)) {
                    showCheckDialog(getX, getY);
                } else {
                    Toasty.warning(TakePhotoActivity.this, "请先拍照！", Toast.LENGTH_SHORT, true).show();
                }
                break;
            default:
                break;
        }
        /**
         * 注意返回值
         * true：view继续响应Touch操作；
         * false：view不再响应Touch操作，故此处若为false，只能显示起始位置，不能显示实时位置和结束位置
         */
        return true;
    }


    private void showCheckDialog(float getX, float getY) {
        checkDialog2 = new SelectDialog(this);
        checkDialog2.setOnSelectClickListener(new SelectDialog.OnSelectClickListener() {
            @Override
            public void onSelectClick(String s) {
                if (!StringUtils.isNullOrEmpty(s)) {
                    componentType = s;
                    L.i("zzz1--getX->" + getX + "--getY->" + getY);
                    L.i("zzz1--getscreenWidth->" + screenWidth + "--screenHeight->" + screenHeight);
                    float x = getX / ((float) screenWidth);
                    float y = getY / ((float) screenHeight);
                    L.i("zzz1--x->" + x + "--y->" + y);
                    PointBean pointBean = new PointBean();
                    pointBean.setX(x);
                    pointBean.setY(y);
                    pointBean.setExtNum("" + index);
                    pointBeanList.add(pointBean);
                    // 存储点

//                    test.setText("结束位置：(" + event.getX() + "," + event.getY());
                    TagGroupModel tagGroupModel = new TagGroupModel();
                    List<TagGroupModel.Tag> tagList = new ArrayList<>();
                    TagGroupModel.Tag tag = new TagGroupModel.Tag();
                    tag.setName(s + "00" + index);
                    tag.setDirection(DirectionUtils.getValue(DIRECTION.RIGHT_TOP_STRAIGHT));
                    tagList.add(tag);
                    tagGroupModel.setTags(tagList);
                    tagGroupModel.setPercentX(x);
                    tagGroupModel.setPercentY(y);

//                tagImageView.setTag(tagGroupModel);
                    tagGroupList.add(tagGroupModel);
                    tagImageView.setTagList(tagGroupList);
                    index++;
                    checkDialog2.dismiss();
                } else {

                }
            }
        });

        checkDialog2.show();
    }


    private void takePhoto() {
        // 单独拍照
        PictureSelector.create(TakePhotoActivity.this)
                .openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
//                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
//                .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                .setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .isUseCustomCamera(true)// 是否使用自定义相机
                //.setOutputCameraPath()// 自定义相机输出目录
                .minSelectNum(1)// 最小选择数量
                //.querySpecifiedFormatSuffix(PictureMimeType.ofPNG())// 查询指定后缀格式资源
                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高，默认为true
                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 如果视频有旋转角度则对换宽高，默认false
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                //.cameraFileName(System.currentTimeMillis() + ".jpg")// 使用相机时保存至本地的文件名称,注意这个只在拍照时可以使用
                //.renameCompressFile(System.currentTimeMillis() + ".jpg")// 重命名压缩文件名、 注意这个不要重复，只适用于单张图压缩使用
                //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 注意这个不要重复，只适用于单张图裁剪使用
                .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                .isPreviewImage(true)// 是否可预览图片
                .isPreviewVideo(false)// 是否可预览视频
                .isEnablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isAndroidQTransform(true)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                .isEnableCrop(false)// 是否裁剪
                //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                .isCompress(true)// 是否压缩
                .compressQuality(60)// 图片压缩后输出质量
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .freeStyleCropMode(OverlayView.DEFAULT_FREESTYLE_CROP_MODE)// 裁剪框拖动模式
                .circleDimmedLayer(false)// 是否圆形裁剪
                //.setCircleDimmedColor(ContextCompat.getColor(this, R.color.app_color_white))// 设置圆形裁剪背景色值
                //.setCircleDimmedBorderColor(ContextCompat.getColor(this, R.color.app_color_white))// 设置圆形裁剪边框色值
                //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isOpenClickSound(true)// 是否开启点击声音
//                .selectionData(mAdapter.getData())// 是否传入已选图片
                .isAutoScalePreviewImage(true)// 如果图片宽度不能充满屏幕则自动处理成充满模式
                //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 废弃 改用cutOutQuality()
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled() // 裁剪是否可旋转图片
                //.scaleEnabled()// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
//                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                .forResult(new TakePhotoActivity.MyResultCallback());
    }

    /**
     * 创建一个ActivityResultLauncher
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
                            // 例如 LocalMedia 里面返回五种path
                            // 1.media.getPath(); 原图path
                            // 2.media.getCutPath();裁剪后path，需判断media.isCut();切勿直接使用
                            // 3.media.getCompressPath();压缩后path，需判断media.isCompressed();切勿直接使用
                            // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                            // 5.media.getAndroidQToPath();Android Q版本特有返回的字段，但如果开启了压缩或裁剪还是取裁剪或压缩路径；注意：.isAndroidQTransform 为false 此字段将返回空
                            // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
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
                                Log.i(TAG, "是否压缩:" + media.isCompressed());
                                Log.i(TAG, "压缩:" + media.getCompressPath());
                                Log.i(TAG, "原图:" + media.getPath());
                                Log.i(TAG, "绝对路径:" + media.getRealPath());
                                Log.i(TAG, "是否裁剪:" + media.isCut());
                                Log.i(TAG, "裁剪:" + media.getCutPath());
                                Log.i(TAG, "是否开启原图:" + media.isOriginal());
                                Log.i(TAG, "原图路径:" + media.getOriginalPath());
                                Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());
                                Log.i(TAG, "宽高: " + media.getWidth() + "x" + media.getHeight());
                                Log.i(TAG, "Size: " + media.getSize());

                                // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
                            }
//                            mAdapter.setList(selectList);
//                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 返回结果回调
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
                Log.i(TAG, "文件名: " + media.getFileName());
                Log.i(TAG, "是否压缩:" + media.isCompressed());
                Log.i(TAG, "压缩:" + media.getCompressPath());
                Log.i(TAG, "原图:" + media.getPath());
                Log.i(TAG, "绝对路径:" + media.getRealPath());
                Log.i(TAG, "是否裁剪:" + media.isCut());
                Log.i(TAG, "裁剪:" + media.getCutPath());
                Log.i(TAG, "是否开启原图:" + media.isOriginal());
                Log.i(TAG, "原图路径:" + media.getOriginalPath());
                Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());
                Log.i(TAG, "宽高: " + media.getWidth() + "x" + media.getHeight());
                Log.i(TAG, "Size: " + media.getSize());

                Log.i(TAG, "onResult: " + media.toString());

                // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息

            }

            LocalMedia media = result.get(0);
            if (media != null) {
                fileName = media.getFileName();
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
    }

    /**
     * 清空缓存包括裁剪、压缩、AndroidQToPath所生成的文件，注意调用时机必须是处理完本身的业务逻辑后调用；非强制性
     */
    private void clearCache() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //PictureCacheManager.deleteCacheDirFile(this, PictureMimeType.ofImage());
            PictureCacheManager.deleteAllCacheDirRefreshFile(getContext());
        } else {
            PermissionChecker.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
        }
    }

}