package App.StaticUtils;

import java.awt.*;

public class ColorUtils {

    public static final Color TRANSPARENT = new Color(0,0,0,0);
    public static final Color CARBON = new Color(35,35,35);
    public static final Color CREAM = new Color(235, 227, 196);
    public static final Color DARK_PURPLE = new Color(50, 43, 56);
    public static final Color GREY = new Color(94, 94, 94);


    public static Color darkenColor(Color color, int darkenBy){
        return new Color(Math.max(color.getRed() - darkenBy, 0), Math.max(color.getGreen() - darkenBy, 0), Math.max(color.getBlue() - darkenBy, 0), color.getAlpha());
    }
    public static Color lightenColor(Color color, int lightenBy){
        return new Color(Math.min(color.getRed() + lightenBy, 255), Math.min(color.getGreen() + lightenBy, 255), Math.min(color.getBlue() + lightenBy, 255), color.getAlpha());
    }

    public static Color genRandomColor(){
        return new Color(((int) (Math.random() * 255)), ((int) (Math.random() * 255)), ((int) (Math.random() * 255)));
    }

}
