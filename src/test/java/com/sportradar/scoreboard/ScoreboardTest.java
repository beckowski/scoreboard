package com.sportradar.scoreboard;

import com.sportradar.scoreboard.exception.MatchAlreadyExistsException;
import com.sportradar.scoreboard.exception.MatchNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreboardTest {

    private static final String HOME_TEAM = "Mexico";
    private static final String AWAY_TEAM = "Canada";

    private FootballScoreboard scoreboard;

    @BeforeEach
    public void setUp() {
        scoreboard = new FootballScoreboard();
    }

    @Test
    void startNewMatch() {
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

    @Test
    void updateMatchScore() {
        var match = scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        scoreboard.updateScore(match.getId(), 0, 5);

        assertEquals(0, match.getHomeScore());
        assertEquals(5, match.getAwayScore());
    }

    @Test
    void updateNonExistingMatchScore() {
        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.updateScore("nonExisting", 0, 5));
    }

    @Test
    void updateMatchWithNegativeScore() {
        var match = scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.updateScore(match.getId(), 0, -5));
    }

    @Test
    void testFinishMatch() {
        var match = scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        scoreboard.finishMatch(match.getId());

        assertDoesNotThrow(() -> scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM));
    }


}
