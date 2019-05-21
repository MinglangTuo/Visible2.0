package com.example.visible2.json_obj;

import java.util.List;

public class tasks {

    public List<task_info> task;

    public static class task_info{
        public String _id;
        public int state;
        public String dates;
        public String content;
        public String owner;
        public String created_at;
        public int __v;
    }

    public List<task_info> get_task(){
        return task;
    }
}
