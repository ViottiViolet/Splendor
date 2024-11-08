package Tokens;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.JButton;
import java.io.*;
public class TokenLoader extends JPanel {
    private String tokenType;
    private BufferedImage token;
    private int tokenNum;
    private final ImageIcon diamond, sapphire, emerald, ruby, onyx;

    public TokenLoader(String tokenType, BufferedImage token, int tokenNum){
        this.tokenType = tokenType;
        this.token = token;
        this.tokenNum = tokenNum;
        diamond = new ImageIcon("src/images/diamond.png");
        sapphire = new ImageIcon("src/images/sapphire.png");
        emerald = new ImageIcon("src/images/emerald.png");
        ruby = new ImageIcon("src/images/ruby.png");
        onyx = new ImageIcon("src/images/onyx.png");
    }
    public void highlightSelectedTokens(Graphics g, String tokenType){
        g.setColor(Color.yellow);
        if (tokenType == "Diamond") {
            g.drawOval(getWidth() - 750, getHeight() - 650, diamond.getIconWidth(), diamond.getIconHeight());
        }
        if (tokenType == "Sapphire") {
            g.drawOval(getWidth() - 675, getHeight() - 650, sapphire.getIconWidth(), sapphire.getIconHeight());
        }
        if(tokenType == "Emerald") {
            g.drawOval(getWidth() - 600, getHeight() - 650, emerald.getIconWidth(), emerald.getIconHeight());
        }
        if(tokenType == "Ruby") {
            g.drawOval(getWidth() - 525, getHeight() - 650, ruby.getIconWidth(), ruby.getIconHeight());
        }
        if(tokenType == "Onyx") {
            g.drawOval(getWidth() - 450, getHeight() - 650, onyx.getIconWidth(), onyx.getIconHeight());
        }
    }
    public void drawTokens(Graphics g){
        if(tokenType == "Diamond"){
            g.drawImage(diamond.getImage(), getWidth()-750, getHeight()-650, 50, 50, null);
        }
        if(tokenType == "Sapphire"){
            g.drawImage(emerald.getImage(), getWidth()-675, getHeight()-650, 50, 50, null);
        }
        if(tokenType == "Emerald"){
            g.drawImage(ruby.getImage(), getWidth()-600, getHeight()-650, 50, 50, null);
        }
        if(tokenType == "Ruby"){
            g.drawImage(onyx.getImage(), getWidth()-525, getHeight()-650, 50, 50, null);
        }
        if(tokenType == "Onyx"){
            g.drawImage(onyx.getImage(), getWidth()-450, getHeight()-650, 50, 50, null);
        }
    }
}