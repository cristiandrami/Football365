package com.cristiandrami.football365.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cristiandrami.football365.databinding.FragmentNewsBinding;
import com.cristiandrami.football365.model.internalDatabase.InternalDatabaseHandler;

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

        //i need before initData because i have to set from the database the news and then
        //i can set the recycler view
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

        newsViewModel.setHandler(recyclerViewHandler);

    }

    private void initData(){
        internalDB=new InternalDatabaseHandler(getContext());
        recyclerViewItems = newsViewModel.refreshNewsList(internalDB, getContext());

        /*for (NewsRecyclerViewItemModel item: recyclerViewItems){
            Log.e("test", item.getNewsTitle());
        }
        Log.e("test size",String.valueOf( recyclerViewItems.size()));*/

        //recyclerViewHandler.notifyDataSetChanged();




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




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}