package chipset.lugmnotifier.resources;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Functions {

    /*
     * Function to get connection status
     */
    public boolean isConnected(Context context) {
        boolean isConnected;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = (activeNetwork != null)
                && (activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }

    /*
     * Function to hide keyboard
     */
    public void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*
     * Function to show notification
     */
    public void showNotification(String title, String subtitle, int icon,
                                 Intent resultIntent, Context context, NotificationManager mNotifyMgr) {

        Uri soundUri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingResultIntent = PendingIntent.getActivity(context,
                0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(title).setContentText(subtitle)
                .setSmallIcon(icon).setContentIntent(pendingResultIntent)
                .setSound(soundUri).setAutoCancel(true).build();
        mNotifyMgr = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.cancelAll();
        mNotifyMgr.notify(0, mBuilder);
    }

    /*
     * Fuction to show notification without sound
     */
    public void showNotificationNoSound(String title, String subtitle,
                                        int icon, Intent resultIntent, Context context,
                                        NotificationManager mNotifyMgr) {

        PendingIntent pendingResultIntent = PendingIntent.getActivity(context,
                0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(title).setContentText(subtitle)
                .setSmallIcon(icon).setContentIntent(pendingResultIntent)
                .setAutoCancel(true).build();
        mNotifyMgr = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.cancelAll();
        mNotifyMgr.notify(0, mBuilder);
    }

	/*
     * Function to s SharedPreferences data
	 */

    public void putSharedPrefs(Context context, String preferenceName,
                               boolean val) {
        SharedPreferences pref = context
                .getSharedPreferences(preferenceName, 0); // 0 - for private
        // mode
        Editor editor = pref.edit();
        editor.clear();
        editor.putBoolean(preferenceName, val);
        editor.commit();
    }

    public void putSharedPrefs(Context context, String preferenceName,
                               int val) {
        SharedPreferences pref = context
                .getSharedPreferences(preferenceName, 0); // 0 - for private
        // mode
        Editor editor = pref.edit();
        editor.clear();
        editor.putInt(preferenceName, val);
        editor.commit();
    }

    public void putSharedPrefs(Context context, String preferenceName,
                               String val) {
        SharedPreferences pref = context
                .getSharedPreferences(preferenceName, 0); // 0 - for private
        // mode
        Editor editor = pref.edit();
        editor.clear();
        editor.putString(preferenceName, val);
        editor.commit();
    }

	/*
     * Function to get SharedPreferences data
	 */

    public boolean getSharedPreferencesBoolean(Context context, String preferenceName) {
        SharedPreferences pref = context
                .getSharedPreferences(preferenceName, 0); // 0 - for private
        // mode

        boolean val = pref.getBoolean(preferenceName, true);
        return val;

    }

    public int getSharedPreferencesInt(Context context, String preferenceName) {
        SharedPreferences pref = context
                .getSharedPreferences(preferenceName, 0); // 0 - for private
        // mode

        int val = pref.getInt(preferenceName, 0);
        return val;

    }

    public String getSharedPreferencesString(Context context, String preferenceName) {
        SharedPreferences pref = context
                .getSharedPreferences(preferenceName, 0); // 0 - for private
        // mode

        String val = pref.getString(preferenceName, "null");
        return val;

    }

    /*
     * Function to start email intent
     */
    public void emailIntent(Context context, String to, String subject, String body) {
        StringBuilder builder = new StringBuilder("mailto:" + Uri.encode(to));
        if (subject != null) {
            builder.append("?subject=" + subject);
            if (body != null) {
                builder.append("&body=" + body);
            }
        }
        String uri = builder.toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
        context.startActivity(intent);
    }

    /*
     * Function to start browser intent
     */
    public void broswerIntent(Context context, String url) {
        context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
