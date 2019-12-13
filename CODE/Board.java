import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.ImageIcon;
import javax.swing.border.*;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
  * ISTE 121 - CANDYLAND
  * Board class.
  *
  * @author Regina Bass
  * @author Miki Mian
  * @author Amy Tan
  * @version 1.0, 120219
  * @version 2.0, 121319
  */
public class Board extends JFrame {
    
    
   // Attributes - to Server
    private User currentPlayer;
    private Vector<User> currentPlayers = new Vector<>();
    private Socket s;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    
   // Attributes - GUI
    private JTextArea jtaMain;
    private JTextPane tPane;
    private JTextField jtfMsg;
    private JButton jbSend;
    public Color c = Color.black;
    public JLabel cardIcon;    

   
    public int score = 0;  // Global Variable score to be changed in Action Listener


    public Board(User currentPlayer, Socket s, ObjectInputStream ois, ObjectOutputStream oos) {
    
        this.currentPlayer = currentPlayer;
        this.s = s;
        this.ois = ois;
        this.oos = oos;
        
        this.setSize(1050, 1000);
        this.setLocationRelativeTo(null);
        this.setTitle("CANDYLAND");
        
        new ReadMessages().start();
        
        try {
            oos.writeObject("getPlayers");
            oos.flush();
        } catch (IOException ioe) { System.out.println(ioe.getMessage()); }
        
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
        
        
        //Display GUI - frame modifications
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
            
	    //create four pieces, each assigned a different gingerbread
            DraggableComponent one = new DraggableComponent(new ImageIcon("Assets/redgin.png").getImage());
            DraggableComponent two = new DraggableComponent(new ImageIcon("Assets/greengin.png").getImage());
            DraggableComponent three = new DraggableComponent(new ImageIcon("Assets/bluegin.png").getImage());
            DraggableComponent four = new DraggableComponent(new ImageIcon("Assets/yellowgin.png").getImage());
            
	    //add piece to board
            board.add(one);
            
	    //place piece near the start of board
            one.setLocation(0, 480);

        }
    }
	
    /**
     * extends JPanel 
     * 
     * creates draggable pieces 
     */
	
    class DraggableComponent extends JPanel {
          
     //x-coordinate of initial position
     private volatile int screenX = 0;
	    
     //y-coordinate of initial position
     private volatile int screenY = 0;
	    
     //x-coordinate of new position
     private volatile int myX = 0;
	    
     //y-coordinate of new position
     private volatile int myY = 0;
	    
     private Image img;

     /**
      * @param Image img 
      * 
      * sets size of draggable piece 
      */
     public DraggableComponent(Image img) {
     
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
        setOpaque(false);
          
       
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
           //prints (x,y) of piece (for debugging purposes)
           System.out.println("X Position is: " + (myX + deltaX) + "  Y Position is: " + (myY + deltaY));

         }
         
         @Override
         public void mouseMoved(MouseEvent e) { }
   
       });
     }
     
     //gets URL of piece 
     public DraggableComponent(String img) {
       this(new ImageIcon(img).getImage());
     }
     
     //paints piece on board 
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
            //jtaMain = new JTextArea(10, 30);
            //jtaMain.setEditable(false);
            tPane = new JTextPane();
            JScrollPane jspMain = new JScrollPane(tPane);
            tPane.setMargin(new Insets(5, 5, 5, 5));
            jpMain.add(jspMain);
            tPane.setFocusable(false);
        
            JPanel jpBottom = new JPanel();
            jpBottom.setLayout(new FlowLayout());
            jtfMsg = new JTextField(20);
            jbSend = new JButton("Send");
            jpBottom.add(jtfMsg);
            jpBottom.add(jbSend);
        
            //getContentPane().add(jpMain);
            this.add(jpMain, BorderLayout.CENTER);
            this.add(jpBottom, BorderLayout.SOUTH);
            
         
         jbSend.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            try {
                String dw = currentPlayer.getUsername() + ": " + jtfMsg.getText();
                c = Color.black;
                oos.writeObject(dw);
                //appendToPane(tPane, "\n" + dw, Color.black);
                oos.flush();
                // System.out.println(currentPlayer.getUsername() + ": " + jtfMsg.getText());
                jtfMsg.setText("");      //blank Text Field - ready for next input
            } catch (IOException ioe) { System.out.println(ioe.getMessage()); }
         }
       });
       
       }
       
    }
    
    /* METHOD - appendToPane
     * @param JTextPane to be appended on
     * @param String of text to be appended
     * @param Color of text
     */
    public void appendToPane(JTextPane tp, String msg, Color c){
        // All this code is stylization
        this.c = c;
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
    
    class CLActive extends JPanel {
    
        public CLActive() {
        
                this.setLayout(new GridLayout(3, 0));
                
                Border border = BorderFactory.createTitledBorder("Active Users");
                this.setBorder(border);
                this.setPreferredSize(new Dimension(350, 200));
                
                for (User player : currentPlayers) {
                    JCheckBox jcbUser = new JCheckBox(player.getUsername());
                    jcbUser.setFont(new Font("Arial", Font.BOLD, 12));
                    jcbUser.setSelected(true);
                    this.add(jcbUser);
                }
        }
    }
    
    class CLDraw extends JPanel {
    
        private JButton jbDraw;
        
        public CLDraw() {
            //Grid Layout, located on the lower right of GUI with a LabelIcon of a card and a draw button
        
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
    
    
    /** This is an ActionListener class for every single component on the GUI except for the send button on the chat**/
    class MyAdapter implements ActionListener { 

         Card card = new Card();   // for Draw Button
         
         public void actionPerformed(ActionEvent ae){
            String choice = ae.getActionCommand();     //gets Source as String
            
            if( choice.equals("About") ){     //Menu Item that opens an 'About the Project' page in a browser
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
            }
            else if(choice.equals("Rules")){    //Menu Item that opens the 'Rules of the game' page in a browser
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
            else if(choice.equals("Key")){ //Menu Bar Item that opens Keys for what the special cards means
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
            else if(choice.equals("Disclaimer")){ //Menu Item that opens the 'Disclaimer' page in a browser
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
            else if(choice.equals("Draw")){ // This method includes a set of heavily nested if-statments. Switch case @Version 1.0, If-Else @Version 2.0
       
               // DEBUG    String currColor = card.getCurrentColor();      
               // DEBUG    String drawnCard = card.getNextColor();
               // DEBUG    System.out.println(currColor);
               // DEBUG    System.out.println(drawnCard);
                  
                 try{
                 if(score < 50){       //If the game is still going on 
                 
                     //draw a card and set the GUI
                     String currColor = card.getCurrentColor();      // calls method which gets current color
                     String drawnCard = card.getNextColor();         // calls a method which  gets the next drawn card's color
                     
                     //sets the card icon to proper color of next Card
                     String cardAsset = new String("Assets/" + drawnCard + ".png"); 
                     cardIcon.setIcon(new ImageIcon(cardAsset));  
                     
                   //If statment for the very first draw
                     if (score == 0 || currColor == null){
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
                  /*
                   
                     NESTED CASE FORMAT:
                     If your card from the last turn was one color (parent nest)
                     and your next Card is whatever color/special, (child nest)
                     here is how the score distrubution is set. 
                     Follows this format for like 200 more lines of code
                   
                  */
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
           
                     /** The star cards are not as simple as the point system for the color cards 
                         because it has to work no matter where you are on the board, which we keep 
                         track by your card score not your piece location (or else it'd be easy to cheat)
                         The board follows a pattern, and knows where the candy pieces are located.
                         Keep in mind that the code format looks at what your NEXT card is, not how your score
                         is calculated by what your current card is.
                     **/
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
                        
                        //SPECIAL CARDS (after special card)
                        else if (drawnCard.equals("goldStar")) {
                           score += 8;
                        } else if (drawnCard.equals("redStar")) {
                           score = score;
                        } else if (drawnCard.equals("blackStar")) {
                          score =  1;
                        } 
                        // CANDY CARDS (after special card)
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
          
                  /** The Candy Cards were slightly easier since it knows where your location is based off the
                      points you get when you get the card. No matter where you are on the board when you draw
                      a candy card it bring you to a specific destination.
                  **/
          
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
                     
                     
                     //Displays the score after setting it
                     currentPlayer.setScore(score);
                     System.out.println(score);
                     
                     //Displays what card you pulled in the chat
                     String cardMessage = new String(currentPlayer.getUsername() + " drew a " + card.getCurrentColor() + " card!");
                     c = Color.red;                                // Sets font color of Card Draws on your Side to Red (see
                     oos.writeObject(cardMessage);
                     oos.flush();
       

                     if(score >=50){ // this code runs when someone wins! It has to be within the large if block since all this code runs AFTER the draw button is clicked
                            
                        try {
                        
                            // oos.writeObject(currentPlayer.getUsername() + " won!");
                            // oos.flush();
                            
                            oos.writeObject(new UserWon(currentPlayer));
                            oos.flush();
                            
                        } catch (IOException ioe) { System.out.println(ioe.getMessage()); }
                    
                        // System.out.println(currentPlayer.getUsername() + " won!");
                        // JOptionPane.showMessageDialog(null, currentPlayer.getUsername() + " won!"); 
                     }
                 } else {
                  System.out.println("Somebody already won the game! Thanks for playing.");  // this else is for all extraneous solutions
                 }
              }// end try
             //catch statements
              catch(NullPointerException npe){
               System.out.println("Caught Exception: " + npe.getMessage());
              }
              catch(IOException ioe){
               System.out.println("Caught Exception: " + ioe.getMessage());
              }
              catch(Exception e){
               System.out.println("Caught Exception.");
              }
               
            }
            else if(choice.equals("Exit")){  //Exits the program
               System.exit(0);
            }
         }
         
     } // end class MyAdapter  
       
     /** 
       * ReadMessages class.
       *
       * -> Reads incoming objects from server.
       */
     class ReadMessages extends Thread {

     Object readObject = null;
    
      /** ReadMessages run method */
      public void run() {
        try {
            while(true) {
            
            readObject = ois.readObject();
            
            if (readObject instanceof String) {
                
               // reads incoming messages, appends to JTextArea  (version 2 - switched from JTextArea to JTextPane)
                readObject = (String) readObject;
                appendToPane(tPane, "\n" + readObject, c);
                c = Color.black;
        
            } else if (readObject instanceof Vector) {
                
                currentPlayers = (Vector) readObject;
       
            } else if (readObject instanceof UserWon) {
            
                UserWon uw = (UserWon) readObject;         
                JOptionPane.showMessageDialog(null, uw);         
                System.exit(0);
            }
          }
        } catch (IOException ioe) { System.out.println(ioe.getMessage()); 
        } catch (NullPointerException npe) { System.out.println(npe.getMessage());
        } catch (Exception e) { System.out.println(e.getMessage());
        }
      }
    } // end class ReadMessages
        

}
