/**
 * Copyright (C) 2013 The  IMN Android Project 
 */
package com.findingfriends.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

import com.example.findingfriends.R;
import com.findingfriends.interfaces.ContactAttachDetachListner;
import com.findingfriends.models.ContactModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



/**
 * <h1>CustomLayout.java</h1>
 * <p>
 * Custom Layout manager. Inserts every child horizontally till it exceeds
 * parent container width ,inserts to new row otherwise
 * </p>
 * 
 */
@RemoteViews.RemoteView
public class RecipientView extends ViewGroup {
	/** The amount of space used by children in the left gutter. */
	private int mLeftWidth;

	/** The amount of space used by children in the right gutter. */
	private int mRightWidth;

	/** These are used for computing child frames based on their gravity. */
	private final Rect mTmpContainerRect = new Rect();
	private final Rect mTmpChildRect = new Rect();
	private HashMap<ContactModel, View> mContactChildPosition = new HashMap<ContactModel, View>();
	private EditText mEditText;
	private Context mContext;
	private ContactAttachDetachListner mAddRemovedListner;

	public RecipientView(Context context) {
		super(context);
		init(context);
	}

	public RecipientView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RecipientView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		setVerticalScrollBarEnabled(true);
	}

	public void setEditText(EditText editText) {
		this.mEditText = editText;
	}

	public void setTextWatcher(TextWatcher watcher) {
		mEditText.addTextChangedListener(watcher);
	}

	public String getTextOnEditText() {
		return mEditText.getText().toString();
	}

	public void addRemovedListner(ContactAttachDetachListner mListner) {
		this.mAddRemovedListner = mListner;
	}

	public void addContact(List<ContactModel> contactsToAdd) {
		int totalChilds = getChildCount();
		for (int contactIndex = 0; contactIndex < contactsToAdd.size(); contactIndex++) {
			final ContactModel imnContact = contactsToAdd.get(contactIndex);
			Button btn = new Button(mContext);
			btn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			btn.setText(imnContact.getName());
			if (mAddRemovedListner != null) {
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View paramView) {
						mAddRemovedListner.contactRemoved(imnContact);
					}
				});
			}
			addView(btn, totalChilds + (contactIndex - 1));
			mContactChildPosition.put(imnContact, btn);
		}
	}

	public void addContact(final ContactModel contactToAdd) {
		Button btn = new Button(mContext);
		btn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		btn.setText(contactToAdd.getName());
		if (mAddRemovedListner != null) {
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View paramView) {
					mAddRemovedListner.contactRemoved(contactToAdd);
				}
			});
		}
		addView(btn, getChildCount() - 1);
		mContactChildPosition.put(contactToAdd, btn);
	}

	public void removeContact(ContactModel contactToRemove) {
		removeView(mContactChildPosition.get(contactToRemove));
		mContactChildPosition.remove(contactToRemove);
	}

	public List<ContactModel> getSelectedContact() {
		List<ContactModel> selectedContact = new ArrayList<ContactModel>();
		Set<Map.Entry<ContactModel, View>> entry = mContactChildPosition.entrySet();
		for (Map.Entry<ContactModel, View> mapEntry : entry) {
			selectedContact.add(mapEntry.getKey());
		}
		return selectedContact;
	}

	public List<String> getSelectedContactIds() {
		List<String> selectedContact = new ArrayList<String>();
		Set<Map.Entry<ContactModel, View>> entry = mContactChildPosition.entrySet();
		for (Map.Entry<ContactModel, View> mapEntry : entry) {
			// TODO remove comment from codes below
			selectedContact.add(mapEntry.getKey().getUser_id());
			//selectedContact.add(mapEntry.getKey().getName());
		}
		return selectedContact;
	}

	@Override
	public boolean shouldDelayChildPressedState() {
		return true;
	}

	/**
	 * Ask all children to measure themselves and compute the measurement of
	 * this layout based on the children.
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int count = getChildCount();

		// These keep track of the space we are using on the left and right for
		// views positioned there; we need member variables so we can also use
		// these for layout later.
		mLeftWidth = 0;
		mRightWidth = 0;

		// Measurement will ultimately be computing these values.
		int maxHeight = 0;
		int maxWidth = 0;
		int childState = 0;
		// Iterate through all children, measuring them and computing our
		// dimensions
		// from their size.
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				// Measure the child.
				measureChildWithMargins(child, widthMeasureSpec, 0,
						heightMeasureSpec, 0);
				// Update our size information based on the layout params.
				// Children
				// that asked to be positioned on the left or right go in those
				// gutters.
				final LayoutParams lp = (LayoutParams) child.getLayoutParams();
				if (lp.position == LayoutParams.POSITION_LEFT) {
					mLeftWidth += Math.max(maxWidth, child.getMeasuredWidth()
							+ lp.leftMargin + lp.rightMargin);
				} else if (lp.position == LayoutParams.POSITION_RIGHT) {
					mRightWidth += Math.max(maxWidth, child.getMeasuredWidth()
							+ lp.leftMargin + lp.rightMargin);
				} else {
					maxWidth = Math.max(maxWidth, child.getMeasuredWidth()
							+ lp.leftMargin + lp.rightMargin);
				}
				maxHeight = Math.max(maxHeight, child.getMeasuredHeight()
						+ lp.topMargin + lp.bottomMargin);
				childState = combineMeasuredStates(childState,
						child.getMeasuredState());
			}
		}
		// Total width is the maximum width of all inner children plus the
		// gutters.
		maxWidth += mLeftWidth + mRightWidth;

		// Check against our minimum height and width
		maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
		maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

		// Report our final dimensions.
		setMeasuredDimension(
				resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
				resolveSizeAndState(maxHeight, heightMeasureSpec, childState));
	}

	/**
	 * Position all children within this layout.
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		final int count = getChildCount();

		// These are the far left and right edges in which we are performing
		// layout.
		int leftPos = getPaddingLeft();
		// These are the top and bottom edges in which we are performing layout.
		final int parentTop = getPaddingTop();
		int parentHeight = 0;
		int existingChildrenWidth = 0;
		int existingChildrenHeight = 0;
		int widthAfterAdding = 0;
		int containerWidth = right;

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			final LayoutParams lp = (LayoutParams) child.getLayoutParams();
			final int width = child.getMeasuredWidth();
			final int height = child.getMeasuredHeight();
			if (child.getVisibility() != GONE) {
				widthAfterAdding = existingChildrenWidth + leftPos + width
						+ lp.leftMargin + lp.rightMargin;

				if ((containerWidth - widthAfterAdding) > 0) {
					// Show on the same Line
					mTmpContainerRect.left = existingChildrenWidth + leftPos
							+ lp.leftMargin;
					mTmpContainerRect.right = existingChildrenWidth + leftPos
							+ getPaddingRight() + width + lp.leftMargin
							+ lp.rightMargin;
					mTmpContainerRect.top = existingChildrenHeight + parentTop
							+ lp.topMargin;
					mTmpContainerRect.bottom = existingChildrenHeight + height
							+ parentTop - lp.bottomMargin;
					Gravity.apply(lp.gravity, width, height, mTmpContainerRect,
							mTmpChildRect);
					child.layout(mTmpChildRect.left, mTmpChildRect.top,
							mTmpChildRect.right, mTmpChildRect.bottom);
					existingChildrenWidth += getPaddingRight() + width
							+ lp.leftMargin + lp.rightMargin;
					parentHeight = existingChildrenHeight + height
							+ getPaddingTop() + top;
				} else {
					existingChildrenHeight += getChildAt(i - 1)
							.getMeasuredHeight();
					existingChildrenWidth = 0;
					// Go to NextLine
					mTmpContainerRect.left = existingChildrenWidth + leftPos
							+ lp.leftMargin;
					mTmpContainerRect.right = existingChildrenWidth + leftPos
							+ getPaddingRight() + width + lp.rightMargin;
					mTmpContainerRect.top = existingChildrenHeight + parentTop
							+ lp.topMargin;
					mTmpContainerRect.bottom = existingChildrenHeight + height
							+ parentTop - lp.bottomMargin;
					Gravity.apply(lp.gravity, width, height, mTmpContainerRect,
							mTmpChildRect);
					child.layout(mTmpChildRect.left, mTmpChildRect.top,
							mTmpChildRect.right, mTmpChildRect.bottom);
					existingChildrenWidth += width + lp.leftMargin
							+ lp.rightMargin;
					parentHeight = existingChildrenHeight + height
							+ getPaddingTop() + getPaddingBottom();
				}
			}
		}

		this.setMinimumHeight(parentHeight);

	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new RecipientView.LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(
			ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	/**
	 * Custom per-child layout information.
	 */
	public static class LayoutParams extends MarginLayoutParams {
		/**
		 * The gravity to apply with the View to which these layout parameters
		 * are associated.
		 */
		@SuppressLint("InlinedApi")
		public int gravity = Gravity.TOP | Gravity.START;

		public static int POSITION_MIDDLE = 0;
		public static int POSITION_LEFT = 1;
		public static int POSITION_RIGHT = 2;

		public int position = POSITION_MIDDLE;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);

			// Pull the layout param values from the layout XML during
			// inflation. This is not needed if you don't care about
			// changing the layout behavior in XML.
			TypedArray a = c.obtainStyledAttributes(attrs,
					R.styleable.CustomLayoutLP);
			gravity = a.getInt(
					R.styleable.CustomLayoutLP_android_layout_gravity, gravity);
			position = a.getInt(R.styleable.CustomLayoutLP_layout_position,
					position);
			a.recycle();
		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}
	}

}