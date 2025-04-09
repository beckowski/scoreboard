package com.sportradar.scoreboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ScoreboardTest {

    private static final String HOME_TEAM = "Mexico";
    private static final String AWAY_TEAM = "Canada";

    private FootballScoreboard scoreboard;

    @Test
    void testStartNewMatch() {
        scoreboard = new FootballScoreboard();
        var match = scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        assertNotNull(match);
        assertEquals(HOME_TEAM, match.getHomeTeam());
        assertEquals(AWAY_TEAM, match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
        assertEquals(0, match.getTotalScore());
    }

}
