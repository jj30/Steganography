package bldg5.jj.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.commons.codec.binary.Hex;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Encode {
    public Bitmap encoded(Bitmap unencoded, String message) {
        // input has to be 255 RGB
        int yHeight = unencoded.getHeight();
        int xWidth = unencoded.getWidth();

        Bitmap bitmapOutput = unencoded.copy(unencoded.getConfig(), true);

        try {
            int base = 0;
            byte[] bMessage = message.getBytes();
            char[] hex = Hex.encodeHex(bMessage);
            List<Integer> writeParam = new ArrayList<Integer>(){};

            for (char x : hex) {
                Integer toBeAdded = Integer.parseInt(String.valueOf(x), 16) + base;
                writeParam.add(toBeAdded);
                base += 16;
            }

            int counter = 0;
            for (int j = 0; j < yHeight; j++) {
                for (int i = 0; i < xWidth; i++) {
                    int pixel = unencoded.getPixel(i, j);

                    int redValue = Color.red(pixel);
                    int greenValue = Color.green(pixel);
                    int blueValue = Color.blue(pixel);

                    int[] normalized = normalize(redValue, greenValue, blueValue);

                    redValue = normalized[0];
                    greenValue = normalized[1];
                    blueValue = normalized[2];

                    if (writeParam.contains(counter)) {
                        redValue = modify(redValue);
                        greenValue = modify(greenValue);
                        blueValue = modify(blueValue);
                    }

                    bitmapOutput.setPixel(i, j, Color.rgb(redValue, greenValue, blueValue));
                    counter++;
                }
            }
        } catch(Exception ex) {
            Log.i("Steg", ex.getMessage());
        }

        return bitmapOutput;
    }

    private int modify(int rgb) {
        if (rgb >= 128) {
            for (int x = 0; x < 9; x++) {
                if (rgb % 8 == 1)
                    return rgb;
                rgb--;
            }
        } else {
            for (int x = 0; x < 9; x++) {
                if (rgb % 8 == 1)
                    return rgb;
                rgb++;
            }
        }

        return rgb;
    }

    private int[] normalize(int redValue, int greenValue, int blueValue) {
        // "normalize"
        if (redValue % 8 == 1 &&
                greenValue % 8 == 1 &&
                blueValue % 8 == 1) {
            final Random rand = new Random();
            int seed = rand.nextInt(3) + 1;

            switch (seed) {
                case 1:
                    redValue = normalizeInner(redValue);
                    break;
                case 2:
                    greenValue = normalizeInner(greenValue);
                    break;
                case 3:
                    blueValue = normalizeInner(blueValue);
                    break;
            }
        }

        return new int[] { redValue, greenValue, blueValue };
    }

    private int normalizeInner(int rgb) {
        if (rgb >= 128) {
            rgb--;
        } else {
            rgb++;
        }

        return rgb;
    }
}

