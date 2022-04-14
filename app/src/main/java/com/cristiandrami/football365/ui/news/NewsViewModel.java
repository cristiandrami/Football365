package com.cristiandrami.football365.ui.news;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.Utilities;
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
import java.util.ArrayList;
import java.util.List;

public class NewsViewModel extends ViewModel {


    public NewsViewModel() {

    }


    public List<NewsRecyclerViewItemModel> getNewsList(InternalDatabaseHandler internalDB, Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        NewsDatabaseModel newsFromDatabase = internalDB.getNews();

        List<NewsRecyclerViewItemModel> newsList = new ArrayList<NewsRecyclerViewItemModel>();

        long millisDifferenceToUpdate = currentTimeMillis - newsFromDatabase.getDate();
        Log.e("data difference: ", String.valueOf(currentTimeMillis - newsFromDatabase.getDate()));
        if (millisDifferenceToUpdate > Utilities.NEWS_FREQUENCY_UPDATE || newsFromDatabase.getDate()==0) {
            internalDB.deleteNews();

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://google-news1.p.rapidapi.com/search?q=" + context.getString(R.string.news_query) + "&country=" + context.getString(R.string.news_country) + "&lang=" + context.getString(R.string.news_language) + "&limit=25&when=2d")
                    .get()
                    .addHeader("X-RapidAPI-Host", "google-news1.p.rapidapi.com")
                    .addHeader("X-RapidAPI-Key", "1f1c8c92c3msh6d7222e6dcfc7c0p1cb4cejsnfcd173de0933")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                    Log.e("api", e.toString());
                }

                @Override
                public void onResponse(Response response) throws IOException {

                    if (response.isSuccessful()) {

                        //.toString doesn't work, .string() yes, why???
                        final String newsData = response.body().string();


                        NewsDatabaseModel newsToUpdate= new NewsDatabaseModel();
                        newsToUpdate.setNews(newsData);
                        newsToUpdate.setDate(currentTimeMillis);
                        internalDB.insertDailyNews(newsToUpdate);



                    }

                }


            });

        }
        updateNewsArray(newsList, internalDB);




        return newsList;

    }

    private void updateNewsArray(List<NewsRecyclerViewItemModel> newsList, InternalDatabaseHandler internalDB) {

        String newsData= internalDB.getNews().getNews();
        //Log.e("news: ", newsData);

        try {
            JSONArray jsonNewsDataArray = new JSONObject(newsData).getJSONArray("articles");

            for (int i = 0; i < jsonNewsDataArray.length(); i++) {
                JSONObject leagueObject = jsonNewsDataArray.getJSONObject(i);

                Log.e("api ", leagueObject.toString());
                String name = String.valueOf(leagueObject.get("title"));
                //String image = String.valueOf(leagueObject.get("thumbnail"));
                String description = String.valueOf(leagueObject.get("published_date"));

                // Log.e("title ", name);
                NewsRecyclerViewItemModel itemModel = new NewsRecyclerViewItemModel("", name, description);

                newsList.add(itemModel);
                // Log.e("app", name);
            }

            //I have to notify the changing here, without it i have to lock and unlock mobile phone to see the Recycler list
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("app", e.toString());
        }
    }
}