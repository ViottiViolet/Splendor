package Cards;

public class Card {
    private final int level;
    private final int prestige;
    private final String illustration;
    private final int diamondBonus;
    private final int sapphireBonus;
    private final int emeraldBonus;
    private final int rubyBonus;
    private final int onyxBonus;
    private final int diamondCost;
    private final int sapphireCost;
    private final int emeraldCost;
    private final int rubyCost;
    private final int onyxCost;

    public Card(int level, int prestige, String illustration, int diamondBonus, int sapphireBonus, int emeraldBonus,
            int rubyBonus, int onyxBonus, int diamondCost, int sapphireCost, int emeraldCost,
            int rubyCost, int onyxCost) {
        this.level = level;
        this.prestige = prestige;
        this.illustration = "src/Images/Level" + level + "/" + illustration + ".png";
        this.diamondBonus = diamondBonus;
        this.sapphireBonus = sapphireBonus;
        this.emeraldBonus = emeraldBonus;
        this.rubyBonus = rubyBonus;
        this.onyxBonus = onyxBonus;
        this.diamondCost = diamondCost;
        this.sapphireCost = sapphireCost;
        this.emeraldCost = emeraldCost;
        this.rubyCost = rubyCost;
        this.onyxCost = onyxCost;
    }

    public int getPrestige() {
        return prestige;
    }

    public int[] getBonus() {
        int[] bonuses = { diamondBonus, sapphireBonus, emeraldBonus, rubyBonus, onyxBonus };
        return bonuses;
    }

    public int[] getCosts() {
        ;
        int[] costs = { diamondCost, sapphireCost, emeraldCost, rubyCost, onyxCost };
        return costs;
    }

    // Method to get the illustration (image path) of the card
    public String getIllustration() {
        return illustration;
    }
}
