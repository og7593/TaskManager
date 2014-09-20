package com.personal.taskmanager.parseobjects;

import org.json.JSONArray;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Project")
public class Project extends ParseObject implements Comparable<Project> {
  public Project() {
    //Default Constructor
  }
  
  public String getName() {
    return getString("name");
  }
  
  public void setName(String name) {
    put("name", name);
  }
  
  public String getUID() {
    return getString("UID");
  }
  
  public void setUID(String UID) {
    put("UID", UID);
  }
  
  public String getPassword() {
    return getString("password");
  }
  
  public void setPassword(String password) {
    put("password", password);
  }
  
  public String getDescription() {
    return getString("description");
  }
  
  public void setDescription(String projectDescription) {
    put("description", projectDescription);
  }
  
  public String getDueDate() {
    return getString("dueDate");
  }
  
  public void setDueDate(String dueDate) {
    put("dueDate", dueDate);
  }
  
  public JSONArray getUser() {
    return getJSONArray("users");
  }
  
  public void addUsers(JSONArray users) {
    put("users", users);
  }
  
  public JSONArray getUserEmail() {
    return getJSONArray("usersEmail");
  }
  
  public void addUserEmails(JSONArray userEmails) {
    put("usersEmail", userEmails);
  }
  
  public ParseUser getAdmin() {
    return  getParseUser("administrator");
  }
  public void setAdmin(ParseUser user) {
    put ("administrator", user);
  }
  
  public JSONArray getTasks() {
    return getJSONArray("tasks");
  }
  
  public void addTasks(JSONArray tasks) {
    put ("tasks", tasks);
  }
  
  //If true, project completed
  //If false, project in progress
  public boolean getCompleted() {
    return getBoolean("completed");
  }
  
  public void setCompleted(boolean status) {
    put("completed", status);
  }
  
  public String getStatus() {
    return getString("status");
  }
  
  public void setStatus(String status) {
    put("status", status);
    if (status.compareTo("Completed") == 0) {
      setCompleted(true);
    }
    else {
      setCompleted(false);
    }
  }

  @Override
  public int compareTo(Project another) {
    return this.getName().compareTo(another.getName());
  }
}
