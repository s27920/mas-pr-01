package Models.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private int x1, x2, y1, y2;
    private boolean clip;
    public ImagePanel(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
            if(image != null){
                this.x1 = 0;
                this.x2 = image.getWidth();
                this.y1 = 0;
                this.y2 = image.getHeight();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Icon image panel
    public ImagePanel(int iconNum){
        try {
            image = ImageIO.read(new File(String.format("resources/icon%s.png", iconNum)));
            if(image != null){
                this.x1 = 0;
                this.x2 = image.getWidth();
                this.y1 = 0;
                this.y2 = image.getHeight();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
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
//                System.out.printf("Panel actual size: %d x %d\n", this.getWidth(), this.getHeight());
//                System.out.printf("Drawing from source rect: (%d,%d) to (%d,%d)\n", x1, y1, x2, y2);
//                System.out.printf("Drawing to dest rect: (0,0) to (%d,%d)\n", this.getWidth(), this.getHeight());

                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), x1, y1, x2, y2, this);
            }else{
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        }
    }
}