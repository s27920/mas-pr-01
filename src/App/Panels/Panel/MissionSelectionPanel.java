package App.Panels.Panel;

import App.Models.Guild.GuildMember;
import App.Models.Magic.RequiredSpell;
import App.Models.Magic.Spells.Spell;
import App.Models.Mission.Mission;
import App.Models.Mission.MissionStatus;
import App.Panels.Components.GuildMemberHeaderPanel;
import App.Panels.Components.SelectedMemberPanel;
import App.Panels.Components.GuildMemberSelectionTile;
import App.Panels.GuiUtil.ImagePanel;
import App.Panels.GuiUtil.PlaceHolderPanel;
import App.Panels.GuiUtil.RoundedPanel;
import App.StaticUtils.ColorUtils;
import App.StaticUtils.ErrorUtils;
import App.StaticUtils.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MissionSelectionPanel extends JPanel {
    private final JPanel thisPanel;

    private GuildMember missionDispatcher;
    private Mission selectedMission;

    private final JPanel memberListPanel;
    private final JPanel selectedPanel;

    private final int WIDTH = 720;
    private final int HEIGHT = 560;

    private GuildMember[] selectedMembers;
    private App.Util.Iterable[] selectedMemberPanels;
    private int selectedMemberPointer;

    private final ImagePanel clippedImagePanel;
    private final JLabel missionDifficultyLabel;
    private final JPanel textWrapperPanel;
    private final JTextArea missionDescriptionArea;
    private final JPanel missionRequiredSpellPanel;
    private RoundedPanel[] requiredSpells;
    private final GridBagConstraints spellPanelGbc;


    private final JLabel missionNameLabel;
    private final JLabel missionAreaLabel;
    private final JPanel missionCenterPanel;
    private final GuildMemberHeaderPanel headerPanel;


    private final int REQUIRED_SPELL_LIST_TILE_HORIZONTAL_MARGIN = 21;
    private final int REQUIRED_SPELL_LIST_TILE_VERTICAL_MARGIN = 1;

    private final int SCROLL_BAR_WIDTH;

    private final HashMap<GuildMember, GuildMemberSelectionTile> guildMemberToPanelMapping;

    private Runnable onMissionCompletionCallback;
    private final JLayeredPane layeredPane;
    private final int[] renderDims = new int[2];

    public MissionSelectionPanel(
            Runnable cancelCallback,
            Runnable returnCallback
    ) {

        this.thisPanel = this;

        layeredPane = new JLayeredPane();
        this.setLayout(new BorderLayout());
        this.add(layeredPane, BorderLayout.CENTER);

        JPanel mainContentPanel = new JPanel(new BorderLayout());

        this.selectedMembers = new GuildMember[4];
        this.selectedMemberPanels = new App.Util.Iterable[4];
        this.selectedMemberPointer = 0;

        this.guildMemberToPanelMapping = new HashMap<>();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(WIDTH, 40));
        topPanel.setBackground(ColorUtils.CARBON);
        headerPanel = new GuildMemberHeaderPanel(()->{
            returnCallback.run();
            resetPanels();
            cleanPanels();
        });
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40));
        topPanel.add(headerPanel);

        JPanel contentWrapper = new JPanel(new BorderLayout());

        mainContentPanel.add(topPanel, BorderLayout.NORTH);
        mainContentPanel.add(contentWrapper, BorderLayout.CENTER);

        JPanel memberSelectionPanel = new JPanel();
        JPanel missionDetailsPanel = new JPanel(new BorderLayout());
        missionDetailsPanel.setBackground(ColorUtils.DARK_PURPLE);

        SCROLL_BAR_WIDTH = UIManager.getInt("ScrollBar.width");

        int halfWidth = WIDTH / 2;
        memberSelectionPanel.setPreferredSize(new Dimension((halfWidth - (SCROLL_BAR_WIDTH)), HEIGHT - 40)); // Subtract 40 for top panel

        JScrollPane missionScrollPane = new JScrollPane();
        missionScrollPane.setBackground(ColorUtils.DARK_PURPLE);
        JPanel missionScrollPanel = new JPanel(new BorderLayout());
        missionScrollPane.setViewportView(missionScrollPanel);
        missionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        missionDetailsPanel.add(missionScrollPane, BorderLayout.CENTER);
        missionScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        missionScrollPane.getVerticalScrollBar().setBlockIncrement(50);

        contentWrapper.add(memberSelectionPanel, BorderLayout.WEST);
        contentWrapper.add(missionDetailsPanel, BorderLayout.CENTER);

        memberSelectionPanel.setLayout(new BoxLayout(memberSelectionPanel, BoxLayout.Y_AXIS));

        selectedPanel = new JPanel();
        JPanel listPanel = new JPanel();

        selectedPanel.setPreferredSize(new Dimension(halfWidth, ((int) ((HEIGHT - 40) * 0.3))));
        selectedPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        listPanel.setPreferredSize(new Dimension(halfWidth, ((int) ((HEIGHT - 40) * 0.7))));

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
        imageHolderPanel.setBackground(ColorUtils.DARK_PURPLE);

        missionAreaLabel = new JLabel();
        missionAreaLabel.setFont(FontUtils.getJomhuriaFont(32));
        missionAreaLabel.setForeground(new Color(235, 227, 196));

        clippedImagePanel = ImagePanel.getWorldMap();
        int availableWidth = halfWidth - SCROLL_BAR_WIDTH * 2;
        clippedImagePanel.setPreferredSize(new Dimension(availableWidth, (int) (availableWidth / 1.66)));
        missionDifficultyLabel = new JLabel();
        missionDifficultyLabel.setFont(FontUtils.getJomhuriaFont(32));
        missionDifficultyLabel.setForeground(new Color(195, 195, 195));

        imageHolderPanel.setPreferredSize(new Dimension(availableWidth, ((int) (availableWidth / 1.66))));
        imageHolderPanel.add(missionAreaLabel, BorderLayout.NORTH);
        imageHolderPanel.add(clippedImagePanel, BorderLayout.CENTER);
        imageHolderPanel.add(missionDifficultyLabel, BorderLayout.SOUTH);
        imageHolderPanel.setBorder(BorderFactory.createEmptyBorder(0, 33, 0, 33));

        missionScrollPanel.add(imageHolderPanel, BorderLayout.NORTH);
        missionScrollPanel.setBackground(ColorUtils.DARK_PURPLE);
        // top panel end

        // center panel set-up
        missionCenterPanel = new JPanel(new BorderLayout());
        missionCenterPanel.setBackground(ColorUtils.DARK_PURPLE);

        missionCenterPanel.setPreferredSize(new Dimension(availableWidth, (int) ((HEIGHT - 40) * 0.92)));
        missionCenterPanel.setBackground(ColorUtils.DARK_PURPLE);
        missionScrollPanel.add(missionCenterPanel, BorderLayout.CENTER);

        // contains mission title and description
        JPanel missionTextDetailPanels = new JPanel(new BorderLayout());
        missionDescriptionArea = new JTextArea() {
            @Override
            public boolean contains(int x, int y) {
                return false;
            }
        };
        missionDescriptionArea.setLineWrap(true);
        missionDescriptionArea.setWrapStyleWord(true);
        missionDescriptionArea.setEditable(false);
        missionDescriptionArea.setFont(FontUtils.getJomhuriaFont(20));
        missionDescriptionArea.setBackground(ColorUtils.DARK_PURPLE);
        missionDescriptionArea.setForeground(new Color(195, 195, 195));

        missionNameLabel = new JLabel();
        missionNameLabel.setFont(FontUtils.getJomhuriaFont(32));
        missionNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        missionNameLabel.setForeground(new Color(195, 195, 195));

        textWrapperPanel = new JPanel();
        textWrapperPanel.setLayout(new BoxLayout(textWrapperPanel, BoxLayout.Y_AXIS));
        textWrapperPanel.setPreferredSize(new Dimension(-1, -1));
        textWrapperPanel.setBackground(ColorUtils.DARK_PURPLE);

        missionDescriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        textWrapperPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        textWrapperPanel.add(missionNameLabel);
        textWrapperPanel.add(missionDescriptionArea);

        missionTextDetailPanels.add(textWrapperPanel, BorderLayout.NORTH);

        // spell list set-up
        missionRequiredSpellPanel = new JPanel(new GridBagLayout());
        missionRequiredSpellPanel.setBackground(ColorUtils.DARK_PURPLE);
        spellPanelGbc = new GridBagConstraints();
        spellPanelGbc.insets = new Insets(REQUIRED_SPELL_LIST_TILE_VERTICAL_MARGIN, REQUIRED_SPELL_LIST_TILE_HORIZONTAL_MARGIN, REQUIRED_SPELL_LIST_TILE_VERTICAL_MARGIN, REQUIRED_SPELL_LIST_TILE_HORIZONTAL_MARGIN);
        spellPanelGbc.gridy = 0;
        spellPanelGbc.gridx = 0;
        spellPanelGbc.weightx = 1.0;
        spellPanelGbc.weighty = 0.0;
        spellPanelGbc.anchor = GridBagConstraints.NORTH;
        spellPanelGbc.fill = GridBagConstraints.HORIZONTAL;

        missionTextDetailPanels.add(missionRequiredSpellPanel, BorderLayout.SOUTH);
        // spell list set-up

        missionCenterPanel.add(missionTextDetailPanels, BorderLayout.CENTER);

        // center panel end

        // bottom panel set-up start
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 0, 100));

        JButton confirmButton = new JButton("confirm");
        JButton cancelButton = new JButton("cancel");
        confirmButton.setFont(FontUtils.getJomhuriaFont(28));
        cancelButton.setFont(FontUtils.getJomhuriaFont(28));

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
                cleanPanels();
            }
        });

        confirmButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (startMission()) {
                    cancelCallback.run();
                    resetPanels();
                    cleanPanels();
                }
            }
        });

        buttonPanel.setPreferredSize(new Dimension(availableWidth, (int) ((500 - 40) * 0.08)));
        missionScrollPanel.add(buttonPanel, BorderLayout.SOUTH);

        // bottom panel set-up end

        mainContentPanel.setBounds(0, 0, /*TODO actual render size is 706 not WIDTH (720)*/706, HEIGHT - 40);
        layeredPane.setPreferredSize(new Dimension(706, HEIGHT - 40));

        layeredPane.add(mainContentPanel, JLayeredPane.DEFAULT_LAYER);
    }

    public boolean startMission() {

        Set<RequiredSpell> requiredSpellsCopy = new HashSet<>(selectedMission.getRequiredSpellsSet());
        if (selectedMemberPointer > 1) {
            for (int i = 0; i < selectedMemberPointer; i++) {
                selectedMembers[i].getKnownSpells().forEach(s -> {
                    if (requiredSpellsCopy.size() == 0) {
                        return;
                    }
                    if (selectedMission.hasCompatibleSpell(s)){
                        requiredSpellsCopy.stream().filter(sp -> sp.getRequiredSpell().equals(s.getSpell())).findAny().ifPresent(requiredSpellsCopy::remove);
                    }
                });
            }
            if (requiredSpellsCopy.size() == 0) {
                GuildMember missionLeader = selectedMembers[0];
                missionLeader.assignNewMission(selectedMission).setMissionLeader(missionLeader);

                for (int i = 1; i < selectedMemberPointer; i++) {
                    GuildMember missionMember = selectedMembers[i];
                    missionMember.assignNewMission(selectedMission).setMissionLeader(missionMember);
                }

                selectedMission.getMissionCompletionTime();
                selectedMission.startMission();
            } else {
                ErrorUtils.showError(layeredPane, "team does not fulfil mission requirements. Please review the required spells.", renderDims);
                return false;
            }
        } else {
            ErrorUtils.showError(layeredPane, "team too small. You need to select at least 2 guild members.", renderDims);
            return false;
        }

        resetPanels();
        return true;
    }

    public void setMissionDispatcher(GuildMember missionDispatcher) {
        this.missionDispatcher = missionDispatcher;
        headerPanel.setLoggedInMember(missionDispatcher);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (missionDispatcher != null) {
            missionDispatcher.getGuild().getInvalidGuildMembers().forEach(member -> {
                JPanel panel;
                if ((panel = guildMemberToPanelMapping.get(member)) != null) {
                    memberListPanel.remove(panel);
                    guildMemberToPanelMapping.remove(member);
                    revalidate();
                    repaint();
                }
            });
            populateMemberSelectionList();
        }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        this.renderDims[0] = width;
        this.renderDims[1] = height;
    }

    public void populateMemberSelectionList() {
        for (GuildMember member : missionDispatcher.getGuild().getValidGuildMembers()) {
            if (!guildMemberToPanelMapping.containsKey(member)) {
                GuildMemberSelectionTile comp = new GuildMemberSelectionTile(member, WIDTH, (panel) -> {
                    int index;
                    if ((index = findMemberIndex(member)) != -1) {
                        removeComponentFromMembers(((SelectedMemberPanel) selectedMemberPanels[index]));
                        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                    } else if (selectedMemberPointer < 4) {
                        panel.setBorder(BorderFactory.createLineBorder(new Color(1, 136, 56), 2));

                        selectedMembers[selectedMemberPointer] = member;
                        selectedPanel.remove((PlaceHolderPanel) selectedMemberPanels[selectedMemberPointer]);

                        selectedMemberPanels[selectedMemberPointer] = new SelectedMemberPanel(
                                member, new Dimension(((int) (WIDTH * 0.125)), ((int) (HEIGHT * 0.35))),
                                selectedMemberPointer
                        );

                        ((SelectedMemberPanel) selectedMemberPanels[0]).makeLeader();

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

    private int findMemberIndex(GuildMember member) {
        for (int i = 0; i < selectedMemberPointer; i++) {
            if (selectedMembers[i] == member) {
                return i;
            }
        }
        return -1;
    }

    private void addMouseClickListenerToMemberPanelHelper(JPanel memberPanel, JPanel memberListSelectionPanel) {
        memberPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Component component = e.getComponent();
                memberListSelectionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                removeComponentFromMembers(component);
            }
        });
    }

    private void removeComponentFromMembers(Component component) {
        selectedPanel.remove(component);
        int selectedGuildMemberIndex = ((SelectedMemberPanel) component).getIndex();
        for (int i = selectedGuildMemberIndex + 1; i < 4; i++) {
            App.Util.Iterable selectedMemberPanel = selectedMemberPanels[i];

            selectedMemberPanel.setIndex(selectedMemberPanel.getIndex() - 1);

            if (selectedMemberPanel instanceof PlaceHolderPanel) {
                selectedPanel.add(((PlaceHolderPanel) selectedMemberPanel), selectedMemberPanel.getIndex());
            } else if (selectedMemberPanel instanceof SelectedMemberPanel) {
                selectedMembers[i - 1] = selectedMembers[i];
                selectedMembers[i] = null;
                selectedPanel.add(((SelectedMemberPanel) selectedMemberPanel), selectedMemberPanel.getIndex());
            } else {
                System.out.println(":(");
            }

            selectedMemberPanels[selectedMemberPanel.getIndex()] = selectedMemberPanel;
        }

        int index = selectedMemberPanels.length - 1;
        PlaceHolderPanel comp = new PlaceHolderPanel(new Dimension(((int) (WIDTH * 0.125)) - 10, ((int) (HEIGHT * 0.35)) - 10), index);
        selectedMemberPanels[index] = comp;
        selectedPanel.add(comp);
        selectedMemberPointer--;
        if (selectedMemberPointer > 0){
            ((SelectedMemberPanel) selectedMemberPanels[0]).makeLeader();
        }
        thisPanel.revalidate();
        thisPanel.repaint();
    }

    private int calculateTextAreaHeight(String text, Font font, int availableWidth) {
        JTextArea tempTextArea = new JTextArea();
        tempTextArea.setFont(font);
        tempTextArea.setLineWrap(true);
        tempTextArea.setWrapStyleWord(true);
        tempTextArea.setEditable(false);

        tempTextArea.setText(text);
        tempTextArea.setSize(availableWidth, Integer.MAX_VALUE);

        return tempTextArea.getPreferredSize().height;
    }

    public void setSelectedMission(Mission selectedMission) {
        if (selectedMission.getStatus() == MissionStatus.CREATED) {
            this.selectedMission = selectedMission;
            missionAreaLabel.setText(String.format("mission area: %s", selectedMission.getTerritory().getTerritoryName()));
            missionAreaLabel.setHorizontalAlignment(SwingConstants.CENTER);

            missionDifficultyLabel.setText(String.format("difficulty: %s", selectedMission.getDifficulty().name()));
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
            int height = calculateTextAreaHeight(selectedMission.getDescription(), missionDescriptionArea.getFont(), WIDTH / 2 - SCROLL_BAR_WIDTH * 2);
            Dimension missionDescriptionPreferredSize = new Dimension(-1, height + 20 + missionNameLabel.getFont().getSize());
            textWrapperPanel.setPreferredSize(missionDescriptionPreferredSize); // 20 for the padding box

            Set<RequiredSpell> selectedMissionRequiredSpellsSet = selectedMission.getRequiredSpellsSet();
            int requiredSpellCount = selectedMissionRequiredSpellsSet.size();
            requiredSpells = new RoundedPanel[requiredSpellCount];

            int panelHeight = 36;
            // TODO requiredSpellsSize bit too big but it'll do
            Dimension requiredSpellsListPreferredSize = new Dimension(WIDTH / 2, REQUIRED_SPELL_LIST_TILE_VERTICAL_MARGIN + (panelHeight + REQUIRED_SPELL_LIST_TILE_VERTICAL_MARGIN) * requiredSpellCount);
            missionRequiredSpellPanel.setPreferredSize(requiredSpellsListPreferredSize);

            missionCenterPanel.setPreferredSize(new Dimension(-1, requiredSpellsListPreferredSize.height + missionDescriptionPreferredSize.height));

            selectedMissionRequiredSpellsSet.forEach((rs) -> {

                Spell selectedSpell = rs.getRequiredSpell();

                RoundedPanel roundedPanel = new RoundedPanel(new Dimension(-1, panelHeight), 10, ColorUtils.GREY);
                roundedPanel.setLayout(new BorderLayout());

                JLabel spellNameLabel = new JLabel(selectedSpell.getName(), SwingConstants.RIGHT);
                JLabel spellLevelLabel = new JLabel(String.format("lvl. %s", rs.getKnownLevel()), SwingConstants.LEFT);
                int halfAvailableWidth = (WIDTH / 2 - SCROLL_BAR_WIDTH * 2 - REQUIRED_SPELL_LIST_TILE_HORIZONTAL_MARGIN * 2) / 2;
                Dimension labelDim = new Dimension(halfAvailableWidth, panelHeight);

                spellNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
                spellLevelLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
                spellNameLabel.setPreferredSize(labelDim);
                spellLevelLabel.setPreferredSize(labelDim);
                spellNameLabel.setFont(FontUtils.getJomhuriaFont(24));
                spellLevelLabel.setFont(FontUtils.getJomhuriaFont(24));
                spellNameLabel.setForeground(ColorUtils.CREAM);
                spellLevelLabel.setForeground(ColorUtils.CREAM);

                roundedPanel.add(spellNameLabel, BorderLayout.WEST);
                roundedPanel.add(spellLevelLabel, BorderLayout.EAST);

                missionRequiredSpellPanel.add(roundedPanel, spellPanelGbc);
                requiredSpells[spellPanelGbc.gridy] = roundedPanel;
                spellPanelGbc.gridy++;
            });

            clippedImagePanel.setClip(x1, y1, x2, y2);
        }
    }

    private void initPlaceHolders() {
        for (int i = 0; i < 4; i++) {
            PlaceHolderPanel panel = new PlaceHolderPanel(new Dimension(((int) (WIDTH * 0.125)) - 10, ((int) (HEIGHT * 0.35)) - 10), i);
            selectedPanel.add(panel, i);
            selectedMemberPanels[i] = panel;
        }
    }

    public void cleanPanels() {
        resetPanels();
        int accessibleChildrenCount = memberListPanel.getAccessibleContext().getAccessibleChildrenCount();
        Component[] components = memberListPanel.getComponents();

        for (int i = 0; i < accessibleChildrenCount; i++) {
            Component component = components[i];
            if (component instanceof GuildMemberSelectionTile) {
                memberListPanel.remove(component);
            }
        }
        guildMemberToPanelMapping.clear();

    }

    public void resetPanels() {
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
        selectedMemberPanels = new App.Util.Iterable[4];
        initPlaceHolders();
        selectedMembers = new GuildMember[4];

        for (RoundedPanel requiredSpell : requiredSpells) {
            missionRequiredSpellPanel.remove(requiredSpell);
        }
        spellPanelGbc.gridy = 0;
    }

    public void setOnMissionCompletionCallback(Runnable onMissionCompletionCallback) {
        this.onMissionCompletionCallback = onMissionCompletionCallback;
    }
}

