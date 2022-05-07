package com.cristiandrami.football365.ui.matches;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.cristiandrami.football365.model.utilities.matchesUtilities.CompetitionsUtilities;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class MatchesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private CompetitionsUtilities competitionsUtilities= CompetitionsUtilities.getInstance();

    public MatchesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void executeAPICall() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.football-data.org/v2/matches?dateFrom=2022-05-02&&dateTo=2022-05-08")
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
                        matchesJSONObject = new JSONObject(response.body().string());
                        JSONArray matchesJSONArray = matchesJSONObject.getJSONArray(UtilitiesStrings.MATCHES_API_JSON_MATCHES_ARRAY_NAME);
                        for(int i=0; i<matchesJSONArray.length();i++){
                            JSONObject singleMatchJSONObject=(JSONObject) matchesJSONArray.get(i);
                            String matchStatus=singleMatchJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_MATCH_STATUS);
                            String matchDate=singleMatchJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_MATCH_DATE);

                            JSONObject competitionJSONObject= (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_COMPETITION_NAME);
                            String competitionId= competitionJSONObject.getString(UtilitiesStrings.MATCHES_API_JSON_COMPETITION_ID_FIELD);

                            JSONObject homeTeam= (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM);
                            String homeTeamName= homeTeam.getString(UtilitiesStrings.MATCHES_API_JSON_HOME_TEAM_NAME);

                            JSONObject awayTeam= (JSONObject) singleMatchJSONObject.get(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM);
                            String awayTeamName= awayTeam.getString(UtilitiesStrings.MATCHES_API_JSON_AWAY_TEAM_NAME);


                        }

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