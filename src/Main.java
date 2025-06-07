import App.Models.Guild.Guild;
import App.Models.Guild.GuildMember;
import App.Models.Guild.Territory;
import App.Models.Magic.Spell;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionDifficulty;
import App.Models.Mission.MissionReward;
import App.Models.Mission.MissionRewardType;
import App.Panels.MainFrame;
import App.Types.Coords;
import App.Util.MissionTimerService;
import App.Util.SuperObject;
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
            new Mission(new Territory(String.format("territory %s", i), coords[i]), MissionDifficulty.values()[i%3], String.format("mission %s", i), "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium  tellus duis convallis. Tempus leo eu aenean sed diam urna tempor....", spells, missionRewards);
        }

        Guild wizardGuild = new Guild("Wizards", "Vis per laborem");
        Guild druidGuild = new Guild("Druids", "Gutta cavat lapidem non vi, sed saepe cadendo");
        Guild warlockGuild = new Guild("Warlocks", "Magna otia caeli");

        GuildMember wizard1 = new GuildMember("merlin 1", 1, wizardGuild);
        wizard1.getPromoted();
        wizard1.getPromoted();
        wizard1.learnSpell(spell);
        wizard1.learnSpell(spell2);
        wizard1.learnSpell(spell1);
        GuildMember wizard2 = new GuildMember("merlin 2", 3, wizardGuild);
        wizard2.learnSpell(spell);
        wizard2.learnSpell(spell2);
        wizard2.learnSpell(spell1);
        GuildMember wizard3 = new GuildMember("merlin 3", 1, wizardGuild);
        wizard3.getPromoted();
        wizard3.learnSpell(spell);
        wizard3.learnSpell(spell2);
        wizard3.learnSpell(spell1);
        GuildMember wizard4 = new GuildMember("merlin 4", 2, wizardGuild);
        wizard4.learnSpell(spell);
        wizard4.learnSpell(spell2);
        wizard4.learnSpell(spell1);
        GuildMember wizard5 = new GuildMember("merlin 5", 4, wizardGuild);
        wizard5.learnSpell(spell);
        wizard5.learnSpell(spell2);
        wizard5.learnSpell(spell1);
        GuildMember wizard6 = new GuildMember("merlin 6", 5, wizardGuild);
        wizard6.learnSpell(spell);
        wizard6.learnSpell(spell2);
        wizard6.learnSpell(spell1);
        GuildMember wizard7 = new GuildMember("merlin 7", 1, wizardGuild);
        wizard7.getPromoted();
        wizard7.learnSpell(spell);
        wizard7.learnSpell(spell2);
        wizard7.learnSpell(spell1);



        GuildMember druid1 = new GuildMember("druid 1", 1, druidGuild);
        druid1.getPromoted();
        druid1.getPromoted();
        druid1.learnSpell(spell);
        druid1.learnSpell(spell2);
        druid1.learnSpell(spell1);
        GuildMember druid2 = new GuildMember("druid 2", 3, druidGuild);
        druid2.learnSpell(spell);
        druid2.learnSpell(spell2);
        druid2.learnSpell(spell1);
        GuildMember druid3 = new GuildMember("druid 3", 1, druidGuild);
        druid3.getPromoted();
        druid3.learnSpell(spell);
        druid3.learnSpell(spell2);
        druid3.learnSpell(spell1);
        GuildMember druid4 = new GuildMember("druid 4", 2, druidGuild);
        druid4.learnSpell(spell);
        druid4.learnSpell(spell2);
        druid4.learnSpell(spell1);
        GuildMember druid5 = new GuildMember("druid 5", 4, druidGuild);
        druid5.learnSpell(spell);
        druid5.learnSpell(spell2);
        druid5.learnSpell(spell1);
        GuildMember druid6 = new GuildMember("druid 6", 5, druidGuild);
        druid6.learnSpell(spell2);
        druid6.learnSpell(spell);
        druid6.learnSpell(spell1);
        GuildMember druid7 = new GuildMember("druid 7", 1, druidGuild);
        druid7.getPromoted();
        druid7.learnSpell(spell);
        druid7.learnSpell(spell2);
        druid7.learnSpell(spell1);


        GuildMember warlock1 = new GuildMember("warlock 1", 1, warlockGuild);
        warlock1.getPromoted();
        warlock1.getPromoted();
        warlock1.learnSpell(spell);
        warlock1.learnSpell(spell2);
        warlock1.learnSpell(spell1);
        GuildMember warlock2 = new GuildMember("warlock 2", 3, warlockGuild);
        warlock2.learnSpell(spell);
        warlock2.learnSpell(spell2);
        warlock2.learnSpell(spell1);
        GuildMember warlock3 = new GuildMember("warlock 3", 1, warlockGuild);
        warlock3.getPromoted();
        warlock3.learnSpell(spell);
        warlock3.learnSpell(spell2);
        warlock3.learnSpell(spell1);
        GuildMember warlock4 = new GuildMember("warlock 4", 2, warlockGuild);
        warlock4.learnSpell(spell);
        warlock4.learnSpell(spell2);
        warlock4.learnSpell(spell1);
        GuildMember warlock5 = new GuildMember("warlock 5", 4, warlockGuild);
        warlock5.learnSpell(spell);
        warlock5.learnSpell(spell2);
        warlock5.learnSpell(spell1);
        GuildMember warlock6 = new GuildMember("warlock 6", 5, warlockGuild);
        warlock6.learnSpell(spell2);
        warlock6.learnSpell(spell);
        warlock6.learnSpell(spell1);
        GuildMember warlock7 = new GuildMember("warlock 7", 1, warlockGuild);
        warlock7.getPromoted();
        warlock7.learnSpell(spell);
        warlock7.learnSpell(spell2);
        warlock7.learnSpell(spell1);

        SuperObject.getObjectsFromClass(GuildMember.class).forEach(m->{
            m.getKnownSpells().forEach(ks-> ks.setMasteryLevel(((int) (Math.random() * 9)) + 1));
            m.rebuildSortedTree();
        });

        FlatDarkLaf.setup();

        SwingUtilities.invokeLater(MainFrame::new);
    }
}
