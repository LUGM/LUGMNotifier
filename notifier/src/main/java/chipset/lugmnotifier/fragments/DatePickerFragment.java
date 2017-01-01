package chipset.lugmnotifier.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Developer: anuraag
 * Package : chipset.lugmnotifier.fragments
 * Project : LUGMNotifier
 * Date Created: 06/12/15
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String DATE_PREFS_FILE ="DatePrefsFile";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    public void onDateSet(DatePicker view, int year, int month, int day){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(DATE_PREFS_FILE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("Year",year);
        editor.putInt("Month",month);
        editor.putInt("Day",day);
        editor.apply();
        DialogFragment dialogFragment=new TimePickerFragment();
        dialogFragment.show(getFragmentManager(),"timePicker");

    }
}
