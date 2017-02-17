package app.mvp;

/**
 * Created by yejinbing on 15/9/23.
 */
public interface MvpBaseView extends MvpView {
    /**
     * 显示Toast通知
     * @param msg
     */
    public void showToast(String msg);

    /**
     * 显示请求错误消息
     * @param title
     * @param message
     */
    public void showErrorMessage(String title, String message);

    /**
     * 显示ProgressDialog
     * @return
     */
    public void showProgressDialog(String message, boolean cancelable);

    /**
     * 隐藏ProgressDialog
     */
    public void dismissProgressDialog();

}
