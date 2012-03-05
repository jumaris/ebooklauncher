package uk.co.droidinactu.common.view;

import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.GridView;

public final class EInkGridView extends GridView {

	public EInkGridView(final Context context) {
		super(context);
	}

	public EInkGridView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public EInkGridView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void invalidate() {
		// super.invalidate(mUpdateMode);
		try {
			final Method invalidateMethod = super.getClass().getMethod("invalidate", int.class);
			invalidateMethod.invoke(this, EInkListView.mUpdateMode);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void invalidate(final int l, final int t, final int r, final int b) {
		// super.invalidate(l, t, r, b, mUpdateMode);
		try {
			final Method invalidateMethod = super.getClass().getMethod("invalidate", int.class, int.class, int.class,
			        int.class, int.class);
			invalidateMethod.invoke(this, l, t, r, b, EInkListView.mUpdateMode);
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void invalidate(final Rect dirty) {
		// super.invalidate(dirty, mUpdateMode);
		try {
			final Method invalidateMethod = super.getClass().getMethod("invalidate", Rect.class, int.class);
			invalidateMethod.invoke(this, dirty, EInkListView.mUpdateMode);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void invalidateDrawable(final Drawable drawable) {
		// super.invalidateDrawable(drawable, mUpdateMode);
		try {
			final Method invalidateMethod = super.getClass().getMethod("invalidateDrawable", Drawable.class, int.class);
			invalidateMethod.invoke(this, drawable, EInkListView.mUpdateMode);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void postInvalidate() {
		// super.postInvalidate(mUpdateMode);
		try {
			final Method invalidateMethod = super.getClass().getMethod("postInvalidate", int.class);
			invalidateMethod.invoke(this, EInkListView.mUpdateMode);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void postInvalidate(final int left, final int top, final int right, final int bottom) {
		// super.postInvalidate(left, top, right, bottom, mUpdateMode);
		try {
			final Method invalidateMethod = super.getClass().getMethod("postInvalidate", int.class, int.class,
			        int.class, int.class, int.class);
			invalidateMethod.invoke(this, left, top, right, bottom, EInkListView.mUpdateMode);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void postInvalidateDelayed(final long delayMilliseconds) {
		// super.postInvalidateDelayed(delayMilliseconds, mUpdateMode);
		try {
			final Method invalidateMethod = super.getClass().getMethod("postInvalidateDelayed", long.class, int.class);
			invalidateMethod.invoke(this, delayMilliseconds, EInkListView.mUpdateMode);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void postInvalidateDelayed(final long delayMilliseconds, final int left, final int top, final int right,
	        final int bottom) {
		// super.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom, mUpdateMode);
		try {
			final Method invalidateMethod = super.getClass().getMethod("postInvalidateDelayed", long.class, int.class,
			        int.class, int.class, int.class, int.class);
			invalidateMethod.invoke(this, delayMilliseconds, left, top, right, bottom, EInkListView.mUpdateMode);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void scrollBy(final int x, final int y) {
		// super.scrollBy(x, y, mUpdateMode);
		try {
			final Method invalidateMethod = super.getClass().getMethod("scrollBy", int.class, int.class, int.class);
			invalidateMethod.invoke(this, x, y, EInkListView.mUpdateMode);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void scrollTo(final int x, final int y) {
		// super.scrollTo(x, y, mUpdateMode);
		try {
			final Method invalidateMethod = super.getClass().getMethod("scrollTo", int.class, int.class, int.class);
			invalidateMethod.invoke(this, x, y, EInkListView.mUpdateMode);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
