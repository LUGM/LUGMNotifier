package chipset.lugmnotifier.resources;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import static chipset.lugmnotifier.resources.Constants.APPLICATION_ID;
import static chipset.lugmnotifier.resources.Constants.CLIENT_KEY;

public class ParseInitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(APPLICATION_ID)
                .clientKey(CLIENT_KEY)
                .server("https://parseapi.back4app.com/").build());
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