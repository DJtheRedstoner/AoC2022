package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day20 implements DayBase {

    List<Integer> numbers = new ArrayList<>();

    public void init(List<String> lines) {
        for (String line : lines) {
            numbers.add(Integer.parseInt(line));
        }
    }

    public void part1(List<String> lines) {
        ArrayList<Entry> workingNumbers = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            workingNumbers.add(new Entry(numbers.get(i), i));
        }

        for (int i = 0; i < numbers.size(); i++) {
            int number = numbers.get(i);
            int idx = workingNumbers.indexOf(new Entry(number, i));

            int newIdx = (idx + number) % (workingNumbers.size() - 1);
            //System.out.println(idx);
            if (newIdx < 0) {
                newIdx += (workingNumbers.size() - 1);
            }
            //System.out.println(newIdx);

            workingNumbers.remove(idx);
            workingNumbers.add(newIdx, new Entry(number, i));

            //System.out.println(workingNumbers);
        }

        int zI = 0;
        for (int i = 0; i < workingNumbers.size(); i++) {
            if (workingNumbers.get(i).number() == 0) zI = i;
        }

        System.out.println(
            workingNumbers.get((zI + 1000) % (workingNumbers.size())).number() +
            workingNumbers.get((zI + 2000) % (workingNumbers.size())).number() +
            workingNumbers.get((zI + 3000) % (workingNumbers.size())).number()
        );
    }

    record Entry(long number, int originalPosition) {}

    public void part2(List<String> lines) {
        long[] encryptedNumbers = numbers.stream().mapToLong(n -> n * 811589153L).toArray();

        ArrayList<Entry> workingNumbers = new ArrayList<>();
        for (int i = 0; i < encryptedNumbers.length; i++) {
            workingNumbers.add(new Entry(encryptedNumbers[i], i));
        }

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < encryptedNumbers.length; i++) {
                long number = encryptedNumbers[i];
                int idx = workingNumbers.indexOf(new Entry(number, i));

                int newIdx = (int)((idx + number) % (workingNumbers.size() - 1));
                //System.out.println(idx);
                if (newIdx < 0) {
                    newIdx += (workingNumbers.size() - 1);
                }
                //System.out.println(newIdx);

                workingNumbers.remove(idx);
                workingNumbers.add(newIdx, new Entry(number, i));

                //System.out.println(workingNumbers);
            }
        }

        int zI = 0;
        for (int i = 0; i < workingNumbers.size(); i++) {
            if (workingNumbers.get(i).number() == 0) zI = i;
        }

        System.out.println(
            workingNumbers.get((zI + 1000) % (workingNumbers.size())).number() +
            workingNumbers.get((zI + 2000) % (workingNumbers.size())).number() +
            workingNumbers.get((zI + 3000) % (workingNumbers.size())).number()
        );
    }

    public static void main(String...args) {
        new Day20().run();
    }
}
