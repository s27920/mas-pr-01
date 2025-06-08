import App.Panels.Panel.MainFrame;
import App.Util.JavaObjectSetupTesting;
import App.Util.MissionTimerService;
import App.Util.SuperObject;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {

//        try {
//            SuperObject.readObjects();
//        } catch (IOException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

        JavaObjectSetupTesting.setup();
        try {
            SuperObject.writeObjects();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MissionTimerService.getInstance();
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(MainFrame::new);
        MissionTimerService.getInstance().rebuildMissions();
    }
}
