package app.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;

import com.sunshine.antilose2.R;


/**
 * tab 显示的button，添加了绘制badge的逻辑
 * 
 * @author asus
 * 
 */
public class TabButton extends Button {

    private boolean needBadge;
    private Drawable badgeImage;
    private float badgePaddingTop;
    private float badgePaddingLeft;

    public TabButton(Context context) {
        this(context, null);
    }

    public TabButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        badgePaddingLeft = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 11, context.getResources().getDisplayMetrics());
        badgePaddingTop = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 7, context.getResources().getDisplayMetrics());
        needBadge = false;
        badgeImage = context.getResources().getDrawable(R.drawable.tab_notify_point);
        setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.tabbar_text_size));
        int paddingTopBottom =
                getResources().getDimensionPixelOffset(R.dimen.tabbar_padding_top_bottom);
        int paddingLeftRight =
                getResources().getDimensionPixelOffset(R.dimen.tabbar_padding_left_right);
        setPadding(paddingLeftRight, paddingTopBottom, paddingLeftRight, paddingTopBottom);
        setGravity(Gravity.CENTER);
    }

    public boolean isNeedBadge() {
        return needBadge;
    }

    public void setNeedBadge(boolean needBadge) {
        this.needBadge = needBadge;
        invalidate();
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean rs = super.setFrame(l, t, r, b);
        if (needBadge) {
            int badgeWidth = badgeImage.getIntrinsicWidth();
            int badgeHeight = badgeImage.getIntrinsicHeight();
            int x =  (int) ((r - l) / 2 + badgePaddingLeft);
            int y = (int) badgePaddingTop;
            badgeImage.setBounds(x, y, x + badgeWidth, y + badgeHeight);
        }
        return rs;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (needBadge) {
            badgeImage.draw(canvas);
        }
    }

    public void setIcon(@DrawableRes int resId) {
        setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(resId), null, null);
    }

    public void setTitle(CharSequence title) {
        setText(title);
    }

}
