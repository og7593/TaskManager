package com.personal.taskmanager.activities;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.personal.taskmanager.R;
import com.personal.taskmanager.adapter.ProjectTabsPagerAdapter;

public class ProjectHomeScreenActivity extends FragmentActivity implements TabListener {
  
  private ViewPager mViewPager;
  private ProjectTabsPagerAdapter mAdapter;
  private ActionBar actionBar;
  private String[] tabs = {"Project Overview", "Tasks", "Create Task"};
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_project_home_screen);

    // Initialization
    mViewPager = (ViewPager) findViewById(R.id.pager);
    actionBar = getActionBar();
    mAdapter = new ProjectTabsPagerAdapter(getSupportFragmentManager());
    mAdapter.setUID(getIntent().getStringExtra("project uid"));
    mAdapter.setName(getIntent().getStringExtra("project name"));
    mAdapter.setAdmin(getIntent().getStringExtra("project admin"));
    mViewPager.setAdapter(mAdapter);
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    getActionBar().setTitle(getIntent().getStringExtra("project name"));
    //setActionBarTitle(getIntent().getStringExtra("project name"));
    
    // Add tabs to activity
    for (String tabNames : tabs) {
      actionBar.addTab(actionBar.newTab().setText(tabNames).setTabListener(this));
    }
    
    mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
      }
    });
  }

  /*@Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.project_home_screen, menu);
    return true;
  }*/

  @Override
  public void onTabReselected(Tab tab, FragmentTransaction ft) {
  }

  @Override
  public void onTabSelected(Tab tab, FragmentTransaction ft) {
    mViewPager.setCurrentItem(tab.getPosition());
  }

  @Override
  public void onTabUnselected(Tab tab, FragmentTransaction ft) {
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case android.R.id.home:
      // This ID represents the Home or Up button. In the case of this
      // activity, the Up button is shown. Use NavUtils to allow users
      // to navigate up one level in the application structure. For
      // more details, see the Navigation pattern on Android Design:
      //
      // http://developer.android.com/design/patterns/navigation.html#up-vs-back
      //
      NavUtils.navigateUpFromSameTask(this);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
  
  public void setActionBarTitle(String title) {
    getActionBar().setTitle(title);
  }

}
