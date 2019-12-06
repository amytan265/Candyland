import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;

public class ChatClient extends JFrame {
    
    // attributes
    private JTextArea jtaMain;
    private JTextField jtfMsg;
    private JButton jbSend;
    private JMenuItem jmiExit;
    
    BufferedReader in = null;
    PrintWriter out = null;
    Socket s;
    
    String username;
    
    /** Main method. */
    public static void main(String[] args) {
        
        // checks if command line argument is specified, uses as parameter for ChatClient constructor
        if (args.length == 0) {
            System.out.println("Please enter a username to enter the chat.");
            System.exit(0);
        } else {
            new ChatClient(args[0]);
        }
    }
    
    /** ChatClient constructor */
    public ChatClient(String _username) {
    
        username = _username;
        
        // instantiates connection with socket, bufferedreader, printwriter
        try {
        
            s = new Socket("localhost", 16789);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            
        } catch (IOException ioe) {} 
        
        // builds GUI interface from ChatGUI method
        this.ChatGUI();
        
        // start inner class of ReadMessages to read incoming messages from other clients
        new ReadMessages().start();
    }
    
    /** ChatGUI method */
    public void ChatGUI() {
        
        setTitle("Chat");
        setLayout(new BorderLayout());
        
        // menu
        JMenuBar jmbMain = new JMenuBar();
        JMenu jmFile = new JMenu("File");
        jmiExit = new JMenuItem("Exit");
        
        // main panel > text area
        JPanel jpMain = new JPanel();
        jtaMain = new JTextArea(10, 30);
        jtaMain.setEditable(false);
        JScrollPane jspMain = new JScrollPane(jtaMain);
        jpMain.add(jspMain);
        
        // message text field, send button
        JPanel jpBottom = new JPanel();
        jpBottom.setLayout(new FlowLayout());
        jpBottom.add(jtfMsg = new JTextField(20));
        jpBottom.add(jbSend = new JButton("Send"));
             
        // add menu bar
        jmFile.add(jmiExit);
        jmbMain.add(jmFile);
        setJMenuBar(jmbMain);
        
        // add to frame
        add(jpMain, BorderLayout.CENTER);
        add(jpBottom, BorderLayout.SOUTH);
        
        // windowadapter for exiting GUI
        WindowListener wl = new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                out.println("Exit");
                out.flush();  
                System.exit(0);
            }
        };
        addWindowListener(wl);
        
        // exit action listener > sends "exit" to server, terminates program
        jmiExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                out.println("Exit");
                out.flush();  
                System.exit(0);
            }
        });
        
        // send actionlistener > sends message with username to server to be sent to all clients
        jbSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                out.println(getUsername() + ": " + jtfMsg.getText());
                System.out.println(getUsername() + ": " + jtfMsg.getText());
                out.flush();
                jtfMsg.setText("");
            }
        });
      
        setResizable(false);
        pack();
        setVisible(true);       
    }
    
    /** getUsername getter. */
    public String getUsername() {
    
        return username;
    }
    
    /** ReadMessages (inner class) */
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