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
            
            JMenu jmManual = new JMenu("Manual");
            JMenuItem jmKey = new JMenuItem("Key");
            JMenuItem jmiRules = new JMenuItem("Rules");
            JMenuItem jmiDisclaimer = new JMenuItem("Disclaimer");
            JMenuItem jmiAbout = new JMenuItem("About");
            
            jmFile.add(jmiExit);
            jmManual.add(jmiRules);
            jmManual.add(jmiDisclaimer);
            jmManual.add(jmiAbout);
            
            //Action Listeners for Menu Items
            MyAdapter ma = new MyAdapter();
            jmKey.addActionListener(ma);
            jmiExit.addActionListener(ma);
            jmiRules.addActionListener(ma);
            jmiDisclaimer.addActionListener(ma);
            jmiAbout.addActionListener(ma);
            jmiExit.addActionListener(ma);
            
            menu.add(jmFile);
            menu.add(jmManual);
            menu.add(jmKey);
        this.setJMenuBar(menu);
         
        this.add(new CLBoard(), BorderLayout.WEST);
        this.add(new CLChat(), BorderLayout.EAST);
        
        /**
        JPanel jpEast = new JPanel();
            jpEast.add(new CLChat(), BorderLayout.CENTER);
            jpEast.add(new CLDraw(), BorderLayout.SOUTH);
        
        this.add(jpEast, BorderLayout.EAST); 
        */
        
        this.setResizable(true);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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

            this.setLayout(new BorderLayout());
            JPanel jpMain = new JPanel();
            jpMain.setLayout(new BorderLayout());
            jtaMain = new JTextArea(10, 30);
            jtaMain.setEditable(false);
            //JScrollPane jspMain = new JScrollPane(jtaMain);
            //jpMain.add(jspMain);
        
            JPanel jpBottom = new JPanel();
            jpBottom.setLayout(new FlowLayout());
            jtfMsg = new JTextField(20);
            jbSend = new JButton("Send");
            MyAdapter ma = new MyAdapter();
            jbSend.addActionListener(ma);
            jpBottom.add(jtfMsg);
            jpBottom.add(jbSend);
        
            jpMain.add(jtaMain);
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
            MyAdapter ma = new MyAdapter();
            jbDraw.addActionListener(ma);
            
            this.add(jpCard, BorderLayout.CENTER);
            this.add(jpButtons, BorderLayout.EAST); 
        }
    }
    
    
    class MyAdapter implements ActionListener {     // You can only have one public class in a file so you don't write public 

         
         public void actionPerformed(ActionEvent ae){
            String choice = ae.getActionCommand();
            
            if( choice.equals("About") ){
//             JFrame about = new JFrame("About");
//             about.setSize(400, 400);
//             about.setLocationRelativeTo(null);
               try {
                  File htmlFile = new File("Assets/about.html");
                  Desktop.getDesktop().open(htmlFile); 
               }
               catch(Exception e) {
                  String msg = "Error occured opening File";
						String title = "Error";
						JOptionPane.showMessageDialog(null, msg, title,
							   JOptionPane.ERROR_MESSAGE);
               }

//                JTextArea textareaAbout = new JTextArea(htmlFile);
//                textareaAbout.setEnabled(false);
//                about.add(textareaAbout);
//             about.setVisible(true); 
            }
            else if(choice.equals("Rules")){
               try {
                  File htmlFile = new File("Assets/rules.html");
                  Desktop.getDesktop().open(htmlFile); 
               }
               catch(Exception e) {
                  String msg = "Error occured opening File";
						String title = "Error";
						JOptionPane.showMessageDialog(null, msg, title,
							   JOptionPane.ERROR_MESSAGE);
               }

            }
            else if(choice.equals("Key")){
               try {
                  File htmlFile = new File("Assets/key.html");
                  Desktop.getDesktop().open(htmlFile); 
               }
               catch(Exception e) {
                  String msg = "Error occured opening File";
						String title = "Error";
						JOptionPane.showMessageDialog(null, msg, title,
							   JOptionPane.ERROR_MESSAGE);
               }

            }
            else if(choice.equals("Disclaimer")){
               try {
                  File htmlFile = new File("Assets/disclaimer.html");
                  Desktop.getDesktop().open(htmlFile); 
               }
               catch(Exception e) {
                  String msg = "Error occured opening File";
						String title = "Error";
						JOptionPane.showMessageDialog(null, msg, title,
							   JOptionPane.ERROR_MESSAGE);
               }

            }
            else if(choice.equals("Draw")){
               System.out.println("Draw was clicked");
            }
            else if(choice.equals("Send")){
               System.out.println("Send was clicked");
            }
            else if(choice.equals("Exit")){
               System.exit(0);
            }
             
            
         }
     } // end class MyAdapter
    
}







