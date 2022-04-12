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
import com.cristiandrami.football365.model.internalDatabase.InternalDatabaseHandler;
import com.cristiandrami.football365.model.internalDatabase.NewsDatabaseModel;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;

    private List<NewsRecyclerViewItemModel> recyclerViewItems = new ArrayList<>();
    private NewsRecyclerViewHandler recyclerViewHandler;
    private RecyclerView recyclerViewNews;
    private LinearLayoutManager layoutNewsRecyclerViewManager;
    private View view;
    private NewsViewModel newsViewModel;

    private InternalDatabaseHandler internalDB;

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
         newsViewModel =
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

    private void initData(){
        internalDB=new InternalDatabaseHandler(getContext());
        recyclerViewItems=newsViewModel.getNewsList(internalDB);

            long millis=System.currentTimeMillis();


            // creating a new object of the class Date

             /*
            NewsDatabaseModel newsToInsert= new NewsDatabaseModel();
            newsToInsert.setNews("ciao");
            newsToInsert.setDate(millis);
            internalDB.insertDailyNews(newsToInsert);
            */

            //internalDB.createTableNews();

            //NewsDatabaseModel news= internalDB.getNews();
            //NewsRecyclerViewItemModel itemModel = new NewsRecyclerViewItemModel("", news.getNews(), new Date(news.getDate()).toString());
            //recyclerViewItems.add(itemModel);

          /*
            Date date = new Date(millis);
            Log.e("date", date.toString());

            NewsDatabaseModel newsToInsert= new NewsDatabaseModel();
            newsToInsert.setNews("ciao");
            newsToInsert.setDate(millis);
            internalDB.insertDailyNews(newsToInsert);
            */
            //internalDB.deleteNews();
            //long millis=System.currentTimeMillis();

            //internalDB.dropTableNews();




            // creating a new object of the class Date

            //recyclerViewHandler.notifyDataSetChanged();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}