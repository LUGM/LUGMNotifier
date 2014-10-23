package chipset.lugmnotifier;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.duncavage.swipetorefresh.widget.SwipeRefreshLayout;

import java.util.List;

import chipset.lugmnotifier.resources.Functions;
import chipset.lugmnotifier.resources.NotificationListViewAdapter;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static chipset.lugmnotifier.resources.Constants.APP_PACKAGE;
import static chipset.lugmnotifier.resources.Constants.APP_VERSION;
import static chipset.lugmnotifier.resources.Constants.KEY_CLASS_NOTIFICATION;
import static chipset.lugmnotifier.resources.Constants.KEY_DETAIL;
import static chipset.lugmnotifier.resources.Constants.KEY_SHOW;
import static chipset.lugmnotifier.resources.Constants.KEY_TITLE;
import static chipset.lugmnotifier.resources.Constants.URL_GITHUB;
import static chipset.lugmnotifier.resources.Constants.URL_PLAY_STORE;

public class HomeActivity extends ActionBarActivity {


    ListView notificationListView;
    SwipeRefreshLayout notificationSwipeRefreshLayout;
    Functions functions = new Functions();
    String[] title = new String[1];
    String[] detail = new String[1];
    boolean flag = false;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ParseAnalytics.trackAppOpened(getIntent());
        try {
            flag = getIntent().getExtras().getBoolean(KEY_SHOW);
            value = getIntent().getExtras().getString(KEY_TITLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notificationSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.notificationSwipeRefreshLayout);
        notificationListView = (ListView) findViewById(R.id.notificationListView);

        notificationSwipeRefreshLayout.setColorScheme(R.color.alizarin, R.color.emerald, R.color.peterRiver, R.color.sunFlower);
        notificationSwipeRefreshLayout.setActionBarSwipeIndicatorText(R.string.swipe_to_refresh);
        notificationSwipeRefreshLayout.setActionBarSwipeIndicatorRefreshingText(R.string.loading);
        notificationSwipeRefreshLayout.setActionBarSwipeIndicatorBackgroundColor(
                getResources().getColor(R.color.black));
        notificationSwipeRefreshLayout.setActionBarSwipeIndicatorTextColor(
                getResources().getColor(R.color.clouds));
        notificationSwipeRefreshLayout.setActionBarSwipeIndicatorRefreshingTextColor(
                getResources().getColor(R.color.clouds));
        notificationSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchData().getNotifications();
            }
        });


    }

    private class FetchData {

        public void getNotifications() {
            if (functions.isConnected(getApplicationContext())) {
                notificationSwipeRefreshLayout.setRefreshing(true);
                ParseQuery<ParseObject> query = ParseQuery.getQuery(KEY_CLASS_NOTIFICATION);
                query.addDescendingOrder("createdAt");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        notificationSwipeRefreshLayout.setRefreshing(false);
                        if (e == null) {
                            if (parseObjects.size() == 0) {
                                title = new String[1];
                                detail = new String[1];
                                title[0] = "Sorry";
                                detail[0] = "No notifications";
                            } else {
                                title = new String[parseObjects.size()];
                                detail = new String[parseObjects.size()];
                                for (int i = 0; i < parseObjects.size(); i++) {
                                    title[i] = parseObjects.get(i).getString(KEY_TITLE);
                                    detail[i] = parseObjects.get(i).getString(KEY_DETAIL);
                                }
                                if (flag == true) {
                                    flag = false;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                    builder.setTitle(title[0]);
                                    builder.setMessage(detail[0]);
                                    builder.setNeutralButton(android.R.string.ok, null);
                                    builder.create();
                                    builder.show();
                                }

                            }
                            notificationListView.setAdapter(new NotificationListViewAdapter(title, detail));
                        } else {
                            Crouton.showText(HomeActivity.this, "Something went wrong\nPlease try again ", Style.ALERT);
                        }
                    }
                });
            } else {
                notificationSwipeRefreshLayout.setRefreshing(false);
                Crouton.showText(HomeActivity.this, "No internet connection", Style.ALERT);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        new FetchData().getNotifications();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_admin) {
            if (functions.isConnected(HomeActivity.this)) {
                final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.setCancelable(false);
                final EditText passwordEditText = new EditText(HomeActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30);
                layoutParams.gravity = Gravity.CENTER_VERTICAL;
                layoutParams.setMargins(30, 0, 30, 0);
                passwordEditText.setLayoutParams(layoutParams);
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Send a push");
                builder.setMessage("Enter the admin password");
                builder.setView(passwordEditText);
                builder.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.show();
                                String password = passwordEditText.getText().toString();
                                if (!password.isEmpty()) {
                                    ParseUser.logInInBackground("admin", password, new LogInCallback() {
                                        public void done(ParseUser user, ParseException e) {
                                            if (e == null) {
                                                if (user != null) {
                                                    progressDialog.dismiss();
                                                    startActivity(new Intent(HomeActivity.this, AdminActivity.class));
                                                } else {
                                                    progressDialog.dismiss();
                                                    Crouton.showText(HomeActivity.this, "Incorrect Password", Style.ALERT);
                                                }
                                            } else {
                                                progressDialog.dismiss();
                                                Crouton.showText(HomeActivity.this, "Incorrect Password", Style.ALERT);
                                            }
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Crouton.showText(HomeActivity.this, "Incorrect Password", Style.ALERT);
                                }
                            }
                        }

                );
                builder.setNeutralButton("CANCEL", null);
                builder.create();
                builder.show();
            } else {
                Crouton.showText(HomeActivity.this, "No internet Connection", Style.ALERT);
            }
        } else if (id == R.id.action_about) {
            if (functions.isConnected(HomeActivity.this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                Button githubButton = new Button(HomeActivity.this);
                githubButton.setText(Html.fromHtml(URL_GITHUB));
                githubButton.setTextColor(getResources().getColor(R.color.peterRiver));
                githubButton.setLinkTextColor(getResources().getColor(R.color.peterRiver));
                githubButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                githubButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(URL_GITHUB)));
                    }
                });
                builder.setTitle("About");
                builder.setMessage("Get notifications and details about all the workshops and events being conducted by Linux Users Group (LUG), Manipal. No registration required.\n\nDeveloped and maintained by CHIPSET\n\nSource code for the app can be found at:");
                builder.setNegativeButton("RATE THE APP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (functions.isConnected(HomeActivity.this)) {
                            functions.browerIntent(HomeActivity.this, URL_PLAY_STORE);
                        } else {
                            Crouton.showText(HomeActivity.this, "No internet Connection", Style.ALERT);
                        }
                    }

                });
                builder.setNeutralButton("SUGGESTIONS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (functions.isConnected(HomeActivity.this)) {
                            String body = "Device: " + Build.MANUFACTURER + " " + Build.BRAND + " " + Build.DEVICE + " " + Build.MODEL + "\nApp Version: " + APP_VERSION + "\nApp Package: " + APP_PACKAGE;
                            functions.emailIntent(HomeActivity.this, "chipset95@gmail.com", "App Suggestion : LUG Manipal", body);
                        } else {
                            Crouton.showText(HomeActivity.this, "No internet Connection", Style.ALERT);
                        }
                    }
                });
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setView(githubButton);
                builder.create();
                builder.show();
            }
//        } else if (id == R.id.action_open_licenses) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//            builder.setTitle("Open Source Licenses");
//            builder.setMessage(message);

        }
        return super.onOptionsItemSelected(item);
    }
}
