package com.cristiandrami.football365.ui.bets;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.databinding.FragmentLikedNewsBinding;
import com.cristiandrami.football365.model.internal_database.InternalDatabaseHandler;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.cristiandrami.football365.ui.news.NewsRecyclerViewHandler;
import com.cristiandrami.football365.ui.news.NewsRecyclerViewItemModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LikedNewsFragment extends Fragment {

    private FragmentLikedNewsBinding binding;
    private ShimmerFrameLayout shimmerFrameLayoutRecyclerView;
    private List<NewsRecyclerViewItemModel> recyclerViewItems = new ArrayList<>();
    private NewsRecyclerViewHandler recyclerViewHandler;
    private RecyclerView recyclerViewNews;
    private LinearLayoutManager layoutNewsRecyclerViewManager;
    private View root;
    private LikedNewsViewModel likedNewsViewModel;
    private InternalDatabaseHandler internalDB;

    public void setRecyclerViewItems(List<NewsRecyclerViewItemModel> recyclerViewItems) {
        this.recyclerViewItems = recyclerViewItems;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        likedNewsViewModel =
                new ViewModelProvider(this).get(LikedNewsViewModel.class);

        binding = FragmentLikedNewsBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        shimmerFrameLayoutRecyclerView = binding.shimmerViewContainerNewsFragment;
        likedNewsViewModel.setShimmerFrameLayout(shimmerFrameLayoutRecyclerView);
        shimmerFrameLayoutRecyclerView.startShimmer();
        likedNewsViewModel.setFragment(this);
        likedNewsViewModel.setNewsList(recyclerViewItems);


        //i need before initData because i have to set from the database the news and then
        //i can set the recycler view
        initData();
        initRecyclerView();


        //recyclerViewHandler.notifyDataSetChanged();


        setTimerToManageChangesOnRecyclerView();


        return root;
    }

    private void setTimerToManageChangesOnRecyclerView() {
        int delay = 1200;   // delay for 5 sec.
        int period=500;

        Timer timer = new Timer();


        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                Log.e("size", String.valueOf(recyclerViewItems.size()));
                if(recyclerViewItems.size()==0){
                    makeNoNewsGraphicVisible(View.VISIBLE);
                }
                else {
                    makeNoNewsGraphicVisible(View.INVISIBLE);
                }

            }
        }, delay, period);
    }


    /**
     * This method is used to create the internal database if it doesn't exists
     * Then it retrieves the daily news from the internal database and sets the recycler view item list
     * with the news item {@link NewsRecyclerViewItemModel}
     */
    private void initData() {
        likedNewsViewModel.refreshNewsList(recyclerViewItems);

    }



    private void makeNoNewsGraphicVisible(int visible) {
        if(getActivity()!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    root.findViewById(R.id.no_liked_news_icon).setVisibility(visible);
                    root.findViewById(R.id.no_liked_news_string).setVisibility(visible);

                    //shimmerFrameLayoutRecyclerView.setVisibility(View.GONE);
                    //shimmerFrameLayoutRecyclerView.stopShimmer();
                }
            });
        }


    }


    /**
     * This method is used to setup the recycler view that will show the liked news
     */
    private void initRecyclerView() {

        recyclerViewNews = binding.likedNewsRecyclerViewInFragment;
        // layout manager initialization
        layoutNewsRecyclerViewManager = new LinearLayoutManager(this.getContext());
        //the layout have an vertical orientation
        layoutNewsRecyclerViewManager.setOrientation(RecyclerView.VERTICAL);

        recyclerViewNews.setLayoutManager(layoutNewsRecyclerViewManager);
        recyclerViewHandler = new NewsRecyclerViewHandler(recyclerViewItems, this.getContext());

        recyclerViewNews.setAdapter(recyclerViewHandler);

        likedNewsViewModel.setRecyclerViewHandler(recyclerViewHandler);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        makeNoNewsGraphicVisible(View.INVISIBLE);
        binding = null;

    }

    public void updateList(List<NewsRecyclerViewItemModel> newsList) {
        this.recyclerViewItems = newsList;


        recyclerViewHandler.notifyDataSetChanged();


    }
}