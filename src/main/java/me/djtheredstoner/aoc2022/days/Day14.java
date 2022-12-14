package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Day14 implements DayBase {

    Map<Pos, State> initialStates = new HashMap<>();

    public void init(List<String> lines) {
        for (String line : lines) {
            List<Pos> l = new ArrayList<>();
            for (String s : line.split(" -> ")) {
                String[] parts = s.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                l.add(new Pos(x, y));
            }
            for (int i = 0; i < l.size() - 1; i++) {
                Pos p1 = l.get(i);
                Pos p2 = l.get(i + 1);
                //System.out.println(p1);
                //System.out.println(p2);
                if (p1.x() == p2.x()) {
                    if (p1.y() > p2.y()) {
                        Pos temp = p1;
                        p1 = p2;
                        p2 = temp;
                    }
                    for (int y = p1.y(); y <= p2.y(); y++) {
                        initialStates.put(new Pos(p1.x(), y), State.ROCK);
                    }
                } else if (p1.y() == p2.y()) {
                    if (p1.x() > p2.x()) {
                        Pos temp = p1;
                        p1 = p2;
                        p2 = temp;
                    }
                    for (int x = p1.x(); x <= p2.x(); x++) {
                        initialStates.put(new Pos(x, p1.y()), State.ROCK);
                    }
                }
            }
        }

        //System.out.println(initialStates);
    }

    record Pos(int x, int y) {
        public Pos down() {
            return new Pos(x, y + 1);
        }
        public Pos downleft() {
            return new Pos(x - 1, y + 1);
        }
        public Pos downright() {
            return new Pos(x + 1, y + 1);
        }
    }

    enum State {
        NONE,
        ROCK,
        SAND;
    }

    public void part1(List<String> lines) {
        Map<Pos, State> states = new HashMap<>(initialStates);

        int count = 0;

        outer:
        while (true) {
            Pos sand = new Pos(500, 0);
            while (true) {
                if (sand.y() > 1000) break outer;

                if (states.getOrDefault(sand.down(), State.NONE) == State.NONE) {
                    sand = sand.down();
                } else if (states.getOrDefault(sand.downleft(), State.NONE) == State.NONE) {
                    sand = sand.downleft();
                } else if (states.getOrDefault(sand.downright(), State.NONE) == State.NONE) {
                    sand = sand.downright();
                } else {
                    break;
                }
            }
            //System.out.printf("Sand came to rest at %s\n", sand);
            states.put(sand, State.SAND);
            count++;
        }

        System.out.println(count);
    }

    public void part2(List<String> lines) {
        int floorY = initialStates.keySet().stream().mapToInt(Pos::y).max().getAsInt() + 2;

        Map<Pos, State> states = new HashMap<>(initialStates);

        Function<Pos, State> get = (pos) -> {
            if (pos.y() == floorY) return State.ROCK;
            return states.getOrDefault(pos, State.NONE);
        };

        int count = 0;

        outer:
        while (true) {
            Pos sand = new Pos(500, 0);
            if (get.apply(sand) == State.SAND) break;

            while (true) {
                //if (sand.y() > 1000) break outer;

                if (get.apply(sand.down()) == State.NONE) {
                    sand = sand.down();
                } else if (get.apply(sand.downleft()) == State.NONE) {
                    sand = sand.downleft();
                } else if (get.apply(sand.downright()) == State.NONE) {
                    sand = sand.downright();
                } else {
                    break;
                }
            }
            //System.out.printf("Sand came to rest at %s\n", sand);
            states.put(sand, State.SAND);
            count++;
        }

        System.out.println(count);
    }

    public static void main(String...args) {
        new Day14().run();
    }
}
