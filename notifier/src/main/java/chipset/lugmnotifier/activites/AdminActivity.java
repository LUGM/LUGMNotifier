package chipset.lugmnotifier.activites;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import chipset.lugmnotifier.R;
import chipset.lugmnotifier.resources.Functions;

import static chipset.lugmnotifier.resources.Constants.KEY_CLASS_NOTIFICATION;
import static chipset.lugmnotifier.resources.Constants.KEY_DETAIL;
import static chipset.lugmnotifier.resources.Constants.KEY_IMAGE;
import static chipset.lugmnotifier.resources.Constants.KEY_TITLE;


public class AdminActivity extends AppCompatActivity {

    String title, detail, image;
    Button sendPushButton;
    EditText pushNotificationTitleEditText, pushNotificationDetailEditText, pushNotificationImageEditText;
    Functions functions = new Functions();
    ProgressDialog progressDialog;
    Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar_admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.admin_coordinator_layout);
        progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        sendPushButton = (Button) findViewById(R.id.sendPushButton);
        pushNotificationTitleEditText = (EditText) findViewById(R.id.pushNotificationTitleEditText);
        pushNotificationDetailEditText = (EditText) findViewById(R.id.pushNotificationDetailEditText);
        pushNotificationImageEditText = (EditText) findViewById(R.id.pushNotificationImageEditText);

        sendPushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (functions.isConnected(getApplicationContext())) {
                    title = pushNotificationTitleEditText.getText().toString();
                    detail = pushNotificationDetailEditText.getText().toString();
                    image = pushNotificationImageEditText.getText().toString();
                    if (image.isEmpty())
                        image = "null";
                    if (title.isEmpty())
                        pushNotificationTitleEditText.setError("Required");
                    if (detail.isEmpty())
                        pushNotificationDetailEditText.setError("Required");
                    if (!title.isEmpty() && !detail.isEmpty()) {
                        progressDialog.show();
                        pushNotificationTitleEditText.setError(null);
                        pushNotificationDetailEditText.setError(null);
                        ParseObject notification = new ParseObject(KEY_CLASS_NOTIFICATION);
                        notification.put(KEY_TITLE, title);
                        notification.put(KEY_DETAIL, detail);
                        notification.put(KEY_IMAGE, image);
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
}
