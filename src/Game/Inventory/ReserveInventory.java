package Game.Inventory;

import Cards.Card;
import Game.Main.SplendorGameScreen;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        ImageIcon cardImage = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(card.getIllustration())))
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

        cardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleCardClick(cardLabel, card);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cardLabel.putClientProperty("hovered", true);
                cardLabel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cardLabel.putClientProperty("hovered", false);
                cardLabel.repaint();
            }
        });

        ((JPanel)getComponent(1)).add(cardLabel);
        revalidate();
        repaint();
    }

    private void handleCardClick(JLabel clickedLabel, Card card) {
        Container parent = this;
        while (!(parent instanceof SplendorGameScreen) && parent != null) {
            parent = parent.getParent();
        }
        if (parent instanceof SplendorGameScreen) {
            SplendorGameScreen gameScreen = (SplendorGameScreen) parent;
            if (gameScreen.getTokenManager().getTokensTakenInTurn().size() > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "You cannot collect tokens and cards in the same turn. Reset your tokens or confirm them.",
                        "Cannot Perform Multiple Actions",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

                // Check if this is the current player's turn
                int currentPlayerIndex = gameScreen.getCycleInventory().getCurrentPlayerIndex();
                if (currentPlayerIndex != gameScreen.getPlayerTurn()) {
                    JOptionPane.showMessageDialog(this,
                            "These are not your reserved cards.",
                            "Not Your Turn",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

        }
        String[] options = { "Buy", "Cancel" };
        int choice = JOptionPane.showOptionDialog(this,
                "Would you like to purchase this card?",
                "Card Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) { // Buy
            SplendorGameScreen gameScreen;
            while (!(parent instanceof SplendorGameScreen) && parent != null) {
                parent = parent.getParent();
            }

            if (parent instanceof SplendorGameScreen) {
                gameScreen = (SplendorGameScreen) parent;
                if (gameScreen.getCycleInventory().getCurrentPlayerIndex() != gameScreen.getPlayerTurn()) return;
            }
            handleBuyCard(clickedLabel, card);
        }
    }

    private void handleBuyCard(JLabel clickedLabel, Card card) {
        // Get reference to SplendorGameScreen
        Container parent = this;
        while (!(parent instanceof SplendorGameScreen) && parent != null) {
            parent = parent.getParent();
        }

        if (parent instanceof SplendorGameScreen) {
            SplendorGameScreen gameScreen = (SplendorGameScreen) parent;
            TokenInventory tokenInventory = gameScreen.getPlayerInventory();


            int[] tempTokens = new int[]{
                    tokenInventory.getTokenCount("white"),
                    tokenInventory.getTokenCount("blue"),
                    tokenInventory.getTokenCount("green"),
                    tokenInventory.getTokenCount("red"),
                    tokenInventory.getTokenCount("black"),
            };
            int[] temp = card.getCosts();
            int i = 0;
            // goes through costs to see if player can afford with bonuses + tokens
            for (String color : new String[]{"white", "blue", "green", "red", "black"}) {
                temp[i] -= tokenInventory.getBonusCount(color);
                while (temp[i] > 0 && tempTokens[i] > 0) {
                    tempTokens[i]--;
                    temp[i]--;
                }
                if (tempTokens[i] < 0) tempTokens[i] = 0;
                if (temp[i] < 0) temp[i] = 0;
                i++;
            }
            int remainingCost = temp[0]+temp[1]+temp[2]+temp[3]+temp[4];

            // if player cannot afford, show warning. otherwise, continue with purchase
            // if player can spend gold tokens on remaining costs, continue
            if (remainingCost != 0 && remainingCost > tokenInventory.getTokenCount("gold"))
            {
                JOptionPane.showMessageDialog(
                        this,
                        "You do not have enough tokens or bonuses to purchase this card.",
                        "Not Enough Tokens",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Map card gem type to token color
            String tokenColor = mapGemToTokenColor(card.getGem());

            // removes tokens from player inventory to buy card and adds bonuses back
            i = 0;
            for (String color : new String[]{"white", "blue", "green", "red", "black"}) {
                if (tokenInventory.getTokenCount(color) != 0) {
                    gameScreen.getTokenManager().addToken(color, card.getCosts()[i]);
                    gameScreen.getTokenManager().removeToken(color, tokenInventory.getBonusCount(color));
                    gameScreen.getTokenManager().setPlayerTokenCountNumber(tempTokens[i]);
                    //gameScreen.getTokenManager().setPlayerTokenCount(tokenInventory.getBonusCount(color));
                    tokenInventory.removeToken(color, card.getCosts()[i]);
                    tokenInventory.addToken(color, tokenInventory.getBonusCount(color));
                }
                i++;
            }

            // Add bonus to player's inventory
            if (tokenColor != null) {
                tokenInventory.addBonus(tokenColor);
            }

            tokenInventory.removeToken("gold", remainingCost);
            gameScreen.getTokenManager().addToken("gold", remainingCost);

            playerReservedCards.get(currentPlayerIndex).remove(card);
            playerCardLabels.get(currentPlayerIndex).remove(clickedLabel);

            gameScreen.nextPlayerTurn();

            ((JPanel)getComponent(1)).remove(clickedLabel);
            revalidate();
            repaint();


        }
    }

    private String mapGemToTokenColor(String gemType) {
        return switch (gemType.toLowerCase()) {
            case "diamond" -> "white";
            case "sapphire" -> "blue";
            case "onyx" -> "black";
            case "ruby" -> "red";
            case "emerald" -> "green";
            default -> null;
        };
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