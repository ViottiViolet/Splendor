package Game.Main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import Cards.Card;
import Cards.CardGridManager;
import Cards.CardLoader;
import Game.Token.TokenManager;
import Nobles.NobleLoader;
import Game.Inventory.*;


public class SplendorGameScreen extends JPanel {
    private final ImageIcon bImageIcon;
    private ImageIcon level1fd, level2fd, level3fd;
    private Stack<Card> level1Cards, level2Cards, level3Cards;
    private JPanel gridPanel1, gridPanel2, gridPanel3;
    private final CardGridManager gridManager;
    private final TokenManager tokenManager;
    private JLabel totalTokensLabel;
    private JPanel nobleGridPanel; // Noble grid panel
    private nobleInventory nobleInventory; // Declare the NobleInventory


    private CycleInventory cycleInventory;
    private static final int TOKEN_INVENTORY_WIDTH = 900;
    private static final int TOKEN_INVENTORY_HEIGHT = 75;

    private static final int GRID_WIDTH = 700;
    private static final int GRID_HEIGHT = 200;
    private static final int VERTICAL_GAP = 10;
    private static final int TOKEN_PANEL_HEIGHT = 100;

    private final int playerCount;
    private JLabel playerNumberLabel;
    private JLabel turnLabel;
    private int playerTurn = 0;

    // New fields for player scores
    private ArrayList<JLabel> playerScoreLabels;
    private ArrayList<Integer> playerScores;

    private ReserveInventory reserveInventory;

    private int winnerIndex = 0;
    private boolean gameEnd = false;

