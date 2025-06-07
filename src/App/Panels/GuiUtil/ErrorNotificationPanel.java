package App.Panels.GuiUtil;

import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;

import javax.swing.*;
import java.awt.*;

public class ErrorNotificationPanel extends RoundedPanel {
    public ErrorNotificationPanel(String message, Dimension dim) {
        super(new Dimension(1,1), 15, ColorUtils.CARBON);

        JTextArea errorTextArea = new JTextArea(){
            @Override
            public boolean contains(int x, int y) {
                return false;
            }
        };
        errorTextArea.setLineWrap(true);
        errorTextArea.setWrapStyleWord(true);
        errorTextArea.setEditable(false);
        errorTextArea.setFont(FontUtils.getJomhuriaFont(20));
        errorTextArea.setOpaque(false);
        errorTextArea.setBackground(ColorUtils.TRANSPARENT);

        errorTextArea.setForeground(new Color(195, 195, 195));

        errorTextArea.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // sizing is a bit wonky here...
        int halfWidth = dim.width / 2;
        int quarterWidth = dim.height / 4;
        int quarterHeight = dim.height / 4;
        this.setBounds(quarterWidth, quarterHeight, halfWidth, quarterHeight);
        this.setBackground(ColorUtils.CARBON);
        this.setLayout(new BorderLayout());
        errorTextArea.setText(message);
        this.add(errorTextArea, BorderLayout.CENTER);
    }
}
