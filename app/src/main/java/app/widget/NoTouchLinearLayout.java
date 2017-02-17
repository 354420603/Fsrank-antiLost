package app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by yejinbing on 16/3/21.
 */
public class NoTouchLinearLayout extends LinearLayout {
    public NoTouchLinearLayout(Context context) {
        super(context);
    }

    public NoTouchLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
