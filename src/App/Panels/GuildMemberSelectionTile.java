package App.Panels;

import App.Callbacks.GenericJPanelCallback;
import App.Models.Guild.GuildMember;
import App.Models.Magic.KnownSpell;
import App.StaticUtils.ColorUtils;
import App.StaticUtils.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GuildMemberSelectionTile extends JPanel {
    private final JPanel thisPanel;

    private Color color;
    private final GenericJPanelCallback selectCallback;

    public GuildMemberSelectionTile(GuildMember member, int width, GenericJPanelCallback callback) {
        this.thisPanel = this;
        this.selectCallback = callback;

        int availableWidth = (int) (width * 0.5) - 16 - 10;
        this.setPreferredSize(new Dimension(availableWidth - 4, 120 - 4));
        this.color = new Color(108, 108, 108);
        this.setBackground(color);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setLayout(new BorderLayout());

        JPanel lPanel = new JPanel();
        lPanel.setBackground(ColorUtils.TRANSPARENT);
        lPanel.setPreferredSize(new Dimension(((int) (this.getPreferredSize().width * 0.42)), this.getPreferredSize().height));

        JPanel rPanel = new JPanel();
        rPanel.setBackground(ColorUtils.TRANSPARENT);
        rPanel.setPreferredSize(new Dimension(((int) (this.getPreferredSize().width * 0.58)), this.getPreferredSize().height));

        JLabel bestLabel = new JLabel("Best spells", SwingConstants.LEFT);
        bestLabel.setPreferredSize(new Dimension(((int) (this.getPreferredSize().width * 0.58)), 32));

        bestLabel.setFont(FontUtils.getJomhuriaFont(32));

        bestLabel.setForeground(new Color(195, 195, 195));


        rPanel.setLayout(new GridBagLayout());
        GridBagConstraints rgbc = new GridBagConstraints();

        rgbc.gridx = 0;
        rgbc.gridy = 0;
        rgbc.weightx = 1.0;
        rgbc.weighty = 0.0;
        rgbc.fill = GridBagConstraints.HORIZONTAL;
        rgbc.anchor = GridBagConstraints.NORTH;
        rgbc.insets = new Insets(1, 0, 1, 0);

        rPanel.add(bestLabel, rgbc);

        Font labelFont = FontUtils.getJomhuriaFont(24);

        Color panelColor = new Color(94, 94, 94);

        KnownSpell[] knownSpells = member.getKnownSpells().toArray(new KnownSpell[0]);
        for (int i1 = 0; i1 < Math.min(3, member.getKnownSpells().size()); i1++) {
            rgbc.gridy++;

            Dimension panelSize = new Dimension(((int) (this.getPreferredSize().width * 0.58)), 20);
            RoundedPanel panel = new RoundedPanel(panelSize, 5, panelColor);
            panel.setLayout(new BorderLayout());

            JLabel spellNameLabel = new JLabel(knownSpells[i1].getSpell().getName(), SwingConstants.RIGHT);
            JLabel spellLevelLabel = new JLabel(String.format("lvl. %d", knownSpells[i1].getMasteryLevel()), SwingConstants.LEFT);

            spellNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            spellLevelLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

            spellLevelLabel.setFont(labelFont);
            spellNameLabel.setFont(labelFont);

            Dimension preferredSize = new Dimension(((int) (this.getPreferredSize().width * 0.29) /* (0.58 / 2) */), 20);
            spellNameLabel.setPreferredSize(preferredSize);
            spellLevelLabel.setPreferredSize(preferredSize);

            panel.add(spellNameLabel, BorderLayout.WEST);
            panel.add(spellLevelLabel, BorderLayout.EAST);

            rPanel.add(panel, rgbc);
        }

        rgbc.gridy++;
        rgbc.weighty = 1.0;
        rgbc.fill = GridBagConstraints.BOTH;
        rgbc.insets = new Insets(0, 0, 0, 0);
        JPanel glue = new JPanel();
        glue.setBackground(ColorUtils.TRANSPARENT);
        rPanel.add(glue, rgbc);

        lPanel.setLayout(new GridBagLayout());
        GridBagConstraints lgbc = new GridBagConstraints();

        lgbc.gridx = 0;
        lgbc.gridy = 0;
        lgbc.weightx = 0.0;
        lgbc.weighty = 0.0;
        lgbc.fill = GridBagConstraints.BOTH;
        lgbc.anchor = GridBagConstraints.NORTH;

        System.out.println("generating broken files");
        ImagePanel panel = ImagePanel.getGuildMemberIcon(member.getChosenIcon());
        panel.setBackground(ColorUtils.TRANSPARENT);
        panel.setPreferredSize(new Dimension(77, 77));
        lPanel.add(panel, lgbc);

        lgbc.gridy = 1;
        JLabel wizardNameUser = new JLabel(member.getName(), SwingConstants.CENTER);
        wizardNameUser.setFont(labelFont);
        lPanel.add(wizardNameUser, lgbc);

        this.add(lPanel, BorderLayout.WEST);
        this.add(rPanel, BorderLayout.EAST);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectCallback.onEvent(thisPanel);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                color = ColorUtils.darkenColor(color, 10);
                thisPanel.setBackground(color);
                thisPanel.revalidate();
                thisPanel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                color = ColorUtils.lightenColor(color, 10);
                thisPanel.setBackground(color);
                thisPanel.revalidate();
                thisPanel.repaint();
            }
        });
    }
}
