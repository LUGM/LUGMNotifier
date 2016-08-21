package chipset.lugmnotifier.resources;

import android.content.Context;

import io.realm.RealmObject;

/**
 * Created by anuraag on 8/21/16.
 */
public class Notifications extends RealmObject {

    String title;
    String detail;
    String image;
    long date;
    Context context;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
