package ru.serega6531;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;

public class Main {

    private static final String url = "https://oeis.org/search?fmt=json&q=%s&start=0";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("199.247.6.180", 14003);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());

        System.out.println(reader.readLine());
        String read = reader.readLine();
        System.out.println(read);
        read = read.substring("Give a string X such that md5(X).hexdigest()[:5]=".length());
        String start = read.substring(0, read.indexOf('.'));

        while (true) {
            String str = RandomStringUtils.random(10, true, true);
            String hash = DigestUtils.md5Hex(str);

            if (hash.substring(0, start.length()).equals(start)) {
                System.out.println("Found " + hash);
                writer.write(str + '\n');
                writer.flush();
                break;
            }
        }

        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());

        while (true) {
            System.out.println(reader.readLine());   //question number n
            System.out.println(reader.readLine());

            String sequence = reader.readLine();
            System.out.println(sequence);

            System.out.println(reader.readLine());
            System.out.println(reader.readLine());

            sequence = sequence.substring(1, sequence.length() - 1).replaceAll(" ", "");
            System.out.println(sequence);

            final JsonNode jsonNode = objectMapper.readTree(new URL(String.format(url, sequence)));
            final String recognised = jsonNode.get("results").get(0).get("data").asText();
            System.out.println(recognised);

            String nextValue = recognised.substring(sequence.length() + 1);
            nextValue = nextValue.substring(0, nextValue.indexOf(','));
            System.out.println(nextValue);

            writer.write(nextValue + "\n");
            writer.flush();

            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
        }
    }
}
