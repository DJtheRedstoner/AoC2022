package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Day12 implements DayBase {

    int[][] levels;
    Point start;
    Point end;

    public void init(List<String> lines) {
        levels = new int[lines.size()][];

        int y = 0;
        for (String line : lines) {
            levels[y] = new int[line.length()];
            int x = 0;
            for (char c : line.toCharArray()) {
                if (c == 'S') {
                    start = new Point(x, y);
                    levels[y][x] = 0;
                } else if (c == 'E') {
                    end = new Point(x, y);
                    levels[y][x] = 25;
                } else {
                    levels[y][x] = c - 'a';
                }
                x++;
            }
            y++;
        }
    }

    record Point(int x, int y) {}

    record Node(Point point, int dist) {}

    public void part1(List<String> lines) {
        HashSet<Point> seen = new HashSet<>();

        LinkedList<Node> queue = new LinkedList<>();

        queue.add(new Node(start, 0));

        while (!queue.isEmpty()) {
            var node = queue.removeFirst();
            var point = node.point();
            if (point.equals(end)) {
                System.out.println(node.dist());
                return;
            }

            for (Point d : List.of(new Point(0, 1), new Point(0, -1), new Point(1, 0), new Point(-1, 0))) {
                Point next = new Point(point.x() + d.x(), point.y() + d.y());
                if (next.x() < 0 || next.x() >= levels[0].length || next.y() < 0 || next.y() >= levels.length) continue;

                if (levels[next.y()][next.x()] - levels[point.y()][point.x()] > 1) continue;
                if (seen.contains(next)) continue;

                seen.add(next);
                queue.add(new Node(next, node.dist() + 1));
            }
        }
    }

    public void part2(List<String> lines) {
        HashSet<Point> seen = new HashSet<>();

        LinkedList<Node> queue = new LinkedList<>();

        for (int y = 0; y < levels.length; y++) {
            for (int x = 0; x < levels[y].length; x++) {
                if (levels[y][x] == 0) {
                    queue.add(new Node(new Point(x, y), 0));
                }
            }
        }

        queue.add(new Node(start, 0));

        while (!queue.isEmpty()) {
            var node = queue.removeFirst();
            var point = node.point();
            if (point.equals(end)) {
                System.out.println(node.dist());
                return;
            }

            for (Point d : List.of(new Point(0, 1), new Point(0, -1), new Point(1, 0), new Point(-1, 0))) {
                Point next = new Point(point.x() + d.x(), point.y() + d.y());
                if (next.x() < 0 || next.x() >= levels[0].length || next.y() < 0 || next.y() >= levels.length) continue;

                if (levels[next.y()][next.x()] - levels[point.y()][point.x()] > 1) continue;
                if (seen.contains(next)) continue;

                seen.add(next);
                queue.add(new Node(next, node.dist() + 1));
            }
        }
    }

    public static void main(String...args) {
        new Day12().run();
    }
}
