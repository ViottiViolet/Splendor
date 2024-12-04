package Game.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Stack;
import Cards.Card;
import Cards.CardGridManager;
import Cards.CardLoader;
import Game.Inventory.CycleInventory;
import Game.Inventory.ReserveInventory;
import Game.Inventory.TokenInventory;
import Game.Token.TokenManager;

public class SplendorGameScreen extends JPanel {
    private final ImageIcon bImageIcon;
    private ImageIcon level1fd, level2fd, level3fd;
    private Stack<Card> level1Cards, level2Cards, level3Cards;
    private JPanel gridPanel1, gridPanel2, gridPanel3;
    private final CardGridManager gridManager;
    private final TokenManager tokenManager;
    private JLabel totalTokensLabel;  // New label for total tokens

    private CycleInventory cycleInventory;
    private static final int TOKEN_INVENTORY_WIDTH = 900;
    private static final int TOKEN_INVENTORY_HEIGHT = 75;

    private static final int GRID_WIDTH = 700;
    private static final int GRID_HEIGHT = 200;
    private static final int VERTICAL_GAP = 10;
    private static final int TOKEN_PANEL_HEIGHT = 100;
    @SuppressWarnings("unused")
    private final int playerCount;
    private JLabel playerNumberLabel;

    private ReserveInventory reserveInventory;

    public SplendorGameScreen(CardLoader cardLoader, int playerCount) {
        this.playerCount = playerCount;
        gridManager = new CardGridManager();
        tokenManager = new TokenManager(playerCount, this); // Pass 'this' reference
        
        // Initialize total tokens label
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
        
        // In SplendorGameScreen constructor, update the listener to match your interface:
        cycleInventory.addPlayerChangeListener(newInventory -> {
            int currentPlayerIndex = cycleInventory.getCurrentPlayerIndex();
            tokenManager.setCurrentPlayerInventory(newInventory, currentPlayerIndex);
            updateTotalTokensLabel(tokenManager.getPlayerTokenCount(currentPlayerIndex));
            playerNumberLabel.setText("Player " + (currentPlayerIndex + 1));
            reserveInventory.switchToPlayer(currentPlayerIndex); // Use switchToPlayer instead of resetForNewPlayer
        });

        playerNumberLabel = new JLabel("Player 1");
        playerNumberLabel.setForeground(Color.WHITE);
        playerNumberLabel.setFont(new Font("Gothic", Font.BOLD, 25));
        playerNumberLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        playerNumberLabel.setBounds(1400, 10, 100, 40); // Adjust the bounds to fit the top-right corner

        // Add it to the panel
        this.add(playerNumberLabel);

        reserveInventory = new ReserveInventory();
        add(reserveInventory);
        
        // Update the updateGridPositions method to include reserve inventory positioning
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

    // Update the method to show current/max tokens
    public void updateTotalTokensLabel(int currentTokens) {
        totalTokensLabel.setText("Total Tokens: " + currentTokens + "/10");
    }

    private void updateGridPositions() {
        int gridX = (getWidth() - GRID_WIDTH) / 2;
        
        tokenManager.setTokenPanelBounds(gridX - 100, 5, GRID_WIDTH + 200, TOKEN_PANEL_HEIGHT);
        
        gridPanel3.setBounds(gridX, 150, GRID_WIDTH, GRID_HEIGHT);
        gridPanel2.setBounds(gridX, 135 + GRID_HEIGHT + VERTICAL_GAP - 5, GRID_WIDTH, GRID_HEIGHT);
        gridPanel1.setBounds(gridX, 110 + (GRID_HEIGHT + VERTICAL_GAP) * 2, GRID_WIDTH, GRID_HEIGHT);

        int totalGridHeight = TOKEN_PANEL_HEIGHT + (GRID_HEIGHT + VERTICAL_GAP) * 3;
        
        int inventoryX = (getWidth() - TOKEN_INVENTORY_WIDTH) / 2;
        int inventoryY = totalGridHeight + 30;
        
        // Position total tokens label above the inventory
        totalTokensLabel.setBounds(inventoryX, inventoryY - 30, TOKEN_INVENTORY_WIDTH, 25);
        
        cycleInventory.setBounds(inventoryX, inventoryY, TOKEN_INVENTORY_WIDTH, TOKEN_INVENTORY_HEIGHT);

        int reserveX = (getWidth() - TOKEN_INVENTORY_WIDTH) / 2;
        int reserveY = cycleInventory.getY() + cycleInventory.getHeight() + 10;
        reserveInventory.setBounds(reserveX, reserveY, 320, 170);
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
    
    public TokenInventory getCurrentPlayerInventory() {
        return cycleInventory.getInventory();
    }

}