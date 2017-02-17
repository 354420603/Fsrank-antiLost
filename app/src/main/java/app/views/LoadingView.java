package app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.sunshine.antilose2.R;


/**
 *
 * @author jinbing
 */
public class LoadingView extends RelativeLayout {
	
	public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_loading, this);
    }
    
    /**
     * 显示动画
     */
    public void show() {
    	this.setVisibility(View.VISIBLE);
    }
    
    /**
     * 隐藏动画
     */
    public void dismiss() {
    	this.setVisibility(View.INVISIBLE);
    }

}
