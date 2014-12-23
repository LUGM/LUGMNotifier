package chipset.lugmnotifier.activites;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nispok.snackbar.Snackbar;
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


public class AdminActivity extends ActionBarActivity {

    String title, detail, image;
    Button sendPushButton;
    EditText pushNotificationTitleEditText, pushNotificationDetailEditText, pushNotificationImageEditText;
    Functions functions = new Functions();
    ProgressDialog progressDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar_admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                                    Snackbar.with(getApplicationContext()) // context
                                            .text("Saved Successfully") // text to display
                                            .show(AdminActivity.this);
                                    ParsePush push = new ParsePush();
                                    push.setChannel("");
                                    push.setMessage(title);
                                    push.sendInBackground(new SendCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                progressDialog.dismiss();
                                                Snackbar.with(getApplicationContext()) // context
                                                        .text("Pushed Successfully") // text to display
                                                        .show(AdminActivity.this);
                                            } else {
                                                Snackbar.with(getApplicationContext()) // context
                                                        .text("Something went wrong\nPlease try again later") // text to display
                                                        .show(AdminActivity.this);
                                            }
                                        }
                                    });
                                } else {
                                    Snackbar.with(getApplicationContext()) // context
                                            .text("Something went wrong\nPlease try again later") // text to display
                                            .show(AdminActivity.this);
                                }
                            }
                        });
                    }
                } else {
                    progressDialog.dismiss();
                    Snackbar.with(getApplicationContext()) // context
                            .text("No Internet Connection") // text to display
                            .show(AdminActivity.this);
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
