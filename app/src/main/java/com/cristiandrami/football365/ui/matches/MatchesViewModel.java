package com.cristiandrami.football365.ui.matches;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cristiandrami.football365.model.utilities.matchesUtilities.CompetitionsUtilities;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
                if(!response.isSuccessful()) {
                    Log.e("api ", response.body().string());
                } else {
                    Log.e("api ok", response.body().string());
                }
            }
        });
    }
}