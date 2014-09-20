package com.personal.taskmanager.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.personal.taskmanager.R;
import com.personal.taskmanager.parseobjects.Task;

public class TaskDetailsActivity extends Activity {
  
  
  private String mTaskName;
  private String mTaskObjectId;
  private String[] months = {"January", "February", "March", "April", "May", "June", "July", 
      "August", "September", "October", "November", "December"};
  
  private TextView mNameView;
  private TextView mDescriptionView;
  private TextView mNotesView;
  private TextView mDueDate;
  private TextView mAssignedTo;
  private TextView mStatus;
  private ProgressBar mProgress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_details);
    
    mTaskName = getIntent().getStringExtra("task name");
    mTaskObjectId = getIntent().getStringExtra("task uid");
    
    mNameView = (TextView) findViewById(R.id.taskDetailsName);
    mDescriptionView = (TextView) findViewById(R.id.taskDetailsDescription);
    mNotesView = (TextView) findViewById(R.id.taskDetailsNotes);
    mDueDate = (TextView) findViewById(R.id.taskDetailsDueDate);
    mAssignedTo = (TextView) findViewById(R.id.taskDetailsAssignedTo);
    mStatus = (TextView) findViewById(R.id.taskDetailsStatus);
    mProgress = (ProgressBar) findViewById(R.id.taskDetailsProgress);
    mNameView.setVisibility(View.INVISIBLE);
    mDescriptionView.setVisibility(View.INVISIBLE);
    mNotesView.setVisibility(View.INVISIBLE);
    mDueDate.setVisibility(View.INVISIBLE);
    mAssignedTo.setVisibility(View.INVISIBLE);
    mStatus.setVisibility(View.INVISIBLE);
    mProgress.bringToFront();
    
    ParseQuery<Task> taskQuery = ParseQuery.getQuery(Task.class);
    taskQuery.whereEqualTo("objectId", mTaskObjectId);
    taskQuery.getFirstInBackground(new GetCallback<Task>() {
      public void done(Task task, ParseException e) {
        if (e == null && task != null) {
          mProgress.setVisibility(View.GONE);
          mNameView.append(mTaskName);
          mDescriptionView.append(task.getDescription());
          mNotesView.append(task.getNotes());
          mDueDate.append(dateTimeFormat(task.getDueDate()));
          mAssignedTo.append(task.getAssignedTo());
          mStatus.append(task.getStatus());
          mNameView.setVisibility(View.VISIBLE);
          mDescriptionView.setVisibility(View.VISIBLE);
          mNotesView.setVisibility(View.VISIBLE);
          mDueDate.setVisibility(View.VISIBLE);
          mAssignedTo.setVisibility(View.VISIBLE);
          mStatus.setVisibility(View.VISIBLE);
        }
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.task_details, menu);
    return true;
  }
  
  private String dateTimeFormat(String date) {
    int monthIndex = date.indexOf("/");
    String output = months[Integer.parseInt(date.substring(0, monthIndex))];
    int dayIndex = date.indexOf("/") + 1;
    int dayIndex2 = date.indexOf("/", dayIndex);
    output = output + (" " + Integer.parseInt(date.substring(dayIndex, dayIndex2)) + ", ");
    int yearIndex = date.indexOf("/", dayIndex) + 1;
    output =  output + (date.substring(yearIndex, yearIndex+4) + " at ");
    int timeIndex = date.lastIndexOf("a") + 3;
    String time = date.substring(timeIndex);
    int hour = Integer.parseInt(time.substring(0, time.indexOf(":")));
    String AmPm = "AM";
    if (hour >= 12) {
      hour -= 12;
      AmPm = "PM";
    }
    String hourString = Integer.toString(hour);
    String minute = time.substring(time.indexOf(":") + 1);
    if (Integer.parseInt(minute) < 10) {
      minute = "0" + minute;
    }
    output =  output + (hourString + ":" + minute + AmPm);
    return output;
  }

}
