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
    
    private User currentPlayer;
    
    public UserWon(User currentPlayer) {
    
        this.currentPlayer = currentPlayer;
    }
     
    public String toString() {
            
        return currentPlayer.getUsername() + " won!";
    }     
}