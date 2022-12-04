package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Day1 implements DayBase {

    PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.reverseOrder());

    public void init(List<String> lines) {
        int current = 0;

        for (String line : lines) {
            if (line.isEmpty()) {
                queue.add(current);
                current = 0;
                continue;
            }
            current += Integer.parseInt(line);
        }
    }

    public void part1(List<String> lines) {
        System.out.println(queue.peek());
    }

    public void part2(List<String> lines) {
        System.out.println(queue.remove() + queue.remove() + queue.remove());
    }

    public static void main(String...args) {
        new Day1().run();
    }
}
