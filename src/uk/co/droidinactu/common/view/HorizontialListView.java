/*
 * HorizontalListView.java v1.5
 *
 * 
 * The MIT License
 * Copyright (c) 2011 Paul Soucy (paul@dev-smart.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package uk.co.droidinactu.common.view;

import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

public class HorizontialListView extends AdapterView<ListAdapter> {

    public boolean mAlwaysOverrideTouch = true;
    protected ListAdapter mAdapter;
    private int mLeftViewIndex = -1;
    private int mRightViewIndex = 0;
    protected int mCurrentX;
    protected int mNextX;
    private int mMaxX = Integer.MAX_VALUE;
    private int mDisplayOffset = 0;
    protected Scroller mScroller;
    private GestureDetector mGesture;
    private final Queue<View> mRemovedViewQueue = new LinkedList<View>();
    private OnItemSelectedListener mOnItemSelected;
    private OnItemClickListener mOnItemClicked;
    private OnItemLongClickListener mOnItemLongClicked;
    private boolean mDataChanged = false;

    private final DataSetObserver mDataObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            synchronized (HorizontialListView.this) {
                HorizontialListView.this.mDataChanged = true;
            }
            HorizontialListView.this.invalidate();
            HorizontialListView.this.requestLayout();
        }

        @Override
        public void onInvalidated() {
            HorizontialListView.this.reset();
            HorizontialListView.this.invalidate();
            HorizontialListView.this.requestLayout();
        }

    };

    private final OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(final MotionEvent e) {
            return HorizontialListView.this.onDown(e);
        }

        @Override
        public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
            return HorizontialListView.this.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {

            synchronized (HorizontialListView.this) {
                HorizontialListView.this.mNextX += (int) distanceX;
            }
            HorizontialListView.this.requestLayout();
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(final MotionEvent e) {
            final Rect viewRect = new Rect();
            for (int i = 0; i < HorizontialListView.this.getChildCount(); i++) {
                final View child = HorizontialListView.this.getChildAt(i);
                final int left = child.getLeft();
                final int right = child.getRight();
                final int top = child.getTop();
                final int bottom = child.getBottom();
                viewRect.set(left, top, right, bottom);
                if (viewRect.contains((int) e.getX(), (int) e.getY())) {
                    if (HorizontialListView.this.mOnItemClicked != null) {
                        HorizontialListView.this.mOnItemClicked.onItemClick(
                                HorizontialListView.this,
                                child,
                                HorizontialListView.this.mLeftViewIndex + 1 + i,
                                HorizontialListView.this.mAdapter.getItemId(HorizontialListView.this.mLeftViewIndex + 1
                                        + i));
                    }
                    if (HorizontialListView.this.mOnItemSelected != null) {
                        HorizontialListView.this.mOnItemSelected.onItemSelected(
                                HorizontialListView.this,
                                child,
                                HorizontialListView.this.mLeftViewIndex + 1 + i,
                                HorizontialListView.this.mAdapter.getItemId(HorizontialListView.this.mLeftViewIndex + 1
                                        + i));
                    }
                    break;
                }
            }
            return true;
        }
    };

    public HorizontialListView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    private void addAndMeasureChild(final View child, final int viewPos) {
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        }

        this.addViewInLayout(child, viewPos, params, true);
        child.measure(MeasureSpec.makeMeasureSpec(this.getWidth(), MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(this.getHeight(), MeasureSpec.AT_MOST));
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        final boolean handled = this.mGesture.onTouchEvent(ev);
        return handled;
    }

    private void fillList(final int dx) {
        int edge = 0;
        View child = this.getChildAt(this.getChildCount() - 1);
        if (child != null) {
            edge = child.getRight();
        }
        this.fillListRight(edge, dx);

        edge = 0;
        child = this.getChildAt(0);
        if (child != null) {
            edge = child.getLeft();
        }
        this.fillListLeft(edge, dx);

    }

    private void fillListLeft(int leftEdge, final int dx) {
        while (((leftEdge + dx) > 0) && (this.mLeftViewIndex >= 0)) {
            final View child = this.mAdapter.getView(this.mLeftViewIndex, this.mRemovedViewQueue.poll(), this);
            this.addAndMeasureChild(child, 0);
            leftEdge -= child.getMeasuredWidth();
            this.mLeftViewIndex--;
            this.mDisplayOffset -= child.getMeasuredWidth();
        }
    }

    private void fillListRight(int rightEdge, final int dx) {
        while (((rightEdge + dx) < this.getWidth()) && (this.mRightViewIndex < this.mAdapter.getCount())) {

            final View child = this.mAdapter.getView(this.mRightViewIndex, this.mRemovedViewQueue.poll(), this);
            this.addAndMeasureChild(child, -1);
            rightEdge += child.getMeasuredWidth();

            if (this.mRightViewIndex == (this.mAdapter.getCount() - 1)) {
                this.mMaxX = (this.mCurrentX + rightEdge) - this.getWidth();
            }
            this.mRightViewIndex++;
        }

    }

    @Override
    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    @Override
    public View getSelectedView() {
        // TODO: implement
        return null;
    }

    private synchronized void initView() {
        this.mLeftViewIndex = -1;
        this.mRightViewIndex = 0;
        this.mDisplayOffset = 0;
        this.mCurrentX = 0;
        this.mNextX = 0;
        this.mMaxX = Integer.MAX_VALUE;
        this.mScroller = new Scroller(this.getContext());
        this.mGesture = new GestureDetector(this.getContext(), this.mOnGesture);
    }

    protected boolean onDown(final MotionEvent e) {
        this.mScroller.forceFinished(true);
        return true;
    }

    protected boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
        synchronized (HorizontialListView.this) {
            this.mScroller.fling(this.mNextX, 0, (int) -velocityX, 0, 0, this.mMaxX, 0, 0);
        }
        this.requestLayout();

        return true;
    }

    @Override
    protected synchronized void onLayout(final boolean changed, final int left, final int top, final int right,
            final int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (this.mAdapter == null) { return; }

        if (this.mDataChanged) {
            final int oldCurrentX = this.mCurrentX;
            this.initView();
            this.removeAllViewsInLayout();
            this.mNextX = oldCurrentX;
            this.mDataChanged = false;
        }

        if (this.mScroller.computeScrollOffset()) {
            final int scrollx = this.mScroller.getCurrX();
            this.mNextX = scrollx;
        }

        if (this.mNextX < 0) {
            this.mNextX = 0;
            this.mScroller.forceFinished(true);
        }
        if (this.mNextX > this.mMaxX) {
            this.mNextX = this.mMaxX;
            this.mScroller.forceFinished(true);
        }

        final int dx = this.mCurrentX - this.mNextX;

        this.removeNonVisibleItems(dx);
        this.fillList(dx);
        this.positionItems(dx);

        this.mCurrentX = this.mNextX;

        if (!this.mScroller.isFinished()) {
            this.post(new Runnable() {
                @Override
                public void run() {
                    HorizontialListView.this.requestLayout();
                }
            });

        }
    }

    private void positionItems(final int dx) {
        if (this.getChildCount() > 0) {
            this.mDisplayOffset += dx;
            int left = this.mDisplayOffset;
            for (int i = 0; i < this.getChildCount(); i++) {
                final View child = this.getChildAt(i);
                final int childWidth = child.getMeasuredWidth();
                child.layout(left, 0, left + childWidth, child.getMeasuredHeight());
                left += childWidth;
            }
        }
    }

    private void removeNonVisibleItems(final int dx) {
        View child = this.getChildAt(0);
        while ((child != null) && ((child.getRight() + dx) <= 0)) {
            this.mDisplayOffset += child.getMeasuredWidth();
            this.mRemovedViewQueue.offer(child);
            this.removeViewInLayout(child);
            this.mLeftViewIndex++;
            child = this.getChildAt(0);

        }

        child = this.getChildAt(this.getChildCount() - 1);
        while ((child != null) && ((child.getLeft() + dx) >= this.getWidth())) {
            this.mRemovedViewQueue.offer(child);
            this.removeViewInLayout(child);
            this.mRightViewIndex--;
            child = this.getChildAt(this.getChildCount() - 1);
        }
    }

    private synchronized void reset() {
        this.initView();
        this.removeAllViewsInLayout();
        this.requestLayout();
    }

    public synchronized void scrollTo(final int x) {
        this.mScroller.startScroll(this.mNextX, 0, x - this.mNextX, 0);
        this.requestLayout();
    }

    @Override
    public void setAdapter(final ListAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataObserver);
        }
        this.mAdapter = adapter;
        this.mAdapter.registerDataSetObserver(this.mDataObserver);
        this.reset();
    }

    @Override
    public void setOnItemClickListener(final AdapterView.OnItemClickListener listener) {
        this.mOnItemClicked = listener;
    }

    // @Override
    // public void setOnItemLongClickListener(final AdapterView.OnItemLongClickListener listener) {
    // this.mOnItemLongClicked = listener;
    // }

    @Override
    public void setOnItemSelectedListener(final AdapterView.OnItemSelectedListener listener) {
        this.mOnItemSelected = listener;
    }

    @Override
    public void setSelection(final int position) {
        // TODO: implement
    }

}
