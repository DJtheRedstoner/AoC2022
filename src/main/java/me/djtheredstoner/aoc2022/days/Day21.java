package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day21 implements DayBase {

    static Map<String, Monkey> monkeys = new HashMap<>();

    public void init(List<String> lines) {
        Pattern op = Pattern.compile("(\\w{4}) ([+\\-*/]) (\\w{4})");
        for (String line : lines) {
            var parts = line.split(": ");
            String name = parts[0];
            Matcher m = op.matcher(parts[1]);
            if (m.find()) {
                monkeys.put(name, new ExpressionMonkey(
                    m.group(1), m.group(3),
                    switch (m.group(2)) {
                        case "+" -> PLUS;
                        case "-" -> MINUS;
                        case "/" -> DIVIDE;
                        case "*" -> TIMES;
                        default -> throw new IllegalArgumentException();
                    }
                ));
            } else {
                monkeys.put(name, new ConstantMonkey(Long.parseLong(parts[1])));
            }
        }
    }

    private final Operation PLUS = new Operation("+", (l, r) -> g(l) + g(r));
    private final Operation MINUS = new Operation("-", (l, r) -> g(l) - g(r));
    private final Operation TIMES = new Operation("*", (l, r) -> g(l) * g(r));
    private final Operation DIVIDE = new Operation("/", (l, r) -> g(l) / g(r));
    private final Operation EQUALS = new Operation("=", (l, r) -> g(l) == g(r) ? 1 : 0);

    record Operation(String name, Exp exp) {}

    private long g(String key) {
        return monkeys.get(key).value();
    }

    interface Exp {
        long apply(String left, String right);
    }

    sealed interface Monkey {
        long value();
    }

    record ConstantMonkey(long value) implements Monkey {
        @Override
        public String toString() {
            return Long.toString(value);
        }
    }

    record ExpressionMonkey(String left, String right, Operation op) implements Monkey {
        public long value() {
            return op.exp().apply(left, right);
        }

        @Override
        public String toString() {
            try {
                return Long.toString(value());
            } catch (Exception e) {
                return "(" + monkeys.get(left()).toString() + " " + op.name() + " " + monkeys.get(right()).toString() + ")";
            }
        }
    }

    public void part1(List<String> lines) {
        System.out.println(monkeys.get("root").value());
    }

    public void part2(List<String> lines) {
        ExpressionMonkey oldRoot = (ExpressionMonkey) monkeys.get("root");

        monkeys.put("root", new ExpressionMonkey(oldRoot.left(), oldRoot.right(), EQUALS));
        monkeys.put("humn", new VariableMonkey());

        String eq = monkeys.get("root").toString();

        System.out.println();
        System.out.println("https://sagecell.sagemath.org/");
        System.out.println("solve(" + eq.replace("=", "==") + ", x)");
    }

    record VariableMonkey() implements Monkey {
        public long value() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "x";
        }
    }
    public static void main(String...args) {
        new Day21().run();
    }
}
