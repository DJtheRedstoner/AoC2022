package me.djtheredstoner.aoc2022;

import java.util.List;

public interface DayBase {

    default void run() {
        int day = Integer.parseInt(getClass().getSimpleName().substring("Day".length()));
        List<String> lines = Util.getLines(day);

        init(lines);
        part1(lines);
        part2(lines);
    }

    void init(List<String> lines);

    void part1(List<String> lines);

    void part2(List<String> lines);

}
