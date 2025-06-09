package App.Panels.GuiUtil;

import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;

import javax.swing.*;
import java.awt.*;

public class ErrorNotificationPanel extends RoundedPanel {
    public ErrorNotificationPanel(String message, Dimension dim) {
        super(new Dimension(1,1), 15, new Color(0x646464));

        this.setBorderColor(ColorUtils.CARBON);
        this.setBorderWidth(2);

        JPanel contextPanel = new JPanel();

        contextPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 0, 20);

        ImagePanel errorWizard = ImagePanel.getErrorWizard();
        JLabel infoLabel = new JLabel("Click to dismiss!", SwingConstants.CENTER);
        errorWizard.setOpaque(false);
        errorWizard.setBackground(ColorUtils.TRANSPARENT);
        infoLabel.setFont(FontUtils.getJomhuriaFont(24));
        infoLabel.setForeground(ColorUtils.CREAM);
        contextPanel.add(errorWizard, gbc);
        gbc.gridy++;
        contextPanel.add(infoLabel, gbc);

        contextPanel.setOpaque(false);

        JTextArea errorTextArea = new JTextArea(){
            @Override
            public boolean contains(int x, int y) {
                return false;
            }
        };
        errorTextArea.setLineWrap(true);
        errorTextArea.setWrapStyleWord(true);
        errorTextArea.setEditable(false);
        errorTextArea.setFont(FontUtils.getJomhuriaFont(26));
        errorTextArea.setOpaque(false);
        errorTextArea.setBackground(ColorUtils.TRANSPARENT);

        errorTextArea.setForeground(ColorUtils.CREAM);

        errorTextArea.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // sizing is a bit wonky here...
        int halfWidth = dim.width / 2;
        int quarterWidth = dim.height / 4;
        int quarterHeight = dim.height / 4;

        errorTextArea.setPreferredSize(new Dimension(((int) ((halfWidth) * 0.6)), quarterHeight - 30));

        int contextPanelWidth = ((int) ((halfWidth) * 0.4));

        contextPanel.setPreferredSize(new Dimension(contextPanelWidth, quarterHeight));
        errorWizard.setPreferredSize(new Dimension(0, ((int) (quarterHeight * 0.8))));
        infoLabel.setPreferredSize(new Dimension(0, ((int) (quarterHeight * 0.2))));

        errorTextArea.setText(message);

        this.setBounds(quarterWidth, quarterHeight, halfWidth, quarterHeight);
//        this.setBackground(ColorUtils.GREY);
        this.setLayout(new BorderLayout());
        this.add(errorTextArea, BorderLayout.WEST);
        this.add(contextPanel, BorderLayout.EAST);
    }
}
