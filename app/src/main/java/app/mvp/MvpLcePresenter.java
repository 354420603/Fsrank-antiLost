package app.mvp;

import android.accounts.NetworkErrorException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import app.util.Utils;


/**
 * Created by jinbing on 2015/5/12 0012.
 */
public abstract class MvpLcePresenter<V extends MvpLceView<M>, M> extends MvpBasePresenter<V> {

    protected boolean loadingRequest = false;
    protected M data;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (isViewAttached()) {
                    boolean netValid = Utils.checkNetwork(getView().getContext());
                    if (netValid) {
                        if (needRefresh()) {
                            onRefresh();
                        }
                    } else {
                        if (!hasData()) {
                            getView().showError(new NetworkErrorException());
                        }
                    }
                }
            }
        }
    };

    @Override
    public void attachView(V view) {
        super.attachView(view);

        if (data != null) {
            view.setData(data);
            view.showContent();
        }
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        loadingRequest = false;
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getView().getContext().registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onStop() {
        getView().getContext().unregisterReceiver(mReceiver);

        super.onStop();
    }

    public abstract void onRefresh();

    public void onNetValid() {

    }


    /**
     * 是否已加载数据
     * @return
     */
    public boolean hasData() {
        return data != null;
    }

    /**
     * 判断是否需要加载数据
     * @return
     */
    public boolean needRefresh() {
        return data == null;
    }

    public boolean isLoadingRequest() {
        return loadingRequest;
    }

    public M getData() {
        return data;
    }
}
