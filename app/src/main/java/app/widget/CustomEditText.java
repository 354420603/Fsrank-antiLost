package app.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by yejinbing on 16/4/19.
 */
public class CustomEditText extends EditText {

    private OnEditorActionListener mOnEditorActionListener;

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        super.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            v.clearFocus();
                        }
                    });
                }
                if (mOnEditorActionListener != null) {
                    return mOnEditorActionListener.onEditorAction(v, actionId, event);
                }
                return false;
            }
        });
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            setGravity(Gravity.CENTER_VERTICAL| Gravity.LEFT);
        } else {
            setGravity(Gravity.CENTER_VERTICAL| Gravity.RIGHT);
        }
    }

    @Override
    public void setOnEditorActionListener(OnEditorActionListener l) {
        mOnEditorActionListener = l;
    }
}
