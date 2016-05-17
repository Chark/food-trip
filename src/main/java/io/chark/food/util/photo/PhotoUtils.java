package io.chark.food.util.photo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PhotoUtils {

    /**
     * Get byte array of given path of the photo.
     *
     * @param path  path to the photo
     * @return      byte array of the photo
     */
    public static byte[] getImageBytes(String path) {
        Resource resource = new ClassPathResource(path);
        try {
            File file = resource.getFile();
            byte[] imageBytes = new byte[(int) file.length()];
            DataInputStream dataIs = new DataInputStream(new FileInputStream(file));
            dataIs.readFully(imageBytes);

            return imageBytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
