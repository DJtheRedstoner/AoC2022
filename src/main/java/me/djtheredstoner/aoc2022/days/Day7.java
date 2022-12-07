package me.djtheredstoner.aoc2022.days;

import me.djtheredstoner.aoc2022.DayBase;

import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Day7 implements DayBase {

    private static final Map<List<String>, List<FsEntry>> fs = new HashMap<>();

    public void init(List<String> lines) {
        LinkedList<String> cwd = new LinkedList<>();

        for (String line : lines) {
            if (line.startsWith("$ cd")) {
                var segment = line.substring("$ cd ".length());
                if (segment.equals("..")) {
                    cwd.removeLast();
                } else {
                    cwd.add(segment);
                }
            } else if (line.startsWith("$ ls")) {
                // ignore
            } else if (line.startsWith("dir ")) {
                String name = line.substring("dir ".length());
                var folderPath = new ArrayList<>(cwd);
                folderPath.add(name);
                fs.computeIfAbsent(new ArrayList<>(cwd), __ -> new ArrayList<>()).add(new FolderEntry(folderPath));
            } else {
                var parts = line.split(" ");
                fs.computeIfAbsent(new ArrayList<>(cwd), __ -> new ArrayList<>()).add(new FileEntry(parts[1], Integer.parseInt(parts[0])));
            }
        }

        //System.out.println(fs);
    }

    public void part1(List<String> lines) {
        int large = 0;

        for (Map.Entry<List<String>, List<FsEntry>> entry : fs.entrySet()) {
            for (FsEntry fsEntry : entry.getValue()) {
                if (fsEntry instanceof FolderEntry folder) {
                    int size = folder.size();
                    if (size < 100000) {
                        large += size;
                    }
                }
            }
        }

        System.out.println(large);
    }

    public void part2(List<String> lines) {
        int requiredSpace = 30000000 - (70000000 - new FolderEntry(List.of("/")).size());

        int smallest = 70000000;

        for (Map.Entry<List<String>, List<FsEntry>> entry : fs.entrySet()) {
            for (FsEntry fsEntry : entry.getValue()) {
                if (fsEntry instanceof FolderEntry folder) {
                    int size = folder.size();
                    if (size > requiredSpace && size < smallest) {
                        smallest = size;
                    }
                }
            }
        }

        System.out.println(smallest);
    }

    sealed interface FsEntry permits FileEntry, FolderEntry {
        int size();
    }

    record FileEntry(String name, int size) implements FsEntry {}

    record FolderEntry(List<String> path) implements FsEntry {
        @Override
        public int size() {
            int size = 0;
            for (FsEntry fsEntry : fs.get(path)) {
                size += fsEntry.size();
            }
            return size;
        }
    }

    public static void main(String...args) {
        new Day7().run();
    }
}
