package com.personal.taskmanager.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;

import java.util.Calendar;

@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment {
  
  private Fragment mFragment;
  
  public TimePickerFragment() {
    
  }
  
  public TimePickerFragment(Fragment callback) {
    mFragment = callback;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    //Use current time is default values
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    return new TimePickerDialog(getActivity(),
                                (OnTimeSetListener) mFragment,
                                hour,
                                minute,
                                DateFormat.is24HourFormat(getActivity()));
  }
  
}
