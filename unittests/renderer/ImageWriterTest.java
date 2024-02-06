package renderer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.Color;

class ImageWriterTest {

    ImageWriter writer = new ImageWriter("test1",800,500);
    @Test
    void imageWriterTest() {
        int ny = writer.getNy();
        int nx = writer.getNx();
        for (int i = 0; i < ny; i += 1)
            for (int j = 0; j < nx; j += 1) {
                writer.writePixel(j,i,new Color(0,127.5,127.5));
            }
        int interval = 50;
        for (int i = 0; i < ny; i += interval) {
            for (int j = 0; j < nx; j += 1) {
                writer.writePixel(j,i,new Color(255,0,0));
            }
        }
        for (int i = 0; i < ny; i += 1) {
            for (int j = 0; j < nx; j += interval) {
                writer.writePixel(j,i,new Color(255,0,0));
            }
        }
        writer.writeToImage();
    }
}