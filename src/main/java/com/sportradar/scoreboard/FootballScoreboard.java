package com.sportradar.scoreboard;

import com.sportradar.scoreboard.exception.MatchAlreadyExistsException;
import com.sportradar.scoreboard.exception.MatchNotFoundException;
import com.sportradar.scoreboard.model.Match;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static java.util.Objects.isNull;

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

    @Override
    public void updateScore(String matchId, int homeScore, int awayScore) {
        var match = liveMatches.get(matchId);

        if (isNull(match)) {
            throw new MatchNotFoundException("Match with given id not found.");
        }

        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Score cannot be negative.");
        }

        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        liveMatches.put(matchId, match);

    }

    @Override
    public void finishMatch(String matchId) {
        liveMatches.remove(matchId);
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
