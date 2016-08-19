package chipset.lugmnotifier.resources;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import chipset.lugmnotifier.R;

/**
 * Created by anuraag on 8/21/16.
 */
public class NotificationAdapter extends RecyclerView.Adapter<ViewHolder> {

    ArrayList<Notifications> notificationsArrayList;
    Context context;

    public NotificationAdapter(ArrayList<Notifications> notificationsArrayList, Context context) {

        this.notificationsArrayList = notificationsArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.notification_list_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder mHolder, int position){

        final Notifications notifications = notificationsArrayList.get(position);
        mHolder.detailTextView.setText(notifications.getDetail());
        mHolder.titleTextView.setText(notifications.getTitle());
        Date eventDate = new Date(notifications.getDate());
        mHolder.dateTextView.setText(new SimpleDateFormat("kk:mm dd/MM/yyyy", Locale.ENGLISH).format(eventDate));
        mHolder.addToCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, notifications.getDate());
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                intent.putExtra(CalendarContract.Events.TITLE, notifications.getTitle());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        mHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mHolder.linearLayout.getVisibility() == View.GONE)
                    mHolder.linearLayout.setVisibility(View.VISIBLE);
                else
                    mHolder.linearLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount(){

        return notificationsArrayList.size();
    }
}
