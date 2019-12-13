import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.ImageIcon;

public class DraggableComponent extends JPanel implements Serializable {
          
     private volatile int screenX = 0;
     private volatile int screenY = 0;
     private volatile int myX = 0;
     private volatile int myY = 0;
     
     private Image img;

     public DraggableComponent(Image img) {
     
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
        setOpaque(false);
             
        //setBackground(Color.WHITE);
        //setSize(25, 25);
          
        addMouseListener(new MouseListener() {
   
        @Override
        public void mouseClicked(MouseEvent e) { }
         
        //record cursor's position on screen when mouse is pressed 
        @Override
        public void mousePressed(MouseEvent e) {
        
           screenX = e.getXOnScreen();
           screenY = e.getYOnScreen();
   
           myX = getX();
           myY = getY();
        }
  
        @Override
        public void mouseReleased(MouseEvent e) { }
   
        @Override
        public void mouseEntered(MouseEvent e) { }
   
        @Override
        public void mouseExited(MouseEvent e) { }
   
       });
       
       //calculate distance between old mouse position and new mouse position 
       addMouseMotionListener(new MouseMotionListener() {
   
         @Override
         public void mouseDragged(MouseEvent e) {
         
           int deltaX = e.getXOnScreen() - screenX;
           int deltaY = e.getYOnScreen() - screenY;
   
           setLocation(myX + deltaX, myY + deltaY);
           System.out.println("X Position is: " + (myX + deltaX) + "  Y Position is: " + (myY + deltaY));

         }
   
         @Override
         public void mouseMoved(MouseEvent e) { }
   
       });
     }
     
     public DraggableComponent(String img) {
       this(new ImageIcon(img).getImage());
     }
   
     public void paintComponent(Graphics g) {
       g.drawImage(img, 0, 0, null);
     }
}