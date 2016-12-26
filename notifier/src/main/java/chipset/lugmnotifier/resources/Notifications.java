package chipset.lugmnotifier.resources;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by anuraag on 8/21/16.
 */
public class Notifications extends RealmObject {

    @PrimaryKey
    private String title;
    private String detail;
    private String image;
    private long date;

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

}
