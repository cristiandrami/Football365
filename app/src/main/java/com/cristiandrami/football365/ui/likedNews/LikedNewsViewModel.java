package com.cristiandrami.football365.ui.likedNews;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.cristiandrami.football365.model.utilities.UtilitiesNumbers;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.cristiandrami.football365.ui.news.NewsRecyclerViewHandler;
import com.cristiandrami.football365.ui.news.NewsRecyclerViewItemModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class LikedNewsViewModel extends ViewModel {

    private ShimmerFrameLayout shimmerFrameLayout;

    public void setRecyclerViewHandler(NewsRecyclerViewHandler recyclerViewHandler) {
        this.recyclerViewHandler = recyclerViewHandler;
    }

    private NewsRecyclerViewHandler recyclerViewHandler;

    public void setLikedNewsFragment(LikedNewsFragment likedNewsFragment) {
        this.likedNewsFragment = likedNewsFragment;
    }

    private List<NewsRecyclerViewItemModel> newsList = new ArrayList<>();
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private LikedNewsFragment likedNewsFragment;

    public List<NewsRecyclerViewItemModel> getNewsList() {
        return newsList;
    }
    public void setNewsList(List<NewsRecyclerViewItemModel> list) {
         this.newsList=list;
    }

    public LikedNewsViewModel() {
        /***
         * This is an empty constructor
         */
    }


    public void setShimmerFrameLayout(ShimmerFrameLayout shimmerFrameLayoutRecyclerView) {
        this.shimmerFrameLayout = shimmerFrameLayoutRecyclerView;
    }

    public void setHandler(NewsRecyclerViewHandler recyclerViewHandler) {
        this.recyclerViewHandler = recyclerViewHandler;

    }

    public void refreshNewsList(List<NewsRecyclerViewItemModel> recyclerViewItems) {
        newsList.clear();

        fireStore.collection(UtilitiesStrings.FIREBASE_LIKED_NEWS_USERS_COLLECTION).
                document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection(UtilitiesStrings.FIREBASE_LIKED_NEWS_SINGLE_USER_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                try {
                                    Gson gsonObject = new Gson();
                                    String jsonArticle = gsonObject.toJson(document.getData().values());
                                    JSONArray jsonArray= null;
                                    jsonArray = new JSONArray(jsonArticle);
                                    String article = (String) jsonArray.get(0);
                                    NewsRecyclerViewItemModel news = gsonObject.fromJson(article, NewsRecyclerViewItemModel.class);

                                    news.setType(UtilitiesNumbers.LIKED_NEWS_TYPE);


                                    Log.e("article", news.toString());


                                    newsList.add(news);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                            //dismissShimmer();

                            recyclerViewHandler.notifyDataSetChanged();
                            dismissShimmer();
                            //likedNewsFragment.updateList(newsList);


                        }


                    }
                });



    }




    private void dismissShimmer() {
        try {
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFragment(LikedNewsFragment likedNewsFragment) {

        this.likedNewsFragment= likedNewsFragment;
    }
}