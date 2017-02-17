package app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunshine.antilose2.R;

import app.util.ColorUtils;



/**
 *
 * @author jinbing
 */
public class TitleView extends RelativeLayout {

    private TextView titleView;
    private FrameLayout titleContent;
    private ImageButton leftImageButton;
    private ImageButton rightImageButton;
    private View leftButton;
    private TextView leftTextButton;
    private View rightButton;
    private TextView rightTextButton;

    private boolean titleCentered = true;

    private OnTitleClickListener mOnTitleClickListener;
    private OnClickListener titleClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.ib_title_left_image) {
                if (mOnTitleClickListener != null)
                    mOnTitleClickListener.onLeftTitleButtonClick();
            } else if (id == R.id.tv_title_left_text) {
                if (mOnTitleClickListener != null)
                    mOnTitleClickListener.onLeftTitleButtonClick();
            } else if (id == R.id.ib_title_right_image) {
                if (mOnTitleClickListener != null)
                    mOnTitleClickListener.onRightTitleButtonClick();
            } else if (id == R.id.fl_title_content) {
                if (mOnTitleClickListener != null)
                    mOnTitleClickListener.onTitleClick();
            } else if (id == R.id.tv_title_right_text) {
                if (mOnTitleClickListener != null)
                    mOnTitleClickListener.onRightTitleButtonClick();
            }

        }
    };

    private OnTouchListener titleTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    if (v instanceof ImageView) {
                        Drawable drawable = ((ImageView) v).getDrawable();
                        if (drawable instanceof BitmapDrawable) {
                            int color = Color.argb(255, 204, 204, 204);
                            Drawable pressedDrawable = ColorUtils.getFilterDrawable(drawable, color);
                            ((ImageView) v).setImageDrawable(pressedDrawable);
                        }
                    } else if (v instanceof TextView) {
                        int color = Color.argb(255, 204, 204, 204);
                        ((TextView) v).setTextColor(color);
                        Drawable[] drawables = ((TextView) v).getCompoundDrawables();
                        Drawable[] pressedDrawables = new Drawable[4];
                        for (int i = 0; i < drawables.length; i++) {
                            Drawable drawable = drawables[i];
                            if (drawable != null) {
                                pressedDrawables[i] = ColorUtils.getFilterDrawable(drawable, color);
                            } else {
                                pressedDrawables[i] = null;
                            }
                        }
                        ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(
                                pressedDrawables[0], pressedDrawables[1], pressedDrawables[2], pressedDrawables[3]);
                    }
                } break;
                case MotionEvent.ACTION_UP: {
                    if (v instanceof ImageView) {
                        Drawable drawable = ((ImageView) v).getDrawable();
                        if (drawable instanceof BitmapDrawable) {
                            int color = Color.argb(255, 255, 255, 255);
                            Drawable pressedDrawable = ColorUtils.getFilterDrawable(drawable, color);
                            ((ImageView) v).setImageDrawable(pressedDrawable);
                        }
                    } else if (v instanceof TextView) {
                        int color = Color.argb(255, 255, 255, 255);
                        ((TextView) v).setTextColor(color);
                        Drawable[] drawables = ((TextView) v).getCompoundDrawables();
                        Drawable[] pressedDrawables = new Drawable[4];
                        for (int i = 0; i < drawables.length; i++) {
                            Drawable drawable = drawables[i];
                            if (drawable != null) {
                                pressedDrawables[i] = ColorUtils.getFilterDrawable(drawable, color);
                            } else {
                                pressedDrawables[i] = null;
                            }
                        }
                        ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(
                                pressedDrawables[0], pressedDrawables[1], pressedDrawables[2], pressedDrawables[3]);
                    }
                } break;
            }
            return false;
        }
    };

    public TitleView(Context context) {
        super(context);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.title_bar, this);

        titleView = new TextView(getContext());
        titleView.setGravity(Gravity.CENTER);

        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_big));
        titleView.setTextColor(getResources().getColor(R.color.color_title));
        titleView.setMaxLines(2);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        leftImageButton = (ImageButton) findViewById(R.id.ib_title_left_image);
        rightImageButton = (ImageButton) findViewById(R.id.ib_title_right_image);
        titleContent = (FrameLayout) findViewById(R.id.fl_title_content);
        leftButton = findViewById(R.id.fly_left_button);
        rightButton = findViewById(R.id.fly_right_button);
        leftImageButton.setOnClickListener(titleClickListener);
        rightImageButton.setOnClickListener(titleClickListener);
        titleContent.setOnClickListener(titleClickListener);
        leftTextButton = (TextView) findViewById(R.id.tv_title_left_text);
        leftTextButton.setOnClickListener(titleClickListener);
        leftTextButton.setOnTouchListener(titleTouchListener);
        rightTextButton = (TextView) findViewById(R.id.tv_title_right_text);
        rightTextButton.setOnClickListener(titleClickListener);
        leftImageButton.setOnTouchListener(titleTouchListener);
        rightImageButton.setOnTouchListener(titleTouchListener);
        rightTextButton.setOnTouchListener(titleTouchListener);

        titleContent.setVisibility(View.GONE);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        calTitleMargin();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void calTitleMargin() {
        LayoutParams params = (LayoutParams) titleContent.getLayoutParams();
        int leftWidth = leftButton.getVisibility() == View.VISIBLE
                ? leftButton.getWidth() : 0;
        int rightWidth = rightButton.getVisibility() == View.VISIBLE
                ? rightButton.getWidth() : 0;

        int margin = Math.max(leftWidth, rightWidth);
        int leftMargin;
        int rightMargin;
        if (titleCentered) {
            leftMargin = margin;
            rightMargin = margin;
        } else {
            leftMargin = leftWidth;
            rightMargin = rightWidth;
        }
        if (params.leftMargin != leftMargin || params.rightMargin != rightMargin) {
            params.leftMargin = leftMargin;
            params.rightMargin = rightMargin;
            titleContent.setLayoutParams(params);
        }
    }

    public void setTitleCentered(boolean centered) {
        titleCentered = centered;
    }

    public void setTitleContent(View view) {
        titleContent.setVisibility(View.VISIBLE);
        if (view.getParent() != null) {
            return;
        }
        ViewGroup.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
        titleContent.removeAllViews();
        titleContent.addView(view, lp);

        calTitleMargin();
    }

    public void setTitle(CharSequence title) {
        if (titleView.getParent() == null) {
            setTitleContent(titleView);
        }
        titleView.setText(title);

        calTitleMargin();
    }

    public void setTitleTextSize(int sp) {
        titleView.setTextSize(sp);

        calTitleMargin();
    }

    public void setTitleColor(int textColor) {
        titleView.setTextColor(textColor);

        calTitleMargin();
    }

    public void setTitle(int titleId) {
        String title = this.getResources().getString(titleId);
        titleView.setText(title);

        calTitleMargin();
    }

    public void setLeftTitleButton(int strId, int bgId) {
        calTitleMargin();
    }

    public void setRightTitleButton(int strId, int bgId) {
        calTitleMargin();
    }

    public void setLeftTitleImageButton(int srcId, int bgId) {
        if (srcId != -1 && srcId != 0) {
            leftImageButton.setVisibility(View.VISIBLE);
            if (bgId != -1 && bgId != 0) {
                leftImageButton.setBackgroundResource(bgId);
            }
            leftImageButton.setImageResource(srcId);
        } else {
            leftImageButton.setVisibility(View.GONE);
        }

        calTitleMargin();
    }

    public void setRightTitleImageButton(int srcId, int bgId) {
        if (srcId != -1 && srcId != 0) {
            rightImageButton.setVisibility(View.VISIBLE);
            if (bgId != -1 && bgId != 0) {
                rightImageButton.setBackgroundResource(bgId);
            }
            rightImageButton.setImageResource(srcId);
        } else {
            rightImageButton.setVisibility(View.GONE);
        }

        calTitleMargin();
    }

    public void setLeftTitleTextButton(String text, int bgId) {
        if (!TextUtils.isEmpty(text)) {
            leftTextButton.setVisibility(View.VISIBLE);
            if (bgId != -1 && bgId != 0) {
                leftTextButton.setBackgroundResource(bgId);
            }
            leftTextButton.setText(text);
        } else {
            leftTextButton.setVisibility(View.GONE);

        }

        calTitleMargin();
    }

    public void setRightTitleTextButton(String text, int bgId) {
        if (!TextUtils.isEmpty(text)) {
            rightTextButton.setVisibility(View.VISIBLE);
            if (bgId != -1 && bgId != 0) {
                rightTextButton.setBackgroundResource(bgId);
            }
            rightTextButton.setText(text);
        } else {
            rightTextButton.setVisibility(View.GONE);
        }

        calTitleMargin();
    }

    public void setLeftTitleTextSize(int sp) {
        leftTextButton.setTextSize(sp);

        calTitleMargin();
    }

    public void setLeftTitleColor(int textColor) {
        leftTextButton.setTextColor(textColor);

        calTitleMargin();
    }

    public void setRightTitleTextSize(int sp) {
        rightTextButton.setTextSize(sp);

        calTitleMargin();
    }

    public void setRightTitleColor(int textColor) {
        rightTextButton.setTextColor(textColor);

        calTitleMargin();
    }

    public TextView getLeftTextButton() {
        return leftTextButton;
    }

    public TextView getRightTextButton() {
        return rightTextButton;
    }

    public FrameLayout getTitleContent() {
        return titleContent;
    }

    public ImageButton getLeftImageButton() {
        return leftImageButton;
    }

    public ImageButton getRightImageButton() {
        return rightImageButton;
    }








    public void setOnTitleClickListener(OnTitleClickListener listener) {
        this.mOnTitleClickListener = listener;
    }

    public static interface OnTitleClickListener {

        public void onLeftTitleButtonClick();

        public void onRightTitleButtonClick();

        public void onTitleClick();

    }

}
