package App.Panels.Components;

import App.Models.Guild.GuildMember;
import App.Models.Mission.Mission;
import App.Panels.GuiUtil.ImagePanel;
import App.Panels.GuiUtil.RoundedPanel;
import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GuildMemberHeaderPanel extends JPanel {
    private final int LABEL_HEIGHT = 40;

    private final JPanel wrapperPanel;
    private final JPanel glue;
    private final JPanel glueTwoElectricBoogaloo;
    private final JLabel wizardNameLabel;
    private final JLabel wizardGuildLabel;
    private final RoundedPanel speedUpPanel;
    private final GridBagConstraints gbc;
    private final ImagePanel speedUpBtn;

    private ImagePanel guildLogoPanel;
    private final Runnable goBackCallback;

    public GuildMemberHeaderPanel(Runnable goBackCallback) {
        this.goBackCallback = goBackCallback;

        this.setLayout(new GridBagLayout());
        this.setBackground(ColorUtils.CARBON);

        wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(ColorUtils.TRANSPARENT);

        wizardNameLabel = new JLabel();
        wizardGuildLabel = new JLabel();

        wizardGuildLabel.setForeground(ColorUtils.CREAM);
        wizardNameLabel.setForeground(ColorUtils.CREAM);

        wizardNameLabel.setFont(FontUtils.getJomhuriaFont(32));
        wizardGuildLabel.setFont(FontUtils.getJomhuriaFont(16));

        wizardGuildLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wizardNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        wrapperPanel.add(wizardNameLabel, BorderLayout.NORTH);
        wrapperPanel.add(wizardGuildLabel, BorderLayout.SOUTH);

        glue = new JPanel();
        glueTwoElectricBoogaloo = new JPanel();
        speedUpPanel = new RoundedPanel(5);

        speedUpPanel.setLayout(new GridBagLayout());
        if (Mission.isFlippedMissionTimeDevScalar()){
            speedUpPanel.setBorderColor(ColorUtils.CARBON);
            speedUpPanel.setBackground(ColorUtils.darkenColor(ColorUtils.DARK_GREY, 10));
        }else {
            speedUpPanel.setBackground(ColorUtils.DARK_GREY);
            speedUpPanel.setBorderColor(ColorUtils.DARK_GREY);
        }
        speedUpPanel.setBorderWidth(2);
        speedUpPanel.setOpaque(false);
        speedUpPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints speedUpGbc = new GridBagConstraints();
        speedUpGbc.gridy = 0;
        speedUpGbc.gridx = 0;
        speedUpGbc.fill = GridBagConstraints.NONE;
        speedUpGbc.weighty = 0.0;
        speedUpGbc.weightx = 0.0;
        speedUpGbc.anchor = GridBagConstraints.CENTER;
        speedUpPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Mission.flipMissionTimeDevScalar();
                if (Mission.isFlippedMissionTimeDevScalar()){
                    speedUpPanel.setBorderColor(ColorUtils.CARBON);
                    speedUpPanel.setBackground(ColorUtils.darkenColor(speedUpPanel.getBackground(), 15));
                }else {
                    speedUpPanel.setBorderColor(ColorUtils.DARK_GREY);
                    speedUpPanel.setBackground(ColorUtils.lightenColor(speedUpPanel.getBackground(), 15));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                speedUpPanel.setBackground(ColorUtils.lightenColor(speedUpPanel.getBackground(), 25));
                speedUpBtn.setBackground(ColorUtils.lightenColor(speedUpBtn.getBackground(), 25));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                speedUpPanel.setBackground(ColorUtils.darkenColor(speedUpPanel.getBackground(), 25));
                speedUpBtn.setBackground(ColorUtils.darkenColor(speedUpBtn.getBackground(), 25));
            }
        });

        speedUpBtn = ImagePanel.getFastForwardButton();
        speedUpBtn.setBackground(ColorUtils.TRANSPARENT);
        speedUpPanel.add(speedUpBtn, speedUpGbc);

        glue.setBackground(ColorUtils.TRANSPARENT);
        glueTwoElectricBoogaloo.setBackground(ColorUtils.TRANSPARENT);

        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.VERTICAL;

        this.add(wrapperPanel, gbc);

        gbc.gridx++;
        this.add(glue, gbc);
        gbc.gridx++;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        this.add(speedUpPanel, gbc);
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx++;
        this.add(glueTwoElectricBoogaloo, gbc);

        gbc.gridx++;
        gbc.insets = new Insets(1, 1, 1, 1);
    }

    public void setLoggedInMember(GuildMember member) {

        if (guildLogoPanel != null) {
            this.remove(guildLogoPanel);
        }

        guildLogoPanel = ImagePanel.getGuildIcon(member.getGuild().getChosenIcon());
        guildLogoPanel.setPreferredSize(new Dimension(LABEL_HEIGHT - 2, LABEL_HEIGHT - 2));
        guildLogoPanel.setBackground(ColorUtils.TRANSPARENT);
        guildLogoPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        guildLogoPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (goBackCallback != null) {
                    goBackCallback.run();
                }
            }
        });
        this.add(guildLogoPanel, gbc);
        wizardNameLabel.setText(member.getName());
        wizardGuildLabel.setText(String.format("Guild: %s", member.getGuild().getGuildName()));
        this.revalidate();
        this.repaint();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        int labelWidth = width / 5;

        wrapperPanel.setPreferredSize(new Dimension(labelWidth, LABEL_HEIGHT));

        // has to account for the guild text wrapper panel size (label width) and half the speedUpPanel width
        int glueOneWidth = width / 2 - labelWidth * 3 / 2;
        int totalGlueWidth = width - labelWidth * 2 - LABEL_HEIGHT;
        int glueTwoElectricBoogalooWidth = totalGlueWidth - glueOneWidth;

        glue.setPreferredSize(new Dimension(glueOneWidth, LABEL_HEIGHT));
        glueTwoElectricBoogaloo.setPreferredSize(new Dimension(glueTwoElectricBoogalooWidth, LABEL_HEIGHT));

        int speedUpPanelHeight = LABEL_HEIGHT * 4 / 5;
        speedUpPanel.setPreferredSize(new Dimension(labelWidth, speedUpPanelHeight));
        double scalar = (double) speedUpBtn.getOriginalImageWidth() / speedUpBtn.getOriginalImageHeight();
        int imageHeight = speedUpPanelHeight * 9 / 10;
        speedUpBtn.setPreferredSize(new Dimension(((int) (imageHeight * scalar)), imageHeight));

        wizardNameLabel.setPreferredSize(new Dimension(labelWidth, wizardNameLabel.getFont().getSize()));
        wizardGuildLabel.setPreferredSize(new Dimension(labelWidth, wizardGuildLabel.getFont().getSize()));

        super.setBounds(x, y, width, height);
    }

}

