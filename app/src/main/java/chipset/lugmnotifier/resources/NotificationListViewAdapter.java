package chipset.lugmnotifier.resources;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import chipset.lugmnotifier.R;

/**
 * Created by chipset on 12/10/14.
 */
public class NotificationListViewAdapter extends BaseAdapter {

    String[] title;
    String[] detail;

    public NotificationListViewAdapter(String[] title, String[] detail) {
        this.title = title;
        this.detail = detail;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int i) {
        return title[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_item, viewGroup, false);
        }
        TextView notificationTitleTextView, notificationDetailTextView;
        notificationTitleTextView = (TextView) view.findViewById(R.id.notificationTitleTextView);
        notificationDetailTextView = (TextView) view.findViewById(R.id.notificationDetailTextView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());
                builder.setTitle(title[i]);
                builder.setMessage(detail[i]);
                builder.setNeutralButton(android.R.string.ok, null);
                builder.create();
                builder.show();
            }
        });
        notificationTitleTextView.setText(title[i]);
        notificationDetailTextView.setText(detail[i]);
        return view;
    }


}
