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

    private void loadCards(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");

                // Check if the line has the expected 13 parts to avoid errors
                if (parts.length < 13) {
                    System.out.println("Skipping line due to incorrect format: " + line);
                    continue;
                }

                int level = Integer.parseInt(parts[0].trim());
                int prestige = Integer.parseInt(parts[1].trim());
                String illustration = parts[2].trim();
                int diamondBonus = Integer.parseInt(parts[3].trim());
                int sapphireBonus = Integer.parseInt(parts[4].trim());
                int emeraldBonus = Integer.parseInt(parts[5].trim());
                int rubyBonus = Integer.parseInt(parts[6].trim());
                int onyxBonus = Integer.parseInt(parts[7].trim());
                int diamondCost = Integer.parseInt(parts[8].trim());
                int sapphireCost = Integer.parseInt(parts[9].trim());
                int emeraldCost = Integer.parseInt(parts[10].trim());
                int rubyCost = Integer.parseInt(parts[11].trim());
                int onyxCost = Integer.parseInt(parts[12].trim());

                Card card = new Card(level, prestige, illustration, diamondBonus, sapphireBonus, emeraldBonus,
                        rubyBonus, onyxBonus, diamondCost, sapphireCost, emeraldCost, rubyCost, onyxCost);
                switch (level) {
                    case 1 -> level1Cards.push(card);
                    case 2 -> level2Cards.push(card);
                    case 3 -> level3Cards.push(card);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing a number in file: " + e.getMessage());
        }
    }

    public Card drawCard(int level) {
        return switch (level) {
            case 1 -> level1Cards.isEmpty() ? null : level1Cards.pop();
            case 2 -> level2Cards.isEmpty() ? null : level2Cards.pop();
            case 3 -> level3Cards.isEmpty() ? null : level3Cards.pop();
            default -> null;
        };
    }

    public Stack<Card> getLevel1Cards() {
        return level1Cards;
    }

    public Stack<Card> getLevel2Cards() {
        return level2Cards;
    }

    public Stack<Card> getLevel3Cards() {
        return level3Cards;
    }
}
