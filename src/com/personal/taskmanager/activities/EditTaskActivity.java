package com.personal.taskmanager.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.personal.taskmanager.R;
import com.personal.taskmanager.fragments.DatePickerFragmentActivity;
import com.personal.taskmanager.fragments.TimePickerFragmentActivity;
import com.personal.taskmanager.parseobjects.Project;
import com.personal.taskmanager.parseobjects.Task;

public class EditTaskActivity extends Activity 
 implements OnDateSetListener, OnTimeSetListener, OnItemSelectedListener {
  
  private String mName;
  private String mDescription;
  private String mNotes;
  private String mAssignedTo;
  private String mOldName;
  private String mDate;
  private String mTaskObjectId;
  private String mProjectUID;
  private String mProjectName;
  private String mProjectAdmin;
  private String mStatus;
  private int mMinute;
  private int mHour;
  private int mYear;
  private int mMonth;
  private int mDay;
  private List<String> mUserName;
  
  private EditText mNameView;
  private EditText mDescriptionView;
  private EditText mNotesView;
  private Spinner mUsers;
  private RadioButton mNotStartedView;
  private RadioButton mInProgressView;
  private RadioButton mCompletedView;
  private TextView mStatusText;
  
  private Context context;
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_task);
    mProjectUID = getIntent().getStringExtra("project uid");
    mProjectName = getIntent().getStringExtra("project name");
    mProjectAdmin = getIntent().getStringExtra("project admin");
    mOldName = getIntent().getStringExtra("task name");
    mTaskObjectId = getIntent().getStringExtra("task uid");
    mStatus = getIntent().getStringExtra("task status");
    setTitle("Editing " + mOldName);
    
    context = getBaseContext();
    
    mNameView = (EditText) findViewById(R.id.editTaskName);
    mDescriptionView = (EditText) findViewById(R.id.editTaskDescription);
    mNotesView = (EditText) findViewById(R.id.editTaskNotes);
    mNotStartedView = (RadioButton) findViewById(R.id.taskNotStarted);
    mInProgressView = (RadioButton) findViewById(R.id.taskInProgress);
    mCompletedView = (RadioButton) findViewById(R.id.taskCompleted);
    mStatusText = (TextView) findViewById(R.id.status);
    mStatusText.setTextColor(getResources().getColor(android.R.color.tertiary_text_dark));
    findViewById(R.id.lineView).setBackgroundColor(getResources()
        .getColor(android.R.color.tertiary_text_dark));
    
    //populate spinner
    mUserName = new ArrayList<String>();
    mUserName.add("Assign To:");
    mUsers = (Spinner) findViewById(R.id.editTaskSpinner);
    ParseQuery<Project> userQuery = ParseQuery.getQuery(Project.class);
    userQuery.whereEqualTo("UID", mProjectUID);
    userQuery.getFirstInBackground(new GetCallback<Project> () {
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
          ArrayAdapter<String> users = new ArrayAdapter<String>(context, 
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
    
    //Set radio button
    if (mStatus.compareTo("Not Started") == 0) {
      mNotStartedView.setChecked(true);
    }
    else if (mStatus.compareTo("In Progress") == 0) {
      mInProgressView.setChecked(true);
    }
    else if (mStatus.compareTo("Completed") == 0) {
      mCompletedView.setChecked(true);
    }
  }
  
  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
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
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.edit_task, menu);
    return true;
  }
  
  public void editDate(View v) {
    DatePickerFragmentActivity datePicker = new DatePickerFragmentActivity();
    datePicker.show(getFragmentManager(), "datePicker");
    return;
  }
  
  public void editTime(View v) {
    TimePickerFragmentActivity timePicker = new TimePickerFragmentActivity();
    timePicker.show(getFragmentManager(), "timePicker");
    return;
  }
  
  @Override
  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    mYear = year;
    mMonth = monthOfYear;
    mDay = dayOfMonth;
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    mHour = hourOfDay;
    mMinute = minute;
  }
  
  public void updateTask(View v) {
    ParseQuery<Task> taskQuery = ParseQuery.getQuery(Task.class);
    taskQuery.whereEqualTo("objectId", mTaskObjectId);
    taskQuery.getFirstInBackground(new GetCallback<Task> () {
      @Override
      public void done(Task object, ParseException e) {
        if (e == null && object != null) {
          //update name
          mName = mNameView.getText().toString();
          if (!mName.isEmpty()) {
            object.setTaskName(mName);
          }
          
          //update description
          mDescription = mDescriptionView.getText().toString();
          if (!mDescription.isEmpty()) {
            object.setDescription(mDescription);
          }
          
          //update notes
          mNotes = mNotesView.getText().toString();
          if(!mNotes.isEmpty()) {
            object.setNotes(mNotes);
          }
          
          //update date
          mDate = mMonth + "/" + mDay + "/" + mYear + " at " + mHour + ":" + mMinute;
          String compareDate = "0/0/0 at 0:0";
          if (mDate.compareTo(compareDate) != 0) {
            object.setDueDate(mDate);
          }
          
          //update assignee
          mAssignedTo = mUsers.getSelectedItem().toString();
          if (mAssignedTo.compareTo("Assign To:") != 0) {
            object.setAssignedTo(mAssignedTo);
          }
          
          //update status
          mStatus = getStatus();
          object.setStatus(mStatus);
          
          object.saveInBackground();
          
          Intent intent = new Intent(EditTaskActivity.this, ProjectHomeScreenActivity.class);
          intent.putExtra("project uid", mProjectUID);
          intent.putExtra("project name", mProjectName);
          intent.putExtra("project admin", mProjectAdmin);
          startActivity(intent);
          finish();
        }
      }
    });
    return;
  }
  
  private String getStatus() {
    if (mNotStartedView.isChecked()) {
      return "Not Started";
    }
    else if (mInProgressView.isChecked()) {
      return "In Progress";
    }
    else {
      return "Completed";
    }
  }

}
