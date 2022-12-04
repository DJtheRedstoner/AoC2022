package me.djtheredstoner.aoc2022;

import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Util {

    public static List<String> getLines(int day) {
        var path = Path.of("inputs", day + ".txt");
        if (!Files.exists(path)) {
            downloadInput(day, path);
        }
        try {
            return Files.readAllLines(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void downloadInput(int day, Path path) {
        try {
            String session = Files.readString(Path.of("session.txt"));

            URI uri = URI.create("https://adventofcode.com/2022/day/" + day + "/input");
            var connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestProperty("Cookie", "session=" + session);
            connection.connect();

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("got bad response " + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            Files.copy(connection.getInputStream(), path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
