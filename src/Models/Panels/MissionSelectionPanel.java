package Models.Panels;

import Models.Guild.GuildMember;
import Models.Guild.MemberState;
import Models.Magic.KnownSpell;
import Models.Magic.Spell;
import Models.Mission.Mission;
import Models.Util.ColorUtils;
import Models.Util.FontUtils;
import Models.Util.GenericJPanelCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class MissionSelectionPanel extends JPanel {
    private final JPanel thisPanel;

    private GuildMember missionDispatcher;
    private Mission selectedMission;

    private final JPanel memberListPanel;
    private final JPanel selectedPanel;

    private final int WIDTH = 720;
    private final int HEIGHT = 560;

    private GuildMember[] selectedMembers;
    private Models.Util.Iterable[] selectedMemberPanels;
    private int selectedMemberPointer;

    private final Runnable cancelCallback;
    private final Runnable confirmCallback;

    private final ImagePanel clippedImagePanel;
    private final JLabel missionDifficultyLabel;
    private final JTextArea missionDescriptionArea;
    private final JPanel missionRequiredSpellPanel;
    private final GridBagConstraints spellPanelGbc;


    private final JLabel missionNameLabel;
    private final JPanel missionTextDetailPanels;
    private final JLabel missionAreaLabel;


    private final int SCROLL_BAR_WIDTH = 8;
    private final double textDescriptionPanelHeight;

    private final HashMap<GuildMember, GuildMemberSelectionTile> guildMemberToPanelMapping;

    private final int MISSION_COMPLETION_TIME_MILLIS = 15000; // TODO hardcoded, change this

    public MissionSelectionPanel(
            Runnable cancelCallback,
            Runnable confirmCallback
    ) {
        this.cancelCallback = cancelCallback;
        this.confirmCallback = confirmCallback;

        this.thisPanel = this;

        this.setLayout(new BorderLayout());
        this.selectedMembers = new GuildMember[4];
        this.selectedMemberPanels = new Models.Util.Iterable[4];
        this.selectedMemberPointer = 0;

        this.guildMemberToPanelMapping = new HashMap<>();

        JPanel memberSelectionPanel = new JPanel();
        JPanel missionDetailsPanel = new JPanel(new BorderLayout());
        missionDetailsPanel.setBackground(new Color(50, 43, 56));


        JScrollPane missionScrollPane = new JScrollPane();
        missionScrollPane.setBackground(new Color(50, 43, 56));
        JPanel missionScrollPanel = new JPanel(new BorderLayout());
        missionScrollPane.setViewportView(missionScrollPanel);
        missionScrollPanel.setBackground(new Color(50, 43, 56));
        missionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        missionDetailsPanel.add(missionScrollPane, BorderLayout.CENTER);
        missionScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        missionScrollPane.getVerticalScrollBar().setBlockIncrement(50);


        memberSelectionPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5) - SCROLL_BAR_WIDTH), HEIGHT));
        missionDetailsPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5) - SCROLL_BAR_WIDTH), HEIGHT));


        this.add(memberSelectionPanel, BorderLayout.WEST);
        this.add(missionDetailsPanel, BorderLayout.EAST);

        memberSelectionPanel.setLayout(new BoxLayout(memberSelectionPanel, BoxLayout.Y_AXIS));

        selectedPanel = new JPanel();
        JPanel listPanel = new JPanel();

        selectedPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), ((int) (HEIGHT * 0.3))));
        listPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), ((int) (HEIGHT * 0.7))));

        memberSelectionPanel.add(selectedPanel);
        memberSelectionPanel.add(listPanel);

        memberListPanel = new JPanel();
        memberListPanel.setLayout(new BoxLayout(memberListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(memberListPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);

        listPanel.setLayout(new BorderLayout());
        listPanel.add(scrollPane, BorderLayout.CENTER);

        selectedPanel.setLayout(new GridLayout(1, 4, 5, 0));

        initPlaceHolders();



        // top panel set-up (mission difficulty, area and image)

        JPanel imageHolderPanel = new JPanel(new BorderLayout());
        imageHolderPanel.setBackground(new Color(50, 43, 56));


        missionAreaLabel = new JLabel();
        missionAreaLabel.setFont(FontUtils.readFontFromFile("resources/Jomhuria-Regular.ttf", 32f));
        missionAreaLabel.setForeground(new Color(235, 227, 196));

        clippedImagePanel = new ImagePanel("resources/world-map.jpg");
        clippedImagePanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), ((int) ((WIDTH * 0.5) / 1.66))));

        missionDifficultyLabel = new JLabel();
        missionDifficultyLabel.setFont(FontUtils.readFontFromFile("resources/Jomhuria-Regular.ttf", 32f));
        missionDifficultyLabel.setForeground(new Color(195, 195, 195));

        imageHolderPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), ((int) ((WIDTH * 0.5) / 1.66))));
        imageHolderPanel.add(missionAreaLabel, BorderLayout.NORTH);
        imageHolderPanel.add(clippedImagePanel, BorderLayout.CENTER);
        imageHolderPanel.add(missionDifficultyLabel, BorderLayout.SOUTH);
        imageHolderPanel.setBorder(BorderFactory.createEmptyBorder(0, 33,0,33));

        missionScrollPanel.add(imageHolderPanel, BorderLayout.NORTH);
        // top panel end


        // center panel set-up
        JPanel missionPanel = new JPanel(new BorderLayout());
        missionPanel.setBackground(new Color(50, 43, 56));

        missionPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), (int) (HEIGHT * 0.92)));
        missionPanel.setBackground(new Color(50, 43, 56));
        missionScrollPanel.add(missionPanel, BorderLayout.CENTER);

        // contains mission title and description
        missionTextDetailPanels = new JPanel(new BorderLayout());
        missionDescriptionArea = new JTextArea();
        missionDescriptionArea.setLineWrap(true);
        missionDescriptionArea.setWrapStyleWord(true);
        missionDescriptionArea.setEditable(false);
        missionDescriptionArea.setFont(FontUtils.readFontFromFile("resources/Jomhuria-Regular.ttf", 20f));
        missionDescriptionArea.setBackground(new Color(50, 43, 56));
        missionDescriptionArea.setForeground(new Color(195, 195, 195));


        missionNameLabel = new JLabel();
        missionNameLabel.setFont(FontUtils.readFontFromFile("resources/Jomhuria-Regular.ttf", 32f));
        missionNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        missionNameLabel.setForeground(new Color(195, 195, 195));


        JPanel textWrapperPanel = new JPanel();
        textWrapperPanel.setLayout(new BoxLayout(textWrapperPanel, BoxLayout.Y_AXIS));
        textWrapperPanel.setPreferredSize(new Dimension(-1, 300)); // TODO make this calculated dynamically
        textWrapperPanel.setBackground(new Color(50, 43, 56));


        missionDescriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        textWrapperPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        textWrapperPanel.add(missionNameLabel);
        textWrapperPanel.add(missionDescriptionArea);

        missionTextDetailPanels.add(textWrapperPanel, BorderLayout.NORTH);


        // spell list set-up
        missionRequiredSpellPanel = new JPanel(new GridBagLayout());
        missionRequiredSpellPanel.setBackground(Color.RED);
        spellPanelGbc = new GridBagConstraints();
        spellPanelGbc.insets = new Insets(1,0,1,0);
        spellPanelGbc.gridy = 0;
        spellPanelGbc.gridx = 0;
        spellPanelGbc.weightx = 1.0;
        spellPanelGbc.weighty = 0.0;
        spellPanelGbc.anchor = GridBagConstraints.NORTH;
        spellPanelGbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < 4; i++) {
            spellPanelGbc.gridy = i;
            RoundedPanel roundedPanel = new RoundedPanel(new Dimension(/*-1 doesn't work (does not stretch as predicted in gbc)*/ 50, 50), 10, ColorUtils.genRandomColor());
            missionRequiredSpellPanel.add(roundedPanel, spellPanelGbc);
        }

        missionTextDetailPanels.add(missionRequiredSpellPanel, BorderLayout.CENTER);
        // spell list set-up


        missionPanel.add(missionTextDetailPanels, BorderLayout.CENTER);

        textDescriptionPanelHeight = 1.0; //setup because they were final so it's needed

        // center panel end



        // bottom panel set-up start
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 0, 100));

        JButton confirmButton = new JButton("confirm");
        JButton cancelButton = new JButton("cancel");

        buttonPanel.add(confirmButton, BorderLayout.WEST);
        buttonPanel.add(cancelButton, BorderLayout.EAST);

        confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        confirmButton.setPreferredSize(new Dimension(-1, 30));

        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cancelCallback.run();
                resetPanels();
            }
        });

        confirmButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Set<Spell> requiredSpells = new HashSet<>(selectedMission.getRequiredSpells());
                if (selectedMemberPointer > 1){
                    for (int i = 0; i < selectedMemberPointer; i++) {
                        selectedMembers[i].getKnownSpells().forEach(s -> {
//                            if (requiredSpells.size() == 0){
//                                return;
//                            }
                            requiredSpells.remove(s.getSpell()); // TODO perhaps add a short circuit here?
                        });
                    }
                    if (requiredSpells.size() == 0){
                        for (int i = 0; i < selectedMemberPointer; i++) {
                            selectedMembers[i].assignNewMission(selectedMission);
                        }

                        selectedMission.setMissionCompletionTimeMillis(MISSION_COMPLETION_TIME_MILLIS);
                        final GuildMember[] guildMembers = Arrays.copyOf(selectedMembers, selectedMemberPointer);
                        selectedMission.startMission(()->{
                            for (GuildMember guildMember : guildMembers) {
                                guildMember.setMemberState(MemberState.ON_STANDBY);
                                thisPanel.revalidate();
                                thisPanel.repaint();
                            }
                            confirmCallback.run();
                        });
                        cancelCallback.run();
                        System.out.println("Start mission");
                    }else {
                        System.out.println("team does not fulfil mission reqs");
                    }
                }else{
                    System.out.println("team too small");
                }
                resetPanels();
            }
        });

        buttonPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), (int) (HEIGHT * 0.08)));
        missionScrollPanel.add(buttonPanel, BorderLayout.SOUTH);

        // bottom panel set-up end

        }

    public void setMissionDispatcher(GuildMember missionDispatcher) {
        this.missionDispatcher = missionDispatcher;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (missionDispatcher != null){
            missionDispatcher.getInvalidGuildMembers().forEach(member -> {
                JPanel panel;
                if ((panel = guildMemberToPanelMapping.get(member))!= null){
                    memberListPanel.remove(panel);
                    guildMemberToPanelMapping.remove(member);
                    revalidate();
                    repaint();
                }
            });
            populateMemberSelectionList();
        }

    }

    public void populateMemberSelectionList(){
        for (GuildMember member : missionDispatcher.getValidGuildMembers()) {
            if (!guildMemberToPanelMapping.containsKey(member)){
                GuildMemberSelectionTile comp = new GuildMemberSelectionTile(member, WIDTH, (panel) -> {
                int index;
                if ((index = findMemberIndex(member)) != -1) {
                    removeComponentFromMembers(((SelectedMemberPanel) selectedMemberPanels[index]));
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                } else if (selectedMemberPointer < 4) {
                    panel.setBorder(BorderFactory.createLineBorder(new Color(1, 136, 56), 2));

                    selectedMembers[selectedMemberPointer] = member;
                    selectedPanel.remove((PlaceHolderPanel) selectedMemberPanels[selectedMemberPointer]);

                    selectedMemberPanels[selectedMemberPointer] = new SelectedMemberPanel(member, new Dimension(((int) (WIDTH * 0.125)), ((int) (HEIGHT * 0.35))), selectedMemberPointer);
                    selectedPanel.add((SelectedMemberPanel) selectedMemberPanels[selectedMemberPointer], selectedMemberPointer);

                    thisPanel.revalidate();
                    thisPanel.repaint();

                    ((SelectedMemberPanel) selectedMemberPanels[selectedMemberPointer]).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                    addMouseClickListenerToMemberPanelHelper((JPanel) selectedMemberPanels[selectedMemberPointer], panel);
                    selectedMemberPointer++;
                    }
                });
                memberListPanel.add(comp);
                guildMemberToPanelMapping.put(member, comp);
            }
        }
    }
    private int findMemberIndex(GuildMember member){
        for (int i = 0; i < selectedMemberPointer; i++) {
            if (selectedMembers[i] == member){
                return i;
            }
        }
        return -1;
    }
    private void addMouseClickListenerToMemberPanelHelper(JPanel memberPanel, JPanel memberListSelectionPanel){
        memberPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Component component = e.getComponent();
                memberListSelectionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                removeComponentFromMembers(component);
            }
        });
    }

    private void removeComponentFromMembers(Component component){
        selectedPanel.remove(component);
        int selectedGuildMemberIndex = ((SelectedMemberPanel) component).getIndex();
        for (int i = selectedGuildMemberIndex + 1; i < 4; i++) {
            Models.Util.Iterable selectedMemberPanel = selectedMemberPanels[i];

            selectedMemberPanel.setIndex(selectedMemberPanel.getIndex() - 1);

            if (selectedMemberPanel instanceof PlaceHolderPanel){
                selectedPanel.add(((PlaceHolderPanel) selectedMemberPanel), selectedMemberPanel.getIndex());
            }else if(selectedMemberPanel instanceof SelectedMemberPanel){
                selectedMembers[i-1] = selectedMembers[i];
                selectedMembers[i] = null;
                selectedPanel.add(((SelectedMemberPanel) selectedMemberPanel), selectedMemberPanel.getIndex());
            }else {
                System.out.println(":(");
            }

            selectedMemberPanels[selectedMemberPanel.getIndex()] = selectedMemberPanel;
        }

        int index = selectedMemberPanels.length - 1;
        PlaceHolderPanel comp = new PlaceHolderPanel(new Dimension(((int) (WIDTH * 0.125)), ((int) (HEIGHT * 0.35))), index);
        selectedMemberPanels[index] = comp;
        selectedPanel.add(comp);
        selectedMemberPointer--;
        thisPanel.revalidate();
        thisPanel.repaint();
    }

    public void setSelectedMission(Mission selectedMission) {
        this.selectedMission = selectedMission;
        missionAreaLabel.setText(String.format("mission area: %s", selectedMission.getTerritory().getTerritoryName()));
        missionAreaLabel.setHorizontalAlignment(SwingConstants.CENTER);

        missionDifficultyLabel.setText(String.format("difficulty: %s", selectedMission.getDifficulty().name())); //TODO seems to not actually be doing it's job
        missionDifficultyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        double xw = 720;
        double yh = 480;

        int missionX = selectedMission.getTerritory().getTerritoryCoordinates().x();
        int missionY = selectedMission.getTerritory().getTerritoryCoordinates().y();

        int imageWidth = clippedImagePanel.getOriginalImageWidth();
        int imageHeight = clippedImagePanel.getOriginalImageHeight();

        double xScale = imageWidth / xw;
        double yScale = imageHeight / yh;

        missionX = (int) (missionX * xScale);
        missionY = (int) (missionY * yScale);

        int zoomWidth = 300;
        int zoomHeight = 200;

        int halfWidth = zoomWidth / 2;
        int halfHeight = zoomHeight / 2;

        int x1 = Math.max(0, missionX - halfWidth);
        int x2 = Math.min(imageWidth, missionX + halfWidth);
        int y1 = Math.max(0, missionY - halfHeight);
        int y2 = Math.min(imageHeight, missionY + halfHeight);

        if (x1 == 0) {
            x2 = Math.min(imageWidth, zoomWidth);
        } else if (x2 == imageWidth) {
            x1 = Math.max(0, imageWidth - zoomWidth);
        }

        if (y1 == 0) {
            y2 = Math.min(imageHeight, zoomHeight);
        } else if (y2 == imageHeight) {
            y1 = Math.max(0, imageHeight - zoomHeight);
        }

        missionNameLabel.setText(String.format("mission title: %s", selectedMission.getName()));
        missionDescriptionArea.setText(selectedMission.getDescription());

        selectedMission.getRequiredSpells().forEach((s)->{
            RoundedPanel roundedPanel = new RoundedPanel(new Dimension(/*-1 doesn't work (does not stretch as predicted in gbc)*/ 50, 50), 10, ColorUtils.genRandomColor());
//            missionRequiredSpellPanel.add(roundedPanel, spellPanelGbc);
//            spellPanelGbc.gridy++;
        });

        clippedImagePanel.setClip(x1, y1, x2, y2);
    }

    private void initPlaceHolders(){
        for (int i = 0; i < 4; i++) {
            PlaceHolderPanel panel = new PlaceHolderPanel(new Dimension(((int) (WIDTH * 0.125)), ((int) (HEIGHT * 0.35))), i);
            selectedPanel.add(panel, i);
            selectedMemberPanels[i] = panel;
        }
    }

    private void resetPanels(){
        for (int i = 0; i < 4; i++) {
            selectedPanel.remove((Component) selectedMemberPanels[i]);
        }
        int accessibleChildrenCount = memberListPanel.getAccessibleContext().getAccessibleChildrenCount();
        Component[] components = memberListPanel.getComponents();

        for (int i = 0; i < accessibleChildrenCount; i++) {
            Component component = components[i];
            if (component instanceof GuildMemberSelectionTile) {
                ((GuildMemberSelectionTile) component).setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
        }

        selectedMemberPointer = 0;
        selectedMemberPanels = new Models.Util.Iterable[4];
        initPlaceHolders();
        selectedMembers = new GuildMember[4];
    }
}

