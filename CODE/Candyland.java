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
    private BufferedReader in = null;
    private PrintWriter out = null;
    
    public static void main(String[] args) {
    
        new Candyland();
    }
    
    public Candyland() {
    
        try {
        
            s = new Socket("localhost", 16789);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            
            // writes validation line to server, flush
            out.println("numberOfUsers");
            out.flush();
          
            // reads line from server
            String line = in.readLine();
            
            // reads "max" or "continue" (server checks client size)
            if (line.equals("max")) {
                
                JOptionPane.showMessageDialog(null, "Max players for Candyland is 4. Please try again later \u2639");
                System.exit(0);
            
            } else if (line.equals("continue")) {
                
                // player's username
                String username = JOptionPane.showInputDialog(null, "Enter Username:","Enter Username", JOptionPane.QUESTION_MESSAGE); 
                
                // creates new player 
                User player = new User(username);
                
                // creates board with player, socket, bufferedreader, printwriter
                Board candyBoard = new Board(player, s, in, out);
            }
              
        } catch (IOException ioe) { System.out.println(ioe.getMessage()); }
    }
}
