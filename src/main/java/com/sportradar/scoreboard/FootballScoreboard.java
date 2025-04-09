package com.sportradar.scoreboard;

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
        var match = new Match(homeTeam, awayTeam);
        liveMatches.put(match.getId(), match);
        return match;
    }
}
