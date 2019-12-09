import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.Random;
import javax.swing.ImageIcon;

public class Card extends JLabel {

    Vector<String> colors = new Vector<String>();

    private String currentColor = null;
    private String randomizeColor = null;

    public Card() {
    
        colors.add("purple");
        colors.add("pink");
        colors.add("blue");
        colors.add("yellow");
        colors.add("orange");
    }

    public String getCurrentColor() {
    
        return currentColor;
    }

    public String getNextColor() {
        
        randomizeColor = this.getRandColor();
        
        if (randomizeColor.equals("purple")) {
        
            setIcon(new ImageIcon("Assets/purple.png"));
            currentColor = randomizeColor;
            
        } else if (randomizeColor.equals("pink")) {
        
            setIcon(new ImageIcon("Assets/pink.png"));
            currentColor = randomizeColor;
      
        } else if (randomizeColor.equals("blue")) {
            
            setIcon(new ImageIcon("Assets/blue.png"));
            currentColor = randomizeColor;
            
        } else if (randomizeColor.equals("yellow")) {
            
            setIcon(new ImageIcon("Assets/yellow.png"));
            currentColor = randomizeColor;
        
        } else if (randomizeColor.equals("orange")) {
            
            setIcon(new ImageIcon("Assets/orange.png"));
            currentColor = randomizeColor;
        }
        
        return currentColor;
    }
    
    public String getRandColor() {
        
        Random rand = new Random();
        
        return colors.get(rand.nextInt(colors.size()));
    
    }
}
