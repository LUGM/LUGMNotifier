package chipset.lugmnotifier.activites;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.design.widget.Snackbar;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import java.util.Calendar;
import java.util.Date;

import chipset.lugmnotifier.R;
import chipset.lugmnotifier.fragments.DatePickerFragment;
import chipset.lugmnotifier.resources.Functions;

import static chipset.lugmnotifier.resources.Constants.KEY_CLASS_NOTIFICATION;
import static chipset.lugmnotifier.resources.Constants.KEY_DATE;
import static chipset.lugmnotifier.resources.Constants.KEY_DETAIL;
import static chipset.lugmnotifier.resources.Constants.KEY_IMAGE;
import static chipset.lugmnotifier.resources.Constants.KEY_TITLE;

/**
 * Developer: chipset
 * Modified by: anuraag
 * Package : chipset.lugmnotifier.activities
 * Project : LUGMNotifier
 * Date Created: 12/10/14
 * Last Modified: 06/12/15
 */

public class AdminActivity extends AppCompatActivity {

    private String title;
    private String detail;
    private String image;
    private String date;
    private final Calendar calendar = Calendar.getInstance();
    private EditText pushNotificationTitleEditText;
    private EditText pushNotificationDetailEditText;
    private EditText pushNotificationImageEditText;
    private EditText  pushNotificationDateEditText;
    private final Functions functions = new Functions();
    private ProgressDialog progressDialog;
    private Long timeSinceEpoch;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_admin);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.admin_coordinator_layout);
        progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        Button sendPushButton = (Button) findViewById(R.id.sendPushButton);
        pushNotificationTitleEditText = (EditText) findViewById(R.id.pushNotificationTitleEditText);
        pushNotificationDetailEditText = (EditText) findViewById(R.id.pushNotificationDetailEditText);
        pushNotificationImageEditText = (EditText) findViewById(R.id.pushNotificationImageEditText);
        pushNotificationDateEditText = (EditText) findViewById(R.id.pushNotificationDateEditText);
        pushNotificationDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "datePicker");
                pushNotificationDateEditText.setText(new Date(returnCalenderMillis()).toString());
            }
        });


        sendPushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (functions.isConnected(getApplicationContext())) {
                    title = pushNotificationTitleEditText.getText().toString();
                    detail = pushNotificationDetailEditText.getText().toString();
                    image = pushNotificationImageEditText.getText().toString();
                    timeSinceEpoch = returnCalenderMillis();
                    date = pushNotificationDateEditText.getText().toString();
                    if (image.isEmpty())
                        image = "null";
                    if (title.isEmpty())
                        pushNotificationTitleEditText.setError(getString(R.string.ERROR_REQ));
                    if (detail.isEmpty())
                        pushNotificationDetailEditText.setError(getString(R.string.ERROR_REQ));
                    if (date.isEmpty())
                        pushNotificationDateEditText.setError(getString(R.string.ERROR_REQ));
                    if (!title.isEmpty() && !detail.isEmpty() && !date.isEmpty()) {
                        progressDialog.show();
                        pushNotificationTitleEditText.setError(null);
                        pushNotificationDetailEditText.setError(null);
                        ParseObject notification = new ParseObject(KEY_CLASS_NOTIFICATION);
                        notification.put(KEY_TITLE, title);
                        notification.put(KEY_DETAIL, detail);
                        notification.put(KEY_IMAGE, image);
                        notification.put(KEY_DATE,timeSinceEpoch);
                        notification.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Snackbar snackbar=Snackbar.make(coordinatorLayout, "Saved Successfully", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    ParsePush push = new ParsePush();
                                    push.setChannel("");
                                    push.setMessage(title);
                                    push.sendInBackground(new SendCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                progressDialog.dismiss();
                                                Snackbar snackbar=Snackbar.make(coordinatorLayout, "Pushed Successfully",Snackbar.LENGTH_SHORT);
                                                snackbar.show();
                                            } else {
                                                Snackbar snackbar=Snackbar.make(coordinatorLayout, "Something went wrong\nPlease try again later",Snackbar.LENGTH_SHORT);
                                                snackbar.show();
                                            }
                                        }
                                    });
                                } else {
                                    Snackbar snackbar=Snackbar.make(coordinatorLayout, "Something went wrong\nPlease try again later",Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    Log.e("com.parse.push","Error",e);
                                }
                            }
                        });
                    }
                } else {
                    progressDialog.dismiss();
                    Snackbar snackbar=Snackbar.make(coordinatorLayout, "No Internet Connection",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        ParseUser.logOut();
        super.onPause();
    }
    private long returnCalenderMillis(){
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(DatePickerFragment.DATE_PREFS_FILE,0);
        int year=sharedPreferences.getInt("Year",0);
        int month=sharedPreferences.getInt("Month",1);
        int day=sharedPreferences.getInt("Day",2);
        int hour=sharedPreferences.getInt("Hour",3);
        int minutes=sharedPreferences.getInt("Minutes",4);
        calendar.clear();
        calendar.set(year, month, day, hour, minutes);
        return calendar.getTimeInMillis();
    }

    public void setDateInPushNotificationDateEditText(String info){
        pushNotificationDateEditText.setText(info);
    }
}
