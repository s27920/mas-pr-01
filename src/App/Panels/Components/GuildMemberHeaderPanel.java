package App.Panels.Components;

import App.Models.Guild.GuildMember;
import App.Panels.GuiUtil.ImagePanel;
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
    private final JLabel wizardNameLabel;
    private final JLabel wizardGuildLabel;
    private final GridBagConstraints gbc;

    private GuildMember loggedInMember;
    private ImagePanel guildLogoPanel;
    private final Runnable goBackCallback;

    public GuildMemberHeaderPanel(Runnable goBackCallback) {
        this.goBackCallback = goBackCallback;

        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(35, 35, 35));

        wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(ColorUtils.TRANSPARENT);

        wizardNameLabel = new JLabel();
        wizardGuildLabel = new JLabel();

        wizardGuildLabel.setForeground(ColorUtils.CREAM);
        wizardNameLabel.setForeground(ColorUtils.CREAM);

        wizardGuildLabel.setFont(FontUtils.getJomhuriaFont(16));
        wizardNameLabel.setFont(FontUtils.getJomhuriaFont(24));

        wizardGuildLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wizardNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        wrapperPanel.add(wizardNameLabel, BorderLayout.NORTH);
        wrapperPanel.add(wizardGuildLabel, BorderLayout.SOUTH);

        glue = new JPanel();
        glue.setBackground(ColorUtils.TRANSPARENT);

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
        gbc.insets = new Insets(1, 1, 1, 1);
    }

    public void setLoggedInMember(GuildMember member) {
        this.loggedInMember = member;

        if (guildLogoPanel != null) {
            this.remove(guildLogoPanel);
        }

        guildLogoPanel = ImagePanel.getGuildIcon(loggedInMember.getGuild().getChosenIcon());
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
        glue.setPreferredSize(new Dimension(width - labelWidth - LABEL_HEIGHT, LABEL_HEIGHT));

        wizardNameLabel.setPreferredSize(new Dimension(labelWidth, wizardNameLabel.getFont().getSize()));
        wizardGuildLabel.setPreferredSize(new Dimension(labelWidth, wizardGuildLabel.getFont().getSize()));

        super.setBounds(x, y, width, height);
    }

    public int getLabelHeight() {
        return LABEL_HEIGHT;
    }
}

