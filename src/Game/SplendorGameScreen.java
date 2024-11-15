package Game;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Stack;
import Cards.Card;
import Cards.CardLoader;

public class SplendorGameScreen extends JPanel {
    private ImageIcon bImageIcon;
    private ImageIcon level1fd, level2fd, level3fd;
    private Stack<Card> level1Cards, level2Cards, level3Cards;
    private JPanel gridPanel1, gridPanel2, gridPanel3;
    private CardGridManager gridManager;
    private ImageIcon rightArrow, leftArrow;
    private JLabel rightArrowLabel, leftArrowLabel;
    private static final int arrowWidth = 75;

    private static final int GRID_WIDTH = 700;
    private static final int GRID_HEIGHT = 200;
    private static final int VERTICAL_GAP = 10;

    public SplendorGameScreen(CardLoader cardLoader) {
        gridManager = new CardGridManager();
        
        bImageIcon = new ImageIcon("src/Images/GameMenu/GameBackground.png");
        level1fd = new ImageIcon("src/Images/Level1/L1BC.png");
        level2fd = new ImageIcon("src/Images/Level2/L2BC.png");
        level3fd = new ImageIcon("src/Images/Level3/L3BC.png");
        rightArrow = new ImageIcon("src/Images/MiscellaneousImages/RightArrow.png");
        leftArrow = new ImageIcon("src/Images/MiscellaneousImages/LeftArrow.png");

        rightArrowLabel = new JLabel(
                new ImageIcon(
                    rightArrow.getImage().getScaledInstance(arrowWidth, arrowWidth, Image.SCALE_SMOOTH)));
        leftArrowLabel = new JLabel(
                new ImageIcon(
                        leftArrow.getImage().getScaledInstance(arrowWidth, arrowWidth, Image.SCALE_SMOOTH)));

        // Load cards from the CardLoader
        level1Cards = cardLoader.getLevel1Cards();
        level2Cards = cardLoader.getLevel2Cards();
        level3Cards = cardLoader.getLevel3Cards();

        // Shuffle all card decks
        Collections.shuffle(level1Cards);
        Collections.shuffle(level2Cards);
        Collections.shuffle(level3Cards);

        setLayout(null);

        // Create grid panels for each level
        gridPanel1 = gridManager.createLevelGrid(level1Cards, level1fd);
        gridPanel2 = gridManager.createLevelGrid(level2Cards, level2fd);
        gridPanel3 = gridManager.createLevelGrid(level3Cards, level3fd);

        add(gridPanel1);
        add(gridPanel2);
        add(gridPanel3);
        add(rightArrowLabel);
        add(leftArrowLabel);

        rightArrowLabel.setBounds(1750, 400, arrowWidth, arrowWidth);
        leftArrowLabel.setBounds(50, 400, arrowWidth, arrowWidth);

        // Handle panel resizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                updateGridPositions();
                if (gridManager.getSelectedCardLabel() != null) {
                    gridManager.updateSelectedCardPosition(gridManager.getSelectedCardLabel(), SplendorGameScreen.this);
                }
            }
        });
    }

    private void updateGridPositions() {
        int gridX = (getWidth() - GRID_WIDTH) / 2;
        
        // Position grids vertically with spacing
        gridPanel3.setBounds(gridX, 135, GRID_WIDTH, GRID_HEIGHT);
        gridPanel2.setBounds(gridX, 130 + GRID_HEIGHT + VERTICAL_GAP - 5, GRID_WIDTH, GRID_HEIGHT);
        gridPanel1.setBounds(gridX, 115 + (GRID_HEIGHT + VERTICAL_GAP) * 2, GRID_WIDTH, GRID_HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}