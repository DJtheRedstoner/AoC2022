package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day17 implements DayBase {

    List<Jet> jets = new ArrayList<>();

    boolean[][][] shapes = new boolean[][][] {
        { {true, true, true, true} },
        { {false, true, false}, {true, true, true}, {false, true, false} },
        { {false, false, true}, {false, false, true}, {true, true, true} },
        { {true}, {true}, {true}, {true} },
        { {true, true}, {true, true} }
    };

    public void init(List<String> lines) {
        for (char c : lines.get(0).toCharArray()) {
            if (c == '<') {
                jets.add(Jet.LEFT);
            } else if (c == '>') {
                jets.add(Jet.RIGHT);
            }
        }
    }

    enum Jet {
        LEFT,
        RIGHT
    }

    public void part1(List<String> lines) {
        int height = 0;

        int ins = 0;

        Map<Integer, boolean[]> layers = new HashMap<>();

        for (int rock = 0; rock < 2022; rock++) {
            boolean[][] shape = shapes[rock % shapes.length];

            int x = 2;
            int y = height + 3;

            while (true) {
                Jet jet = jets.get(ins++ % jets.size());
                if (jet == Jet.LEFT && x > 0) {
                    if (checkMove(shape, x - 1, y, layers)) {
                        x--;
                    }
                } else if (jet == Jet.RIGHT && x < 7 - shape[0].length) {
                    if (checkMove(shape, x + 1, y, layers)) {
                        x++;
                    }
                }
                if (checkMove(shape, x, y - 1, layers) && y > 0) {
                    y--;
                } else {
                    for (int sY = 0; sY < shape.length; sY++) {
                        int actualY = y + (shape.length - sY);
                        boolean[] shapeLayer = shape[sY];
                        boolean[] layer = layers.computeIfAbsent(actualY, __ -> new boolean[7]);
                        for (int sX = 0; sX < shapeLayer.length; sX++) {
                            layer[x + sX] |= shapeLayer[sX];
                        }
                    }
                    break;
                }
            }

            //for (int i : layers.keySet().stream().mapToInt(Integer::intValue).sorted().toArray()) {
            //    System.out.println(Arrays.toString(layers.get(i)));
            //}
            //
            //System.out.println();

            height = layers.keySet().stream().mapToInt(Integer::intValue).max().getAsInt();
        }

        System.out.println(height);
    }

    private boolean checkMove(boolean[][] shape, int x, int y, Map<Integer, boolean[]> layers) {
        for (int sY = 0; sY < shape.length; sY++) {
            int actualY = y + (shape.length - sY);
            if (!layers.containsKey(actualY)) continue;
            boolean[] shapeLayer = shape[sY];
            boolean[] layer = layers.get(actualY);
            for (int sX = 0; sX < shapeLayer.length; sX++) {
                if (layer[x + sX] && shapeLayer[sX]) return false;
            }
        }

        return true;
    }

    public void part2(List<String> lines) {
        int height = 0;

        int ins = 0;

        Map<Integer, boolean[]> layers = new HashMap<>();

        Map<SeenKey, SeenValue> seen = new HashMap<>();

        long extraHeight = 0;

        for (long rock = 0; rock < 1000000000000L; rock++) {
            boolean[][] shape = shapes[(int)(rock % shapes.length)];

            int x = 2;
            int y = height + 3;

            while (true) {
                Jet jet = jets.get(ins++ % jets.size());
                if (jet == Jet.LEFT && x > 0) {
                    if (checkMove(shape, x - 1, y, layers)) {
                        x--;
                    }
                } else if (jet == Jet.RIGHT && x < 7 - shape[0].length) {
                    if (checkMove(shape, x + 1, y, layers)) {
                        x++;
                    }
                }
                if (checkMove(shape, x, y - 1, layers) && y > 0) {
                    y--;
                } else {
                    for (int sY = 0; sY < shape.length; sY++) {
                        int actualY = y + (shape.length - sY);
                        boolean[] shapeLayer = shape[sY];
                        boolean[] layer = layers.computeIfAbsent(actualY, __ -> new boolean[7]);
                        for (int sX = 0; sX < shapeLayer.length; sX++) {
                            layer[x + sX] |= shapeLayer[sX];
                        }
                    }
                    break;
                }
            }

            height = layers.keySet().stream().mapToInt(Integer::intValue).max().getAsInt();

            var h = layers.keySet().stream().mapToInt(Integer::intValue).sorted().toArray();

            int[] view = new int[7];
            Arrays.fill(view, -1);
            for (int i = 0; i < 7; i++) {
                for (int j = h.length - 1; j >= 0; j--) {
                    if (layers.get(h[j])[i]) {
                        view[i] = (height - h[j]);
                        break;
                    }
                }
            }

            SeenKey key = new SeenKey((ins - 1) % jets.size(), (int)(rock % shapes.length), Arrays.stream(view).boxed().toList());
            if (seen.containsKey(key)) {
                SeenValue old = seen.get(key);
                long repeat = (1000000000000L - rock) / (rock - old.rock());
                rock += (rock - old.rock()) * repeat;
                extraHeight += repeat * (height - old.height());
            }

            seen.put(key, new SeenValue(rock, height));
        }

        System.out.println(height + extraHeight);
    }

    record SeenKey(int ins, int shape, List<Integer> view) {}
    record SeenValue(long rock, int height) {}

    public static void main(String...args) {
        new Day17().run();
    }
}
