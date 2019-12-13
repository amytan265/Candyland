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
    
    // attributes
    private Socket s;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    
    /** main method. */
    public static void main(String[] args) {
    
        new Candyland();
    }
    
    /** Candyland constructor. */
    public Candyland() {
        
        // receives IP address to join onto Server
        String ipAddress = JOptionPane.showInputDialog(null, "Enter IP Address:","Enter IP Address", JOptionPane.QUESTION_MESSAGE); 
    
        try {
            
            // instantiates socket, objectinputstream, objectoutputstream
            s = new Socket(ipAddress, 16789);
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
                    
                    // displays maximum player size message, exits program
                    JOptionPane.showMessageDialog(null, "Max players for Candyland is 4. Please try again later.");
                    System.exit(0);
                
                    } else if (readObject.equals("continue")) {
                    
                        // acquires username from player
                        String username = JOptionPane.showInputDialog(null, "Enter Username:","Enter Username", JOptionPane.QUESTION_MESSAGE); 
                                     
                        // creates new player, sends to server 
                        User player = new User(username);
                        oos.writeObject(player);
                        oos.flush();
                       
                        // creates board with player, socket, objectinputstream, objectoutputstream
                        Board candyBoard = new Board(player, s, ois, oos);
                    }
             }
        } catch (IOException ioe) { System.out.println(ioe.getMessage()); 
        } catch (ClassNotFoundException cnfe) { System.out.println(cnfe.getMessage()); }
    }
}
