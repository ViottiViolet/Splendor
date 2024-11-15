package Tokens;
import java.util.*;
import java.io.*;
public class Token {
    private String tokenType, illustration;
    private int numTokens;
    public Token(String tokenType /*Player p*/, String numTokens){
        this.tokenType = tokenType;
        illustration = "src/TokenImages/token_" + tokenType + ".png";
    }

    public void increase(int amount){numTokens+=amount;}
    public void decrease(int amount){if(numTokens > amount)numTokens-=amount;}
    public String getTokenType() { return tokenType; }
    public String getIllustration() { return illustration; }
    public int getNumTokens() { return numTokens; }
}
