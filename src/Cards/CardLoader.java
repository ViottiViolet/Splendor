package Cards;

import java.awt.image.BufferedImage;

public class CardLoader {
    private int level;
    private int prestige;
    private BufferedImage image;
    private int[] bonus = new int[5];
    private int[] cost = new int[5];

    public CardLoader(int level, int prestige, BufferedImage image, int[] bonus, int[] cost) {
        this.level = level;
        this.prestige = prestige;
        this.image = image;
        this.bonus = bonus;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
