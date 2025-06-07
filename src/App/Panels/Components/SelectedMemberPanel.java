package App.Panels.Components;

import App.Models.Guild.GuildMember;
import App.Panels.GuiUtil.ImagePanel;
import App.Panels.GuiUtil.RoundedPanel;
import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;

import javax.swing.*;
import java.awt.*;

public class SelectedMemberPanel extends RoundedPanel implements App.Util.Iterable {
    private int index;

    public SelectedMemberPanel(GuildMember guildMember, Dimension dimension, int index) {
        super(15);
        this.index = index;
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(94, 94, 94));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 10, 0, 10);

        ImagePanel imagePanel = ImagePanel.getGuildMemberIcon(guildMember.getChosenIcon());
        imagePanel.setPreferredSize(new Dimension(dimension.width, dimension.width));
        imagePanel.setBackground(ColorUtils.TRANSPARENT);
        this.add(imagePanel, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridy++;

        JLabel nameLabel = new JLabel(guildMember.getName(), SwingConstants.CENTER);
        nameLabel.setFont(FontUtils.getJomhuriaFont(15));
        nameLabel.setForeground(new Color(235, 227, 196));
        nameLabel.setBackground(ColorUtils.TRANSPARENT);
        this.add(nameLabel, gbc);

    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
