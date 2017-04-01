package stega.jj.bldg5.steganography;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.*;

public class Decode {
    // https://github.com/subc/steganography/blob/master/steganography/steganography.py
    public String d(Bitmap b) {
        int x = b.getWidth();
        int y = b.getHeight();
        int counter = 0;
        List<Integer> result = new ArrayList<Integer>() {};

        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                int pixel = b.getPixel(i, j);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);

                if (redValue % 8 == 1 &&
                        blueValue % 8 == 1 &&
                        greenValue % 8 == 1) {
                    result.add(counter);
                }

                counter++;
                counter = counter % 16;
            }
        }

        List<String> hex = new ArrayList<String>() {};
        for (int k = 0; k < result.size(); k++) {
            // radix hex
            String hexify = Integer.toString(result.get(k), 16);
            hex.add(hexify);
        }

        String hexAll = TextUtils.join("", hex);
        byte[] bytes = new byte[0];

        try {
            bytes = Hex.decodeHex(hexAll.toCharArray());
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException | DecoderException e) {
            e.printStackTrace();
            return "Exception!";
        }
    }

    // http://stackoverflow.com/questions/12039341/hex-to-string-in-java-performance-is-too-slow
    public static String hexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        char[] hexData = hex.toCharArray();
        for (int count = 0; count < hexData.length - 1; count += 2) {
            int firstDigit = Character.digit(hexData[count], 16);
            int lastDigit = Character.digit(hexData[count + 1], 16);
            int decimal = firstDigit * 16 + lastDigit;
            sb.append((char)decimal);
        }
        return sb.toString();
    }
}
