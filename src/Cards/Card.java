package Cards;

public class Card {
    @SuppressWarnings("unused")
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

    // get each value of the card and create a card object
    public Card(int level, int prestige, String illustration, int diamondBonus,
            int sapphireBonus, int emeraldBonus, int rubyBonus, int onyxBonus,
            int diamondCost, int sapphireCost, int emeraldCost, int rubyCost, int onyxCost) {
        this.level = level;
        this.prestige = prestige;
        this.illustration = illustration;
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

    // used to get the image of the card that we want to be drawn
    public String getIllustration() {
        return illustration;
    }

    // get the prestige of a card
    public int getPrestige() {
        return prestige;
    }

    // we need to update this based on the gameplay that we will have
    public String getBonus() {
        if (diamondBonus > 0)
            return "Diamond";
        if (sapphireBonus > 0)
            return "Sapphire";
        if (emeraldBonus > 0)
            return "Emerald";
        if (rubyBonus > 0)
            return "Ruby";
        if (onyxBonus > 0)
            return "Onyx";
        return "None";
    }

    // template: update based on the way we will get the costs of the cards
    public String getCosts() {
        return "Diamond: " + diamondCost + ", Sapphire: " + sapphireCost + ", Emerald: " + emeraldCost + ", Ruby: "
                + rubyCost + ", Onyx: " + onyxCost;
    }
}
