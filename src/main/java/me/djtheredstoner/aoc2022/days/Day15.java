package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15 implements DayBase {

    List<Sensor> sensors = new ArrayList<>();

    public void init(List<String> lines) {
        Pattern regex = Pattern.compile("Sensor at x=(.+), y=(.+): closest beacon is at x=(.+), y=(.+)");

        for (String line : lines) {
            var matcher = regex.matcher(line);
            if (!matcher.find()) throw new IllegalStateException(line);
            sensors.add(new Sensor(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4))
            ));
        }

        //System.out.println(sensors);
    }

    record Sensor(int x, int y, int closestX, int closestY) {}

    record Pos(int x, int y) {}

    public void part1(List<String> lines) {
        int line = 2000000;//10;

        Set<Pos> taken = new HashSet<>();

        for (Sensor sensor : sensors) {
            int range = Math.abs(sensor.x() - sensor.closestX()) + Math.abs(sensor.y() - sensor.closestY());
            int dy = Math.abs(sensor.y() - line);

            if (range - dy < 1) continue;

            int startX = sensor.x() - (range - dy);
            int endX = sensor.x() + (range - dy);

            for (int i = startX; i < endX; i++) {
                taken.add(new Pos(i, line));
            }
        }

        System.out.println(taken.size());
    }

    public void part2(List<String> lines) {
        int max = 4000000;

        for (Sensor sensor : sensors) {
            int range = Math.abs(sensor.x() - sensor.closestX() ) + Math.abs(sensor.y() - sensor.closestY());

            for (int x = Math.max(0, sensor.x() - range - 1); x <= Math.min(max, sensor.x() + range + 1); x++) {
                int offset = range + 1 - Math.abs(x - sensor.x());
                for (int y : new int[]{sensor.y() + offset, sensor.y() - offset}) {
                    if (y > 0 && y < max) {
                        boolean found = true;
                        for (Sensor sensor1 : sensors) {
                            int range1 = Math.abs(sensor1.x() - sensor1.closestX()) + Math.abs(sensor1.y() - sensor1.closestY());
                            int d = Math.abs(x - sensor1.x()) + Math.abs(y - sensor1.y());
                            if (d <= range1) {
                                found = false;
                                break;
                            }
                        }
                        if (found) {
                            System.out.println((long)x * max + y);
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void main(String...args) {
        new Day15().run();
    }
}
