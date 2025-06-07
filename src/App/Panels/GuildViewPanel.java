package App.Panels;

import App.Models.Guild.GuildMember;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionAssignment;
import App.Models.Mission.MissionStatus;
import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;
import App.Types.Coords;
import App.Callbacks.MissionSelectionCallback;
import App.Callbacks.ShapeIntersectCallback;
import App.Util.SuperObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GuildViewPanel extends JPanel {
    private GuildMember loggedInMember;

    private final int LABEL_HEIGHT = 40;
    private final int PANEL_HEIGHT = 520;

    private final int DESC_TILE_WIDTH = 150;
    private final int DESC_TILE_HEIGHT = 200;

    private final MissionSelectionCallback callback;

    private final List<MissionMarker> missionMarkers = new ArrayList<>();

    private final JPanel thisPanel;
    private final JPanel labelPanel;
    private final JPanel wrapperPanel;
    private final JPanel glue;
    private final JLabel wizardNameLabel;
    private final JLabel wizardGuildLabel;


    public GuildViewPanel(MissionSelectionCallback missionSelectionCallback, Runnable goBackCallback) {

        this.thisPanel = this;
        this.callback = missionSelectionCallback;
        labelPanel = new JPanel(new GridBagLayout());
        labelPanel.setBackground(new Color(35, 35, 35));

        wrapperPanel = new JPanel(new BorderLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.VERTICAL;

        wrapperPanel.setBackground(ColorUtils.TRANSPARENT);
        labelPanel.add(wrapperPanel, gbc);

        wizardNameLabel = new JLabel();
        wizardGuildLabel = new JLabel();

        wizardGuildLabel.setForeground(ColorUtils.CREAM);
        wizardNameLabel.setForeground(ColorUtils.CREAM);

        wizardGuildLabel.setFont(FontUtils.getJomhuriaFont(16));
        wizardNameLabel.setFont(FontUtils.getJomhuriaFont(24));

        wizardGuildLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wizardNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx++;

        glue = new JPanel();
        glue.setBackground(ColorUtils.TRANSPARENT);
        labelPanel.add(glue, gbc);

        gbc.gridx++;
        gbc.insets = new Insets(1, 1, 1, 1);

        ImagePanel guildLogoPanel = ImagePanel.getGuildIcon(1);
        guildLogoPanel.setPreferredSize(new Dimension(LABEL_HEIGHT - 2, LABEL_HEIGHT - 2));
        guildLogoPanel.setBackground(ColorUtils.TRANSPARENT);
        guildLogoPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        labelPanel.add(guildLogoPanel, gbc);

        wrapperPanel.add(wizardNameLabel, BorderLayout.NORTH);
        wrapperPanel.add(wizardGuildLabel, BorderLayout.SOUTH);

        JLayeredPane backgroundImagePanel = new JLayeredPane();
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
            MissionMarker missionMarker = new MissionMarker(
                    color,
                    mission.getTerritory().getTerritoryCoordinates(),
                    mission,
                    missionSelectionCallback,
                    () -> backgroundImagePanel.add(new DetailPanel(
                            loggedInMember,
                            mission,
                            mission.getTerritory().getTerritoryCoordinates(),
                            new Dimension(DESC_TILE_WIDTH, DESC_TILE_HEIGHT),
                            new Dimension(720, PANEL_HEIGHT - LABEL_HEIGHT), // TODO suboptimal, try to extract at runtime
                            color,
                            missionSelectionCallback,
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
                    ), JLayeredPane.PALETTE_LAYER)
            );
            markers[i] = missionMarker;

            missionMarkers.add(missionMarker);
            backgroundImagePanel.add(missionMarker, JLayeredPane.PALETTE_LAYER);
        }

        guildLogoPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goBackCallback.run();
            }
        });

        this.setLayout(new BorderLayout());
        this.add(labelPanel, BorderLayout.NORTH);
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
        int labelWidth = width / 5;
        wrapperPanel.setPreferredSize(new Dimension(labelWidth, LABEL_HEIGHT));
        glue.setPreferredSize(new Dimension(width - labelWidth - LABEL_HEIGHT, LABEL_HEIGHT));
        labelPanel.setPreferredSize(new Dimension(width, LABEL_HEIGHT));

        wizardNameLabel.setPreferredSize(new Dimension(labelWidth, wizardNameLabel.getFont().getSize()));
        wizardGuildLabel.setPreferredSize(new Dimension(labelWidth, wizardGuildLabel.getFont().getSize()));

        super.setBounds(x, y, width, height);
    }


    public void setLoggedInMember(GuildMember member) {
        this.loggedInMember = member;
        for (MissionMarker marker : missionMarkers) { // TODO suboptimal
            marker.setSelectedMember(member);
        }
        wizardNameLabel.setText(member.getName());
        wizardGuildLabel.setText(String.format("Guild: %s", member.getGuild().getGuildName()));
        // TODO modify label size based on text max size
    }
}


