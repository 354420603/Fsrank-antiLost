package app.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunshine.antilose2.R;


public class CustomDialog extends AlertDialog {

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomDialog(Context context) {
        super(context);
    }

    public static class Builder extends AlertDialog.Builder {

        public Builder(Context context) {
            super(context);
            this.context = context;
        }

        public Builder(Context context, int theme) {
            super(context, theme);
        }


        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private Boolean cancelable = true;
        private Boolean canceledOnTouchOutside;

        private OnClickListener
                positiveButtonClickListener,
                negativeButtonClickListener;


        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public AlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.dialog_custom, null);
            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setCancelable(cancelable)
                    .create();
            dialog.setView(layout, 0, 0, 0, 0);
            if (canceledOnTouchOutside != null) {
                dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            }
            TextView tvTitle = ((TextView) layout.findViewById(R.id.tv_title));
            View titleDivider = layout.findViewById(R.id.title_divider);
            if (title != null) {
                tvTitle.setText(title);
                tvTitle.setVisibility(View.VISIBLE);
                titleDivider.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
                titleDivider.setVisibility(View.GONE);
            }
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_positive)).setText(positiveButtonText);
                layout.findViewById(R.id.btn_positive)
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (positiveButtonClickListener != null)
                                    positiveButtonClickListener.onClick(
                                            dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                dialog.dismiss();
                            }
                        });
            } else {
                layout.findViewById(R.id.btn_positive).setVisibility(View.GONE);
            }
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_negative)).setText(negativeButtonText);
                layout.findViewById(R.id.btn_negative)
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (negativeButtonClickListener != null)
                                    negativeButtonClickListener.onClick(
                                            dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                dialog.dismiss();
                            }
                        });
            } else {
                layout.findViewById(R.id.btn_negative).setVisibility(View.GONE);
            }
            if (positiveButtonText == null && negativeButtonText == null) {
                layout.findViewById(R.id.buttonPanel).setVisibility(View.GONE);
            }
            if (message != null) {
                ((TextView) layout.findViewById(R.id.tv_message)).setText(message);
            } else if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                layout.findViewById(R.id.content).setVisibility(View.GONE);
            }

            return dialog;
        }

        public AlertDialog show() {
            AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

    }

}
