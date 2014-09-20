package com.personal.taskmanager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.personal.taskmanager.fragments.CreateProjectFragment;
import com.personal.taskmanager.fragments.JoinProjectFragment;
import com.personal.taskmanager.fragments.MyProjectsFragment;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {
  
  public TabsPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int index) {

    switch (index) {
    case 0:
      // My Project
      return new MyProjectsFragment();
    case 1:
      // Create Project
      return new CreateProjectFragment();
    case 2:
      // Join Projects
      return new JoinProjectFragment();
    }

    return null;
  }

  @Override
  public int getCount() {
    // get item count - equal to number of tabs
    return 3;
  }

}
