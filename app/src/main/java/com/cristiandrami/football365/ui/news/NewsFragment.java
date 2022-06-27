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
import com.cristiandrami.football365.model.internal_database.InternalDatabaseHandler;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is used as a Controller for the News Fragment
 * It contains all methods to manage the News Fragment Graphic.
 * It contains an object used to manage the Model of News Fragment
 *
 * @author Cristian D. Dramisino
 * @see NewsViewModel
 */
public class NewsFragment extends Fragment {

    public static NewsRecyclerViewHandler recyclerViewHandler;
    private FragmentNewsBinding binding;
    private ShimmerFrameLayout shimmerFrameLayoutRecyclerView;
    private List<NewsRecyclerViewItemModel> recyclerViewItems = new ArrayList<>();
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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);


        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        shimmerFrameLayoutRecyclerView = binding.shimmerViewContainerNewsFragment;
        newsViewModel.setShimmerFrameLayout(shimmerFrameLayoutRecyclerView);
        shimmerFrameLayoutRecyclerView.startShimmer();

        initData();
        initRecyclerView();


        return root;
    }

    /**
     * This method is used to setup the recycler view that will show the daily news
     */
    private void initRecyclerView() {

        recyclerViewNews = binding.newsRecyclerViewInFragment;
        // layout manager initialization
        layoutNewsRecyclerViewManager = new LinearLayoutManager(this.getContext());
        //the layout have an vertical orientation
        layoutNewsRecyclerViewManager.setOrientation(RecyclerView.VERTICAL);

        recyclerViewNews.setLayoutManager(layoutNewsRecyclerViewManager);
        recyclerViewHandler = new NewsRecyclerViewHandler(recyclerViewItems, this.getContext());

        recyclerViewNews.setAdapter(recyclerViewHandler);

        newsViewModel.setHandler(recyclerViewHandler);

        startTimerToUpdateLikedNews();


    }

    private void startTimerToUpdateLikedNews() {
        int delay = 1000;   // delay for 5 sec.
        int period = 3000;

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewHandler.notifyDataSetChanged();
                    }
                };


                if (getActivity() == null)
                    return;

                getActivity().runOnUiThread(runnable);


            }
        }, delay, period);
    }


    /**
     * This method is used to create the internal database if it doesn't exists
     * Then it retrieves the daily news from the internal database and sets the recycler view item list
     * with the news item {@link NewsRecyclerViewItemModel}
     */
    private void initData() {
        internalDB = new InternalDatabaseHandler(getContext());
        recyclerViewItems = newsViewModel.refreshNewsList(internalDB, getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}