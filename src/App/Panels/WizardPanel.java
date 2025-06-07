package App.Panels;

import App.Models.Guild.GuildMember;
import App.Callbacks.MemberSelectionCallback;
import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WizardPanel extends RoundedPanel {
    private final JPanel currPanel;
    private final MemberSelectionCallback switchCardsCallback;
    private final JPanel textPanel;
    private final ImagePanel imagePanel;

    public WizardPanel(GuildMember guildMember, MemberSelectionCallback callback) {
        super(new Dimension(1,1), 15, new Color(64, 64, 64));
        this.switchCardsCallback = callback;
        this.currPanel = this;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        this.setLayout(new BorderLayout());

        textPanel = new JPanel();
        textPanel.setBackground(ColorUtils.TRANSPARENT);

        textPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(3,0,3,0);

        JLabel nameLabel = new JLabel(guildMember.getName(), SwingConstants.CENTER);
        nameLabel.setForeground(ColorUtils.CREAM);
        nameLabel.setFont(FontUtils.getJomhuriaFont(64));
        JLabel clanLabel = new JLabel(String.format("Guild: %s", guildMember.getGuild().getGuildName()), SwingConstants.CENTER);
        clanLabel.setForeground(ColorUtils.CREAM);
        clanLabel.setFont(FontUtils.getJomhuriaFont(40));

        textPanel.add(nameLabel, gbc);
        gbc.gridy++;
        textPanel.add(clanLabel, gbc);


        imagePanel = ImagePanel.getGuildIcon(1);
        imagePanel.setBackground(ColorUtils.TRANSPARENT);

        this.add(textPanel, BorderLayout.CENTER);
        this.add(imagePanel, BorderLayout.EAST);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                currPanel.setBackground(ColorUtils.darkenColor(currPanel.getBackground(), 25));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                currPanel.setBackground(ColorUtils.lightenColor(currPanel.getBackground(), 25));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                switchCardsCallback.onMemberSelect(guildMember);
            }
        });
    }
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        this.imagePanel.setPreferredSize(new Dimension(height, height));
    }
}