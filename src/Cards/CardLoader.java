package Cards;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class CardLoader {

    private Stack<Card> level1Cards = new Stack<>();
    private Stack<Card> level2Cards = new Stack<>();
    private Stack<Card> level3Cards = new Stack<>();

    public CardLoader(String filename) {
        loadCards(filename);
    }

    // loading the info of the cards from the text-file
    private void loadCards(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                int level = Integer.parseInt(parts[0]);
                int prestige = Integer.parseInt(parts[1]);
                String illustration = parts[2];
                int diamondBonus = Integer.parseInt(parts[3]);
                int sapphireBonus = Integer.parseInt(parts[4]);
                int emeraldBonus = Integer.parseInt(parts[5]);
                int rubyBonus = Integer.parseInt(parts[6]);
                int onyxBonus = Integer.parseInt(parts[7]);
                int diamondCost = Integer.parseInt(parts[8]);
                int sapphireCost = Integer.parseInt(parts[9]);
                int emeraldCost = Integer.parseInt(parts[10]);
                int rubyCost = Integer.parseInt(parts[11]);
                int onyxCost = Integer.parseInt(parts[12]);

                Card card = new Card(level, prestige, illustration, diamondBonus, sapphireBonus,
                        emeraldBonus, rubyBonus, onyxBonus, diamondCost, sapphireCost,
                        emeraldCost, rubyCost, onyxCost);

                switch (level) {
                    case 1 -> level1Cards.push(card);
                    case 2 -> level2Cards.push(card);
                    case 3 -> level3Cards.push(card);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // this will be used to draw the top card of the deck for respective levels
    public Card drawCard(int level) {
        return switch (level) {
            case 1 -> level1Cards.isEmpty() ? null : level1Cards.pop();
            case 2 -> level2Cards.isEmpty() ? null : level2Cards.pop();
            case 3 -> level3Cards.isEmpty() ? null : level3Cards.pop();
            default -> null;
        };
    }

    // stack for level 1
    public Stack<Card> getLevel1Cards() {
        return level1Cards;
    }

    // stack for level 2
    public Stack<Card> getLevel2Cards() {
        return level2Cards;
    }

    // stack for level 3
    public Stack<Card> getLevel3Cards() {
        return level3Cards;
    }
}
