package ru.serega6531;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static List<Integer> list;
    private static URL currentNumberUrl;

    static {
        try {
            currentNumberUrl = new URL("http://199.247.6.180:12000/?guess=1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static int getCurrentNumber() throws IOException {
        String response = sendGetRequest(currentNumberUrl);

        //print result
        String sub = response.substring(response.indexOf("The Monolith desired:<br>") + "The Monolith desired:<br>".length());
        sub = sub.substring(0, sub.indexOf("<br>"));

        return Integer.valueOf(sub);
    }

    private static void findAllNumbers() throws IOException {
        list = new ArrayList<>();

        while (true) {
            int desired = getCurrentNumber();

            if(list.contains(desired)) {
                System.out.printf("Desired %d found %d -> %d\n", desired, list.indexOf(desired), list.size());
                return;
            }
            list.add(desired);
        }
    }

    private static String sendGetRequest(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        conn.setRequestProperty("Cookie", "PHPSESSID=866f774590bee3048943be33baf639a5");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static void getFlag() throws IOException {
        /*list = Files.lines(new File("numbers.txt").toPath())
                .map(Integer::parseInt)
                .collect(Collectors.toList());*/

        int currentNumber = getCurrentNumber();
        int currentPos = list.indexOf(currentNumber);

        while (true) {
            if(currentPos == list.size() - 1) {
                currentPos = 0;
            } else {
                currentPos++;
            }

            currentNumber = list.get(currentPos);
            URL inputNumberUrl = new URL("http://199.247.6.180:12000/?guess=" + currentNumber);
            final String answer = sendGetRequest(inputNumberUrl);
            if (answer.contains("X-MAS")) {
                System.out.println(answer);
                return;
            }

        }
    }

    public static void main(String[] args) throws IOException {
        findAllNumbers();
        getFlag();
    }
}
