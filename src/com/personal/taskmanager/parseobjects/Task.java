package com.personal.taskmanager.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Task")
public class Task extends ParseObject implements Comparable<Task> {
  
  public Task() {
    //Default Constructor
  }
  
  public String getAssignedTo() {
    return getString("assignedTo");
  }
  
  public void setAssignedTo(String user) {
    put("assignedTo", user);
  }
  
  public String getName() {
    return getString("name");
  }
  
  public void setTaskName(String name) {
    put("name", name);
  }
  
  public String getDescription() {
    return getString("description");
  }
  
  public void setDescription(String description) {
    put("description", description);
  }
  
  //If true, task completed
  //If false, task in progress
  public boolean getCompleted() {
    return getBoolean("completed");
  }
  
  public void setCompleted(boolean status) {
    put("completed", status);
  }
  
  public String getNotes() {
    return getString("notes");
  }
  
  public void setNotes(String notes) {
    put("notes", notes);
  }
  
  public String getDueDate() {
    return getString ("dueDate");
  }
  
  public void setDueDate(String dueDate) {
    put("dueDate", dueDate);
  }
  
  public String getProjectUID() {
    return getString("projectUID");
  }
  
  public void setProjectUID(String projectUID) {
    put("projectUID", projectUID);
  }
  
  public String getStatus() {
    return getString("status");
  }
  
  public void setStatus(String status) {
    put ("status", status);
    if (status.compareTo("Completed") == 0) {
      setCompleted(true);
    }
    else {
      setCompleted(false);
    }
  }
  
  public String getProjectAdmin() {
    return getString("projectAdmin");
  }
  
  public void setProjectAdmin(String admin) {
    put ("projectAdmin", admin);
  }
  
  public String getProjectName() {
    return getString("projectName");
  }
  
  public void setProjectName(String name) {
    put("projectName", name);
  }
  
  @Override
  public int compareTo(Task task) {
    return this.getName().compareTo(task.getName());
  }
}
