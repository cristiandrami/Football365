package com.cristiandrami.football365.ui.news;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.internalDatabase.InternalDatabaseHandler;
import com.cristiandrami.football365.model.internalDatabase.NewsDatabaseModel;
import com.cristiandrami.football365.model.utilities.UtilitiesNumbers;
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
    private long currentTimeMillis=0;
    private NewsApiClient newsApiClient = new NewsApiClient(UtilitiesStrings.NEWS_API_KEY);

    private NewsRecyclerViewHandler recyclerViewHandler;


    public NewsViewModel() {

    }

    public List<NewsRecyclerViewItemModel> refreshNewsList(InternalDatabaseHandler internalDB, Context context) {
        executeAPINewsCallIfNeeded(internalDB, context);
        if(!isFirstCall())
            setNewsGraphicalList(internalDB);
        return newsList;
    }

    private void setNewsGraphicalList(InternalDatabaseHandler internalDB) {
        String databaseNewsString = getNewsStringFromDatabase(internalDB);
        updateNewsArray(databaseNewsString);
    }

    private boolean executeAPINewsCallIfNeeded(InternalDatabaseHandler internalDB, Context context) {
        currentTimeMillis = System.currentTimeMillis();
        NewsDatabaseModel newsFromDatabase = internalDB.getNews();

        long datesMillisecondsDifference = currentTimeMillis - newsFromDatabase.getDate();

        if (APICallIsNeeded(newsFromDatabase, datesMillisecondsDifference)) {

            firstCall=true;

            newsApiClient.getEverything(
                    new EverythingRequest.Builder()
                            .q(context.getString(R.string.news_query))
                            .language(context.getString(R.string.news_language))
                            .sortBy(UtilitiesStrings.API_ARTICLE_SORTING)
                            .domains(context.getString(R.string.news_domains))
                            .from(new Date(System.currentTimeMillis() - UtilitiesNumbers.dayInMilliseconds).toString())
                            .build(),
                    new NewsApiClient.ArticlesResponseCallback() {
                        @Override
                        public void onSuccess(ArticleResponse response) {
                            storeNewsOnDatabase(response, internalDB);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    }
            );

            return true;

        }
        return false;
    }

    private boolean APICallIsNeeded(NewsDatabaseModel newsFromDatabase, long datesMillisecondsDifference) {
        return datesMillisecondsDifference > UtilitiesNumbers.NEWS_FREQUENCY_UPDATE || newsFromDatabase.getDate() == 0;
    }

    private void storeNewsOnDatabase(ArticleResponse response, InternalDatabaseHandler internalDB) {
        List<Article> articles = response.getArticles();

        String newsToStoreJSON = buildNewsJSON(articles);

        NewsDatabaseModel newsToStore = new NewsDatabaseModel();
        newsToStore.setNews(newsToStoreJSON);
        newsToStore.setDate(currentTimeMillis);

        internalDB.deleteNews();
        internalDB.insertDailyNews(newsToStore);

        setNewsGraphicalList(internalDB);
        firstCall=false;

    }

    private String buildNewsJSON(List<Article> articles) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(UtilitiesStrings.API_JSON_CREATION_ARRAY_FILED);
        stringBuilder.append(new Gson().toJson(articles));
        stringBuilder.append(UtilitiesStrings.API_JSON_CREATION_ARRAY_CLOSING);
        return stringBuilder.toString();
    }

    private void updateNewsArray(String newsData) {
        JSONObject newsJSONObject = null;
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
                    String author =(String) source.get(UtilitiesStrings.API_JSON_SOURCE_AUTHOR_FIELD);
                    String url = (String) article.get(UtilitiesStrings.API_JSON_ARTICLE_URL_FIELD);

                    NewsRecyclerViewItemModel news = new NewsRecyclerViewItemModel(image, title, author);
                    news.setArticleLink(url);
                    newsList.add(news);
                }
                if(firstCall) {
                    recyclerViewHandler.notifyDataSetChanged();
                }
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

    public boolean isFirstCall() {
        return firstCall;
    }

    public List<NewsRecyclerViewItemModel> getNewsList() {
        return newsList;
    }
}