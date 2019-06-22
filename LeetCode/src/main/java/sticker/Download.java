package sticker;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import org.junit.Test;

public class Download {

    public void saveImage(String imageUrl, String des) {
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, Paths.get(des));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveImage() {
        String prefix = "https://stickershop.line-scdn.net/stickershop/v1/sticker/";
        String suffix = "/IOS/sticker.png";
        long startId = 49832302, endId = 49832325;
        startId--;
        Random r = new Random();
        while (++startId <= endId) {
            try {
                Thread.sleep(100 + r.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String url = prefix + startId + suffix;
            String des = "D:/Data/Image/" + startId + ".png";
            saveImage(url, des);
        }
    }

    @Test
    public void test() throws IOException {
        // https://stickershop.line-scdn.net/stickershop/v1/sticker/28070614/IOS/sticker.png; 28070623
        // https://stickershop.line-scdn.net/stickershop/v1/sticker/28128864/IOS/sticker.png; 28128902
        // https://stickershop.line-scdn.net/stickershop/v1/sticker/22759785/IOS/sticker.png; 22759822
        // 56014, 56053
        
        saveImage();
    }
}
