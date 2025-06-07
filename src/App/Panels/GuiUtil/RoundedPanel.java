package App.Panels.GuiUtil;

import App.StaticUtils.ColorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {

    private final Dimension panelDim;
    private final int roundingValue;

    public RoundedPanel(Dimension panelDim, int roundingValue, Color color) {
        this.panelDim = panelDim;
        this.roundingValue = roundingValue;
        this.setOpaque(false);
        this.setBackground(color);
        this.setPreferredSize(panelDim);
    }
    public RoundedPanel(Dimension panelDim, int roundingValue) {
        this.panelDim = panelDim;
        this.roundingValue = roundingValue;
        this.setOpaque(false);
        this.setBackground(ColorUtils.TRANSPARENT);
        this.setPreferredSize(panelDim);
    }

    public RoundedPanel() {
        panelDim = null;
        roundingValue = 0;
    }

    public RoundedPanel(int roundingValue) {
        panelDim = null;
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
            graphics.dispose();
    }
}
