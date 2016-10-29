package chipset.lugmnotifier.activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import chipset.lugmnotifier.R;
import chipset.lugmnotifier.resources.Functions;
import chipset.lugmnotifier.resources.NotificationAdapter;
import chipset.lugmnotifier.resources.Notifications;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

import static chipset.lugmnotifier.resources.Constants.EMAIL_MAILING;
import static chipset.lugmnotifier.resources.Constants.KEY_CLASS_NOTIFICATION;
import static chipset.lugmnotifier.resources.Constants.KEY_DATE;
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
 * Modified by: nisrulz
 * Package : chipset.lugmnotifier.activities
 * Project : LUGMNotifier
 * Date Created: 12/10/14
 * Last Modified: 29/10/16
 */
public class HomeActivity extends AppCompatActivity {
  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mDrawerToggle;

  private RecyclerView notificationsRecyclerView;
  private ArrayList<Notifications> notificationsArrayList;
  private SwipeRefreshLayout notificationSwipeRefreshLayout;
  private ProgressBar notificationLoadingProgressBar;
  private Functions functions = new Functions();
  private Realm realm;
  private boolean flag = false;
  private String value;
  private CoordinatorLayout coordinatorLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    notificationLoadingProgressBar =
        (ProgressBar) findViewById(R.id.notifications_loading_progress_bar);
    notificationLoadingProgressBar.setVisibility(View.VISIBLE);
    coordinatorLayout = (CoordinatorLayout) findViewById(R.id.home_coordinator_layout);
    notificationsArrayList = new ArrayList<>();
    try {
      flag = getIntent().getExtras().getBoolean(KEY_SHOW);
      value = getIntent().getExtras().getString(KEY_TITLE);
    } catch (Exception e) {
      e.printStackTrace();
    }
    RealmConfiguration realmConfig =
        new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
    realm = Realm.getInstance(realmConfig);
    //getNotifications();

    initNavDrawerToggel();

    notificationSwipeRefreshLayout =
        (SwipeRefreshLayout) findViewById(R.id.notificationSwipeRefreshLayout);
    notificationsRecyclerView = (RecyclerView) findViewById(R.id.notification_recycler_view);
    notificationSwipeRefreshLayout.setColorSchemeResources(R.color.peterRiver, R.color.alizarin,
        R.color.sunFlower, R.color.emerald);
    notificationSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        new FetchData().getNotifications();
      }
    });
  }

  private class FetchData {
    @SuppressWarnings("unchecked")
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
                realm.beginTransaction();
                Notifications notifications = new Notifications();
                notifications.setTitle("Sorry");
                notifications.setDetail("No Notifications");
                notifications.setDate(0);
                notifications.setImage("null");
                //realm.close();
              }
              else {
                notificationsArrayList.clear();
                for (int i = 0; i < parseObjects.size(); i++) {
                  Notifications notifications = new Notifications();
                  notifications.setTitle(parseObjects.get(i).getString(KEY_TITLE));
                  notifications.setDetail(parseObjects.get(i).getString(KEY_DETAIL));
                  notifications.setDate(parseObjects.get(i).getLong(KEY_DATE));
                  notifications.setImage(parseObjects.get(i).getString(KEY_IMAGE));
                  notificationsArrayList.add(notifications);
                  Log.d("DEBUG", notifications.getTitle());
                }
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(notificationsArrayList);
                realm.commitTransaction();
                if (flag) {
                  flag = false;
                  AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                  builder.setTitle(notificationsArrayList.get(0).getTitle());
                  builder.setMessage(notificationsArrayList.get(0).getDetail());
                  builder.setPositiveButton(android.R.string.ok, null);
                  builder.create();
                  builder.show();
                }
              }
              notificationsRecyclerView.setAdapter(
                  new NotificationAdapter(notificationsArrayList, getApplicationContext()));
            }
            else {
              Snackbar snackbar =
                  Snackbar.make(coordinatorLayout, "Something went wrong\nPlease try again later",
                      Snackbar.LENGTH_SHORT);
              snackbar.show();
            }
            display();
          }
        });
      }
      else {
        notificationLoadingProgressBar.setVisibility(View.GONE);
        notificationSwipeRefreshLayout.setRefreshing(false);
        RealmResults results = realm.where(Notifications.class).findAll();
        if (results.size() > 0) {

          notificationsArrayList = new ArrayList<>(results.subList(0, results.size()));
          display();
        }
        else {
          Snackbar snackbar =
              Snackbar.make(coordinatorLayout, "Please Connect to Internet and Try Again",
                  Snackbar.LENGTH_SHORT);
          snackbar.show();
        }
      }
    }
  }

  private void initNavDrawerToggel() {
    Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_home);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_design_support_layout);
    mDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, mDrawerLayout, R.string.app_name,
        R.string.app_name);
    mDrawerLayout.addDrawerListener(mDrawerToggle);

    NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(MenuItem item) {

            //Checking if the item is in checked state or not, if not make it in checked state
            if (item.isChecked()) {
              item.setChecked(false);
            }
            else {
              item.setChecked(true);
            }

            //Closing drawer on item click
            mDrawerLayout.closeDrawers();

            switch (item.getItemId()) {
              case R.id.nav_menu_item1: {
                functions.browserIntent(HomeActivity.this, URL_GITHUB_ORG);
                break;
              }
              case R.id.nav_menu_item2: {
                functions.browserIntent(HomeActivity.this, URL_FB_PAGE);
                break;
              }
              case R.id.nav_menu_item3: {
                functions.browserIntent(HomeActivity.this, URL_FB_GROUP);
                break;
              }
              case R.id.nav_menu_item4: {
                functions.browserIntent(HomeActivity.this, URL_TW_HANDLER);
                break;
              }
              case R.id.nav_menu_item5: {
                functions.browserIntent(HomeActivity.this, URL_WEBSITE);
                break;
              }
              case R.id.nav_menu_item6: {
                functions.browserIntent(HomeActivity.this, URL_CORE_COMM);
                break;
              }
              case R.id.nav_menu_item7: {
                functions.emailIntent(HomeActivity.this, EMAIL_MAILING, "",
                    "\n\n\n\nSent from LUG Manipal Android App");
                break;
              }
              default:
                //do nothing
                break;
            }
            return true;
          }
        });

    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
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

    // This is required to make the drawer toggle work
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }

        /*
         * if you have other menu items in your activity/toolbar
         * handle them here and return true
         */

    int id = item.getItemId();

    switch (id) {
      case R.id.action_admin:
        startActivity(new Intent(HomeActivity.this, DialogActivity.class).putExtra("Fragment", 0));
        break;
      case R.id.action_about:
        startActivity(new Intent(HomeActivity.this, DialogActivity.class).putExtra("Fragment", 1));
        break;
      default:
        // do nothing
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
      mDrawerLayout.closeDrawers();
      return;
    }
    super.onBackPressed();
  }

  private void display() {
    notificationsRecyclerView.setHasFixedSize(true);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    notificationsRecyclerView.setLayoutManager(linearLayoutManager);
    notificationsRecyclerView.setAdapter(
        new NotificationAdapter(notificationsArrayList, getApplicationContext()));
  }
}