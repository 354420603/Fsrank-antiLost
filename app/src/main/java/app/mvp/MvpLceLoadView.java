package app.mvp;

/**
 * Created by jinbing on 2015/5/14 0014.
 */
public interface MvpLceLoadView<M> extends MvpLceView<M> {

    /**
     * 本次加载结束
     */
    public void loadCompleted();

    /**
     * 已全部加载更多列表
     */
    public void loadFinished();

}
