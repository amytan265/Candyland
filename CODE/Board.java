import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.ImageIcon;
import javax.swing.border.*;

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
    
    private JTextArea jtaMain;
    private JTextField jtfMsg;
    private JButton jbSend;
    
    private Socket s;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Vector<User> players = null;
   
    public int score = 0;
    public JLabel cardIcon;

    public Board(User currentPlayer, Socket s, BufferedReader in, PrintWriter out) {
    
        this.currentPlayer = currentPlayer;
        this.s = s;
        this.in = in;
        this.out = out;
    
        this.setSize(1050, 1000);
        this.setLocationRelativeTo(null);
        this.setTitle("CANDYLAND");
        
        new ReadMessages().start();
        
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
            
            ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("Assets/candylandboardRevised.png"));
            Image image = imageIcon.getImage();
            Image newimg = image.getScaledInstance(709, 800,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            imageIcon = new ImageIcon(newimg);  // transform it back
            //ImageIcon scaledImage = imageIcon.getImageIcon().getScaledInstance(709, 800,Image.SCALE_DEFAULT);
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
            
            one.setLocation(0, 500);
            two.setLocation(0, 510);
            three.setLocation(0,520);
            four.setLocation(0, 530);
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
     

   }//end of DraggableComponent class
    
     class CLChat extends JPanel {
        
        public CLChat() {
        
            this.ChatGUI();
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
    }
    
    class CLActive extends JPanel {
    
        public CLActive() {
        
            this.setLayout(new GridLayout(3, 0));
            
            // JLabel jlHeader = new JLabel ("Users Active:");
            // jlHeader.setFont(new Font("Arial", Font.BOLD, 24));
            // jlHeader.setForeground(Color.GREEN);

            
            Border border = BorderFactory.createTitledBorder("Active Users");
            this.setBorder(border);
            this.setPreferredSize(new Dimension(350, 200));
       
            JCheckBox jcb1 = new JCheckBox("Amy");
            JCheckBox jcb2 = new JCheckBox("Regina");
            JCheckBox jcb3 = new JCheckBox("Miki");
            
            jcb1.setFont(new Font("Arial", Font.BOLD, 12));
            jcb2.setFont(new Font("Arial", Font.BOLD, 12));
            jcb3.setFont(new Font("Arial", Font.BOLD, 12));
            
            jcb1.setSelected(true);
            jcb2.setSelected(true);
            jcb3.setSelected(true);
            
            jcb1.setSelected(true);
            jcb2.setSelected(true);
            jcb3.setSelected(true);
                      
            jcb2.setEnabled(false); 
            jcb3.setEnabled(false); 
            
            // this.add(jlHeader);
            this.add(jcb1);
            this.add(jcb2);
            this.add(jcb3);
        
        }
    
    }
    
    class CLDraw extends JPanel {
    
        private JButton jbDraw;
        
        public CLDraw() {
        
            JPanel jpCard = new JPanel();
            jpCard.setLayout(new GridLayout(2, 1));
            
            cardIcon = new JLabel();
            cardIcon.setIcon(new ImageIcon("Assets/startCard.png"));
            
            jpCard.add(cardIcon);
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
                     if (score == 0){
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
                        //SPECIAL CARDS
                          else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score =  1;
                        } 
                        // CANDY CARDS
                          else if (drawnCard.equals("candyCorn")) {
                           score = 12;
                        } else if (drawnCard.equals("candyCane")) {
                           score = 25;
                        } else if (drawnCard.equals("chocolate")) {
                           score = 32;
                        } else if (drawnCard.equals("lollipop")) {
                           score = 41;
                        } 

                     }
                     else if (currColor.equals("purple")) {
                     
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
                        //SPECIAL CARDS
                          else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score =  1;
                        } 
                        // CANDY CARDS
                          else if (drawnCard.equals("candyCorn")) {
                           score = 12;
                        } else if (drawnCard.equals("candyCane")) {
                           score = 25;
                        } else if (drawnCard.equals("chocolate")) {
                           score = 32;
                        } else if (drawnCard.equals("lollipop")) {
                           score = 41;
                        }  
                     } 
                     
                     else if (currColor.equals("pink")) {
                     
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
                        //SPECIAL CARDS
                          else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score =  1;
                        } 
                        // CANDY CARDS
                          else if (drawnCard.equals("candyCorn")) {
                           score = 12;
                        } else if (drawnCard.equals("candyCane")) {
                           score = 25;
                        } else if (drawnCard.equals("chocolate")) {
                           score = 32;
                        } else if (drawnCard.equals("lollipop")) {
                           score = 41;
                        }  
                     } 
                     
                      else if (currColor.equals("blue")) {
                     
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
                        //SPECIAL CARDS
                          else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score =  1;
                        } 
                        // CANDY CARDS
                          else if (drawnCard.equals("candyCorn")) {
                           score = 12;
                        } else if (drawnCard.equals("candyCane")) {
                           score = 25;
                        } else if (drawnCard.equals("chocolate")) {
                           score = 32;
                        } else if (drawnCard.equals("lollipop")) {
                           score = 41;
                        }  
                     } 
                     
                     else if (currColor.equals("yellow")) {
                     
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
                        //SPECIAL CARDS
                          else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score = 1;
                        } 
                        // CANDY CARDS
                          else if (drawnCard.equals("candyCorn")) {
                           score = 12;
                        } else if (drawnCard.equals("candyCane")) {
                           score = 25;
                        } else if (drawnCard.equals("chocolate")) {
                           score = 32;
                        } else if (drawnCard.equals("lollipop")) {
                           score = 41;
                        } 
                     } 
                     
                     else if (currColor.equals("orange")) {
                     
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
                        //SPECIAL CARDS
                          else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score =  1;
                        } 
                        // CANDY CARDS
                          else if (drawnCard.equals("candyCorn")) {
                           score = 12;
                        } else if (drawnCard.equals("candyCane")) {
                           score = 25;
                        } else if (drawnCard.equals("chocolate")) {
                           score = 32;
                        } else if (drawnCard.equals("lollipop")) {
                           score = 41;
                        }  
                     }
           // NESTED STAR CARDS 
                     else if (currColor.equals("redStar")) {
                        if (score == 1 || score == 6 || score == 11 || score == 16 || score == 21 || score == 26 || score == 31 || score == 36 || score == 46){ // if you're on a purple at the time of drawing the redStar card
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
                        else if (score == 2 || score == 7 || score == 17 || score == 22 || score == 27 || score == 37 || score == 42 || score == 47){ // if you're on a pink at the time of drawing the redStar card
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
                        else if (score == 3 || score == 8 || score == 13 || score == 18 || score == 23 || score == 28 || score == 33 || score == 38 || score == 43 || score == 48){ // if you're on a blue at the time of drawing the redStar card
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
                        else if (score == 4 || score == 9 || score == 14 || score == 19 || score == 24 || score == 29 || score == 34 || score == 39 || score == 44 || score == 49){ // if you're on a yellow at the time of drawing the redStar card
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
                        else if (score == 5 || score == 10 || score == 15 || score == 20 || score == 30 || score == 35 || score == 40 || score == 45 || score == 50){ // if you're on a orange at the time of drawing the redStar card
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
                        
                        //SPECIAL CARDS
                        else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score =  1;
                        } 
                        // CANDY CARDS
                         else if (drawnCard.equals("candyCorn")) {
                           score = 12;
                        } else if (drawnCard.equals("candyCane")) {
                           score = 25;
                        } else if (drawnCard.equals("chocolate")) {
                           score = 32;
                        } else if (drawnCard.equals("lollipop")) {
                           score = 41;
                        }  
                     } // end of RedStar instance
                     
                     else if (currColor.equals("goldStar")) {
                     
                        /* purple + 8   = yellow
                           pink   + 8   = orange
                           blue   + 8   = purple
                           yellow + 8   = pink 
                           orange + 8   = blue
                        */
                        if (score == 11 || score == 16 || score == 21 || score == 26 || score == 31 || score == 36 || score == 46){ // if you're on a purple due to drawing the goldStar last turn
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
                              //SPECIAL CARDS
                                else if (drawnCard.equals("goldStar")) {
                                 score += 8;
                              } else if (drawnCard.equals("redStar")) {
                                 score = score;
                              } else if (drawnCard.equals("blackStar")) {
                                score =  1;
                              } 
                              // CANDY CARDS
                                else if (drawnCard.equals("candyCorn")) {
                                 score = 12;
                              } else if (drawnCard.equals("candyCane")) {
                                 score = 25;
                              } else if (drawnCard.equals("chocolate")) {
                                 score = 32;
                              } else if (drawnCard.equals("lollipop")) {
                                 score = 41;
                              }
                        }
                        else if (score == 17 || score == 22 || score == 27 || score == 37 || score == 42 || score == 47){ // if you're on a pink due to drawing the goldStar last turn
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
                              //SPECIAL CARDS
                                else if (drawnCard.equals("goldStar")) {
                                 score += 8;
                              } else if (drawnCard.equals("redStar")) {
                                 score = score;
                              } else if (drawnCard.equals("blackStar")) {
                                score =  1;
                              } 
                              // CANDY CARDS
                                else if (drawnCard.equals("candyCorn")) {
                                 score = 12;
                              } else if (drawnCard.equals("candyCane")) {
                                 score = 25;
                              } else if (drawnCard.equals("chocolate")) {
                                 score = 32;
                              } else if (drawnCard.equals("lollipop")) {
                                 score = 41;
                              }
                        }
                        else if (score == 8 || score == 13 || score == 18 || score == 23 || score == 28 || score == 33 || score == 38 || score == 43 || score == 48){ // if you're on a blue due to drawing the goldStar last turn
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
                              //SPECIAL CARDS
                                else if (drawnCard.equals("goldStar")) {
                                 score += 8;
                              } else if (drawnCard.equals("redStar")) {
                                 score = score;
                              } else if (drawnCard.equals("blackStar")) {
                                score =  1;
                              } 
                              // CANDY CARDS
                                else if (drawnCard.equals("candyCorn")) {
                                 score = 12;
                              } else if (drawnCard.equals("candyCane")) {
                                 score = 25;
                              } else if (drawnCard.equals("chocolate")) {
                                 score = 32;
                              } else if (drawnCard.equals("lollipop")) {
                                 score = 41;
                              }   
                        }
                        else if (score == 9 || score == 14 || score == 19 || score == 24 || score == 29 || score == 34 || score == 39 || score == 44 || score == 49){ // if you're on a yellow due to drawing the goldStar last turn
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
                              //SPECIAL CARDS
                                else if (drawnCard.equals("goldStar")) {
                                 score += 8;
                              } else if (drawnCard.equals("redStar")) {
                                 score = score;
                              } else if (drawnCard.equals("blackStar")) {
                                score =  1;
                              } 
                              // CANDY CARDS
                                else if (drawnCard.equals("candyCorn")) {
                                 score = 12;
                              } else if (drawnCard.equals("candyCane")) {
                                 score = 25;
                              } else if (drawnCard.equals("chocolate")) {
                                 score = 32;
                              } else if (drawnCard.equals("lollipop")) {
                                 score = 41;
                              }
                        }
                        else if (score == 10 || score == 15 || score == 20 || score == 30 || score == 35 || score == 40 || score == 45 || score == 50){ // if you're on an orange due to drawing the goldStar last turn
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
                              //SPECIAL CARDS
                                else if (drawnCard.equals("goldStar")) {
                                 score += 8;
                              } else if (drawnCard.equals("redStar")) {
                                 score = score;
                              } else if (drawnCard.equals("blackStar")) {
                                score =  1;
                              } 
                              // CANDY CARDS
                                else if (drawnCard.equals("candyCorn")) {
                                 score = 12;
                              } else if (drawnCard.equals("candyCane")) {
                                 score = 25;
                              } else if (drawnCard.equals("chocolate")) {
                                 score = 32;
                              } else if (drawnCard.equals("lollipop")) {
                                 score = 41;
                              }  
                        }
                     }
                     else if (currColor.equals("blackStar")) {
                     
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
                        //SPECIAL CARDS
                          else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score =  1;
                        } 
                        // CANDY CARDS
                          else if (drawnCard.equals("candyCorn")) {
                           score = 12;
                        } else if (drawnCard.equals("candyCane")) {
                           score = 25;
                        } else if (drawnCard.equals("chocolate")) {
                           score = 32;
                        } else if (drawnCard.equals("lollipop")) {
                           score = 41;
                        }  
                     }  // end of blackStar instance
          //NESTED CANDY CARDS          
                     else if (currColor.equals("chocolate")) {
                     
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
                        //SPECIAL CARDS
                          else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score =  1;
                        } 
                        // CANDY CARDS
                          else if (drawnCard.equals("candyCorn")) {
                           score = 12;
                        } else if (drawnCard.equals("candyCane")) {
                           score = 25;
                        } else if (drawnCard.equals("chocolate")) {
                           score = 32;
                        } else if (drawnCard.equals("lollipop")) {
                           score = 41;
                        }  
                     } // end of chocolate instance
                     else if (currColor.equals("candyCorn")) {
                     
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
                        //SPECIAL CARDS
                          else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score =  1;
                        } 
                        // CANDY CARDS
                          else if (drawnCard.equals("candyCorn")) {
                           score = 12;
                        } else if (drawnCard.equals("candyCane")) {
                           score = 25;
                        } else if (drawnCard.equals("chocolate")) {
                           score = 32;
                        } else if (drawnCard.equals("lollipop")) {
                           score = 41;
                        }  
                     } //end of candyCorn instance
                     else if (currColor.equals("candyCane")) {
                     
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
                        //SPECIAL CARDS
                          else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score =  1;
                        } 
                        // CANDY CARDS
                          else if (drawnCard.equals("candyCorn")) {
                           score = 12;
                        } else if (drawnCard.equals("candyCane")) {
                           score = 25;
                        } else if (drawnCard.equals("chocolate")) {
                           score = 32;
                        } else if (drawnCard.equals("lollipop")) {
                           score = 41;
                        }  
                     } // end of candyCane instance
                     else if (currColor.equals("lollipop")) {
                     
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
                        //SPECIAL CARDS
                          else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score =  1;
                        } 
                        // CANDY CARDS
                          else if (drawnCard.equals("candyCorn")) {
                           score = 12;
                        } else if (drawnCard.equals("candyCane")) {
                           score = 25;
                        } else if (drawnCard.equals("chocolate")) {
                           score = 32;
                        } else if (drawnCard.equals("lollipop")) {
                           score = 41;
                        }  
                     } // end of lollipop instance
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
              finally{  
                  String cardAsset = new String("Assets/" + drawnCard + ".png");
                  cardIcon.setIcon(new ImageIcon(cardAsset));
                  currentPlayer.setScore(score);
                  System.out.println(score);
               }
               
            }
            else if(choice.equals("Exit")){
               System.exit(0);
            }
             
            
         }
     } // end class MyAdapter
     
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







