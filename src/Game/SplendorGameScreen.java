package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Stack;
import Cards.Card;
import Cards.CardLoader;

public class SplendorGameScreen extends JPanel {
    private ImageIcon bImageIcon;
    private ImageIcon level1fd, level2fd, level3fd;
    private Stack<Card> level1Cards, level2Cards, level3Cards;
    private JLabel selectedCardLabel;
    private JPanel gridPanel1, gridPanel2, gridPanel3;

    private static final int GRID_WIDTH = 600;
    private static final int GRID_HEIGHT = 200;
    private static final int CARD_WIDTH = 120;
    private static final int CARD_HEIGHT = 180;
    private static final float HOVER_SCALE = 1.08f;
    private static final int VERTICAL_GAP = 10;

    public SplendorGameScreen(CardLoader cardLoader) {
        bImageIcon = new ImageIcon("src/Images/GameMenu/GameBackground.png");
        level1fd = new ImageIcon("src/Images/Level1/L1BC.png");
        level2fd = new ImageIcon("src/Images/Level2/L2BC.png");
        level3fd = new ImageIcon("src/Images/Level3/L3BC.png");

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
        gridPanel1 = createLevelGrid(level1Cards, level1fd);
        gridPanel2 = createLevelGrid(level2Cards, level2fd);
        gridPanel3 = createLevelGrid(level3Cards, level3fd);

        add(gridPanel1);
        add(gridPanel2);
        add(gridPanel3);

        // Handle panel resizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                updateGridPositions();
                if (selectedCardLabel != null) {
                    updateSelectedCardPosition();
                }
            }
        });
    }

    private JPanel createLevelGrid(Stack<Card> cardStack, ImageIcon faceDown) {
        JPanel grid = new JPanel(new GridLayout(1, 5, 5, 5));
        grid.setOpaque(false);

        // Add face-down deck image
        ImageIcon scaledFdIcon = new ImageIcon(
                faceDown.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH));
        JLabel faceDownLabel = new JLabel(scaledFdIcon);
        grid.add(faceDownLabel);

        // Add four cards from the deck
        for (int i = 0; i < 4; i++) {
            addCardToGrid(grid, cardStack);
        }

        return grid;
    }

    private void updateGridPositions() {
        int gridX = (getWidth() - GRID_WIDTH) / 2;
        
        // Position grids vertically with spacing
        gridPanel3.setBounds(gridX, 170, GRID_WIDTH, GRID_HEIGHT);
        gridPanel2.setBounds(gridX, 160 + GRID_HEIGHT + VERTICAL_GAP, GRID_WIDTH, GRID_HEIGHT);
        gridPanel1.setBounds(gridX, 140 + (GRID_HEIGHT + VERTICAL_GAP) * 2, GRID_WIDTH, GRID_HEIGHT);
    }

    private void addCardToGrid(JPanel grid, Stack<Card> cardStack) {
        if (!cardStack.isEmpty()) {
            Card card = cardStack.pop();
            ImageIcon cardImage = new ImageIcon(new ImageIcon(card.getIllustration())
                    .getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH));
            JLabel cardLabel = createClickableLabel(cardImage, card, cardStack, grid);
            grid.add(cardLabel);
        } else {
            JLabel emptyLabel = new JLabel("Empty Slot", SwingConstants.CENTER);
            grid.add(emptyLabel);
        }
    }

    private JLabel createClickableLabel(ImageIcon icon, Card card, Stack<Card> cardStack, JPanel grid) {
        JLabel label = new JLabel(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();

                if (getClientProperty("hovered") != null && (Boolean) getClientProperty("hovered")) {
                    double scale = HOVER_SCALE;
                    int w = getWidth();
                    int h = getHeight();
                    int x = (int) (w - (w * scale)) / 2;
                    int y = (int) (h - (h * scale)) / 2;

                    g2d.translate(x, y);
                    g2d.scale(scale, scale);
                }

                super.paintComponent(g2d);
                g2d.dispose();
            }
        };

        label.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleCardClick(label, card, cardStack, grid);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label.putClientProperty("hovered", true);
                label.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.putClientProperty("hovered", false);
                label.repaint();
            }
        });

        return label;
    }

    private void handleCardClick(JLabel clickedLabel, Card card, Stack<Card> cardStack, JPanel grid) {
        ImageIcon clickedCardIcon = (ImageIcon) clickedLabel.getIcon();

        if (selectedCardLabel != null) {
            remove(selectedCardLabel);
        }

        selectedCardLabel = new JLabel(clickedCardIcon);
        updateSelectedCardPosition();
        add(selectedCardLabel);

        if (!cardStack.isEmpty()) {
            Card newCard = cardStack.pop();
            ImageIcon newCardImage = new ImageIcon(new ImageIcon(newCard.getIllustration())
                    .getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH));

            JLabel newCardLabel = createClickableLabel(newCardImage, newCard, cardStack, grid);

            int index = grid.getComponentZOrder(clickedLabel);
            grid.remove(index);
            grid.add(newCardLabel, index);
            grid.revalidate();
        } else {
            int index = grid.getComponentZOrder(clickedLabel);
            grid.remove(index);
            JLabel emptyLabel = new JLabel("Empty Slot", SwingConstants.CENTER);
            grid.add(emptyLabel, index);
            grid.revalidate();
        }

        repaint();
    }

    private void updateSelectedCardPosition() {
        if (selectedCardLabel != null) {
            int x = getWidth() - CARD_WIDTH - 20;
            int y = getHeight() - CARD_HEIGHT - 20;
            selectedCardLabel.setBounds(x, y, CARD_WIDTH, CARD_HEIGHT);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}