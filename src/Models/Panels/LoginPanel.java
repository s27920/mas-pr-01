package Models.Panels;

import Models.Guild.GuildMember;
import Models.Guild.GuildRank;
import Models.Util.MemberSelectionCallback;
import Models.Util.SuperObject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginPanel extends JPanel {
    public LoginPanel(MemberSelectionCallback callback) {

        JScrollPane scrollPane = new JScrollPane();
        JPanel tiledPanel = new JPanel();

        tiledPanel.setLayout(new BoxLayout(tiledPanel, BoxLayout.Y_AXIS));

        List<GuildMember> guildMembers = SuperObject.getObjectsFromClass(GuildMember.class).stream().filter(w -> w.getRank() == GuildRank.Master || w.getRank() == GuildRank.Elder).toList();

        for (GuildMember member : guildMembers){
            tiledPanel.add(new WizardPanel(member, callback));
        }

        scrollPane.setViewportView(tiledPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }
}
