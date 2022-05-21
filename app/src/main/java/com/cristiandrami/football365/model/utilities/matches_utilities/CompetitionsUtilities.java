package com.cristiandrami.football365.model.utilities.matches_utilities;

import android.annotation.SuppressLint;
import android.util.Log;

import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class is used to store temporarily the information about the available football competitions
 * All information are retrieved by an API call
 *
 * This class is a Singleton class, we need only one instance in all application
 * @author Cristian D. Dramisino
 *
 */

public class CompetitionsUtilities {


    private HashMap<String, Competition> competitions= new HashMap<>();
    private static CompetitionsUtilities instance=null;

    public HashMap<String, Competition> getCompetitions() {
        return competitions;
    }

    public static CompetitionsUtilities getInstance(){
        if(instance==null){
            instance= new CompetitionsUtilities();
        }
        return instance;
    }

    private CompetitionsUtilities(){
        retrieveCompetitionsFromAPI();
    }



    /**
     *This method call an API to retrieve the competitions and stores them in an HashMap
     * The key of the HashMap is the competition id
     * The value of the HashMap is the competition name - competition geographic area 
     */
    private  void retrieveCompetitionsFromAPI() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(UtilitiesStrings.COMPETITION_API_URL)
                .get()
                .addHeader("X-Auth-Token", UtilitiesStrings.COMPETITION_API_KEY)
                .build();
        client.setProtocols(Arrays.asList(Protocol.HTTP_1_1));

        client.newCall(request).enqueue(new Callback() {


            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("api", e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(!response.isSuccessful()) {
                    Log.e("api ", response.body().string());
                } else {
                    fillCompetitionsList(response.body().string());


                }
            }
        });
    }



    /**
     *This method reads the information obtained by the API and stores in the HashMap the competitions
     * The information are contained in a String that is converted in an JSON
     */
    @SuppressLint("LongLogTag")
    private void fillCompetitionsList(String competitionsString) {
        try {
            JSONObject competitionsJSON=new JSONObject(competitionsString);
            JSONArray competitionsJSONArray = competitionsJSON.getJSONArray(UtilitiesStrings.COMPETITION_API_COMPETITIONS_ARRAY_NAME);

            for(int i=0; i<competitionsJSONArray.length(); i++){
                JSONObject competitionJSONObject= (JSONObject) competitionsJSONArray.get(i);

                JSONObject competitionAreaJSONObject=competitionJSONObject.getJSONObject(UtilitiesStrings.COMPETITION_API_JSON_AREA_OBJECT_NAME);
                String name=competitionJSONObject.getString(UtilitiesStrings.COMPETITION_API_JSON_COMPETITION_NAME_FIELD);
                String area=competitionAreaJSONObject.getString(UtilitiesStrings.COMPETITION_API_JSON_COMPETITION_AREA_NAME_FIELD);
                String id= competitionJSONObject.getString(UtilitiesStrings.COMPETITION_API_JSON_COMPETITION_ID_FIELD);
                String imageUrl= competitionAreaJSONObject.getString(UtilitiesStrings.COMPETITION_API_JSON_COMPETITION_IMAGE_FIELD);

                Competition competition= new Competition();
                competition.setId(id);
                competition.setGeographicalArea(area);
                competition.setName(name);
                competition.setImageUrl(imageUrl);


                competitions.put(id, competition);
            }

        }catch (Exception e){
            Log.e("[CompetitionUtilities] method setCompetitionsMapValues", e.toString());

        }
    }



}
