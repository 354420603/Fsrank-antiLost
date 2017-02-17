package app.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunshine.antilose2.R;


/**
 * @author jinbing
 */
public class NetErrorView extends LinearLayout implements View.OnClickListener {

    private ImageView mIconIv;
    private TextView mMessageTv;
    private Button mRetryBtn;
    private Button mNetSettingBtn;

    private OnNetErrorListener mListener;

    public NetErrorView(Context context) {
        this(context, null);
    }

    public NetErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public NetErrorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_net_error, this);

        setOrientation(VERTICAL);

        mIconIv = (ImageView) findViewById(R.id.iv_icon);
        mMessageTv = (TextView) findViewById(R.id.tv_message);
        mRetryBtn = (Button) findViewById(R.id.btn_retry);
        mNetSettingBtn = (Button) findViewById(R.id.btn_net_setting);

        mRetryBtn.setOnClickListener(this);
        mNetSettingBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_retry:
                if (mListener != null) {
                    mListener.onRetry();
                }
                break;
            case R.id.btn_net_setting:
                if (mListener != null) {
                    mListener.onSettingNet();
                }
                break;
            default:
                break;
        }
    }

    public void showNetError() {
        mIconIv.setVisibility(View.VISIBLE);
        mMessageTv.setText("亲~~~~~~网络异常了");
        mRetryBtn.setVisibility(View.GONE);
        mNetSettingBtn.setVisibility(View.VISIBLE);
    }

    public void showConnectError() {
        mIconIv.setVisibility(View.VISIBLE);
        mMessageTv.setText("连接超时");
        mRetryBtn.setVisibility(View.VISIBLE);
        mNetSettingBtn.setVisibility(View.GONE);
    }

    public void showEmptyError(String errorMsg) {
        mIconIv.setVisibility(View.GONE);
        mMessageTv.setText(errorMsg);
        mRetryBtn.setVisibility(View.VISIBLE);
        mNetSettingBtn.setVisibility(View.GONE);
    }

    public void showErrorMessage(String errorMsg) {
        mIconIv.setVisibility(View.GONE);
        mMessageTv.setText(errorMsg);
        mRetryBtn.setVisibility(View.GONE);
        mNetSettingBtn.setVisibility(View.GONE);
    }

    public void setOnNetErrorListener(OnNetErrorListener l) {
        this.mListener = l;
    }

    public static interface OnNetErrorListener {
        public void onRetry();

        public void onSettingNet();
    }
}
