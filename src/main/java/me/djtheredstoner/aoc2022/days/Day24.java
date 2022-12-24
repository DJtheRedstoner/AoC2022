package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Day24 implements DayBase {

    Pos start;
    Pos end;
    Map<Pos, Blizzard> initialBlizzards = new HashMap<>();
    List<Pos> walls = new ArrayList<>();

    int width;
    int height;

    public void init(List<String> lines) {
        start = new Pos(lines.get(0).indexOf('.'), 0);
        int size = lines.size();
        end = new Pos(lines.get(lines.size() - 1).indexOf('.'), size - 1);

        for (int i = 0; i < size; i++) {
            int x = 0;
            for (char c : lines.get(i).toCharArray()) {
                if (c == '#') walls.add(new Pos(x, i));

                Direction d = switch (c) {
                    case '^' -> Direction.UP;
                    case 'v' -> Direction.DOWN;
                    case '<' -> Direction.LEFT;
                    case '>' -> Direction.RIGHT;
                    default -> null;
                };
                if (d != null) {
                    Pos p = new Pos(x, i);
                    initialBlizzards.put(p, new Blizzard(p, d));
                }
                x++;
            }
        }

        width = lines.get(0).length();
        height = lines.size();

        walls.add(new Pos(1, -1));
        walls.add(new Pos(width - 2, height));

        //System.out.println(start);
        //System.out.println(end);
        //System.out.println(initialBlizzards);
        //System.out.println(walls);
        //System.out.println(width);
        //System.out.println(height);
    }

    record Pos(int x, int y) {
        public Pos move(Direction d) {
            return new Pos(x + d.move().x(), y + d.move().y());
        }
    }

    record Blizzard(Pos pos, Direction direction) {}

    enum Direction {
        UP(new Pos(0, -1)),
        DOWN(new Pos(0, 1)),
        LEFT(new Pos(-1, 0)),
        RIGHT(new Pos(1, 0)),
        NONE(new Pos(0, 0));

        final Pos move;

        Direction(Pos move) {
            this.move = move;
        }

        public Pos move() {
            return move;
        }
    }

    public void part1(List<String> lines) {
        LinkedList<State> queue = new LinkedList<>();

        queue.add(new State(start, 0));

        List<List<Blizzard>> blizzardsAt = new ArrayList<>();
        blizzardsAt.add(new ArrayList<>(initialBlizzards.values()));

        for (int i = 0; i < 1000; i++) {
            List<Blizzard> blizzards = blizzardsAt.get(blizzardsAt.size() - 1);
            ArrayList<Blizzard> newBlizzards = new ArrayList<>();
            for (Blizzard b : blizzards) {
                Pos newPos = b.pos().move(b.direction());
                if (walls.contains(newPos)) {
                    newPos = switch (b.direction()) {
                        case UP -> new Pos(b.pos().x(), height - 2);
                        case DOWN -> new Pos(b.pos().x(), 1);
                        case LEFT -> new Pos(width - 2, b.pos().y());
                        case RIGHT -> new Pos(1, b.pos().y());
                        case NONE -> throw new IllegalStateException();
                    };
                }
                newBlizzards.add(new Blizzard(newPos, b.direction()));
            }
            blizzardsAt.add(newBlizzards);
        }

        List<List<Pos>> positionsAt = blizzardsAt.stream().map(l -> l.stream().map(Blizzard::pos).toList()).toList();

        System.out.println(fun(new State(start, 0), end, positionsAt));
    }

    record State(Pos pos, int timeTaken) {}

    private int fun(State start, Pos end, List<List<Pos>> positionsAt) {
        LinkedList<State> queue = new LinkedList<>();

        queue.add(start);

        /*for (int i = 0; i < 20; i++) {
            System.out.println();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Pos p = new Pos(x, y);
                    if (walls.contains(p)) {
                        System.out.print('#');
                    } else if (positionsAt.get(i).contains(p)) {
                        System.out.print('@');
                    } else {
                        System.out.print('.');
                    }
                }
                System.out.println();
            }
        }*/

        HashSet<State> visited = new HashSet<>();

        //Map<State, List<Pos>> pathByState = new HashMap<>();

        while (queue.size() > 0) {
            State s = queue.remove();

            if (visited.contains(s)) continue;
            visited.add(s);

            if (s.pos().equals(end)) {
                //System.out.println(pathByState.get(s));
                //System.out.println(s.timeTaken());
                return s.timeTaken();
            }

            List<Pos> positions = positionsAt.get(s.timeTaken() + 1);

            for (Direction value : Direction.values()) {
                Pos newPos = s.pos().move(value);
                if (!walls.contains(newPos) && !positions.contains(newPos)) {
                    State s1 = new State(newPos, s.timeTaken() + 1);
                    //var h = new ArrayList<>(pathByState.get(s));
                    //h.add(newPos);
                    //pathByState.put(s1, h);
                    queue.add(s1);
                }
            }

            //State s1 = new State(s.pos(), s.timeTaken() + 1);
            //pathByState.put(s1, pathByState.get(s));
            //queue.add(s1);
        }
        throw new IllegalStateException();
    }

    public void part2(List<String> lines) {
        {
            List<List<Blizzard>> blizzardsAt = new ArrayList<>();
            blizzardsAt.add(new ArrayList<>(initialBlizzards.values()));

            for (int i = 0; i < 1000; i++) {
                List<Blizzard> blizzards = blizzardsAt.get(blizzardsAt.size() - 1);
                ArrayList<Blizzard> newBlizzards = new ArrayList<>();
                for (Blizzard b : blizzards) {
                    Pos newPos = b.pos().move(b.direction());
                    if (walls.contains(newPos)) {
                        newPos = switch (b.direction()) {
                            case UP -> new Pos(b.pos().x(), height - 2);
                            case DOWN -> new Pos(b.pos().x(), 1);
                            case LEFT -> new Pos(width - 2, b.pos().y());
                            case RIGHT -> new Pos(1, b.pos().y());
                            case NONE -> throw new IllegalStateException();
                        };
                    }
                    newBlizzards.add(new Blizzard(newPos, b.direction()));
                }
                blizzardsAt.add(newBlizzards);
            }

            List<List<Pos>> positionsAt = blizzardsAt.stream().map(l -> l.stream().map(Blizzard::pos).toList()).toList();

            int there = fun(new State(start, 0), end, positionsAt);
            int back = fun(new State(end, there), start, positionsAt);
            int thereAgain = fun(new State(start, back), end, positionsAt);
            System.out.println(thereAgain);
        }
    }

    public static void main(String...args) {
        new Day24().run();
    }
}
