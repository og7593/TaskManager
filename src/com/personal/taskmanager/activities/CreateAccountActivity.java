package com.personal.taskmanager.activities;

import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.personal.taskmanager.R;

public class CreateAccountActivity extends Activity {
  //User Info
  private String mUserName;
  private String mUserEmail;
  private String mUserPassword;

  //UI Elements
  private EditText mUserNameTextView;
  private EditText mUserEmailTextView;
  private EditText mUserPasswordTextView;
  private ProgressDialog progress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_account);
    // Show the Up button in the action bar.

    mUserNameTextView = (EditText) findViewById(R.id.accountCreateName);
    mUserEmailTextView = (EditText) findViewById(R.id.accountCreateEmail);
    mUserPasswordTextView = (EditText) findViewById(R.id.accountCreatePassword);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.account_creation, menu);
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

  public void createNewAccount(View view) {
    //Progress Dialog
    progress = new ProgressDialog(this);
    progress.setTitle("Creating Account");
    progress.setMessage("Please Wait...");
    progress.show();
    
    //Reset Errors
    mUserNameTextView.setError(null);
    mUserPasswordTextView.setError(null);
    mUserEmailTextView.setError(null);
    
    //Set user attributes
    mUserName = mUserNameTextView.getText().toString();
    mUserEmail = mUserEmailTextView.getText().toString();
    mUserPassword = mUserPasswordTextView.getText().toString();
    
    //Error Checking
    if(!this.errorChecking()) {
      return;
    }
    
    this.signUp();
  }
  
  private boolean errorChecking() {
    if (mUserName.isEmpty()) {
      progress.dismiss();
      mUserNameTextView.setError(getString(R.string.blankField));
      mUserNameTextView.requestFocus();
      Toast.makeText(getApplicationContext(),
                     R.string.blankUserNameToast,
                     Toast.LENGTH_SHORT).show();
      return false;
    }
    else if (mUserEmail.isEmpty()) {
      progress.dismiss();
      mUserEmailTextView.setError(getString(R.string.blankField));
      mUserEmailTextView.requestFocus();
      Toast.makeText(getApplicationContext(), R.string.blankEmailToast, Toast.LENGTH_SHORT).show();
      return false;
    }
    else if (mUserPassword.isEmpty()) {
      progress.dismiss();
      mUserPasswordTextView.setError(getString(R.string.blankField));
      mUserPasswordTextView.requestFocus();
      Toast.makeText(getApplicationContext(),
                     R.string.blankPasswordToast,
                     Toast.LENGTH_SHORT).show();
      return false;
    }
    else {
      return true;
    }
  }
  
  private void signUp() {
    //Create Parse User
    ParseUser newUser = new ParseUser();
    newUser.setUsername(mUserEmail.toLowerCase(Locale.getDefault()));
    newUser.setEmail(mUserEmail.toLowerCase(Locale.getDefault()));
    newUser.setPassword(mUserPassword);
    newUser.put("name", mUserName);

    //Upload User
    newUser.signUpInBackground(new SignUpCallback() {
      public void done(ParseException e) {
        if (e == null) {
          //account create
          Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
          //go to next activity
          progress.dismiss();
          Intent intent = new Intent(CreateAccountActivity.this, HomeScreenActivity.class);
          startActivity(intent);
        }
        else {
          //error check exception
          if (e.getCode() == 125) {
            progress.dismiss();
            mUserEmailTextView.setError(getString(R.string.invalidUserEmail));
            mUserEmailTextView.requestFocus();
            Toast.makeText(getApplicationContext(),
                           R.string.invalidUserEmail,
                           Toast.LENGTH_SHORT).show();
          }
          if (e.getCode() == 203 || e.getCode() == 202) {
            progress.dismiss();
            mUserEmailTextView.setError(getString(R.string.emailInUse));
            mUserEmailTextView.requestFocus();
            Toast.makeText(getApplicationContext(),
                           R.string.emailInUseToast,
                           Toast.LENGTH_SHORT).show();
          }
        }
      }
    });
  }

}
