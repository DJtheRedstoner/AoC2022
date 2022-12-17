package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Day16 implements DayBase {

    Map<String, Valve> valves = new HashMap<>();

    public void init(List<String> lines) {
        var pattern = Pattern.compile("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)");

        int i = 0;

        for (String line : lines) {
            var matcher = pattern.matcher(line);
            if (!matcher.find()) throw new IllegalStateException(line);
            valves.put(matcher.group(1), new Valve(
                matcher.group(1),
                Integer.parseInt(matcher.group(2)),
                List.of(matcher.group(3).split(", ")),
                i,
                1L << i++
            ));
        }
    }

    record Valve(String name, int flow, List<String> tunnels, int id, long bit) {}

    public void part1(List<String> lines) {
        Valve start = valves.get("AA");

        List<State> states = new ArrayList<>();
        states.add(new State(start, 0, 0));

        HashMap<Best, Integer> best = new HashMap<>();

        for (int i = 1; i < 31; i++) {
            List<State> newStates = new ArrayList<>();

            for (State state : states) {
                Valve v = state.position();
                Best key = new Best(v, state.opened());
                if (best.containsKey(key) && state.releasedPressure() <= best.get(key)) {
                    continue;
                }

                best.put(key, state.releasedPressure());

                if ((v.bit() & state.opened()) == 0 && v.flow() > 0) {
                    newStates.add(new State(v, state.opened() | v.bit(), state.releasedPressure() + v.flow() * (30 - i)));
                } else {
                    for (String tunnel : v.tunnels()) {
                        newStates.add(new State(valves.get(tunnel), state.opened(), state.releasedPressure()));
                    }
                }
            }

            states = newStates;
        }

        System.out.println(states.stream().mapToInt(State::releasedPressure).max().getAsInt());
    }

    record State(Valve position, long opened, int releasedPressure) {}

    record Best(Valve position, long opened) {}

    public void part2(List<String> lines) {
        List<Valve> positiveValves = new ArrayList<>();

        int[][] graph = new int[100][100];
        for (int[] row : graph) {
            Arrays.fill(row, 10000000);
        }

        for (int i = 0; i < 100; i++) {
            graph[i][i] = 0;
        }

        for (Valve valve : valves.values()) {
            if (valve.flow() > 0 || "AA".equals(valve.name())) {
                positiveValves.add(valve);
            }
            for (String tunnel : valve.tunnels()) {
                Valve neighbour = valves.get(tunnel);
                graph[valve.id()][neighbour.id()] = Math.min(1, graph[valve.id()][neighbour.id()]);
            }
        }

        int n = valves.size();

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    graph[i][j] = Math.min(graph[i][j], graph[i][k] + graph[k][j]);
                }
            }
        }

        LinkedList<State2> queue = new LinkedList<>();
        Map<Best2, Integer> best = new HashMap<>();

        // i hate this
        var add = new Object() {
            void invoke(Valve position, long opened, int pressure, int time) {
                var key = new Best2(position, opened, time);
                if (time >= 0 && pressure > best.getOrDefault(key, -1)) {
                    best.put(key, pressure);
                    queue.add(new State2(position, opened, pressure, time));
                }
            }
        };

        add.invoke(valves.get("AA"), 0, 0, 26);
        while (queue.size() > 0) {
            //System.out.println(queue.size());
            State2 state = queue.pop();
            Valve v = state.position();
            if ((state.opened() & (1L << positiveValves.indexOf(v))) == 0 && state.time() >= 1) {
                add.invoke(v, state.opened() | (1L << positiveValves.indexOf(v)), state.pressure() + (state.time() - 1) * v.flow(), state.time() - 1);
            }

            for (Valve v2 : positiveValves) {
                int move = graph[v.id()][v2.id()];
                if (move <= state.time()) {
                    add.invoke(v2, state.opened(), state.pressure(), state.time() - move);
                }
            }
        }

        int m = positiveValves.size();

        HashMap<Long, Integer> table = new HashMap<>();
        for (Map.Entry<Best2, Integer> entry : best.entrySet()) {
            table.put(entry.getKey().opened(), Math.max(table.getOrDefault(entry.getKey().opened(), 0), entry.getValue()));
        }

        int ret = 0;
        for (long mask = 0; mask < (1L << m); mask++) {
            long invMask = ((1L << m) - 1) ^ mask;
            ret = Math.max(ret, table.getOrDefault(invMask, 0));
            long mask2 = mask;
            while (mask2 > 0) {
                ret = Math.max(ret, table.getOrDefault(invMask, 0) + table.getOrDefault(mask2, 0));
                mask2 = (mask2 - 1) & mask;
            }
        }

        System.out.println(ret);
    }

    record State2(Valve position, long opened, int pressure, int time) {}

    record Best2(Valve position, long opened, int time) {}

    public static void main(String...args) {
        new Day16().run();
    }
}
