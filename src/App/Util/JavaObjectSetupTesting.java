package App.Util;

import App.Models.Guild.Guild;
import App.Models.Guild.GuildMember;
import App.Models.Guild.Territory;
import App.Models.Magic.KnownSpell;
import App.Models.Magic.RequiredSpell;
import App.Models.Magic.Spells.*;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionDifficulty;
import App.Models.Mission.MissionReward;
import App.Models.Mission.MissionRewardType;
import App.Types.Coords;

import java.util.*;

public class JavaObjectSetupTesting {
    public static void setup() {
        Spell[] spellsArr = {
                new Spell("Lightning bolt", null, null, new DamageSpell(10.0)), // 0
                new Spell("Leech", null, null, new DamageSpell(10.0), new HealingSpell(15.0)), // 1
                new Spell("Blessing", null, null, new HealingSpell(8.0), new SupportSpell("gives invincibility")), // 2
                new Spell("Charisma+", null, null, new SupportSpell("Gives the owner immaculate rizz")), // 3
                new Spell("Persuasion+", null, null, new SupportSpell("Makes the caster highly persuasive")), // 4
                new Spell("Eagle eye", null, null, new SupportSpell("Gives the owner superhuman vision")), // 5
                new Spell("Teleportation", null, null, new SupportSpell("teleport user")), // 6
                new Spell("Psio blast", null, null, new DamageSpell(25.0)), // 7
                new Spell("Fireball", null, null, new DamageSpell(18.0)), // 8
                new Spell("Healing", null, null, new HealingSpell(30.0)), // 9
                new Spell("Intelligence+", null, null, new SupportSpell("Makes the caster highly intelligent for a short period")), // 10
        };


        Guild wizardGuild = new Guild("Wizards", "Vis per laborem", 1);
        Guild druidGuild = new Guild("Druids", "Omnia subiecta sunt naturae", 3);
        Guild warlockGuild = new Guild("Warlocks", "Magna otia caeli", 2);

        Set<MissionReward> missionRewards = new HashSet<>();
        MissionReward reward1 = new MissionReward(MissionRewardType.COIN, 100);
        MissionReward reward2 = new MissionReward(MissionRewardType.MAGICAL_RESOURCE, 7);
        missionRewards.add(reward1);
        missionRewards.add(reward2);

        Territory[] territories = {
                new Territory("Lowlands", POIS.get("Lowlands"), 5, false, 2571, druidGuild),
                new Territory("Icy spires", POIS.get("Icy spires"), 5, true, 0, null),
                new Territory("Fractured wastes", POIS.get("Fractured wastes"), 5, true, 581, warlockGuild),
                new Territory("Buurna woods", POIS.get("Buurna woods"), 5, false, 1879, druidGuild),
                new Territory("Crystal tower", POIS.get("Crystal tower"), 1, false, 1, wizardGuild),
                new Territory("Deepenkins woods", POIS.get("Deepenkins woods"), 3, false, 11257, null),
                new Territory("unnamed", POIS.get("Pyramid"), 4, true, 0, null),
                new Territory("the ezelainen oasis", POIS.get("Oasis"), 4, true, 115, null),
                new Territory("Ruins of Aarson", POIS.get("Aarson"), 2, false, 87, null),
                new Territory("Mzuelfe", POIS.get("Mzuelfe"), 2, false, 15, warlockGuild),
                new Territory("Fractured Spine", POIS.get("Fractured spine"), 3, false, 8, warlockGuild),
                new Territory("Savili", POIS.get("Savili"), 5, true, 0, wizardGuild),
                new Territory("Isles of the lost", POIS.get("Isles of the lost"), 3, true, 1395, null),
                new Territory("Ezelainen archipelago", POIS.get("Archipelago"), 2, true, 747, wizardGuild),
                new Territory("Crystal castle", POIS.get("Crystal castle"), 1, false, 124, wizardGuild),
                new Territory("Sedsemoor", POIS.get("Sedsemoor"), 2, false, 16723, druidGuild)
        };


        Mission[] missions = {
                new Mission(territories[0], MissionDifficulty.Hard, "Cleaning House",
                        "The fields surrounding the druid capital are sparsely populated, and with recent events that trend may continue. We've been getting multiple reports flowing in from the scant villages dotted about. Apparently villagers keep disappearing, being found by their own weeks later dead and drained of their blood. Now if it was just one instance we could chalk it up to the local loon or drunk making up stories but this can't be ignored. We've managed to trace whatever is causing these events to a long abandoned mineshaft located in the feet of the Colewater ridge. Investigate it."),
                new Mission(territories[1], MissionDifficulty.Hard, "Pest control",
                        "Settle down gentlemen. A team comprised of Druid scouts just recently returned bearing grim news. Apparently deep in the northern mountains a small party of depth striders was spotted. Seems like the inquisition 3 years ago didn't weed all of them out. This very well could be their last hold-out and I believe I do not need to explain why they cannot be allowed to stay. Gather the men get going ASAP and get ready for a fight. And team... don't go dying on me out there"),
                new Mission(territories[2], MissionDifficulty.Hard, "Pact Breaker",
                        "Intelligence reports suggest a rogue warlock cell has been conducting forbidden rituals in the ruins of their old strongholds, attempting to reestablish contact with demonic forces despite the Coalition agreements. The magical corruption is spreading, turning the already desolate landscape into something far worse. This could unravel everything the three guilds have worked for. Elimination is the only option."),
                new Mission(territories[3], MissionDifficulty.Hard, "Blood Moon Rising",
                        "The western forests have gone silent. No word from trading posts, no migrating birds, nothing. Scout reports mention an unnatural crimson glow emanating from deep within the woods during nighttime hours. Local wildlife has either fled or... changed. Something ancient may have awakened during the recent magical disturbances. Prepare for the worst."),
                new Mission(territories[4], MissionDifficulty.Medium, "Worlds eye",
                        "Gentlemen I believe most of you are aware of the existence and work of one Merlin the great, our wizarding friends in particular. Well recently he was given access to study a particularly powerful crystal formed after lightning struck the skeletal remains of some depth dwelling beast washed ashore. He claims it may hold the secrets to the very nature of magic. Now whether you believe him or not the wizard guild put in a formal request for the brightest minds we have to assist in research. This is a great honor and we intend to live up to our name and rise to the occasion."),
                new Mission(territories[5], MissionDifficulty.Medium, "Interspecies relations",
                        "An elvish emissary just arrived in the eternal woods, he was seeking an audience with {name} the Druid leader. Supposedly after 400 years of isolationism, staying dormant during the seven year war and the ensuing inquisition now they decide to reach out. He got the audience as requested. He did not speak much but requested a small party of diplomats be sent to the Deppenkins woods for talks. This stinks to high hell and I don't trust that pointy eared leaf lover in the slightest but if they truly have decided to change their minds we cannot miss this opportunity. Take some charismatic people with you and go."),
                new Mission(territories[6], MissionDifficulty.Medium, "Archeology Lessons",
                        "Most of you remember the expedition to the ancient tomb in the west of the Araml desert, hell most of you were there. Well a caravan of traders carrying goods from the Isles of the lost just came in. They claim to have gotten lost in the desert. Just when they thought there were done for an oasis manifested itself to them, alongside which an ancient pyramid, they attempted entering the tomb but found it to be sealed shut, this may be the big break we've been looking for. The other part of the artifact may very well lie deep in there hidden under some magical veil that made it inaccessible. The Bedouin will guide you there, they've been compensated handsomely for their efforts. Get going."),
                new Mission(territories[7], MissionDifficulty.Medium, "Supply Lines",
                        "Caravans traveling the main trade route between guild territories have been systematically disappearing. No bodies, no wreckage, just... gone. The Coalition's reconstruction efforts depend on these supply lines. Whatever's causing this knows our routes and schedules. Investigation and neutralization required."),
                new Mission(territories[8], MissionDifficulty.Medium, "Echoes of War",
                        "Construction crews working on painstaking project of rebuilding Aarson have unearthed something disturbing - a massive underground chamber filled with crystallized remains of the \"sea phantoms.\" The crystals are still active, resonating with unknown energy. The wizards want to study them, the druids want them destroyed, and the warlocks... well, they're being unusually quiet about the whole thing."),
                new Mission(territories[9], MissionDifficulty.Medium, "Lost Arts",
                        "An old druid sanctuary, thought lost during the war, has been rediscovered by refugee settlers. The problem? It's still inhabited by druids who refuse to acknowledge the war ever ended. They view the Coalition as heresy and any non-druid as an enemy. Diplomatic solutions preferred, but be prepared for hostility."),
                new Mission(territories[10], MissionDifficulty.Medium, "The Singing Stones",
                        "Miners in the western territories report that the mountain itself has begun to \"sing\" - a low, haunting melody that emerges from deep within the peaks. Workers are reporting vivid dreams and some haven't returned from their shifts. The local foreman believes they've struck something that should have stayed buried."),
                new Mission(territories[11], MissionDifficulty.Medium, "Wormhole",
                        "The waters surrounding Awha have always been notoriously unwieldy. For the last 3 years however, ever since the war ended, the frequency with which storms appear, as well as their severity has been gradually increasing. We've been monitoring the situation to ensure it doesn't get out of hand. Unfortunately we must have missed the signs as recently the situation not only reached a tipping point but quickly boiled over. The central island of the Storm archipelago got swallowed up in a surprise whirlpool. The monitoring stations claim the whirlpool seems to be a giant gaping hole in the middle of the ocean growing at a dangerous rate. We need to figure something out. Get on it."),
                new Mission(territories[12], MissionDifficulty.Easy, "Census Taker",
                        "With reconstruction underway, the Coalition needs accurate population counts for resource allocation. Simple enough, except many communities are still suspicious of authority figures, especially those representing the \"enemy\" guilds. Diplomacy and patience required."),
                new Mission(territories[13], MissionDifficulty.Easy, "Census Taker",
                        "With reconstruction underway, the Coalition needs accurate population counts for resource allocation. Simple enough, except many communities are still suspicious of authority figures, especially those representing the \"enemy\" guilds. Diplomacy and patience required."),
                new Mission(territories[14], MissionDifficulty.Easy, "Census Taker",
                        "With reconstruction underway, the Coalition needs accurate population counts for resource allocation. Simple enough, except many communities are still suspicious of authority figures, especially those representing the \"enemy\" guilds. Diplomacy and patience required."),
                new Mission(territories[15], MissionDifficulty.Easy, "Relic Recovery",
                        "A merchant claims to have purchased several pre-war artifacts from a suspicious dealer. Given the Coalition's agreements about dangerous magical items, these need to be verified and possibly confiscated. The merchant is cooperative but the artifacts' origins are questionable.")

        };

        missions[0].addToRequiredSpells(spellsArr[7]);
        missions[0].addToRequiredSpells(spellsArr[8]);
        missions[0].addToRequiredSpells(spellsArr[9]);

        missions[1].addToRequiredSpells(spellsArr[6]);
        missions[1].addToRequiredSpells(spellsArr[7]);
        missions[1].addToRequiredSpells(spellsArr[8]);
        missions[1].addToRequiredSpells(spellsArr[9]);

        missions[2].addToRequiredSpells(spellsArr[0]);
        missions[2].addToRequiredSpells(spellsArr[1]);
        missions[2].addToRequiredSpells(spellsArr[7]);

        missions[3].addToRequiredSpells(spellsArr[0]);
        missions[3].addToRequiredSpells(spellsArr[2]);
        missions[3].addToRequiredSpells(spellsArr[6]);

        missions[4].addToRequiredSpells(spellsArr[10]);
        missions[4].addToRequiredSpells(spellsArr[5]);
        missions[4].addToRequiredSpells(spellsArr[4]);

        missions[5].addToRequiredSpells(spellsArr[3]);
        missions[5].addToRequiredSpells(spellsArr[4]);
        missions[5].addToRequiredSpells(spellsArr[10]);

        missions[6].addToRequiredSpells(spellsArr[5]);
        missions[6].addToRequiredSpells(spellsArr[6]);
        missions[6].addToRequiredSpells(spellsArr[9]);

        missions[7].addToRequiredSpells(spellsArr[5]);
        missions[7].addToRequiredSpells(spellsArr[10]);
        missions[7].addToRequiredSpells(spellsArr[8]);

        missions[8].addToRequiredSpells(spellsArr[5]);
        missions[8].addToRequiredSpells(spellsArr[9]);
        missions[8].addToRequiredSpells(spellsArr[2]);

        missions[9].addToRequiredSpells(spellsArr[2]);
        missions[9].addToRequiredSpells(spellsArr[3]);
        missions[9].addToRequiredSpells(spellsArr[7]);

        missions[10].addToRequiredSpells(spellsArr[6]);
        missions[10].addToRequiredSpells(spellsArr[0]);
        missions[10].addToRequiredSpells(spellsArr[1]);

        missions[11].addToRequiredSpells(spellsArr[6]);
        missions[11].addToRequiredSpells(spellsArr[9]);
        missions[11].addToRequiredSpells(spellsArr[2]);

        missions[12].addToRequiredSpells(spellsArr[2]);
        missions[12].addToRequiredSpells(spellsArr[10]);
        missions[12].addToRequiredSpells(spellsArr[3]);

        missions[13].addToRequiredSpells(spellsArr[2]);
        missions[13].addToRequiredSpells(spellsArr[10]);
        missions[13].addToRequiredSpells(spellsArr[3]);

        missions[14].addToRequiredSpells(spellsArr[2]);
        missions[14].addToRequiredSpells(spellsArr[10]);
        missions[14].addToRequiredSpells(spellsArr[3]);

        missions[15].addToRequiredSpells(spellsArr[5]);
        missions[15].addToRequiredSpells(spellsArr[7]);
        missions[15].addToRequiredSpells(spellsArr[3]);


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

        SuperObject.getObjectsFromClass(Mission.class).forEach((m)->{
            m.getRequiredSpellsSet().forEach((rs -> {
                rs.setKnownLevel(((int) ((Math.random() * 2 * (m.getDifficulty().ordinal() + 1)) + 1)));
            }));
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
                members[i].learnSpell(spellsArr[spellIndex]).setMasteryLevel(((int) (Math.random() * 9)) + 1);

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


    private final static Map<String, Coords> POIS = new HashMap<>();

    static {
        POIS.put("Aarson", new Coords(71, 69)); //
        POIS.put("Buurna woods", new Coords(96, 165)); //
        POIS.put("Wetlands", new Coords(162, 305));
        POIS.put("Crystal castle", new Coords(253, 361)); //
        POIS.put("Crystal palace", new Coords(104, 340)); //
        POIS.put("Crystal tower", new Coords(130, 402)); //
        POIS.put("Icy spires", new Coords(255, 16)); //
        POIS.put("Fractured spine", new Coords(538, 127)); //
        POIS.put("Fractured wastes", new Coords(568, 61)); //
        POIS.put("Veil islands", new Coords(367, 148)); //
        POIS.put("Deepenkins woods", new Coords(412, 55)); //
        POIS.put("Sedsemoor", new Coords(339, 75)); //
        POIS.put("Mines", new Coords(208, 147)); //
        POIS.put("Isles of the lost", new Coords(611, 431)); //
        POIS.put("Yishaw", new Coords(291, 230)); //
        POIS.put("Savili", new Coords(359, 304)); //
        POIS.put("Oasis", new Coords(428, 340)); //
        POIS.put("Lowlands", new Coords(142, 103)); //
        POIS.put("Far desert", new Coords(619, 263)); //
        POIS.put("Mzuelfe", new Coords(496, 191));//
        POIS.put("Spooky castle", new Coords(269, 150)); //
        POIS.put("Bulwark", new Coords(257, 117));
        POIS.put("Lowlands 2", new Coords(400, 252));
        POIS.put("Pyramid", new Coords(551, 375)); //
        POIS.put("Archipelago", new Coords(296, 443)); //
    }

    private final static Coords[] POIS_ARR = {
            new Coords(71, 69), // Aarson
            new Coords(101, 204), // Buurna woods
            new Coords(162, 305), // Wetlands
            new Coords(256, 404), // Crystal castle
            new Coords(105, 379), // crystal palace
            new Coords(131, 441), // Crystal tower
            new Coords(255, 16), // icy spires
            new Coords(540, 167), // fractured spine
            new Coords(580, 99), // fractured wastes
            new Coords(367, 148), // veil islands
            new Coords(423, 97), // deepenkins woods
            new Coords(355, 111), // sedsemoor
            new Coords(207, 186), // mines
            new Coords(603, 478), // isles of the lost
            new Coords(284, 271), // Yishaw
            new Coords(358, 347), //savili
            new Coords(427, 381), //oasis
            new Coords(137, 145), //lowlands
            new Coords(623, 307), //far desert
            new Coords(493, 232), // Mzuelfe
            new Coords(270, 192), // spooky castle
            new Coords(257, 117), // bulwark
            new Coords(400, 252) // lowlands 2
    };

    private final static Coords[] POIS_OG = {
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
                poi = POIS_ARR[((int) (Math.random() * POIS_ARR.length))];
            } while (selectedPois.contains(poi));
            selectedPois.add(poi);
        }
        return selectedPois.toArray(new Coords[0]);
    }
}