package app.views;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sunshine.antilose2.R;

import app.widget.TitleView;



/**
 *
 * @author jinbing
 */
public class BasePreferencesActivity extends PreferenceActivity {

    private TitleView mTitleView;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        View view = getLayoutInflater().inflate(R.layout.base_preference_container, null);
        View listView = findViewById(android.R.id.list);
        ViewGroup parent = (ViewGroup) listView.getParent();
        parent.addView(view, 0);
        if (parent instanceof FrameLayout) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) listView.getLayoutParams();
            params.topMargin = params.topMargin
                    + getResources().getDimensionPixelSize(R.dimen.titlebar_height);
            params.gravity = Gravity.TOP;
            listView.setLayoutParams(params);
        } else if (parent instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) listView.getLayoutParams();
            params.topMargin = params.topMargin
                    + getResources().getDimensionPixelSize(R.dimen.titlebar_height);
            listView.setLayoutParams(params);
        }

        mTitleView = (TitleView) findViewById(R.id.title_view);
        mTitleView.setOnTitleClickListener(new TitleView.OnTitleClickListener() {
            @Override
            public void onLeftTitleButtonClick() {
                BasePreferencesActivity.this.onLeftTitleButtonClick();
            }

            @Override
            public void onRightTitleButtonClick() {
                BasePreferencesActivity.this.onRightTitleButtonClick();
            }

            @Override
            public void onTitleClick() {
                BasePreferencesActivity.this.onTitleClick();
            }
        });

        initTitle();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mToast != null) {
            mToast.cancel();
        }

        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_exit_no_anim, R.anim.activity_exit);
    }

    @Override
    public void finish() {
        super.finish();
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivityForResult(Intent intent, int requestCode,
                                       Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(
                R.anim.activity_enter, R.anim.activity_exit_no_anim);
    }

    private void initTitle() {
        setTitle(getTitle());
        try {
            int resId = this.getPackageManager().getActivityInfo(getComponentName(),0).labelRes;
            if(resId !=0){
                setTitle(resId);
            }
        } catch (NameNotFoundException e) { }
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
     * @param s 显示的字符
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
     * @param resId 显示的字符资源ID
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
}
