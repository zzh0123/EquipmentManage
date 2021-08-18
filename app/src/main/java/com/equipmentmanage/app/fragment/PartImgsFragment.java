package com.equipmentmanage.app.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.LazyFragment;
import com.equipmentmanage.app.bean.TagGroupModel;
import com.equipmentmanage.app.utils.DirectionUtils;
import com.equipmentmanage.app.view.TagImageView;
import com.licrafter.tagview.DIRECTION;
import com.licrafter.tagview.TagAdapter;
import com.licrafter.tagview.TagViewGroup;
import com.licrafter.tagview.views.ITagView;
import com.licrafter.tagview.views.TagTextView;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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

    public PartImgsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
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

        tagImageView.setImageRes(R.mipmap.ic_test1);
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

}