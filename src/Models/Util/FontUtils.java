package Models.Util;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class FontUtils {

    // 0 - Font.TRUETYPE_FONT
    // 1 - Font.BOLD
    //
    //
    //
    public static Font readFontFromFile(String filePath, float fontSize){
        try {
            FileInputStream fontStream = new FileInputStream(filePath);
            return Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(fontSize);
        } catch (IOException | FontFormatException e) {
            return new Font("Britannic Bold", Font.BOLD, 25);
        }
    }
}
