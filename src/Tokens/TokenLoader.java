package Tokens;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

public class TokenLoader extends JPanel {
    public enum TokenType {
        DIAMOND("diamond"),
        SAPPHIRE("sapphire"),
        EMERALD("emerald"),
        RUBY("ruby"),
        ONYX("onyx");

        private final String filename;

        TokenType(String filename) {
            this.filename = filename;
        }

        public String getFilename() {
            return filename;
        }
    }

    private final TokenType tokenType;
    private final BufferedImage token;
    private final int tokenNum;
    private final Map<TokenType, ImageIcon> tokenImages;
    private static final int TOKEN_SIZE = 50;
    private static final int BASE_X = 750;
    private static final int BASE_Y = 650;
    private static final int SPACING = 75;

    public TokenLoader(TokenType tokenType, BufferedImage token, int tokenNum) {
        this.tokenType = tokenType;
        this.token = token;
        this.tokenNum = tokenNum;

        this.tokenImages = new EnumMap<>(TokenType.class);
        for (TokenType type : TokenType.values()) {
            String path = "src/Images/Tokens/" + type.getFilename() + ".png";
            tokenImages.put(type, new ImageIcon(path));
        }
    }

    public void highlightSelectedTokens(Graphics g, TokenType tokenType) {
        if (tokenType == null) return;

        g.setColor(Color.yellow);
        ImageIcon icon = tokenImages.get(tokenType);
        if (icon != null) {
            int xPos = getWidth() - BASE_X + getTokenXOffset(tokenType);
            int yPos = getHeight() - BASE_Y;
            g.drawOval(xPos, yPos, icon.getIconWidth(), icon.getIconHeight());
        }
    }

    public void drawTokens(Graphics g) {
        if (tokenType == null) return;

        ImageIcon icon = tokenImages.get(tokenType);
        if (icon != null) {
            int xPos = getWidth() - BASE_X + getTokenXOffset(tokenType);
            int yPos = getHeight() - BASE_Y;
            g.drawImage(icon.getImage(), xPos, yPos, TOKEN_SIZE, TOKEN_SIZE, null);
        }
    }

    private int getTokenXOffset(TokenType type) {
        return type.ordinal() * SPACING;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTokens(g);
    }
}
