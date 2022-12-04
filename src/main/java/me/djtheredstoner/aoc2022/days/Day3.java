package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day3 implements DayBase {

    public void init(List<String> lines) {

    }

    public void part1(List<String> lines) {
        int prior = 0;

        for (String line : lines) {
            String left = line.substring(0, line.length() / 2);
            String right = line.substring(line.length() / 2);

            Set<Character> chars = new HashSet<>();

            for (char c : left.toCharArray()) {
                chars.add(c);
            }

            for (char c : right.toCharArray()) {
                if (chars.contains(c)) {
                    if (Character.isUpperCase(c)) prior += 26;
                    c = Character.toLowerCase(c);
                    prior += (int) c - 96;
                    break;
                }
            }
        }

        System.out.println(prior);
    }

    public void part2(List<String> lines) {
        int prior = 0;

        for (int i = 0; i < lines.size(); i += 3) {
            String a = lines.get(i);
            String b = lines.get(i + 1);
            String d = lines.get(i + 2);

            Set<Character> chars = new HashSet<>();

            for (char c : a.toCharArray()) {
                chars.add(c);
            }

            char h = chars.stream()
                .filter(c -> b.indexOf(c) > -1)
                .filter(c -> d.indexOf(c) > -1)
                .toList().get(0);

            if (Character.isUpperCase(h)) prior += 26;
            h = Character.toLowerCase(h);
            prior += (int) h - 96;
        }

        System.out.println(prior);
    }

    public static void main(String...args) {
        new Day3().run();
    }
}
