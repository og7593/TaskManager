package com.personal.taskmanager.fragments;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment {

  private OnDateSetListener mFragment;

  public DatePickerFragment() {
    //Default
  }

  public DatePickerFragment(Fragment callback) {
    mFragment = (OnDateSetListener) callback;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstaceState) {
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    return new DatePickerDialog(getActivity(), mFragment, year, month, day);
  }

}
