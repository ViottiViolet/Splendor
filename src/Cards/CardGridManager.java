package Cards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

public class CardGridManager {
    private static final int CARD_WIDTH = 120;
    private static final int CARD_HEIGHT = 180;
    private static final float HOVER_SCALE = 1.08f;
    private JLabel selectedCardLabel;

    public JPanel createLevelGrid(Stack<Card> cardStack, ImageIcon faceDown) {
        JPanel grid = new JPanel(new GridLayout(1, 5, 10, 10));
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
            // Remove from parent container
            Container parent = selectedCardLabel.getParent();
            if (parent != null) {
                parent.remove(selectedCardLabel);
                parent.repaint();
            }
        }

        selectedCardLabel = new JLabel(clickedCardIcon);
        updateSelectedCardPosition(selectedCardLabel, grid.getParent());
        grid.getParent().add(selectedCardLabel);

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

        grid.getParent().repaint();
    }

    public void updateSelectedCardPosition(JLabel selectedCardLabel, Container parent) {
        if (selectedCardLabel != null && parent != null) {
            int x = parent.getWidth() - CARD_WIDTH - 20;
            int y = parent.getHeight() - CARD_HEIGHT - 20;
            selectedCardLabel.setBounds(x, y, CARD_WIDTH, CARD_HEIGHT);
        }
    }

    public JLabel getSelectedCardLabel() {
        return selectedCardLabel;
    }

    public void setSelectedCardLabel(JLabel label) {
        this.selectedCardLabel = label;
    }
}
