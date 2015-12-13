package chipset.lugmnotifier.activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.nispok.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import chipset.lugmnotifier.R;
import chipset.lugmnotifier.chromecustomtabs.CustomTabActivityHelper;
import chipset.lugmnotifier.chromecustomtabs.WebViewFallback;
import chipset.lugmnotifier.resources.Functions;
import chipset.lugmnotifier.resources.NotificationListViewAdapter;

import static chipset.lugmnotifier.resources.Constants.EMAIL_MAILING;
import static chipset.lugmnotifier.resources.Constants.KEY_CLASS_NOTIFICATION;
import static chipset.lugmnotifier.resources.Constants.KEY_DETAIL;
import static chipset.lugmnotifier.resources.Constants.KEY_IMAGE;
import static chipset.lugmnotifier.resources.Constants.KEY_SHOW;
import static chipset.lugmnotifier.resources.Constants.KEY_TITLE;
import static chipset.lugmnotifier.resources.Constants.URL_CORE_COMM;
import static chipset.lugmnotifier.resources.Constants.URL_FB_GROUP;
import static chipset.lugmnotifier.resources.Constants.URL_FB_PAGE;
import static chipset.lugmnotifier.resources.Constants.URL_GITHUB_ORG;
import static chipset.lugmnotifier.resources.Constants.URL_TW_HANDLER;
import static chipset.lugmnotifier.resources.Constants.URL_WEBSITE;

/**
 * Developer: chipset
 * Package : chipset.lugmnotifier.resources
 * Project : LUGMNotifier
 * Date : 12/10/14
 */
public class HomeActivity extends ActionBarActivity {
    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
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
        mToolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        notificationLoadingProgressBar = (ProgressBar) findViewById(R.id.notifications_loading_progress_bar);
        notificationLoadingProgressBar.setVisibility(View.VISIBLE);
        try {
            flag = getIntent().getExtras().getBoolean(KEY_SHOW);
            value = getIntent().getExtras().getString(KEY_TITLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        String[] val = {"GitHub Organisation", "Facebook Page", "Facebook Group", "Twitter", "Website", "Core Committee", "Mailing List"};
        notificationSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.notificationSwipeRefreshLayout);
        notificationListView = (ListView) findViewById(R.id.notificationListView);
        drawerListView = (ListView) findViewById(R.id.drawer_list);
        drawerListView.setAdapter(new ArrayAdapter<>(HomeActivity.this, R.layout.navigation_drawer_list_item, R.id.navigation_drawer_item, val));

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
                String URL = null;
                switch (i) {
                    case 0: {
                        //functions.browserIntent(getApplicationContext(), URL_GITHUB_ORG);
                        URL = URL_GITHUB_ORG;
                        break;
                    }
                    case 1: {
                        //functions.browserIntent(getApplicationContext(), URL_FB_PAGE);
                        URL  = URL_FB_PAGE;
                        break;
                    }
                    case 2: {
                        //functions.browserIntent(getApplicationContext(), URL_FB_GROUP);
                        URL = URL_FB_GROUP;
                        break;
                    }
                    case 3: {
                        //functions.browserIntent(getApplicationContext(), URL_TW_HANDLER);
                        URL = URL_TW_HANDLER;
                        break;
                    }
                    case 4: {
                        //functions.browserIntent(getApplicationContext(), URL_WEBSITE);
                        URL  = URL_WEBSITE;
                        break;
                    }
                    case 5: {
                        //functions.browserIntent(getApplicationContext(), URL_CORE_COMM);
                        URL  = URL_CORE_COMM;
                        break;
                    }
                    case 6: {
                        functions.emailIntent(getApplicationContext(), EMAIL_MAILING, "", "\n\n\n\nSent from LUG Manipal Android App");
                        break;
                    }
                }
                if(URL != null) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setStartAnimations(getApplicationContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                    // vice versa
                    builder.setExitAnimations(getApplicationContext(), R.anim.slide_in_left, R.anim.slide_out_right);
                    CustomTabsIntent customTabsIntent = builder.build();
                    CustomTabActivityHelper.openCustomTab(
                            HomeActivity.this, customTabsIntent, Uri.parse(URL), new WebViewFallback());
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
                            Log.e("Parse error",e.getMessage());
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.action_admin) {
            startActivity(new Intent(HomeActivity.this, DialogActivity.class).putExtra("Fragment", 0));
        } else if (id == R.id.action_about) {
            startActivity(new Intent(HomeActivity.this, DialogActivity.class).putExtra("Fragment", 1));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}