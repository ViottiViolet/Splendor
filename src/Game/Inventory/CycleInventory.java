package Game.Inventory;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CycleInventory extends JPanel {
    private final JLabel leftArrow;
    private final JLabel rightArrow;
    private final int totalPlayers;
    private int currentPlayerIndex;
    private static final int ARROW_SIZE = 50;
    private final TokenInventory inventory;
    private final java.util.List<PlayerChangeListener> playerChangeListeners;

    // Interface for player change notifications
    public interface PlayerChangeListener {
        void onPlayerChanged(TokenInventory newInventory);
    }

    public CycleInventory(int playerCount) {
        this.totalPlayers = playerCount;
        this.currentPlayerIndex = 0;
        this.playerChangeListeners = new java.util.ArrayList<>();

        setLayout(null);
        setOpaque(false);

        // Initialize navigation arrows with proper scaling
        ImageIcon leftIcon = new ImageIcon("src/Images/MiscellaneousImages/LeftArrow.png");
        ImageIcon rightIcon = new ImageIcon("src/Images/MiscellaneousImages/RightArrow.png");

        // Scale the images to the desired size while maintaining aspect ratio
        Image leftImg = leftIcon.getImage().getScaledInstance(ARROW_SIZE, ARROW_SIZE, Image.SCALE_SMOOTH);
        Image rightImg = rightIcon.getImage().getScaledInstance(ARROW_SIZE, ARROW_SIZE, Image.SCALE_SMOOTH);

        leftArrow = new JLabel(new ImageIcon(leftImg));
        rightArrow = new JLabel(new ImageIcon(rightImg));

        // Create inventory
        inventory = new TokenInventory(1, totalPlayers);

        // Add mouse listeners for arrows
        leftArrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPreviousPlayer();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        rightArrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNextPlayer();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        // Add components
        add(leftArrow);
        add(rightArrow);
        add(inventory);
    }

    // Method to add player change listeners
    public void addPlayerChangeListener(PlayerChangeListener listener) {
        playerChangeListeners.add(listener);
    }

    // Method to notify listeners of player change
    private void notifyPlayerChanged() {
        for (PlayerChangeListener listener : playerChangeListeners) {
            listener.onPlayerChanged(inventory);
        }
    }

    private void showNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % totalPlayers;
        inventory.setCurrentPlayerIndex(currentPlayerIndex);
        notifyPlayerChanged();
        repaint();
    }

    private void showPreviousPlayer() {
        currentPlayerIndex = (currentPlayerIndex - 1 + totalPlayers) % totalPlayers;
        inventory.setCurrentPlayerIndex(currentPlayerIndex);
        notifyPlayerChanged();
        repaint();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);

        // Position arrows with proper spacing
        leftArrow.setBounds(5, height/2 - ARROW_SIZE/2, ARROW_SIZE, ARROW_SIZE);
        rightArrow.setBounds(width - ARROW_SIZE - 5, height/2 - ARROW_SIZE/2, ARROW_SIZE, ARROW_SIZE);

        // Position inventory between arrows
        inventory.setBounds(ARROW_SIZE + 10, 0, width - (2 * ARROW_SIZE) - 20, height);
    }

    public TokenInventory getInventory() {
        return inventory;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    // Method to get current player's inventory (convenience method)
    public TokenInventory getCurrentInventory() {
        return inventory;
    }

}