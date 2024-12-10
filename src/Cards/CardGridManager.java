package Cards;

import javax.swing.*;

import Game.Inventory.ReserveInventory;
import Game.Inventory.TokenInventory;
import Game.Main.SplendorGameScreen;
import Game.Token.TokenManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Objects;
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
            ImageIcon cardImage = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(card.getIllustration())))
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
                SplendorGameScreen gameScreen;
                Container parent = grid;
                while (!(parent instanceof SplendorGameScreen) && parent != null) {
                    parent = parent.getParent();
                }

                if (parent instanceof SplendorGameScreen) {
                    gameScreen = (SplendorGameScreen) parent;
                    if (gameScreen.getCycleInventory().getCurrentPlayerIndex() != gameScreen.getPlayerTurn())
                        return;
                    handleCardClick(label, card, cardStack, grid);
                }

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
        Container parent = grid;
        while (!(parent instanceof SplendorGameScreen) && parent != null) {
            parent = parent.getParent();
        }
        if (parent instanceof SplendorGameScreen) {
            SplendorGameScreen gameScreen = (SplendorGameScreen) parent;
            if (gameScreen.getTokenManager().getTokensTakenInTurn().size() > 0) {
                JOptionPane.showMessageDialog(
                        grid,
                        "You cannot collect tokens and cards in the same turn. Reset your tokens or confirm them.",
                        "Cannot Perform Multiple Actions",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        String[] options = { "Buy", "Reserve", "Cancel" };
        int choice = JOptionPane.showOptionDialog(grid,
                "What would you like to do with this card?",
                "Card Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) { // Buy
            handleBuyCard(clickedLabel, card, cardStack, grid);
        } else if (choice == 1) { // Reserve
            handleReserveCard(clickedLabel, card, cardStack, grid);
        }
    }

    private void handleBuyCard(JLabel clickedLabel, Card card, Stack<Card> cardStack, JPanel grid) {
        // Get reference to SplendorGameScreen
        Container parent = grid;
        while (!(parent instanceof SplendorGameScreen) && parent != null) {
            parent = parent.getParent();
        }

        if (parent instanceof SplendorGameScreen) {
            SplendorGameScreen gameScreen = (SplendorGameScreen) parent;
            TokenInventory tokenInventory = gameScreen.getPlayerInventory();
            TokenManager tokenManager = gameScreen.getTokenManager();

            int[] tempTokens = new int[] {
                    tokenInventory.getTokenCount("white"),
                    tokenInventory.getTokenCount("blue"),
                    tokenInventory.getTokenCount("green"),
                    tokenInventory.getTokenCount("red"),
                    tokenInventory.getTokenCount("black"),
            };

            // Create a copy of original costs to track remaining costs
            int[] remainingCosts = Arrays.copyOf(card.getCosts(), card.getCosts().length);

            // Track which colors have been fully covered by bonuses
            boolean[] fullyPaidByBonus = new boolean[remainingCosts.length];

            // Subtract bonuses from costs
            int i = 0;
            for (String color : new String[] { "white", "blue", "green", "red", "black" }) {
                int bonusCount = tokenInventory.getBonusCount(color);

                // If bonuses fully cover the cost for this color
                if (bonusCount >= card.getCosts()[i]) {
                    remainingCosts[i] = 0;
                    fullyPaidByBonus[i] = true;
                } else {
                    // Reduce remaining cost by bonus count
                    remainingCosts[i] = Math.max(0, remainingCosts[i] - bonusCount);
                }

                i++;
            }

            // Calculate total remaining cost after bonuses
            int totalRemainingCost = Arrays.stream(remainingCosts).sum();

            int totalAvailableTokens = 0;
            for (int j = 0; j < remainingCosts.length; j++) {
                if (!fullyPaidByBonus[j]) {
                    totalAvailableTokens += Math.min(tempTokens[j], remainingCosts[j]);
                }
            }
            // Include gold tokens in the total available count
            totalAvailableTokens += tokenInventory.getTokenCount("gold");
                       

            int goldTokensNeeded = Math.max(0, totalRemainingCost - totalAvailableTokens);
            if (goldTokensNeeded > tokenInventory.getTokenCount("gold")) {
                JOptionPane.showMessageDialog(
                        grid,
                        "You do not have enough tokens or bonuses to purchase this card.",
                        "Not Enough Tokens",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Deduct regular tokens used
            i = 0;
            for (String color : new String[] { "white", "blue", "green", "red", "black" }) {
                if (!fullyPaidByBonus[i]) {
                    int tokensToUse = Math.min(tempTokens[i], remainingCosts[i]);

                    // Remove tokens from player inventory
                    tokenInventory.removeToken(color, tokensToUse);

                    // Update the token manager's total token count
                    tokenManager.setPlayerTokenCount(-tokensToUse);

                    // Add tokens back to the token pool
                    tokenManager.addToken(color, tokensToUse);
                }
                i++;
            }

            goldTokensNeeded = Math.max(0, totalRemainingCost - (totalAvailableTokens - tokenInventory.getTokenCount("gold")));
            if (goldTokensNeeded > 0) {
                tokenInventory.removeToken("gold", goldTokensNeeded);
                tokenManager.setPlayerTokenCount(-goldTokensNeeded);
                tokenManager.addToken("gold", goldTokensNeeded);
            }
            

            // Map card gem type to token color
            String tokenColor = mapGemToTokenColor(card.getGem());

            // Add bonus to player's inventory
            if (tokenColor != null) {
                tokenInventory.addBonus(tokenColor);
            }

            // Update player's score with the card's prestige points
            int currentPlayerIndex = gameScreen.getCycleInventory().getCurrentPlayerIndex();
            gameScreen.updatePlayerScore(currentPlayerIndex, card.getPrestige());

            // Replace the card in the grid with a new one
            replaceCardInGrid(clickedLabel, cardStack, grid);

            // Move to the next player's turn
            gameScreen.nextPlayerTurn();
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

    private void handleReserveCard(JLabel clickedLabel, Card card, Stack<Card> cardStack, JPanel grid) {
        // Get reference to SplendorGameScreen
        Container parent = grid;
        while (!(parent instanceof SplendorGameScreen) && parent != null) {
            parent = parent.getParent();
        }

        if (parent instanceof SplendorGameScreen) {
            SplendorGameScreen gameScreen = (SplendorGameScreen) parent;
            ReserveInventory reserveInventory = gameScreen.getReserveInventory();

            if (reserveInventory.canReserveCard()) {
                // Get TokenManager and TokenInventory from gameScreen
                TokenManager tokenManager = gameScreen.getTokenManager();
                TokenInventory tokenInventory = gameScreen.getPlayerInventory(); // Use getPlayerInventory instead

                // Check if gold tokens are available
                if (tokenManager.getTokenCount("gold") >= 0) {
                    // Add gold token to player's inventory
                    if(tokenManager.getTokenCount("gold") != 0){
                        tokenInventory.addToken("gold");
                        gameScreen.getTokenManager().setPlayerTokenCount(1);
                    }
                    // Decrease gold token count in token manager
                    tokenManager.decrementToken("gold");
                    // Add card to reserve
                    reserveInventory.addReservedCard(card);
                    // Replace the card in grid
                    replaceCardInGrid(clickedLabel, cardStack, grid);
                    // increase token counter

                } else {
                    JOptionPane.showMessageDialog(grid,
                            "No gold tokens available for reserving a card!",
                            "Cannot Reserve",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(grid,
                        "Maximum reserve limit reached!",
                        "Cannot Reserve",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            gameScreen.nextPlayerTurn();
        }
    }

    private void replaceCardInGrid(JLabel clickedLabel, Stack<Card> cardStack, JPanel grid) {
        if (!cardStack.isEmpty()) {
            Card newCard = cardStack.pop();
            ImageIcon newCardImage = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(newCard.getIllustration())))
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