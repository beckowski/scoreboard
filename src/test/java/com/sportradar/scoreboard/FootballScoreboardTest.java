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
        assertEquals(HOME_TEAM, match.homeTeam());
        assertEquals(AWAY_TEAM, match.awayTeam());
        assertEquals(0, match.homeScore());
        assertEquals(0, match.awayScore());
        assertEquals(0, match.totalScore());
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

        var updatedMatch = scoreboard.updateScore(match.id(), 0, 5);

        assertEquals(0, updatedMatch.homeScore());
        assertEquals(5, updatedMatch.awayScore());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingMatch() {
        assertThrows(MatchNotFoundException.class,
                () -> scoreboard.updateScore("wrongId", 0, 5));
    }

    @Test
    void shouldThrowWhenUpdatingMatchWithNegativeScore() {
        var match = scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        assertThrows(IllegalArgumentException.class,
                () -> scoreboard.updateScore(match.id(), 0, -5));
    }

    @Test
    void shouldStartNewMatchWhenSameMatchIsFinished() {
        var match = scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        scoreboard.finishMatch(match.id());

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
        var updatedMatch1 = scoreboard.updateScore(match1.id(), 0, 5);
        var updatedMatch2 = scoreboard.updateScore(match2.id(), 10, 2);
        var updatedMatch3 = scoreboard.updateScore(match3.id(), 2, 2);
        var updatedMatch4 = scoreboard.updateScore(match4.id(), 6, 6);
        var updatedMatch5 = scoreboard.updateScore(match5.id(), 3, 1);

        // Get the live matches summary
        var summary = scoreboard.getLiveMatchesSummary();

        // Verify the summary order
        assertEquals(updatedMatch4.toString(), summary.get(0));
        assertEquals(updatedMatch2.toString(), summary.get(1));
        assertEquals(updatedMatch1.toString(), summary.get(2));
        assertEquals(updatedMatch5.toString(), summary.get(3));
        assertEquals(updatedMatch3.toString(), summary.get(4));
    }

}
