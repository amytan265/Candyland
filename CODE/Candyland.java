import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
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
    
    public static void main(String[] args) {
        String username = new String();
        username = JOptionPane.showInputDialog(null, "Enter Username:",
                                               "Enter Username", JOptionPane.QUESTION_MESSAGE);
        
        new Candyland();
    }
    
    public Candyland() {
    
        Board candyBoard = new Board();
    }
}
