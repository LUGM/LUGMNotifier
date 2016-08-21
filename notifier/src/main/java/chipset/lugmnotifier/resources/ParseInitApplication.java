package chipset.lugmnotifier.resources;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class ParseInitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(getApplicationContext(),"Ba1WfrHmjuzjNOg4Q0rsA8FnbPPHTLRRsbbtbZHo","7SvjoweOT5lbm82A5tiDCvITL84uyontsfW7Equx");
        new Parse.Configuration.Builder(this)
                .applicationId("Ba1WfrHmjuzjNOg4Q0rsA8FnbPPHTLRRsbbtbZHo")
                .clientKey("7SvjoweOT5lbm82A5tiDCvITL84uyontsfW7Equx")
                .server("https://parseapi.back4app.com/").build();
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

}