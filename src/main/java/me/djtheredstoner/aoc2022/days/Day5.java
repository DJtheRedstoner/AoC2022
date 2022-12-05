package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 implements DayBase {

    private final Pattern MOVE_REGEX = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

    public void init(List<String> lines) {

    }

    private List<List<String>> getCrates() {
        List<List<String>> crates = new ArrayList<>();

        // parsing is for n00bs
        crates.add(new ArrayList<>(List.of("D", "L", "J", "R", "V", "G", "F")));
        crates.add(new ArrayList<>(List.of("T", "P", "M", "B", "V", "H", "J", "S")));
        crates.add(new ArrayList<>(List.of("V", "H", "M", "F", "D", "G", "P", "C")));
        crates.add(new ArrayList<>(List.of("M", "D", "P", "N", "G", "Q")));
        crates.add(new ArrayList<>(List.of("J", "L", "H", "N", "F")));
        crates.add(new ArrayList<>(List.of("N", "F", "V", "Q", "D", "G", "T", "Z")));
        crates.add(new ArrayList<>(List.of("F", "D", "B", "L")));
        crates.add(new ArrayList<>(List.of("M", "J", "B", "S", "V", "D", "N")));
        crates.add(new ArrayList<>(List.of("D", "L", "G")));

        return crates;
    }

    public void part1(List<String> lines) {
        var crates = getCrates();

        for (String line : lines) {
            Matcher m = MOVE_REGEX.matcher(line);
            if (!m.find()) {
                continue;
            }

            int count = Integer.parseInt(m.group(1));
            int from = Integer.parseInt(m.group(2));
            int to = Integer.parseInt(m.group(3));


            List<String> fromStack = crates.get(from - 1);
            List<String> toStack = crates.get(to - 1);

            List<String> grabbed = fromStack.subList(fromStack.size() - count, fromStack.size());
            Collections.reverse(grabbed);
            toStack.addAll(grabbed);

            crates.set(from - 1, fromStack.subList(0, fromStack.size() - count));
        }

        StringBuilder builder = new StringBuilder();

        for (List<String> crate : crates) {
            builder.append(crate.get(crate.size() - 1));
        }

        System.out.println(builder);
    }

    public void part2(List<String> lines) {
        var crates = getCrates();

        for (String line : lines) {
            Matcher m = MOVE_REGEX.matcher(line);
            if (!m.find()) {
                continue;
            }

            int count = Integer.parseInt(m.group(1));
            int from = Integer.parseInt(m.group(2));
            int to = Integer.parseInt(m.group(3));


            List<String> fromStack = crates.get(from - 1);
            List<String> toStack = crates.get(to - 1);

            List<String> grabbed = fromStack.subList(fromStack.size() - count, fromStack.size());
            toStack.addAll(grabbed);

            crates.set(from - 1, fromStack.subList(0, fromStack.size() - count));
        }

        StringBuilder builder = new StringBuilder();

        for (List<String> crate : crates) {
            builder.append(crate.get(crate.size() - 1));
        }

        System.out.println(builder);
    }

    public static void main(String...args) {
        new Day5().run();
    }
}
