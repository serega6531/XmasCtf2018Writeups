package ru.serega6531;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main {

    private static Socket socket;
    private static BufferedReader reader;
    private static OutputStreamWriter writer;

    private static final TreeMap<Double, Double> values = new TreeMap<>(Double::compareTo);

    public static void main(String[] args) throws IOException {
        socket = new Socket("95.179.163.167", 14001);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new OutputStreamWriter(socket.getOutputStream());

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

        read = reader.readLine();
        System.out.println(read);
        read = read.substring("Given a random function defined in range (".length());
        read = read.substring(0, read.indexOf(')'));
        final String[] spl = read.split(", ");

        double left = Double.parseDouble(spl[0]);
        double right = Double.parseDouble(spl[1]);

        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());

        findMaximum(left, right, 500);

        double maxX = left, maxY = values.get(left);
        for (Map.Entry<Double, Double> ent : values.subMap(left, right).entrySet()) {
            if(ent.getValue() > maxY) {
                maxX = ent.getKey();
                maxY = ent.getValue();
            }
        }

        System.out.println("Found maximum: " + maxX + " - " + maxY);

        writer.write("2\n");
        writer.flush();

        writer.write(maxY + "\n");
        writer.flush();

        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
    }

    private static void findMaximum(double left, double right, int stepsLeft) throws IOException {
        System.out.println("Iterating over " + left + " to " + right);

        final XYChart chart = new XYChartBuilder()
                .width(2400)
                .height(600)
                .build();

        String read;

        for(double x = left; x <= right; x += (right - left) / (double) 100) {
            if(stepsLeft == 1) {
                System.out.println("END");
                return;
            }

            writer.write("1\n");
            writer.flush();

            reader.skip(15);  // Enter a float:

            writer.write(x + "\n");
            writer.flush();

            read = reader.readLine();  //f(...) = value
            System.out.println(read);

            read = read.substring(read.indexOf('=') + 1);
            double y = Double.parseDouble(read);

            values.put(x, y);
            stepsLeft--;

            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
        }

        double maxX = left, maxY = values.get(left);
        final SortedMap<Double, Double> subMap = values.subMap(left, right);
        for (Map.Entry<Double, Double> ent : subMap.entrySet()) {
            if(ent.getValue() > maxY) {
                maxX = ent.getKey();
                maxY = ent.getValue();
            }
        }

        System.out.println("Found maximum: " + maxX + " - " + maxY);

        chart.addSeries("Values",
                subMap.keySet().stream().mapToDouble(a -> a).toArray(),
                subMap.values().stream().mapToDouble(a -> a).toArray());

        BitmapEncoder.saveBitmap(chart, "values-" + left + "-" + right, BitmapEncoder.BitmapFormat.PNG);

        double newLeft = maxX - (right - left) / 100;
        double newRight = maxX + (right - left) / 100;
        findMaximum(newLeft, newRight, stepsLeft);
    }
}
