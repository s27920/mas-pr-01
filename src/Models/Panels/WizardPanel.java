package Models.Panels;

import Models.Guild.GuildMember;
import Models.Panels.ImagePanel;
import Models.Util.MemberSelectionCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WizardPanel extends RoundedPanel {
    private final int BORDER = 3;

    private final JPanel currPanel;
    private final MemberSelectionCallback switchCardsCallback;

    public WizardPanel(GuildMember guildMember, MemberSelectionCallback callback) {
        super(new Dimension(-1, 150), 15, new Color(0x606060));

        this.switchCardsCallback = callback;
        this.currPanel = this;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel guildLabel = new JLabel(String.format("Guild: %s", guildMember.getGuild().getGuildName()));
        JLabel nameLabel = new JLabel(String.format("Wizard: %s", guildMember.getName()));
        guildLabel.setForeground(new Color(229, 211, 198));
        nameLabel.setForeground(new Color(229, 211, 198));
        guildLabel.setFont(new Font("Broadway", Font.BOLD, 50));
        nameLabel.setFont(new Font("Broadway", Font.BOLD, 50));


        JPanel imagePanel = new ImagePanel(guildMember.getChosenIcon());
        imagePanel.setPreferredSize(new Dimension(150, 150));

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.add(guildLabel);
        labelPanel.add(Box.createVerticalStrut(10));
        labelPanel.add(nameLabel);

        add(labelPanel, BorderLayout.WEST);
        add(imagePanel, BorderLayout.EAST);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                Color mainPanelColor = currPanel.getBackground();
                currPanel.setBackground(new Color(Math.max(0, mainPanelColor.getRed() + 25), Math.max(0, mainPanelColor.getGreen() + 25), Math.max(0, mainPanelColor.getBlue() + 25)));
                currPanel.revalidate();
                currPanel.repaint();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                Color mainPanelColor = currPanel.getBackground();
                currPanel.setBackground(new Color(Math.max(0, mainPanelColor.getRed() - 25), Math.max(0, mainPanelColor.getGreen() - 25), Math.max(0, mainPanelColor.getBlue() - 25)));
                currPanel.revalidate();
                currPanel.repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                switchCardsCallback.onMemberSelect(guildMember);
            }
        });
    }
}
