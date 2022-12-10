package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.List;

public class Day10 implements DayBase {

    int x = 1;
    int power;
    int cycle;

    boolean[] row = new boolean[40];

    public void init(List<String> lines) {

    }

    public void part1(List<String> lines) {
        for (String line : lines) {
            if (line.equals("noop")) {
                cycle();
            } else if (line.startsWith("addx ")) {
                cycle();
                cycle();
                x += Integer.parseInt(line.substring("addx ".length()));
            }
        }

        System.out.println(power);
    }

    private void cycle() {
        cycle++;
        if ((cycle + 20) % 40 == 0) {
            power += x * cycle;
        }
    }

    public void part2(List<String> lines) {
        cycle = 0;
        x = 1;

        for (String line : lines) {
            if (line.equals("noop")) {
                cycle2();
            } else if (line.startsWith("addx ")) {
                cycle2();
                cycle2();
                x += Integer.parseInt(line.substring("addx ".length()));
            }
        }
    }

    private void cycle2() {
        cycle++;

        int delta = (cycle - 1) % 40;
        if (x >= delta - 1 && x <= delta + 1) {
            row[delta] = true;
        } else {
            row[delta] = false;
        }

        if (delta == 39) {
            StringBuilder sb = new StringBuilder();
            for (boolean b : row) {
                if (b) sb.append('#'); else sb.append('.');
            }
            System.out.println(sb);
        }
    }

    public static void main(String...args) {
        new Day10().run();
    }
}
