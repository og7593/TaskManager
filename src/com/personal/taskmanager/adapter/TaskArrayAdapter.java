package com.personal.taskmanager.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.personal.taskmanager.R;
import com.personal.taskmanager.activities.EditTaskActivity;
import com.personal.taskmanager.activities.TaskDetailsActivity;
import com.personal.taskmanager.parseobjects.Task;

public class TaskArrayAdapter extends ArrayAdapter<Task> {
  
  private int mListLayoutResId;
  private String[] months = {"January", "February", "March", "April", "May", "June", "July", 
      "August", "September", "October", "November", "December"};
  
  public TaskArrayAdapter(Context context, List<Task> taskList) {
    super(context, R.layout.my_task_row, taskList);
    mListLayoutResId = R.layout.my_task_row;
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) getContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    View listItemView = convertView;
    if (convertView == null) {
      listItemView = inflater.inflate(mListLayoutResId, parent, false);
      
      TextView lineOneView = (TextView) listItemView.findViewById(R.id.text1);
      TextView lineTwoView = (TextView) listItemView.findViewById(R.id.text2);
      TextView lineThreeView = (TextView) listItemView.findViewById(R.id.text3);
      TextView lineFourView = (TextView) listItemView.findViewById(R.id.text4);
      
      //formatting textviews
      final Task task = getItem(position);
      lineOneView.setText(lineOneText(task));
      lineTwoView.setText("Due on " + dateTimeFormat(lineTwoText(task)));
      lineThreeView.setText("Assigned to: " + lineThreeText(task));
      lineFourView.setText("Status: " + lineFourText(task));
      if (task.getCompleted()) {
        lineOneView.setTypeface(lineOneView.getTypeface(), Typeface.ITALIC);
        lineOneView.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        lineOneView.setPaintFlags(lineOneView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        lineTwoView.setTypeface(lineTwoView.getTypeface(), Typeface.ITALIC);
        lineTwoView.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        lineTwoView.setPaintFlags(lineTwoView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        lineThreeView.setTypeface(lineTwoView.getTypeface(), Typeface.ITALIC);
        lineThreeView.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        lineThreeView.setPaintFlags(lineTwoView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        lineFourView.setTypeface(lineTwoView.getTypeface(), Typeface.ITALIC);
        lineFourView.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        lineFourView.setPaintFlags(lineTwoView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      }
      
      //Edit button listener
      ImageButton button = (ImageButton) listItemView.findViewById(R.id.editTaskButton);
      button.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          if (ParseUser.getCurrentUser().getString("name").compareTo(task.getAssignedTo()) == 0) {
            Intent intent = new Intent(v.getContext(), EditTaskActivity.class); //change to edit task
            intent.putExtra("project uid", task.getProjectUID());
            intent.putExtra("task name", lineOneText(task));
            intent.putExtra("task uid", task.getObjectId());
            intent.putExtra("task status", lineFourText(task));
            intent.putExtra("project name", task.getProjectName());
            intent.putExtra("project admin", task.getProjectAdmin());
            v.getContext().startActivity(intent);
          }
          else {
            Toast.makeText(getContext(), 
                           "Only assignee can edit task details.",
                           Toast.LENGTH_LONG).show();
          }
        }
      });
      
      // list view listener
      listItemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(v.getContext(), TaskDetailsActivity.class); // change to view task
          intent.putExtra("project uid", task.getProjectUID());
          intent.putExtra("task name", lineOneText(task));
          intent.putExtra("task uid", task.getObjectId());
          v.getContext().startActivity(intent);
        }
      });
      
    }
    
    return listItemView;
  }
  
  public String lineOneText(Task task) {
    return task.getName();
  }

  public String lineTwoText(Task task) {
    return task.getDueDate();
  }
  
  public String lineThreeText(Task task) {
    return task.getAssignedTo();
  }
  
  public String lineFourText(Task task) {
    return task.getStatus();
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
