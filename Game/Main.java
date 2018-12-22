package ru.serega6531;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("95.179.163.167", 14002);
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

        int[] piles = new int[15];
        int xorSum;

        while (true) {
            read = reader.readLine();
            System.out.println(read);

            read = read.substring("Current state of the game: [".length());
            read = read.substring(0, read.length() - 1);

            final String[] spl = read.split(", ");
            xorSum = 0;
            for (int i = 0; i < spl.length; i++) {
                piles[i] = Integer.parseInt(spl[i]);
                xorSum ^= piles[i];
            }
            System.out.println("Sum = " + xorSum);

            System.out.println(reader.readLine());
            System.out.println(reader.readLine());

            outer:
            for (int i = 0; i < 15; i++) {
                for (int j = 1; j <= piles[i]; j++) {
                    int newSum = 0;
                    for (int k = 0; k < spl.length; k++) {
                        if (i != k) {
                            newSum ^= piles[k];
                        } else {
                            newSum ^= piles[k] - j;
                        }
                    }

                    if (newSum != 0) {
                        continue;
                    }

                    System.out.println("Sum = " + xorSum);

                    System.out.println(i + " " + j);
                    writer.write(i + "\n");
                    writer.flush();

                    System.out.println(reader.readLine());
                    writer.write(j + "\n");

                    break outer;
                }
            }

            writer.flush();

            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());


        }
    }
}
