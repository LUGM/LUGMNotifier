package chipset.lugmnotifier.activites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import chipset.lugmnotifier.R;
import chipset.lugmnotifier.fragments.AboutFragment;
import chipset.lugmnotifier.fragments.LoginFragment;

/**
 * Developer: chipset
 * Package : chipset.lugmnotifier.activites
 * Project : LUGMNotifier
 * Date : 24/12/14
 */
public class DialogActivity extends AppCompatActivity {
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        int f = getIntent().getIntExtra("Fragment", 1);
        switch (f) {
            case 0: {
                fragment = new LoginFragment();
                break;
            }
            case 1:
            default: {
                fragment = new AboutFragment();
                break;
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
    }
}
