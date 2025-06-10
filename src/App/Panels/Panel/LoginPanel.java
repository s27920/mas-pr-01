package App.Panels.Panel;

import App.Models.Guild.GuildMember;
import App.Models.Guild.GuildRank;
import App.Callbacks.MemberSelectionCallback;
import App.Panels.Components.WizardPanel;
import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;
import App.Util.SuperObject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginPanel extends JPanel {
    public LoginPanel(MemberSelectionCallback callback) {

        JPanel wrapperPanel = new JPanel();

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.gridy = 0;
        gbc.gridx = 0;

        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(-1, 61));
        infoPanel.setBackground(ColorUtils.CARBON);
        infoPanel.setLayout(new BorderLayout());
        JLabel informationLabel = new JLabel("Guild management system", SwingConstants.CENTER);
        informationLabel.setForeground(ColorUtils.CREAM);
        informationLabel.setFont(FontUtils.getJomhuriaFont(64));

        infoPanel.add(informationLabel, BorderLayout.CENTER);

        this.setBackground(ColorUtils.CARBON);
        wrapperPanel.setBackground(new Color(43, 43, 43));

        this.add(infoPanel, gbc);

        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridy++;

        this.add(wrapperPanel, gbc);

        wrapperPanel.setLayout(new GridBagLayout());
        GridBagConstraints wrapperGbc = new GridBagConstraints();
        wrapperGbc.anchor = GridBagConstraints.NORTH;
        wrapperGbc.insets = new Insets(5, 9, 5, 9);
        wrapperGbc.weightx = 1.0;
        wrapperGbc.weighty = 1.0;
        wrapperGbc.fill = GridBagConstraints.BOTH;
        wrapperGbc.gridx = 0;
        wrapperGbc.gridy = 0;

        List<GuildMember> guildMembers = SuperObject.getObjectsFromClass(GuildMember.class).stream().filter(w -> w.getRank() == GuildRank.Master).toList();

        for (GuildMember member : guildMembers) {
            wrapperPanel.add(new WizardPanel(member, callback), wrapperGbc);
            wrapperGbc.gridy++;
        }
    }
}
