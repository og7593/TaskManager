package com.personal.taskmanager.fragments;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.personal.taskmanager.R;
import com.personal.taskmanager.parseobjects.Project;
import com.personal.taskmanager.parseobjects.Task;

public class ProjectDetailsFragment extends Fragment {
  
  private TextView mNameView;
  private TextView mDescriptionView;
  private TextView mUIDView;
  private TextView mAdminView;
  private TextView mDateView;
  private TextView mUsersView;
  private TextView mTasksView;
  private ProgressBar mProgress;
  
  private String mUID;
  private String[] months = {"January", "February", "March", "April", "May", "June", "July", 
                              "August", "September", "October", "November", "December"};

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_project_details, container, false);
    
    mUID = getArguments().getString("project uid");
    
    mNameView = (TextView) rootView.findViewById(R.id.projectDetailsName);
    mNameView.setVisibility(View.INVISIBLE);
    mDescriptionView = (TextView) rootView.findViewById(R.id.projectDetailsDescription);
    mDescriptionView.setVisibility(View.INVISIBLE);
    mUIDView = (TextView) rootView.findViewById(R.id.projectDetailsUID);
    mUIDView.setVisibility(View.INVISIBLE);
    mAdminView = (TextView) rootView.findViewById(R.id.projectDetailsAdmin);
    mAdminView.setVisibility(View.INVISIBLE);
    mDateView = (TextView) rootView.findViewById(R.id.projectDueDateTime);
    mDateView.setVisibility(View.INVISIBLE);
    mUsersView = (TextView) rootView.findViewById(R.id.projectDetailsUsers);
    mUsersView.setVisibility(View.INVISIBLE);
    mTasksView = (TextView) rootView.findViewById(R.id.projectDetailsTasks);
    mTasksView.setVisibility(View.INVISIBLE);
    
    mProgress = (ProgressBar) rootView.findViewById(R.id.projectDetailsProgress);
    mProgress.bringToFront();
    mProgress.setVisibility(View.VISIBLE);
    
    ParseQuery<Project> projectQuery = ParseQuery.getQuery(Project.class);
    projectQuery.whereEqualTo("UID", mUID);
    projectQuery.getFirstInBackground(new GetCallback<Project>() {
      @Override
      public void done(Project object, ParseException e) {
        if (object != null && e == null) {
          mProgress.setVisibility(View.GONE);
          mNameView.append(object.getName());
          mNameView.setVisibility(View.VISIBLE);
          
          mDescriptionView.append(object.getDescription());
          mDescriptionView.setVisibility(View.VISIBLE);
          
          mUIDView.append(object.getUID());
          mUIDView.setVisibility(View.VISIBLE);
          
          //get admin
          try {
            mAdminView.append(object.getAdmin().fetchIfNeeded().getString("name"));
            mAdminView.setVisibility(View.VISIBLE);
          } 
          catch (ParseException e1) {
            e1.printStackTrace();
          }
          
          //get date
          String date = object.getDueDate();
          mDateView.append(dateTimeFormat(date));
          mDateView.setVisibility(View.VISIBLE);
          
          //Get users
          JSONArray users = object.getUser();
          String output = "";
          for (int i = 0; i < users.length(); i++) {
            try {
              output = output + users.getString(i) + ", ";
            } 
            catch (JSONException e1) {
              e1.printStackTrace();
            }
          }
          output = output.substring(0, output.length() - 2);
          mUsersView.append(output);
          mUsersView.setVisibility(View.VISIBLE);
        }
      }
    });
    
    //Get information about tasks
    ParseQuery<Task> taskQuery = ParseQuery.getQuery(Task.class);
    taskQuery.whereEqualTo("projectUID", mUID);
    taskQuery.findInBackground(new FindCallback<Task>() {
      public void done(List<Task> objects, ParseException e) {
        if (e == null) {
          if (objects == null || objects.size() == 0) {
            mTasksView.setText("No tasks created for this project");
          }
          else if (objects.size() > 0) {
            int numTasksCompleted = 0;
            for (int i = 0; i < objects.size(); i++) {
              if (objects.get(i).getCompleted()) {
                numTasksCompleted++;
              }
            }
            mTasksView.setText("Task Overview: " + numTasksCompleted + "/" + objects.size() + " tasks completed.");
            mTasksView.setVisibility(View.VISIBLE);
          }
        }
      }
    });
    return rootView;
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
  