package App.Panels;

import App.StaticUtils.FontUtils;

import javax.swing.*;
import java.awt.*;

public class PlaceHolderPanel extends JPanel implements App.Util.Iterable {
    private int index;

    public PlaceHolderPanel(Dimension dimension, int index) {

        setIndex(index);
        this.setLayout(new GridBagLayout());

        RoundedPanel roundedPanel = new RoundedPanel(new Dimension(dimension.width - 10, dimension.height - 10), 10, new Color(94, 94, 94));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 1, 0, 0);

        roundedPanel.setLayout(new GridBagLayout());
        JLabel comp = new JLabel("+", SwingConstants.CENTER);
        comp.setForeground(new Color(235, 227, 196));
        comp.setFont(FontUtils.getJomhuriaFont(40));
        roundedPanel.add(comp);

        this.setPreferredSize(dimension);
        this.add(roundedPanel, gbc);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
