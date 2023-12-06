package net.bobnar.marketplace.loaderAgent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class TestBase {
    protected String loadTestResource(String name) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("src/test/resources/" + name);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        return data;
    }
}
