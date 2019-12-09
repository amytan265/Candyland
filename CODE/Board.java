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
            jpControls.add(new CLChat(), BorderLayout.CENTER);
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
            
            DraggableComponent one = new DraggableComponent(new ImageIcon("Assets/redgin.png").getImage());
            DraggableComponent two = new DraggableComponent(new ImageIcon("Assets/greengin.png").getImage());
            DraggableComponent three = new DraggableComponent(new ImageIcon("Assets/bluegin.png").getImage());
            DraggableComponent four = new DraggableComponent(new ImageIcon("Assets/yellowgin.png").getImage());
                                  
            board.add(one);
            board.add(two);
            board.add(three);
            board.add(four);
        }
    }
    
    class DraggableComponent extends JPanel {
          
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
     

   }//end of DraggableComponent class
    
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
           catch(NullPointerException npe){
               System.out.println("Caught Exception: " + npe.getMessage());
         }
           catch(Exception e){
               System.out.println("Caught Exception.");
         }
         finally{   
            this.ChatGUI();
            new ReadMessages().start();
         }
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
              catch(NullPointerException npe){
               System.out.println("Caught Exception: " + npe.getMessage());
              }
              catch(Exception e){
               System.out.println("Caught Exception.");
              }
          }
        }
    }
    
    class CLActive extends JPanel {
    
        public CLActive() {
        
            this.setLayout(new GridLayout(3, 0));
            
            // JLabel jlHeader = new JLabel ("Users Active:");
            // jlHeader.setFont(new Font("Arial", Font.BOLD, 24));
            // jlHeader.setForeground(Color.GREEN);
       
            JRadioButton jrb1 = new JRadioButton("Amy");
            JRadioButton jrb2 = new JRadioButton("Regina");
            JRadioButton jrb3 = new JRadioButton("Miki");
            
            jrb1.setFont(new Font("Arial", Font.BOLD, 12));
            jrb2.setFont(new Font("Arial", Font.BOLD, 12));
            jrb3.setFont(new Font("Arial", Font.BOLD, 12));
            
            jrb1.setSelected(true);
            jrb2.setSelected(true);
            jrb3.setSelected(true);
            
            
            // this.add(jlHeader);
            this.add(jrb1);
            this.add(jrb2);
            this.add(jrb3);
        
        }
    
    }
    
    class CLDraw extends JPanel {
    
        private JButton jbDraw;
        
        public CLDraw() {
        
            JPanel jpCard = new JPanel();
            jpCard.setLayout(new GridLayout(2, 1));
            
            JLabel mockup = new JLabel();
            mockup.setIcon(new ImageIcon("../Assets/cardmockup.png"));
            
            jpCard.add(mockup);
            jpCard.add(jbDraw = new JButton("Draw"));

            MyAdapter ma = new MyAdapter();
            jbDraw.addActionListener(ma);
            
            this.add(jpCard);
        } 
    }
    
    
    class MyAdapter implements ActionListener {     // You can only have one public class in a file so you don't write public 

         Card card = new Card();
         
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
       
               String currColor = card.getCurrentColor();      // calls method which gets current color
               String drawnCard = card.getNextColor();
               
               // System.out.println(currColor);
               // System.out.println(drawnCard);
                  
                 try{
                  if(score < 50){
                     if (currColor.equals("purple")) {
                     
                        if (drawnCard.equals("purple")) {
                           score += 5;
                        } else if (drawnCard.equals("pink")) {
                           score += 1;
                        } else if (drawnCard.equals("blue")) {
                           score += 2;
                        } else if (drawnCard.equals("yellow")) {
                           score += 3;
                        } else if (drawnCard.equals("orange")) {
                           score += 4;
                        } 
                     } 
                     
                     if (currColor.equals("pink")) {
                     
                        if (drawnCard.equals("purple")) {
                           score += 4;
                        } else if (drawnCard.equals("pink")) {
                           score += 5;
                        } else if (drawnCard.equals("blue")) {
                           score += 1;
                        } else if (drawnCard.equals("yellow")) {
                           score += 2;
                        } else if (drawnCard.equals("orange")) {
                           score += 3;
                        } 
                     } 
                     
                      if (currColor.equals("blue")) {
                     
                        if (drawnCard.equals("purple")) {
                           score += 3;
                        } else if (drawnCard.equals("pink")) {
                           score += 4;
                        } else if (drawnCard.equals("blue")) {
                           score += 5;
                        } else if (drawnCard.equals("yellow")) {
                           score += 1;
                        } else if (drawnCard.equals("orange")) {
                           score += 2;
                        } 
                     } 
                     
                     if (currColor.equals("yellow")) {
                     
                        if (drawnCard.equals("purple")) {
                           score += 2;
                        } else if (drawnCard.equals("pink")) {
                           score += 3;
                        } else if (drawnCard.equals("blue")) {
                           score += 4;
                        } else if (drawnCard.equals("yellow")) {
                           score += 5;
                        } else if (drawnCard.equals("orange")) {
                           score += 1;
                        } 
                     } 
                     
                     if (currColor.equals("orange")) {
                     
                        if (drawnCard.equals("purple")) {
                           score += 1;
                        } else if (drawnCard.equals("pink")) {
                           score += 2;
                        } else if (drawnCard.equals("blue")) {
                           score += 3;
                        } else if (drawnCard.equals("yellow")) {
                           score += 4;
                        } else if (drawnCard.equals("orange")) {
                           score += 5;
                        } 
                     } 
                 }
                 else {
                  System.out.println(currentPlayer.getUsername() + " won!");
                  JOptionPane.showMessageDialog(null, currentPlayer.getUsername() + " won!");
                 }
              }
              catch(NullPointerException npe){
               System.out.println("Caught Exception: " + npe.getMessage());
              }
              catch(Exception e){
               System.out.println("Caught Exception.");
              }
               
               /**
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
               */
            finally{   
               currentPlayer.setScore(score);
               System.out.println(score);
            }
               
            }
            else if(choice.equals("Exit")){
               System.exit(0);
            }
             
            
         }
     } // end class MyAdapter
    
}







