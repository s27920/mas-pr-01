package App.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {

    private static final Map<String, BufferedImage> imageCache = new HashMap<>();
    private final BufferedImage image;
    private int x1, x2, y1, y2;
    private boolean clip;
    private ImagePanel(String imagePath) {
        this.image = readImageFromFile(imagePath);
        if(image != null){
            this.x1 = 0;
            this.x2 = image.getWidth();
            this.y1 = 0;
            this.y2 = image.getHeight();
        }
    }

    public static ImagePanel getGuildMemberIcon(int iconNum){
        return new ImagePanel(String.format("resources/icon%s.png", iconNum));
    }

    public static ImagePanel getGuildIcon(int iconNum){
        return new ImagePanel(String.format("resources/guild-icon%s.png", iconNum));
    }

    public static ImagePanel getWorldMap(){
        return new ImagePanel("resources/world-map.jpg");
    }

    private BufferedImage readImageFromFile(String path){
        if (imageCache.containsKey(path)){
            return imageCache.get(path);
        }
        try {
            BufferedImage readImage = ImageIO.read(new File(path));
            imageCache.put(path, readImage);
            return readImage;
        }catch (IOException e) {
            return null;
        }
    }

    public void setClip(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.clip = true;
        repaint();
    }

    public void disableClipping() {
        this.clip = false;
        repaint();
    }

    public int getOriginalImageWidth() {
        return image != null ? image.getWidth() : 0;
    }

    public int getOriginalImageHeight() {
        return image != null ? image.getHeight() : 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            if (clip){
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), x1, y1, x2, y2, this);
            }else{
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        }
    }
}