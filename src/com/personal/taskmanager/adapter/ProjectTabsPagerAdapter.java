package com.personal.taskmanager.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.personal.taskmanager.fragments.CreateTaskFragment;
import com.personal.taskmanager.fragments.ProjectDetailsFragment;
import com.personal.taskmanager.fragments.ProjectTasksFragment;

public class ProjectTabsPagerAdapter extends FragmentStatePagerAdapter {
  String mUID;
  String mName;
  String mAdmin;
  
  public ProjectTabsPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int index) {
    
    Bundle bundle = new Bundle();
    bundle.putString("project uid", mUID);
    bundle.putString("project name", mName);
    bundle.putString("project admin", mAdmin);

    switch (index) {
    case 0:
      // Project Details;
      ProjectDetailsFragment frag = new ProjectDetailsFragment();
      frag.setArguments(bundle);
      return frag;
    case 1:
      // Project Tasks
      ProjectTasksFragment fragment = new ProjectTasksFragment();
      fragment.setArguments(bundle);
      return fragment;
    case 2:
      // Create Task
      CreateTaskFragment fragment2 = new CreateTaskFragment();
      fragment2.setArguments(bundle);
      return fragment2;
    }

    return null;
  }

  @Override
  public int getCount() {
    // get item count - equal to number of tabs
    return 3;
  }
  
  public void setUID(String uid) {
    this.mUID = uid;
  }
  
  public void setName (String name) {
    this.mName = name;
  }
  
  public void setAdmin(String admin) {
    this.mAdmin = admin;
  }

}
