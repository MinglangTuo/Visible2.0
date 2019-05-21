package com.example.visible2.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class Utils {

    public Utils(){}

//    public static void parseJsonWithJsonObject(Response response) throws IOException {
//        String responseData=response.body().string();
//        try{
//            JSONArray jsonArray=new JSONArray(responseData);
//            for(int i=0;i<jsonArray.length();i++)
//            {
//                JSONObject jsonObject=jsonArray.getJSONObject(i);
//                String id=jsonObject.getString("id");
//                String name=jsonObject.getString("name");
//                idList.add(id);
//                nameList.add(name);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
