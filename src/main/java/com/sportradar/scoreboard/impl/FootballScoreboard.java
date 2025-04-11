package com.sportradar.scoreboard.impl;

import com.sportradar.scoreboard.Scoreboard;
import com.sportradar.scoreboard.exception.MatchAlreadyExistsException;
import com.sportradar.scoreboard.exception.MatchNotFoundException;
import com.sportradar.scoreboard.model.Match;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class FootballScoreboard implements Scoreboard {

    private final Map<String, Match> liveMatches;
    private final Set<String> activeTeams;

    public FootballScoreboard() {
        this.liveMatches = new LinkedHashMap<>();
        this.activeTeams = new HashSet<>();
    }

    @Override
    public Match startNewMatch(String homeTeam, String awayTeam) {
        validateTeams(homeTeam, awayTeam);
        var match = new Match(homeTeam, awayTeam);
        storeMatch(match);
        return match;
    }

    @Override
    public Match updateScore(String matchId, int homeScore, int awayScore) {
        var match = liveMatches.get(matchId);

        if (isNull(match)) {
            throw new MatchNotFoundException("Match with given id not found.");
        }
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Score cannot be negative.");
        }

        var updatedMatch = match.withScore(homeScore, awayScore);
        liveMatches.put(matchId, updatedMatch);
        return updatedMatch;

    }

    @Override
    public void finishMatch(String matchId) {
        var match = liveMatches.remove(matchId);
        if (nonNull(match)) {
            activeTeams.remove(match.homeTeam());
            activeTeams.remove(match.awayTeam());
        }
    }

    @Override
    public List<String> getLiveMatchesSummary() {
        var liveMatchesList = new ArrayList<>(liveMatches.entrySet());
        sortMatchesList(liveMatchesList);

        return liveMatchesList.stream()
                .map(entry -> entry.getValue().toString())
                .collect(Collectors.toList());
    }

    private void validateTeams(String homeTeam, String awayTeam) {
        if (homeTeam == null || homeTeam.isBlank() || awayTeam == null || awayTeam.isBlank()) {
            throw new IllegalArgumentException("Team(s) must not be null or blank.");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Teams must be different.");
        }
        if (activeTeams.contains(homeTeam) || activeTeams.contains(awayTeam)) {
            throw new MatchAlreadyExistsException("Match with given team(s) already exists.");
        }
    }

    private void storeMatch(Match match) {
        liveMatches.put(match.id(), match);
        activeTeams.add(match.homeTeam());
        activeTeams.add(match.awayTeam());
    }

    private void sortMatchesList(List<Map.Entry<String, Match>> matchesList) {
        matchesList.sort((entry1, entry2) -> {
            int totalScore1 = entry1.getValue().totalScore();
            int totalScore2 = entry2.getValue().totalScore();
            if (totalScore1 != totalScore2) {
                return Integer.compare(totalScore2, totalScore1);
            }

            return Integer.compare(matchesList.indexOf(entry2), matchesList.indexOf(entry1));
        });
    }

}
