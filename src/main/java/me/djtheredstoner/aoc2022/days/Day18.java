package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day18 implements DayBase {

    List<Cube> cubes = new ArrayList<>();

    public void init(List<String> lines) {
        for (String line : lines) {
            var parts = line.split(",");
            cubes.add(new Cube(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2])
            ));
        }
    }

    record Cube(int x, int y, int z) {}

    public void part1(List<String> lines) {
        int exposed = 0;

        for (Cube cube : cubes) {
            for (Cube dir : List.of(
                new Cube(-1, 0, 0),
                new Cube(1, 0, 0),
                new Cube(0, 1, 0),
                new Cube(0, -1, 0),
                new Cube(0, 0, 1),
                new Cube(0, 0, -1)
            )) {
                Cube newPos = new Cube(cube.x() + dir.x(), cube.y() + dir.y(), cube.z() + dir.z());
                if (!cubes.contains(newPos)) {
                    exposed++;
                }
            }
        }

        System.out.println(exposed);
    }

    public void part2(List<String> lines) {
        List<Cube> allCubes = new ArrayList<>();

        for (int x = 0; x < 25; x++) {
            for (int y = 0; y < 25; y++) {
                for (int z = 0; z < 25; z++) {
                    allCubes.add(new Cube(x, y, z));
                }
            }
        }

        List<Cube> emptyCubes = new ArrayList<>(allCubes);
        emptyCubes.removeAll(cubes);

        LinkedList<Cube> queue = new LinkedList<>();
        queue.add(new Cube(0, 0, 0));

        while (queue.size() > 0) {
            Cube c = queue.remove();
            if (emptyCubes.contains(c)) {
                emptyCubes.remove(c);
                for (Cube dir : List.of(
                    new Cube(-1, 0, 0),
                    new Cube(1, 0, 0),
                    new Cube(0, 1, 0),
                    new Cube(0, -1, 0),
                    new Cube(0, 0, 1),
                    new Cube(0, 0, -1)
                )) {
                    Cube newPos = new Cube(c.x() + dir.x(), c.y() + dir.y(), c.z() + dir.z());
                    queue.add(newPos);
                }
            }
        }

        int exposed = 0;

        for (Cube cube : cubes) {
            for (Cube dir : List.of(
                new Cube(-1, 0, 0),
                new Cube(1, 0, 0),
                new Cube(0, 1, 0),
                new Cube(0, -1, 0),
                new Cube(0, 0, 1),
                new Cube(0, 0, -1)
            )) {
                Cube newPos = new Cube(cube.x() + dir.x(), cube.y() + dir.y(), cube.z() + dir.z());
                if (!cubes.contains(newPos) && !emptyCubes.contains(newPos)) {
                    exposed++;
                }
            }
        }

        System.out.println(exposed);
    }

    public static void main(String...args) {
        new Day18().run();
    }
}
