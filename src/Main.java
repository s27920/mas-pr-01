import Models.Guild.Guild;
import Models.Guild.GuildMember;
import Models.Guild.GuildRank;
import Models.Panels.MainFrame;
import Models.Wizard;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Wizard wizard = new Wizard("Merlin");

        Wizard wizard1 = new Wizard("Merlin 2");

        Guild guild = new Guild("guild 1");

        GuildMember member = new GuildMember("Merlin", 5, GuildRank.Elder, guild);
        GuildMember member1 = new GuildMember("Merlin 1", 3, GuildRank.Elder, guild);
//        GuildMember member2 = new GuildMember("Merlin 2", GuildRank.Elder, guild);
//        GuildMember member3 = new GuildMember("Merlin 3", GuildRank.Elder, guild);
//        GuildMember member4 = new GuildMember("Merlin 4", GuildRank.Elder, guild);

        SwingUtilities.invokeLater(MainFrame::new);
    }
}