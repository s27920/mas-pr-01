import Models.Guild.Guild;
import Models.Guild.GuildMember;
import Models.Guild.GuildRank;
import Models.Guild.Territory;
import Models.Magic.Spell;
import Models.Mission.Mission;
import Models.Mission.MissionDifficulty;
import Models.Panels.MainFrame;
import Models.Util.Coords;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        Set<Spell> spells = new HashSet<>();

        Spell spell = new Spell("fireball");
        Spell spell1 = new Spell("Healing");
        Spell spell2 = new Spell("Teleportation");

        spells.add(spell);
        spells.add(spell1);
        spells.add(spell2);

        int n = 23;

        Coords[] coords = Territory.getNUniquePois(n);

        for (int i = 0; i < n; i++) {
            new Mission(new Territory(String.format("territory %s", i), coords[i]), MissionDifficulty.values()[i%3], String.format("mission %s", i), String.format("mission %s description", i), spells);
        }

        Guild guild = new Guild("guild 1");

        GuildMember member = new GuildMember("merlin 1", 1, GuildRank.Elder, guild);
        member.learnSpell(spell);
        member.learnSpell(spell2);
        member.learnSpell(spell1);
        GuildMember member1 = new GuildMember("merlin 2", 3, GuildRank.Apprentice, guild);
        member1.learnSpell(spell);
        member1.learnSpell(spell2);
        member1.learnSpell(spell1);
        GuildMember member2 = new GuildMember("merlin 3", 1, GuildRank.Apprentice, guild);
        member2.learnSpell(spell);
        member2.learnSpell(spell2);
        member2.learnSpell(spell1);
        GuildMember member3 = new GuildMember("merlin 4", 2, GuildRank.Master, guild);
        member3.learnSpell(spell);
        member3.learnSpell(spell2);
        member3.learnSpell(spell1);
        GuildMember member4 = new GuildMember("merlin 5", 4, GuildRank.Apprentice, guild);
        member4.learnSpell(spell);
        member4.learnSpell(spell2);
        member4.learnSpell(spell1);
        GuildMember member5 = new GuildMember("merlin 6", 5, GuildRank.Apprentice, guild);
        member5.learnSpell(spell);
        member5.learnSpell(spell2);
        member5.learnSpell(spell1);
        GuildMember member6 = new GuildMember("merlin 7", 1, GuildRank.Elder, guild);
        member6.learnSpell(spell);
        member6.learnSpell(spell2);
        member6.learnSpell(spell1);


        FlatDarkLaf.setup();

        SwingUtilities.invokeLater(MainFrame::new);
    }
}