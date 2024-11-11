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
    private ImageIcon level1fd;
    private Stack<Card> level1Cards;
    private JLabel selectedCardLabel;
    private JPanel gridPanel;

    private static final int GRID_WIDTH = 600;
    private static final int GRID_HEIGHT = 200;
    private static final int CARD_WIDTH = 120;
    private static final int CARD_HEIGHT = 180;
    private static final float HOVER_SCALE = 1.1f;

    public SplendorGameScreen(CardLoader cardLoader) {
        bImageIcon = new ImageIcon("src/Images/GameMenu/GameBackground.png");
        level1fd = new ImageIcon("src/Images/Level1/L1BC.png");

        // Load Level 1 cards from the CardLoader
        level1Cards = cardLoader.getLevel1Cards();
        Collections.shuffle(level1Cards);

        setLayout(null);

        // Create a JPanel to hold the grid and set GridLayout
        gridPanel = new JPanel(new GridLayout(1, 5, 5, 5)); // 5-pixel gap between columns
        gridPanel.setOpaque(false);

        // Add level1fd image in the first column
        ImageIcon scaledFdIcon = new ImageIcon(
                level1fd.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH));
        JLabel level1fdLabel = new JLabel(scaledFdIcon);
        gridPanel.add(level1fdLabel);

        for (int i = 0; i < 4; i++) {
            addCardToGrid();
        }

        add(gridPanel);

        // Handle panel resizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int gridX = (getWidth() - GRID_WIDTH) / 2;
                int gridY = (getHeight() - GRID_HEIGHT) / 2 - 250;
                gridPanel.setBounds(gridX, gridY, GRID_WIDTH, GRID_HEIGHT);

                if (selectedCardLabel != null) {
                    updateSelectedCardPosition();
                }
            }
        });
    }

    private void addCardToGrid() {
        if (!level1Cards.isEmpty()) {
            Card card = level1Cards.pop();
            ImageIcon cardImage = new ImageIcon(new ImageIcon(card.getIllustration())
                    .getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH));
            JLabel cardLabel = createClickableLabel(cardImage, card);
            gridPanel.add(cardLabel);
        } else {
            JLabel emptyLabel = new JLabel("Empty Slot", SwingConstants.CENTER);
            gridPanel.add(emptyLabel);
        }
    }

    private JLabel createClickableLabel(ImageIcon icon, Card card) {
        JLabel label = new JLabel(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();

                // Check if the mouse is hovering
                if (getClientProperty("hovered") != null && (Boolean) getClientProperty("hovered")) {
                    // Apply scaling transformation
                    double scale = HOVER_SCALE;
                    int w = getWidth();
                    int h = getHeight();
                    int x = (int) (w - (w * scale)) / 2;
                    int y = (int) (h - (h * scale)) / 2;

                    g2d.translate(x, y);
                    g2d.scale(scale, scale);
                }

                // Draw the image
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };

        label.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleCardClick(label, card);
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

    private void handleCardClick(JLabel clickedLabel, Card card) {
        // Store the clicked card's image
        ImageIcon clickedCardIcon = (ImageIcon) clickedLabel.getIcon();

        // Remove previous selected card if it exists
        if (selectedCardLabel != null) {
            remove(selectedCardLabel);
        }

        selectedCardLabel = new JLabel(clickedCardIcon);
        updateSelectedCardPosition();
        add(selectedCardLabel);

        // Replace the clicked card with a new one from the deck
        if (!level1Cards.isEmpty()) {
            Card newCard = level1Cards.pop();
            ImageIcon newCardImage = new ImageIcon(new ImageIcon(newCard.getIllustration())
                    .getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH));

            // Create new clickable label for the new card
            JLabel newCardLabel = createClickableLabel(newCardImage, newCard);

            // Replace the old card with the new one
            int index = gridPanel.getComponentZOrder(clickedLabel);
            gridPanel.remove(index);
            gridPanel.add(newCardLabel, index);
            gridPanel.revalidate();
        } else {
            // If no more cards, show empty slot
            int index = gridPanel.getComponentZOrder(clickedLabel);
            gridPanel.remove(index);
            JLabel emptyLabel = new JLabel("Empty Slot", SwingConstants.CENTER);
            gridPanel.add(emptyLabel, index);
            gridPanel.revalidate();
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
