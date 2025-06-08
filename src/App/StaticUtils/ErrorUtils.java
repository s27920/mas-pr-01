package App.StaticUtils;

import App.Panels.GuiUtil.ErrorNotificationPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ErrorUtils {
    public static void showError(JLayeredPane layeredPane, String message, final int[] dims) {
        final ErrorNotificationPanel[] errorNotificationHolder = new ErrorNotificationPanel[1];

        Runnable dismissCallback = () -> {
            layeredPane.remove(errorNotificationHolder[0]);
            layeredPane.repaint();
        };

        ErrorNotificationPanel errorPanel = new ErrorNotificationPanel(message, new Dimension(dims[0], dims[1]));
        errorNotificationHolder[0] = errorPanel;

        layeredPane.add(errorPanel, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();

        Timer autoRemoveTimer = new Timer(3000, (e) -> dismissCallback.run());
        autoRemoveTimer.setRepeats(false);
        autoRemoveTimer.start();

        errorPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        errorPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (autoRemoveTimer.isRunning()) {
                    autoRemoveTimer.stop();
                }
                dismissCallback.run();
            }
        });
    }
}
