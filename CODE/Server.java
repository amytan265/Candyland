import java.io.*;
import java.net.*;
import java.util.*;

/**
  * ISTE 121 - CANDYLAND
  * Server class.
  *
  * @author Regina Bass
  * @author Miki Mian
  * @author Amy Tan
  * @version 1.0, 120219
  */
public class Server {
    
    // thread safe list of clients
    Vector<ClientThread> clients = new Vector<ClientThread>();
    
    public static void main(String[] args) {
    
        new Server();
    }
    
    /** ChatServer constructor */
    public Server() {
   
        try {
            
            // instantiates serversocket
            ServerSocket ss = new ServerSocket(16789);
            Socket cs;
       
            while(true) {
               
               // accepts socket connection, puts each client connection in vector list
               System.out.println("Waiting for a client connection.");
               cs = ss.accept();
               System.out.println("Have a client connection." + cs);
               ClientThread ct = new ClientThread(cs);
               clients.add(ct);      
            }
        } catch (IOException ioe) {}  
    }
    
    /** ClientThread (inner class) */
    class ClientThread extends Thread {
        
        // attributes
        BufferedReader in;
        PrintWriter out;
        Socket cs;
        
        boolean keepGoing = true;
        
        /** ClientThread constructor */
        public ClientThread(Socket _cs) {
            
            // socket attribute
            cs = _cs;
            
            try  {
                
                // instantiates bufferedreader, printwriter for clientthread, start run method
                in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(cs.getOutputStream()));
                this.start();
            
            } catch (IOException ioe) { }      
        } 
          
        // run method
        public void run() {
        
            String line;
            try {
                while(keepGoing) {
                    
                    // reads line 
                    line = in.readLine();
                    
                    if (line.equals("Exit")) {
                        
                        // recieves exit message, prints that user has exited
                        System.out.println("Client Exited");
                        keepGoing = false;
                        
                    } else {
                        
                        // iterates through clients to print message, flush
                        for (int i = 0; i < clients.size(); i++) {
                            clients.get(i).out.println(line);
                            clients.get(i).out.flush();
                        }
                    }
                }
            } catch (IOException ioe) {}
        }
    }
}