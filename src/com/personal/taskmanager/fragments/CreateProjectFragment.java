package com.personal.taskmanager.fragments;

import org.json.JSONArray;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.personal.taskmanager.R;
import com.personal.taskmanager.parseobjects.Project;
import com.personal.taskmanager.utilities.BCrypt;

public class CreateProjectFragment extends Fragment 
implements OnClickListener, OnDateSetListener, OnTimeSetListener {

  //Project data
  private Project mNewProject;
  private String mProjectName;
  private String mUID;
  private String mPassword;
  private String mDescription;
  private String mDueDate;
  private int mMinutePicker;
  private int mHourPicker;
  private int mYear;
  private int mMonth;
  private int mDay;
  private JSONArray usersName;
  private JSONArray usersEmail;
  private JSONArray tasks;

  //UI Elements
  private EditText mProjectNameView;
  private EditText mPasswordView;
  private EditText mDescriptionView;
  private EditText mUIDView;
  private Button mDateButton;
  private Button mTimeButton;
  private Button mSubmitButton;
  private ProgressDialog progress;

  @Override
  public View onCreateView(LayoutInflater inflater, 
      ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_create_project, container, false);
    progress = new ProgressDialog(rootView.getContext());
      progress.setTitle("Creating Project");
      progress.setMessage("Please Wait...");
    progress.dismiss();
    mNewProject = new Project();
    usersName = new JSONArray();
    usersEmail = new JSONArray();
    usersName.put(ParseUser.getCurrentUser().getString("name"));
    usersEmail.put(ParseUser.getCurrentUser().getString("username"));
    
    tasks = new JSONArray();
    mProjectNameView = (EditText) rootView.findViewById(R.id.createProjectName);
    mDescriptionView = (EditText) rootView.findViewById(R.id.createNewProjectDescription);
    mPasswordView = (EditText) rootView.findViewById(R.id.createProjectPassword);
    mUIDView = (EditText) rootView.findViewById(R.id.createProjectUIDText);

    mDateButton = (Button) rootView.findViewById(R.id.createProjectDueDateButton);
    mDateButton.setOnClickListener(this);
    mTimeButton = (Button) rootView.findViewById(R.id.createNewProjectTimeButton);
    mTimeButton.setOnClickListener(this);
    mSubmitButton = (Button) rootView.findViewById(R.id.createProjectButton);
    mSubmitButton.setOnClickListener(this);

    return rootView;
  }
  
  @Override
  public void setMenuVisibility(final boolean visible) {
    super.setMenuVisibility(visible);
    if (!visible) {
      clearViews();
    }
  }

  @Override
  public void onClick(View view) {
    DialogFragment newFragment;
    FragmentTransaction ft;
    switch(view.getId()) {

    case R.id.createProjectDueDateButton:
      ft = getFragmentManager().beginTransaction();
      newFragment = new DatePickerFragment(this);
      newFragment.show(ft, "datePicker");
      break;

    case R.id.createNewProjectTimeButton:
      ft = getFragmentManager().beginTransaction();
      newFragment = new TimePickerFragment(this);
      newFragment.show(ft, "timePicker");
      break;

    case R.id.createProjectButton:
      submit();
      break;
    }
  }

  @Override
  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    mYear = year;
    mMonth = monthOfYear;
    mDay = dayOfMonth;
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    mHourPicker = hourOfDay;
    mMinutePicker = minute;
  }

  private void submit() {
    progress.show();
    //Reset Error
    mProjectNameView.setError(null);
    mPasswordView.setError(null);
    mDescriptionView.setError(null);
    mUIDView.setError(null);

    //Initialize fields
    mProjectName = mProjectNameView.getText().toString();
    mUID = mUIDView.getText().toString();
    mPassword = mPasswordView.getText().toString();
    mDescription = mDescriptionView.getText().toString();
    mDueDate = mMonth + "/" + mDay + "/" + mYear + " at " + mHourPicker + ":" + mMinutePicker;

    //error checking
    if (!errorChecking()) {
      progress.dismiss();
      return;
    }

    //check if UID is unique
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
    query.whereEqualTo("UID", mUID);
    query.countInBackground(new CountCallback() {
      public void done(int count, ParseException e) {
        if (e == null && count > 0) {
          mUIDView.setError(getString(R.string.duplicateUIDError));
          mUIDView.requestFocus();
          progress.dismiss();
          Toast.makeText(getActivity(), R.string.duplicateUIDErrorToast, Toast.LENGTH_LONG).show();
          return;
        }
        else if (e == null && count == 0) {
          //Setup project and push to cloud
          mPassword = BCrypt.hashpw(mPassword, BCrypt.gensalt());
          mNewProject.setName(mProjectName);
          mNewProject.setUID(mUID);
          mNewProject.setPassword(mPassword);
          mNewProject.setDescription(mDescription);
          mNewProject.setDueDate(mDueDate);
          mNewProject.setAdmin(ParseUser.getCurrentUser());
          mNewProject.addUsers(usersName);
          mNewProject.addUserEmails(usersEmail);
          mNewProject.addTasks(tasks);
          mNewProject.setStatus("Not Started");
          mNewProject.setCompleted(false);
          mNewProject.saveInBackground();
          progress.dismiss();
          clearViews();
          Toast.makeText(getActivity(), "Project successfully created!", Toast.LENGTH_LONG).show();
          mNewProject = new Project();
        }
      }
    });
  }

  private boolean errorChecking() {
    if (mProjectName.isEmpty()) {
      mProjectNameView.setError(getString(R.string.blankField));
      mProjectNameView.requestFocus();
      return false;
    }
    else if (mUID.isEmpty()) {
      mUIDView.setError(getString(R.string.blankField));
      mUIDView.requestFocus();
      return false;
    }
    else if (mPassword.isEmpty()) {
      mPasswordView.setError(getString(R.string.blankField));
      mPasswordView.requestFocus();
      return false;
    }
    else if (mDescription.isEmpty()) {
      mDescriptionView.setError(getString(R.string.blankField));
      mDescriptionView.requestFocus();
      return false;
    }
    else {
      return true;
    }
  }

  private void clearViews() {
    if (mProjectNameView == null || 
        mPasswordView == null || 
        mUIDView == null || 
        mDescriptionView == null) {
      return;
    }
      mProjectNameView.setText("");
      mProjectNameView.setError(null);
      mPasswordView.setText("");
      mPasswordView.setError(null);
      mUIDView.setText("");
      mUIDView.setError(null);
      mDescriptionView.setText("");
      mDescriptionView.setError(null);
      mProjectNameView.requestFocus();
  }

}