    public SplendorGameScreen(CardLoader cardLoader, int playerCount) {
        this.playerCount = playerCount;

        // Initialize player scores
        initializePlayerScores();

        gridManager = new CardGridManager();
        tokenManager = new TokenManager(playerCount, this);

        totalTokensLabel = new JLabel("Total Tokens: 0/10");
        totalTokensLabel.setForeground(Color.WHITE);
        totalTokensLabel.setFont(new Font("Gothic", Font.BOLD, 20));
        totalTokensLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(totalTokensLabel);

        bImageIcon = new ImageIcon("src/Images/GameMenu/GameBackground.png");
        level1fd = new ImageIcon("src/Images/Level1/L1BC.png");
        level2fd = new ImageIcon("src/Images/Level2/L2BC.png");
        level3fd = new ImageIcon("src/Images/Level3/L3BC.png");

        level1Cards = cardLoader.getLevel1Cards();
        level2Cards = cardLoader.getLevel2Cards();
        level3Cards = cardLoader.getLevel3Cards();

        Collections.shuffle(level1Cards);
        Collections.shuffle(level2Cards);
        Collections.shuffle(level3Cards);

        setLayout(null);

        gridPanel1 = gridManager.createLevelGrid(level1Cards, level1fd);
        gridPanel2 = gridManager.createLevelGrid(level2Cards, level2fd);
        gridPanel3 = gridManager.createLevelGrid(level3Cards, level3fd);

        add(tokenManager.getTokenPanel());
        add(gridPanel1);
        add(gridPanel2);
        add(gridPanel3);

        cycleInventory = new CycleInventory(playerCount);
        add(cycleInventory);

        tokenManager.setCurrentPlayerInventory(cycleInventory.getInventory(), cycleInventory.getCurrentPlayerIndex());

        cycleInventory.addPlayerChangeListener(newInventory -> {
            int currentPlayerIndex = cycleInventory.getCurrentPlayerIndex();
            tokenManager.setCurrentPlayerInventory(newInventory, currentPlayerIndex);
            updateTotalTokensLabel(tokenManager.getPlayerTokenCount(currentPlayerIndex));
            playerNumberLabel.setText("Player " + (currentPlayerIndex + 1));
            reserveInventory.switchToPlayer(currentPlayerIndex);
            nobleInventory.switchToPlayer(currentPlayerIndex);

            // Update player score label highlighting
            updatePlayerScoreLabelHighlighting(currentPlayerIndex);
        });

        playerNumberLabel = new JLabel("Player 1");
        playerNumberLabel.setForeground(Color.WHITE);
        playerNumberLabel.setFont(new Font("Gothic", Font.BOLD, 25));
        playerNumberLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        playerNumberLabel.setBounds(1400, 10, 200, 40);
        this.add(playerNumberLabel);

        turnLabel = new JLabel("Current Turn: " + (getPlayerTurn() + 1));
        turnLabel.setForeground(Color.YELLOW);
        turnLabel.setFont(new Font("Gothic", Font.BOLD, 15));
        turnLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        turnLabel.setBounds(1405, 50, 200, 40);
        this.add(turnLabel);

        reserveInventory = new ReserveInventory();
        add(reserveInventory);

        // Instantiate and add NobleInventory
        nobleInventory = new nobleInventory();
        add(nobleInventory);

        // Add player score labels
        addPlayerScoreLabels();

        // Noble Loader setup
        NobleLoader nobleLoader = new NobleLoader("src/Nobles/NobleData.txt");
        try {
            nobleLoader.loadNobles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the noble grid panel
        nobleGridPanel = nobleLoader.createNobles();
        nobleGridPanel.setBackground(new Color(0, 0, 0, 50)); // Semi-transparent background
        add(nobleGridPanel);

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

    // Initialize player scores
    private void initializePlayerScores() {
        playerScores = new ArrayList<>(playerCount);
        for (int i = 0; i < playerCount; i++) {
            playerScores.add(0);
        }
    }

    // Add player score labels to the screen
    private void addPlayerScoreLabels() {
        playerScoreLabels = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            JLabel scoreLabel = new JLabel("Player " + (i + 1) + ": 0");
            scoreLabel.setForeground(Color.WHITE);
            if (i == 0) scoreLabel.setForeground(Color.YELLOW);
            scoreLabel.setFont(new Font("Gothic", Font.BOLD, 20));
            scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(scoreLabel);
            playerScoreLabels.add(scoreLabel);
        }
    }

    // Update player score label highlighting
    private void updatePlayerScoreLabelHighlighting(int currentPlayerIndex) {
        for (int i = 0; i < playerScoreLabels.size(); i++) {
            JLabel scoreLabel = playerScoreLabels.get(i);
            if (i == currentPlayerIndex) {
                scoreLabel.setForeground(Color.YELLOW);  // Highlight current player's score
            } else {
                scoreLabel.setForeground(Color.WHITE);   // Normal color for other players
            }
        }
    }

    // Method to update player score when buying a card
    public void updatePlayerScore(int playerIndex, int prestigePoints) {
        if (playerIndex >= 0 && playerIndex < playerScores.size()) {
            // Update the score
            playerScores.set(playerIndex, playerScores.get(playerIndex) + prestigePoints);
            playerScoreLabels.get(playerIndex).setText("Player " + (playerIndex + 1) + ": " + playerScores.get(playerIndex));

            // Check if the current player has won
            checkForWinner(playerIndex);
        }
    }

    // New method to check for a winner
    private void checkForWinner(int playerIndex) {
        if (playerScores.get(playerIndex) >= 15) {

            winnerIndex = playerIndex;
            gameEnd = true;
            System.out.println("FINAL TURNS");

        }
    }

    private void transitionToEnd()
    {

        if (gameEnd && playerTurn == 0) {
            // Player has won - close the game window and open end screen
            SwingUtilities.invokeLater(() -> {
                // Get the parent window (JFrame)
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (parentFrame != null) {
                    parentFrame.dispose(); // Close the game window
                }

                // Open the game end screen with the winning player number
                new GameEndScreen(winnerIndex + 1).setVisible(true);
            });
        }

    }

    // Update the method to show current/max tokens
    public void updateTotalTokensLabel(int currentTokens) {
        totalTokensLabel.setText("Total Tokens: " + currentTokens + "/10");
    }

    private void updateGridPositions() {
        int gridX = (getWidth() - GRID_WIDTH) / 2;
    
        tokenManager.setTokenPanelBounds(gridX - 100, 5, GRID_WIDTH + 200, TOKEN_PANEL_HEIGHT);
    
        gridPanel3.setBounds(gridX - 150, 150, GRID_WIDTH, GRID_HEIGHT);
        gridPanel2.setBounds(gridX - 150, 135 + GRID_HEIGHT + VERTICAL_GAP - 5, GRID_WIDTH, GRID_HEIGHT);
        gridPanel1.setBounds(gridX - 150, 110 + (GRID_HEIGHT + VERTICAL_GAP) * 2, GRID_WIDTH, GRID_HEIGHT);
    
        int nobleGridX = gridX + GRID_WIDTH - 100;
        int nobleGridY = 150;
        int nobleGridWidth = 2 * (NobleLoader.NOBLE_WIDTH + 10) + 10; // Account for horizontal gap
        int nobleGridHeight = 3 * (NobleLoader.NOBLE_HEIGHT + 15) + 15; // Account for vertical gap
        nobleGridPanel.setBounds(nobleGridX, nobleGridY, nobleGridWidth, nobleGridHeight);
    
        int totalGridHeight = TOKEN_PANEL_HEIGHT + (GRID_HEIGHT + VERTICAL_GAP) * 3;
    
        int inventoryX = (getWidth() - TOKEN_INVENTORY_WIDTH) / 2;
        int inventoryY = totalGridHeight + 30;
    
        totalTokensLabel.setBounds(inventoryX, inventoryY - 30, TOKEN_INVENTORY_WIDTH, 25);
        cycleInventory.setBounds(inventoryX, inventoryY, TOKEN_INVENTORY_WIDTH, TOKEN_INVENTORY_HEIGHT);
    
        int reserveX = (getWidth() - TOKEN_INVENTORY_WIDTH) / 2;
        int reserveY = cycleInventory.getY() + cycleInventory.getHeight() + 10;
        reserveInventory.setBounds(reserveX, reserveY, 320, 170);

        // Position NobleInventory to the left of ReserveInventory
        int nobleX = reserveX + 340; // Adjust width + spacing
        int nobleY = reserveY; // Align vertically with ReserveInventory
        nobleInventory.setBounds(nobleX, nobleY, 550, 170);

        // Position player score labels
        int scoreLabelsHeight = TOKEN_INVENTORY_HEIGHT / playerCount;
        int scoreLabelsWidth = TOKEN_INVENTORY_WIDTH / playerCount;
        for (int i = 0; i < playerScoreLabels.size(); i++) {
            JLabel scoreLabel = playerScoreLabels.get(i);
            scoreLabel.setBounds(100, inventoryY + i * scoreLabelsHeight - 500, scoreLabelsWidth, 30);
        }
    }
    

    public TokenInventory getPlayerInventory() {
        return cycleInventory.getInventory();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    public ReserveInventory getReserveInventory() {
        return reserveInventory;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public nobleInventory getNobleInventory(){
        return nobleInventory;
    }

    public TokenInventory getCurrentPlayerInventory() {
        return cycleInventory.getInventory();
    }

    public int getPlayerTurn() { return playerTurn; }

    public void nextPlayerTurn() {
        playerTurn = (playerTurn + 1) % playerCount;
        cycleInventory.showSpecificPlayer(playerTurn);

        // Directly update the existing turnLabel instead of creating a new one
        turnLabel.setText("Current Turn: " + (playerTurn + 1));

        transitionToEnd();
    }

    public CycleInventory getCycleInventory() {
        return cycleInventory;
    }


}