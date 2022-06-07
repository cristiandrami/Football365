package com.cristiandrami.football365.ui.matches;

import android.content.Intent;
import android.os.Bundle;
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
import com.cristiandrami.football365.model.utilities.UtilitiesNumbers;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.cristiandrami.football365.model.utilities.matches_utilities.Competition;
import com.cristiandrami.football365.model.utilities.matches_utilities.CompetitionsUtilities;
import com.cristiandrami.football365.model.utilities.matches_utilities.Match;
import com.cristiandrami.football365.ui.detailed_match.DetailedMatchActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MatchesFragment extends Fragment {

    private static final int maxPosition = UtilitiesNumbers.MATCHES_DAYS;
    private static final int minPosition = (UtilitiesNumbers.MATCHES_PREVIOUS_DAYS * -1);
    private final HashMap<String, View> graphicCompetitionHashMap = new HashMap<>();
    private final HashMap<String, View> graphicMatchesHashMap = new HashMap<>();
    private final HashMap<Integer, String> datesPositionMap = new HashMap<>();
    private MatchesViewModel matchesViewModel;
    private FragmentMatchesBinding binding;
    private LinearLayout matchFragmentLinearLayout;
    private HashMap<String, Competition> competitionHashMap;
    private List<Match> matchList;
    private int currentPosition = 0;

    private TextView leftArrow;
    private TextView rightArrow;

    private TextView currentDateTextView;

    private ProgressBar progressBar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        matchesViewModel =
                new ViewModelProvider(this).get(MatchesViewModel.class);

        binding = FragmentMatchesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        matchFragmentLinearLayout = binding.matchesFragmentLinearLayout;
        progressBar = binding.matchesFragmentProgressBar;

        matchFragmentLinearLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        leftArrow = binding.matchesFragmentLeftArrow;
        rightArrow = binding.matchesFragmentRightArrow;
        currentDateTextView = binding.matchesFragmentCurrentDate;

        setLeftArrowClickListener();
        setRightArrowClickListener();


        matchesViewModel.setPositionDatesMap(minPosition, maxPosition, datesPositionMap);

        setCurrentDateCardViewInfo();

        competitionHashMap = CompetitionsUtilities.getInstance().getCompetitions();


        matchesViewModel.updateMatchesListV2(this, datesPositionMap.get(currentPosition), getContext());

        rightArrow.setVisibility(View.INVISIBLE);
        leftArrow.setVisibility(View.INVISIBLE);
        matchesViewModel.updateNextAndPreviousMatches(minPosition, maxPosition, datesPositionMap, getContext(), this);


        updateMatchesEachMinute();


        return root;
    }

    public void notifyNextAndPreviousMatchesUpdateFinished() {
        try {
            Runnable showArrowsRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    rightArrow.setVisibility(View.VISIBLE);
                    leftArrow.setVisibility(View.VISIBLE);

                }
            };
            getActivity().runOnUiThread(showArrowsRunnable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMatchesEachMinute() {
        int delay = 2000;   // delay for 5 sec.
        int period = 1000 * 60;  // repeat every 60 sec.
        Timer timer = new Timer();
        MatchesFragment fragment = this;

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (currentPosition == 0) {
                    matchesViewModel.updateMatchesListV2(fragment, datesPositionMap.get(currentPosition), getContext());
                }
            }
        }, delay, period);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void createCompetitionView(String competitionId) {

        Competition competition = competitionHashMap.get(competitionId);

        if (competition != null) {
            try {
                View viewCompetition = getLayoutInflater().inflate(R.layout.competitions_matches_layout, null);

                TextView competitionInfo = viewCompetition.findViewById(R.id.competition_matches_info_text);
                ImageView competitionIcon = viewCompetition.findViewById(R.id.competition_matches_icon);

                competitionInfo.setText(competition.toString());
                ImageUtilities.loadSVGInImageView(getActivity(), competition.getImageUrl(), competitionIcon);

                /**
                 * adding the competition banner to the matches fragment */
                matchFragmentLinearLayout.addView(viewCompetition);
                /**
                 * store competition view in an HashMap (key = competition id)  */
                graphicCompetitionHashMap.put(competition.getId(), viewCompetition);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void refreshMatchesViewV2(List<Match> matchesList) {
        matchList = matchesList;

        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showMatchesList(matchesList);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showMatchesList(List<Match> matchesList) {
        /**
         * data structures that contain views and information have to be cleaned each time the application
         * updates the graphics*/
        matchFragmentLinearLayout.removeAllViews();
        graphicMatchesHashMap.clear();
        graphicCompetitionHashMap.clear();
        if (matchesList != null) {
            showMatchesFromMatchList(matchesList);
        }


        /**
         * show competitions and matches and hide progressbar*/
        hideWaitingEffect(View.VISIBLE, View.INVISIBLE);
    }

    private void showMatchesFromMatchList(List<Match> matchesList) {
        for (int i = 0; i < matchesList.size(); i++) {
            Match matches = matchesList.get(i);

            if (graphicCompetitionHashMap.get(matches.getCompetitionId()) == null) {
                createCompetitionView(matches.getCompetitionId());
            }

            /**
             * getting section in which the app will put the matches, for each competition*/

            View competitionSection = graphicCompetitionHashMap.get(matches.getCompetitionId());
            LinearLayout competitionSectionLinearLayout;
            try {
                competitionSectionLinearLayout = competitionSection.findViewById(R.id.competition_matches_linear_layout);
                /**
                 * new single match creation*/
                View matchView = getLayoutInflater().inflate(R.layout.single_match_layout, null);

                TextView homeTeam = matchView.findViewById(R.id.single_match_home_team);
                homeTeam.setText(matches.getHomeTeam());

                TextView awayTeam = matchView.findViewById(R.id.single_match_away_team);
                awayTeam.setText(matches.getAwayTeam());

                TextView status = matchView.findViewById(R.id.single_match_status);
                TextView currentTime = matchView.findViewById(R.id.single_match_current_time);

                /**
                 * new single match graphical values setting, this is based on the match current status*/
                setMatchesGraphicValuesFromStatus(matches, status, currentTime);


                addSwitchToDetailedActivityListenerToMatch(matchView, matches);

                /**
                 * adding a the single match to the competition section layout*/
                competitionSectionLinearLayout.addView(matchView);
                graphicMatchesHashMap.put(matches.getMatchId(), matchView);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    private void addSwitchToDetailedActivityListenerToMatch(View matchView, Match match) {
        matchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchToDetailedMatchActivity = new Intent(getActivity(), DetailedMatchActivity.class);
                switchToDetailedMatchActivity.putExtra("match", new Gson().toJson(match));
                startActivity(switchToDetailedMatchActivity);
            }
        });
    }

    private void setMatchesGraphicValuesFromStatus(Match matches, TextView status, TextView currentTime) {
        HashMap<String,String> graphicMapValues=matchesViewModel.getGraphicValuesFromStatus(matches);
        String statusString= graphicMapValues.get(UtilitiesStrings.MATCHES_API_JSON_MATCH_STATUS);
        String currentTimeString= graphicMapValues.get(UtilitiesStrings.MATCHES_API_JSON_MATCH_CURRENT_TIME);


        status.setText(statusString);

        if(currentTimeString!=null){
            Integer currentTimeID= Integer.valueOf(currentTimeString);
            currentTime.setText(getString(currentTimeID));
        }


    }


    private void setLeftArrowClickListener() {
        MatchesFragment fragment = this;
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 *we are going back to previous date, the current position have to be decreased */
                currentPosition--;

                /**
                 * current date card view updating */
                setCurrentDateCardViewInfo();

                /**
                 * matches updating calling the API  */
                showWaitingEffect(View.VISIBLE, View.INVISIBLE);

                updateViewOnDateChanging(fragment);


            }
        });
    }

    private void updateViewOnDateChanging(MatchesFragment fragment) {
        //matchFragmentLinearLayout.removeAllViews();
        if (currentPosition == 0) {
            matchesViewModel.updateMatchesListV2(fragment, datesPositionMap.get(currentPosition), getContext());
        } else {
            List<Match> nextMatches = matchesViewModel.getMatchesListFromDate(datesPositionMap.get(currentPosition));
            showMatchesList(nextMatches);
        }
    }


    private void setRightArrowClickListener() {
        MatchesFragment fragment = this;
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 *we are going back to previous date, the current position have to be increased */
                currentPosition++;

                /**
                 * current date card view updating */
                setCurrentDateCardViewInfo();

                /**
                 * matches updating calling the API  */
                showWaitingEffect(View.VISIBLE, View.INVISIBLE);
                updateViewOnDateChanging(fragment);

            }
        });
    }


    private void hideWaitingEffect(int visible, int invisible) {
        progressBar.setVisibility(invisible);
        matchFragmentLinearLayout.setVisibility(visible);
    }

    private void showWaitingEffect(int visible, int invisible) {
        progressBar.setVisibility(visible);
        matchFragmentLinearLayout.setVisibility(invisible);
    }


    private void setCurrentDateCardViewInfo() {
        /**
         * switch over current date position */
        switch (currentPosition) {
            /**
             * first date position, doesn't allow to go back, left arrow is hidden */
            case minPosition + 1:
                leftArrow.setVisibility(View.INVISIBLE);
                rightArrow.setVisibility(View.VISIBLE);
                break;
            /**
             * last date position, doesn't allow to go to next date, right arrow is hidden */
            case maxPosition - 1:
                leftArrow.setVisibility(View.VISIBLE);
                rightArrow.setVisibility(View.INVISIBLE);
                break;
            /**
             * default case: both arrows are visible */
            default:
                leftArrow.setVisibility(View.VISIBLE);
                rightArrow.setVisibility(View.VISIBLE);
                break;

        }

        currentDateTextView.setText(datesPositionMap.get(currentPosition));
    }

}