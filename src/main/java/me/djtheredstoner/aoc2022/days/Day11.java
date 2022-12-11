package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class Day11 implements DayBase {

    List<Monkey> monkeys = new ArrayList<>();

    public void init(List<String> lines) {
        List<Long> items = new ArrayList<>();
        Operation op = null;
        int test = 0;
        int _true = 0;
        int _false = 0;

        for (String line : lines) {
            if (line.startsWith("  Starting items: ")) {
                items = new ArrayList<>(Arrays.stream(line.substring("  Starting items: ".length()).split(", ")).map(Long::parseLong).toList());
            } else if (line.startsWith("  Operation: new = ")) {
                String opStr = line.substring("  Operation: new = ".length());
                if (opStr.equals("old * old")) {
                    op = old -> old * old;
                } else if (opStr.startsWith("old + ")) {
                    int val = Integer.parseInt(opStr.substring("old + ".length()));
                    op = old -> old + val;
                } else if (opStr.startsWith("old * ")) {
                    int val = Integer.parseInt(opStr.substring("old * ".length()));
                    op = old -> old * val;
                } else throw new IllegalStateException(opStr);
            } else if (line.startsWith("  Test: divisible by ")) {
                test = Integer.parseInt(line.substring("  Test: divisible by ".length()));
            } else if (line.startsWith("    If true: throw to monkey ")) {
                _true = Integer.parseInt(line.substring("    If true: throw to monkey ".length()));
            }  else if (line.startsWith("    If false: throw to monkey ")) {
                _false = Integer.parseInt(line.substring("    If false: throw to monkey ".length()));
            } else if (line.startsWith("Monkey ") && op!= null) {
                monkeys.add(new Monkey(items, op, test, _true, _false));
            }
        }

        monkeys.add(new Monkey(items, op, test, _true, _false));

        System.out.println(monkeys);
    }

    public void part1(List<String> lines) {
        Map<Monkey, Integer> throwz = new IdentityHashMap<>();

        for (int round = 1; round < 21; round++) {
            for (Monkey monkey : monkeys) {
                while (monkey.items.size() > 0) {
                    long worry = monkey.items.remove(0);
                    worry = monkey.op.apply(worry);
                    worry /= 3;
                    if (worry % monkey.test == 0) {
                        monkeys.get(monkey._true).items.add(worry);
                    } else {
                        monkeys.get(monkey._false).items.add(worry);
                    }
                    throwz.put(monkey, throwz.getOrDefault(monkey, 0) + 1);
                }
            }

            //System.out.println(monkeys);
        }

        System.out.println(throwz.values().stream().sorted(Comparator.reverseOrder()).limit(2).reduce(1, (x, y) -> x* y));
    }

    interface Operation {
        long apply(long x);
    }

    record Monkey(List<Long> items, Operation op, int test, int _true, int _false) {

    }

    public void part2(List<String> lines) {
        monkeys = new ArrayList<>();
        init(lines);

        Map<Monkey, Integer> throwz = new IdentityHashMap<>();

        long modulus = monkeys.stream().map(Monkey::test).reduce(1, (x, y) -> x* y);

        System.out.println(modulus);

        for (int round = 1; round < 10001; round++) {
            for (Monkey monkey : monkeys) {
                while (monkey.items.size() > 0) {
                    long worry = monkey.items.remove(0);
                    worry = monkey.op.apply(worry);
                    //System.out.println(worry);
                    worry = worry % modulus;
                    if (worry % monkey.test == 0) {
                        monkeys.get(monkey._true).items.add(worry);
                    } else {
                        monkeys.get(monkey._false).items.add(worry);
                    }
                    throwz.put(monkey, throwz.getOrDefault(monkey, 0) + 1);
                }
            }

            //System.out.println(monkeys);
        }

        System.out.println(throwz.values().stream().sorted(Comparator.reverseOrder()).limit(2).mapToLong(Integer::longValue).reduce(1, (x, y) -> x* y));
    }

    public static void main(String...args) {
        new Day11().run();
    }
}
