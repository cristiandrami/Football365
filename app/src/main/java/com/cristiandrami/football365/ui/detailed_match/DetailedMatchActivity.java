package com.cristiandrami.football365.ui.detailed_match;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.detailed_match.comment.CommentItem;
import com.cristiandrami.football365.model.detailed_match.line_up.Player;
import com.cristiandrami.football365.model.utilities.matches_utilities.Match;
import com.google.gson.Gson;

import java.util.List;

/**
 * This is the class that manages the Detailed Match Activity, it is a graphical controller
 *
 * {@link Match} is passed from the Match Fragment and it is used to get the data
 * @see Match
 * @see Player
 * @see CommentItem
 * @see DetailedMatchActivityModel
 * @author Cristian D. Dramisino
 *
 */
public class DetailedMatchActivity extends AppCompatActivity {

    private DetailedMatchActivityModel detailedMatchActivityModel;

    private Match match;
    private LinearLayout lineUpLayout;
    private LinearLayout commentLayout;

    private LinearLayout refereesLayout;

    private TextView backToMainActivityTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_match);
        this.getSupportActionBar().hide();

        detailedMatchActivityModel= new DetailedMatchActivityModel();

        setGraphicalBinding();



        setGraphicalValues();
        setBackToMainActivityTextViewListener();
    }

    private void setBackToMainActivityTextViewListener() {
        backToMainActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setGraphicalValues() {
        match= new Gson().fromJson(getIntent().getStringExtra("match"), Match.class);
        setPlayersLineUp(match);
        setTeamsNames(match);
        setMatchScore(match);
        setCommentSection(match);
        setRefereesSection(match);
        setDate(match);
    }

    private void setGraphicalBinding() {
        lineUpLayout= findViewById(R.id.activity_detailed_match_line_up_linear_layout);
        commentLayout=findViewById(R.id.activity_detailed_match_comment_layout);
        refereesLayout=findViewById(R.id.activity_detailed_match_referees_layout);
        backToMainActivityTextView=findViewById(R.id.activity_detailed_match_back_to_main_activity_text_view);
    }


    /**
     * This method is called when we open the app, it shows the match date
     * @param match contains all information */
    private void setDate(Match match) {
        TextView dateView= findViewById(R.id.detailed_match_date);
        dateView.setText(match.getDate());
    }


    /**
     * This method is called when we open the app, here we get the referees list from the Match object
     * For each referee it creates a new View that is added to the referees linear layout
     * @param match contains all information */
    private void setRefereesSection(Match match) {

        List<String> refereesList= match.getRefereesList();
        for(int i=0; i<refereesList.size(); i++){
            View refereeView= getLayoutInflater().inflate(R.layout.detailed_match_referees_item_layout, null);
            TextView refereeName= refereeView.findViewById(R.id.detailed_match_referees_name);
            refereeName.setText(refereesList.get(i));

            refereesLayout.addView(refereeView);
        }

    }



    /**
     * This method is called when we open the app, here we get the comment items list from the Match object
     * For each comment item it creates a new View that is added to the comment linear layout
     * if the comment item is related to the home team, the away team information are hidden
     * if the comment item is related to the away team, the home team information are hidden
     * @param match contains all information */
    private void setCommentSection(Match match) {

        detailedMatchActivityModel.fillCommentSectionLayout(match, this, commentLayout);
    }




    /**
     * This method is called when we open the app, here we get current match score, if the match is
     * not started this method shows the starting time
     * @see DetailedMatchActivityModel
     * @param match contains all information */
    private void setMatchScore(Match match) {

        TextView matchStatus= findViewById(R.id.detailed_match_current_score);
        String statusToSet=detailedMatchActivityModel.getScoreToSetFromStatus(match, this);
        matchStatus.setText(statusToSet);
    }



    /**
     * This method is called when we open the app, it sets the teams match
     * @param match contains all information */
    private void setTeamsNames(Match match) {
        TextView homeTeamName= findViewById(R.id.detailed_match_home_team_name);
        homeTeamName.setText(match.getHomeTeam());

        TextView awayTeamName= findViewById(R.id.detailed_match_away_team_name);
        awayTeamName.setText(match.getAwayTeam());


    }

    /**
     * This method is called when we open the app, it is used to show the team's line up
     * In addition it sets the team's modules counting the number of player per type
     * @param match contains all information */

    private void setPlayersLineUp(Match match) {


        detailedMatchActivityModel.fillLineUpLayout(match, this, lineUpLayout);

        TextView homeDefendersNumber= findViewById(R.id.detailed_match_activity_line_up_home_defenders_number);
        TextView homeMidfieldersNumber= findViewById(R.id.detailed_match_activity_line_up_home_midfielder_number);
        TextView homeStrikersNumber= findViewById(R.id.detailed_match_activity_line_up_home_strikers_number);


        TextView awayDefendersNumber= findViewById(R.id.detailed_match_activity_line_up_away_defenders_number);
        TextView awayMidfieldersNumber= findViewById(R.id.detailed_match_activity_line_up_away_midfielder_number);
        TextView awayStrikersNumber= findViewById(R.id.detailed_match_activity_line_up_away_strikers_number);

        homeDefendersNumber.setText(String.valueOf(detailedMatchActivityModel.getHomeDefenders()));
        homeMidfieldersNumber.setText(String.valueOf(detailedMatchActivityModel.getHomeMidfielders()));
        homeStrikersNumber.setText(String.valueOf(detailedMatchActivityModel.getHomeStrikers()));

        awayDefendersNumber.setText(String.valueOf(detailedMatchActivityModel.getAwayDefenders()));
        awayMidfieldersNumber.setText(String.valueOf(detailedMatchActivityModel.getAwayMidfielders()));
        awayStrikersNumber.setText(String.valueOf(detailedMatchActivityModel.getAwayStrikers()));

    }




}