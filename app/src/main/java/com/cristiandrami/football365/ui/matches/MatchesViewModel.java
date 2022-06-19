package com.cristiandrami.football365.ui.matches;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.detailed_match.comment.CommentItemsListTest;
import com.cristiandrami.football365.model.detailed_match.line_up.PlayersListTest;
import com.cristiandrami.football365.model.detailed_match.referees.RefereesItemsListTest;
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

    private final CompetitionsUtilities competitionsUtilities = CompetitionsUtilities.getInstance();
    private final List<Match> matchesOfTheDayList = new ArrayList<>();


    private final HashMap<String, List<Match>> matchesOfOtherDaysMap = new HashMap<>();

    public MatchesViewModel() {
        /***
         * This is an empty constructor
         */
    }


    /***
     * This method is used to fill the HashMap with the dates for the different match days
     * Position 0 contains the current date (today)
     * Position -1 contains the date of yesterday
     * Position 1 contains the date of tomorrow
     *
     * With the for instruction we fill the dates from the minimum position to the maximum position
     *
     * minPosition has to be <= 0 to work properly
     *
     * @param minPosition
     * @param maxPosition
     * @param datesPositionMap
     */

    public void setPositionDatesMap(int minPosition, int maxPosition, HashMap<Integer, String> datesPositionMap) {

        if (minPosition <= 0) {

            /**
             *  This allow us to start with the date that corresponds to the minimum position, if min position is -1 the date
             *  is today date-1 (yesterday)
             *  if max position is
             */
            Date currentDate = new Date(System.currentTimeMillis() + (UtilitiesNumbers.DAY_IN_MILLISECONDS * minPosition));

            for (int i = minPosition; i <= maxPosition; i++) {
                datesPositionMap.put(i, currentDate.toString());
                currentDate = new Date(currentDate.getTime() + UtilitiesNumbers.DAY_IN_MILLISECONDS);
            }
        }

    }

    public void updateMatchesListV2(MatchesFragment matchesFragment, String date, Context context) {
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
                        matchesOfTheDayList.clear();
                        matchesJSONObject = new JSONObject(response.body().string());
                        JSONArray matchesJSONArray = matchesJSONObject.getJSONArray(UtilitiesStrings.MATCHES_API_JSON_MATCHES_ARRAY_NAME);

                        for (int i = 0; i < matchesJSONArray.length(); i++) {
                            Match match = setMatchesFromJSONArray(matchesJSONArray, i);
                            if(context!=null)
                                addTimeZoneOnStartTime(match, context.getString(R.string.match_time_zone));
                        }

                        /** This sorts the matches from their geographic areas (alphabetic sorting) */
                        Collections.sort(matchesOfTheDayList, new MatchesComparator());

                        /** This notifies the fragment the correct matches update and passes to it the updated list*/
                        matchesFragment.refreshMatchesViewV2(matchesOfTheDayList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    Log.e("[updateMatchesListV2]", UtilitiesStrings.DEBUG_ERROR + response.body().string());
                }
            }
        });
    }

    /***
     * This method is used to add a match in his correct data structure
     *
     * If the return of the the method {@link #changeStartTimeAndDateIfNeeded(Match, String)} is true
     * we have to add the match to the map of the matches that aren't of the day
     * in other case we add it on the daily matches
     * @param match
     * @param currentTimeZone
     */
    private void addTimeZoneOnStartTime(Match match, String currentTimeZone) {
        if (!currentTimeZone.equals(UtilitiesStrings.MATCH_TIME_ZONE_0)) {

            if (changeStartTimeAndDateIfNeeded(match, currentTimeZone)) {
                addMatchOnTheOtherDaysMap(match);
            } else {
                matchesOfTheDayList.add(match);
            }
        }
    }

    @NonNull
    private Match setMatchesFromJSONArray(JSONArray matchesJSONArray, int i) throws JSONException {
        Match match = null;
        JSONObject singleMatchJSONObject = null;

        match = new Match();
        singleMatchJSONObject = (JSONObject) matchesJSONArray.get(i);
        String matchStatus = singleMatchJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_MATCH_STATUS);
        String matchDate = singleMatchJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_MATCH_DATE);
        String startTime = matchDate.substring(matchDate.indexOf("T") + 1);
        startTime = startTime.substring(0, startTime.indexOf("Z") - 3);


        matchDate = matchDate.substring(0, matchDate.indexOf("T"));

        JSONObject competitionJSONObject = (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_COMPETITION_NAME);
        System.out.println(competitionJSONObject);
        String competitionId = String.valueOf(competitionJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_COMPETITION_ID_FIELD));

        JSONObject homeTeam = (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM);
        String homeTeamName = String.valueOf(homeTeam.get(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM_NAME));

        JSONObject awayTeam = (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM);
        String awayTeamName = String.valueOf(awayTeam.get(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM_NAME));

        JSONObject score = (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_SCORE);

        JSONObject halfTimeJSON = (JSONObject) score.get(UtilitiesStrings.MATCHES_API_JSON_HALF_TIME);
        String halfTimeHomeTeamScore = String.valueOf(halfTimeJSON.get(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM));
        String halfTimeAwayTeamScore = String.valueOf(halfTimeJSON.get(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM));

        JSONObject fullTimeJSON = (JSONObject) score.get(UtilitiesStrings.MATCHES_API_JSON_FULL_TIME);
        String fullTimeHomeTeamScore = String.valueOf(fullTimeJSON.get(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM));
        String fullTimeAwayTeamScore = String.valueOf(fullTimeJSON.get(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM));


        String matchId = String.valueOf(singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_MATCH_ID));


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
        match.setRefereesList(RefereesItemsListTest.getInstance().getReferees());


        return match;
    }

    /**
     * This method is used to add a match in the matchesOfOtherDaysMap, that contains for each
     * date string a list of matches, is used to store all matches that are not dailies matches
     *
     * @param match
     */
    private void addMatchOnTheOtherDaysMap(Match match) {
        /** if there is a List for the match date key this part of code is responsible to
         * create a list and store it with a key == match date
         */
        if (matchesOfOtherDaysMap.get(match.getDate()) == null) {
            List<Match> matchesDate = new ArrayList<>();
            matchesOfOtherDaysMap.put(match.getDate(), matchesDate);
        }

        /** if the match is not already in the list it is added
         */
        if (!matchesOfOtherDaysMap.get(match.getDate()).contains(match))
            matchesOfOtherDaysMap.get(match.getDate()).add(match);
    }


    /**
     * This method is used to add to the start time of a match the current time zone
     * It is responsible to add to the start time the hours ot the System Time Zone, if for example
     * the time zone is 2 it has to add 2 hours to the start time
     * <p>
     * it returns true if the date is changed within the start time
     * it returns false if the date is still the same original date
     *
     * @param match
     * @param currentTimeZone
     */
    public boolean changeStartTimeAndDateIfNeeded(Match match, String currentTimeZone) {
        boolean dateChanged = false;
        String startTime = match.getStartTime();

        /** getting hour and minute form the start time*/
        String hour = startTime.substring(0, startTime.indexOf(":"));
        String minute = startTime.substring(startTime.indexOf(":"));

        /** parsing hour string to a int*/
        int intHour = Integer.parseInt(hour);

        /** adding the time zone int value to the original hours*/
        intHour += Integer.parseInt(currentTimeZone);

        /** if the new hour is >=24 we have to switch the date to the next day */
        if (intHour >= 24) {

            /** getting the correct new hour */
            intHour -= 24;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            /** creating the new date and converting it in a String */
            try {
                java.util.Date date = formatter.parse(match.getDate());
                long milliseconds = date.getTime();
                milliseconds += UtilitiesNumbers.DAY_IN_MILLISECONDS;
                Date sqlDate = new Date(milliseconds);
                match.setDate(sqlDate.toString());


            } catch (ParseException e) {
                e.printStackTrace();
            }

            /** the date has been changed */
            dateChanged = true;
        } else if (intHour < 0) {
            /** if the new hour is <0> we have to switch the date to the previous day */

            /** getting the correct new hour */
            intHour += 24;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            /** creating the new date and converting it in a String */
            try {
                java.util.Date date = formatter.parse(match.getDate());
                long milliseconds = date.getTime();
                milliseconds -= UtilitiesNumbers.DAY_IN_MILLISECONDS;
                Date sqlDate = new Date(milliseconds);
                match.setDate(sqlDate.toString());


            } catch (ParseException e) {
                e.printStackTrace();
            }

            /** the date has been changed */
            dateChanged = true;
        }

        /** if date is 0 here is changed to 00 */
        hour = String.valueOf(intHour);
        if (hour.equals(UtilitiesStrings.MATCHES_MIDNIGHT_HOUR_0)) {
            hour = UtilitiesStrings.MATCHES_MIDNIGHT_HOUR;
        }

        /** reassembling the hours and minutes as a string  */
        startTime = hour + minute;
        match.setStartTime(startTime);

        return dateChanged;
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
                    matchesOfOtherDaysMap.clear();
                    JSONObject matchesJSONObject = null;
                    try {
                        matchesJSONObject = new JSONObject(response.body().string());
                        JSONArray matchesJSONArray = matchesJSONObject.getJSONArray(UtilitiesStrings.MATCHES_API_JSON_MATCHES_ARRAY_NAME);

                        for (int i = 0; i < matchesJSONArray.length(); i++) {
                            Match match = setMatchesFromJSONArray(matchesJSONArray, i);


                            if (!context.getString(R.string.match_time_zone).equals(UtilitiesStrings.MATCH_TIME_ZONE_0)) {
                                changeStartTimeAndDateIfNeeded(match, context.getString(R.string.match_time_zone));
                                addMatchOnTheOtherDaysMap(match);
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


    /**
     * This method is used to return an HashMap that contains the value for the match status and the match time
     * the app has to show to the user
     *
     * @param matches
     * @return
     */
    public HashMap<String, String> getGraphicValuesFromStatus(Match matches) {

        /**
         * This is the HashMap the method will return, it has a string for the key and a string for the
         * value
         *
         * The value of current time is also a String but it represent the string value of an integer, that
         * is the id associated to a string that depends on application settings ( language )
         */
        HashMap<String, String> statusAndCurrentTime = new HashMap<>();
        switch (matches.getStatus()) {
            /**
             * when the match is over, the status have to show the final score*/
            case UtilitiesStrings.MATCHES_STATUS_FINISHED:
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_STATUS, matches.getFullTimeHomeTeamScore() + " - " + matches.getFullTimeAwayTeamScore());
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_CURRENT_TIME, String.valueOf(R.string.match_current_time_finished));

                break;
            /**
             * when the match is paused (end of first time), the status has to contain the half time score
             **/
            case UtilitiesStrings.MATCHES_STATUS_PAUSED:
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_STATUS, matches.getHalfTimeHomeTeamScore() + " - " + matches.getHalfTimeAwayTeamScore());
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_CURRENT_TIME, String.valueOf(R.string.match_current_time_paused));
                break;
            /**
             * when the match is cancelled , the match current time have to show a label to allow user to know that is cancelled*/
            case UtilitiesStrings.MATCHES_STATUS_CANCELED:
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_STATUS, "");
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_CURRENT_TIME, String.valueOf(R.string.match_current_time_canceled));
                break;
            /**
             * when the match is suspended , the match current time have to show a label to allow user to know that is suspended*/
            case UtilitiesStrings.MATCHES_STATUS_SUSPENDED:
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_STATUS, "");
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_CURRENT_TIME, String.valueOf(R.string.match_current_time_suspended));
                break;

            /**
             * when the match is scheduled , the status have to be replaced with the match start time
             * in it is considered the local time zone*/
            case UtilitiesStrings.MATCHES_STATUS_SCHEDULED:
                String startTime = matches.getStartTime();
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_STATUS, startTime);
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_CURRENT_TIME, null);
                break;


            //TODO set current score (payment to API needed)
            case UtilitiesStrings.MATCHES_STATUS_IN_PLAY:
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_STATUS, matches.getHalfTimeHomeTeamScore() + " - " + matches.getHalfTimeAwayTeamScore());
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_CURRENT_TIME, String.valueOf(R.string.match_current_time_live));
                break;
            /**
             * default case */
            default:
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_STATUS, "");
                statusAndCurrentTime.put(UtilitiesStrings.MATCHES_API_JSON_MATCH_CURRENT_TIME, null);
                break;

            //TODO live case (payment to API needed)

        }

        return statusAndCurrentTime;
    }


    public HashMap<String, List<Match>> getMatchesOfOtherDaysMap() {
        return matchesOfOtherDaysMap;
    }

    public List<Match> getMatchesOfTheDayList() {
        return matchesOfTheDayList;
    }

    public List<Match> getMatchesListFromDate(String date) {
        return matchesOfOtherDaysMap.get(date);
    }

}