
package nz.ac.otago.psyanlab.common.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class ManualHorizontalScrollView extends HorizontalScrollView {

    // true if we can scroll (not locked)
    // false if we cannot scroll (locked)
    private boolean mScrollable = true;

    public ManualHorizontalScrollView(Context context) {
        super(context);
    }

    public ManualHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ManualHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScrollingEnabled(boolean enabled) {
        mScrollable = enabled;
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (mScrollable) {
                    return super.onTouchEvent(ev);
                }
                // only continue to handle the touch event if scrolling enabled
                return mScrollable; // mScrollable is always false at this point
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mScrollable = false;
        super.onSizeChanged(w, h, oldw, oldh);

        // No scrolling while the view is resizing.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollable = true;
            }
        }, 100);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!mScrollable) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (!mScrollable) {
            return;
        }
        super.scrollTo(x, y);
    }

    // Custom smooth scroll method since norm is final and cannot be overridden
    public final void smooothScrollToIfEnabled(int x, int y) {
        if (!mScrollable) {
            return;
        }
        smoothScrollTo(x, y);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (!mScrollable) {
            return 0;
        }
        // Disable focusing to stop skipping around when various things happen.
        // return 0;
        return super.computeScrollDeltaToGetChildRectOnScreen(rect);
    }

}