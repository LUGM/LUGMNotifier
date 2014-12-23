package chipset.lugmnotifier.resources;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import chipset.lugmnotifier.activites.HomeActivity;

import static chipset.lugmnotifier.resources.Constants.KEY_SHOW;
import static chipset.lugmnotifier.resources.Constants.KEY_TITLE;

public class PushBroadcastReceiver extends ParsePushBroadcastReceiver {

    String value = null;

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.e("Push", "Clicked");
        context.startActivity(new Intent(context, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra(KEY_SHOW, true).putExtra(KEY_TITLE, value));
    }
}
