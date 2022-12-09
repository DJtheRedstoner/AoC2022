package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day9 implements DayBase {

    private List<Instruction> instructions = new ArrayList<>();

    public void init(List<String> lines) {
        for (String line : lines) {
            var parts = line.split(" ");
            instructions.add(new Instruction(Direction.fromString(parts[0]), Integer.parseInt(parts[1])));
        }

        System.out.println(instructions);
    }

    public void part1(List<String> lines) {
        var visited = new HashSet<Pair>();

        int headX, headY, tailX, tailY;
        headX = headY = tailX = tailY = 0;

        visited.add(new Pair(tailX, tailY));

        for (Instruction instruction : instructions) {
            for (int i = 0; i < instruction.distance; i++) {
                switch (instruction.direction) {
                    case UP -> headY++;
                    case DOWN -> headY--;
                    case LEFT -> headX--;
                    case RIGHT -> headX++;
                }

                //int dX = Math.abs(headX - tailX);
                //int dY = Math.abs(headY - tailY);

                //if (dX > 1 || dY > 1 || dX * dY > 1) {
                    Pair newTail = moveTail(new Pair(headX, headY), new Pair(tailX, tailY));
                    visited.add(newTail);
                    tailX = newTail.x();
                    tailY = newTail.y();
                //}
            }
        }

        System.out.println(visited.size());
    }

    private Pair moveTail(Pair head, Pair tail) {
        int dX = Math.abs(head.x() - tail.x());
        int dY = Math.abs(head.y() - tail.y());

        if (dX > 1 || dY > 1 || dX * dY > 1) {
            if (head.x() > tail.x() && head.y() == tail.y()) {
                return new Pair(tail.x() + 1, tail.y());
            } else if (head.x() < tail.x() && head.y() == tail.y()) {
                return new Pair(tail.x() - 1, tail.y());
            } else if (head.x() == tail.x() && head.y() > tail.y()) {
                return new Pair(tail.x(), tail.y() + 1);
            } else if (head.x() == tail.x() && head.y() < tail.y()) {
                return new Pair(tail.x(), tail.y() - 1);
            } else if (head.x() > tail.x() && head.y() > tail.y()) {
                return new Pair(tail.x() + 1, tail.y() + 1);
            } else if (head.x() < tail.x() && head.y() < tail.y()) {
                return new Pair(tail.x() - 1, tail.y() - 1);
            } else if (head.x() < tail.x() && head.y() > tail.y()) {
                return new Pair(tail.x() - 1, tail.y() + 1);
            } else if (head.x() > tail.x() && head.y() < tail.y()) {
                return new Pair(tail.x() + 1, tail.y() - 1);
            } else throw new IllegalStateException();
        } else {
            return tail;
        }
    }

    public void part2(List<String> lines) {
        var visited = new HashSet<Pair>();

        Pair head = new Pair(0, 0);
        List<Pair> tails = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            tails.add(new Pair(0, 0));
        }

        for (Instruction instruction : instructions) {
            for (int i = 0; i < instruction.distance; i++) {
                head = switch (instruction.direction) {
                    case UP -> new Pair(head.x(), head.y() + 1);
                    case DOWN -> new Pair(head.x(), head.y() - 1);
                    case LEFT -> new Pair(head.x() - 1, head.y());
                    case RIGHT -> new Pair(head.x() + 1, head.y());
                };

                for (int j = 0; j < tails.size(); j++) {
                    Pair prev = j == 0 ? head : tails.get(j - 1);
                    tails.set(j, moveTail(prev, tails.get(j)));
                    if (j == tails.size() - 1) {
                        visited.add(tails.get(j));
                    }
                }
            }
        }

        System.out.println(visited.size());
    }

    record Pair(int x, int y) {}

    record Instruction(Direction direction, int distance) {}

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;

        static Direction fromString(String s) {
            return switch (s) {
                case "U" -> UP;
                case "D" -> DOWN;
                case "L" -> LEFT;
                case "R" -> RIGHT;
                default -> throw new IllegalStateException();
            };
        }
    }

    public static void main(String...args) {
        new Day9().run();
    }
}
