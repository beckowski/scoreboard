package com.sportradar.scoreboard.model;

import java.util.UUID;

public record Match(String id, String homeTeam, String awayTeam, int homeScore, int awayScore) {

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