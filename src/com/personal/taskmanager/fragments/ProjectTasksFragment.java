package com.personal.taskmanager.fragments;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.personal.taskmanager.R;
import com.personal.taskmanager.adapter.TaskArrayAdapter;
import com.personal.taskmanager.parseobjects.Task;

public class ProjectTasksFragment extends Fragment {
  
  private String mProjectUID;
  
  private View innerView;
  private ListView taskListView;
  private ProgressBar progress;
  private TextView loadTextView;
  private TextView noTasksView;
  
  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_project_tasks, container, false);
    
    mProjectUID = getArguments().getString("project uid");
    
    progress = (ProgressBar) rootView.findViewById(R.id.projectTaskProgressBar);
    loadTextView = (TextView) rootView.findViewById(R.id.projectTasksLoad);
    noTasksView = (TextView) rootView.findViewById(R.id.projectTasksNone);
    taskListView = (ListView) rootView.findViewById(R.id.listTasks);
    
    noTasksView.setVisibility(View.GONE);
    progress.bringToFront();
    loadTextView.bringToFront();
    innerView = rootView;
    
    //Query Tasks in project
    ParseQuery<Task> taskQuery = ParseQuery.getQuery(Task.class);
    taskQuery.whereEqualTo("projectUID", mProjectUID);
    taskQuery.findInBackground(new FindCallback<Task> () {
      @Override
      public void done(List<Task> objects, ParseException e) {
        progress.setVisibility(View.INVISIBLE);
        loadTextView.setVisibility(View.INVISIBLE);
        
        if (e == null && objects.size() > 0) {
          Collections.sort(objects);
          taskListView.setAdapter(new TaskArrayAdapter(innerView.getContext(), objects));
        }
        else if (e != null || objects.size() == 0) {
          noTasksView.setVisibility(View.VISIBLE);
        }
      }
    });
    return rootView;
  }
  
  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) { 
    super.setUserVisibleHint(isVisibleToUser); 
    if (isVisibleToUser) {
      mProjectUID = getArguments().getString("project uid");
      
      progress = (ProgressBar) getView().findViewById(R.id.projectTaskProgressBar);
      loadTextView = (TextView) getView().findViewById(R.id.projectTasksLoad);
      noTasksView = (TextView) getView().findViewById(R.id.projectTasksNone);
      taskListView = (ListView) getView().findViewById(R.id.listTasks);
      
      noTasksView.setVisibility(View.GONE);
      progress.bringToFront();
      loadTextView.bringToFront();
      innerView = getView();
      
      //Query Tasks in project
      ParseQuery<Task> taskQuery = ParseQuery.getQuery(Task.class);
      taskQuery.whereEqualTo("projectUID", mProjectUID);
      taskQuery.findInBackground(new FindCallback<Task> () {
        @Override
        public void done(List<Task> objects, ParseException e) {
          progress.setVisibility(View.INVISIBLE);
          loadTextView.setVisibility(View.INVISIBLE);
          
          if (e == null && objects.size() > 0) {
            Collections.sort(objects);
            taskListView.setAdapter(new TaskArrayAdapter(innerView.getContext(), objects));
          }
          else if (e != null || objects.size() == 0) {
            noTasksView.setVisibility(View.VISIBLE);
          }
        }
      });
    }
  }
}
