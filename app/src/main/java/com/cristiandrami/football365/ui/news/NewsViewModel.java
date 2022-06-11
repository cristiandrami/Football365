package com.cristiandrami.football365.ui.news;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.internal_database.InternalDatabaseHandler;
import com.cristiandrami.football365.model.internal_database.NewsDatabaseModel;
import com.cristiandrami.football365.model.news.NewsUtilities;
import com.cristiandrami.football365.model.utilities.UtilitiesNumbers;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the model object of the {@link NewsFragment} class
 *This class is used as a support to retrieve news information from the internal database and to
 * execute the API's calls if the news have to be updated
 *
 *
 * @see NewsFragment
 * @see NewsApiClient
 * @author      Cristian D. Dramisino
 *
 */

public class NewsViewModel extends ViewModel {
    private boolean databaseUpdateNeeded = false;
    private List<NewsRecyclerViewItemModel> newsList = new ArrayList<>();
    private long currentTimeMillis=0;




    private ShimmerFrameLayout shimmerFrameLayout;

    private NewsApiClient newsApiClient = new NewsApiClient(UtilitiesStrings.NEWS_API_KEY);

    private NewsRecyclerViewHandler recyclerViewHandler;


    public NewsViewModel() {

        /**This is an empty constructor*/
    }

    public List<NewsRecyclerViewItemModel> refreshNewsList(InternalDatabaseHandler internalDB, Context context) {
        shimmerFrameLayout.startShimmer();
        apiCallsManagement(internalDB, context);
        if(!isDatabaseUpdateNeeded())
            setNewsGraphicalList(internalDB);
        return newsList;
    }

    private void setNewsGraphicalList(InternalDatabaseHandler internalDB) {
        String databaseNewsString = getNewsStringFromDatabase(internalDB);
        updateNewsArray(databaseNewsString);
        try{
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmer();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private boolean apiCallsManagement(InternalDatabaseHandler internalDB, Context context) {
        currentTimeMillis = System.currentTimeMillis();
        NewsDatabaseModel newsFromDatabase = internalDB.getNews();

        long datesMillisecondsDifference = currentTimeMillis - newsFromDatabase.getDate();

        if (apiCallIsNeeded(newsFromDatabase, datesMillisecondsDifference)) {
            databaseUpdateNeeded = true;
            shimmerFrameLayout.startShimmer();
            executeNewsAPICall(internalDB, context);
            return true;
        }
        return false;
    }




    /**
     *This method call an API to retrieve the daily news and stores them in the internal database
     */
    private void executeNewsAPICall(InternalDatabaseHandler internalDB, Context context) {
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q(context.getString(R.string.news_query))
                        .language(context.getString(R.string.news_language))
                        .sortBy(UtilitiesStrings.NEWS_API_ARTICLE_SORTING)
                        .domains(context.getString(R.string.news_domains))
                        .from(new Date(System.currentTimeMillis() - (UtilitiesNumbers.DAY_IN_MILLISECONDS)*3).toString())
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        List<Article> articles = response.getArticles();
                        String articlesJSON= new Gson().toJson(articles);
                        String newsToStoreJSON = buildNewsJSON(articlesJSON);
                        storeNewsOnDatabase(newsToStoreJSON, internalDB);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
        );
    }


    /**
     *This method checks if a new API call is needed
     * An API call is needed if the database doesn't contains yet news or the last news stored
     * were stored more than 8 hours before
     */
    private boolean apiCallIsNeeded(NewsDatabaseModel newsFromDatabase, long datesMillisecondsDifference) {
        return datesMillisecondsDifference > UtilitiesNumbers.NEWS_FREQUENCY_UPDATE || newsFromDatabase.getDate() == 0;
    }

    /**
     *This method stores the daily news on the internal database with the updating date in milliseconds
     */

    private void storeNewsOnDatabase(String newsToStoreJSON, InternalDatabaseHandler internalDB) {
        currentTimeMillis = System.currentTimeMillis();
        NewsDatabaseModel newsToStore = new NewsDatabaseModel();
        newsToStore.setDailyNews(newsToStoreJSON);
        newsToStore.setDate(currentTimeMillis);
        internalDB.deleteNews();
        internalDB.insertDailyNews(newsToStore);
        setNewsGraphicalList(internalDB);
        databaseUpdateNeeded =false;
    }



    private String buildNewsJSON(String articles) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(UtilitiesStrings.NEWS_API_JSON_CREATION_ARRAY_FILED);
        stringBuilder.append(articles);
        stringBuilder.append(UtilitiesStrings.NEWS_API_JSON_CREATION_ARRAY_CLOSING);
        return stringBuilder.toString();
    }



    /**
     *This method reads the information obtained by the API and stores them in the internal List newsList
     *The information are contained in a String that is converted in an JSON
     */
    private void updateNewsArray(String newsData) {
        JSONObject newsJSONObject = null;
        try {
            newsJSONObject = new JSONObject(newsData);
            JSONArray newsJSONArray = newsJSONObject.getJSONArray(UtilitiesStrings.NEWS_API_JSON_ARTICLES_ARRAY_NAME);
            if (newsJSONArray != null) {
                newsList.clear();
                for (int i = 0; i < newsJSONArray.length(); i++) {
                    JSONObject article = (JSONObject) newsJSONArray.get(i);
                    NewsRecyclerViewItemModel news = NewsUtilities.getNewsObjectFromJSON(article);
                    news.setType(UtilitiesNumbers.DAILY_NEWS_TYPE);
                    newsList.add(news);
                }
                if(databaseUpdateNeeded) {
                    recyclerViewHandler.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private String getNewsStringFromDatabase(InternalDatabaseHandler internalDB) {
        return internalDB.getNews().getDailyNews();
    }

    public void setHandler(NewsRecyclerViewHandler recyclerViewHandler) {
        this.recyclerViewHandler=recyclerViewHandler;
    }

    public boolean isDatabaseUpdateNeeded() {
        return databaseUpdateNeeded;
    }

    public List<NewsRecyclerViewItemModel> getNewsList() {
        return newsList;
    }

    public void setShimmerFrameLayout(ShimmerFrameLayout shimmerFrameLayout) {
        this.shimmerFrameLayout = shimmerFrameLayout;
    }

}