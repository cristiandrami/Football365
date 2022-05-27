package com.cristiandrami.football365.ui.matches;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.detailed_match.comment.CommentItemsListTest;
import com.cristiandrami.football365.model.detailed_match.line_up.PlayersListTest;
import com.cristiandrami.football365.model.utilities.UtilitiesNumbers;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.cristiandrami.football365.model.utilities.matches_utilities.CompetitionsUtilities;
import com.cristiandrami.football365.model.utilities.matches_utilities.Match;
import com.cristiandrami.football365.model.utilities.matches_utilities.MatchesComparator;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MatchesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final CompetitionsUtilities competitionsUtilities = CompetitionsUtilities.getInstance();
    private final List<Match> matchesList = new ArrayList<>();
    private final HashMap<String, List<Match>> nextMatches = new HashMap<>();

    public MatchesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public List<Match> getMatchesList() {
        return matchesList;
    }

    public LiveData<String> getText() {
        return mText;
    }


    public void setPositionDatesMap(int minPosition, int maxPosition, HashMap<Integer, String> datesPositionMap) {
        Date currentDate = new Date(System.currentTimeMillis()+(UtilitiesNumbers.DAY_IN_MILLISECONDS*minPosition));
        for (int i = minPosition; i < maxPosition; i++) {
                datesPositionMap.put(i, currentDate.toString());
                Log.e("position ", i+" "+currentDate.toString());
                currentDate = new Date(currentDate.getTime() + UtilitiesNumbers.DAY_IN_MILLISECONDS);
        }
    }

    public void updateMatchesListV2(MatchesFragment matchesFragment, String date, Context context) {
        Log.e("request date", date);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.football-data.org/v2/matches?dateFrom=" + date + "&&dateTo=" + date)
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

                if (response.isSuccessful()) {
                    JSONObject matchesJSONObject = null;
                    try {
                        matchesList.clear();
                        matchesJSONObject = new JSONObject(response.body().string());
                        JSONArray matchesJSONArray = matchesJSONObject.getJSONArray(UtilitiesStrings.MATCHES_API_JSON_MATCHES_ARRAY_NAME);

                        for (int i = 0; i < matchesJSONArray.length(); i++) {
                            Match match = setMatchesFromJSONArray(matchesJSONArray, i, context);


                            try {
                                addTimeZoneOnStartTime(match, context);
                            } catch (Exception e) {

                                e.printStackTrace();
                            }


                        }

                        Log.e("api ", "refresh");
                        Collections.sort(matchesList, new MatchesComparator());
                        matchesFragment.refreshMatchesViewV2(matchesList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("api ok", response.body().string());
                }
            }
        });
    }

    private void addTimeZoneOnStartTime(Match match, Context context) {
        if (!context.getString(R.string.match_time_zone).equals(UtilitiesStrings.MATCH_TIME_ZONE_0)) {
            if (!addTimeZoneToStartTime(context, match)) {
                addNextMatch(match);
            } else {
                matchesList.add(match);
            }
        }
    }

    @NonNull
    private Match setMatchesFromJSONArray(JSONArray matchesJSONArray, int i, Context context) throws JSONException {
        Match match = new Match();
        JSONObject singleMatchJSONObject = (JSONObject) matchesJSONArray.get(i);
        String matchStatus = singleMatchJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_MATCH_STATUS);
        String matchDate = singleMatchJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_MATCH_DATE);
        String startTime = matchDate.substring(matchDate.indexOf("T") + 1);
        startTime = startTime.substring(0, startTime.indexOf("Z") - 3);


        matchDate = matchDate.substring(0, matchDate.indexOf("T"));

        Log.e("match", singleMatchJSONObject.toString());

        JSONObject competitionJSONObject = (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_COMPETITION_NAME);
        String competitionId = competitionJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_COMPETITION_ID_FIELD);

        JSONObject homeTeam = (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM);
        String homeTeamName = homeTeam.getString(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM_NAME);

        JSONObject awayTeam = (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM);
        String awayTeamName = awayTeam.getString(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM_NAME);

        JSONObject score = (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_SCORE);

        JSONObject halfTimeJSON = (JSONObject) score.get(UtilitiesStrings.MATCHES_API_JSON_HALF_TIME);
        String halfTimeHomeTeamScore = halfTimeJSON.getString(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM);
        String halfTimeAwayTeamScore = halfTimeJSON.getString(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM);

        JSONObject fullTimeJSON = (JSONObject) score.get(UtilitiesStrings.MATCHES_API_JSON_FULL_TIME);
        String fullTimeHomeTeamScore = fullTimeJSON.getString(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM);
        String fullTimeAwayTeamScore = fullTimeJSON.getString(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM);


        String matchId = singleMatchJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_MATCH_ID);


        match.setAwayTeam(awayTeamName);
        match.setHomeTeam(homeTeamName);
        match.setCompetitionId(competitionId);
        match.setDate(matchDate);
        match.setStatus(matchStatus);
        match.setFullTimeAwayTeamScore(fullTimeAwayTeamScore);
        match.setFullTimeHomeTeamScore(fullTimeHomeTeamScore);
        match.setHalfTimeAwayTeamScore(halfTimeAwayTeamScore);
        match.setHalfTimeHomeTeamScore(halfTimeHomeTeamScore);
        match.setMatchId(matchId);
        match.setStartTime(startTime);
        match.setHomePlayers(PlayersListTest.getInstance().getHomePlayers());
        match.setAwayPlayers(PlayersListTest.getInstance().getAwayPlayers());
        match.setCommentItemList(CommentItemsListTest.getInstance().getCommentList());


        return match;
    }

    private void addNextMatch(Match match) {
        if (nextMatches.get(match.getDate()) == null) {
            List<Match> matchesDate = new ArrayList<>();
            nextMatches.put(match.getDate(), matchesDate);
        }

        if(!nextMatches.get(match.getDate()).contains(match))
            nextMatches.get(match.getDate()).add(match);
    }


    @NonNull
    public boolean addTimeZoneToStartTime(Context context, Match match) {
        boolean currentDate = true;
        String startTime = match.getStartTime();
        String hour = startTime.substring(0, startTime.indexOf(":"));
        String minute = startTime.substring(startTime.indexOf(":"));
        int intHour = Integer.parseInt(hour);
        intHour += Integer.parseInt(context.getString(R.string.match_time_zone));
        if (intHour >= 24) {
            intHour -= 24;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            try {
                java.util.Date date = formatter.parse(match.getDate());
                long milliseconds = date.getTime();
                milliseconds += UtilitiesNumbers.DAY_IN_MILLISECONDS;
                Date sqlDate = new Date(milliseconds);
                match.setDate(sqlDate.toString());


            } catch (ParseException e) {
                e.printStackTrace();
            }

            currentDate = false;
        }
        hour = String.valueOf(intHour);
        if (hour.equals(UtilitiesStrings.MATCHES_MIDNIGHT_HOUR_0)) {
            hour = UtilitiesStrings.MATCHES_MIDNIGHT_HOUR;
        }
        startTime = hour + minute;
        match.setStartTime(startTime);

        return currentDate;
    }

    public void updateNextAndPreviousMatches(int minPosition, int maxPosition, HashMap<Integer, String> datesPositionMap, Context context, MatchesFragment fragment) {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.football-data.org/v2/matches?dateFrom=" + datesPositionMap.get(minPosition) + "&&dateTo=" + datesPositionMap.get(maxPosition - 1))
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

                if (response.isSuccessful()) {
                    nextMatches.clear();
                    JSONObject matchesJSONObject = null;
                    try {
                        matchesJSONObject = new JSONObject(response.body().string());
                        JSONArray matchesJSONArray = matchesJSONObject.getJSONArray(UtilitiesStrings.MATCHES_API_JSON_MATCHES_ARRAY_NAME);

                        for (int i = 0; i < matchesJSONArray.length(); i++) {
                            Match match = setMatchesFromJSONArray(matchesJSONArray, i, context);


                            if (!context.getString(R.string.match_time_zone).equals(UtilitiesStrings.MATCH_TIME_ZONE_0)) {
                                addTimeZoneToStartTime(context, match);
                                addNextMatch(match);
                            }
                        }
                        fragment.notifyNextAndPreviousMatchesUpdateFinished();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("api ok", response.body().string());
                }
            }
        });


    }

    public List<Match> getNextMatchesList(String date) {
        return nextMatches.get(date);
    }
}