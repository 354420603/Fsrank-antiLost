package app.views;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunshine.antilose2.R;

import app.ui.Constanst;
import app.widget.CustomDialog;
import app.widget.TitleView;


/**
 * Created by yejinbing on 15/9/14.
 */
public class BaseFragmentActivity extends FragmentActivity implements DialogInterface.OnCancelListener {

    public static final String EXTRA_SRC_RECT = "extra.src_rect";

    private TitleView mTitleView;
    private FrameLayout baseContainer;

    private Toast mToast;

    private Dialog mProgressDialog;
    private AlertDialog mErrorMsgDialog;

    private boolean mResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_container);
        baseContainer = (FrameLayout) this.findViewById(R.id.base_container);
        mTitleView = (TitleView) findViewById(R.id.title_view);
        mTitleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
            @Override
            public void onLeftTitleButtonClick() {
                BaseFragmentActivity.this.onLeftTitleButtonClick();
            }

            @Override
            public void onRightTitleButtonClick() {
                BaseFragmentActivity.this.onRightTitleButtonClick();
            }

            @Override
            public void onTitleClick() {
                BaseFragmentActivity.this.onTitleClick();
            }
        });

        initTitle();

//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
        mResumed = true;
//        WeixinShareEvent weixinShareEvent = EventBus.getDefault().getStickyEvent(WeixinShareEvent.class);
//        if (weixinShareEvent != null) {
//            onEventMainThread(weixinShareEvent);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
        mResumed = false;
    }

    @Override
    protected void onDestroy() {
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        baseContainer.removeAllViews();
        LayoutInflater.from(this).inflate(layoutResID, baseContainer, true);
        initTitle();
    }

    @Override
    public void setContentView(View view) {
        baseContainer.removeAllViews();
        baseContainer.addView(view);
        initTitle();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        baseContainer.removeAllViews();
        baseContainer.addView(view, params);
        initTitle();
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constanst.EXTRA_BACK_TO_INTENT)) {
            Intent toIntent = intent.getParcelableExtra(Constanst.EXTRA_BACK_TO_INTENT);
            startActivity(toIntent);
            finish();
            overridePendingTransition(0, 0);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_exit_no_anim, R.anim.activity_exit);
        }
    }

    public void setTitleViewGone() {
        mTitleView.setVisibility(View.GONE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        baseContainer.setLayoutParams(params);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(
                R.anim.activity_enter, R.anim.activity_exit_no_anim);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        overridePendingTransition(
                R.anim.activity_enter, R.anim.activity_exit_no_anim);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(
                R.anim.activity_enter, R.anim.activity_exit_no_anim);
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        super.startActivityFromFragment(fragment, intent, requestCode);
        overridePendingTransition(
                R.anim.activity_enter, R.anim.activity_exit_no_anim);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivityForResult(Intent intent, int requestCode,
                                       Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(
                R.anim.activity_enter, R.anim.activity_exit_no_anim);
    }

//    public void onEventMainThread(WeixinShareEvent event) {
//        if (mResumed) {
//            if (event.successed) {
//                showLargeToast(getString(R.string.share_successed));
//            }
//            EventBus.getDefault().removeStickyEvent(WeixinShareEvent.class);
//        }
//    }

    private void initTitle() {
        setTitle(getTitle());
        try {
            int resId = this.getPackageManager().getActivityInfo(getComponentName(), 0).labelRes;
            if (resId != 0) {
                setTitle(resId);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        ((View) this.findViewById(Window.ID_ANDROID_CONTENT).getParent())
                .setBackgroundColor(Color.WHITE);
    }

    public TitleView getTitleView() {
        return mTitleView;
    }

    public void setTitleContent(View view) {
        mTitleView.setTitleContent(view);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleView.setTitle(title);
    }

    public void setTitleTextSize(int sp) {
        mTitleView.setTitleTextSize(sp);
    }

    @Override
    public void setTitleColor(int textColor) {
        mTitleView.setTitleColor(textColor);
    }

    @Override
    public void setTitle(int titleId) {
        mTitleView.setTitle(titleId);
    }

    public void setLeftTitleButton(int strId, int bgId) {
        mTitleView.setLeftTitleButton(strId, bgId);
    }

    public void setRightTitleButton(int strId, int bgId) {
        mTitleView.setRightTitleButton(strId, bgId);
    }

    public void setLeftTitleImageButton(int srcId, int bgId) {
        mTitleView.setLeftTitleImageButton(srcId, bgId);
    }

    public void setRightTitleImageButton(int srcId, int bgId) {
        mTitleView.setRightTitleImageButton(srcId, bgId);
    }


    protected void onLeftTitleButtonClick() {
    }

    protected void onRightTitleButtonClick() {
    }

    protected void onTitleClick() {
    }

    /**
     * 显示Toast，该方式会复用之前的Toast，也就是如果之前的没显示完，便会被覆盖
     *
     * @param s        显示的字符
     * @param duration 显示的时间
     */
    public void showToast(CharSequence s, int duration) {
        if (mToast != null) {
            mToast.setText(s);
            mToast.setDuration(duration);
            mToast.show();
        } else {
            mToast = Toast.makeText(this, s, duration);
            mToast.show();
        }
    }

    /**
     * 显示Toast，该方式会复用之前的Toast，也就是如果之前的没显示完，便会被覆盖
     *
     * @param resId    显示的字符资源ID
     * @param duration 显示的时间
     */
    public void showToast(@StringRes int resId, int duration) {
        if (mToast != null) {
            mToast.setText(resId);
            mToast.setDuration(duration);
            mToast.show();
        } else {
            mToast = Toast.makeText(this, resId, duration);
            mToast.show();
        }
    }

    public void showLargeToast(String message) {
        View layout = LayoutInflater.from(this).inflate(R.layout.layout_toast, null);
        TextView tvToast = (TextView) layout.findViewById(R.id.tv_toast);
        tvToast.setText(message);
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public boolean isProgressDialogShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public void showProgressDialog(String message, boolean cancelable) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(this, R.style.CheckVersionDialog);
            View view = getLayoutInflater().inflate(R.layout.layout_check_new_version, null);
            mProgressDialog.setContentView(view);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(this);
        }

        mProgressDialog.setCancelable(cancelable);
        TextView tvHint = (TextView) mProgressDialog.findViewById(R.id.tv_check_version);
        if (!TextUtils.isEmpty(message)) {
            tvHint.setText(message);
            tvHint.setVisibility(View.VISIBLE);
        } else {
            tvHint.setVisibility(View.GONE);
        }

        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showErrorMessage(String title, String message) {
        if (mErrorMsgDialog != null && mErrorMsgDialog.isShowing()) {
            mErrorMsgDialog.dismiss();
        }

        mErrorMsgDialog = new CustomDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

}
