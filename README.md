# Live Football World Cup Scoreboard

This library provides functionality to manage and display ongoing football matches in a World Cup scoreboard. It allows you to start new matches, update match scores, finish ongoing matches, and retrieve a summary of current matches.

## Features

- **Start a new match**: Add a new match with home and away teams, initialized with a score of 0 – 0. If a match with the same teams already exists, a `MatchAlreadyExistsException` will be thrown.
- **Update match score**: Update the score of an ongoing match providing home and away team scores. If the match does not exist, a `MatchNotFoundException` will be thrown. Providing a negative score will throw an `IllegalArgumentException`.
- **Finish a match**: Remove a match from the scoreboard once it's completed.
- **Get match summary**: Retrieve a list of ongoing matches ordered by their total score. If two matches have the same score, they will be ordered by their start time, with the most recent match appearing first.

## Installation

To include the Live Football World Cup Scoreboard library in your project, add it as a dependency:

### Maven

```xml
<dependency>
    <groupId>com.sportradar.scoreboard</groupId>
    <artifactId>scoreboard</artifactId>
    <version>1.0.0</version>
</dependency>