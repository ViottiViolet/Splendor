package Nobles;

import Game.Inventory.TokenInventory;
import Game.Main.SplendorGameScreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class NobleLoader {
    private String file;
    private ArrayList<Noble> nobleList;
    public static final int NOBLE_WIDTH = 165;
    public static final int NOBLE_HEIGHT = 165;
    private static final float HOVER_SCALE = 1.08f;

    public NobleLoader(String file) {
        this.file = file;
        this.nobleList = new ArrayList<>();
    }

    public void loadNobles() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                String nobleName = parts[0];
                int diamondCost = Integer.parseInt(parts[1].trim());
                int sapphireCost = Integer.parseInt(parts[2].trim());
                int emeraldCost = Integer.parseInt(parts[3].trim());
                int rubyCost = Integer.parseInt(parts[4].trim());
                int onyxCost = Integer.parseInt(parts[5].trim());
                String imageName = nobleName;

                Noble noble = new Noble(nobleName, diamondCost, sapphireCost, emeraldCost, rubyCost, onyxCost, imageName);
                nobleList.add(noble);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JPanel createNobles() {
        Collections.shuffle(nobleList);

        JPanel grid = new JPanel(new GridLayout(3, 2, 10, 15));
        grid.setOpaque(false);

        Stack<Noble> nobleStack = new Stack<>();
        int noblesCount = Math.min(5, nobleList.size());
        for (int i = 0; i < noblesCount; i++) {
            nobleStack.push(nobleList.get(i));
        }

        while (!nobleStack.isEmpty()) {
            addNobleToGrid(grid, nobleStack);
        }

        while (grid.getComponentCount() < 5) {
            JLabel emptyLabel = new JLabel("Empty Slot", SwingConstants.CENTER);
            emptyLabel.setPreferredSize(new Dimension(NOBLE_WIDTH, NOBLE_HEIGHT));
            grid.add(emptyLabel);
        }

        return grid;
    }

    private void addNobleToGrid(JPanel grid, Stack<Noble> nobleStack) {
        if (!nobleStack.isEmpty()) {
            Noble noble = nobleStack.pop();
            try {
                BufferedImage nobleImage = ImageIO.read(new File(noble.getImage())); // Load the image
                ImageIcon nobleIcon = new ImageIcon(nobleImage.getScaledInstance(NOBLE_WIDTH, NOBLE_HEIGHT, Image.SCALE_SMOOTH));
                JLabel nobleLabel = createClickableLabel(nobleIcon, noble, nobleStack, grid);
                grid.add(nobleLabel);
            } catch (IOException e) {
                e.printStackTrace();
                JLabel errorLabel = new JLabel("Image Load Error", SwingConstants.CENTER);
                grid.add(errorLabel);
            }
        } else {
            JLabel emptyLabel = new JLabel("Empty Slot", SwingConstants.CENTER);
            grid.add(emptyLabel);
        }
    }

    private JLabel createClickableLabel(ImageIcon icon, Noble noble, Stack<Noble> nobleStack, JPanel grid) {
        JLabel label = new JLabel(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                if (getClientProperty("hovered") != null && (Boolean) getClientProperty("hovered")) {
                    double scale = HOVER_SCALE;
                    int w = getWidth();
                    int h = getHeight();
                    int x = (int) (w - (w * scale)) / 2;
                    int y = (int) (h - (h * scale)) / 2;

                    g2d.translate(x, y);
                    g2d.scale(scale, scale);
                }
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };

        label.setPreferredSize(new Dimension(NOBLE_WIDTH, NOBLE_HEIGHT));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                label.putClientProperty("hovered", false);
                label.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Container parent = grid;
                while (!(parent instanceof SplendorGameScreen) && parent != null) {
                    parent = parent.getParent();
                }

                if (parent instanceof SplendorGameScreen) {
                    SplendorGameScreen gameScreen = (SplendorGameScreen) parent;

                    // Check if this is the current player's turn
                    int currentPlayerIndex = gameScreen.getCycleInventory().getCurrentPlayerIndex();
                    if (currentPlayerIndex != gameScreen.getPlayerTurn()) {
                        JOptionPane.showMessageDialog(grid,
                                "It's not your turn to interact with the nobles.",
                                "Not Your Turn",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    nobleClicked(label, noble, nobleStack, grid);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label.putClientProperty("hovered", true);
                label.repaint();
            }
        });

        return label;
    }

    private void nobleClicked(JLabel clickedLabel, Noble noble, Stack<Noble> nobleStack, JPanel grid) {
        String[] options = { "Take", "Cancel" };
        int choice = JOptionPane.showOptionDialog(grid, "What would you like to do?", "Action",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            takeNoble(clickedLabel, noble, nobleStack, grid);
        }
    }

    private void takeNoble(JLabel clickedLabel, Noble noble, Stack<Noble> nobleStack, JPanel grid) {
        Container parent = grid;
        while (!(parent instanceof SplendorGameScreen) && parent != null) {
            parent = parent.getParent();
        }

        if (parent instanceof SplendorGameScreen) {
            SplendorGameScreen gameScreen = (SplendorGameScreen) parent;
            TokenInventory tokenInventory = gameScreen.getPlayerInventory();

            int[] nobleCosts = noble.getCost();
            int[] playerBonuses = {
                    tokenInventory.getBonusCount("white"),
                    tokenInventory.getBonusCount("blue"),
                    tokenInventory.getBonusCount("green"),
                    tokenInventory.getBonusCount("red"),
                    tokenInventory.getBonusCount("black")
            };

            boolean canTakeNoble = true;
            for (int i = 0; i < nobleCosts.length; i++) {
                if (nobleCosts[i] > playerBonuses[i]) {
                    canTakeNoble = false;
                    break;
                }
            }

            if (!canTakeNoble) {
                JOptionPane.showMessageDialog(grid,
                        "You do not have enough cards to take this noble.",
                        "Insufficient Resources",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int currentPlayerIndex = gameScreen.getCycleInventory().getCurrentPlayerIndex();
            gameScreen.updatePlayerScore(currentPlayerIndex, noble.getPrestige());

            gameScreen.getNobleInventory().addNoble(noble);

            grid.remove(clickedLabel);

            if (!nobleStack.isEmpty()) {
                addNobleToGrid(grid, nobleStack);
            } else {
                JLabel emptyLabel = new JLabel("Empty Slot", SwingConstants.CENTER);
                grid.add(emptyLabel);
            }

            grid.revalidate();
            grid.repaint();

            gameScreen.nextPlayerTurn();
        }
    }

    public ArrayList<Noble> getNobleList() {
        return nobleList;
    }
}
