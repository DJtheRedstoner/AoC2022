package me.djtheredstoner.aoc2022.days.alternate;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import me.djtheredstoner.aoc2022.Util;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day7Jimfs {

    private static final FileSystem fs = Jimfs.newFileSystem(Configuration.unix());

    public static void main(String[] args) throws IOException {
        List<String> lines = Util.getLines(7);

        Path cwd = fs.getPath("");

        for (String line : lines) {
            if (line.startsWith("$ cd")) {
                var segment = line.substring("$ cd ".length());
                if (segment.equals("..")) {
                    cwd = cwd.getParent();
                } else {
                    cwd = cwd.resolve(segment);
                }
            } else if (line.startsWith("$ ls")) {
                // ignore
            } else if (line.startsWith("dir ")) {
                String name = line.substring("dir ".length());

                Path path = cwd.resolve(name);
                Files.createDirectories(path);
            } else {
                var parts = line.split(" ");

                Path path = cwd.resolve(parts[1]);

                Files.write(path, new byte[Integer.parseInt(parts[0])]);
            }
        }

        part1();
        part2();
    }

    private static void part1() throws IOException {
        System.out.println(Files.walk(fs.getPath("/"))
            .filter(Files::isDirectory)
            .mapToLong(Day7Jimfs::size)
            .filter(i -> i < 100000)
            .sum());
    }

    private static void part2() throws IOException {
        long usedSpace = size(fs.getPath("/"));

        long requiredSpace = 30000000 - (70000000 - usedSpace);

        System.out.println(Files.walk(fs.getPath("/"))
            .filter(Files::isDirectory)
            .mapToLong(Day7Jimfs::size)
            .filter(i -> i > requiredSpace)
            .min().getAsLong());
    }

    private static long size(Path path) {
        try {
            return Files.walk(path).filter(Files::isRegularFile).filter(Files::isRegularFile).mapToLong(p1 -> {
                try {
                    return Files.size(p1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).sum();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
