import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.ImageIcon;

/**
  * ISTE 121 - CANDYLAND
  * Board class.
  *
  * @author Regina Bass
  * @author Miki Mian
  * @author Amy Tan
  * @version 1.0, 120219
  */
public class Board extends JFrame {

    public Board() {
    
        this.setSize(1050, 1000);
        this.setLocationRelativeTo(null);
        this.setTitle("CANDYLAND");
        
        JMenuBar menu = new JMenuBar();
            JMenu jmFile = new JMenu("File");
            JMenuItem jmiExit = new JMenuItem("Exit");
            
            JMenu jmAbout = new JMenu("About");
            JMenuItem jmiRules = new JMenuItem("Rules");
            JMenuItem jmiKey = new JMenuItem("Key");
            
            jmFile.add(jmiExit);
            jmAbout.add(jmiRules);
            jmAbout.add(jmiKey);
            
            menu.add(jmFile);
            menu.add(jmAbout);
        this.setJMenuBar(menu);
         
        this.add(new CLBoard(), BorderLayout.WEST);
        
        /**
        JPanel jpEast = new JPanel();
            jpEast.add(new CLChat(), BorderLayout.CENTER);
            jpEast.add(new CLDraw(), BorderLayout.SOUTH);
        
        this.add(jpEast, BorderLayout.EAST); 
        */
       
        this.setResizable(false);
        this.setVisible(true);
    }
    
    class CLBoard extends JPanel {
    
        public CLBoard() {
            
            ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("Assets/board.jpg"));
            JLabel board = new JLabel(imageIcon);
            this.add(board);
            //this.setPreferredSize(new Dimension(200, 200));
        }
    }
    
    class CLChat extends JPanel {
        
        private JTextArea jtaMain;
        private JTextField jtfMsg;
        private JButton jbSend;
        
        public CLChat() {

            JPanel jpMain = new JPanel();
            jtaMain = new JTextArea(10, 30);
            jtaMain.setEditable(false);
            JScrollPane jspMain = new JScrollPane(jtaMain);
            jpMain.add(jspMain);
        
            JPanel jpBottom = new JPanel();
            jpBottom.setLayout(new FlowLayout());
            jpBottom.add(jtfMsg = new JTextField(20));
            jpBottom.add(jbSend = new JButton("Send"));
        
            this.add(jpMain, BorderLayout.CENTER);
            this.add(jpBottom, BorderLayout.SOUTH);
        }
    }
    
    class CLDraw extends JPanel {
    
        private JButton jbDraw;
        private JButton jbKey;
        
        public CLDraw() {
        
            JPanel jpCard = new JPanel();
            
            JPanel jpButtons = new JPanel();
            jpButtons.setLayout(new GridLayout(0, 1));
            jpButtons.add(jbDraw = new JButton("Draw"));
            jpButtons.add(jbKey = new JButton("Key"));
            
            this.add(jpCard, BorderLayout.CENTER);
            this.add(jpButtons, BorderLayout.EAST); 
        }
    }
    
}







