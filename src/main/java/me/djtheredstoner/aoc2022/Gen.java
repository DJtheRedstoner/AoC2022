package me.djtheredstoner.aoc2022;

import java.nio.file.Files;
import java.nio.file.Path;

public class Gen {

    public static void main(String[] args) throws Exception {
        for (int day = 1; day <= 25; day++) {
            String file = """
                package me.djtheredstoner.aoc2022.days;
                
                import me.djtheredstoner.aoc2022.DayBase;
                
                import java.util.List;
                
                public class Day%% implements DayBase {
                    
                    public void init(List<String> lines) {
                    
                    }
                    
                    public void part1(List<String> lines) {
                    
                    }
                    
                    public void part2(List<String> lines) {
                    
                    }
                    
                    public static void main(String...args) {
                        new Day%%().run();
                    }
                }
                """.replace("%%", day + "");

            Files.writeString(Path.of("src", "main", "java", "me", "djtheredstoner", "aoc2022", "days", "Day" + day + ".java"), file);
        }
    }

}
