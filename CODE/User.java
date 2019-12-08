
/**
  * ISTE 121 - CANDYLAND
  * User class.
  *
  * @author Regina Bass
  * @author Miki Mian
  * @author Amy Tan
  * @version 1.0, 120219
  */
public class User {
    
    private int id;
    private String username;
    private String userPiece;

    public User(String username) {
        
        this.username = username;
    }

    public String getUsername() {
    
        return this.username;
    }
}