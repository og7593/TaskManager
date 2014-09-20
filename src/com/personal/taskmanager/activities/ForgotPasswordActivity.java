package com.personal.taskmanager.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.personal.taskmanager.R;

public class ForgotPasswordActivity extends Activity {

  private String mEmailAddress;
  private EditText mEmailAddressTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_password);
    mEmailAddressTextView = (EditText) findViewById(R.id.forgotPasswordEmail);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.forgot_password, menu);
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

  public void onClickSubmit(View view) {
    mEmailAddressTextView.setError(null);
    mEmailAddress = mEmailAddressTextView.getText().toString();
    if (!errorChecking()) {
      return;
    }

    ParseUser.requestPasswordResetInBackground(mEmailAddress, new RequestPasswordResetCallback() {
      public void done(ParseException e) {
        if (e == null) {
          Toast.makeText(getApplicationContext(), "Email Sent", Toast.LENGTH_SHORT).show();
          finish();
        } 
        else {
          if (e.getCode() == 125) {
            mEmailAddressTextView.setError(getString(R.string.invalidUserEmail));
            mEmailAddressTextView.requestFocus();
            Toast.makeText(getApplicationContext(),
                           R.string.invalidUserEmail,
                           Toast.LENGTH_SHORT).show();
          }
          else if (e.getCode() == 205) {
              mEmailAddressTextView.setError("Email Not Found");
              mEmailAddressTextView.requestFocus();
              Toast.makeText(getApplicationContext(),
                      "The email address entered was not found on file. Please try again.",
                      Toast.LENGTH_LONG).show();
          }
          else {
              Toast.makeText(getApplicationContext(), Integer.toString(e.getCode()), Toast.LENGTH_SHORT).show();
          }
        }
      }
    });
  }

  private boolean errorChecking() {
    if (mEmailAddress.isEmpty()) {
      mEmailAddressTextView.setError(getString(R.string.blankField));
      mEmailAddressTextView.requestFocus();
      return false;
    }
    else {
      return true;
    }
  }

}
