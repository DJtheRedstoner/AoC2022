package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.List;

public class Day2 implements DayBase {

    public void init(List<String> lines) {

    }

    public void part1(List<String> lines) {
        int score = 0;

        for (String line : lines) {
            String[] parts = line.split(" ");
            RPS left = RPS.fromLeft(parts[0]);
            RPS right = RPS.fromRight(parts[1]);

            score += right.score;
            if (left == right) {
                score += 3;
            } else if (right.beats.equals(left.toString())) {
                score += 6;
            }
        }

        System.out.println(score);
    }

    public void part2(List<String> lines) {
        int score = 0;

        for (String line : lines) {
            String[] parts = line.split(" ");
            RPS left = RPS.fromLeft(parts[0]);

            String right = parts[1];
            switch (right) {
                case "X" -> score += RPS.valueOf(left.beats).score;
                case "Y" -> {
                    score += 3;
                    score += left.score;
                }
                case "Z" -> {
                    score += 6;
                    score += left.beatenBy().score;
                }
            }
        }

        System.out.println(score);
    }

    enum RPS {
        ROCK("A", "X", "SCISSORS", 1),
        PAPER("B", "Y", "ROCK", 2),
        SCISSORS("C", "Z", "PAPER", 3);

        String left, right, beats;
        int score;

        RPS(String left, String right, String beats, int score) {
            this.left = left; this.right = right;
            this.beats = beats;
            this.score = score;
        }

        RPS beatenBy() {
            for (RPS rps : values()) {
                if (rps.beats.equals(this.toString())) {
                    return rps;
                }
            }
            throw new IllegalStateException();
        }

        static RPS fromLeft(String left) {
            for (RPS rps : values()) {
                if (rps.left.equals(left)) {
                    return rps;
                }
            }
            throw new IllegalArgumentException();
        }

        static RPS fromRight(String right) {
            for (RPS rps : values()) {
                if (rps.right.equals(right)) {
                    return rps;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public static void main(String...args) {
        new Day2().run();
    }
}
