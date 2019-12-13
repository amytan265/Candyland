import java.io.*;
import java.net.*;
import java.util.*;

/**
  * ISTE 121 - CANDYLAND
  * UserWon class.
  *
  * @author Regina Bass
  * @author Miki Mian
  * @author Amy Tan
  * @version 1.0, 120219
  */
class UserWon implements Serializable {
    
    // attributes
    private User currentPlayer;
    
    /** 
      * UserWon constructor.
      *
      * @param currentPlayer - User
      */
    public UserWon(User currentPlayer) {
    
        this.currentPlayer = currentPlayer;
    }
     
    /**
      * UserWon toString method.
      * @return formatted winner message
      */
    public String toString() {
            
        return currentPlayer.getUsername() + " won!";
    }     
}