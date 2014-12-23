package chipset.lugmnotifier.activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.nispok.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import chipset.lugmnotifier.R;
import chipset.lugmnotifier.resources.Functions;
import chipset.lugmnotifier.resources.NotificationListViewAdapter;

import static chipset.lugmnotifier.resources.Constants.APP_PACKAGE;
import static chipset.lugmnotifier.resources.Constants.APP_VERSION;
import static chipset.lugmnotifier.resources.Constants.EMAIL_MAILING;
import static chipset.lugmnotifier.resources.Constants.KEY_CLASS_NOTIFICATION;
import static chipset.lugmnotifier.resources.Constants.KEY_DETAIL;
import static chipset.lugmnotifier.resources.Constants.KEY_IMAGE;
import static chipset.lugmnotifier.resources.Constants.KEY_SHOW;
import static chipset.lugmnotifier.resources.Constants.KEY_TITLE;
import static chipset.lugmnotifier.resources.Constants.URL_CORE_COMM;
import static chipset.lugmnotifier.resources.Constants.URL_FB_GROUP;
import static chipset.lugmnotifier.resources.Constants.URL_FB_PAGE;
import static chipset.lugmnotifier.resources.Constants.URL_GITHUB;
import static chipset.lugmnotifier.resources.Constants.URL_GITHUB_ORG;
import static chipset.lugmnotifier.resources.Constants.URL_PLAY_STORE;
import static chipset.lugmnotifier.resources.Constants.URL_TW_HANDLER;
import static chipset.lugmnotifier.resources.Constants.URL_WEBSITE;

public class HomeActivity extends ActionBarActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ListView notificationListView, drawerListView;
    SwipeRefreshLayout notificationSwipeRefreshLayout;
    ProgressBar notificationLoadingProgressBar;
    Functions functions = new Functions();
    String[] title = new String[1];
    String[] detail = new String[1];
    String[] image = new String[1];
    boolean flag = false;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        notificationLoadingProgressBar = (ProgressBar) findViewById(R.id.notifications_loading_progress_bar);
        notificationLoadingProgressBar.setVisibility(View.VISIBLE);
        try {
            flag = getIntent().getExtras().getBoolean(KEY_SHOW);
            value = getIntent().getExtras().getString(KEY_TITLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        String[] val = {"GitHub Organisation", "Facebook Page", "Facebook Group", "Twitter", "Website", "Core Committee", "Mailing List"};
        notificationSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.notificationSwipeRefreshLayout);
        notificationListView = (ListView) findViewById(R.id.notificationListView);
        drawerListView = (ListView) findViewById(R.id.drawer_list);
        drawerListView.setAdapter(new ArrayAdapter<String>(HomeActivity.this, R.layout.navigation_drawer_list_item, R.id.navigation_drawer_item, val));

        notificationSwipeRefreshLayout.setColorSchemeResources(R.color.peterRiver, R.color.alizarin, R.color.sunFlower, R.color.emerald);
        notificationSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchData().getNotifications();
            }
        });

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: {
                        functions.browserIntent(getApplicationContext(), URL_GITHUB_ORG);
                        break;
                    }
                    case 1: {
                        functions.browserIntent(getApplicationContext(), URL_FB_PAGE);
                        break;
                    }
                    case 2: {
                        functions.browserIntent(getApplicationContext(), URL_FB_GROUP);
                        break;
                    }
                    case 3: {
                        functions.browserIntent(getApplicationContext(), URL_TW_HANDLER);
                        break;
                    }
                    case 4: {
                        functions.browserIntent(getApplicationContext(), URL_WEBSITE);
                        break;
                    }
                    case 5: {
                        functions.browserIntent(getApplicationContext(), URL_CORE_COMM);
                        break;
                    }
                    case 6: {
                        functions.emailIntent(getApplicationContext(), EMAIL_MAILING, "", "\n\n\n\nSent from LUG Manipal Android App");
                        break;
                    }
                }
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
                        notificationLoadingProgressBar.setVisibility(View.GONE);
                        if (e == null) {
                            if (parseObjects.size() == 0) {
                                title = new String[1];
                                detail = new String[1];
                                title[0] = "Sorry";
                                detail[0] = "No notifications";
                                image[0] = "null";
                            } else {
                                title = new String[parseObjects.size()];
                                detail = new String[parseObjects.size()];
                                image = new String[parseObjects.size()];
                                for (int i = 0; i < parseObjects.size(); i++) {
                                    title[i] = parseObjects.get(i).getString(KEY_TITLE);
                                    detail[i] = parseObjects.get(i).getString(KEY_DETAIL);
                                    image[i] = parseObjects.get(i).getString(KEY_IMAGE);
                                }
                                if (flag) {
                                    flag = false;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                    builder.setTitle(title[0]);
                                    builder.setMessage(detail[0]);
                                    builder.setPositiveButton(android.R.string.ok, null);
                                    builder.create();
                                    builder.show();
                                }

                            }
                            notificationListView.setAdapter(new NotificationListViewAdapter(title, detail, image));
                        } else {
                            Snackbar.with(getApplicationContext()) // context
                                    .text("Something went wrong\nPlease try again later") // text to display
                                    .show(HomeActivity.this);
                        }
                    }
                });
            } else {
                notificationLoadingProgressBar.setVisibility(View.GONE);
                notificationSwipeRefreshLayout.setRefreshing(false);
                Snackbar.with(getApplicationContext()) // context
                        .text("No Internet Connection") // text to display
                        .show(HomeActivity.this);
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
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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
                                                    Snackbar.with(getApplicationContext()) // context
                                                            .text("Incorrect Password") // text to display
                                                            .show(HomeActivity.this);
                                                }
                                            } else {
                                                progressDialog.dismiss();
                                                Snackbar.with(getApplicationContext()) // context
                                                        .text("Incorrect Password") // text to display
                                                        .show(HomeActivity.this);
                                            }
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Snackbar.with(getApplicationContext()) // context
                                            .text("Incorrect Password") // text to display
                                            .show(HomeActivity.this);
                                }
                            }
                        }

                );
                builder.setNeutralButton("CANCEL", null);
                builder.create();
                builder.show();
            } else {
                Snackbar.with(getApplicationContext()) // context
                        .text("No Internet Connection") // text to display
                        .show(HomeActivity.this);
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
                builder.setNegativeButton("RATE & REVIEW THE APP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (functions.isConnected(HomeActivity.this)) {
                            functions.browserIntent(HomeActivity.this, URL_PLAY_STORE);
                        } else {
                            Snackbar.with(getApplicationContext()) // context
                                    .text("No Internet Connection") // text to display
                                    .show(HomeActivity.this);
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
                            Snackbar.with(getApplicationContext()) // context
                                    .text("No Internet Connection") // text to display
                                    .show(HomeActivity.this);
                        }
                    }
                });
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setView(githubButton);
                builder.create();
                builder.show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}