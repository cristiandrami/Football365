package com.cristiandrami.football365.ui.news;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import android.content.Context;

import com.cristiandrami.football365.model.internal_database.InternalDatabaseHandler;
import com.cristiandrami.football365.model.internal_database.NewsDatabaseModel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class NewsViewModelTest {
    private static NewsViewModel newsViewModel;
    private static InternalDatabaseHandler internalDatabaseHandler;
    private static Context context;
    @BeforeAll
    public static void setup(){
        newsViewModel= Mockito.spy(NewsViewModel.class);
        internalDatabaseHandler= Mockito.mock(InternalDatabaseHandler.class);
        context=Mockito.mock(Context.class);
    }

    @DisplayName("updateNewsArray should return a list of size of the corrisponding news number if there are news in JSON object")
    @ParameterizedTest
    @MethodSource("newsDatabaseModelParametrizedObjects")
     void should_updateNewsArray_returns_a_list_of_size_one_when_thereIs_onlyOneNew(int expectedSize, NewsDatabaseModel newsToTest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method updateNewsArrayMethod = NewsViewModel.class.getDeclaredMethod("updateNewsArray", String.class);
        updateNewsArrayMethod.setAccessible(true);
        updateNewsArrayMethod.invoke(newsViewModel, newsToTest.getDailyNews());
        int obtainedSize= newsViewModel.getNewsList().size();
        assertThat(obtainedSize).isEqualTo(expectedSize);

    }
/*
    @DisplayName("updateNewsArray should raise an JSONException if the news JSON string is bad formed")
    @Test
    public void should_updateNewsArray_raise_an_JSONException_if_JSONFormatIsNotCorrect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method updateNewsArrayMethod = NewsViewModel.class.getDeclaredMethod("updateNewsArray", String.class);
        updateNewsArrayMethod.setAccessible(true);
        assertThatThrownBy(()->{
           updateNewsArrayMethod.invoke(newsViewModel,"");
        }).isInstanceOf(Exception.class);
    }
    */

    @DisplayName("getNewsStringFromDatabase have to return daily news stored in the database ")
    @Test
     void should_getNewsStringFromDatabase_returns_dailyNewsStoredInDatabase () throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getNewsStringFromDatabaseMethod = NewsViewModel.class.getDeclaredMethod("getNewsStringFromDatabase", InternalDatabaseHandler.class);
        getNewsStringFromDatabaseMethod.setAccessible(true);

        NewsDatabaseModel newsWithZeroArticles= new NewsDatabaseModel();
        newsWithZeroArticles.setDate(0);
        newsWithZeroArticles.setDailyNews("{\"articles\":[]}");
        Mockito.when(internalDatabaseHandler.getNews()).thenReturn(newsWithZeroArticles);


        String obtainedNews= (String) getNewsStringFromDatabaseMethod.invoke(newsViewModel, internalDatabaseHandler);;
        assertThat(obtainedNews).isEqualTo(newsWithZeroArticles.getDailyNews());

    }

    @DisplayName("setHandler have to modify the internal NewsRecyclerViewHandler")
    @Test
     void should_setHandler_modify_internalNewsRecyclerViewHandler () throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setHandlerMethod = NewsViewModel.class.getDeclaredMethod("setHandler", NewsRecyclerViewHandler.class);
        setHandlerMethod.setAccessible(true);

        List<NewsRecyclerViewItemModel> itemModelList= new ArrayList<>();
        NewsRecyclerViewHandler expectedHandler= new NewsRecyclerViewHandler(itemModelList, context);

        setHandlerMethod.invoke(newsViewModel, expectedHandler);

        NewsRecyclerViewHandler obtainedHandler= (NewsRecyclerViewHandler) ReflectionTestUtils.getField(newsViewModel, "recyclerViewHandler");

        assertThat(obtainedHandler).isEqualTo(expectedHandler);

    }

    @DisplayName("storeNewsOnDatabase have to update the news on internal database ")
    @Test
     void should_storeNewsOnDatabase_updates_internalDatabaseNews () throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method storeNewsOnDatabaseMethod = NewsViewModel.class.getDeclaredMethod("storeNewsOnDatabase",
                String.class, InternalDatabaseHandler.class);
        storeNewsOnDatabaseMethod.setAccessible(true);

        String dailyNews="{\"articles\":[]}";
        NewsDatabaseModel newsWithZeroArticles= new NewsDatabaseModel();
        newsWithZeroArticles.setDate(0);
        newsWithZeroArticles.setDailyNews(dailyNews);

        Mockito.when(internalDatabaseHandler.getNews()).thenReturn(newsWithZeroArticles);

        storeNewsOnDatabaseMethod.invoke(newsViewModel,dailyNews, internalDatabaseHandler );

        Mockito.verify(internalDatabaseHandler).deleteNews();
        Mockito.verify(internalDatabaseHandler).insertDailyNews(ArgumentMatchers.any(NewsDatabaseModel.class));

    }

    @DisplayName("buildNewsJSON have to create the right JSON string ")
    @Test
     void should_buildNewsJSON_creates_newsJSONString () throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method buildNewsJSONMethod = NewsViewModel.class.getDeclaredMethod("buildNewsJSON",
                String.class);
        buildNewsJSONMethod.setAccessible(true);

        String dailyNews="[]";

        String expectedJSONNews="{\"articles\" : []}";


        String obtainedJSONString=(String) buildNewsJSONMethod.invoke(newsViewModel, dailyNews);

       assertThat(obtainedJSONString).isEqualTo(expectedJSONNews);

    }



    /*@DisplayName("updateNewsArray should insert in database the news obtained by api if the database is empty")
    @Test
    public void should_updateNewsArray_insertNewsObtainedByAPIinDb_if_databaseIsEmpty(){
        Mockito.when(context.getString(Mockito.anyInt())).thenReturn("test");

        NewsDatabaseModel newsToTest= new NewsDatabaseModel();
        newsToTest.setNews("test");
        newsToTest.setDate(0);
        Mockito.when(internalDatabaseHandler.getNews()).thenReturn(newsToTest);


        List<NewsRecyclerViewItemModel> newsList= newsViewModel.getNewsList(internalDatabaseHandler, context);
        //Mockito.verify(internalDatabaseHandler, Mockito.times(2)).getNews();
        Mockito.verify(internalDatabaseHandler).insertDailyNews(Mockito.any(NewsDatabaseModel.class));


    }*/


    private static Stream<Arguments> newsDatabaseModelParametrizedObjects() {
        NewsDatabaseModel newsWithOneArticle= new NewsDatabaseModel();
        newsWithOneArticle.setDate(0);
        newsWithOneArticle.setDailyNews("{\"articles\" : [{\"author\":\"G.B. Olivero\",\"description\":\"Milan, il nostro inviato nella sede di Investcorp: tutti i segreti della trattativaL\\u0027obiettivo del fondo è ambizioso e duplice: vincere sul piano sportivo e garantire una crescita finanziaria che riduca progressivamente il gap da top team europei\",\"publishedAt\":\"2022-04-25T21:34:52Z\",\"source\":{\"name\":\"Gazzetta.it\"},\"title\":\"Milan, il nostro inviato nella sede di Investcorp: tutti i segreti della trattativa\",\"url\":\"https://www.gazzetta.it/Calcio/Serie-A/Milan/25-04-2022/milan-investcorp-segreti-trattativa-440212100907.shtml\",\"urlToImage\":\"https://images2.gazzettaobjects.it/methode_image/2022/04/25/Calcio/Foto_Calcio_-_Trattate/283e5592d10447b22208696beb4d5cee_1200x675.jpg?v\\u003d202204252333\"}]}");
        NewsDatabaseModel newsWithTwoArticles= new NewsDatabaseModel();
        newsWithTwoArticles.setDate(0);
        newsWithTwoArticles.setDailyNews("{\"articles\" : [{\"author\":\"G.B. Olivero\",\"description\":\"Milan, il nostro inviato nella sede di Investcorp: tutti i segreti della trattativaL\\u0027obiettivo del fondo è ambizioso e duplice: vincere sul piano sportivo e garantire una crescita finanziaria che riduca progressivamente il gap da top team europei\",\"publishedAt\":\"2022-04-25T21:34:52Z\",\"source\":{\"name\":\"Gazzetta.it\"},\"title\":\"Milan, il nostro inviato nella sede di Investcorp: tutti i segreti della trattativa\",\"url\":\"https://www.gazzetta.it/Calcio/Serie-A/Milan/25-04-2022/milan-investcorp-segreti-trattativa-440212100907.shtml\",\"urlToImage\":\"https://images2.gazzettaobjects.it/methode_image/2022/04/25/Calcio/Foto_Calcio_-_Trattate/283e5592d10447b22208696beb4d5cee_1200x675.jpg?v\\u003d202204252333\"},{\"author\":\"Tommaso Marcoli\",\"description\":\"Successo clamoroso per la nuova sportiva compatta del marchio giapponese. In appena 90 minuti tutti gli esemplari destinati al Regno Unito sono stati ordinati.\",\"publishedAt\":\"2022-04-25T06:24:19Z\",\"source\":{\"name\":\"Gazzetta.it\"},\"title\":\"Toyota GR86: la sportiva compatta sold out nel Regno Unito in 90 minuti\",\"url\":\"https://www.gazzetta.it/motori/la-mia-auto/25-04-2022/toyota-gr86-regno-unito-sold-out-90-minuti.shtml\",\"urlToImage\":\"https://dimages2.gazzettaobjects.it/files/og_thumbnail/uploads/2022/02/10/62059303c8620.jpeg\"}]}");

        NewsDatabaseModel newsWithZeroArticles= new NewsDatabaseModel();
        newsWithZeroArticles.setDate(0);
        newsWithZeroArticles.setDailyNews("{\"articles\" : []}");

        return Stream.of(
                Arguments.of(1, newsWithOneArticle),
                Arguments.of(2, newsWithTwoArticles),
                Arguments.of(0, newsWithZeroArticles)
        );
    }


    @DisplayName("APICallIsNeeded have to return true if date is 0")
    @Test
     void should_APICallIsNeeded_returns_true_if_dateIs0 () throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method APICallIsNeededMethod = NewsViewModel.class.getDeclaredMethod("apiCallIsNeeded", NewsDatabaseModel.class, long.class);
        APICallIsNeededMethod.setAccessible(true);

        NewsDatabaseModel newsWithZeroArticles= new NewsDatabaseModel();
        newsWithZeroArticles.setDate(0);
        newsWithZeroArticles.setDailyNews("{\"articles\" : []}");

        boolean obtainedValue=(boolean)APICallIsNeededMethod.invoke(newsViewModel, newsWithZeroArticles, 900000000);
        boolean expectedValue=true;
        assertThat(obtainedValue).isEqualTo(expectedValue);

    }

    @DisplayName("APICallIsNeeded have to return true if datesMillisecondsDifference is greater than 8 hours")
    @Test
     void should_APICallIsNeeded_returns_true_if_datesMillisecondsDifferenceIsGreaterThan8Hours () throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method APICallIsNeededMethod = NewsViewModel.class.getDeclaredMethod("apiCallIsNeeded", NewsDatabaseModel.class, long.class);
        APICallIsNeededMethod.setAccessible(true);

        NewsDatabaseModel newsWithZeroArticles= new NewsDatabaseModel();
        newsWithZeroArticles.setDate(1500);
        newsWithZeroArticles.setDailyNews("{\"articles\" : []}");

        boolean obtainedValue=(boolean)APICallIsNeededMethod.invoke(newsViewModel, newsWithZeroArticles, 900000000);
        boolean expectedValue=true;
        assertThat(obtainedValue).isEqualTo(expectedValue);

    }

    @DisplayName("APICallIsNeeded have to return false if datesMillisecondsDifference is smaller than 8 hours and date is not 0")
    @Test
     void should_APICallIsNeeded_returns_true_if_datesMillisecondsDifferenceIsSmallerThan8Hours_and_dateIsNot0 () throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method APICallIsNeededMethod = NewsViewModel.class.getDeclaredMethod("apiCallIsNeeded", NewsDatabaseModel.class, long.class);
        APICallIsNeededMethod.setAccessible(true);

        NewsDatabaseModel newsWithZeroArticles= new NewsDatabaseModel();
        newsWithZeroArticles.setDate(1500);
        newsWithZeroArticles.setDailyNews("{\"articles\" : []}");

        boolean obtainedValue=(boolean)APICallIsNeededMethod.invoke(newsViewModel, newsWithZeroArticles, 100);
        boolean expectedValue=false;
        assertThat(obtainedValue).isEqualTo(expectedValue);

    }




}