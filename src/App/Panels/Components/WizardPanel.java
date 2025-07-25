package App.Panels.Components;

import App.Models.Guild.GuildMember;
import App.Callbacks.MemberSelectionCallback;
import App.Panels.GuiUtil.ImagePanel;
import App.Panels.GuiUtil.RoundedPanel;
import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WizardPanel extends RoundedPanel {
    private final JPanel currPanel;
    private final MemberSelectionCallback switchCardsCallback;
    private final ImagePanel imagePanel;

    public WizardPanel(GuildMember guildMember, MemberSelectionCallback callback) {
        super(new Dimension(1, 1), 15, ColorUtils.DARK_GREY);
        this.switchCardsCallback = callback;
        this.currPanel = this;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        this.setLayout(new BorderLayout());

        JPanel textPanel = new JPanel();
        textPanel.setBackground(ColorUtils.TRANSPARENT);

        textPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(3, 0, 3, 0);

        JLabel nameLabel = new JLabel(guildMember.getName(), SwingConstants.CENTER);
        nameLabel.setForeground(ColorUtils.CREAM);
        nameLabel.setFont(FontUtils.getJomhuriaFont(64));
        JLabel clanLabel = new JLabel(String.format("Guild: %s", guildMember.getGuild().getGuildName()), SwingConstants.CENTER);
        clanLabel.setForeground(ColorUtils.CREAM);
        clanLabel.setFont(FontUtils.getJomhuriaFont(40));

        textPanel.add(nameLabel, gbc);
        gbc.gridy++;
        textPanel.add(clanLabel, gbc);


        JPanel wrapperPanel = new JPanel(new GridBagLayout());

        GridBagConstraints wrapperGbc = new GridBagConstraints();
        wrapperGbc.weightx = 1.0;
        wrapperGbc.weighty = 1.0;
        wrapperGbc.fill = GridBagConstraints.BOTH;
        wrapperGbc.gridy = 0;
        wrapperGbc.gridx = 0;
        wrapperGbc.anchor = GridBagConstraints.NORTH;
        wrapperGbc.insets = new Insets(0, 3, 0, 3);

        imagePanel = ImagePanel.getGuildIcon(guildMember.getGuild().getChosenIcon());
        imagePanel.setBackground(ColorUtils.TRANSPARENT);
        wrapperPanel.add(imagePanel, wrapperGbc);
        wrapperGbc.gridy++;
        wrapperGbc.weighty = 0.0;
        wrapperGbc.fill = GridBagConstraints.BOTH;
        wrapperGbc.insets = new Insets(0, 0, 0, 0);

        JLabel mottoLabel = new JLabel(guildMember.getGuild().getMotto(), SwingConstants.CENTER);
        mottoLabel.setFont(FontUtils.getJomhuriaFont(18));
        mottoLabel.setForeground(ColorUtils.CREAM);
        wrapperPanel.add(mottoLabel, wrapperGbc);
        wrapperPanel.setBackground(ColorUtils.TRANSPARENT);


        this.add(textPanel, BorderLayout.CENTER);
        this.add(wrapperPanel, BorderLayout.EAST);

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