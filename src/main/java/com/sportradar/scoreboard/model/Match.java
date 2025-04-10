package com.sportradar.scoreboard.model;

import java.util.UUID;

public record Match(String id, String homeTeam, String awayTeam, int homeScore, int awayScore) {

    public Match {
        if (homeTeam == null || homeTeam.isBlank() || awayTeam == null || awayTeam.isBlank()) {
            throw new IllegalArgumentException("Teams must not be null or blank.");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Teams must be different.");
        }
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores must be non-negative.");
        }
    }

    public Match(String homeTeam, String awayTeam) {
        this(UUID.randomUUID().toString(), homeTeam, awayTeam, 0, 0);
    }

    public Match withScore(int homeScore, int awayScore) {
        return new Match(id, homeTeam, awayTeam, homeScore, awayScore);
    }

    public int totalScore() {
        return homeScore + awayScore;
    }

    @Override
    public String toString() {
        return homeTeam + " " + homeScore + " - " + awayScore + " " + awayTeam;
    }

}