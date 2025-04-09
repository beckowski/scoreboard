package com.sportradar.scoreboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreboardTest {

    private static final String HOME_TEAM = "Mexico";
    private static final String AWAY_TEAM = "Canada";

    private FootballScoreboard scoreboard;

    @Test
    void startNewMatch() {
        scoreboard = new FootballScoreboard();
        var match = scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        assertNotNull(match);
        assertEquals(HOME_TEAM, match.getHomeTeam());
        assertEquals(AWAY_TEAM, match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
        assertEquals(0, match.getTotalScore());
    }

    @Test
    void cannotStartNewMatchWithTeamAlreadyInMatch() {
        scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        assertThrows(MatchAlreadyExistsException.class, () -> scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM));
    }

}
