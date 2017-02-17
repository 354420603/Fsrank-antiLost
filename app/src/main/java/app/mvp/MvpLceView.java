package app.mvp;

/**
 * Created by jinbing on 2015/5/12 0012.
 */
public interface MvpLceView<M> extends MvpView {

    /**
     * 显示Toast通知
     * @param msg
     */
    public void showToast(String msg);

    /**
     * 显示界面加载动画
     */
    public void showLoading();

    public void showProgress();

    /**
     * 显示错误
     * @param e
     */
    public void showError(Throwable e);

    /**
     * 显示请求错误消息
     * @param title
     * @param message
     */
    public void showErrorMessage(String title, String message);

    /**
     * 显示内容界面
     */
    public void showContent();

    /**
     * 显示ProgressDialog
     * @return
     */
    public void showProgressDialog(String message, boolean cancelable);

    /**
     * 隐藏ProgressDialog
     */
    public void dismissProgressDialog();

    public void setData(M data);

    public void loginToLoad();

    public void login();

    public void login(String prompt);
}
