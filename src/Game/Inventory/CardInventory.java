public class CardInventory extends JPanel {
    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 120;
    private static final int CORNER_RADIUS = 15;
    private static final float BORDER_THICKNESS = 2.0f;
    private static final int CARDS_PER_ROW = 4;
    private static final int HORIZONTAL_GAP = 15;
    private static final int VERTICAL_GAP = 10;
    
    // Store cards for each player
    private final Map<Integer, ArrayList<Card>> playerCards;
    private final Map<Integer, ArrayList<JLabel>> playerCardLabels;
    private int currentPlayerIndex;
    
    public CardInventory() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
      
        playerCards = new HashMap<>();
        playerCardLabels = new HashMap<>();
        currentPlayerIndex = 0;

        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(),
            "Purchased Cards",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Gothic", Font.BOLD, 16),
            new Color(220, 220, 220)
        );
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            border
        ));

        JPanel cardsPanel = new JPanel(new GridBagLayout());
        cardsPanel.setOpaque(false);
    }

    private void ensurePlayerStorageExists(int playerIndex) {
        playerCards.computeIfAbsent(playerIndex, k -> new ArrayList<>());
        playerCardLabels.computeIfAbsent(playerIndex, k -> new ArrayList<>());
    }

    public void addCard(Card card) {
        ensurePlayerStorageExists(currentPlayerIndex);
        ArrayList<Card> currentPlayerCards = playerCards.get(currentPlayerIndex);
        ArrayList<JLabel> currentPlayerLabels = playerCardLabels.get(currentPlayerIndex);

        currentPlayerCards.add(card);
        
        ImageIcon cardImage = new ImageIcon(new ImageIcon(card.getIllustration())
                .getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH));
        
        JLabel cardLabel = new JLabel(cardImage) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
                
                super.paintComponent(g);
            }
        };
        
        cardLabel.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        cardLabel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180, 100), 1));
        cardLabel.setToolTipText(String.format("Prestige Points: %d", card.getPrestige()));
        
        currentPlayerLabels.add(cardLabel);
                updateGridLayout();
    }

    private void updateGridLayout() {
        JPanel cardsPanel = (JPanel)(getComponent(0)).getViewport().getView();
        cardsPanel.removeAll();
        
        ArrayList<JLabel> currentPlayerLabels = playerCardLabels.get(currentPlayerIndex);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(VERTICAL_GAP, HORIZONTAL_GAP, VERTICAL_GAP, HORIZONTAL_GAP);
        gbc.anchor = GridBagConstraints.CENTER;
        
        for (int i = 0; i < currentPlayerLabels.size(); i++) {
            JLabel label = currentPlayerLabels.get(i);
            
            gbc.gridx = i % CARDS_PER_ROW;
            gbc.gridy = i / CARDS_PER_ROW;
            
            // Last card in a row
            if (gbc.gridx == CARDS_PER_ROW - 1) {
                gbc.gridwidth = GridBagConstraints.REMAINDER;
            } else {
                gbc.gridwidth = 1;
            }
            
            cardsPanel.add(label, gbc);
        }
        
        // Add filler component to prevent cards from stretching
        gbc.gridx = 0;
        gbc.gridy = (currentPlayerLabels.size() / CARDS_PER_ROW) + 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        cardsPanel.add(Box.createGlue(), gbc);
        
        revalidate();
        repaint();
    }

    public ArrayList<Card> getCards() {
        ensurePlayerStorageExists(currentPlayerIndex);
        return playerCards.get(currentPlayerIndex);
    }

    public void switchToPlayer(int playerIndex) {
        currentPlayerIndex = playerIndex;
        refreshDisplay();
    }

    private void refreshDisplay() {
        ensurePlayerStorageExists(currentPlayerIndex);
        updateGridLayout();
    }

    public void clearCards() {
        ensurePlayerStorageExists(currentPlayerIndex);
        playerCards.get(currentPlayerIndex).clear();
        playerCardLabels.get(currentPlayerIndex).clear();
        updateGridLayout();
    }

    public int getTotalPrestige() {
        ensurePlayerStorageExists(currentPlayerIndex);
        return playerCards.get(currentPlayerIndex).stream()
                .mapToInt(Card::getPrestige)
                .sum();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = getWidth();
        int height = getHeight();

        // Create gradient background
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
