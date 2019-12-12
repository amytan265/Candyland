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

    public String getNextColor() {
        
        randomizeColor = this.getRandColor();
        
        if (randomizeColor.equals("purple")) {

            currentColor = randomizeColor;
            
        } else if (randomizeColor.equals("pink")) {

            currentColor = randomizeColor;
      
        } else if (randomizeColor.equals("blue")) {

            currentColor = randomizeColor;
            
        } else if (randomizeColor.equals("yellow")) {
            
            currentColor = randomizeColor;
        
        } else if (randomizeColor.equals("orange")) {
            
            currentColor = randomizeColor;
            // randomizeColor = currentColor;
        }
        
        return currentColor;
    }
    
    public String getCurrentColor() {
    
        return currentColor;
    }
    
    public String getRandColor() {
        
        Random rand = new Random();
        
        return colors.get(rand.nextInt(colors.size()));
    
    }
}
