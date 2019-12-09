import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
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

    private User currentPlayer;
    public int score = 0;

    public Board(User currentPlayer) {
    
        this.currentPlayer = currentPlayer;
    
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
        // this.add(new CLChat(), BorderLayout.EAST);
        
        // creates east panel of GUI
        // jpControls - full east panel of Chat and jpMini
        // jpMini - active users, draw 
        JPanel jpControls = new JPanel();
            jpControls.setLayout(new BorderLayout());
            jpControls.add(new CLChat(), BorderLayout.NORTH);
                JPanel jpMini = new JPanel();
                jpMini.setLayout(new BorderLayout());
                jpMini.add(new CLActive(), BorderLayout.WEST);
                jpMini.add(new CLDraw(), BorderLayout.EAST);
            jpControls.add(jpMini, BorderLayout.SOUTH);
        this.add(jpControls, BorderLayout.EAST);
        
        /**
        JPanel jpEast = new JPanel();
            jpEast.add(new CLChat(), BorderLayout.CENTER);
            jpEast.add(new CLDraw(), BorderLayout.SOUTH);
        
        this.add(jpEast, BorderLayout.EAST); 
        */
        
        this.setResizable(false);
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
    
        BufferedReader in = null;
        PrintWriter out = null;
        Socket s;
        
        public CLChat() {
        
            try {
        
                // s = new Socket("129.21.73.119", 16789);
                s = new Socket("localhost", 16789);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            
            } catch (IOException ioe) {} 
            
            this.ChatGUI();
            new ReadMessages().start();
         }
         
         public void ChatGUI() {

            this.setLayout(new BorderLayout());
            JPanel jpMain = new JPanel();
            jpMain.setLayout(new BorderLayout());
            jtaMain = new JTextArea(10, 30);
            jtaMain.setEditable(false);
            JScrollPane jspMain = new JScrollPane(jtaMain);
            jpMain.add(jspMain);
        
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
            
         
         jbSend.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            out.println(currentPlayer.getUsername() + ": " + jtfMsg.getText());
            System.out.println(currentPlayer.getUsername() + ": " + jtfMsg.getText());
            out.flush();
            jtfMsg.setText("");
            
         }
       });
       
       }
       
       class ReadMessages extends Thread {
    
          String line;
        
          // run method
          public void run() {
            try {
                while(true) {
                    
                    // reads incoming messages, appends to JTextArea
                    line = in.readLine();
                    jtaMain.append(line + "\n");     
                }
            } catch (IOException ioe) {}
          }
        }
    }
    
    class CLActive extends JPanel {
    
    
    
    }
    
    class CLDraw extends JPanel {
    
        private JButton jbDraw;
        
        public CLDraw() {
        
            JPanel jpCard = new JPanel();
            
            JPanel jpButtons = new JPanel();
            jpButtons.setLayout(new GridLayout(2, 1, 0, 10));   //rows, columns, hgap vgap
            jpButtons.add(jbDraw = new JButton("Draw"));
            MyAdapter ma = new MyAdapter();
            jbDraw.addActionListener(ma);
            
            this.add(jpCard);
            this.add(jpButtons); 
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
               Card card = new Card();
               String currColor = card.getCurrentColor();      // calls method which gets current color
               String drawnCard = card.getNextColor();
               switch(currColor){        // Parent switch case
                  case "purple" :
                     switch(drawnCard){   //nested switch case
                        case "purple":
                           score += 5;
                           break;
                        case "pink":
                           score += 1;
                           break;
                        case "blue":
                           score += 2;
                           break;
                        case "yellow":
                           score += 3;
                           break;
                        case "orange":
                           score += 4;
                           break;
                      }
                  case "pink" :
                     switch(drawnCard){   //nested switch case
                        case "purple":
                           score += 4;
                           break;
                        case "pink":
                           score += 5;
                           break;
                        case "blue":
                           score += 1;
                           break;
                        case "yellow":
                           score += 2;
                           break;
                        case "orange":
                           score += 3;
                           break;
                      }
                  case "blue" :
                     switch(drawnCard){   //nested switch case
                        case "purple":
                           score += 3;
                           break;
                        case "pink":
                           score += 4;
                           break;
                        case "blue":
                           score += 5;
                           break;
                        case "yellow":
                           score += 1;
                           break;
                        case "orange":
                           score += 2;
                           break;
                      }
                  case "yellow" :
                     switch(drawnCard){   //nested switch case
                        case "purple":
                           score += 2;
                           break;
                        case "pink":
                           score += 3;
                           break;
                        case "blue":
                           score += 4;
                           break;
                        case "yellow":
                           score += 5;
                           break;
                        case "orange":
                           score += 1;
                           break;
                      }
                  case "orange" :
                     switch(drawnCard){   //nested switch case
                        case "purple":
                           score += 1;
                           break;
                        case "pink":
                           score += 2;
                           break;
                        case "blue":
                           score += 3;
                           break;
                        case "yellow":
                           score += 4;
                           break;
                        case "orange":
                           score += 5;
                           break;
                      }
               
               
               
               }
            }
            else if(choice.equals("Exit")){
               System.exit(0);
            }
             
            
         }
     } // end class MyAdapter
    
}







