package chipset.lugmnotifier.chromecustomtabs;

/**
 * Created by saketh on 20/9/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import chipset.lugmnotifier.activites.WebViewActivity;

/**
 * A Fallback that opens a Webview when Custom Tabs is not available
 */
public class WebViewFallback implements CustomTabActivityHelper.CustomTabFallback {
    @Override
    public void openUri(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_URL, uri.toString());
        activity.startActivity(intent);
    }
}