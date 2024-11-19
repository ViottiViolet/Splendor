package Game.Inventory;

import Cards.Card;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;

public class ReserveInventory extends JPanel {
    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 120;
    private static final int MAX_RESERVED_CARDS = 3;
    private static final int CORNER_RADIUS = 15;
    private static final float BORDER_THICKNESS = 2.0f;
    
    // Store reserved cards for each player
    private final Map<Integer, ArrayList<Card>> playerReservedCards;
    private final Map<Integer, ArrayList<JLabel>> playerCardLabels;
    private int currentPlayerIndex;
    
    public ReserveInventory() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // Initialize player-specific storage
        playerReservedCards = new HashMap<>();
        playerCardLabels = new HashMap<>();
        currentPlayerIndex = 0;

        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(),
            "Reserved Cards",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Gothic", Font.BOLD, 16),
            new Color(220, 220, 220)
        );
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            border
        ));

        // Panel for cards with padding
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        cardsPanel.setOpaque(false);
        
        add(Box.createVerticalGlue());
        add(cardsPanel);
        add(Box.createVerticalGlue());

        setPreferredSize(new Dimension(300, 200));
    }

    private void ensurePlayerStorageExists(int playerIndex) {
        playerReservedCards.computeIfAbsent(playerIndex, k -> new ArrayList<>());
        playerCardLabels.computeIfAbsent(playerIndex, k -> new ArrayList<>());
    }

    public boolean canReserveCard() {
        ensurePlayerStorageExists(currentPlayerIndex);
        return playerReservedCards.get(currentPlayerIndex).size() < MAX_RESERVED_CARDS;
    }

    public void addReservedCard(Card card) {
        ensurePlayerStorageExists(currentPlayerIndex);
        ArrayList<Card> currentPlayerCards = playerReservedCards.get(currentPlayerIndex);
        ArrayList<JLabel> currentPlayerLabels = playerCardLabels.get(currentPlayerIndex);

        if (!canReserveCard()) {
            JOptionPane.showMessageDialog(this,
                "Maximum reserved cards limit reached (3 cards)",
                "Cannot Reserve Card",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentPlayerCards.add(card);
        ImageIcon cardImage = new ImageIcon(new ImageIcon(card.getIllustration())
                .getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH));
        
        JLabel cardLabel = new JLabel(cardImage) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
                super.paintComponent(g);
            }
        };
        
        cardLabel.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        cardLabel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180, 100), 1));
        currentPlayerLabels.add(cardLabel);
        
        ((JPanel)getComponent(1)).add(cardLabel);
        revalidate();
        repaint();
    }

    public ArrayList<Card> getReservedCards() {
        ensurePlayerStorageExists(currentPlayerIndex);
        return playerReservedCards.get(currentPlayerIndex);
    }

    public void removeReservedCard(int index) {
        ensurePlayerStorageExists(currentPlayerIndex);
        ArrayList<Card> currentPlayerCards = playerReservedCards.get(currentPlayerIndex);
        ArrayList<JLabel> currentPlayerLabels = playerCardLabels.get(currentPlayerIndex);

        if (index >= 0 && index < currentPlayerCards.size()) {
            currentPlayerCards.remove(index);
            JLabel label = currentPlayerLabels.get(index);
            ((JPanel)getComponent(1)).remove(label);
            currentPlayerLabels.remove(index);
            revalidate();
            repaint();
        }
    }

    public void switchToPlayer(int playerIndex) {
        currentPlayerIndex = playerIndex;
        refreshDisplay();
    }

    private void refreshDisplay() {
        JPanel cardsPanel = (JPanel)getComponent(1);
        cardsPanel.removeAll();
        
        ensurePlayerStorageExists(currentPlayerIndex);
        ArrayList<JLabel> currentPlayerLabels = playerCardLabels.get(currentPlayerIndex);
        
        for (JLabel label : currentPlayerLabels) {
            cardsPanel.add(label);
        }
        
        revalidate();
        repaint();
    }

    // This method now only clears the current player's cards
    public void clearReservedCards() {
        ensurePlayerStorageExists(currentPlayerIndex);
        playerReservedCards.get(currentPlayerIndex).clear();
        playerCardLabels.get(currentPlayerIndex).clear();
        ((JPanel)getComponent(1)).removeAll();
        revalidate();
        repaint();
    }

    // Modified to switch to the new player instead of clearing
    public void resetForNewPlayer() {
        switchToPlayer(currentPlayerIndex);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = getWidth();
        int height = getHeight();

        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(0, 0, 0, 160),
            0, height, new Color(0, 0, 0, 140)
        );
        g2d.setPaint(gradient);

        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
            BORDER_THICKNESS, BORDER_THICKNESS,
            width - 2 * BORDER_THICKNESS,
            height - 2 * BORDER_THICKNESS,
            CORNER_RADIUS, CORNER_RADIUS
        );
        g2d.fill(roundedRect);

        g2d.setStroke(new BasicStroke(BORDER_THICKNESS));
        g2d.setColor(new Color(140, 140, 140, 100));
        g2d.draw(roundedRect);

        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawRoundRect(3, 3, width - 6, height - 6, CORNER_RADIUS - 2, CORNER_RADIUS - 2);
    }
}