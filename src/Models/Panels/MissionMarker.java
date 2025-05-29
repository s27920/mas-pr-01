package Models.Panels;

import Models.Guild.GuildMember;
import Models.Mission.Mission;
import Models.Util.ColorUtils;
import Models.Util.Coords;
import Models.Util.MissionSelectionCallback;
import Models.Util.PointSize;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MissionMarker extends JPanel {

    private Color pointColor;
    private Color borderColor;
    private final JPanel thisPoint;
    private GuildMember selectedMember;

    public void setSelectedMember(GuildMember selectedMember) {
        this.selectedMember = selectedMember;
    }

    public MissionMarker(
            Color color,
            Coords coords,
            PointSize size,
            MissionSelectionCallback callback,
            Mission mission
    ) {
        this.pointColor = color;
        this.thisPoint = this;

        this.borderColor = switch (mission.getDifficulty()) {
            case Easy -> Color.GREEN;
            case Medium -> Color.YELLOW;
            case Hard -> Color.RED;
        };

        this.setBounds(coords.x(), coords.y(), size.width() + 2, size.height() + 2);
        this.setOpaque(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                callback.onMissionSelect(selectedMember, mission);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pointColor = ColorUtils.darkenColor(pointColor, 25);
                borderColor = ColorUtils.darkenColor(borderColor, 25);
                thisPoint.revalidate();
                thisPoint.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pointColor = ColorUtils.lightenColor(pointColor, 25);
                borderColor = ColorUtils.lightenColor(borderColor, 25);
                thisPoint.revalidate();
                thisPoint.repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = ((Graphics2D) g.create());

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(pointColor);
        g2d.fillOval(0, 0, 20, 20);

        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawOval(0, 0, 20, 20);

    }

}
