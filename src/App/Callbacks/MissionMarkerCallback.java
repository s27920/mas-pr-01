package App.Callbacks;

import App.Panels.Components.MissionMarker;

import javax.swing.*;

@FunctionalInterface
public interface MissionMarkerCallback {
    void onComplete(MissionMarker marker, JLayeredPane layeredPane);
}
