package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.List;

public class Day4 implements DayBase {

    public void init(List<String> lines) {

    }

    public void part1(List<String> lines) {
        int i = 0;

        for (String line : lines) {
            String[] pairs = line.split(",");
            String[] left = pairs[0].split("-");
            String[] right = pairs[1].split("-");
            int x0 = Integer.parseInt(left[0], 10);
            int y0 = Integer.parseInt(left[1], 10);
            int x1 = Integer.parseInt(right[0], 10);
            int y1 = Integer.parseInt(right[1], 10);

            if (y1 - x1 < y0 - x0) {
                int xp = x1, yp = y1;
                x1 = x0;
                y1 = y0;
                x0 = xp;
                y0 = yp;
            }

            if (x0 >= x1 && y0<=y1) {
                i++;
            }
        }

        System.out.println(i);
    }

    public void part2(List<String> lines) {
        int i = 0;

        for (String line : lines) {
            String[] pairs = line.split(",");
            String[] left = pairs[0].split("-");
            String[] right = pairs[1].split("-");
            int x0 = Integer.parseInt(left[0], 10);
            int y0 = Integer.parseInt(left[1], 10);
            int x1 = Integer.parseInt(right[0], 10);
            int y1 = Integer.parseInt(right[1], 10);

            if (y1 - x1 < y0 - x0) {
                int xp = x1, yp = y1;
                x1 = x0;
                y1 = y0;
                x0 = xp;
                y0 = yp;
            }

            if (x0 >=x1 && x0 <= y1 || y0 >= x1 && y0<= y1) {
                i++;
            }
        }

        System.out.println(i);
    }

    public static void main(String...args) {
        new Day4().run();
    }
}
