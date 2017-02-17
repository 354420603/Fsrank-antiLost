package app.mvp;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.sunshine.antilose2.R;

import app.util.Utils;
import app.views.LoadingView;
import app.widget.NetErrorView;



/**
 * Created by jinbing on 2015/5/12 0012.
 */
public abstract class MvpLceActivity<M, V extends MvpLceView<M>, P extends MvpLcePresenter<V, M>>
        extends MvpActivity<V, P>
        implements MvpLceView<M> {

    private static final int REQUEST_CODE_LOGIN = 10001;
    protected LoadingView mLoadingProgress;
    protected NetErrorView mNetErrorV;
    protected ViewGroup mContentV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.mvp_lce_activity);

        mContentV = (ViewGroup) findViewById(R.id.v_content);
        mLoadingProgress = (LoadingView) findViewById(R.id.lv_progress);

        mNetErrorV.setVisibility(View.GONE);
        mContentV.setVisibility(View.GONE);
        mLoadingProgress.dismiss();
        mNetErrorV = (NetErrorView) findViewById(R.id.v_net_error);

        mNetErrorV.setOnNetErrorListener(new NetErrorView.OnNetErrorListener() {
            @Override
            public void onRetry() {
                presenter.onRefresh();
            }

            @Override
            public void onSettingNet() {
                Utils.startNetworkSettingActivity(getContext());
            }
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentV.removeAllViews();
        LayoutInflater.from(this).inflate(layoutResID, mContentV, true);
    }

    @Override
    public void setContentView(View view) {
        mContentV.removeAllViews();
        mContentV.addView(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.onRefresh();
            } else {
                if (!presenter.hasData()) {
                    finish();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void showLoading() {
        if (!presenter.hasData()) {
            mNetErrorV.setVisibility(View.GONE);
            mLoadingProgress.show();
        }
    }

    @Override
    public void showProgress() {
        mNetErrorV.setVisibility(View.GONE);
        mLoadingProgress.show();
    }

    @Override
    public void showError(Throwable e) {
        hideProgress();

        if (!presenter.hasData()) {
            hideContent();
            mNetErrorV.setVisibility(View.VISIBLE);
            if (e instanceof NetworkErrorException) {
                mNetErrorV.showConnectError();
            } else {
                mNetErrorV.showEmptyError(e.getMessage());
            }
        }
    }

    @Override
    public void showContent() {
        hideProgress();
        mNetErrorV.setVisibility(View.GONE);

        if (mContentV.getVisibility() != View.VISIBLE) {
            mContentV.startAnimation(
                    AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        }
        mContentV.setVisibility(View.VISIBLE);
    }

    @Override
    public void loginToLoad() {

    }

    @Override
    public void login() {


    }

    @Override
    public void login(String prompt) {

    }

    public void hideProgress() {
        mLoadingProgress.dismiss();
    }

    public void hideContent() {
        mContentV.setVisibility(View.GONE);
    }

}
