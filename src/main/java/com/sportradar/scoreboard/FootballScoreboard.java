package com.sportradar.scoreboard;

import com.sportradar.scoreboard.exception.MatchAlreadyExistsException;
import com.sportradar.scoreboard.exception.MatchNotFoundException;
import com.sportradar.scoreboard.model.Match;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class FootballScoreboard implements Scoreboard {

    private final LinkedHashMap<String, Match> liveMatches;

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

    @Override
    public List<String> getLiveMatchesSummary() {
        var liveMatchesList = new ArrayList<>(liveMatches.entrySet());
        sortMatchesList(liveMatchesList);

        return liveMatchesList.stream()
                .map(entry -> entry.getValue().toString())
                .collect(Collectors.toList());
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

    private void sortMatchesList(List<Map.Entry<String, Match>> matchesList) {
        matchesList.sort((entry1, entry2) -> {
            int totalScore1 = entry1.getValue().getTotalScore();
            int totalScore2 = entry2.getValue().getTotalScore();
            if (totalScore1 != totalScore2) {
                return Integer.compare(totalScore2, totalScore1);
            }

            return Integer.compare(matchesList.indexOf(entry2), matchesList.indexOf(entry1));
        });
    }

}
