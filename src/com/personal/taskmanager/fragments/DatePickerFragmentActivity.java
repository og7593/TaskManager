package com.personal.taskmanager.fragments;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;

public class DatePickerFragmentActivity extends DialogFragment {

  private Activity mActivity;
  private OnDateSetListener mListener;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mActivity = activity;
    mListener = (OnDateSetListener) activity;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current time as the default values for the picker
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    return new DatePickerDialog(mActivity, mListener, year, month, day);
  }
}
