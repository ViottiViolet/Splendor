package Game.Inventory;

import Nobles.Noble;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class nobleInventory extends JPanel {
    private static final int MAX_NOBLES = 5; // Maximum number of nobles displayed per player
    private static final int PADDING = 10;
    private static final int CORNER_RADIUS = 15;
    private static final float BORDER_THICKNESS = 2.0f;

    private final Map<Integer, ArrayList<Noble>> playerNobles;
    private final Map<Integer, ArrayList<JLabel>> playerNobleLabels;
    private int currentPlayerIndex;

    public nobleInventory() {
        setLayout(new FlowLayout(FlowLayout.LEFT, PADDING, PADDING));
        setOpaque(false);

        // Initialize player-specific storage
        playerNobles = new HashMap<>();
        playerNobleLabels = new HashMap<>();
        currentPlayerIndex = 0;

        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(),
            "Noble Cards",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Gothic", Font.BOLD, 16),
            new Color(220, 220, 220)
        );
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING),
            border
        ));

        setPreferredSize(new Dimension(500, 200)); // Adjust width and height as needed
    }

    private void ensurePlayerStorageExists(int playerIndex) {
        playerNobles.computeIfAbsent(playerIndex, k -> new ArrayList<>());
        playerNobleLabels.computeIfAbsent(playerIndex, k -> new ArrayList<>());
    }

    public void addNoble(Noble noble) {
        ensurePlayerStorageExists(currentPlayerIndex);

        ArrayList<Noble> currentPlayerNobles = playerNobles.get(currentPlayerIndex);
        ArrayList<JLabel> currentPlayerLabels = playerNobleLabels.get(currentPlayerIndex);

        if (currentPlayerNobles.size() >= MAX_NOBLES) {
            JOptionPane.showMessageDialog(
                this,
                "Maximum number of nobles reached for the player.",
                "Cannot Add Noble",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        currentPlayerNobles.add(noble);

        // Calculate the size of the noble image dynamically
        int panelWidth = getWidth();
        int imageWidth = (panelWidth - (PADDING * (MAX_NOBLES + 1))) / MAX_NOBLES; // Divide panel space evenly
        int imageHeight = (int) (imageWidth * 1.4) - 20; // Maintain aspect ratio

        ImageIcon nobleImage = new ImageIcon(new ImageIcon(noble.getImage())
            .getImage()
            .getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH));

        JLabel nobleLabel = new JLabel(nobleImage) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
                super.paintComponent(g);
            }
        };

        nobleLabel.setPreferredSize(new Dimension(imageWidth, imageHeight));
        nobleLabel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180, 100), 1));

        currentPlayerLabels.add(nobleLabel);
        add(nobleLabel);
        revalidate();
        repaint();
    }

    public ArrayList<Noble> getNobles() {
        ensurePlayerStorageExists(currentPlayerIndex);
        return playerNobles.get(currentPlayerIndex);
    }

    public void switchToPlayer(int playerIndex) {
        currentPlayerIndex = playerIndex;
        refreshDisplay();
    }

    private void refreshDisplay() {
        removeAll();
        ensurePlayerStorageExists(currentPlayerIndex);
        ArrayList<JLabel> currentPlayerLabels = playerNobleLabels.get(currentPlayerIndex);

        for (JLabel label : currentPlayerLabels) {
            add(label);
        }

        revalidate();
        repaint();
    }

    public void clearNoblesForCurrentPlayer() {
        ensurePlayerStorageExists(currentPlayerIndex);
        playerNobles.get(currentPlayerIndex).clear();
        playerNobleLabels.get(currentPlayerIndex).clear();
        removeAll();
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = getWidth();
        int height = getHeight();

        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(0, 0, 0, 160),
            0, height, new Color(0, 0, 0, 140)
        );
        g2d.setPaint(gradient);

        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
            BORDER_THICKNESS, BORDER_THICKNESS,
            width - 2 * BORDER_THICKNESS,
            height - 2 * BORDER_THICKNESS,
            CORNER_RADIUS, CORNER_RADIUS
        );
        g2d.fill(roundedRect);

        g2d.setStroke(new BasicStroke(BORDER_THICKNESS));
        g2d.setColor(new Color(140, 140, 140, 100));
        g2d.draw(roundedRect);

        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawRoundRect(3, 3, width - 6, height - 6, CORNER_RADIUS - 2, CORNER_RADIUS - 2);
    }
}
