package chipset.lugmnotifier;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

import static chipset.lugmnotifier.resources.Constants.KEY_CLASS_NOTIFICATION;
import static chipset.lugmnotifier.resources.Constants.KEY_DETAIL;
import static chipset.lugmnotifier.resources.Constants.KEY_SHOW;
import static chipset.lugmnotifier.resources.Constants.KEY_TITLE;

public class HomeActivity extends Activity {


    ListView notificationsListView;
    SwipeRefreshLayout notificationSwipeRefreshLayout;
    Functions functions = new Functions();
    String[] title = new String[1];
    String[] detail = new String[1];
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ParseAnalytics.trackAppOpened(getIntent());
        try {
            flag = getIntent().getExtras().getBoolean(KEY_SHOW);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notificationSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.notificationSwipeRefreshLayout);
        notificationsListView = (ListView) findViewById(R.id.notificationListView);
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
        new FetchData().getNotifications();


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
                            notificationsListView.setAdapter(new NotificationListViewAdapter(getApplicationContext(), title, detail));
                            notificationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                    builder.setTitle(title[i]);
                                    builder.setMessage(detail[i]);
                                    builder.setNeutralButton(android.R.string.ok, null);
                                    builder.create();
                                    builder.show();
                                }
                            });
                        } else {
                            Crouton.showText(HomeActivity.this, e.getMessage(), Style.ALERT);
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
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_admin) {
            final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            final EditText passwordEditText = new EditText(HomeActivity.this);
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Admin Panel");
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
                                            Crouton.showText(HomeActivity.this, e.getMessage(), Style.ALERT);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
