package App.Callbacks;

import javax.swing.*;

@FunctionalInterface
public interface GenericJPanelCallback {
    void onEvent(JPanel thisPanel);
}
