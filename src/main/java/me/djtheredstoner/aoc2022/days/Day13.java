package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day13 implements DayBase {

    List<Pair> pairs = new ArrayList<>();

    List<Element> allPackets = new ArrayList<>();

    public void init(List<String> lines) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        List<Element> pair = new ArrayList<>();

        for (String line : lines) {
            if (line.isEmpty()) {
                pairs.add(new Pair(pair.get(0), pair.get(1)));
                pair.clear();
                continue;
            }
            try {
                ScriptObjectMirror result = (ScriptObjectMirror) engine.eval(line);
                var element = parseObject(result);
                pair.add(element);
                allPackets.add(element);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Element parseObject(Object object) {
        if (object instanceof Integer i) {
            return new Int(i);
        } else if (object instanceof ScriptObjectMirror mirror) {
            List<Element> elements = new ArrayList<>();
            for (Object value : mirror.values()) {
                elements.add(parseObject(value));
            }
            return new Lst(elements);
        } else throw new IllegalArgumentException(Objects.toString(object));
    }

    public void part1(List<String> lines) {
        int idx = 1;
        int sum = 0;
        for (Pair pair : pairs) {
            if (pair.left().compareTo(pair.right) < 0) {
                sum += idx;
            }
            idx++;
        }

        System.out.println(sum);
    }

    public void part2(List<String> lines) {
        Element div1 = new Lst(List.of(new Lst(List.of(new Int(2)))));
        Element div2 = new Lst(List.of(new Lst(List.of(new Int(6)))));

        allPackets.add(div1);
        allPackets.add(div2);

        allPackets.sort(Element::compareTo);

        int idx = 1;
        int prod = 1;
        for (Element packet : allPackets) {
            if (packet.equals(div1) || packet.equals(div2)) {
                prod *= idx;
            }
            idx++;
        }

        System.out.println(prod);
    }

    record Pair(Element left, Element right) {}

    sealed interface Element extends Comparable<Element> permits Int, Lst {}

    record Int(int value) implements Element {
        @Override
        public int compareTo(Element o) {
            if (o instanceof Int i) {
                return Integer.compare(value(), i.value());
            } else if (o instanceof Lst lst) {
                return new Lst(List.of(this)).compareTo(o);
            } else throw new IllegalArgumentException(Objects.toString(o));
        }
    }

    record Lst(List<Element> values) implements Element {
        @Override
        public int compareTo(Element o) {
            if (o instanceof Int i) {
                return this.compareTo(new Lst(List.of(i)));
            } else if (o instanceof Lst lst) {
                int len = Math.min(values().size(), lst.values().size());
                for (int i = 0; i < len; i++) {
                    int r = values().get(i).compareTo(lst.values().get(i));
                    if (r != 0) return r;
                }
                if (values().size() == lst.values().size()) return 0;
                return values().size() < lst.values().size() ? -1 : 1;
            } else throw new IllegalArgumentException(Objects.toString(o));
        }
    }

    public static void main(String...args) {
        new Day13().run();
    }
}
