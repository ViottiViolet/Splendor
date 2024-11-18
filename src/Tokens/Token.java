package Tokens;

public class Token {
    private final TokenLoader.TokenType tokenType;
    private final String illustration;
    private int numTokens;

    public Token(TokenLoader.TokenType tokenType, int initialTokens) {
        this.tokenType = tokenType;
        this.numTokens = initialTokens;
        this.illustration = "src/TokenImages/token_" + tokenType.getFilename() + ".png";
    }

    public void increase(int amount) {
        if (amount > 0) {
            numTokens += amount;
        }
    }

    public void decrease(int amount) {
        if (amount > 0 && numTokens >= amount) {
            numTokens -= amount;
        }
    }

    public TokenLoader.TokenType getTokenType() {
        return tokenType;
    }

    public String getIllustration() {
        return illustration;
    }

    public int getNumTokens() {
        return numTokens;
    }
}
