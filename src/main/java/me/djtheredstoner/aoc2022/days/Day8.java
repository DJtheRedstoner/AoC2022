package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.util.Arrays;
import java.util.List;

public class Day8 implements DayBase {

    private int[][] trees;

    public void init(List<String> lines) {
        trees = new int[lines.size()][];

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            trees[i] = new int[line.length()];
            for (int j = 0; j < line.length(); j++) {
                trees[i][j] = Integer.parseInt(line.substring(j, j + 1));
            }
        }
    }

    public void part1(List<String> lines) {
        int visible = 0;

        for (int i = 0; i < trees.length; i++) {
            int[] row = trees[i];

            StringBuilder s = new StringBuilder();

            for (int j = 0; j < row.length; j++) {

                //int height = row[j];
                //if (Arrays.stream(row).limit(j).filter(h -> h >= height).findAny().isEmpty()
                //    || Arrays.stream(row).skip(j).filter(h -> h >= height).findAny().isEmpty()) {
                //    visible++;
                //    System.out.printf("%d,%d is visible from sides\n", j, i);
                //    continue;
                //}
                //boolean v = true;
                //
                //for (int x = 0; x < j; x++) {
                //    if (trees[i][x] >= height) {
                //        v = false;
                //        break;
                //    }
                //}
                //if (!v) {
                //    visible++;
                //    continue;
                //}
                //
                //for (int x = j + 1; x < row.length; x++) {
                //    if (trees[i][x] >= height) {
                //        v = false;
                //        break;
                //    }
                //}
                //if (!v) {
                //    visible++;
                //    continue;
                //}
                //
                //for (int y = 0; y < i; y++) {
                //    if (trees[y][j] >= height) {
                //        v = false;
                //        break;
                //    }
                //}
                //if (!v) {
                //    visible++;
                //    continue;
                //}
                //
                //for (int y = i + 1; y < trees.length; y++) {
                //    if (trees[y][j] >= height) {
                //        v = false;
                //        break;
                //    }
                //}
                //if (!v) {
                //    visible++;
                //}

                if (visible(row, i, j)) {
                    s.append("X");
                    visible++;
                } else {
                    s.append(" ");
                }
            }

            System.out.println(s);
        }

        System.out.println(visible);
    }

    private boolean visible(int[] row, int i, int j) {
        int height = row[j];
        //if (Arrays.stream(row).limit(j).filter(h -> h >= height).findAny().isEmpty()
        //    || Arrays.stream(row).skip(j).filter(h -> h >= height).findAny().isEmpty()) {
        //    visible++;
        //    System.out.printf("%d,%d is visible from sides\n", j, i);
        //    continue;
        //}
        boolean v = true;

        for (int x = 0; x < j; x++) {
            if (trees[i][x] >= height) {
                v = false;
                break;
            }
        }
        if (v) {
            return true;
        }

        v = true;

        for (int x = j + 1; x < row.length; x++) {
            if (trees[i][x] >= height) {
                v = false;
                break;
            }
        }
        if (v) {
            return true;
        }

        v = true;

        for (int y = 0; y < i; y++) {
            if (trees[y][j] >= height) {
                v = false;
                break;
            }
        }
        if (v) {
            return true;
        }

        v = true;

        for (int y = i + 1; y < trees.length; y++) {
            if (trees[y][j] >= height) {
                v = false;
                break;
            }
        }
        if (v) {
            return true;
        }
        return false;
    }

    public void part2(List<String> lines) {
        int maxScore = 0;

        for (int i = 0; i < trees.length; i++) {
            int[] row = trees[i];

            for (int j = 0; j < row.length; j++) {
                int score = scenicScore(i, j);
                if (score > maxScore) {
                    maxScore = score;
                }
            }
        }

        System.out.println(maxScore);
    }

    private int scenicScore(int row, int col) {
        int height = trees[row][col];

        int up, down, left, right;
        up = down = left = right = 0;

        for (int y = row - 1; y >= 0; y--) {
            up++;
            if (trees[y][col] >= height) break;
        }

        for (int y = row + 1; y < trees.length; y++) {
            down++;
            if (trees[y][col] >= height) break;
        }

        for (int x = col - 1; x >= 0; x--) {
            left++;
            if (trees[row][x] >= height) break;
        }

        for (int x = col + 1; x < trees[row].length; x++) {
            right++;
            if (trees[row][x] >= height) break;
        }

        return up * down * left * right;
    }

    public static void main(String...args) {
        new Day8().run();
    }
}
