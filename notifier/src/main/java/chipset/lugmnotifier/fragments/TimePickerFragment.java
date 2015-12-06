package chipset.lugmnotifier.fragments;

import android.app.Dialog;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import chipset.lugmnotifier.activites.AdminActivity;

/**
 * Developer: anuraag
 * Package : chipset.lugmnotifier.fragments
 * Project : LUGMNotifier
 * Date Created: 06/12/15
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    Calendar calendar=Calendar.getInstance();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(DatePickerFragment.PrefsFile, 0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("Hour",hourOfDay);
        editor.putInt("Minutes",minutes);
        editor.apply();
        AdminActivity.pushNotificationDateEditText.setText(new Date(returnCalenderMillis()).toString());
    }
    public long returnCalenderMillis(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(DatePickerFragment.PrefsFile,0);
        int year=sharedPreferences.getInt("Year", 0);
        int month=sharedPreferences.getInt("Month",1);
        int day=sharedPreferences.getInt("Day",2);
        int hour=sharedPreferences.getInt("Hour",3);
        int minutes=sharedPreferences.getInt("Minutes",4);
        calendar.clear();
        calendar.set(year, month, day, hour, minutes);
        return calendar.getTimeInMillis();
    }
}

