package com.cristiandrami.football365.ui.detailed_match;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.detailed_match.comment.CommentItem;
import com.cristiandrami.football365.model.detailed_match.comment.CommentUtilities;
import com.cristiandrami.football365.model.detailed_match.line_up.Player;
import com.cristiandrami.football365.model.detailed_match.line_up.PlayerUtilities;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.cristiandrami.football365.model.utilities.matches_utilities.Match;
import com.google.gson.Gson;

import java.util.List;

public class DetailedMatchActivity extends AppCompatActivity {

    private int homeDefenders=0;
    private int homeMidfielders=0;
    private int homeStrikers=0;
    private int awayDefenders=0;
    private int awayMidfielders=0;
    private int awayStrikers=0;

    private Match match;
    private LinearLayout lineUpLayout;
    private LinearLayout commentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_match);

        lineUpLayout= findViewById(R.id.detailed_match_activity_line_up_linear_layout);
        commentLayout=findViewById(R.id.detailed_match_activity_comment_layout);
        match= new Gson().fromJson(getIntent().getStringExtra("match"), Match.class);

        setPlayersLineUp(match);
        setTeamsNames(match);
        setMatchScore(match);
        setCommentSection(match);
    }

    private void setCommentSection(Match match) {

        List<CommentItem> commentItemList= match.getCommentItemList();
        for(int i=0; i<commentItemList.size(); i++){
            CommentItem commentItem= commentItemList.get(i);
            View commentView= getLayoutInflater().inflate(R.layout.detailed_match_comment_item_layout, null);;
            TextView homeEvent= commentView.findViewById(R.id.detailed_match_comment_item_home_event);
            TextView awayEvent= commentView.findViewById(R.id.detailed_match_comment_item_away_event);
            ImageView commentIcon= commentView.findViewById(R.id.detailed_match_comment_item_icon);
            TextView commentMinute= commentView.findViewById(R.id.detailed_match_comment_item_minute);
            if(commentItem.getTeam().equals(CommentUtilities.HOME_TEAM_COMMENT)){
                homeEvent.setText(commentItem.getDescription());
                awayEvent.setText("");
            }else if(commentItem.getTeam().equals(CommentUtilities.AWAY_TEAM_COMMENT)){
                awayEvent.setText(commentItem.getDescription());
                homeEvent.setText("");
            }

            commentIcon.setImageResource(CommentUtilities.commentIconFromType.get(commentItem.getType()));
            commentMinute.setText(String.valueOf(commentItem.getMinute()));
            commentLayout.addView(commentView);
        }
        Log.e("comment list size", String.valueOf(commentItemList.size()));
        Log.e("comment view size", String.valueOf(commentLayout.getChildCount()));
    }

    private void setMatchScore(Match match) {

        TextView matchStatus= findViewById(R.id.detailed_match_current_score);

        switch (match.getStatus()) {
            /**
             * when the match is over, the status have to show the final score*/
            case UtilitiesStrings.MATCHES_STATUS_FINISHED:
                matchStatus.setText(match.getFullTimeHomeTeamScore() + " - " + match.getFullTimeAwayTeamScore());
                break;
            /**
             * when the match is paused (end of first time), the status have to show the half time score*/
            case UtilitiesStrings.MATCHES_STATUS_PAUSED:
                matchStatus.setText(match.getHalfTimeHomeTeamScore() + " - " + match.getHalfTimeAwayTeamScore());
                break;
            /**
             * when the match is cancelled , the match current time have to show a label to allow user to know that is cancelled*/
            case UtilitiesStrings.MATCHES_STATUS_CANCELED:
                matchStatus.setText(getString(R.string.match_current_time_canceled));
                break;
            /**
             * when the match is suspended , the match current time have to show a label to allow user to know that is suspended*/
            case UtilitiesStrings.MATCHES_STATUS_SUSPENDED:
                matchStatus.setText(getString(R.string.match_current_time_suspended));
                break;
            /**
             * when the match is scheduled , the status have to be replaced with the match start time
             * in it is considered the local time zone*/
            case UtilitiesStrings.MATCHES_STATUS_SCHEDULED:
                String startTime = match.getStartTime();
                matchStatus.setText(startTime);
                break;

            /**
             * default case */
            //TODO set current score (payment to API needed)
            case UtilitiesStrings.MATCHES_STATUS_IN_PLAY:
                matchStatus.setText(match.getHalfTimeHomeTeamScore() + " - " + match.getHalfTimeAwayTeamScore());
                break;
            /**
             * default case */
            default:
                break;
            //TODO live case

        }
    }

    private void setTeamsNames(Match match) {
        TextView homeTeamName= findViewById(R.id.detailed_match_home_team_name);
        homeTeamName.setText(match.getHomeTeam());

        TextView awayTeamName= findViewById(R.id.detailed_match_away_team_name);
        awayTeamName.setText(match.getAwayTeam());


    }

    private void setPlayersLineUp(Match match) {


        for(int i=0; i<match.getAwayPlayers().size(); i++){
            Player homePlayer= match.getHomePlayers().get(i);
            Player awayPlayer= match.getAwayPlayers().get(i);


            View playersView = getLayoutInflater().inflate(R.layout.line_up_players_layout, null);
            ImageView homePlayerIcon = playersView.findViewById(R.id.line_up_player_home_icon);
            ImageView awayPlayerIcon = playersView.findViewById(R.id.line_up_player_away_icon);
            TextView homePlayerName= playersView.findViewById(R.id.line_up_player_home);
            TextView awayPlayerName= playersView.findViewById(R.id.line_up_player_away);

            homePlayerIcon.setImageResource(PlayerUtilities.playerIconFromType.get(homePlayer.getPosition()));
            awayPlayerIcon.setImageResource(PlayerUtilities.playerIconFromType.get(awayPlayer.getPosition()));

            homePlayerName.setText(homePlayer.getName());
            awayPlayerName.setText(awayPlayer.getName());

            updateHomeCounters(homePlayer);
            updateAwayCounters(awayPlayer);

            lineUpLayout.addView(playersView);

        }
        Log.e("line up children",String.valueOf( lineUpLayout.getChildCount()));

        TextView homeDefendersNumber= findViewById(R.id.detailed_match_activity_line_up_home_defenders_number);
        TextView homeMidfieldersNumber= findViewById(R.id.detailed_match_activity_line_up_home_midfielder_number);
        TextView homeStrikersNumber= findViewById(R.id.detailed_match_activity_line_up_home_strikers_number);


        TextView awayDefendersNumber= findViewById(R.id.detailed_match_activity_line_up_away_defenders_number);
        TextView awayMidfieldersNumber= findViewById(R.id.detailed_match_activity_line_up_away_midfielder_number);
        TextView awayStrikersNumber= findViewById(R.id.detailed_match_activity_line_up_away_strikers_number);

        homeDefendersNumber.setText(String.valueOf(homeDefenders));
        homeMidfieldersNumber.setText(String.valueOf(homeMidfielders));
        homeStrikersNumber.setText(String.valueOf(homeStrikers));

        awayDefendersNumber.setText(String.valueOf(awayDefenders));
        awayMidfieldersNumber.setText(String.valueOf(awayMidfielders));
        awayStrikersNumber.setText(String.valueOf(awayStrikers));

    }

    private void updateAwayCounters(Player awayPlayer) {
        switch(awayPlayer.getPosition()){
            case PlayerUtilities.DEFENDER_PLAYER_TYPE:
                awayDefenders++;
                break;
            case PlayerUtilities.MIDFIELDER_PLAYER_TYPE:
                awayMidfielders++;
                break;
            case PlayerUtilities.ATTACKER_PLAYER_TYPE:
                awayStrikers++;
                break;
            default:
                break;


        }
    }

    private void updateHomeCounters(Player homePlayer) {
        switch(homePlayer.getPosition()){
            case PlayerUtilities.DEFENDER_PLAYER_TYPE:
                homeDefenders++;
                break;
            case PlayerUtilities.MIDFIELDER_PLAYER_TYPE:
                homeMidfielders++;
                break;
            case PlayerUtilities.ATTACKER_PLAYER_TYPE:
                homeStrikers++;
                break;
            default:
                break;


        }
    }
}