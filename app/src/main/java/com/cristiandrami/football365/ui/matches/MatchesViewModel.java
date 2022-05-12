package com.cristiandrami.football365.ui.matches;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.cristiandrami.football365.model.utilities.matchesUtilities.CompetitionsUtilities;
import com.cristiandrami.football365.model.utilities.matchesUtilities.Match;
import com.cristiandrami.football365.model.utilities.matchesUtilities.MatchesComparator;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MatchesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private CompetitionsUtilities competitionsUtilities= CompetitionsUtilities.getInstance();

    public List<Match> getMatchesList() {
        return matchesList;
    }

    private List<Match> matchesList = new ArrayList<>();

    public MatchesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void updateMatchesList(MatchesFragment matchesFragment) {

        OkHttpClient client = new OkHttpClient();

        Date currentDate= new Date(System.currentTimeMillis());
        Date finalDate= new Date(System.currentTimeMillis()+(7*24*60*60*1000));

        Request request = new Request.Builder()
                .url("https://api.football-data.org/v2/matches?dateFrom="+currentDate.toString()+"&&dateTo="+finalDate.toString())
                .get()
                .addHeader("X-Auth-Token", "c0c99cafe93949beb14871c37bacfa5f")
                .build();
        client.setProtocols(Arrays.asList(Protocol.HTTP_1_1));

        client.newCall(request).enqueue(new Callback() {


            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("api", e.toString());

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()) {
                    JSONObject matchesJSONObject = null;
                    try {
                        matchesList.clear();
                        matchesJSONObject = new JSONObject(response.body().string());
                        JSONArray matchesJSONArray = matchesJSONObject.getJSONArray(UtilitiesStrings.MATCHES_API_JSON_MATCHES_ARRAY_NAME);
                        Log.e("matches length", String.valueOf(matchesJSONArray.length()));
                        for(int i=0; i<matchesJSONArray.length();i++){
                            Match match= new Match();
                            JSONObject singleMatchJSONObject=(JSONObject) matchesJSONArray.get(i);
                            String matchStatus=singleMatchJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_MATCH_STATUS);
                            String matchDate=singleMatchJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_MATCH_DATE);

                            JSONObject competitionJSONObject= (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_COMPETITION_NAME);
                            String competitionId= competitionJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_COMPETITION_ID_FIELD);

                            JSONObject homeTeam= (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM);
                            String homeTeamName= homeTeam.getString(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM_NAME);

                            JSONObject awayTeam= (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM);
                            String awayTeamName= awayTeam.getString(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM_NAME);

                            //String minute= singleMatchJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_MINUTE);


                            JSONObject score= (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_SCORE);

                            JSONObject halfTimeJSON= (JSONObject) score.get(UtilitiesStrings.MATCHES_API_JSON_HALF_TIME);
                            String halfTimeHomeTeamScore= halfTimeJSON.getString(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM);
                            String halfTimeAwayTeamScore= halfTimeJSON.getString(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM);

                            JSONObject fullTimeJSON= (JSONObject) score.get(UtilitiesStrings.MATCHES_API_JSON_FULL_TIME);
                            String fullTimeHomeTeamScore= fullTimeJSON.getString(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM);
                            String fullTimeAwayTeamScore= fullTimeJSON.getString(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM);

                            match.setAwayTeam(awayTeamName);
                            match.setHomeTeam(homeTeamName);
                            match.setCompetitionId(competitionId);
                            match.setDate(matchDate);
                            match.setStatus(matchStatus);
                            match.setFullTimeAwayTeamScore(fullTimeAwayTeamScore);
                            match.setFullTimeHomeTeamScore(fullTimeHomeTeamScore);
                            match.setHalfTimeAwayTeamScore(halfTimeAwayTeamScore);
                            match.setHalfTimeHomeTeamScore(halfTimeHomeTeamScore);

                            matchesList.add(match);

                        }

                        Collections.sort(matchesList, new MatchesComparator());
                        matchesFragment.refreshMatchesView(matchesList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("api ok", response.body().string());
                }
            }
        });
    }

}