package com.cristiandrami.football365.model.utilities.matches_utilities;

import com.cristiandrami.football365.model.detailed_match.comment.CommentItem;
import com.cristiandrami.football365.model.detailed_match.line_up.Player;

import java.util.List;
import java.util.Objects;

public class Match {

    private String homeTeam;
    private String awayTeam;
    private String status;
    private String date;
    private String startTime;
    private String competitionId;
    private String halfTimeHomeTeamScore;
    private String halfTimeAwayTeamScore;
    private String fullTimeHomeTeamScore;
    private String fullTimeAwayTeamScore;
    private String matchId;



    private List<Player> homePlayers;
    private List<Player> awayPlayers;

    private List<CommentItem> commentItemList;



    public Match(){
        /**this is an empty constructor*/
    }


    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getHalfTimeHomeTeamScore() {
        return halfTimeHomeTeamScore;
    }

    public void setHalfTimeHomeTeamScore(String halfTimeHomeTeamScore) {
        this.halfTimeHomeTeamScore = halfTimeHomeTeamScore;
    }

    public String getHalfTimeAwayTeamScore() {
        return halfTimeAwayTeamScore;
    }

    public void setHalfTimeAwayTeamScore(String halfTimeAwayTeamScore) {
        this.halfTimeAwayTeamScore = halfTimeAwayTeamScore;
    }

    public String getFullTimeHomeTeamScore() {
        return fullTimeHomeTeamScore;
    }

    public void setFullTimeHomeTeamScore(String fullTimeHomeTeamScore) {
        this.fullTimeHomeTeamScore = fullTimeHomeTeamScore;
    }

    public String getFullTimeAwayTeamScore() {
        return fullTimeAwayTeamScore;
    }

    public void setFullTimeAwayTeamScore(String fullTimeAwayTeamScore) {
        this.fullTimeAwayTeamScore = fullTimeAwayTeamScore;
    }
    public List<Player> getHomePlayers() {
        return homePlayers;
    }

    public void setHomePlayers(List<Player> homePlayers) {
        this.homePlayers = homePlayers;
    }

    public List<Player> getAwayPlayers() {
        return awayPlayers;
    }

    public void setAwayPlayers(List<Player> awayPlayers) {
        this.awayPlayers = awayPlayers;
    }

    public List<CommentItem> getCommentItemList() {
        return commentItemList;
    }

    public void setCommentItemList(List<CommentItem> commentItemList) {
        this.commentItemList = commentItemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;
        Match match = (Match) o;
        return Objects.equals(getHomeTeam(), match.getHomeTeam()) && Objects.equals(getAwayTeam(), match.getAwayTeam()) && Objects.equals(getStatus(), match.getStatus()) && Objects.equals(getDate(), match.getDate()) && Objects.equals(getStartTime(), match.getStartTime()) && Objects.equals(getCompetitionId(), match.getCompetitionId()) && Objects.equals(getHalfTimeHomeTeamScore(), match.getHalfTimeHomeTeamScore()) && Objects.equals(getHalfTimeAwayTeamScore(), match.getHalfTimeAwayTeamScore()) && Objects.equals(getFullTimeHomeTeamScore(), match.getFullTimeHomeTeamScore()) && Objects.equals(getFullTimeAwayTeamScore(), match.getFullTimeAwayTeamScore()) && Objects.equals(getMatchId(), match.getMatchId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHomeTeam(), getAwayTeam(), getStatus(), getDate(), getStartTime(), getCompetitionId(), getHalfTimeHomeTeamScore(), getHalfTimeAwayTeamScore(), getFullTimeHomeTeamScore(), getFullTimeAwayTeamScore(), getMatchId());
    }
}
