package app.mvp;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
public abstract class MvpLceFragment<M, V extends MvpLceView<M>, P extends MvpLcePresenter<V, M>>
        extends MvpFragment<V, P>
        implements MvpLceView<M> {

    protected static final int REQUEST_CODE_LOGIN = 10001;

    protected LoadingView mLoadingProgress;
    protected NetErrorView mNetErrorV;
    protected ViewGroup mContentV;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mvp_lce_fragment, container, false);

        ViewGroup contentParent = (ViewGroup) view.findViewById(R.id.v_content);
        onAddContentView(inflater, contentParent);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mContentV = (ViewGroup) view.findViewById(R.id.v_content);
        mLoadingProgress = (LoadingView) view.findViewById(R.id.lv_progress);
        mNetErrorV = (NetErrorView) view.findViewById(R.id.v_net_error);

        mNetErrorV.setVisibility(View.GONE);
        mContentV.setVisibility(View.GONE);
        mLoadingProgress.dismiss();

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

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.onRefresh();
            } else {
                if (!presenter.hasData()) {
//                    finish();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public abstract void onAddContentView(LayoutInflater inflater, @NonNull ViewGroup container);

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
                    AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
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
