package App.StaticUtils;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FontUtils {

    // 0 - Font.TRUETYPE_FONT
    // 1 - Font.BOLD
    //
    //
    //

    private static final Map<Float, Font> fontCache = new HashMap<>();
    public static Font readFontFromFile(String filePath, float fontSize){
        if (fontCache.containsKey(fontSize)){
            return fontCache.get(fontSize);
        }
        try {
            FileInputStream fontStream = new FileInputStream(filePath);
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(fontSize);
            fontCache.put(fontSize, font);
            return font;
        } catch (IOException | FontFormatException e) {
            return new Font("Britannic Bold", Font.BOLD, 25);
        }
    }

    public static Font getJomhuriaFont(float fontSize){
        return readFontFromFile("resources/Jomhuria-Regular.ttf", fontSize);
    }
}
