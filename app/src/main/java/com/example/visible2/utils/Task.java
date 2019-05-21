package com.example.visible2.utils;

public class Task {
    public String Owner;
    public String TaskName;
    public String TaskDate;
    public String TaskDuration;
    public boolean TaskCompleted;

    public Task (String owner, String taskname, String taskdate, String taskduration, boolean completed){
        Owner = owner;
        TaskName = taskname;
        TaskDate = taskdate;
        TaskDuration = taskduration;
        TaskCompleted = completed;
    }
}
