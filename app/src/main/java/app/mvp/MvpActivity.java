package app.mvp;

import android.content.Context;
import android.os.Bundle;

import app.views.BaseFragmentActivity;


/**
 * Created by jinbing on 2015/5/12 0012.
 */
public abstract class MvpActivity<V extends MvpView, P extends MvpPresenter<V>> extends
        BaseFragmentActivity
        implements MvpView {

    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!presenter.isViewAttached()) {
            presenter.attachView((V) this);
        }

        presenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    protected abstract P createPresenter();

}
