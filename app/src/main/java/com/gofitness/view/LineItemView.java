package com.gofitness.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.gofitness.R;

public class LineItemView extends LinearLayout {

    ImageView imageView;
    TextView leftTextView;
    TextView rightTextView;

    public LineItemView(Context context) {
        super(context);
        init(context, null);
    }

    public LineItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LineItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    TypedArray mTypedArray;

    private void init(Context context, AttributeSet attrs) {
        // 绑定属性 绑定布局
        mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.LineItemView);
        LayoutInflater.from(context).inflate(R.layout.view_line_item, this);

        // 获取各项属性
        String leftText = mTypedArray.getString(R.styleable.LineItemView_itemLeftText);
        String rightText = mTypedArray.getString(R.styleable.LineItemView_itemRightText);
        Drawable itemImage = mTypedArray.getDrawable(R.styleable.LineItemView_itemImage);
        Boolean isTop = mTypedArray.getBoolean(R.styleable.LineItemView_itemIsTop, false);
        Boolean isBottom = mTypedArray.getBoolean(R.styleable.LineItemView_itemIsBottom, false);

        // 初始化view
        imageView = findViewById(R.id.view_image);
        leftTextView = findViewById(R.id.view_left_text);
        rightTextView = findViewById(R.id.view_right_text);

        // 设置各项属性
        setLeftText(leftText);
        setRightText(rightText);
        setItemImage(itemImage);
        isTop(isTop);
        isBottom(isBottom);

        mTypedArray.recycle();
    }

    /**
     * 设置top view
     *
     * @param isTop
     */
    public LineItemView isTop(Boolean isTop) {
        if (isTop) {
            findViewById(R.id.view_top_line).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.view_top_line).setVisibility(INVISIBLE);
        }
        return this;
    }

    /**
     * 设置bottom view
     */
    public LineItemView isBottom(Boolean isBottom) {
        if (isBottom) {
            findViewById(R.id.view_bottom_line).setVisibility(VISIBLE);
            findViewById(R.id.view_bottom_line_left_padding).setVisibility(INVISIBLE);
        } else {
            findViewById(R.id.view_bottom_line).setVisibility(INVISIBLE);
            findViewById(R.id.view_bottom_line_left_padding).setVisibility(VISIBLE);
        }
        return this;
    }

    /**
     * 设置左边文字
     */
    public LineItemView setLeftText(String leftText) {
        if (!TextUtils.isEmpty(leftText))
            leftTextView.setText(leftText);
        return this;
    }

    /**
     * 设置右边文字
     */
    public LineItemView setRightText(String rightText) {
        if (!TextUtils.isEmpty(rightText))
            rightTextView.setText(rightText);
        return this;
    }

    /**
     * 设置左侧图像
     */
    public LineItemView setItemImage(Object imageSrc) {
        if (imageSrc != null) {
            imageView.setVisibility(VISIBLE);
            Glide.with(imageView).asBitmap().load(imageSrc)
                    .placeholder(R.drawable.ic_transparency).into(imageView);
        }
        return this;
    }

    /**
     * 设置左侧图像
     */
    public LineItemView setItemImageForDrawableRes(String imageSrc) {
        int imageId = getResources().getIdentifier(imageSrc, "drawable", getContext().getPackageName());
        imageView.setVisibility(VISIBLE);
        Glide.with(imageView).load(imageId)
                .placeholder(R.drawable.ic_transparency).into(imageView);
        return this;
    }

    /**
     * set view tag
     */
    public LineItemView setViewTag(String tag) {
        super.setTag(tag);
        return this;
    }

    /**
     * 设置背景颜色
     */
    public LineItemView setViewBackgroundColor(@ColorRes int color) {
        findViewById(R.id.root_view).setBackgroundResource(color);
        return this;
    }

    /**
     * 设置背景颜色
     */
    public LineItemView setViewLayoutHeight(@DimenRes int dimen) {
        ViewGroup.LayoutParams layoutParams = findViewById(R.id.root_view).getLayoutParams();
        layoutParams.height = (int) getResources().getDimension(dimen);
        findViewById(R.id.root_view).setLayoutParams(layoutParams);
        return this;
    }

    /**
     * 设置左边文字颜色
     */
    public LineItemView setLeftTextColor(@ColorRes int color) {
        leftTextView.setTextColor(getContext().getColor(color));
        return this;
    }

    /**
     * 设置左边文字大小
     */
    public LineItemView setLeftTextSize(@DimenRes int size) {
        leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(size));
        return this;
    }

}
