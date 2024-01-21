package renderer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.Color;

class ImageWriterTest {

    ImageWriter writer = new ImageWriter("test1",800,500);
    @Test
    void imageWriterTest() {
        for (int i = 0; i < 500; i += 1) {
            for (int j = 0; j < 800; j += 1) {
                if(i%50 == 0 || j % 50 == 0)
                    writer.writePixel(j,i,new Color(255,0,0));
                else writer.writePixel(j,i,new Color(0,127.5,127.5));
            }
        }
        writer.writeToImage();
    }
}