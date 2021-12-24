package com.equipmentmanage.app.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.bean.TagGroupModel;
import com.equipmentmanage.app.utils.AnimatorUtils;
import com.equipmentmanage.app.utils.DirectionUtils;
import com.equipmentmanage.app.utils.L;
import com.licrafter.tagview.DIRECTION;
import com.licrafter.tagview.TagAdapter;
import com.licrafter.tagview.TagViewGroup;
import com.licrafter.tagview.views.ITagView;
import com.licrafter.tagview.views.RippleView;
import com.licrafter.tagview.views.TagTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * author: shell
 * date 2016/12/30 下午3:13
 **/
public class TagImageView extends FrameLayout {

    private ImageView mImageView;
    private FrameLayout mContentLayout;
    private List<TagGroupModel> mTagGroupModelList = new ArrayList<>();
    private List<TagViewGroup> mTagGroupViewList = new ArrayList<>();
    private TagViewGroup.OnTagGroupClickListener mClickListener;
    private TagViewGroup.OnTagGroupDragListener mDragListener;
    private boolean mIsEditMode;
    private int num;
    private Context mContext;

    public TagImageView(Context context) {
        this(context, null);
        mContext = context;
    }

    public TagImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public TagImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.layout_tag_imageview, this, true);
        mImageView = (ImageView) rootView.findViewById(R.id.imageview);
        mContentLayout = (FrameLayout) rootView.findViewById(R.id.tagsGroup);
    }

    public void setTagList(List<TagGroupModel> tagGroupList) {
        clearTags();
        for (TagGroupModel model : tagGroupList) {
            addTagGroup(model);
        }
    }

    public void setTag(TagGroupModel tagGroupModel) {
        clearTags();
        addTagGroup(tagGroupModel);
    }

    public void addTagGroup(TagGroupModel model) {
        mTagGroupModelList.add(model);
        TagViewGroup tagViewGroup = getTagViewGroup(model);
        tagViewGroup.setTagAlpha(10);
        tagViewGroup.setOnTagGroupClickListener(mClickListener);
        tagViewGroup.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContentLayout.addView(tagViewGroup);
        mTagGroupViewList.add(tagViewGroup);
    }

    public void clearTags() {
        mTagGroupModelList.clear();
        mContentLayout.removeAllViews();
        mTagGroupViewList.clear();
    }

    public TagViewGroup getTagViewGroup(final TagGroupModel model) {
        TagViewGroup tagViewGroup = new TagViewGroup(getContext());
        if (!mIsEditMode) {
            setTagGroupAnimation(tagViewGroup);
        } else {
            tagViewGroup.setOnTagGroupDragListener(mDragListener);
        }
        tagViewGroup.setTagAdapter(new TagAdapter() {
            @Override
            public int getCount() {
                return model.getTags().size();
//                return model.getTags().size() + 1;
            }

            @Override
            public boolean getData() {
                return model.isLastest();
            }

            @Override
            public ITagView getItem(int position) {
                L.i("zzz1--position->" + position + "--getTags.size--" + model.getTags().size());
                return makeTagTextView(model.getTags().get(position));
//                return makeRippleView();
//                if (position < model.getTags().size()) {
//                    return makeTagTextView(model.getTags().get(position));
//                } else {
//                    return makeRippleView();
//                }
            }
        });
        tagViewGroup.setPercent(model.getPercentX(), model.getPercentY(), model.getPercentX1(), model.getPercentY1());
        return tagViewGroup;
    }

    public RippleView makeRippleView() {
        RippleView rippleView = new RippleView(getContext());
        rippleView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rippleView.setDirection(DIRECTION.CENTER);
//        rippleView.setVisibility(GONE);
        return rippleView;
    }

    public TagTextView makeTagTextView(TagGroupModel.Tag tag) {
        TagTextView tagTextView = new TagTextView(getContext());
//        tagTextView.setTextColor(getResources().getColor(R.color.c_409EFF));
        tagTextView.setDirection(DirectionUtils.getDirection(tag.getDirection()));
        tagTextView.setBackgroundColor(getResources().getColor(R.color.c_409EFF));
        tagTextView.setText(tag.getName());
        return tagTextView;
    }

    public void setTagGroupAnimation(final TagViewGroup group) {
        Animator showAnimator = AnimatorUtils.getTagShowAnimator(group);
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                group.setVisibility(VISIBLE);
            }
        });
        Animator hideAnimator = AnimatorUtils.getTagHideAnimator(group);
        hideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                group.setVisibility(INVISIBLE);
            }
        });
        group.setHideAnimator(hideAnimator).setShowAnimator(showAnimator);
    }

    public void excuteTagsAnimation() {
        for (TagViewGroup tagViewGroup : mTagGroupViewList) {
            if (tagViewGroup.getVisibility() == INVISIBLE) {
                tagViewGroup.startShowAnimator();
            } else {
                tagViewGroup.startHideAnimator();
            }
        }
    }

    public List<TagGroupModel> getTagGroupModels() {
        return mTagGroupModelList;
    }

    public void onTagClicked(TagViewGroup group, ITagView tagView, int position) {
        mTagGroupModelList.get(mTagGroupViewList.indexOf(group)).getTags().get(position).setDirection(DirectionUtils.getValue(tagView.getDirection()) % 10 + 1);
        group.getTagAdapter().notifyDataSetChanged();
    }

    public void onDrag(TagViewGroup group, float percentX, float percentY, float percentX1, float percentY1) {
        mTagGroupModelList.get(mTagGroupViewList.indexOf(group)).setPercentX(percentX);
        mTagGroupModelList.get(mTagGroupViewList.indexOf(group)).setPercentY(percentY);
        mTagGroupModelList.get(mTagGroupViewList.indexOf(group)).setPercentX1(percentX1);
        mTagGroupModelList.get(mTagGroupViewList.indexOf(group)).setPercentY1(percentY1);
    }

    public void setEditMode(boolean editMode) {
        mIsEditMode = editMode;
    }

    public void setImageUrl(String url) {
        Glide.with(mImageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(mImageView);
    }

    public void setImageRes(int resourceId) {
        Glide.with(mImageView.getContext()).load(resourceId).into(mImageView);

    }

    public void removeTagGroup(TagViewGroup tagViewGroup) {
        mContentLayout.removeView(tagViewGroup);
    }

    public void setTagGroupClickListener(TagViewGroup.OnTagGroupClickListener tagGroupClickListener) {
        mClickListener = tagGroupClickListener;
    }

    public void setTagGroupScrollListener(TagViewGroup.OnTagGroupDragListener scrollListener) {
        mDragListener = scrollListener;
    }
}
