package App.Panels.GuiUtil;

import App.StaticUtils.ColorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {

    private final int roundingValue;
    private boolean hasBorder;
    private int borderWidth;
    private Color borderColor;

    public RoundedPanel(Dimension panelDim, int roundingValue, Color color) {
        this.roundingValue = roundingValue;
        this.setOpaque(false);
        this.setBackground(color);
        this.setPreferredSize(panelDim);
    }
    public RoundedPanel(Dimension panelDim, int roundingValue) {
        this.roundingValue = roundingValue;
        this.setOpaque(false);
        this.setBackground(ColorUtils.TRANSPARENT);
        this.setPreferredSize(panelDim);
    }

    public RoundedPanel() {
        roundingValue = 0;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        this.hasBorder = true;
        repaint();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        this.hasBorder = true;
        repaint();
    }

    public RoundedPanel(int roundingValue) {
        this.setBackground(ColorUtils.TRANSPARENT);
        this.roundingValue = roundingValue;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        RoundRectangle2D mainPanel = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), roundingValue, roundingValue);

        graphics.setColor(getBackground());
        graphics.fill(mainPanel);

        if (hasBorder && borderWidth > 0) {
            graphics.setColor(borderColor);
            graphics.setStroke(new BasicStroke(borderWidth));
            float offset = borderWidth / 2.0f;
            RoundRectangle2D borderRect = new RoundRectangle2D.Float(offset, offset,getWidth() - borderWidth,getHeight() - borderWidth,roundingValue, roundingValue
            );
            graphics.draw(borderRect);
        }

        graphics.dispose();
    }
}
