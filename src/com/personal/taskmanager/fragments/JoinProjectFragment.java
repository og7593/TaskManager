package com.personal.taskmanager.fragments;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.personal.taskmanager.R;
import com.personal.taskmanager.parseobjects.Project;
import com.personal.taskmanager.utilities.BCrypt;

public class JoinProjectFragment extends Fragment implements OnClickListener {

    private String mUID;
    private String mPassword;

    private EditText mUIDView;
    private EditText mPasswordView;
    private Button mJoinButton;
    private ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_join_project, container, false);
        progress = new ProgressDialog(rootView.getContext());
        progress.setTitle("Joining Project");
        progress.setMessage("Please Wait...");
        progress.dismiss();
        mUIDView = (EditText) rootView.findViewById(R.id.joinProjectUID);
        mPasswordView = (EditText) rootView.findViewById(R.id.joinProjectPassword);
        mJoinButton = (Button) rootView.findViewById(R.id.joinProjectSubmitButton);
        mJoinButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.joinProjectSubmitButton) {
            progress.show();
            //click Join Submit Button
            mUIDView.setError(null);
            mPasswordView.setError(null);

            mUID = mUIDView.getText().toString();
            mPassword = mPasswordView.getText().toString();

            if (!errorChecking()) {
                progress.dismiss();
                return;
            }

            ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
            query.whereEqualTo("UID", mUID);
            query.getFirstInBackground(new GetCallback<Project>() {
                @Override
                public void done(Project object, ParseException e) {
                    if (object != null && e == null) {
                        String hashedPassword = object.getPassword();
                        if (BCrypt.checkpw(mPassword, hashedPassword)) {
                            //Password correct
                            JSONArray usersName = object.getUser();
                            JSONArray usersEmail = object.getUserEmail();
                            //check if you have already joined this project
                            for (int i = 0; i < usersEmail.length(); i++) {
                                try {
                                    if (ParseUser.getCurrentUser().getUsername()
                                            .compareTo(usersEmail.getString(i)) == 0) {
                                        progress.dismiss();
                                        Toast.makeText(getActivity(),
                                                       "You have already joined this project.",
                                                       Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                                catch (JSONException exc) {
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                                }
                            }
                            usersName.put(ParseUser.getCurrentUser().getString("name"));
                            usersEmail.put(ParseUser.getCurrentUser().getString("username"));
                            object.addUsers(usersName);
                            object.addUserEmails(usersEmail);
                            object.saveInBackground();
                            progress.dismiss();
                            clearEditText();
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            //Password is incorrect
                            progress.dismiss();
                            mPasswordView.setError(getString(R.string.joinProjectNotFound));
                            mPasswordView.requestFocus();
                            Toast.makeText(getActivity(), R.string.joinProjectNotFoundToast, Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    else if (e != null && object == null) {
                        progress.dismiss();
                        mPasswordView.setError(getString(R.string.joinProjectNotFound));
                        mPasswordView.requestFocus();
                        Toast.makeText(getActivity(), R.string.joinProjectNotFoundToast, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            });

        }
    }

    private boolean errorChecking() {
        if (mUID.isEmpty()) {
            mUIDView.setError(getString(R.string.blankField));
            mUIDView.requestFocus();
            return false;
        }
        else if (mPassword.isEmpty()) {
            mPasswordView.setError(getString(R.string.blankField));
            mPasswordView.requestFocus();
            return false;
        }
        else {
            return true;
        }
    }

    private void clearEditText() {
        mUIDView.setText("");
        mPasswordView.setText("");
    }

}