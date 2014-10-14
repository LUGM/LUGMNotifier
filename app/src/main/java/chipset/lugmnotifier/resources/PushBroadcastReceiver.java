package chipset.lugmnotifier.resources;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import chipset.lugmnotifier.HomeActivity;

import static chipset.lugmnotifier.resources.Constants.KEY_SHOW;
public class PushBroadcastReceiver extends ParsePushBroadcastReceiver {

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.e("Push", "Clicked");
        context.startActivity(new Intent(context, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra(KEY_SHOW, true));
    }
}
