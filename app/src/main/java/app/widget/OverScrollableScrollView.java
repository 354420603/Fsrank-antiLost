package app.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * 用于解决ScrollView中嵌套ViewPager再嵌套ListView或ScrollView的滑动问题
 * @author jinbing
 */
public class OverScrollableScrollView extends ScrollView {

    private float mLastMotionY;

    public OverScrollableScrollView(Context context) {
        super(context);
    }

    public OverScrollableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public OverScrollableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void scrollBy(int x, int y) {
        //region 解决个人中心Fragment ViewPager ScrollView切换tab后自动滑动问题，临时解决方法，未找到原因
        //        super.scrollBy(x, y);
        //endregion
    }

    public static boolean canScroll(View v, boolean checkV, int dy, int x, int y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            for (int i = count - 1; i >= 0; i--) {
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft()
                        && x + scrollX < child.getRight()
                        && y + scrollY >= child.getTop()
                        && y + scrollY < child.getBottom()
                        && canScroll(child, true, dy,
                        x + scrollX - child.getLeft(), y + scrollY
                                - child.getTop())) {
                    return true;
                }
            }
        }
        return checkV && ViewCompat.canScrollVertically(v, -dy);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return super.onInterceptTouchEvent(event);
        }

        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        float dy = y - mLastMotionY;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if ((dy > 0 || !ViewCompat.canScrollVertically(this, (int) -dy))
                        && canScroll(this, false, (int) dy, (int) x, (int) y)) {
                    mLastMotionY = y;
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

}
