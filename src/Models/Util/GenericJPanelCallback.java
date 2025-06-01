package Models.Util;

import javax.swing.*;

@FunctionalInterface
public interface GenericJPanelCallback {
    void onEvent(JPanel thisPanel);
}
