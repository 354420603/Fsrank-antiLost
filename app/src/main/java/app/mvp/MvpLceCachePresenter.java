package app.mvp;

/**
 * Created by jinbing on 2015/5/13 0013.
 */
public abstract class MvpLceCachePresenter<V extends MvpLceView<M>, M> extends MvpLcePresenter<V, M> {

    private String cacheDataName;
    private Class<M> dataClass;
    /**
     * 是否有从网络获取数据
     */
    protected boolean mLoadedFromNet = false;

    public MvpLceCachePresenter(String cacheDataName, Class<M> dataClass) {
        this.cacheDataName = cacheDataName;
        this.dataClass = dataClass;
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean needRefresh() {
        return super.needRefresh() || !mLoadedFromNet;
    }

    protected boolean needSaveToLocal() {
        return true;
    }



}
