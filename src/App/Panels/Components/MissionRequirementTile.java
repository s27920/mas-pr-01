package App.Panels.Components;

import App.Models.Magic.Spell;
import App.Panels.GuiUtil.RoundedPanel;
import App.StaticUtils.ColorUtils;

import javax.swing.*;
import java.awt.*;

public class MissionRequirementTile extends RoundedPanel {

    private final JLabel spellLabel;
    private Spell spell;

    public MissionRequirementTile(Dimension dim, Spell spell) {
        super(5);
        this.setPreferredSize(dim);
        this.setMaximumSize(dim);
        this.setMinimumSize(dim);
        this.setBackground(ColorUtils.genRandomColor());
        this.setLayout(new BorderLayout());

        spellLabel = new JLabel();
        spellLabel.setHorizontalAlignment(SwingConstants.CENTER);
        spellLabel.setText(spell.getName());
        spellLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.revalidate();
        this.repaint();

        this.add(spellLabel, BorderLayout.CENTER);
    }
}
