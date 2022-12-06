package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class Day6 implements DayBase {

    public void init(List<String> lines) {

    }

    public void part1(List<String> lines) {
        LinkedList<Character> previous = new LinkedList<>();

        int i = 0;

        for (char c : lines.get(0).toCharArray()) {
            previous.add(c);
            i++;

            if (new HashSet<>(previous).size() == previous.size() && previous.size() == 4) {
                System.out.println(i);
                return;
            }

            if (previous.size() == 4) {
                previous.removeFirst();
            }
        }
    }

    public void part2(List<String> lines) {
        LinkedList<Character> previous = new LinkedList<>();

        int i = 0;

        for (char c : lines.get(0).toCharArray()) {
            previous.add(c);
            i++;

            if (new HashSet<>(previous).size() == previous.size() && previous.size() == 14) {
                System.out.println(i);
                return;
            }

            if (previous.size() == 14) {
                previous.removeFirst();
            }
        }
    }

    public static void main(String...args) {
        new Day6().run();
    }
}
