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

        // Set the default close operation, size, and layout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1900, 1050);
        frame.setLayout(new BorderLayout());

        // Initialize CardLoader with the path to the card data file
        CardLoader cardLoader = new CardLoader("src/Cards/CardData.txt");

        // Create an instance of SplendorGameScreen with cardLoader and add it to the frame
        SplendorGameScreen gameScreen = new SplendorGameScreen(cardLoader, playerCount);
        frame.add(gameScreen, BorderLayout.CENTER);

        // Center the frame on the screen and make it visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}