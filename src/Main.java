import App.Panels.Panel.MainFrame;
import App.Util.JavaObjectSetupTesting;
import App.Util.MissionTimerService;
import App.Util.SuperObject;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;


public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();

        try {
            SuperObject.readObjects();
        } catch(FileNotFoundException | InvalidClassException  ex) {
            JavaObjectSetupTesting.setup();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        MissionTimerService missionTimerServiceInstance = MissionTimerService.getInstance();
        SwingUtilities.invokeLater(MainFrame::new);
        missionTimerServiceInstance.rebuildMissions();
    }
}
