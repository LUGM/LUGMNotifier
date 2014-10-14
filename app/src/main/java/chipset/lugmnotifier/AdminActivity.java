package chipset.lugmnotifier;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import chipset.lugmnotifier.resources.Functions;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static chipset.lugmnotifier.resources.Constants.KEY_CLASS_NOTIFICATION;
import static chipset.lugmnotifier.resources.Constants.KEY_DETAIL;
import static chipset.lugmnotifier.resources.Constants.KEY_TITLE;


public class AdminActivity extends Activity {

    Button sendPushButton;
    EditText pushNotificationTitleEditText, pushNotificationDetailEditText;
    Functions functions = new Functions();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        sendPushButton = (Button) findViewById(R.id.sendPushButton);
        pushNotificationTitleEditText = (EditText) findViewById(R.id.pushNotificationTitleEditText);
        pushNotificationDetailEditText = (EditText) findViewById(R.id.pushNotificationDetailEditText);

        sendPushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if (functions.isConnected(getApplicationContext())) {
                    String title = pushNotificationTitleEditText.getText().toString();
                    String detail = pushNotificationDetailEditText.getText().toString();
                    if (title.isEmpty())
                        pushNotificationTitleEditText.setError("Required");
                    if (detail.isEmpty())
                        pushNotificationDetailEditText.setError("Required");
                    else {
                        pushNotificationTitleEditText.setError(null);
                        pushNotificationDetailEditText.setError(null);
                        ParseObject notification = new ParseObject(KEY_CLASS_NOTIFICATION);
                        notification.put(KEY_TITLE, title);
                        notification.put(KEY_DETAIL, detail);
                        notification.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Crouton.showText(AdminActivity.this, "Saved Successfully", Style.CONFIRM);
                                } else {
                                    Crouton.showText(AdminActivity.this, e.getMessage(), Style.ALERT);
                                }
                            }
                        });
                        ParsePush push = new ParsePush();
                        push.setChannel("");
                        push.setMessage(title);
                        push.sendInBackground(new SendCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    progressDialog.dismiss();
                                    Crouton.showText(AdminActivity.this, "Pushed Successfully", Style.CONFIRM);
                                } else {
                                    Crouton.showText(AdminActivity.this, e.getMessage(), Style.ALERT);
                                }
                            }
                        });
                    }
                } else {
                    progressDialog.dismiss();
                    Crouton.showText(AdminActivity.this, "No internet Connection", Style.ALERT);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        ParseUser.logOut();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
