package App.Util;

import App.Models.Guild.Guild;
import App.Models.Guild.GuildMember;
import App.Models.Guild.Territory;
import App.Models.Magic.RequiredSpell;
import App.Models.Magic.Spells.*;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionDifficulty;
import App.Models.Mission.MissionReward;
import App.Models.Mission.MissionRewardType;
import App.Types.Coords;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class JavaObjectSetupTesting {
    public static void setup() {
        Spell[] spellsArr = {
                new Spell("Lightning bolt", null, null, new DamageSpell(10.0)),
                new Spell("Leech", null, null, new DamageSpell(10.0), new HealingSpell(15.0)),
                new Spell("Blessing", null, null, new HealingSpell(8.0), new SupportSpell("gives invincibility")),
                new Spell("Psio blast", null, null, new DamageSpell(25.0)),
                new Spell("Fire breath", null, null, new DamageSpell(6.0)),
                new Spell("Fireball", null, null, new DamageSpell(18.0)),
                new Spell("Healing", null, null, new HealingSpell(30.0)),
                new Spell("Lightning strike", null, null, new DamageSpell(21.0)),
                new Spell("Teleportation", null, null, new SupportSpell("teleport user"))
        };

        int n = 22;

        Coords[] coords = getNUniquePois(n);

        Guild wizardGuild = new Guild("Wizards", "Vis per laborem", 1);
        Guild druidGuild = new Guild("Druids", "Omnia subiecta sunt naturae", 3);
        Guild warlockGuild = new Guild("Warlocks", "Magna otia caeli", 2);

        Set<MissionReward> missionRewards = new HashSet<>();
        MissionReward reward1 = new MissionReward(MissionRewardType.COIN, 100);
        MissionReward reward2 = new MissionReward(MissionRewardType.MAGICAL_RESOURCE, 7);
        missionRewards.add(reward1);
        missionRewards.add(reward2);

        for (int i = 0; i < n; i++) {
            Territory territory = new Territory(String.format("territory %s", i), coords[i], i % 5, i % 2 == 0, i * 100, wizardGuild);
            MissionDifficulty diff = MissionDifficulty.values()[i % 3];
            Mission mission = new Mission(territory, diff, String.format("mission %s", i), "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium  tellus duis convallis. Tempus leo eu aenean sed diam urna tempor....");
            createSpellRequirements(spellsArr, mission);
        }

        GuildMember[] druidMembersArr = {
                new GuildMember("druid 1", 1, druidGuild),
                new GuildMember("druid 2", 3, druidGuild),
                new GuildMember("druid 3", 1, druidGuild),
                new GuildMember("druid 4", 2, druidGuild),
                new GuildMember("druid 5", 4, druidGuild),
                new GuildMember("druid 6", 5, druidGuild),
                new GuildMember("druid 7", 1, druidGuild)
        };
        druidMembersArr[0].getPromoted();
        druidMembersArr[0].getPromoted();
        initializeMembersWithRandomSpells(druidMembersArr, spellsArr);

        GuildMember[] wizardMembersArr = {
                new GuildMember("merlin 1", 1, wizardGuild),
                new GuildMember("merlin 2", 3, wizardGuild),
                new GuildMember("merlin 3", 1, wizardGuild),
                new GuildMember("merlin 4", 2, wizardGuild),
                new GuildMember("merlin 5", 4, wizardGuild),
                new GuildMember("merlin 6", 5, wizardGuild),
                new GuildMember("merlin 7", 1, wizardGuild)
        };
        wizardMembersArr[0].getPromoted();
        wizardMembersArr[0].getPromoted();
        wizardMembersArr[2].getPromoted();
        wizardMembersArr[6].getPromoted();
        initializeMembersWithRandomSpells(wizardMembersArr, spellsArr);

        GuildMember[] warlockMembersArr = {
                new GuildMember("warlock 1", 1, warlockGuild),
                new GuildMember("warlock 2", 3, warlockGuild),
                new GuildMember("warlock 3", 1, warlockGuild),
                new GuildMember("warlock 4", 2, warlockGuild),
                new GuildMember("warlock 5", 4, warlockGuild),
                new GuildMember("warlock 6", 5, warlockGuild),
                new GuildMember("warlock 7", 1, warlockGuild)
        };
        warlockMembersArr[0].getPromoted();
        warlockMembersArr[0].getPromoted();
        warlockMembersArr[2].getPromoted();
        warlockMembersArr[6].getPromoted();
        initializeMembersWithRandomSpells(warlockMembersArr, spellsArr);

        SuperObject.getObjectsFromClass(GuildMember.class).forEach(m -> {
            m.getKnownSpells().forEach(ks -> ks.setMasteryLevel(((int) (Math.random() * 9)) + 1));
            m.rebuildSortedTree();
        });
    }

    private static void createSpellRequirements(Spell[] spellArr, Mission mission){
        int reqCount = generateNormalRandomInt(2, 5);
        HashSet<Integer> knownSpellsIndices = new HashSet<>();
        for (int i = 0; i < reqCount; i++) {
            int spellIndex;
            do {
                spellIndex = ((int) (Math.random() * spellArr.length));
            } while (knownSpellsIndices.contains(spellIndex));
            knownSpellsIndices.add(spellIndex);
            ;
            new RequiredSpell(spellArr[spellIndex], mission, generateNormalRandomInt(1, 3 * (mission.getDifficulty().ordinal() + 1)));
        }
    }
    private static void initializeMembersWithRandomSpells(GuildMember[] members, Spell[] spellsArr) {
        for (int i = 0; i < members.length; i++) {
            HashSet<Integer> knownSpellsIndices = new HashSet<>();
            for (int j = 0; j < generateNormalRandomInt(2, 7); j++) {
                int spellIndex;
                do {
                    spellIndex = ((int) (Math.random() * spellsArr.length));
                } while (knownSpellsIndices.contains(spellIndex));
                knownSpellsIndices.add(spellIndex);
                members[i].learnSpell(spellsArr[spellIndex]);
            }
        }
    }

    private static int generateNormalRandomInt(int min, int max) {
        int minNew = Math.min(min, max);
        int maxNew = Math.max(min, max);
        Random random = new Random();
        double mean = (minNew + maxNew) / 2.0;
        double stdDev = (maxNew - minNew) / 4.0;
        while (true) {
            double normalValue = random.nextGaussian() * stdDev + mean;
            int rounded = (int) Math.round(normalValue);
            if (rounded >= minNew && rounded <= maxNew) {
                return rounded;
            }
        }
    }


    private final static Coords[] POIS = {
            new Coords(71, 69),
            new Coords(142, 103),
            new Coords(96, 165),
            new Coords(269, 150),
            new Coords(208, 147),
            new Coords(255, 16),
            new Coords(339, 75),
            new Coords(412, 55),
            new Coords(367, 148),
            new Coords(291, 230),
            new Coords(104, 340),
            new Coords(130, 402),
            new Coords(253, 361),
            new Coords(296, 443),
            new Coords(359, 304),
            new Coords(428, 340),
            new Coords(551, 375),
            new Coords(611, 431),
            new Coords(496, 191),
            new Coords(538, 127),
            new Coords(568, 61),
            new Coords(619, 263)
    };

    private static Coords[] getNUniquePois(int n) {
        Set<Coords> selectedPois = new HashSet<>();
        for (int i = 0; i < n; i++) {
            Coords poi;
            do {
                poi = POIS[((int) (Math.random() * POIS.length))];
            } while (selectedPois.contains(poi));
            selectedPois.add(poi);
        }
        return selectedPois.toArray(new Coords[0]);
    }
}