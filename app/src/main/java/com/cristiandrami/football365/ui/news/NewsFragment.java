package com.cristiandrami.football365.ui.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.databinding.FragmentNewsBinding;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;

    private final List<NewsRecyclerViewItemModel> recyclerViewItems = new ArrayList<>();
    private NewsRecyclerViewHandler recyclerViewHandler;
    private RecyclerView recyclerViewNews;
    private LinearLayoutManager layoutNewsRecyclerViewManager;
    private View view;

    private boolean alreadyCalled = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            alreadyCalled = savedInstanceState.getBoolean("api_called", true);
        }
    }




    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("api_called", alreadyCalled);
    }







    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewsViewModel homeViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        initData();
        initRecyclerView();
        return root;
    }

    private void initRecyclerView() {

        recyclerViewNews = binding.newsRecyclerViewInFragment;
        // layout manager initialization
        layoutNewsRecyclerViewManager = new LinearLayoutManager(this.getContext());
        //the layout have an vertical orientation
        layoutNewsRecyclerViewManager.setOrientation(RecyclerView.VERTICAL);

        recyclerViewNews.setLayoutManager(layoutNewsRecyclerViewManager);
        recyclerViewHandler = new NewsRecyclerViewHandler(recyclerViewItems, this.getContext());

        recyclerViewNews.setAdapter(recyclerViewHandler);
        //recyclerViewHandler.notifyDataSetChanged();

    }

    private void initData() {

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("https://api-football-v1.p.rapidapi.com/v3/leagues")
                .get()
                .addHeader("x-rapidapi-host", "api-football-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "1f1c8c92c3msh6d7222e6dcfc7c0p1cb4cejsnfcd173de0933")
                .build();


        if (!alreadyCalled) {
            Log.e("api:", "api called");
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {
                    Toast.makeText(getContext(), "API error", Toast.LENGTH_SHORT).show();
                    Log.e("api", "fail");
                }

                @Override
                public void onResponse(Response response) throws IOException {

                    if (response.isSuccessful()) {

                        //.toString doesn't work, .string() yes, why???
                        final String newsData = response.body().string();


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONArray jsonNewsDataArray = new JSONObject(newsData).getJSONArray("response");

                                    for (int i = 0; i < 20; i++) {
                                        JSONObject leagueObject = jsonNewsDataArray.getJSONObject(i).getJSONObject("league");

                                        String name = String.valueOf(leagueObject.get("name"));
                                        String image = String.valueOf(leagueObject.get("logo"));
                                        String description = String.valueOf(leagueObject.get("type"));
                                        NewsRecyclerViewItemModel itemModel = new NewsRecyclerViewItemModel(image, name, description);

                                        recyclerViewItems.add(itemModel);
                                        Log.e("app", name);


                                    }

                                    //I have to notify the changing here, without it i have to lock and unlock mobile phone to see the Recycler list
                                    recyclerViewHandler.notifyDataSetChanged();
                                    alreadyCalled = true;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                    }

                }


            });
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}