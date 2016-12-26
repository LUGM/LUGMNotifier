package chipset.lugmnotifier.resources;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import chipset.lugmnotifier.R;

/**
 * Created by anuraag on 8/21/16.
 */
public class ViewHolder extends RecyclerView.ViewHolder{

    private TextView titleTextView, detailTextView, dateTextView;
    private ImageView imageView;
    private Button addToCalendarButton;
    private CardView cardView;
    private LinearLayout linearLayout;

    public ViewHolder(View itemView) {

        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        titleTextView = (TextView) itemView.findViewById(R.id.notificationTitleTextView);
        detailTextView = (TextView) itemView.findViewById(R.id.notificationDetailTextView);
        dateTextView = (TextView) itemView.findViewById(R.id.notificationDateTextView);
        imageView = (ImageView) itemView.findViewById(R.id.notificationImageView);
        addToCalendarButton = (Button) itemView.findViewById(R.id.notificationAddToCalenderButton);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
    }

    public void setTitleText(String text) {
        titleTextView.setText(text);
    }

    public void setDetailText(String text) {
        detailTextView.setText(text);
    }

    public void setDateText(String text) {
        dateTextView.setText(text);
    }

    public void setCalendarOnClick(View.OnClickListener listener) {
        addToCalendarButton.setOnClickListener(listener);
    }

    public void setCardOnClick(View.OnClickListener listener) {
        cardView.setOnClickListener(listener);
    }

    public void setLayoutVisibility(int visibility) {
        linearLayout.setVisibility(visibility);
    }

    public int getLayoutVisibility() {
        return linearLayout.getVisibility();
    }
}
