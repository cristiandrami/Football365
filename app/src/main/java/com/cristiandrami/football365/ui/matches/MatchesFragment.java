package com.cristiandrami.football365.ui.matches;

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
import com.cristiandrami.football365.model.utilities.matchesUtilities.Competition;
import com.cristiandrami.football365.model.utilities.matchesUtilities.CompetitionsUtilities;
import com.cristiandrami.football365.model.utilities.matchesUtilities.Match;


import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MatchesFragment extends Fragment {

    private MatchesViewModel matchesViewModel;
    private FragmentMatchesBinding binding;
    private LinearLayout matchFragmentLinearLayout;
    private HashMap<String, Competition> competitionHashMap;
    private final HashMap<String, View> graphicCompetitionHashMap = new HashMap<>();
    private HashMap<Integer, String> datesPositionMap= new HashMap<>();
    private int currentPosition=0;
    private static final int maxPosition= UtilitiesNumbers.MATCHES_DAYS;

    private List<Match> matchList;

    private final HashMap<String, View> graphicMatchesHashMap = new HashMap<>();

    private TextView leftArrow;
    private TextView rightArrow;

    private TextView currentDateTextView;

    private ProgressBar progressBar;


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
        leftArrow=binding.matchesFragmentLeftArrow;
        rightArrow=binding.matchesFragmentRightArrow;
        currentDateTextView=binding.matchesFragmentCurrentDate;

        setLeftArrowClickListener();
        setRightArrowClickListener();


        matchesViewModel.setPositionDatesMap(datesPositionMap);

        setCurrentDateCardViewInfo();

        competitionHashMap = CompetitionsUtilities.getInstance().getCompetitions();

        //matchesViewModel.updateMatchesList(this);


        matchesViewModel.updateMatchesListV2(this, datesPositionMap.get(currentPosition), getContext());

        rightArrow.setVisibility(View.INVISIBLE);
        matchesViewModel.updateNextMatches(maxPosition, datesPositionMap, getContext(), this);
        //final TextView textView = binding.textDashboard;
        //matchesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        updateMatchesEachMinute();



        return root;
    }

    public void makeRightArrowVisible() {
        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    rightArrow.setVisibility(View.VISIBLE);

                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateMatchesEachMinute() {
        int delay = 5000;   // delay for 5 sec.
        int period = 1000*60;  // repeat every sec.
        Timer timer = new Timer();
        MatchesFragment fragment=this;

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                matchesViewModel.updateMatchesListV2(fragment, datesPositionMap.get(currentPosition), getContext());
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
            try{
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
            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }

    public void refreshMatchesViewV2(List<Match> matchesList) {
        this.matchList=matchesList;

        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showMatchesList(matchesList);

                }
            });

        }catch(Exception e){
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
        for (int i = 0; i< matchesList.size(); i++) {
            Match matches= matchesList.get(i);

            if (graphicCompetitionHashMap.get(matches.getCompetitionId()) == null) {
                createCompetitionView(matches.getCompetitionId());
            }

            /**
             * getting section in which the app will put the matches, for each competition*/

            View competitionSection = graphicCompetitionHashMap.get(matches.getCompetitionId());
            LinearLayout competitionSectionLinearLayout = competitionSection.findViewById(R.id.competition_matches_linear_layout);


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


            /**
             * adding a the single match to the competition section layout*/
            competitionSectionLinearLayout.addView(matchView);
            graphicMatchesHashMap.put(matches.getMatchId(), matchView);
        }

        //setMatchesViewInfo();


        /**
         * show competitions and matches and hide progressbar*/
        setGraphicalWaitingEffect(View.INVISIBLE, View.VISIBLE);
    }

    private void setMatchesGraphicValuesFromStatus(Match matches, TextView status, TextView currentTime) {
        switch (matches.getStatus()) {
            /**
             * when the match is over, the status have to show the final score*/
            case UtilitiesStrings.MATCHES_STATUS_FINISHED:
                status.setText(matches.getFullTimeHomeTeamScore() + " - " + matches.getFullTimeAwayTeamScore());
                currentTime.setText(getString(R.string.match_current_time_finished));
                break;
            /**
             * when the match is paused (end of first time), the status have to show the half time score*/
            case UtilitiesStrings.MATCHES_STATUS_PAUSED:
                status.setText(matches.getHalfTimeHomeTeamScore() + " - " + matches.getHalfTimeAwayTeamScore());
                currentTime.setText(getString(R.string.match_current_time_paused));
                break;
            /**
             * when the match is cancelled , the match current time have to show a label to allow user to know that is cancelled*/
            case UtilitiesStrings.MATCHES_STATUS_CANCELED:
                currentTime.setText(getString(R.string.match_current_time_canceled));
                break;
            /**
             * when the match is suspended , the match current time have to show a label to allow user to know that is suspended*/
            case UtilitiesStrings.MATCHES_STATUS_SUSPENDED:
                currentTime.setText(getString(R.string.match_current_time_suspended));
                break;
            /**
             * when the match is scheduled , the status have to be replaced with the match start time
             * in it is considered the local time zone*/
            case UtilitiesStrings.MATCHES_STATUS_SCHEDULED:
                String startTime= matches.getStartTime();
                status.setText(startTime);
                currentTime.setText("");

                break;

            /**
             * default case */
            //TODO set current score (payment to API needed)
            case UtilitiesStrings.MATCHES_STATUS_IN_PLAY:
                status.setText(matches.getHalfTimeHomeTeamScore() + " - " + matches.getHalfTimeAwayTeamScore());
                //currentTime.setText(getString(R.string.match_current_time_paused));
                break;
            /**
             * default case */
            default:
                break;
            //TODO live case

        }
    }



    private void setLeftArrowClickListener() {
        MatchesFragment fragment= this;
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
                setGraphicalWaitingEffect(View.VISIBLE, View.INVISIBLE);

                updateViewOnDateChanging(fragment);


                //setMatchesViewInfo();
            }
        });
    }

    private void updateViewOnDateChanging(MatchesFragment fragment) {
        matchFragmentLinearLayout.removeAllViews();
        if(currentPosition==0){
            matchesViewModel.updateMatchesListV2(fragment, datesPositionMap.get(currentPosition), getContext());
        }else{
            List<Match> nextMatches=matchesViewModel.getNextMatchesList(datesPositionMap.get(currentPosition));
            showMatchesList(nextMatches);
        }
    }


    private void setRightArrowClickListener() {
        MatchesFragment fragment= this;
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
                setGraphicalWaitingEffect(View.VISIBLE, View.INVISIBLE);
                updateViewOnDateChanging(fragment);
                //setMatchesViewInfo();

            }
        });
    }


    private void setGraphicalWaitingEffect(int visible, int invisible) {
        progressBar.setVisibility(visible);
        matchFragmentLinearLayout.setVisibility(invisible);
    }





    private void setCurrentDateCardViewInfo() {
        /**
         * switch over current date position */
        switch(currentPosition){
            /**
             * first date position, doesn't allow to go back, left arrow is hidden */
            case 0:
                leftArrow.setVisibility(View.INVISIBLE);
                rightArrow.setVisibility(View.VISIBLE);
                break;
            /**
             * last date position, doesn't allow to go to next date, right arrow is hidden */
            case maxPosition-1:
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