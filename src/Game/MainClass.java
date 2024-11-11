package Game;

import javax.swing.*;
import Cards.CardLoader;
import java.awt.*;

public class MainClass {

    public static void main(String[] args) {
        // Create a new JFrame
        JFrame frame = new JFrame("Splendor Game Screen");
        
        // Set the default close operation, size, and layout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1900, 1050);
        frame.setLayout(new BorderLayout());

        // Initialize CardLoader with the path to the card data file
        CardLoader cardLoader = new CardLoader("C:/Coding/Kush Comp Sci 3/Splendor/src/Cards/CardData.txt"); // Update with the actual path

        // Create an instance of SplendorGameScreen with cardLoader and add it to the frame
        SplendorGameScreen gameScreen = new SplendorGameScreen(cardLoader);
        frame.add(gameScreen, BorderLayout.CENTER);

        // Center the frame on the screen and make it visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
