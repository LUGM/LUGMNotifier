package chipset.lugmnotifier.resources;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import chipset.lugmnotifier.R;

/**
 * Developer: chipset
 * Modified by: anuraag
 * Package : chipset.lugmnotifier.resources
 * Project : LUGMNotifier
 * Date Created: 12/10/14
 * Last Modified: 06/12/15
 */
public class NotificationListViewAdapter extends BaseAdapter {

    String[] title;
    String[] detail;
    String[] image;
    long[] date;
    Context context;

    public NotificationListViewAdapter(String[] title, String[] detail, String[] image, long date[], Context context) {
        this.title = title;
        this.detail = detail;
        this.image = image;
        this.date=date;
        this.context=context;

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

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_item, viewGroup, false);
        }
        TextView notificationTitleTextView, notificationDetailTextView, notificationDateTextView;
        notificationTitleTextView = (TextView) view.findViewById(R.id.notificationTitleTextView);
        notificationDetailTextView = (TextView) view.findViewById(R.id.notificationDetailTextView);
        notificationDateTextView = (TextView) view.findViewById(R.id.notificationDateTextView);
        ImageView notificationImageView = (ImageView) view.findViewById(R.id.notificationImageView);
        Button addToCalenderButton = (Button) view.findViewById(R.id.notificationAddToCalenderButton);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());
                builder.setTitle(title[i]);
                builder.setMessage(detail[i]);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.create();
                builder.show();
            }
        });
        notificationTitleTextView.setText(title[i]);
        notificationDetailTextView.setText(detail[i]);
        if(date[i]!=0) {
            notificationDateTextView.setVisibility(View.VISIBLE);
            Date eventDate=new Date(date[i]);
            notificationDateTextView.setText(new SimpleDateFormat("kk:mm dd/MM/yyyy").format(eventDate));
        }
        if (!image[i].equals("null")) {
            notificationImageView.setVisibility(View.VISIBLE);
            Picasso.with(viewGroup.getContext()).load(image[i]).placeholder(viewGroup.getContext().getResources().getDrawable(R.drawable.ic_launcher)).into(notificationImageView);
        }
        if(date[i]!=0) {
            addToCalenderButton.setVisibility(View.VISIBLE);
        }
        addToCalenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date[i]);
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                intent.putExtra(CalendarContract.Events.TITLE, title[i]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
