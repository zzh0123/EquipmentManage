package com.equipmentmanage.app.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.activity.LoginActivity;
import com.equipmentmanage.app.base.LazyFragment;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.LiveTaskAppAssignedPage;
import com.equipmentmanage.app.bean.LoginPostBean;
import com.equipmentmanage.app.bean.LoginResultBean;
import com.equipmentmanage.app.bean.TagGroupModel;
import com.equipmentmanage.app.bean.TaskBean;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.ActivityCollector;
import com.equipmentmanage.app.utils.DirectionUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.equipmentmanage.app.view.TagImageView;
import com.google.gson.reflect.TypeToken;
import com.licrafter.tagview.DIRECTION;
import com.licrafter.tagview.TagAdapter;
import com.licrafter.tagview.TagViewGroup;
import com.licrafter.tagview.views.ITagView;
import com.licrafter.tagview.views.TagTextView;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 组件图片Fragment
 * @Author: zzh
 * @CreateDate: 2021/8/17
 */
public class PartImgsFragment extends LazyFragment {

    @BindView(R.id.tagImageView)
    TagImageView tagImageView; //组件图片

    @BindView(R.id.read_value)
    TextView readValue; //读数

    @BindView(R.id.tv_reread)
    TextView tvReread; //重新读数

    @BindView(R.id.tv_last)
    TextView tvLast; //上一个

    @BindView(R.id.tv_next)
    TextView tvNext; //下一个

    @BindView(R.id.progress_bar)
    ProgressBar progressBar; //下一个

    List<TaskBean> records = new ArrayList<>();
    private TaskBean bean;

    public PartImgsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param
     * @param
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
        return R.layout.fragment_part_imgs;
    }

    @Override
    protected void initView(View view) {
        progressBar.setProgress(40);
    }

    @Override
    protected void initData() {
        tagImageView.setTagGroupClickListener(mTagGroupClickListener);

        Bundle bundle = getArguments();
        bean = (TaskBean) bundle.getSerializable(Constant.taskBean);

        // liveTaskAppPicPageList
        tagImageView.setImageUrl(bean.getLiveTaskAppPicPageList().get(0).getPictPath());

//        tagImageView.setImageRes(R.mipmap.ic_test1);
        TagGroupModel tagGroupModel = new TagGroupModel();
        List<TagGroupModel.Tag> tagList = new ArrayList<>();
        TagGroupModel.Tag tag = new TagGroupModel.Tag();
        tag.setName("V0001");
        tag.setDirection(DirectionUtils.getValue(DIRECTION.RIGHT_TOP_STRAIGHT));
        tagList.add(tag);
        tagGroupModel.setTags(tagList);
        tagGroupModel.setPercentX(0.4f);
        tagGroupModel.setPercentY(0.8f);

        tagImageView.setTag(tagGroupModel);
//        tagImageView.setTagList();


        //"检测组件")
        List<LiveTaskAppAssignedPage> liveTaskAppAssignedPageList = bean.getLiveTaskAppPicPageList().get(0).getLiveTaskAppAssignedPageList();
        for (int i = 0; i < liveTaskAppAssignedPageList.size(); i++){
            liveTaskAppAssignedPageList.get(i).setDetectionVal(BigDecimal.valueOf(i));
        }


        records.add(bean);


    }




    @Override
    protected void initEvent() {


    }

    @OnClick({R.id.tagImageView})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tagImageView:
//                if (valid()){
//                    login();
//                }
                accessResultList();
                break;
        }
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


    /**
     * 检查结果上传
     */
    private void accessResultList(){
//        loginPostBean.captcha ="";
//        loginPostBean.checkKey ="";
//        loginPostBean.setUsername(etAccount.getText().toString().trim());
//        loginPostBean.setPassword(etPassword.getText().toString().trim());

//        kv.removeValuesForKeys(new String[]{Constant.token, Constant.userId, Constant.username,
//                Constant.realname, Constant.orgCode, Constant.orgCodeTxt, Constant.telephone,
//                Constant.post});

        Subscribe.accessResultList(records, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    BaseBean<LoginResultBean> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<LoginResultBean>>() {
                    }.getType());

                    if (baseBean != null) {
                        if (baseBean.isSuccess()) {
                            Toasty.success(getActivity(), R.string.logout_success, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Toasty.error(getActivity(), R.string.logout_fail, Toast.LENGTH_SHORT, true).show();
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

}