import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.Random;
import javax.swing.ImageIcon;

/**
  * ISTE 121 - CANDYLAND
  * Card class.
  *
  * @author Regina Bass
  * @author Miki Mian
  * @author Amy Tan
  * @version 1.0, 120219
  */
public class Card extends JLabel {
    
    // attributes
    private Vector<String> colors = new Vector<String>();
    
    private String currentColor = null;
    private String randomizeColor = null;

    /** Card constructor */
    public Card() {
      //adds colors into pool of cards
        colors.add("purple");
        colors.add("pink");
        colors.add("blue");
        colors.add("yellow");
        colors.add("orange");
        
       //2nd set of color cards are added so that there is a higher liklihood to draw these cards
        colors.add("purple");
        colors.add("pink");
        colors.add("blue");
        colors.add("yellow");
        colors.add("orange");
        
        //special cards and candy cards are added
        colors.add("blackStar");
        colors.add("redStar");
        colors.add("goldStar");
        colors.add("candyCane");
        colors.add("candyCorn");
        colors.add("lollipop");
        colors.add("chocolate");
    }

    /** 
      * getNextColor method.
      *
      * @return currentColor - String
      */
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

        } else if (randomizeColor.equals("lollipop")) {
            
            currentColor = randomizeColor;
        
        } else if (randomizeColor.equals("chocolate")) {
            
            currentColor = randomizeColor;
        
        } else if (randomizeColor.equals("candyCane")) {
            
            currentColor = randomizeColor;
        
        } else if (randomizeColor.equals("candyCorn")) {
            
            currentColor = randomizeColor;
        
        } else if (randomizeColor.equals("redStar")) {
            
            currentColor = randomizeColor;
        
        } else if (randomizeColor.equals("goldStar")) {
            
            currentColor = randomizeColor;
        
        } else if (randomizeColor.equals("blackStar")) {
            
            currentColor = randomizeColor;
        
        }
        
        return currentColor;
    }
    
    /**
      * getCurrentColor accessor.
      *
      * @return currentColor - String
      */
    public String getCurrentColor() {
    
        return currentColor;
    }
    
    /**
      * getRandColor accessor.
      *
      * @return randomized color for card.
      */
    public String getRandColor() {
        
        Random rand = new Random();
        
        return colors.get(rand.nextInt(colors.size()));
    
    }
}
