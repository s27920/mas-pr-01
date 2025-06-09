package App.Panels.Components;

import App.Models.Guild.GuildMember;
import App.Panels.GuiUtil.ImagePanel;
import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;

import javax.swing.*;
import java.awt.*;

public class AssignedMemberPanel extends JPanel {
    private final GuildMember guildMember;
    private final Dimension panelDimension;
    private final int panelHeight;
    private JPanel fillerPanel;
    private boolean isLeader = false;


    public AssignedMemberPanel(GuildMember guildMember, Dimension panelDimension, int panelHeight) {
        this.guildMember = guildMember;
        this.panelDimension = panelDimension;
        this.panelHeight = panelHeight;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(panelDimension.width, panelHeight));
        setBackground(ColorUtils.TRANSPARENT);

        JPanel detailPanel = createDetailPanel();

        JPanel[] fillerPanels = createFillerPanels();

        add(fillerPanels[0], BorderLayout.WEST);
        add(fillerPanels[1], BorderLayout.EAST);
        add(detailPanel, BorderLayout.CENTER);
    }

    private JPanel createDetailPanel() {
        JPanel detailPanel = new JPanel(new BorderLayout());
        int detailPanelWidth = (int) (panelDimension.width * 0.9);

        detailPanel.setPreferredSize(new Dimension(detailPanelWidth, panelHeight));
        detailPanel.setBackground(ColorUtils.TRANSPARENT);

        ImagePanel iconPanel = ImagePanel.getGuildMemberIcon(guildMember.getChosenIcon());
        iconPanel.setPreferredSize(new Dimension(panelHeight, panelHeight));
        iconPanel.setBackground(ColorUtils.TRANSPARENT);

        JLabel memberName = new JLabel(guildMember.getName(), SwingConstants.CENTER);
        memberName.setPreferredSize(new Dimension(detailPanelWidth - panelHeight, panelHeight));
        memberName.setFont(FontUtils.getJomhuriaFont(20));
        memberName.setForeground(ColorUtils.DARK_PURPLE);

        fillerPanel = new JPanel();
        fillerPanel.setPreferredSize(new Dimension(panelHeight, panelHeight));
        fillerPanel.setBackground(ColorUtils.TRANSPARENT);

        iconPanel.setPreferredSize(new Dimension(panelHeight, panelHeight));
        iconPanel.setBackground(ColorUtils.TRANSPARENT);

        detailPanel.add(iconPanel, BorderLayout.WEST);
        detailPanel.add(memberName, BorderLayout.CENTER);
        detailPanel.add(fillerPanel, BorderLayout.EAST);

        return detailPanel;
    }

    private JPanel[] createFillerPanels() {
        int detailPanelWidth = (int) (panelDimension.width * 0.9);
        int paddingWidth = (panelDimension.width - detailPanelWidth) / 2;

        JPanel fillerLeft = new JPanel();
        JPanel fillerRight = new JPanel();

        fillerLeft.setPreferredSize(new Dimension(paddingWidth, panelHeight));
        fillerLeft.setBackground(ColorUtils.TRANSPARENT);

        fillerRight.setPreferredSize(new Dimension(paddingWidth, panelHeight));
        fillerRight.setBackground(ColorUtils.TRANSPARENT);

        return new JPanel[]{fillerLeft, fillerRight};
    }

    public void makeLeader(){
        if (!isLeader){
            this.fillerPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = 0;
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weighty = 0.0;
            gbc.weightx = 0.0;
            gbc.fill = GridBagConstraints.NONE;

            ImagePanel leaderMark = ImagePanel.getLeaderMark();
            leaderMark.setPreferredSize(new Dimension(20, 20 * leaderMark.getOriginalImageHeight() / leaderMark.getOriginalImageWidth()));
            this.fillerPanel.add(leaderMark, gbc);
            leaderMark.setBackground(ColorUtils.TRANSPARENT);
        }
    }

}
