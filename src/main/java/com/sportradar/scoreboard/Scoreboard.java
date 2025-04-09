package com.sportradar.scoreboard;

import com.sportradar.scoreboard.model.Match;

public interface Scoreboard {
    Match startNewMatch(String homeTeam, String awayTeam);
    void updateScore(String matchId, int homeScore, int awayScore);
    void finishMatch(String matchId);
}
