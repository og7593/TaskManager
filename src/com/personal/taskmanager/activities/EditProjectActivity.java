package com.personal.taskmanager.activities;

import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.personal.taskmanager.R;
import com.personal.taskmanager.fragments.DatePickerFragmentActivity;
import com.personal.taskmanager.fragments.TimePickerFragmentActivity;
import com.personal.taskmanager.parseobjects.Project;
import com.personal.taskmanager.parseobjects.Task;
import com.personal.taskmanager.utilities.BCrypt;

public class EditProjectActivity extends Activity
        implements OnDateSetListener, OnTimeSetListener/*, OnItemSelectedListener*/ {

    private String mName;
    private String mUID;
    private String mPassword;
    private String mDescription;
    private String mDueDate;
    private String mStatus;
    private String mOldUID;
    //private String mAdmin;
    private int mMinutePicker;
    private int mHourPicker;
    private int mYear;
    private int mMonth;
    private int mDay;
    //private boolean mUIDisUnique;
    //private List<String> mAdminList;

    private EditText mProjectNameView;
    private EditText mPasswordView;
    private EditText mDescriptionView;
    //private EditText mUIDView;
    private TextView mStatusText;
    //private Spinner mAdminSpinner;
    private RadioButton mNotStartedView;
    private RadioButton mInProgressView;
    private RadioButton mCompletedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);

        setTitle("Editing " + getIntent().getStringExtra("project name"));
        //mUIDisUnique = true;
        mUID = getIntent().getStringExtra("project uid");
        mOldUID = mUID;
        mStatus = getIntent().getStringExtra("project status");

        mProjectNameView = (EditText) findViewById(R.id.editProjectName);
        mPasswordView = (EditText) findViewById(R.id.editProjectPassword);
        mDescriptionView = (EditText) findViewById(R.id.editProjectDescription);
        //mUIDView = (EditText) findViewById(R.id.editProjectUIDText);

        mStatusText = (TextView) findViewById(R.id.projectStatus);
        mStatusText.setTextColor(getResources().getColor(android.R.color.tertiary_text_dark));
        findViewById(R.id.projectLineView).setBackgroundColor(getResources()
                .getColor(android.R.color.tertiary_text_dark));

        //status
        mNotStartedView = (RadioButton) findViewById(R.id.projectNotStarted);
        mInProgressView = (RadioButton) findViewById(R.id.projectInProgress);
        mCompletedView = (RadioButton) findViewById(R.id.projectCompleted);
        if (mStatus.compareTo("Not Started") == 0) {
            mNotStartedView.setChecked(true);
        }
        else if (mStatus.compareTo("In Progress") == 0) {
            mInProgressView.setChecked(true);
        }
        else if (mStatus.compareTo("Completed") == 0) {
            mCompletedView.setChecked(true);
        }

        //spinner
    /*mAdminSpinner = (Spinner) findViewById(R.id.adminSpinner);
    mAdminList = new ArrayList<String>();
    mAdminList.add("Administrator:");
    ParseQuery<Project> userQuery = ParseQuery.getQuery(Project.class);
    userQuery.whereEqualTo("UID", mUID);
    userQuery.getFirstInBackground(new GetCallback<Project> () {
      public void done(Project object, ParseException e) {
        if (e == null && object != null) {
          JSONArray usersArray = object.getUser();
          for (int i = 0 ; i < usersArray.length(); i++) {
            try {
              mAdminList.add(usersArray.getString(i));
            }
            catch (JSONException e1) {
              e1.printStackTrace();
            }
          }
          ArrayAdapter<String> users = new ArrayAdapter<String>(getBaseContext(), 
              android.R.layout.simple_spinner_item,
              android.R.id.text1,
              mAdminList);
          users.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          mAdminSpinner.setAdapter(users);
          mAdminSpinner.setSelection(0, true);
        }
      }
    });
    mAdminSpinner.setOnItemSelectedListener(this);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_project, menu);
        return true;
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
        mHourPicker = hourOfDay;
        mMinutePicker = minute;
    }
  
  /*
  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    if (((TextView) parent.getChildAt(0)).getText().toString().compareTo("Administrator:") == 0) {
      ((TextView) parent.getChildAt(0)).setTextColor(getResources()
          .getColor(android.R.color.tertiary_text_dark));
    }
    ((TextView) parent.getChildAt(0)).setTextSize(18);
    parent.getItemAtPosition(pos);
    parent.setSelection(pos);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
  }*/

    public void editProject(View v) {
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
        query.whereEqualTo("UID", mUID);
        query.getFirstInBackground(new GetCallback<Project>() {
            @Override
            public void done(Project object, ParseException e) {
                if (object != null && e == null) {
                    //Update Name
                    mName = mProjectNameView.getText().toString();
                    if (!mName.isEmpty()) {
                        object.setName(mName);
                        //Update project name in list of tasks
                        ParseQuery<Task> taskQuery = ParseQuery.getQuery(Task.class);
                        taskQuery.whereEqualTo("projectUID", mOldUID);
                        taskQuery.findInBackground(new FindCallback<Task> () {
                            public void done(List<Task> objects, ParseException e) {
                                if (e == null && objects != null) {
                                    for (Task curTask : objects) {
                                        curTask.setProjectName(mName);
                                        curTask.saveInBackground();
                                    }
                                }
                            }
                        });
                    }
                    /*
                    //check if uid is unique
                    if (!mUIDView.getText().toString().isEmpty()) {
                        mUID = mUIDView.getText().toString();
                        //check if new uid is unique
                        ParseQuery<Project> projectQuery = ParseQuery.getQuery(Project.class);
                        projectQuery.whereEqualTo("UID", mUID);
                        projectQuery.countInBackground(new CountCallback() {
                            public void done(int count, ParseException e) {
                                if (e == null && count > 0) {
                                    mUIDisUnique = false;
                                    mUIDView.setError(getString(R.string.duplicateUIDError));
                                    mUIDView.requestFocus();
                                    //Toast.makeText(getApplicationContext(), R.string.duplicateUIDErrorToast, Toast.LENGTH_LONG).show();
                                    //return;
                                }
                            }
                        });
                        if (!mUIDisUnique) {
                            Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else
                            mUIDisUnique = true;
                    }
                    //UID
                    if (!mUIDView.getText().toString().isEmpty() && mUIDisUnique) {
                        mUID = mUIDView.getText().toString();
                        object.setUID(mUID);
                        //Update project uid in list of tasks
                        ParseQuery<Task> taskQuery = ParseQuery.getQuery(Task.class);
                        taskQuery.whereEqualTo("projectUID", mOldUID);
                        taskQuery.findInBackground(new FindCallback<Task> () {
                            public void done(List<Task> objects, ParseException e) {
                                if (e == null && objects != null) {
                                    for (Task curTask : objects) {
                                        curTask.setProjectUID(mUID);
                                        curTask.saveInBackground();
                                    }
                                }
                            }
                        });
                    }*/

                    //Password
                    mPassword = mPasswordView.getText().toString();
                    if (!mPassword.isEmpty()) {
                        mPassword = BCrypt.hashpw(mPassword, BCrypt.gensalt());
                        object.setPassword(mPassword);
                    }

                    //Description
                    mDescription = mDescriptionView.getText().toString();
                    if (!mDescription.isEmpty()) {
                        object.setDescription(mDescription);
                    }

                    //Date and Time
                    mDueDate = mMonth + "/" + mDay + "/" + mYear + " at " + mHourPicker + ":" + mMinutePicker;
                    String compareDate = "0/0/0 at 0:0";
                    if (mDueDate.compareTo(compareDate) != 0) {
                        object.setDueDate(mDueDate);
                    }

                    //status
                    mStatus = getStatus();
                    object.setStatus(mStatus);

                    object.saveInBackground();

                    Intent intent = new Intent(EditProjectActivity.this, HomeScreenActivity.class);
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
