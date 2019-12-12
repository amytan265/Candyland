import java.io.*;
import java.net.*;
import javax.swing.*;
/**
  * ISTE 121 - CANDYLAND
  * User class.
  *
  * @author Regina Bass
  * @author Miki Mian
  * @author Amy Tan
  * @version 1.0, 120219
  */
public class User implements Serializable {
    
    private int id;
    private String username;
    private String userPiece;
    private int userScore;

    public User(String username) {
        
        this.username = username;
    }

    public String getUsername() {
    
        return this.username;
    }
    
    public void setScore(int userScore) {
    
        this.userScore = userScore;
    }
}
