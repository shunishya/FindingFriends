package com.findingfriends.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MaxHeightScrollView extends ScrollView {

	public final int MAX_HEIGHT = 200;

	public MaxHeightScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureHeight(int heightMeasureSpec) {
		int size = 0;
		if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
			size = getChildAt(0).getMeasuredHeight();
		} else {
			size = MeasureSpec.getSize(heightMeasureSpec);
		}
		return size > MAX_HEIGHT ? MAX_HEIGHT : size;
	}
}
