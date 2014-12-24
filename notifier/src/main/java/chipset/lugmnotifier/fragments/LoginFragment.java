package chipset.lugmnotifier.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.nispok.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import chipset.lugmnotifier.R;
import chipset.lugmnotifier.activites.AdminActivity;
import chipset.lugmnotifier.resources.Functions;

/**
 * Developer: chipset
 * Package : chipset.lugmnotifier.fragments
 * Project : LUGMNotifier
 * Date : 24/12/14
 */
public class LoginFragment extends Fragment {
    Button loginButton, cancelButton;
    EditText passwordEditText;
    Functions functions = new Functions();
    Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.activity = getActivity();
        return inflater.inflate(R.layout.fragment_login, container,
                false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = (Button) view.findViewById(R.id.login_button);
        cancelButton = (Button) view.findViewById(R.id.cancel_button);
        passwordEditText = (EditText) view.findViewById(R.id.password_edit_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            ProgressDialog progressDialog = new ProgressDialog(activity);

            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait...");
                passwordEditText.setError(null);
                if (functions.isConnected(view.getContext())) {
                    progressDialog.show();
                    String password = passwordEditText.getText().toString();
                    if (!password.isEmpty()) {
                        ParseUser.logInInBackground("admin", password, new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (e == null) {
                                    if (user != null) {
                                        progressDialog.dismiss();
                                        startActivity(new Intent(activity, AdminActivity.class));
                                        activity.finish();
                                    } else {
                                        progressDialog.dismiss();
                                        Snackbar.with(activity) // context
                                                .text("Something wen wrong, please try again") // text to display
                                                .show(activity);
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Snackbar.with(view.getContext()) // context
                                            .text(e.getMessage()) // text to display
                                            .show(activity);
                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        passwordEditText.setError(getString(R.string.ep));
                    }
                } else {
                    Snackbar.with(view.getContext()) // context
                            .text("No Internet Connection") // text to display
                            .show(activity);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

    }
}