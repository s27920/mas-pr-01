package App.Panels.Components;

import App.Models.Guild.GuildMember;
import App.Panels.GuiUtil.ImagePanel;
import App.Panels.GuiUtil.RoundedPanel;
import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;

import javax.swing.*;
import java.awt.*;

public class SelectedMemberPanel extends JPanel implements App.Util.Iterable {
    private int index;
    private final JPanel paddingPanel;
    private final int imageDims;
    private boolean isLeader = false;

    public SelectedMemberPanel(
            GuildMember guildMember,
            Dimension dimension,
            int index
    ) {
        setIndex(index);

        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 1, 0, 0);

        RoundedPanel roundedPanel = new RoundedPanel(new Dimension(dimension.width - 10, dimension.height - 10), 10, ColorUtils.GREY);

        roundedPanel.setLayout(new GridBagLayout());
        this.setBackground(ColorUtils.TRANSPARENT);
        this.setOpaque(false);

        GridBagConstraints roundedPanelGbc = new GridBagConstraints();
        roundedPanelGbc.weightx = 1.0;
        roundedPanelGbc.weighty = 0.0;
        roundedPanelGbc.anchor = GridBagConstraints.NORTH;
        roundedPanelGbc.fill = GridBagConstraints.HORIZONTAL;
        roundedPanelGbc.gridx = 0;
        roundedPanelGbc.gridy = 0;
        int sidePadding = 10;
        imageDims = dimension.width - 10 - sidePadding * 2;

        roundedPanelGbc.insets = new Insets(20, sidePadding, 5, sidePadding);

        paddingPanel = new JPanel();
        paddingPanel.setPreferredSize(new Dimension(1, 20));
        roundedPanelGbc.insets = new Insets(0, sidePadding, 5, sidePadding);
        paddingPanel.setBackground(ColorUtils.TRANSPARENT);

        roundedPanel.add(paddingPanel, roundedPanelGbc);
        roundedPanelGbc.gridy++;

        ImagePanel imagePanel = ImagePanel.getGuildMemberIcon(guildMember.getChosenIcon());
        imagePanel.setPreferredSize(new Dimension(imageDims, imageDims));
        roundedPanel.add(imagePanel, roundedPanelGbc);

        imagePanel.setBackground(ColorUtils.TRANSPARENT);
        imagePanel.setOpaque(false);

        roundedPanelGbc.insets = new Insets(0, sidePadding, 20, sidePadding);
        roundedPanelGbc.gridy++;


        JLabel nameLabel = new JLabel(guildMember.getName(), SwingConstants.CENTER);
        nameLabel.setFont(FontUtils.getJomhuriaFont(15));
        nameLabel.setForeground(new Color(235, 227, 196));
        nameLabel.setBackground(ColorUtils.TRANSPARENT);
        nameLabel.setOpaque(false);
        roundedPanel.add(nameLabel, roundedPanelGbc);

        this.add(roundedPanel, gbc);

    }

    public void makeLeader(){
        if (!isLeader){
            this.isLeader = true;

            paddingPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            ImagePanel leaderMark = ImagePanel.getLeaderMark();
            leaderMark.setBackground(ColorUtils.TRANSPARENT);
            leaderMark.setOpaque(false);
            double proportions = leaderMark.getOriginalImageWidth() / (double)leaderMark.getOriginalImageHeight();
            int leaderMarkerWidth = imageDims / 2;
            leaderMark.setPreferredSize(new Dimension(leaderMarkerWidth, (int) (leaderMarkerWidth / proportions)));
            paddingPanel.add(leaderMark);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
