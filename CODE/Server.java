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
    public static Vector<ClientThread> clients = new Vector<ClientThread>();
    
    Vector<User> players = new Vector<User>();
    
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
        ObjectInputStream ois;
        ObjectOutputStream oos;
        Socket cs;
        
        boolean keepGoing = true;
        
        /** ClientThread constructor */
        public ClientThread(Socket _cs) {
            
            // socket attribute
            cs = _cs;
            
            try  {
                
                oos = new ObjectOutputStream(cs.getOutputStream());
                ois = new ObjectInputStream(cs.getInputStream());
                this.start();
            
            } catch (IOException ioe) { }      
        } 
          
        // run method
        public void run() {
        
            Object readObject = null;
            
            try {
                while(keepGoing) {
                    
                    // reads line 
                    readObject = ois.readObject();
                    
                    if (readObject instanceof String) {
                        
                        if (readObject.equals("numberOfUsers")) {
                            
                            if (clients.size() >= 4) {
                            
                                oos.writeObject("max");
                                oos.flush();
                            
                            } else {
                            
                                oos.writeObject("continue");
                                oos.flush(); 
                                                 
                                User readPlayer = (User) ois.readObject();
                                players.add(readPlayer);
                            }

                        } else {
                            
                            for (int i = 0; i < clients.size(); i++) {
                            
                                clients.get(i).oos.writeObject(readObject);
                                clients.get(i).oos.flush();
                            }              
                        }                       
                    } 
                }
            } catch(IOException ioe) {
               System.out.print("IO Exception: " + ioe);
            } catch(ClassNotFoundException cnfe) {
               System.out.println("Class not found exception: " + cnfe.getMessage());
            }
             catch(Exception e) {
               System.out.println("Exception found: " + e.getMessage());
            }
        }
    }
}