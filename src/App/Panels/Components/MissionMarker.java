package App.Panels.Components;

import App.Callbacks.RunnableCallback;
import App.Models.Guild.GuildMember;
import App.Models.Guild.MemberState;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionStatus;
import App.StaticUtils.ColorUtils;
import App.Types.Coords;
import App.Callbacks.MissionSelectionCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MissionMarker extends JPanel {

    private Color pointColor;
    private Color borderColor;
    private final JPanel thisPoint;
    private GuildMember selectedMember;
    private Mission mission;
    private boolean showedError;

    private final int DIAMETER = 20;
    private final int BORDER_THICKNESS = 2;

    private final Timer hoverTimer;

    private boolean isHovered = false;

    private final int SHOW_DETAILS_DELAY_MILIS = 500;

    private final int x1, y1;

    private final Coords coords;

    private final RunnableCallback callback;

    public MissionMarker(
            Color color,
            Coords coords,
            Mission mission,
            RunnableCallback callback,
            Runnable hoveredCallback
    ) {
        this.callback = callback;
        this.mission = mission;
        this.pointColor = color;
        this.thisPoint = this;
        this.coords = coords;

        this.hoverTimer = new Timer(SHOW_DETAILS_DELAY_MILIS, e -> {
            if (isHovered){
                thisPoint.setVisible(false);
                hoveredCallback.run();

            }
        });
        hoverTimer.setRepeats(false);

        this.borderColor = switch (mission.getDifficulty()) {
            case Easy -> new Color(0, 255, 0, 200);
            case Medium -> new Color(255, 255, 0, 200);
            case Hard -> new Color(255, 0, 0, 200);
        };

        this.x1 = coords.x() - (DIAMETER + BORDER_THICKNESS) / 2;
        this.y1 = coords.y() - (DIAMETER + BORDER_THICKNESS) / 2;
        this.setBounds(x1, y1, DIAMETER + BORDER_THICKNESS * 2, DIAMETER + BORDER_THICKNESS * 2);
        this.setOpaque(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                callback.run(()->{});
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pointColor = ColorUtils.darkenColor(pointColor, 25);
                borderColor = ColorUtils.darkenColor(borderColor, 25);
                thisPoint.revalidate();
                thisPoint.repaint();
                isHovered = true;
                hoverTimer.restart();

            }

            @Override
            public void mouseExited(MouseEvent e) {
                pointColor = ColorUtils.lightenColor(pointColor, 25);
                borderColor = ColorUtils.lightenColor(borderColor, 25);
                thisPoint.revalidate();
                thisPoint.repaint();
                isHovered = false;
                if (hoverTimer.isRunning()){
                    hoverTimer.stop();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = ((Graphics2D) g.create());

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(pointColor);
        g2d.fillOval(BORDER_THICKNESS, BORDER_THICKNESS, DIAMETER, DIAMETER);

        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(BORDER_THICKNESS));
        g2d.drawOval(BORDER_THICKNESS, BORDER_THICKNESS, 20, 20);

    }

    public boolean isShowedError() {
        return showedError;
    }
    public void flipShowedError(){
        showedError = !showedError;
    }

    public Mission getMission() {
        return mission;
    }

    public void validateColor(){
        this.borderColor = switch (mission.getStatus()){
            case CREATED -> this.borderColor;
            case IN_PROGRESS -> Color.MAGENTA;
            case COMPLETED -> Color.GRAY;
        };
        this.revalidate();
        this.repaint();
    }

    public void setSelectedMember(GuildMember selectedMember) {
        this.selectedMember = selectedMember;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getDiameterWithBorder(){
        return DIAMETER + BORDER_THICKNESS * 2;
    }

    public Coords getCoords() {
        return coords;
    }
}
