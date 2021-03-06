package com.wenxin.dongyouji.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.wenxin.dongyouji.R;
import com.wenxin.dongyouji.Url;
import com.wenxin.dongyouji.activity.RaidersActivity;
import com.wenxin.dongyouji.activity.TravelNotesActivity;
import com.wenxin.dongyouji.adapter.RaidersAdapter;
import com.wenxin.dongyouji.listener.RecyclerItemClickListener;
import com.wenxin.dongyouji.model.RaidersModel;
import com.wenxin.dongyouji.model.TravelNotesModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linSir on 17/3/11.攻略
 */
public class RaidersFragment extends Fragment {

    private RecyclerView recyclerView;
    private RaidersAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<RaidersModel> mLists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raider, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new RaidersAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), onItemClickListener));

        String url = Url.url + "get_all_raiders";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("null", "null")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), "获取游记失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {

                        List<RaidersModel> lists = new ArrayList<RaidersModel>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                RaidersModel raidersModel = new RaidersModel(
                                        jsonArray.getJSONObject(i).optString("title"),
                                        jsonArray.getJSONObject(i).optString("description"),
                                        jsonArray.getJSONObject(i).optString("details_description"),
                                        jsonArray.getJSONObject(i).optString("background"),
                                        jsonArray.getJSONObject(i).optString("text1"),
                                        jsonArray.getJSONObject(i).optString("img1"),
                                        jsonArray.getJSONObject(i).optString("text2"),
                                        jsonArray.getJSONObject(i).optString("img2"),
                                        jsonArray.getJSONObject(i).optString("text3"),
                                        jsonArray.getJSONObject(i).optString("img3")
                                );

                                lists.add(raidersModel);

                            }

                            mLists = lists;
                            adapter.refreshList(lists);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });






        return view;
    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            if (position == mLists.size()){
                return;
            }

            Intent intent = new Intent(getActivity(), RaidersActivity.class);
            intent.putExtra("title", mLists.get(position).getTitle());
            intent.putExtra("description",mLists.get(position).getDescription());
            intent.putExtra("details_description",mLists.get(position).getDetails_description());
            intent.putExtra("background",mLists.get(position).getBackground());
            intent.putExtra("text1",mLists.get(position).getText1());
            intent.putExtra("img1",mLists.get(position).getImg1());
            intent.putExtra("text2",mLists.get(position).getText2());
            intent.putExtra("img2",mLists.get(position).getImg2());
            intent.putExtra("text3",mLists.get(position).getText3());
            intent.putExtra("img3",mLists.get(position).getImg3());

            startActivity(intent);
        }
    };

}
