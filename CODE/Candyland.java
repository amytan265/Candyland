import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
  * ISTE 121 - CANDYLAND
  * Candyland.java
  *
  * @author Regina Bass
  * @author Miki Mian
  * @author Amy Tan
  * @version 1.0, 120219
  */
  
public class Candyland {
    
    private Socket s;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    
    public static void main(String[] args) {
    
        new Candyland();
    }
    
    public Candyland() {
    
        try {
        
            s = new Socket("localhost", 16789);
            ois = new ObjectInputStream(s.getInputStream());
            oos = new ObjectOutputStream(s.getOutputStream());
            
            // writes validation line to server, flush
            oos.writeObject("numberOfUsers");
            oos.flush();
          
            // reads line from server
            Object readObject = ois.readObject();
            
            // reads "max" or "continue" (server checks client size)
            if (readObject instanceof String) {
                
                    if (readObject.equals("max")) {
                    
                    JOptionPane.showMessageDialog(null, "Max players for Candyland is 4. Please try again later \u2639");
                    System.exit(0);
                
                    } else if (readObject.equals("continue")) {
                    
                        // player's username
                        String username = JOptionPane.showInputDialog(null, "Enter Username:","Enter Username", JOptionPane.QUESTION_MESSAGE); 
                                     
                        // creates new player 
                        User player = new User(username);
                        oos.writeObject(player);
                        oos.flush();
                       
                        // creates board with player, socket, bufferedreader, printwriter
                        Board candyBoard = new Board(player, s, ois, oos);
                    }
             }
        } catch (IOException ioe) { System.out.println(ioe.getMessage()); 
        } catch (ClassNotFoundException cnfe) { System.out.println(cnfe.getMessage()); }
    }
}
