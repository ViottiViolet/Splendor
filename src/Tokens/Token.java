package Tokens;
import java.util.*;
import java.io.*;
public class Token {
    private String tokenType, illustration;
    public Token(String tokenType /*Player p*/){
        this.tokenType = tokenType;
        illustration = "src/TokenImages/token_" + tokenType + ".png";
    }
    public String getTokenType() { return tokenType; }
    public String getIllustration() { return illustration; }
}
