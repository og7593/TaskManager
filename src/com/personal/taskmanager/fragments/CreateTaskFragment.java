package com.personal.taskmanager.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.personal.taskmanager.R;
import com.personal.taskmanager.parseobjects.Project;
import com.personal.taskmanager.parseobjects.Task;

public class CreateTaskFragment extends Fragment 
implements OnClickListener, OnDateSetListener, OnTimeSetListener, OnItemSelectedListener {
  // task data
  private Task mTask;
  private String mProjectUID;
  private String mName;
  private String mDescription;
  private String mNotes;
  private String mDueDate;
  private String mAdmin;
  private String mProjectName;
  private int mMinute;
  private int mHour;
  private int mYear;
  private int mMonth;
  private int mDay;
  private List<String> mUserName;
  
  // data elements
  private EditText mNameView;
  private EditText mDescriptionView;
  private EditText mNotesView;
  private Spinner mUsers;
  private Button mDateButton;
  private Button mTimeButton;
  private Button mSubmitButton;
  private View rootView;
  private ProgressDialog progress;
  
  @Override
  public View onCreateView(LayoutInflater inflater, 
                           ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_create_task, container, false);
    progress = new ProgressDialog(rootView.getContext());
    progress.setTitle("Creating Task");
    progress.setMessage("Please wait...");
    progress.dismiss();
    // initialize variables
    mTask = new Task();
    mProjectUID = getArguments().getString("project uid");
    mProjectName = getArguments().getString("project name");
    mAdmin = getArguments().getString("project admin");
    mNameView = (EditText) rootView.findViewById(R.id.createTaskName);
    mDescriptionView = (EditText) rootView.findViewById(R.id.createTaskDescription);
    mNotesView = (EditText) rootView.findViewById(R.id.createTaskNotes);
    
    // populate spinner
    mUserName = new ArrayList<String>();
    mUserName.add("Assign To:");
    mUsers = (Spinner) rootView.findViewById(R.id.spinnerUser);
    ParseQuery<Project> userQuery = ParseQuery.getQuery(Project.class);
    userQuery.whereEqualTo("UID", mProjectUID);
    userQuery.getFirstInBackground(new GetCallback<Project>() {
      @Override
      public void done(Project object, ParseException e) {
        if (e == null && object != null) {
          JSONArray usersArray = object.getUser();
          for (int i = 0 ; i < usersArray.length(); i++) {
            try {
              mUserName.add(usersArray.getString(i));
            }
            catch (JSONException e1) {
              e1.printStackTrace();
            }
          }
          ArrayAdapter<String> users = new ArrayAdapter<String>(rootView.getContext(), 
              android.R.layout.simple_spinner_item,
              android.R.id.text1,
              mUserName);
          users.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          mUsers.setAdapter(users);
          mUsers.setSelection(0, true);
        } 
      }
    });
    mUsers.setOnItemSelectedListener(this);
    
    
    mDateButton = (Button) rootView.findViewById(R.id.createTaskDate);
    mDateButton.setOnClickListener(this);
    mTimeButton = (Button) rootView.findViewById(R.id.createTaskTime);
    mTimeButton.setOnClickListener(this);
    mSubmitButton = (Button) rootView.findViewById(R.id.createTaskButton);
    mSubmitButton.setOnClickListener(this);
    return rootView;
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    mHour = hourOfDay;
    mMinute = minute;
  }

  @Override
  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    mYear = year;
    mMonth = monthOfYear;
    mDay = dayOfMonth;
  }

  @Override
  public void onClick(View v) {
    DialogFragment newFragment;
    FragmentTransaction ft;
    switch(v.getId()) {

    case R.id.createTaskDate:
      ft = getFragmentManager().beginTransaction();
      newFragment = new DatePickerFragment(this);
      newFragment.show(ft, "datePicker");
      break;

    case R.id.createTaskTime:
      ft = getFragmentManager().beginTransaction();
      newFragment = new TimePickerFragment(this);
      newFragment.show(ft, "timePicker");
      break;

    case R.id.createTaskButton:
      submit();
      break;
    }
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    // TODO Auto-generated method stub
    if (((TextView) parent.getChildAt(0)).getText().toString().compareTo("Assign To:") == 0) {
      ((TextView) parent.getChildAt(0)).setTextColor(getResources()
          .getColor(android.R.color.tertiary_text_dark));
    }
    ((TextView) parent.getChildAt(0)).setTextSize(18);
    parent.getItemAtPosition(pos);
    parent.setSelection(pos);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
  }
  
  @Override
  public void setMenuVisibility(final boolean visible) {
    super.setMenuVisibility(visible);
    if (!visible) {
      clearViews();
    }
  }
  
  private void submit() {
    progress.show();
    //reset error
    mNameView.setError(null);
    mDescriptionView.setError(null);
    mNotesView.setError(null);
    
    //get text
    mName = mNameView.getText().toString();
    mDescription = mDescriptionView.getText().toString();
    mNotes = mNotesView.getText().toString();
    mDueDate = mMonth + "/" + mDay + "/" + mYear + " at " + mHour + ":" + mMinute;
    
    if (!errorChecking()) {
      progress.dismiss();
      return;
    }
    
    mTask.setTaskName(mName);
    mTask.setDescription(mDescription);
    mTask.setNotes(mNotes);
    mTask.setDueDate(mDueDate);
    mTask.setAssignedTo(mUsers.getSelectedItem().toString());
    if (!isAdmin() && 
        mUsers.getSelectedItem().toString().
          compareTo(ParseUser.getCurrentUser().getString("name")) != 0) {
      progress.dismiss();
      Toast.makeText(getActivity(), 
                    "Only administrator can assign tasks to other users.",
                    Toast.LENGTH_LONG).show();
      return;
    }
    else if (mUsers.getSelectedItem().toString().compareTo("Assign To:") == 0) {
      progress.dismiss();
      Toast.makeText(getActivity(), "Please assign task to somebody", Toast.LENGTH_LONG);
    }
    mTask.setProjectAdmin(mAdmin);
    mTask.setCompleted(false);
    mTask.setProjectUID(mProjectUID);
    mTask.setStatus("Not Started");
    mTask.setProjectName(mProjectName);
    mTask.saveInBackground();
    progress.dismiss();
    clearViews();
    Toast.makeText(getActivity(), "Task successfully created!", Toast.LENGTH_LONG).show();
    mTask = new Task();
  }
  
  private boolean errorChecking() {
    if (mName.isEmpty()) {
      mNameView.setError(getString(R.string.blankField));
      mNameView.requestFocus();
      return false;
    }
    else if (mDescription.isEmpty()) {
      mDescriptionView.setError(getString(R.string.blankField));
      mDescriptionView.requestFocus();
      return false;
    }
    else if (mNotes.isEmpty()) {
      mNotesView.setError(getString(R.string.blankField));
      mNotesView.requestFocus();
      return false;
    }
    else {
      return true;
    }
  }
  
  private void clearViews() {
    if (mName == null || 
        mNotesView == null || 
        mDescriptionView == null) {
      return;
    }
      mNameView.setText("");
      mNameView.setError(null);
      mNotesView.setText("");
      mNotesView.setError(null);
      mDescriptionView.setText("");
      mDescriptionView.setError(null);
      mUsers.setSelection(0, true);
      mNameView.requestFocus();
  }
  
  private boolean isAdmin() {
    if (ParseUser.getCurrentUser().getString("name").compareTo(mAdmin) == 0) {
      return true;
    }
    else {
      return false;
    }
  }
  
}
