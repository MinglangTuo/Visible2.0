package com.example.visible2.json_obj;

import java.util.List;

public class ranks {

    public List<rank_info> rank;

    public static class rank_info{
        public int credits;
        public String _id;
        public String nickname;
        public String password;
        public String email;
        public String created_at;
        public int __v;
    }

    public List<rank_info> get_rank(){
        return rank;
    }
}
