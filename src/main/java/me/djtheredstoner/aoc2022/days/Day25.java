package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Day25 implements DayBase {

    List<BigInteger> numbers = new ArrayList<>();

    public void init(List<String> lines) {
        for (String line : lines) {
            BigInteger accum = BigInteger.ZERO;
            for (int i = 0; i < line.length(); i++) {
                BigInteger place = BigInteger.valueOf(5).pow(i);
                char c = line.charAt(line.length() - 1 - i);
                BigInteger value = place.multiply(switch (c) {
                    case '0' -> BigInteger.ZERO;
                    case '1' -> BigInteger.ONE;
                    case '2' -> BigInteger.TWO;
                    case '-' -> BigInteger.valueOf(-1);
                    case '=' -> BigInteger.valueOf(-2);
                    default -> throw new IllegalStateException(Character.toString(c));
                });
                accum = accum.add(value);
            }
            numbers.add(accum);
        }
    }

    public void part1(List<String> lines) {
        BigInteger sum = BigInteger.ZERO;

        for (BigInteger number : numbers) {
            sum = sum.add(number);
        }

        StringBuilder sb = new StringBuilder();
        do {
            BigInteger b = sum.mod(BigInteger.valueOf(5));
            if (b.equals(BigInteger.ZERO)) {
                sb.append("0");
                sum = sum.divide(BigInteger.valueOf(5));
            } else if (b.equals(BigInteger.ONE)) {
                sb.append("1");
                sum = sum.subtract(BigInteger.ONE);
                sum = sum.divide(BigInteger.valueOf(5));
            } else if (b.equals(BigInteger.TWO)) {
                sb.append("2");
                sum = sum.subtract(BigInteger.TWO);
                sum = sum.divide(BigInteger.valueOf(5));
            } else if (b.equals(BigInteger.valueOf(3))) {
                sb.append("=");
                sum = sum.add(BigInteger.TWO);
                sum = sum.divide(BigInteger.valueOf(5));
            } else if (b.equals(BigInteger.valueOf(4))) {
                sb.append("-");
                sum = sum.add(BigInteger.ONE);
                sum = sum.divide(BigInteger.valueOf(5));
            }
        } while (!sum.equals(BigInteger.ZERO));
        System.out.println(sb.reverse());
    }

    public void part2(List<String> lines) {
        System.out.println("Start the blender!");
    }

    public static void main(String...args) {
        new Day25().run();
    }
}
