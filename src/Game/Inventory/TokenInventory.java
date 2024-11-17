package Game.Inventory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import Cards.Card;

public class TokenInventory extends JPanel {
    private final Map<Integer, Map<String, Integer>> playerTokens;
    private final Map<Integer, Map<String, Integer>> playerBonuses;
    private final java.util.List<Card> cards;
    private final Map<Integer, Integer> playerPoints;
    private int currentPlayerIndex;
    @SuppressWarnings("unused")
    private final int totalPlayers;

    public TokenInventory(int playerNumber, int totalPlayers) {
        this.currentPlayerIndex = 0;
        this.totalPlayers = totalPlayers;
        this.playerTokens = new HashMap<>();
        this.playerBonuses = new HashMap<>();
        this.cards = new java.util.ArrayList<>();
        this.playerPoints = new HashMap<>();

        setLayout(null);
        setOpaque(false);

        // Initialize token counts and bonuses for each player
        for (int i = 0; i < totalPlayers; i++) {
            Map<String, Integer> tokens = new HashMap<>();
            Map<String, Integer> bonuses = new HashMap<>();
            tokens.put("white", 0);
            tokens.put("blue", 0);
            tokens.put("green", 0);
            tokens.put("red", 0);
            tokens.put("black", 0);
            tokens.put("gold", 0);

            bonuses.put("white", 0);
            bonuses.put("blue", 0);
            bonuses.put("green", 0);
            bonuses.put("red", 0);
            bonuses.put("black", 0);

            playerTokens.put(i, tokens);
            playerBonuses.put(i, bonuses);
            playerPoints.put(i, 0);
        }
    }

    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
        repaint();
    }

    public void addToken(String color) {
        Map<String, Integer> currentPlayerTokens = playerTokens.get(currentPlayerIndex);
        currentPlayerTokens.put(color, currentPlayerTokens.get(color) + 1);
        repaint();
    }

    public void removeToken(String color) {
        Map<String, Integer> currentPlayerTokens = playerTokens.get(currentPlayerIndex);
        if (currentPlayerTokens.get(color) > 0) {
            currentPlayerTokens.put(color, currentPlayerTokens.get(color) - 1);
            repaint();
        }
    }

    public void addBonus(String color) {
        Map<String, Integer> currentPlayerBonuses = playerBonuses.get(currentPlayerIndex);
        currentPlayerBonuses.put(color, currentPlayerBonuses.get(color) + 1);
        repaint();
    }

    public void addCard(Card card) {
        cards.add(card);
        playerPoints.put(currentPlayerIndex, playerPoints.get(currentPlayerIndex) + card.getPrestige());
        repaint();
    }

    public int getTokenCount(String color) {
        return playerTokens.get(currentPlayerIndex).get(color);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    // Get total tokens excluding gold for current player
    public int getCurrentPlayerRegularTokens() {
        Map<String, Integer> currentPlayerTokens = playerTokens.get(currentPlayerIndex);
        return currentPlayerTokens.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("gold"))
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRoundRect(0, 0, getWidth(), getHeight() - 1, 20, 20);

        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        Map<String, Color> tokenColors = Map.of(
                "white", Color.WHITE,
                "blue", new Color(25, 189, 255),
                "green", new Color(62, 160, 85),
                "red", Color.RED,
                "black", Color.BLACK);

        Color goldColor = new Color(255, 215, 0);

        Map<String, Integer> currentTokens = playerTokens.get(currentPlayerIndex);
        Map<String, Integer> currentBonuses = playerBonuses.get(currentPlayerIndex);

        int x = 10;
        int y = 12;
        int circleDiameter = 50;
        int rectangleWidth = 40;
        int rectangleHeight = circleDiameter;
        int circleToRectangleSpacing = 10;
        int colorGroupSpacing = 50 + circleDiameter + rectangleWidth;

        for (Map.Entry<String, Color> entry : tokenColors.entrySet()) {
            String tokenColor = entry.getKey();
            Color color = entry.getValue();
            Integer tokenCount = currentTokens.getOrDefault(tokenColor, 0);
            Integer bonusCount = currentBonuses.getOrDefault(tokenColor, 0);

            g2d.setColor(color);
            g2d.fillOval(x, y, circleDiameter, circleDiameter);

            g2d.setColor(Color.GRAY);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(x, y, circleDiameter, circleDiameter);

            g2d.setColor(color.equals(Color.BLACK) ? Color.WHITE : Color.BLACK);
            g2d.setFont(new Font("Gothic", Font.BOLD, 25));
            String tokenText = String.valueOf(tokenCount);

            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(tokenText);
            int textHeight = metrics.getAscent();
            int textX = x + (circleDiameter - textWidth) / 2 + 1;
            int textY = y + (circleDiameter - textHeight) / 2 + textHeight - 1;
            g2d.drawString(tokenText, textX, textY);

            int rectX = x + circleDiameter + circleToRectangleSpacing;
            g2d.setColor(color);
            g2d.fillRoundRect(rectX, y, rectangleWidth, rectangleHeight, 15, 15);

            g2d.setColor(Color.GRAY);
            g2d.drawRoundRect(rectX, y, rectangleWidth, rectangleHeight, 15, 15);

            g2d.setColor(color.equals(Color.BLACK) ? Color.WHITE : Color.BLACK);
            String bonusText = String.valueOf(bonusCount);
            int bonusTextWidth = metrics.stringWidth(bonusText);
            int rectTextX = rectX + (rectangleWidth - bonusTextWidth) / 2;
            int rectTextY = y + (rectangleHeight - textHeight) / 2 + textHeight;
            g2d.drawString(bonusText, rectTextX, rectTextY);

            x += colorGroupSpacing;
        }

        Integer goldCount = currentTokens.getOrDefault("gold", 0);
        g2d.setColor(goldColor);
        g2d.fillOval(x, y, circleDiameter, circleDiameter);

        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x, y, circleDiameter, circleDiameter);

        g2d.setColor(Color.BLACK);
        String goldText = String.valueOf(goldCount);
        FontMetrics goldMetrics = g2d.getFontMetrics();
        int goldTextWidth = goldMetrics.stringWidth(goldText);
        int goldTextHeight = goldMetrics.getAscent();
        int goldTextX = x + (circleDiameter - goldTextWidth) / 2 + 1;
        int goldTextY = y + (circleDiameter - goldTextHeight) / 2 + goldTextHeight - 1;
        g2d.drawString(goldText, goldTextX, goldTextY);
    }
}