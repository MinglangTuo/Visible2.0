package com.example.visible2.json_obj;

public class user_info {

    public _user user;

    public _user get_user_info() {
        return user;
    }
    public static class _user {
        public int credits;
        public String _id;
        public String nickname;
        public String password;
        public String email;
        public String created_at;
        public int __v;
    }
}
