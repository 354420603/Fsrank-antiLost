package app.mvp;

import java.lang.ref.WeakReference;

/**
 * Created by jinbing on 2015/5/12 0012.
 */
public abstract class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private WeakReference<V> reference;

    public void onStart() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onStop() {

    }

    @Override
    public void attachView(V view) {
        reference = new WeakReference<>(view);

    }

    @Override
    public void detachView(boolean retainInstance) {
        if (reference != null) {
            reference.clear();
            reference = null;
        }
    }

    @Override
    public boolean isViewAttached() {
        return reference != null && reference.get() != null;
    }

    @Override
    public V getView() {
        return reference.get();
    }



}
