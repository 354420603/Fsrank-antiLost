package app.mvp;

/**
 * Created by jinbing on 2015/5/12 0012.
 */
public interface MvpPresenter<V extends MvpView> {

    public void onStart();

    public void onResume();

    public void onPause();

    public void onStop();

    public void attachView(V view);

    public void detachView(boolean retainInstance);

    public boolean isViewAttached();

    public V getView();

}
