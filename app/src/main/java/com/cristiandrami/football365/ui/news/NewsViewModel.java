package com.cristiandrami.football365.ui.news;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.internalDatabase.InternalDatabaseHandler;
import com.cristiandrami.football365.model.internalDatabase.NewsDatabaseModel;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
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

public class NewsViewModel extends ViewModel {
    private boolean firstCall= false;
    private List<NewsRecyclerViewItemModel> newsList = new ArrayList<NewsRecyclerViewItemModel>();

    private NewsRecyclerViewHandler recyclerViewHandler;


    public NewsViewModel() {

    }

    public List<NewsRecyclerViewItemModel> getNewsList(InternalDatabaseHandler internalDB, Context context) {
        executeAPINewsCallIfNeeded(internalDB, context, newsList);
        if(!firstCall)
            setNewsGraphicalList(internalDB, context, newsList);
        return newsList;
    }

    private void setNewsGraphicalList(InternalDatabaseHandler internalDB, Context context, List<NewsRecyclerViewItemModel> newsList_) {
        String databaseNewsString = getNewsStringFromDatabase(internalDB);
        updateNewsArray(newsList, databaseNewsString, context);
    }

    private boolean executeAPINewsCallIfNeeded(InternalDatabaseHandler internalDB, Context context, List<NewsRecyclerViewItemModel> newsList_) {
        long currentTimeMillis = System.currentTimeMillis();
        NewsDatabaseModel newsFromDatabase = internalDB.getNews();

        long millisDifferenceToUpdate = currentTimeMillis - newsFromDatabase.getDate();
        NewsApiClient newsApiClient = new NewsApiClient(UtilitiesStrings.NEWS_API_KEY);



        if (millisDifferenceToUpdate > UtilitiesStrings.NEWS_FREQUENCY_UPDATE || newsFromDatabase.getDate() == 0) {

            firstCall=true;
            long dayInMillis=24 * 60 * 60 * 1000;

            newsApiClient.getEverything(
                        new EverythingRequest.Builder()
                                .q(context.getString(R.string.news_query))
                                .language(context.getString(R.string.news_language))
                                .sortBy(UtilitiesStrings.API_ARTICLE_SORTING)
                                .domains(context.getString(R.string.news_domains))
                                .from(new Date(System.currentTimeMillis() - dayInMillis).toString())
                                .build(),
                        new NewsApiClient.ArticlesResponseCallback() {
                            @Override
                            public void onSuccess(ArticleResponse response) {
                                List<Article> articles = response.getArticles();

                                String newsToStoreJSON = "";
                                StringBuilder stringBuilder = new StringBuilder(newsToStoreJSON);
                                stringBuilder.append(UtilitiesStrings.API_JSON_CREATION_ARRAY_FILED);
                                stringBuilder.append(new Gson().toJson(articles));
                                stringBuilder.append(UtilitiesStrings.API_JSON_CREATION_ARRAY_CLOSING);

                                NewsDatabaseModel newsToStore = new NewsDatabaseModel();
                                newsToStore.setNews(stringBuilder.toString());
                                newsToStore.setDate(currentTimeMillis);

                                internalDB.deleteNews();
                                internalDB.insertDailyNews(newsToStore);

                                setNewsGraphicalList(internalDB, context, newsList);
                                Log.e("api", "end api call");
                                firstCall=false;

                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                               Log.e("api failure", throwable.getMessage());
                            }
                        }
                );

                return true;

        }
        return false;
    }

    private void updateNewsArray(List<NewsRecyclerViewItemModel> newsList_, String newsData, Context context) {
        JSONObject newsJSONObject = null;
        //Log.e("data", newsData);
        try {


            newsJSONObject = new JSONObject(newsData);
            JSONArray newsJSONArray = newsJSONObject.getJSONArray(UtilitiesStrings.API_JSON_ARTICLES_ARRAY_NAME);
            if (newsJSONArray != null) {
                newsList.clear();
                for (int i = 0; i < newsJSONArray.length(); i++) {
                    JSONObject article = (JSONObject) newsJSONArray.get(i);
                    JSONObject source = (JSONObject) article.get(UtilitiesStrings.API_JSON_SOURCE_OBJECT_NAME);

                    String title = (String) article.get(UtilitiesStrings.API_JSON_ARTICLE_TITLE_FIELD);
                    String image = (String) article.get(UtilitiesStrings.API_JSON_ARTICLE_IMAGE_FIELD);
                    String author = (String) source.get(UtilitiesStrings.API_JSON_SOURCE_AUTHOR_FIELD);
                    String url = (String) article.get(UtilitiesStrings.API_JSON_ARTICLE_URL_FIELD);

                    NewsRecyclerViewItemModel news = new NewsRecyclerViewItemModel(image, title, author);
                    news.setArticleLink(url);

                    newsList.add(news);
                }

                if(firstCall)
                    recyclerViewHandler.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String getNewsStringFromDatabase(InternalDatabaseHandler internalDB) {
        return internalDB.getNews().getNews();
    }

    public void setHandler(NewsRecyclerViewHandler recyclerViewHandler) {
        this.recyclerViewHandler=recyclerViewHandler;
    }
}