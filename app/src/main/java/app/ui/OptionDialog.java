package app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 *
 * @author jinbing
 */
public class OptionDialog implements AdapterView.OnItemClickListener  {

    private Dialog dialog;
    private OnItemClickListener mOnItemClickListener;

    public OptionDialog(Activity activity,
                        String title,
                        String[] text,
                        OnItemClickListener listener) {
        Context context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            context = activity;
        } else {
            context = activity.getApplicationContext();
        }
        this.mOnItemClickListener = listener;
        final ListView listview = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1,
                text);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listview.setOnItemClickListener(this);

        dialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(listview)
                .setCancelable(true)
                .create();
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(position);
        dialog.dismiss();
    }

    public static interface OnItemClickListener {

        public void onItemClick(int position);

    }
}
