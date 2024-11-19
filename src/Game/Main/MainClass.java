package Game.Main;

import javax.swing.*;
import Cards.CardLoader;
import java.awt.*;

public class MainClass {
    private static int playerCount = 2;

    public static void setPlayerCount(int count) {
        playerCount = count;
    }

    public static int getPlayerCount() {
        return playerCount;
    }

    public static void main(String[] args) {
        // Create a new JFrame
        JFrame frame = new JFrame("Splendor Game Screen");

        // Set the default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Get the screen dimensions
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        @SuppressWarnings("unused")
        DisplayMode dm = gd.getDisplayMode();
        
        // Set to maximized state with window decorations
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Set the layout
        frame.setLayout(new BorderLayout());

        // Initialize CardLoader with the path to the card data file
        CardLoader cardLoader = new CardLoader("src/Cards/CardData.txt");

        // Create an instance of SplendorGameScreen with cardLoader and add it to the frame
        SplendorGameScreen gameScreen = new SplendorGameScreen(cardLoader, playerCount);
        frame.add(gameScreen, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
    }
}