package chipset.lugmnotifier.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    private final Functions functions = new Functions();
    private Activity activity;
    private CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        super.onCreateView(inflater, container, savedInstanceState);
        this.activity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_about, container, false);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.about_coordinator_layout);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button githubButton = (Button) view.findViewById(R.id.github_button);
        Button rnrButton = (Button) view.findViewById(R.id.rnr_button);
        Button sugButton = (Button) view.findViewById(R.id.sug_button);
        Button okButton = (Button) view.findViewById(R.id.ok_button);

        githubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functions.isConnected(view.getContext())) {
                    functions.browserIntent(getActivity(), URL_GITHUB);
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.no_internet_con, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });

        rnrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functions.isConnected(view.getContext())) {
                    functions.browserIntent(getActivity(), URL_PLAY_STORE);
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.no_internet_con, Snackbar.LENGTH_SHORT);
                    snackbar.show();
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
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.no_internet_con, Snackbar.LENGTH_SHORT);
                    snackbar.show();
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