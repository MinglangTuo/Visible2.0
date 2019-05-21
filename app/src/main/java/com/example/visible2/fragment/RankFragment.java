package com.example.visible2.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.visible2.MainActivity;
import com.example.visible2.R;
import com.example.visible2.adapter.RecyclerviewAdapter;
import com.example.visible2.adapter.TaskAdapter;
import com.example.visible2.json_obj.ranks;
import com.example.visible2.json_obj.tasks;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RankFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    public RecyclerviewAdapter adapter;
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    public List<ranks.rank_info> mDataset;


    public void feed_dataset(List<ranks.rank_info> dataset) {
        mDataset = dataset;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /** load the root view (fragment_task.xml) **/
        View rootView = inflater.inflate(R.layout.fragment_rank, container, false);

        /** load the recycler view from root view **/
        recyclerView = rootView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        /** specify the layout manager **/
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        /** specify the adapter **/
        mDataset = ((MainActivity) getActivity()).get_rDataset();
        adapter = new RecyclerviewAdapter(mDataset);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

}
