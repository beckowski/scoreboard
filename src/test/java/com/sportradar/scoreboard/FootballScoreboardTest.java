package com.sportradar.scoreboard;

import com.sportradar.scoreboard.exception.MatchAlreadyExistsException;
import com.sportradar.scoreboard.exception.MatchNotFoundException;
import com.sportradar.scoreboard.impl.FootballScoreboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FootballScoreboardTest {

    private static final String HOME_TEAM = "Mexico";
    private static final String AWAY_TEAM = "Canada";

    private FootballScoreboard scoreboard;

    @BeforeEach
    public void setUp() {
        scoreboard = new FootballScoreboard();
    }

    @Test
    void shouldStartNewMatchWhenTeamsAreAvailable() {
        var match = scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        assertNotNull(match);
        assertEquals(HOME_TEAM, match.getHomeTeam());
        assertEquals(AWAY_TEAM, match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
        assertEquals(0, match.getTotalScore());
    }

    @Test
    void shouldThrowWhenStaringNewMatchWithTeamAlreadyInMatch() {
        scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        assertThrows(MatchAlreadyExistsException.class,
                () -> scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM));
    }

    @Test
    void shouldThrowWhenStartNewMatchWithInvalidTeam() {
        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.startNewMatch(null, AWAY_TEAM));

        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.startNewMatch(HOME_TEAM, null));

        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.startNewMatch("", AWAY_TEAM));

        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.startNewMatch(HOME_TEAM, ""));

        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.startNewMatch(HOME_TEAM, HOME_TEAM));
    }

    @Test
    void shouldUpdateScoreForOngoingMatch() {
        var match = scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        scoreboard.updateScore(match.getId(), 0, 5);

        assertEquals(0, match.getHomeScore());
        assertEquals(5, match.getAwayScore());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentMatch() {
        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.updateScore("nonExisting", 0, 5));
    }

    @Test
    void shouldThrowWhenUpdatingMatchWithNegativeScore() {
        var match = scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.updateScore(match.getId(), 0, -5));
    }

    @Test
    void shouldStartNewMatchWhenSameMatchIsFinished() {
        var match = scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        scoreboard.finishMatch(match.getId());

        assertDoesNotThrow(() -> scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM));
    }

    @Test
    public void shouldReturnLiveMatchesSummarySortedByScoreAndInsertOrder() {
        // Start the matches
        var match1 = scoreboard.startNewMatch("Mexico", "Canada");
        var match2 = scoreboard.startNewMatch("Spain", "Brazil");
        var match3 = scoreboard.startNewMatch("Germany", "France");
        var match4 = scoreboard.startNewMatch("Uruguay", "Italy");
        var match5 = scoreboard.startNewMatch("Argentina", "Australia");

        // Update scores
        scoreboard.updateScore(match1.getId(), 0, 5);
        scoreboard.updateScore(match2.getId(), 10, 2);
        scoreboard.updateScore(match3.getId(), 2, 2);
        scoreboard.updateScore(match4.getId(), 6, 6);
        scoreboard.updateScore(match5.getId(), 3, 1);

        // Get the live matches summary
        var summary = scoreboard.getLiveMatchesSummary();

        // Verify the summary order
        assertEquals(summary.get(0), match4.toString());
        assertEquals(summary.get(1), match2.toString());
        assertEquals(summary.get(2), match1.toString());
        assertEquals(summary.get(3), match5.toString());
        assertEquals(summary.get(4), match3.toString());
    }

}
