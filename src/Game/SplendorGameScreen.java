package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SplendorGameScreen extends JFrame {

  // Inner class to paint background image
  class BackgroundPanel extends JPanel {
      @Override
      protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          ImageIcon bg = new ImageIcon("src/Images/GameMenu/GameBackground.png");
          g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
      }
  }
  
  public static void main(String[] args) {
      //new SplendorGameScreen();
  }

{
