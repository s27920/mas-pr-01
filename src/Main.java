import App.Models.Guild.Guild;
import App.Models.Guild.GuildMember;
import App.Models.Guild.Territory;
import App.Models.Magic.Spell;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionDifficulty;
import App.Models.Mission.MissionReward;
import App.Models.Mission.MissionRewardType;
import App.Panels.Panel.MainFrame;
import App.Types.Coords;
import App.Util.JavaObjectSetupTesting;
import App.Util.MissionMarkerCreationCountDownLatch;
import App.Util.MissionTimerService;
import App.Util.SuperObject;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;


public class Main {
    public static void main(String[] args) {

        try {
            SuperObject.readObjects();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

//        JavaObjectSetupTesting.setup();
//        try {
//            SuperObject.writeObjects();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        MissionTimerService.getInstance();
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(MainFrame::new);
        MissionTimerService.getInstance().rebuildMissions();
    }
}
