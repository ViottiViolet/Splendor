package Game.Token;

import javax.swing.*;

import Game.Inventory.CycleInventory;
import Game.Inventory.TokenInventory;
import Game.Main.SplendorGameScreen;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TokenManager {
    private final Map<String, JLabel> tokens;
    private final Map<String, JLabel> tokenCounts;
    private final Map<String, Integer> availableTokens;
    private final Map<String, ImageIcon> normalIcons;
    private final Map<String, ImageIcon> hoverIcons;
    private final JPanel tokenPanel;
    private final JLabel resetLabel;
    private final JLabel confirmLabel;

    private static final int TOKEN_SIZE = 70;
    private static final int TOKEN_SPACING = 10;
    private static final float HOVER_SCALE = 1.1f;
    private static final int PANEL_PADDING = 20;

    private static final Color COUNT_BACKGROUND = new Color(20, 20, 20, 230);
    private static final Color COUNT_BORDER = new Color(255, 255, 255, 100);
    private static final int COUNT_HEIGHT = 28;
    private static final int COUNT_WIDTH = 40;
    private static final int COUNT_VERTICAL_OFFSET = 40;

    private TokenInventory currentPlayerInventory;

    private final SplendorGameScreen gameScreen; // Add reference to game screen
    private final CycleInventory cycleInventory;
    private int totalTokenCount = 0; // Track total tokens
    private static final int MAX_TOKENS = 10;
    private final Map<Integer, Integer> playerTokenCounts; // Store token counts for each player
    private final ArrayList<String> tokensTakenInTurn;
    private int currentPlayer = 0; // Track current player

    public TokenManager(int playerCount, SplendorGameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.cycleInventory = gameScreen.getCycleInventory();
        tokens = new HashMap<>();
        tokenCounts = new HashMap<>();
        availableTokens = new HashMap<>();
        normalIcons = new HashMap<>();
        hoverIcons = new HashMap<>();
        tokenPanel = new JPanel(null);
        tokenPanel.setOpaque(false);
        this.playerTokenCounts = new HashMap<>();
        tokensTakenInTurn = new ArrayList<>();
        // Initialize token counts for each player
        for (int i = 0; i < playerCount; i++) {
            playerTokenCounts.put(i, 0);
        }

        // Initialize token counts based on player count
        initializeTokenCounts(playerCount);

        // Initialize token counts based on player count
        initializeTokenCounts(playerCount);

        String[] tokenColors = { "white", "blue", "green", "red", "black", "gold" };

        for (String color : tokenColors) {
            // Load and resize images
            ImageIcon originalIcon = new ImageIcon("src/Images/Tokens/" + color + ".png");
            normalIcons.put(color, resizeImageIcon(originalIcon, TOKEN_SIZE, TOKEN_SIZE));

            int hoverSize = (int) (TOKEN_SIZE * HOVER_SCALE);
            hoverIcons.put(color, resizeImageIcon(originalIcon, hoverSize, hoverSize));

            // Create token label
            JLabel tokenLabel = createTokenLabel(color);
            tokens.put(color, tokenLabel);
            tokenPanel.add(tokenLabel);

            // Create count label
            JLabel countLabel = createCountLabel(color);
            tokenCounts.put(color, countLabel);
            tokenPanel.add(countLabel);
        }

        // Create reset button
        resetLabel = new JLabel("reset");
        resetLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resetLabel.setForeground(Color.WHITE);
        resetLabel.setBounds(tokenPanel.getWidth()/2,tokenPanel.getHeight()/2 + 25, 50, 25);
        tokenPanel.add(resetLabel);

        resetLabel.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {

            }


            public void mouseExited(MouseEvent e) {

            }


            public void mouseClicked(MouseEvent e) {
                if (gameScreen.getPlayerTurn() != gameScreen.getCycleInventory().getCurrentPlayerIndex()) return;
                if (!tokensTakenInTurn.isEmpty()) resetPick();
            }
        });

        confirmLabel = new JLabel("confirm");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 16));
        confirmLabel.setForeground(Color.WHITE);
        confirmLabel.setBounds(tokenPanel.getWidth()/2,tokenPanel.getHeight()/2 + 75, 75, 25);
        tokenPanel.add(confirmLabel);

        confirmLabel.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {

            }


            public void mouseExited(MouseEvent e) {

            }


            public void mouseClicked(MouseEvent e) {
                if (gameScreen.getPlayerTurn() != gameScreen.getCycleInventory().getCurrentPlayerIndex()) return;
                tokensTakenInTurn.removeAll(tokensTakenInTurn);
                gameScreen.nextPlayerTurn();
            }
        });
    }

    public void setCurrentPlayerInventory(TokenInventory inventory, int playerIndex) {
        this.currentPlayerInventory = inventory;
        this.currentPlayer = playerIndex;
        // Update the total token count to reflect the current player's count
        this.totalTokenCount = playerTokenCounts.getOrDefault(playerIndex, 0);
        // Update the UI to show current player's token count
        gameScreen.updateTotalTokensLabel(totalTokenCount);
    }

    private void initializeTokenCounts(int playerCount) {
        int regularTokenCount;
        int goldTokenCount;

        switch (playerCount) {
            case 2:
                regularTokenCount = 4;
                goldTokenCount = 2;
                break;
            case 3:
                regularTokenCount = 5;
                goldTokenCount = 3;
                break;
            case 4:
                regularTokenCount = 7;
                goldTokenCount = 5;
                break;
            default:
                regularTokenCount = 4;
                goldTokenCount = 2;
        }

        String[] colors = { "white", "blue", "green", "red", "black" };
        for (String color : colors) {
            availableTokens.put(color, regularTokenCount);
        }
        availableTokens.put("gold", goldTokenCount);
    }

    private JLabel createCountLabel(String color) {
        JLabel label = new JLabel(availableTokens.get(color).toString()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Create rounded rectangle background
                RoundRectangle2D.Float background = new RoundRectangle2D.Float(
                        0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fill(new RoundRectangle2D.Float(2, 2, getWidth() - 1, getHeight() - 1, 12, 12));

                // Draw background
                g2d.setColor(COUNT_BACKGROUND);
                g2d.fill(background);

                // Draw border
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(COUNT_BORDER);
                g2d.draw(background);

                // Draw text with shadow
                String text = getText();
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

                // Draw text shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(text, textX + 1, textY + 1);

                // Draw text
                g2d.setColor(Color.WHITE);
                g2d.drawString(text, textX, textY);

                g2d.dispose();
            }
        };

        // Set font and properties
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setSize(COUNT_WIDTH, COUNT_HEIGHT);
        label.setOpaque(false);

        return label;
    }

    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        // Scale the image smoothly to maintain quality
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    public void setCurrentPlayerInventory(TokenInventory inventory) {
        this.currentPlayerInventory = inventory;
    }

    private JLabel createTokenLabel(String color) {
        JLabel label = new JLabel(normalIcons.get(color));
        label.setSize(TOKEN_SIZE, TOKEN_SIZE);

        label.addMouseListener(new MouseAdapter() {
            private Point originalLocation;

            @Override
            public void mouseEntered(MouseEvent e) {
                // Only show hover effect if tokens are available and player hasn't reached
                // limit
                if (availableTokens.get(color) > 0 && totalTokenCount < MAX_TOKENS) {
                    originalLocation = label.getLocation();
                    int offsetX = (int) ((TOKEN_SIZE * HOVER_SCALE - TOKEN_SIZE) / 2);
                    int offsetY = (int) ((TOKEN_SIZE * HOVER_SCALE - TOKEN_SIZE) / 2);

                    label.setLocation(originalLocation.x - offsetX, originalLocation.y - offsetY);
                    label.setIcon(hoverIcons.get(color));
                    label.setSize((int) (TOKEN_SIZE * HOVER_SCALE), (int) (TOKEN_SIZE * HOVER_SCALE));
                    label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    tokenPanel.setComponentZOrder(label, 0);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (originalLocation != null) {
                    label.setLocation(originalLocation);
                    label.setIcon(normalIcons.get(color));
                    label.setSize(TOKEN_SIZE, TOKEN_SIZE);
                    label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameScreen.getPlayerTurn() != gameScreen.getCycleInventory().getCurrentPlayerIndex()) return;
                if (totalTokenCount >= MAX_TOKENS) {
                    JOptionPane.showMessageDialog(
                        tokenPanel,
                        "Cannot choose more tokens. Maximum limit (10/10) reached!",
                        "Token Limit Reached",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (tokensTakenInTurn.size() == 3)
                {
                    JOptionPane.showMessageDialog(
                            tokenPanel,
                            "Cannot choose more tokens. Can only pick 2 identical tokens or 3 different tokens. Please reset tokens.",
                            "Token Limit Reached",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (tokensTakenInTurn.size() == 2 && tokensTakenInTurn.get(0).equals(tokensTakenInTurn.get(1)))
                {
                    JOptionPane.showMessageDialog(
                            tokenPanel,
                            "Cannot choose more tokens. Can only pick 2 identical tokens or 3 different tokens. Please reset tokens.",
                            "Token Limit Reached",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (tokensTakenInTurn.contains(color))
                {

                    if (tokensTakenInTurn.size()!=2)
                    {
                        takeToken(color);
                        return;
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(
                                tokenPanel,
                                "Cannot choose more tokens. Can only pick 2 identical tokens or 3 different tokens. Please reset tokens.",
                                "Token Limit Reached",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                }

                if (currentPlayerInventory != null && availableTokens.get(color) > 0) {
                    if (canTakeToken(color)) {
                        takeToken(color);
                    }
                }
            }
        });

        return label;
    }

    private boolean canTakeToken(String color) {
        // Rule 1: Can't take if no tokens available
        if (availableTokens.get(color) <= 0) {
            return false;
        }

        // Rule 2: Can't take gold tokens directly
        if (color.equals("gold")) {
            return false;
        }

        // Rule 3: Player can't have more than 10 tokens total
        return totalTokenCount < MAX_TOKENS;
    }

    private void takeToken(String color)
    {
        decrementToken(color);
        tokensTakenInTurn.add(color);
        currentPlayerInventory.addToken(color);
        totalTokenCount++;
        // Store the updated count for the current player
        playerTokenCounts.put(currentPlayer, totalTokenCount);
        gameScreen.updateTotalTokensLabel(totalTokenCount);
        updateCountLabel(color, availableTokens.get(color));
        tokenPanel.repaint();
    }

    public void addToken(String color, int num) {
        int currentCount = availableTokens.get(color);
        availableTokens.put(color, currentCount + num);
        updateCountLabel(color, currentCount + num);
    }

    public void removeToken(String color, int num) {
        int currentCount = availableTokens.get(color);
        if (currentCount > 0) {
            availableTokens.put(color, currentCount - num);
            updateCountLabel(color, currentCount - num);
        }
    }

    private void resetPick()
    {
        String color = "";
        while(!tokensTakenInTurn.isEmpty())
        {
            color = tokensTakenInTurn.remove(0);

            incrementToken(color);
            updateCountLabel(color, availableTokens.get(color));

            currentPlayerInventory.removeToken(color);
            totalTokenCount--;
            playerTokenCounts.put(currentPlayer, totalTokenCount);
            gameScreen.updateTotalTokensLabel(totalTokenCount);
        }
    }

    public void updateTokenPositions(int containerWidth) {
        int totalWidth = (TOKEN_SIZE * 6) + (TOKEN_SPACING * 5);
        int startX = (containerWidth - totalWidth) / 2;
        int currentX = startX;
        int centerY = (tokenPanel.getHeight() - TOKEN_SIZE) / 2;

        String[] orderedColors = { "white", "blue", "green", "red", "black", "gold" };
        for (String color : orderedColors) {
            // Position token
            JLabel token = tokens.get(color);
            token.setLocation(currentX, centerY);

            // Position count label centered above token
            JLabel countLabel = tokenCounts.get(color);
            countLabel.setLocation(
                    currentX + (TOKEN_SIZE - COUNT_WIDTH) / 2,
                    centerY - COUNT_VERTICAL_OFFSET);

            currentX += TOKEN_SIZE + TOKEN_SPACING;
        }
    }

    public int getTokenCount(String color) {
        return availableTokens.get(color);
    }

    public void decrementToken(String color) {
        int currentCount = availableTokens.get(color);
        if (currentCount > 0) {
            availableTokens.put(color, currentCount - 1);
            updateCountLabel(color, currentCount - 1);
        }
    }

    public void incrementToken(String color) {
        int currentCount = availableTokens.get(color);
        availableTokens.put(color, currentCount + 1);
        updateCountLabel(color, currentCount + 1);
    }

    public JPanel getTokenPanel() {
        return tokenPanel;
    }

    public void setTokenPanelBounds(int x, int y, int width, int height) {
        // Calculate minimum height needed for tokens plus hover effect and padding
        int minHeight = (int) (TOKEN_SIZE * HOVER_SCALE) + PANEL_PADDING * 5;
        tokenPanel.setBounds(x, y, width, minHeight);
        updateTokenPositions(width);
    }

    private void updateCountLabel(String color, int newValue) {
        JLabel countLabel = tokenCounts.get(color);
        countLabel.setText(String.valueOf(newValue));
        // Trigger repaint for the visual update
        countLabel.repaint();
    }
    
    // Add method to get token count for a specific player
    public int getPlayerTokenCount(int playerIndex) {
        return playerTokenCounts.getOrDefault(playerIndex, 0);
    }

    public void setPlayerTokenCount(int num) {
        playerTokenCounts.put(currentPlayer, totalTokenCount += num);
        gameScreen.updateTotalTokensLabel(totalTokenCount);
        tokenPanel.repaint();
    }
}