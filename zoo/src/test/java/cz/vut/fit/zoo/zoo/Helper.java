package cz.vut.fit.zoo.zoo;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

abstract class Helper {
    static void storeImage(byte[] image, String id) throws IOException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream("retrieved" + id + "-photo.jpeg"));
            out.write(image);
        } finally {
            if (out != null) out.close();
        }
    }
}
