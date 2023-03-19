package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {

    // FUNCTION FOR IMPROVED RENDERING EFFICIENCY
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {

        // CREATE NEW INSTANCE FOR SCALED IMAGE, DRAW THE RIGHT SCALE INTO IT
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }
}
