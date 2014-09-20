package com.personal.taskmanager.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.WindowManager;

import com.personal.taskmanager.R;
import com.personal.taskmanager.adapter.TabsPagerAdapter;

public class HomeScreenActivity extends FragmentActivity implements TabListener {

  private ViewPager mViewPager;
  private TabsPagerAdapter mAdapter;
  private ActionBar actionBar;
  private String[] tabs = {"My Projects", "Create Project", "Join Project"};
  //private String mUID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_screen);

    //Hide keyboard
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    
    //mUID = getIntent().getStringExtra("project uid");

    // Initialization
    mViewPager = (ViewPager) findViewById(R.id.pager);
    actionBar = getActionBar();
    mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(mAdapter);
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Adding Tabs
    for (String tab_name : tabs) {
      actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
    }


    mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        // on changing the page
        // make respected tab selected
        actionBar.setSelectedNavigationItem(position);
      }
    });
  }

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

  /*
  public void viewMyProjects(View view) {
    //Example of how to query an object
    Project parseProject = new Project();
    ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
    try {
      parseProject = (Project) query.getFirst();
      Toast.makeText(getApplicationContext(), parseProject.getDueDate().toString(), Toast.LENGTH_LONG).show();
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Intent intent = new Intent(HomeScreen.this, MyProjects.class);
    this.startActivity(intent);
  }*/

}
