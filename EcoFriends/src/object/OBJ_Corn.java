package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Corn extends SuperObject {
    public OBJ_Corn () {
        super(0);
        name = "corn";
        try {                                                                                                           //reads an image file name from directory
            image = ImageIO.read(getClass().getResourceAsStream("/player/images/corn.png"));                      //catcher variable is a BufferedImage that stores the image in directory
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
