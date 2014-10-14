package chipset.lugmnotifier.resources;

import android.content.Context;
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

    Context context;
    LayoutInflater layoutInflater;
    // ArrayList<HashMap<String, String>> dataArrayList = new ArrayList<HashMap<String, String>>();
    String[] title;
    String[] detail;

    public NotificationListViewAdapter(Context context, String[] title, String[] detail) {
        this.context = context;
        this.title = title;
        this.detail = detail;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.notification_list_item, null);
        }
        TextView notificationTitleTextView = (TextView) view.findViewById(R.id.notificationTitleTextView);
        TextView notificationDetailTextView = (TextView) view.findViewById(R.id.notificationDetailTextView);

        notificationTitleTextView.setText(title[i]);
        notificationDetailTextView.setText(detail[i]);

        return view;
    }
}
