package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class Day23 implements DayBase {

    Set<Pos> startingGrid = new HashSet<>();

    public void init(List<String> lines) {
        int y = 0;
        for (String line : lines) {
            int x = 0;
            for (char c : line.toCharArray()) {
                if (c == '#')
                    startingGrid.add(new Pos(x, y));
                x++;
            }
            y++;
        }

        System.out.println(startingGrid);
    }

    record Pos(int x, int y) {
        Pos north() {
            return new Pos(x, y - 1);
        }

        Pos south() {
            return new Pos(x, y + 1);
        }

        Pos east() {
            return new Pos(x + 1, y);
        }

        Pos west() {
            return new Pos(x - 1, y);
        }

        Pos northeast() {
            return north().east();
        }

        Pos northwest() {
            return north().west();
        }

        Pos southeast() {
            return south().east();
        }

        Pos southwest() {
            return south().west();
        }
    }

    private boolean c(Pos p, Set<Pos> g) {
        return !g.contains(p);
    }

    record Check(Function<Pos, Boolean> condition, Consumer<Pos> applicator) {}

    public void part1(List<String> lines) {
        Set<Pos> g = new HashSet<>(startingGrid);

        int checkOffset = 0;

        for (int i = 0; i < 10; i++){
            /*System.out.println();

            int minX = g.stream().mapToInt(Pos::x).min().getAsInt();
            int maxX = g.stream().mapToInt(Pos::x).max().getAsInt();
            int minY = g.stream().mapToInt(Pos::y).min().getAsInt();
            int maxY = g.stream().mapToInt(Pos::y).max().getAsInt();

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    if (g.contains(new Pos(x, y))) {
                        System.out.print('#');
                    } else {
                        System.out.print('.');
                    }
                }
                System.out.println();
            }*/


            Map<Pos, Pos> proposals = new HashMap<>();

            List<Check> checks = List.of(
                new Check(pos -> c(pos.north(), g) && c(pos.northeast(), g) && c(pos.northwest(), g), pos -> proposals.put(pos, pos.north())),
                new Check(pos -> c(pos.south(), g) && c(pos.southeast(), g) && c(pos.southwest(), g), pos -> proposals.put(pos, pos.south())),
                new Check(pos -> c(pos.west(), g) && c(pos.northwest(), g) && c(pos.southwest(), g), pos -> proposals.put(pos, pos.west())),
                new Check(pos -> c(pos.east(), g) && c(pos.northeast(), g) && c(pos.southeast(), g), pos -> proposals.put(pos, pos.east()))
            );

            /*for (Pos pos : g) {
                if (c(pos.north(), g) && c(pos.northeast(), g) && c(pos.northwest(), g)) {
                    proposals.put(pos, pos.north());
                } else if (c(pos.south(), g) && c(pos.southeast(), g) && c(pos.southwest(), g)) {
                    proposals.put(pos, pos.south());
                } else if (c(pos.west(), g) && c(pos.northwest(), g) && c(pos.southwest(), g)) {
                    proposals.put(pos, pos.west());
                } else if (c(pos.east(), g) && c(pos.northeast(), g) && c(pos.southeast(), g)) {
                    proposals.put(pos, pos.east());
                }
            }*/

            for (Pos pos : g) {
                boolean any = false;
                for (Pos pos1 : List.of(new Pos(-1, -1), new Pos(-1, 0), new Pos(-1, 1), new Pos(0, -1), new Pos(1, -1), new Pos(1, 0), new Pos(1, 1), new Pos(0, 1))) {
                    if (g.contains(new Pos(pos.x() + pos1.x(), pos.y() + pos1.y()))) {
                        any = true;
                        break;
                    }
                }
                if (!any) continue;

                for (int j = 0; j < 4; j++) {
                    var check = checks.get((j + checkOffset) % 4);
                    if (check.condition().apply(pos)) {
                        check.applicator().accept(pos);
                        break;
                    }
                }
            }

            checkOffset++;

            HashSet<Pos> multiProposals = new HashSet<>();
            HashSet<Pos> visited = new HashSet<>();

            for (Map.Entry<Pos, Pos> entry : proposals.entrySet()) {
                if (!visited.add(entry.getValue())) {
                    multiProposals.add(entry.getValue());
                }
            }

            //System.out.println(multiProposals.size());

            for (Map.Entry<Pos, Pos> entry : proposals.entrySet()) {
                if (multiProposals.contains(entry.getValue())) {
                    //System.out.printf("skipping %s\n", entry.getValue());
                    continue;
                }
                g.remove(entry.getKey());
                g.add(entry.getValue());
            }
        }

        int minX = g.stream().mapToInt(Pos::x).min().getAsInt();
        int maxX = g.stream().mapToInt(Pos::x).max().getAsInt();
        int minY = g.stream().mapToInt(Pos::y).min().getAsInt();
        int maxY = g.stream().mapToInt(Pos::y).max().getAsInt();

        /*for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (g.contains(new Pos(x, y))) {
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }

        System.out.println(g);*/

        System.out.println((maxX - minX + 1) * (maxY - minY + 1) - g.size());
    }

    public void part2(List<String> lines) {
        Set<Pos> g = new HashSet<>(startingGrid);

        int checkOffset = 0;

        while(true) {
            /*System.out.println();

            int minX = g.stream().mapToInt(Pos::x).min().getAsInt();
            int maxX = g.stream().mapToInt(Pos::x).max().getAsInt();
            int minY = g.stream().mapToInt(Pos::y).min().getAsInt();
            int maxY = g.stream().mapToInt(Pos::y).max().getAsInt();

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    if (g.contains(new Pos(x, y))) {
                        System.out.print('#');
                    } else {
                        System.out.print('.');
                    }
                }
                System.out.println();
            }*/


            Map<Pos, Pos> proposals = new HashMap<>();

            List<Check> checks = List.of(
                new Check(pos -> c(pos.north(), g) && c(pos.northeast(), g) && c(pos.northwest(), g), pos -> proposals.put(pos, pos.north())),
                new Check(pos -> c(pos.south(), g) && c(pos.southeast(), g) && c(pos.southwest(), g), pos -> proposals.put(pos, pos.south())),
                new Check(pos -> c(pos.west(), g) && c(pos.northwest(), g) && c(pos.southwest(), g), pos -> proposals.put(pos, pos.west())),
                new Check(pos -> c(pos.east(), g) && c(pos.northeast(), g) && c(pos.southeast(), g), pos -> proposals.put(pos, pos.east()))
            );

            /*for (Pos pos : g) {
                if (c(pos.north(), g) && c(pos.northeast(), g) && c(pos.northwest(), g)) {
                    proposals.put(pos, pos.north());
                } else if (c(pos.south(), g) && c(pos.southeast(), g) && c(pos.southwest(), g)) {
                    proposals.put(pos, pos.south());
                } else if (c(pos.west(), g) && c(pos.northwest(), g) && c(pos.southwest(), g)) {
                    proposals.put(pos, pos.west());
                } else if (c(pos.east(), g) && c(pos.northeast(), g) && c(pos.southeast(), g)) {
                    proposals.put(pos, pos.east());
                }
            }*/

            for (Pos pos : g) {
                boolean any = false;
                for (Pos pos1 : List.of(new Pos(-1, -1), new Pos(-1, 0), new Pos(-1, 1), new Pos(0, -1), new Pos(1, -1), new Pos(1, 0), new Pos(1, 1), new Pos(0, 1))) {
                    if (g.contains(new Pos(pos.x() + pos1.x(), pos.y() + pos1.y()))) {
                        any = true;
                        break;
                    }
                }
                if (!any) continue;

                for (int j = 0; j < 4; j++) {
                    var check = checks.get((j + checkOffset) % 4);
                    if (check.condition().apply(pos)) {
                        check.applicator().accept(pos);
                        break;
                    }
                }
            }

            if (proposals.size() == 0) {
                System.out.println(checkOffset + 1);
                return;
            }

            checkOffset++;

            HashSet<Pos> multiProposals = new HashSet<>();
            HashSet<Pos> visited = new HashSet<>();

            for (Map.Entry<Pos, Pos> entry : proposals.entrySet()) {
                if (!visited.add(entry.getValue())) {
                    multiProposals.add(entry.getValue());
                }
            }

            //System.out.println(multiProposals.size());

            for (Map.Entry<Pos, Pos> entry : proposals.entrySet()) {
                if (multiProposals.contains(entry.getValue())) {
                    //System.out.printf("skipping %s\n", entry.getValue());
                    continue;
                }
                g.remove(entry.getKey());
                g.add(entry.getValue());
            }
        }

        //int minX = g.stream().mapToInt(Pos::x).min().getAsInt();
        //int maxX = g.stream().mapToInt(Pos::x).max().getAsInt();
        //int minY = g.stream().mapToInt(Pos::y).min().getAsInt();
        //int maxY = g.stream().mapToInt(Pos::y).max().getAsInt();

        /*for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (g.contains(new Pos(x, y))) {
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }

        System.out.println(g);*/

        //System.out.println((maxX - minX + 1) * (maxY - minY + 1) - g.size());
    }

    public static void main(String...args) {
        new Day23().run();
    }
}
