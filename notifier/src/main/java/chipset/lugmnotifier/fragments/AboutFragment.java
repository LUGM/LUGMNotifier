package chipset.lugmnotifier.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nispok.snackbar.Snackbar;

import chipset.lugmnotifier.R;
import chipset.lugmnotifier.resources.Functions;

import static chipset.lugmnotifier.resources.Constants.APP_PACKAGE;
import static chipset.lugmnotifier.resources.Constants.APP_VERSION;
import static chipset.lugmnotifier.resources.Constants.URL_GITHUB;
import static chipset.lugmnotifier.resources.Constants.URL_PLAY_STORE;

/**
 * Developer: chipset
 * Package : chipset.lugmnotifier.fragments
 * Project : LUGMNotifier
 * Date : 24/12/14
 */
public class AboutFragment extends Fragment {
    Button githubButton, rnrButton, sugButton, okButton;
    Functions functions = new Functions();
    Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.activity = getActivity();
        return inflater.inflate(R.layout.fragment_about, container,
                false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        githubButton = (Button) view.findViewById(R.id.github_button);
        rnrButton = (Button) view.findViewById(R.id.rnr_button);
        sugButton = (Button) view.findViewById(R.id.sug_button);
        okButton = (Button) view.findViewById(R.id.ok_button);

        githubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functions.isConnected(view.getContext())) {
                    functions.browserIntent(view.getContext(), URL_GITHUB);
                } else {
                    Snackbar.with(view.getContext()) // context
                            .text("No Internet Connection") // text to display
                            .show(activity);
                }
            }
        });

        rnrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functions.isConnected(view.getContext())) {
                    functions.browserIntent(view.getContext(), URL_PLAY_STORE);
                } else {
                    Snackbar.with(view.getContext()) // context
                            .text("No Internet Connection") // text to display
                            .show(activity);
                }
            }
        });

        sugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functions.isConnected(view.getContext())) {
                    String body = "Device: " + Build.MANUFACTURER + " " + Build.BRAND + " " + Build.DEVICE + " " + Build.MODEL + "\nApp Version: " + APP_VERSION + "\nApp Package: " + APP_PACKAGE;
                    functions.emailIntent(view.getContext(), "chipset95@gmail.com", "App Suggestion : LUG Manipal", body);
                } else {
                    Snackbar.with(view.getContext()) // context
                            .text("No Internet Connection") // text to display
                            .show(activity);
                }
            }

        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

    }
}