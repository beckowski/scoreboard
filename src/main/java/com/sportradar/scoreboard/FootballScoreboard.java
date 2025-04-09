package com.sportradar.scoreboard;

import com.sportradar.scoreboard.exception.MatchAlreadyExistsException;
import com.sportradar.scoreboard.model.Match;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class FootballScoreboard implements Scoreboard {

    private final HashMap<String, Match> liveMatches;

    public FootballScoreboard() {
        this.liveMatches = new LinkedHashMap<>();
    }

    @Override
    public Match startNewMatch(String homeTeam, String awayTeam) {
        validateMatchTeams(homeTeam, awayTeam);
        var match = new Match(homeTeam, awayTeam);
        liveMatches.put(match.getId(), match);
        return match;
    }

    private void validateMatchTeams(String homeTeam, String awayTeam) {
        for (var match : liveMatches.values()) {
            var matchHomeTeam = match.getHomeTeam();
            var matchAwayTeam = match.getAwayTeam();

            if (matchHomeTeam.equals(homeTeam) || matchHomeTeam.equals(awayTeam) ||
                    matchAwayTeam.equals(homeTeam) || matchAwayTeam.equals(awayTeam)) {
                throw new MatchAlreadyExistsException("Match with given team(s) already exists.");
            }
        }
    }

}
