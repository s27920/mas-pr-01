import Models.Guild.Guild;
import Models.Guild.GuildMember;
import Models.Guild.GuildRank;
import Models.Guild.Territory;
import Models.Magic.Spell;
import Models.Mission.Mission;
import Models.Mission.MissionDifficulty;
import Models.Mission.MissionReward;
import Models.Mission.MissionRewardType;
import Models.Panels.MainFrame;
import Models.Util.Coords;
import Models.Util.MissionTimerService;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        MissionTimerService.getInstance();

        Set<Spell> spells = new HashSet<>();

        Spell spell = new Spell("fireball");
        Spell spell1 = new Spell("Healing");
        Spell spell2 = new Spell("Teleportation");

        spells.add(spell);
        spells.add(spell1);
        spells.add(spell2);

        int n = 22;

        Coords[] coords = Territory.getNUniquePois(n);

        Set<MissionReward> missionRewards = new HashSet<>();

        MissionReward reward1 = new MissionReward(MissionRewardType.COIN, 100);
        MissionReward reward2 = new MissionReward(MissionRewardType.MAGICAL_RESOURCE, 7);

        missionRewards.add(reward1);
        missionRewards.add(reward2);

        for (int i = 0; i < n; i++) {
            new Mission(new Territory(String.format("territory %s", i), coords[i]), MissionDifficulty.values()[i%3], String.format("mission %s", i), String.format("Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium  tellus duis convallis. Tempus leo eu aenean sed diam urna tempor....", i), spells, missionRewards);
        }

        Guild guild = new Guild("guild 1", "pax quaeritur bello");

        GuildMember member = new GuildMember("merlin 1", 1, guild);
        member.getPromoted();
        member.getPromoted();
        member.learnSpell(spell);
        member.learnSpell(spell2);
        member.learnSpell(spell1);
        GuildMember member1 = new GuildMember("merlin 2", 3, guild);
        member1.learnSpell(spell);
        member1.learnSpell(spell2);
        member1.learnSpell(spell1);
        GuildMember member2 = new GuildMember("merlin 3", 1, guild);
        member2.getPromoted();
        member2.learnSpell(spell);
        member2.learnSpell(spell2);
        member2.learnSpell(spell1);
        GuildMember member3 = new GuildMember("merlin 4", 2, guild);
        member3.learnSpell(spell);
        member3.learnSpell(spell2);
        member3.learnSpell(spell1);
        GuildMember member4 = new GuildMember("merlin 5", 4, guild);
        member4.learnSpell(spell);
        member4.learnSpell(spell2);
        member4.learnSpell(spell1);
        GuildMember member5 = new GuildMember("merlin 6", 5, guild);
        member5.learnSpell(spell);
        member5.learnSpell(spell2);
        member5.learnSpell(spell1);
        GuildMember member6 = new GuildMember("merlin 7", 1, guild);
        member6.getPromoted();
        member6.learnSpell(spell);
        member6.learnSpell(spell2);
        member6.learnSpell(spell1);

        guild.getMembers().forEach(m->{
            m.getKnownSpells().forEach(ks-> ks.setMasteryLevel(((int) (Math.random() * 9)) + 1));
            m.rebuildSortedTree();
        });

        FlatDarkLaf.setup();

        SwingUtilities.invokeLater(MainFrame::new);
    }
}
