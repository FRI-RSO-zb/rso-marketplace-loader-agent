package net.bobnar.marketplace.loaderAgent;

import net.bobnar.marketplace.loaderAgent.services.loader.LoaderBase;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class TestBase {
    protected String loadTestResource(String name) throws IOException {
        ClassLoader classLoader = LoaderBase.class.getClassLoader();
        InputStream fis = classLoader.getResourceAsStream(name);
//        FileInputStream fis = new FileInputStream("src/main/resources/samples/" + name);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        return data;
    }
}
