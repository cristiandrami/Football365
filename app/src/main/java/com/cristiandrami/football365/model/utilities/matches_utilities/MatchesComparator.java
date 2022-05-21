package com.cristiandrami.football365.model.utilities.matches_utilities;

import java.util.Comparator;

public class MatchesComparator implements Comparator<Match> {
    @Override
    public int compare(Match match, Match match2) {
       String competitionAreaMatch=CompetitionsUtilities.getInstance().getCompetitions().get(match.getCompetitionId()).getGeographicalArea();
       String competitionAreaMatch2=CompetitionsUtilities.getInstance().getCompetitions().get(match2.getCompetitionId()).getGeographicalArea();

       return competitionAreaMatch.compareTo(competitionAreaMatch2);
    }
}
