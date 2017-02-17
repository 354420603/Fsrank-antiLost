package app.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sunshine.antilose2.R;

import app.widget.CustomDialog;



/**
 *
 * @author jinbing
 */
public class BaseFragment extends Fragment implements DialogInterface.OnCancelListener {

    private Dialog mProgressDialog;
    private AlertDialog mErrorMsgDialog;

    private Toast mToast;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    /**
     * 显示Toast，该方式会复用之前的Toast，也就是如果之前的没显示完，便会被覆盖
     * @param s 显示的字符
     * @param duration 显示的时间
     */
    public void showToast(CharSequence s, int duration) {
        if (mToast != null) {
            mToast.setText(s);
            mToast.setDuration(duration);
            mToast.show();
        } else {
            mToast = Toast.makeText(getActivity().getApplication(), s, duration);
            mToast.show();
        }
    }

    /**
     * 显示Toast，该方式会复用之前的Toast，也就是如果之前的没显示完，便会被覆盖
     * @param resId 显示的字符资源ID
     * @param duration 显示的时间
     */
    public void showToast(@StringRes int resId, int duration) {
        if (mToast != null) {
            mToast.setText(resId);
            mToast.setDuration(duration);
            mToast.show();
        } else {
            mToast = Toast.makeText(getActivity().getApplication(), resId, duration);
            mToast.show();
        }
    }

    public boolean isProgressDialogShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public void showProgressDialog(String message, boolean cancelable) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(getActivity(), R.style.CheckVersionDialog);
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.layout_check_new_version, null);
            mProgressDialog.setContentView(view);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(this);
        }

        mProgressDialog.setCancelable(cancelable);
        TextView tvHint = (TextView) mProgressDialog.findViewById(R.id.tv_check_version);
        if (!TextUtils.isEmpty(message)) {
            tvHint.setText(message);
            tvHint.setVisibility(View.VISIBLE);
        } else {
            tvHint.setVisibility(View.GONE);
        }

        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showErrorMessage(String title, String message) {
        if (mErrorMsgDialog != null && mErrorMsgDialog.isShowing()) {
            mErrorMsgDialog.dismiss();
        }

        mErrorMsgDialog = new CustomDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

}
