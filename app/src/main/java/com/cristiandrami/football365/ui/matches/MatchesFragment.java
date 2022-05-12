package com.cristiandrami.football365.ui.matches;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.databinding.FragmentMatchesBinding;
import com.cristiandrami.football365.model.utilities.ImageUtilities;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.cristiandrami.football365.model.utilities.matchesUtilities.Competition;
import com.cristiandrami.football365.model.utilities.matchesUtilities.CompetitionsUtilities;
import com.cristiandrami.football365.model.utilities.matchesUtilities.Match;


import java.util.HashMap;
import java.util.List;

public class MatchesFragment extends Fragment {

    private FragmentMatchesBinding binding;
    private LinearLayout linearLayout;
    private HashMap<String, Competition> competitionHashMap;
    private final HashMap<String, View> graphicCompetitionHashMap = new HashMap<>();

    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MatchesViewModel matchesViewModel =
                new ViewModelProvider(this).get(MatchesViewModel.class);

        binding = FragmentMatchesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        linearLayout = binding.matchesFragmentLinearLayout;
        progressBar = binding.matchesFragmentProgressBar;

        linearLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        competitionHashMap = CompetitionsUtilities.getInstance().getCompetitions();

        matchesViewModel.updateMatchesList(this);


        //final TextView textView = binding.textDashboard;
        //matchesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void refreshMatchesView(List<Match> matchesList) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Match matches : matchesList) {

                    if (graphicCompetitionHashMap.get(matches.getCompetitionId()) == null) {
                        createCompetitionView(matches.getCompetitionId());
                    }

                    View matchesBanner = graphicCompetitionHashMap.get(matches.getCompetitionId());
                    LinearLayout linearLayout = matchesBanner.findViewById(R.id.competition_matches_linear_layout);

                    View matchView = getLayoutInflater().inflate(R.layout.single_match_layout, null);
                    TextView homeTeam = matchView.findViewById(R.id.single_match_home_team);
                    homeTeam.setText(matches.getHomeTeam());
                    TextView awayTeam = matchView.findViewById(R.id.single_match_away_team);
                    awayTeam.setText(matches.getAwayTeam());
                    TextView status = matchView.findViewById(R.id.single_match_status);
                    TextView currentTime = matchView.findViewById(R.id.single_match_current_time);

                    switch (matches.getStatus()) {
                        case UtilitiesStrings.MATCHES_STATUS_FINISHED:
                            status.setText(matches.getFullTimeHomeTeamScore() + " - " + matches.getFullTimeAwayTeamScore());
                            currentTime.setText(getString(R.string.match_current_time_finished));
                            break;
                        case UtilitiesStrings.MATCHES_STATUS_PAUSED:
                            status.setText(matches.getHalfTimeHomeTeamScore() + " - " + matches.getHalfTimeAwayTeamScore());
                            currentTime.setText(getString(R.string.match_current_time_paused));
                            break;
                        case UtilitiesStrings.MATCHES_STATUS_CANCELED:
                            currentTime.setText(getString(R.string.match_current_time_canceled));
                            break;
                        case UtilitiesStrings.MATCHES_STATUS_SUSPENDED:
                            currentTime.setText(getString(R.string.match_current_time_suspended));
                            break;
                        case UtilitiesStrings.MATCHES_STATUS_SCHEDULED:
                            Log.e("date", matches.getDate());

                            String date = matches.getDate();
                            date = date.substring(date.indexOf("T") + 1);
                            date = date.substring(0, date.indexOf("Z") - 3);

                            status.setText(date);
                            currentTime.setText("");

                            break;
                        //TODO live case

                    }

                    linearLayout.addView(matchView);
                }

                progressBar.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);

            }
        });


    }

    private void createCompetitionView(String competitionId) {


        Competition competition = competitionHashMap.get(competitionId);

        if (competition != null) {
            View viewCompetition = getLayoutInflater().inflate(R.layout.competitions_matches_layout, null);

            TextView competitionInfo = viewCompetition.findViewById(R.id.competition_matches_info_text);
            ImageView competitionIcon = viewCompetition.findViewById(R.id.competition_matches_icon);


            competitionInfo.setText(competition.toString());
            //Log.e("image", competitionObject.getImageUrl());
            ImageUtilities.loadSVGInImageView(getActivity(), competition.getImageUrl(), competitionIcon);

            linearLayout.addView(viewCompetition);

            graphicCompetitionHashMap.put(competition.getId(), viewCompetition);
        }

    }
}