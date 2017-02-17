package app.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import app.views.BaseTabActivity;


/**
 * Created by jinbing on 2015/5/12 0012.
 */
public abstract class MvpTab<V extends MvpView, P extends MvpPresenter<V>>
        extends BaseTabActivity.Tab
        implements MvpView {

    protected P presenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create the presenter if needed
        if (presenter == null) {
            presenter = createPresenter();
        }
        presenter.attachView((V) this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView(getRetainInstance());
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    protected abstract P createPresenter();
}
