package App.Panels.Panel;

import App.Callbacks.MissionMarkerCallback;
import App.Callbacks.MissionSelectionCallback;
import App.Callbacks.RunnableCallback;
import App.Models.Guild.GuildMember;
import App.Models.Guild.MemberState;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionStatus;
import App.Panels.Components.DetailPanel;
import App.Panels.Components.GuildMemberHeaderPanel;
import App.Panels.Components.MissionMarker;
import App.Panels.GuiUtil.ImagePanel;
import App.StaticUtils.ErrorUtils;
import App.Util.MissionMarkerCreationCountDownLatch;
import App.Util.MissionTimerService;
import App.Util.SuperObject;

import javax.swing.*;
import java.awt.*;
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

            int finalI = i;

            MissionMarkerCallback missionCompletionCallback = (marker, layeredPane) ->{
                marker.setBorderColor(Color.GRAY);
                marker.revalidate();
                marker.repaint();
                layeredPane.repaint();
            };

            RunnableCallback wrapper = (onErrorCallback) -> {
                if(mission.getStatus() == MissionStatus.CREATED){
                    if (loggedInMember.getGuild().getValidGuildMembers().stream().filter(gm -> gm.getMemberState() == MemberState.ON_STANDBY).count() < 2){
                        onErrorCallback.run();
                        ErrorUtils.showError(backgroundImagePanel, "Not enough guild members available! At least 2 members must be on standby to start a mission. Please wait for a mission to complete", dims);
                    } else {
                        // TODO potential double callback issuing idk
                        missionSelectionCallback.onMissionSelect(loggedInMember, mission, ()->{
                            missionCompletionCallback.onComplete(markers[finalI], backgroundImagePanel);
                        });
                    }
                }
            };

            MissionMarker missionMarker = new MissionMarker(
                    new Color(168, 168, 168, 150),
                    mission.getTerritory().getTerritoryCoordinates(),
                    mission,
                    wrapper,
                    () -> backgroundImagePanel.add(new DetailPanel(
                            loggedInMember,
                            mission,
                            mission.getTerritory().getTerritoryCoordinates(),
                            new Dimension(DESC_TILE_WIDTH, DESC_TILE_HEIGHT),
                            new Dimension(720, PANEL_HEIGHT - LABEL_HEIGHT),
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
            MissionTimerService.getInstance().setRegisterControlTask(mission, () -> missionCompletionCallback.onComplete(markers[finalI], backgroundImagePanel));


            missionMarkers.add(missionMarker);
            backgroundImagePanel.add(missionMarker, JLayeredPane.PALETTE_LAYER);
        }

        System.out.println("finished");
        MissionMarkerCreationCountDownLatch.getInstance().countDown();

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