class MissionRequirementTile extends RoundedPanel{

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

class GuildMemberSelectionTile extends JPanel{
    private final JPanel thisPanel;

    private Color color;
    private final GenericJPanelCallback selectCallback;

    public GuildMemberSelectionTile(GuildMember member, int width, GenericJPanelCallback callback) {
        this.thisPanel = this;
        this.selectCallback = callback;

        int availableWidth = (int)(width * 0.5) - 16 - 10;
        this.setPreferredSize(new Dimension(availableWidth - 4, 120 - 4));
        this.color = new Color(108,108,108);
        this.setBackground(color);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setLayout(new BorderLayout());

        JPanel lPanel = new JPanel();
        lPanel.setBackground(ColorUtils.transparent);
        lPanel.setPreferredSize(new Dimension(((int) (this.getPreferredSize().width * 0.42)), this.getPreferredSize().height));

        JPanel rPanel = new JPanel();
        rPanel.setBackground(ColorUtils.transparent);
        rPanel.setPreferredSize(new Dimension(((int) (this.getPreferredSize().width * 0.58)), this.getPreferredSize().height));

        JLabel bestLabel = new JLabel("Best spells", SwingConstants.LEFT);
        bestLabel.setPreferredSize(new Dimension(((int) (this.getPreferredSize().width * 0.58)), 32));

        bestLabel.setFont(FontUtils.readFontFromFile("resources/Jomhuria-Regular.ttf", 32f));

        bestLabel.setForeground(new Color(195,195,195));


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

        Font labelFont = FontUtils.readFontFromFile("resources/Jomhuria-Regular.ttf", 24f);

        Color panelColor = new Color(94,94,94);
        Dimension paddingDim = new Dimension(-1, 1);

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
        glue.setBackground(ColorUtils.transparent);
        rPanel.add(glue, rgbc);

        lPanel.setLayout(new GridBagLayout());
        GridBagConstraints lgbc = new GridBagConstraints();

        lgbc.gridx = 0;
        lgbc.gridy = 0;
        lgbc.weightx = 0.0;
        lgbc.weighty = 0.0;
        lgbc.fill = GridBagConstraints.BOTH;
        lgbc.anchor = GridBagConstraints.NORTH;

        ImagePanel panel = new ImagePanel(member.getChosenIcon());
        panel.setBackground(ColorUtils.transparent);
        panel.setPreferredSize(new Dimension(77,77));
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

class SelectedMemberPanel extends JPanel implements Models.Util.Iterable {
    private int index;
    public SelectedMemberPanel(GuildMember guildMember, Dimension dimension, int index) {
        this.index = index;
        this.setLayout(new BorderLayout());

        ImagePanel imagePanel = new ImagePanel(String.format("resources/icon%s.png", guildMember.getChosenIcon()));
        imagePanel.setPreferredSize(new Dimension(dimension.width, dimension.width));
        this.add(imagePanel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        this.setPreferredSize(new Dimension(dimension.width, dimension.height - dimension.width));
        this.add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout());

        Font font = new Font("Britannic Bold", Font.PLAIN, 15);

        JLabel rankLabel = new JLabel(guildMember.getRank().name(), SwingConstants.CENTER);
        panel.add(rankLabel, BorderLayout.NORTH);
        rankLabel.setFont(font);

        JLabel nameLabel = new JLabel(guildMember.getName(), SwingConstants.CENTER);
        panel.add(nameLabel, BorderLayout.SOUTH);
        nameLabel.setFont(font);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

class PlaceHolderPanel extends JPanel implements Models.Util.Iterable {
    private int index;
    public PlaceHolderPanel(Dimension dimension, int index) {

        setIndex(index);

        this.setPreferredSize(dimension);
        Color color = new Color(233, 224, 210);
        this.setLayout(new BorderLayout());
        JLabel label = new JLabel("+", SwingConstants.CENTER);
        label.setFont(new Font("Britannic Bold", Font.BOLD, 25));
        label.setForeground(new Color(54, 0, 0));
        this.setBackground(color);
        this.add(label, BorderLayout.CENTER);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}