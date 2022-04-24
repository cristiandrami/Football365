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


    public NewsViewModel() {

    }


    public List<NewsRecyclerViewItemModel> getNewsList(InternalDatabaseHandler internalDB, Context context) {

        List<NewsRecyclerViewItemModel> newsList = new ArrayList<NewsRecyclerViewItemModel>();
        executeAPINewsCallIfNeeded(internalDB, context, newsList);
        setNewsGraphicalList(internalDB, context, newsList);
        return newsList;
    }

    private void setNewsGraphicalList(InternalDatabaseHandler internalDB, Context context, List<NewsRecyclerViewItemModel> newsList) {
        String databaseNewsString = getNewsStringFromDatabase(internalDB);
        updateNewsArray(newsList, databaseNewsString, context);
    }

    private void executeAPINewsCallIfNeeded(InternalDatabaseHandler internalDB, Context context, List<NewsRecyclerViewItemModel> newsList) {
        long currentTimeMillis = System.currentTimeMillis();
        NewsDatabaseModel newsFromDatabase = internalDB.getNews();

        long millisDifferenceToUpdate = currentTimeMillis - newsFromDatabase.getDate();
        NewsApiClient newsApiClient = new NewsApiClient("4ba69400af194410b9767bd4f83a013f");


        if (millisDifferenceToUpdate > UtilitiesStrings.NEWS_FREQUENCY_UPDATE || newsFromDatabase.getDate() == 0) {


                newsApiClient.getEverything(
                        new EverythingRequest.Builder()
                                .q(context.getString(R.string.news_query))
                                .language(context.getString(R.string.news_language))
                                .sortBy(UtilitiesStrings.API_ARTICLE_SORTING)
                                .domains(context.getString(R.string.news_domains))
                                .from(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000).toString())
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

                                Log.e("api", "prima chiamata");
                                internalDB.deleteNews();
                                internalDB.insertDailyNews(newsToStore);
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                System.out.println(throwable.getMessage());
                            }
                        }
                );

        }
    }

    private void updateNewsArray(List<NewsRecyclerViewItemModel> newsList, String newsData, Context context) {
        JSONObject newsJSONObject = null;
        try {
            newsJSONObject = new JSONObject(newsData);
            JSONArray newsJSONArray = newsJSONObject.getJSONArray(UtilitiesStrings.API_JSON_ARTICLES_ARRAY_NAME);
            if (newsJSONArray != null) {
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String getNewsStringFromDatabase(InternalDatabaseHandler internalDB) {
        return internalDB.getNews().getNews();
    }
}