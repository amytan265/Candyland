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
    
    // attributes
    private int id;
    private String username;
    private String userPiece;
    private int userScore;

    /** 
      * User constructor.
      *
      * @param username - String
      */ 
    public User(String username) {
        
        this.username = username;
    }

    /**
      * getUsername accessor.
      *
      * @return username
      */
    public String getUsername() {
    
        return this.username;
    }
    
    /**
      * setScore mutator.
      */
    public void setScore(int userScore) {
    
        this.userScore = userScore;
    }
}

