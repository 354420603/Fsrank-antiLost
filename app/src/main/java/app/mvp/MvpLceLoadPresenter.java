package app.mvp;

/**
 * Created by yejinbing on 15/5/13.
 */
public abstract class MvpLceLoadPresenter<V extends MvpLceLoadView<M>, M> extends MvpLcePresenter<V, M> {

    private boolean cleanOlds = false;

    /** 下次请求的Page位置 */
    protected int mNextPage = 1;

    /** 加载是否已完成标识 */
    protected boolean mLoadFinished = false;

    @Override
    public void attachView(V view) {
        super.attachView(view);
        if (data != null) {
            if (mLoadFinished)
                view.loadFinished();
            else
                view.loadCompleted();
        }
    }


    @Override
    public void detachView(boolean retainInstance) {
        if (isViewAttached()) {
            getView().loadCompleted();
        }
        super.detachView(retainInstance);
    }

    @Override
    public void onRefresh() {
        cleanOlds = true;
    }

    public void onLoadMore() {
        cleanOlds = false;
    }

    /**
     * 判断是否已加载完成
     * @param data
     * @return
     */
    protected boolean isLoadFinished(M data) {
        return isDataEmpty(data);
    }

    public boolean isOnRefresh() {
        return cleanOlds;
    }

    /**
     * 追加合并数据
     * @param front 前部数据
     * @param tail 需要追加的尾部数据
     * @return 追加合并后的结果
     */
    protected abstract M appendData(M front, M tail);

    /**
     * 判断数据是否为空
     * @param data
     * @return
     */
    protected abstract boolean isDataEmpty(M data);

}
