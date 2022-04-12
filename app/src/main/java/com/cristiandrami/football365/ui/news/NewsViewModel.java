package com.cristiandrami.football365.ui.news;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cristiandrami.football365.model.internalDatabase.InternalDatabaseHandler;
import com.cristiandrami.football365.model.internalDatabase.NewsDatabaseModel;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsViewModel extends ViewModel {



    public NewsViewModel() {

    }




    public List<NewsRecyclerViewItemModel> getNewsList(InternalDatabaseHandler internalDB) {
        long currentTimeMillis=System.currentTimeMillis();
        Date currentDate= new Date(currentTimeMillis);
        NewsDatabaseModel newsFromDatabase= internalDB.getNews();

        List<NewsRecyclerViewItemModel> newsList = new ArrayList<NewsRecyclerViewItemModel>();
        if (!currentDate.toString().equals(new Date(newsFromDatabase.getDate()).toString())) {
            internalDB.deleteNews();

            OkHttpClient client = new OkHttpClient();


            Request request = new Request.Builder()
                    .url("https://api-football-v1.p.rapidapi.com/v3/leagues")
                    .get()
                    .addHeader("x-rapidapi-host", "api-football-v1.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "1f1c8c92c3msh6d7222e6dcfc7c0p1cb4cejsnfcd173de0933")
                    .build();

            Log.e("date", "i will update");

            //TODO call API and store news in database


            /*
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


            });*/


        }

        return newsList;

    }
}