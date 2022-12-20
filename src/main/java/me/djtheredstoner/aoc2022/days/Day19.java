package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19 implements DayBase {

    ArrayList<Blueprint> blueprints = new ArrayList<>();

    public void init(List<String> lines) {
        Pattern p = Pattern.compile("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.");
        for (String line : lines) {
            Matcher m = p.matcher(line);
            if (!m.find()) throw new IllegalStateException(line);
            blueprints.add(new Blueprint(
                Integer.parseInt(m.group(1)),
                new Resources(Integer.parseInt(m.group(2)), 0, 0, 0),
                new Resources(Integer.parseInt(m.group(3)), 0, 0, 0),
                new Resources(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), 0, 0),
                new Resources(Integer.parseInt(m.group(6)), 0, Integer.parseInt(m.group(7)), 0)
            ));
        }
    }

    record Blueprint(int id, Resources ore, Resources clay, Resources obsidian, Resources geode) {
        Resources getByIndex(int i) {
            return switch (i) {
                case 0 -> ore();
                case 1 -> clay();
                case 2 -> obsidian();
                case 3 -> geode();
                default ->  throw new IllegalArgumentException(Integer.toString(i));
            };
        }
    }

    public void part1(List<String> lines) {
        int qualitySum = 0;

        for (Blueprint blueprint : blueprints) {
            qualitySum += blueprint.id() * maxGeodes(blueprint, 24);
        }

        System.out.println(qualitySum);
    }

    private int maxGeodes(Blueprint blueprint, int time) {

        int[] maxRobots = new int[4];

        for (int i = 0; i < 3; i++) {
            int max = 0;
            for (int j = 0; j < 4; j++) {
                max = Math.max(blueprint.getByIndex(j).getByIndex(i), max);
            }
            maxRobots[i] = max;
        }
        maxRobots[3] = 10000;

        LinkedList<State> queue = new LinkedList<>();
        queue.add(new State(
            new int[]{0, 0, 0, 0},
            new int[]{1, 0, 0, 0},
            0
        ));

        int maxGeodes = 0;

        while (queue.size() > 0) {
            //System.out.println(queue.size());

            State state = queue.remove();
            int[] inventory = state.inventory();
            int[] bots = state.bots();

            for (int r = 0; r < 4; r++) {
                if (bots[r] == maxRobots[r]) continue;

                Resources recipe = blueprint.getByIndex(r);
                int maxTime = 0;

                for (int i = 0; i < 4; i++) {
                    maxTime = Math.max(maxTime, timeToGet(inventory[i], recipe.getByIndex(i), bots[i]));
                }

                int newElapsed = state.elapsed() + maxTime + 1;

                if (newElapsed > time) continue;

                int[] newInventory = new int[4];
                for (int i = 0; i < 4; i++) {
                    newInventory[i] = inventory[i] + bots[i] * (maxTime + 1) - recipe.getByIndex(i);
                }

                int[] newBots = Arrays.copyOf(state.bots(), 4);
                newBots[r] += 1;

                int remainingTime = time - newElapsed;
                if ((remainingTime - 1) * remainingTime / 2 + newInventory[3] + remainingTime * newBots[3] < maxGeodes) continue;

                queue.add(new State(
                    newInventory,
                    newBots,
                    newElapsed
                ));
            }

            int geodes = inventory[3] + bots[3] * (time - state.elapsed());
            maxGeodes = Math.max(geodes, maxGeodes);
        }

        return maxGeodes;
    }

    private int timeToGet(int current, int required, int production) {
        if (current >= required) {
            return 0;
        }

        if (production == 0) return 100000;

        return (required - current + production - 1) / production;
    }

    enum ResourceType {
        ORE,
        CLAY,
        OBSIDIAN,
        GEODE
    }

    record State(int[] inventory, int[] bots, int elapsed) {}

    record Resources(int ore, int clay, int obsidian, int geode) {
        int getByIndex(int i) {
            return switch (i) {
                case 0 -> ore();
                case 1 -> clay();
                case 2 -> obsidian();
                case 3 -> geode();
                default ->  throw new IllegalArgumentException(Integer.toString(i));
            };
        }
    }

    public void part2(List<String> lines) {
        int product = 1;

        for (int i = 0; i < 3; i++) {
            product *= maxGeodes(blueprints.get(i), 32);
        }

        System.out.println(product);
    }

    public static void main(String...args) {
        new Day19().run();
    }
}
