package com.personal.taskmanager.activities;

//Android Imports
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.personal.taskmanager.R;
import com.personal.taskmanager.parseobjects.Project;
import com.personal.taskmanager.parseobjects.Task;

public class SignInActivity extends Activity {

  // User variables
  private String mEmailAddress;
  private String mPassword;

  //UI Elements
  private EditText mEmailAddressTextView;
  private EditText mPasswordTextView;
  private ProgressDialog progress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in);
    ParseObject.registerSubclass(Project.class);
    ParseObject.registerSubclass(Task.class);
    Parse.initialize(this,
        "GbaYEF523qXPEJPjnKWTE7GUsV9b4MhZn6zcDblq",
        "vChOBRQNbbh2vgYxQy7TBlfsSAfMf9dhXyI5Pdbx");
    ParseAnalytics.trackAppOpened(getIntent());

    mEmailAddressTextView = (EditText) findViewById(R.id.emailTextBox);
    mPasswordTextView = (EditText) findViewById(R.id.passwordTextBox);
    
    if (ParseUser.getCurrentUser() != null) {
      ParseUser.logOut();
      Toast.makeText(getApplicationContext(), "You have been logged out.", Toast.LENGTH_LONG).show();
    }
  }

  /*@Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.sign_in_or_create_account, menu);
    return true;
  }*/

  // Create New Account
  public void newAccount(View view) {
    Intent intent = new Intent(SignInActivity.this, CreateAccountActivity.class);
    this.startActivity(intent);
  }

  // Sign In
  public void signIn(View view) {
    //Progress Dialog
    progress = new ProgressDialog(this);
    progress.setTitle("Signing In");
    progress.setMessage("Please Wait...");
    progress.show();

    //Reset Error
    mEmailAddressTextView.setError(null);
    mPasswordTextView.setError(null);

    //Get User Info + error checking
    mEmailAddress = mEmailAddressTextView.getText().toString();
    mPassword = mPasswordTextView.getText().toString();
    if (!this.errorChecking()) {
      return;
    }

    //Log In
    this.logInParse();
  }

  public void userForgotPassword(View view) {
    Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
    this.startActivity(intent);
  }

  private void logInParse() {
    ParseUser.logInInBackground(mEmailAddress.toLowerCase(Locale.getDefault()),
        mPassword,
        new LogInCallback() {
      public void done(ParseUser user, ParseException e) {
        if (user != null) {
          progress.dismiss();
          Intent intent = new Intent(SignInActivity.this, HomeScreenActivity.class);
          startActivity(intent);
          //finish();
          return;
        }
        else {
          if (e.getCode() == 101) {
            progress.dismiss();
            mPasswordTextView.setError(getString(R.string.accountNotFound));
            mPasswordTextView.requestFocus();
            Toast.makeText(getApplicationContext(),
                R.string.accountNotFoundToast,
                Toast.LENGTH_LONG).show();
            return;
          }
          else if (e.getCode() == 100) {
            //no network connection
          }
          else {
            progress.dismiss();
            Toast.makeText(getApplicationContext(),
                Integer.toString(e.getCode()),
                Toast.LENGTH_SHORT).show();
            return;
          }
        }
      }
    });
  }

  private boolean errorChecking() {
    if (mEmailAddress.isEmpty()) {
      progress.dismiss();
      mEmailAddressTextView.setError(getString(R.string.blankField));
      mEmailAddressTextView.requestFocus();
      Toast.makeText(getApplicationContext(), R.string.blankEmailToast, Toast.LENGTH_SHORT).show();
      return false;
    }
    else if (mPassword.isEmpty()) {
      progress.dismiss();
      mPasswordTextView.setError(getString(R.string.blankField));
      mPasswordTextView.requestFocus();
      Toast.makeText(getApplicationContext(),
          R.string.blankPasswordToast,
          Toast.LENGTH_SHORT).show();
      return false;
    }
    else {
      return true;
    }
  }

}
