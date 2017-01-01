package chipset.lugmnotifier.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    private EditText passwordEditText;
    private final Functions functions = new Functions();
    private Activity activity;
    private CoordinatorLayout coordinatorLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_login, container,
                false);
        super.onCreateView(inflater, container, savedInstanceState);
        coordinatorLayout=(CoordinatorLayout)rootView.findViewById(R.id.login_coordinator_layout);
        this.activity = getActivity();
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button loginButton = (Button) view.findViewById(R.id.login_button);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        passwordEditText = (EditText) view.findViewById(R.id.password_edit_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            final ProgressDialog progressDialog = new ProgressDialog(activity);

            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait...");
                passwordEditText.setError(null);
                if (functions.isConnected(view.getContext())) {
                    progressDialog.show();
                    String password = passwordEditText.getText().toString();
                    if (!password.isEmpty()) {
                        ParseUser.logInInBackground("admin", password, new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (e == null) {
                                    if (user != null) {
                                        progressDialog.dismiss();
                                        startActivity(new Intent(activity, AdminActivity.class));
                                        activity.finish();
                                    } else {
                                        progressDialog.dismiss();
                                        Snackbar snackbar= Snackbar.make(coordinatorLayout, "Something went wrong, please try again", Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Snackbar snackbar= Snackbar.make(coordinatorLayout, e.getMessage(), Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        passwordEditText.setError(getString(R.string.ep));
                    }
                } else {
                    Snackbar snackbar=Snackbar.make(coordinatorLayout, "No Internet Connection",Snackbar.LENGTH_SHORT);
                    snackbar.show();
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