package Models.Panels;

import Models.Guild.GuildMember;
import Models.Guild.MemberState;
import Models.Magic.Spell;
import Models.Mission.Mission;
import Models.Util.ColorUtils;
import Models.Util.GenericJPanelCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private final JLabel missionNameLabel;
    private final JLabel missionDescriptionLabel;
    private final JLabel missionDifficultyLabel;
    private final JPanel missionRequirementsScrollablePanel;
    private final JPanel missionTextDetailPanels;

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
        JPanel missionDetailsPanel = new JPanel();

        memberSelectionPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5) - SCROLL_BAR_WIDTH), HEIGHT));
        missionDetailsPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5) - SCROLL_BAR_WIDTH), HEIGHT));

        memberSelectionPanel.setBackground(Color.RED);
        missionDetailsPanel.setBackground(Color.GREEN);

        this.add(memberSelectionPanel, BorderLayout.WEST);
        this.add(missionDetailsPanel, BorderLayout.EAST);

        memberSelectionPanel.setLayout(new BoxLayout(memberSelectionPanel, BoxLayout.Y_AXIS));

        selectedPanel = new JPanel();
        JPanel listPanel = new JPanel();

        selectedPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), ((int) (HEIGHT * 0.3))));
        listPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), ((int) (HEIGHT * 0.7))));

        selectedPanel.setBackground(Color.BLUE);
        listPanel.setBackground(Color.MAGENTA);

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

        missionDetailsPanel.setBackground(Color.BLUE);
        missionDetailsPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new GridLayout(1, 2, 0, 100));

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
        missionDetailsPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel missionPanel = new JPanel();
        missionPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), (int) (HEIGHT * 0.92)));
        missionDetailsPanel.add(missionPanel, BorderLayout.NORTH);
        missionPanel.setLayout(new BorderLayout());

        JPanel imageHolderPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Mission area", SwingConstants.CENTER);

        clippedImagePanel = new ImagePanel("resources/world-map.jpg");
        clippedImagePanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), ((int) ((WIDTH * 0.5) / 1.66))));

        missionPanel.add(imageHolderPanel, BorderLayout.NORTH);
        imageHolderPanel.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), ((int) ((WIDTH * 0.5) / 1.66))));
        imageHolderPanel.add(label, BorderLayout.NORTH);
        imageHolderPanel.add(clippedImagePanel, BorderLayout.CENTER);

        missionTextDetailPanels = new JPanel();
        missionTextDetailPanels.setPreferredSize(new Dimension(((int) (WIDTH * 0.5)), (int) ((HEIGHT * 0.92) - ((WIDTH * 0.5) / 1.66))));
        missionTextDetailPanels.setBackground(Color.ORANGE);
        missionPanel.add(missionTextDetailPanels, BorderLayout.SOUTH);

        JPanel textDescriptionPanel = new JPanel();

        textDescriptionPanelHeight = 0.30;
        textDescriptionPanel.setPreferredSize(new Dimension(missionTextDetailPanels.getPreferredSize().width, ((int) (missionTextDetailPanels.getPreferredSize().height * textDescriptionPanelHeight))));
        textDescriptionPanel.setLayout(new BorderLayout());
        textDescriptionPanel.setBackground(Color.PINK);
        missionTextDetailPanels.add(textDescriptionPanel, BorderLayout.NORTH);

        missionNameLabel = new JLabel();
        missionDescriptionLabel = new JLabel();
        missionDifficultyLabel = new JLabel();

        textDescriptionPanel.add(missionNameLabel, BorderLayout.NORTH);
        textDescriptionPanel.add(missionDescriptionLabel, BorderLayout.CENTER);
        textDescriptionPanel.add(missionDifficultyLabel, BorderLayout.SOUTH);

        missionNameLabel.setForeground(Color.BLACK);
        missionDescriptionLabel.setForeground(Color.BLACK);
        missionDifficultyLabel.setForeground(Color.BLACK);

        int panelWidth = missionTextDetailPanels.getPreferredSize().width;
        int panelHeight = (int) (missionTextDetailPanels.getPreferredSize().height * (1 - textDescriptionPanelHeight) - 48);

        Dimension dim = new Dimension(panelWidth, panelHeight);

        JPanel missionRequirementsPanel = new JPanel(new BorderLayout());
        missionRequirementsPanel.setMaximumSize(dim);
        missionRequirementsPanel.setPreferredSize(dim);
        missionTextDetailPanels.add(missionRequirementsPanel, BorderLayout.SOUTH);

        JScrollPane missionRequirementsScrollPane = new JScrollPane();
        missionRequirementsPanel.add(missionRequirementsScrollPane, BorderLayout.CENTER);

        missionRequirementsScrollablePanel = new JPanel();

        missionRequirementsScrollPane.setViewportView(missionRequirementsScrollablePanel);
        missionRequirementsScrollablePanel.setLayout(new BoxLayout(missionRequirementsScrollablePanel, BoxLayout.Y_AXIS));
        missionRequirementsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        missionRequirementsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        missionRequirementsScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        missionRequirementsScrollPane.getVerticalScrollBar().setBlockIncrement(50);
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
                GuildMemberSelectionTile comp = new GuildMemberSelectionTile(WIDTH, (panel) -> {
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

        missionNameLabel.setText(selectedMission.getName());
        missionNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        missionDescriptionLabel.setText(selectedMission.getDescription());
        missionDescriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        missionDifficultyLabel.setText(selectedMission.getDifficulty().name());
        missionDifficultyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        selectedMission.getRequiredSpells().forEach((s)->{
            int panelWidth = missionTextDetailPanels.getPreferredSize().width;
            int panelHeight = (int) ((missionTextDetailPanels.getPreferredSize().height * (1 - textDescriptionPanelHeight) - 48) * 0.333333);
            missionRequirementsScrollablePanel.add(new MissionRequirementTile(new Dimension(panelWidth, panelHeight), s));
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

class MissionRequirementTile extends JPanel{

    private final JLabel spellLabel;
    private Spell spell;
    public MissionRequirementTile(Dimension dim, Spell spell) {
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

    public GuildMemberSelectionTile(int width, GenericJPanelCallback callback) {
        this.thisPanel = this;
        this.selectCallback = callback;

        int availableWidth = (int)(width * 0.5) - 16 - 10;
        this.setPreferredSize(new Dimension(availableWidth - 4, 100 - 4));
        this.color = new Color(65,65,65);
        this.setBackground(color);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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