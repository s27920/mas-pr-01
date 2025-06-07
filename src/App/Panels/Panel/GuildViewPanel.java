package App.Panels.Panel;

import App.Callbacks.RunnableCallback;
import App.Models.Guild.GuildMember;
import App.Models.Guild.MemberState;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionStatus;
import App.Panels.Components.GuildMemberHeaderPanel;
import App.Panels.Components.MissionMarker;
import App.Callbacks.MissionSelectionCallback;
import App.Panels.Components.DetailPanel;
import App.Panels.GuiUtil.ErrorNotificationPanel;
import App.Panels.GuiUtil.ImagePanel;
import App.Panels.GuiUtil.RoundedPanel;
import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;
import App.Util.SuperObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GuildViewPanel extends JPanel {
    private GuildMember loggedInMember;

    private final int LABEL_HEIGHT = 40;
    private final int PANEL_HEIGHT = 520;

    private final int DESC_TILE_WIDTH = 150;
    private final int DESC_TILE_HEIGHT = 200;

    private final List<MissionMarker> missionMarkers = new ArrayList<>();
    private final GuildMemberHeaderPanel headerPanel;
    private final JLayeredPane backgroundImagePanel;

    private final int[] dims = new int[2];

    private void showError(String message) {
        final ErrorNotificationPanel[] errorNotificationHolder = new ErrorNotificationPanel[1];

        Runnable dismissCallback = () -> {
            backgroundImagePanel.remove(errorNotificationHolder[0]);
            backgroundImagePanel.repaint();
        };

        ErrorNotificationPanel errorPanel = new ErrorNotificationPanel(message, new Dimension(dims[0], dims[1]));
        errorNotificationHolder[0] = errorPanel;

        backgroundImagePanel.add(errorPanel, JLayeredPane.POPUP_LAYER);
        backgroundImagePanel.repaint();

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

    public GuildViewPanel(
            MissionSelectionCallback missionSelectionCallback,
            Runnable goBackCallback
    ) {
        headerPanel = new GuildMemberHeaderPanel(goBackCallback);

        backgroundImagePanel = new JLayeredPane();
        ImagePanel backgroundImage = ImagePanel.getWorldMap();
        Dimension imageDim = new Dimension(backgroundImage.getOriginalImageWidth(), backgroundImage.getOriginalImageHeight());

        backgroundImagePanel.setPreferredSize(imageDim);

        backgroundImage.setBounds(0, 0, 720, PANEL_HEIGHT - LABEL_HEIGHT);
        backgroundImagePanel.add(backgroundImage, JLayeredPane.DEFAULT_LAYER);

        List<Mission> missions = SuperObject.getObjectsFromClass(Mission.class);
        final MissionMarker[] markers = new MissionMarker[missions.size()];

        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);

            Color color = new Color(168, 168, 168, 150);
            int finalI = i;
            RunnableCallback wrapper = (onErrorCallback) -> {
                if(mission.getStatus() == MissionStatus.CREATED){
                    if (loggedInMember.getValidGuildMembers().stream().filter(gm -> gm.getMemberState() == MemberState.ON_STANDBY).count() < 2){
                        onErrorCallback.run();
                        showError("Not enough guild members available! At least 2 members must be on standby to start a mission. Please wait for a mission to complete");
                    } else {
                        missionSelectionCallback.onMissionSelect(loggedInMember, mission);
                    }
                }
            };
            MissionMarker missionMarker = new MissionMarker(
                    color,
                    mission.getTerritory().getTerritoryCoordinates(),
                    mission,
                    wrapper,
                    () -> backgroundImagePanel.add(new DetailPanel(
                            loggedInMember,
                            mission,
                            mission.getTerritory().getTerritoryCoordinates(),
                            new Dimension(DESC_TILE_WIDTH, DESC_TILE_HEIGHT),
                            new Dimension(720, PANEL_HEIGHT - LABEL_HEIGHT),
                            color,
                            wrapper,
                            (shape) -> {
                                List<MissionMarker> markersDisappeared = new ArrayList<>();
                                for (MissionMarker marker : markers) {
                                    if (shape.intersects(marker.getX1(), marker.getY1(), marker.getDiameterWithBorder(), marker.getDiameterWithBorder())) {
                                        marker.setVisible(false);
                                        markersDisappeared.add(marker);
                                    }
                                }
                                return markersDisappeared.toArray(new MissionMarker[0]);
                            },
                            () -> markers[finalI].setVisible(true)
                    ), JLayeredPane.PALETTE_LAYER));
            markers[i] = missionMarker;

            missionMarkers.add(missionMarker);
            backgroundImagePanel.add(missionMarker, JLayeredPane.PALETTE_LAYER);
        }

        this.setLayout(new BorderLayout());
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(backgroundImagePanel, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            onMount();
        }
    }

    private void onMount() {
        for (MissionMarker marker : missionMarkers) {
            marker.validateColor();
        }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        headerPanel.setPreferredSize(new Dimension(width, LABEL_HEIGHT));
        this.dims[0] = width;
        this.dims[1] = height;
        super.setBounds(x, y, width, height);
    }

    public void setLoggedInMember(GuildMember member) {
        this.loggedInMember = member;
        headerPanel.setLoggedInMember(member);

        for (MissionMarker marker : missionMarkers) {
            marker.setSelectedMember(member);
        }
    }
}