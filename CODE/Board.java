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
    
        BufferedReader in = null;
        PrintWriter out = null;
        Socket s;
        
        public CLChat() {
        
            try {
        
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
               JFrame about = new JFrame("About");
               about.setSize(400, 400);
               about.setLocationRelativeTo(null);
               StringBuilder htmlBuilder  = new StringBuilder();
               htmlBuilder.append("<html>");
               htmlBuilder.append("<h1>About</h1><br />");
               htmlBuilder.append("<p>For our final group project in <i>Computational Problem-Solving in the information Domain II</i> ISTE-121, our group created a digital version of the popular kid's board game Candyland to which we added a small twist. ");
               htmlBuilder.append("This program was written in java and assets were made entirely by the group members.");
               htmlBuilder.append("To maximize interactiveness, we decided to let the users themselves have control over their pieces, and added a chat functionality on the side so that players can communicate with their opponents. ");
               htmlBuilder.append("For your convenience, the rules/directions for this game were added to the Menu bar along with this about page, and there is a key/guide in the interface underneath the 'Draw' button that tells you what our special cards mean.</p>");
               htmlBuilder.append("<br /><p>We hope you enjoy!</p><hr /><br /> <strong>Regina Bass</strong>  &nbsp; - &nbsp;2nd Year Web and Mobile Computing <br /> <strong>Amy Tan</strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -  &nbsp;2nd Year Web and Mobile Computing <br /> <strong>Miki Mian</strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   -  &nbsp;3rd Year Human Centered Computing</html>");
               String html = htmlBuilder.toString();
               JTextArea textareaAbout = new JTextArea(html);
               textareaAbout.setEnabled(false);
               about.add(textareaAbout);
               about.setVisible(true); 
            }
            else if(choice.equals("Rules")){
               System.out.println("Rules was clicked");
            }
            else if(choice.equals("Key")){
               System.out.println("Key was clicked");
            }
            else if(choice.equals("Disclaimer")){
               System.out.println("Disclaimer was clicked");
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







