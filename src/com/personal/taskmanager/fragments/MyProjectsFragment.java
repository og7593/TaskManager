package com.personal.taskmanager.fragments;

import java.util.ArrayList;
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
import com.parse.ParseUser;
import com.personal.taskmanager.R;
import com.personal.taskmanager.adapter.ProjectArrayAdapter;
import com.personal.taskmanager.parseobjects.Project;

public class MyProjectsFragment extends Fragment {

  private ParseUser currentUser;
  private View innerView;
  private ListView projectListView;
  private ProgressBar progress;
  private TextView loadTextView;
  private TextView noProjectsTextView;
  private boolean refresh = false;

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    
    View rootView = inflater.inflate(R.layout.fragment_my_projects, container, false);
    
    progress = (ProgressBar) rootView.findViewById(R.id.myProjectProgressBar);
    loadTextView = (TextView) rootView.findViewById(R.id.myProjectLoad);
    noProjectsTextView = (TextView) rootView.findViewById(R.id.myProjectsNone);
    
    
    noProjectsTextView.setVisibility(View.GONE);
    progress.bringToFront();
    loadTextView.bringToFront();
    innerView = rootView;
    projectListView = (ListView) innerView.findViewById(R.id.list);
    currentUser = ParseUser.getCurrentUser();

    //Get projects where user is admin
    ParseQuery<Project> queryAdmin = ParseQuery.getQuery(Project.class);
    queryAdmin.whereEqualTo("administrator", currentUser);

    //Get projects where user is non-admin
    ParseQuery<Project> queryUser = ParseQuery.getQuery(Project.class);
    queryUser.whereEqualTo("users", currentUser.getString("name"));

    List<ParseQuery<Project>> queries = new ArrayList<ParseQuery<Project>>();
    queries.add(queryAdmin);
    queries.add(queryUser);

    ParseQuery<Project> mainQuery = ParseQuery.or(queries);
    mainQuery.findInBackground(new FindCallback<Project>() {
      @Override
      public void done(List<Project> objects, ParseException e) {
        progress.setVisibility(View.GONE);
        loadTextView.setVisibility(View.GONE);

        if (e == null && objects.size() > 0) {
          Collections.sort(objects);
          projectListView.setAdapter(new ProjectArrayAdapter(innerView.getContext(), objects));
        }
        else if (e != null || objects.size() == 0) {
          noProjectsTextView.setVisibility(View.VISIBLE);
        }

      }

    });
    refresh = true;
    return rootView;
  }
  
  
  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) { 
    super.setUserVisibleHint(isVisibleToUser); 
    if (isVisibleToUser && this.refresh) {
      progress = (ProgressBar) getView().findViewById(R.id.myProjectProgressBar);
      loadTextView = (TextView) getView().findViewById(R.id.myProjectLoad);
      noProjectsTextView = (TextView) getView().findViewById(R.id.myProjectsNone);


      noProjectsTextView.setVisibility(View.GONE);
      progress.bringToFront();
      loadTextView.bringToFront();
      innerView = getView();
      projectListView = (ListView) innerView.findViewById(R.id.list);
      currentUser = ParseUser.getCurrentUser();

      //Get projects where user is admin
      ParseQuery<Project> queryAdmin = ParseQuery.getQuery(Project.class);
      queryAdmin.whereEqualTo("administrator", currentUser);

      //Get projects where user is non-admin
      ParseQuery<Project> queryUser = ParseQuery.getQuery(Project.class);
      queryUser.whereEqualTo("users", currentUser.getString("name"));

      List<ParseQuery<Project>> queries = new ArrayList<ParseQuery<Project>>();
      queries.add(queryAdmin);
      queries.add(queryUser);

      ParseQuery<Project> mainQuery = ParseQuery.or(queries);
      mainQuery.findInBackground(new FindCallback<Project>() {
        @Override
        public void done(List<Project> objects, ParseException e) {
          progress.setVisibility(View.GONE);
          loadTextView.setVisibility(View.GONE);

          if (e == null && objects.size() > 0) {
            Collections.sort(objects);
            projectListView.setAdapter(new ProjectArrayAdapter(innerView.getContext(), objects));
          }
          else if (e != null || objects.size() == 0) {
            noProjectsTextView.setVisibility(View.VISIBLE);
          }

        }

      });
    }
  }

  
  
}
