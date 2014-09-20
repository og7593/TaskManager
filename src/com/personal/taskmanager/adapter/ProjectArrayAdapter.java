package com.personal.taskmanager.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.personal.taskmanager.R;
import com.personal.taskmanager.activities.EditProjectActivity;
import com.personal.taskmanager.activities.ProjectHomeScreenActivity;
import com.personal.taskmanager.parseobjects.Project;

public class ProjectArrayAdapter extends ArrayAdapter<Project> {
  private int mListItemLayoutResId;
  private String[] months = {"January", "February", "March", "April", "May", "June", "July", 
      "August", "September", "October", "November", "December"};

  public ProjectArrayAdapter(Context context, List<Project> projectList) {
      super(context, R.layout.my_project_row, projectList);
      mListItemLayoutResId = R.layout.my_project_row;
  }

  @Override
  public android.view.View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater)getContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View listItemView = convertView;
    if (null == convertView) { 
      listItemView = inflater.inflate(mListItemLayoutResId, parent, false);

      // The ListItemLayout must use the standard text item IDs.
      TextView lineOneView = (TextView)listItemView.findViewById(R.id.text1);
      TextView lineTwoView = (TextView)listItemView.findViewById(R.id.text2);
      TextView lineThreeView = (TextView) listItemView.findViewById(R.id.text3);

      //Formatting text views
      final Project project = (Project) getItem(position); 
      lineOneView.setText(lineOneText(project));
      lineTwoView.setText("Due on " + dateTimeFormat(lineTwoText(project)));
      lineThreeView.setText(lineThreeText(project));
      if (project.getCompleted()) {
        lineOneView.setTypeface(lineOneView.getTypeface(), Typeface.ITALIC);
        lineOneView.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        lineOneView.setPaintFlags(lineOneView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        lineTwoView.setTypeface(lineTwoView.getTypeface(), Typeface.ITALIC);
        lineTwoView.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        lineTwoView.setPaintFlags(lineTwoView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        lineThreeView.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        lineThreeView.setPaintFlags(lineTwoView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      }

      // Edit Button Listener
      ImageButton button = (ImageButton) listItemView.findViewById(R.id.editProjectButton);
      button.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          if (ParseUser.getCurrentUser().getObjectId().
              compareTo(project.getAdmin().getObjectId()) == 0) {
            Intent intent = new Intent(v.getContext(), EditProjectActivity.class);
            intent.putExtra("project name", lineOneText(project));
            intent.putExtra("project uid", project.getUID());
            intent.putExtra("project status", project.getStatus());
            v.getContext().startActivity(intent);
          }
          else {
            Toast.makeText(getContext(), 
                           "Only administrator can edit project details.",
                           Toast.LENGTH_LONG).show();
          }
        }
      });

      //List View Listener
      listItemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(v.getContext(), ProjectHomeScreenActivity.class);
          intent.putExtra("project uid", project.getUID());
          intent.putExtra("project name", lineOneText(project));
          try {
            intent.putExtra("project admin", project.getAdmin().fetchIfNeeded().getString("name"));
          } catch (ParseException e) {
            e.printStackTrace();
          }
          v.getContext().startActivity(intent);
        }
      });
    }
    return listItemView;
  }
  

  public String lineOneText(Project project) {
    return project.getName();
  }

  public String lineTwoText(Project project) {
    return project.getDueDate();
  }
  
  public String lineThreeText(Project project) {
    return project.getStatus();
  }
  
  private String dateTimeFormat(String date) {
    int monthIndex = date.indexOf("/");
    String output = months[Integer.parseInt(date.substring(0, monthIndex))];
    int dayIndex = date.indexOf("/") + 1;
    int dayIndex2 = date.indexOf("/", dayIndex);
    output = output + (" " + Integer.parseInt(date.substring(dayIndex, dayIndex2)) + ", ");
    int yearIndex = date.indexOf("/", dayIndex) + 1;
    output =  output + (date.substring(yearIndex, yearIndex+4) + " at ");
    int timeIndex = date.lastIndexOf("a") + 3;
    String time = date.substring(timeIndex);
    int hour = Integer.parseInt(time.substring(0, time.indexOf(":")));
    String AmPm = "AM";
    if (hour >= 12) {
      hour -= 12;
      AmPm = "PM";
    }
    String hourString = Integer.toString(hour);
    String minute = time.substring(time.indexOf(":") + 1);
    if (Integer.parseInt(minute) < 10) {
      minute = "0" + minute;
    }
    output =  output + (hourString + ":" + minute + AmPm);
    return output;
  }
  
}