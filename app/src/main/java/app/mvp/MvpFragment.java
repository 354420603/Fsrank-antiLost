package app.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import app.views.BaseFragment;


/**
 * Created by jinbing on 2015/5/12 0012.
 */
public abstract class MvpFragment<V extends MvpView, P extends MvpPresenter<V>>
        extends BaseFragment
        implements MvpView {

    protected P presenter;
    private Toast mToast;

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


    public void showToast(CharSequence s, int duration) {
        if (mToast != null) {
            mToast.setText(s);
            mToast.setDuration(duration);
            mToast.show();
        } else {
            mToast = Toast.makeText(getActivity(), s, duration);
            mToast.show();
        }
    }

}
