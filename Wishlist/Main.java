package ru.serega6531;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final int TOP_OFFSET = 90;
    private static final int LEFT_OFFSET = 0;
    private static final int LETTER_WIDTH = 90;
    private static final int LETTER_HEIGHT = 90;

    private static final int MAX_LETTERS_AT_ROW = 46;

    private static int countBlackPixels(BufferedImage letter) {
        int count = 0;

        for (int x = 0; x < LETTER_WIDTH; x++) {
            for (int y = 0; y < LETTER_HEIGHT; y++) {
                int rgb = letter.getRGB(x, y);
                int r = (rgb & 0xFF000000) >> 24;
                int g = (rgb & 0x00FF0000) >> 16;
                int b = (rgb & 0x0000FF00) >> 8;

                if (r + g + b < 15) {
                    count++;
                }
            }
        }

        return count;
    }

    public static void main(String[] args) throws IOException {
        File f = new File("wishlist.png");
        BufferedImage image = ImageIO.read(f);

        int rows = (image.getHeight() - TOP_OFFSET) / LETTER_HEIGHT;
        //File saveDir = new File("letters");
        //saveDir.mkdir();

        Set<Integer> pixelsCount = new HashSet<>();
        Map<Integer, Integer> pixelsByPos = new HashMap<>();  // pos = 1000 * x + y

        for (int x = 0; x < MAX_LETTERS_AT_ROW; x++) {
            for (int y = 0; y < rows; y += 2) {
                final BufferedImage letter = image.getSubimage(
                        LEFT_OFFSET + x * LETTER_WIDTH,
                        TOP_OFFSET + y * LETTER_HEIGHT,
                        LETTER_WIDTH,
                        LETTER_HEIGHT);

                int blackPixels = countBlackPixels(letter);
                //final File blackPixelsDir = new File(saveDir, "" + blackPixels);
                //blackPixelsDir.mkdir();
                //ImageIO.write(letter, "png", new File(blackPixelsDir, "letter" + x + "-" + y + ".png"));

                pixelsCount.add(blackPixels);
                pixelsByPos.put(x * 1000 + y, blackPixels);
            }
        }

        Map<Integer, Character> charsMap = new HashMap<>();
        List<Integer> pixelsCountSorted = pixelsCount.stream().sorted().collect(Collectors.toList());
        char currentChar = 'A';

        for (int i = 0; i < pixelsCountSorted.size(); i++) {
            int count = pixelsCountSorted.get(i);

            if (count != 0) {
                charsMap.put(count, currentChar);
                currentChar++;
            } else {
                charsMap.put(0, ' ');
            }
        }

        for (int y = 0; y < rows; y += 2) {
            StringBuilder builder = new StringBuilder(MAX_LETTERS_AT_ROW);

            for (int x = 0; x < MAX_LETTERS_AT_ROW; x++) {
                int pixels = pixelsByPos.get(x * 1000 + y);
                char c = charsMap.get(pixels);

                builder.append(c);
            }

            System.out.println(builder.toString().trim());
        }
    }
}
