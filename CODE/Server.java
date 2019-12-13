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
    
    public static Vector<ClientThread> clients = new Vector<ClientThread>();
    Vector<User> players = new Vector<>();
    
    /** Server main method. */
    public static void main(String[] args) {
    
        new Server();
    }
    
    /** Server constructor */
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
    
    /** broadCoast Players method. */
    public void broadcastPlayers() {
    
        for (ClientThread client : clients) {
            client.sendPlayers(players);
        }
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
                
                // instantiates ObjectInputStream, ObjectOutputStream
                oos = new ObjectOutputStream(cs.getOutputStream());
                ois = new ObjectInputStream(cs.getInputStream());
                this.start();
            
            } catch (IOException ioe) { }      
        } 
        
        /** 
          * sendPlayer method. 
          *
          * @param currentPlayer - Vector<User>
          */
        public void sendPlayers(Vector<User> currentPlayers) {
            
            try {
                oos.writeObject(currentPlayers);
            } catch (IOException ioe) { System.out.println(ioe.getMessage()); }
        }
          
        /** 
          * (ClientThread) run method.
          */
        public void run() {
        
            Object readObject = null;
            
            try {
                while(keepGoing) {
                    
                    readObject = ois.readObject();
                    
                    if (readObject instanceof String) {
                        
                        if (readObject.equals("numberOfUsers")) {
                            
                            if (clients.size() > 4) {
                            
                                oos.writeObject("max");
                                oos.flush();
                            
                                keepGoing = false;
                                
                            } else if (clients.size() <= 4) {
                                
                                oos.writeObject("continue");
                                oos.flush();
                                
                                Object readObj = ois.readObject(); 
                                
                                if (readObj instanceof User) {
                                
                                    User readPlayer = (User) readObj;
                                    players.add(readPlayer);
                                    // broadcastPlayers();
                                }                         
                            }
                            
                        } else if (readObject.equals("getPlayers")) {
                                           
                           broadcastPlayers();          
                                           
                        } else {
                            
                            for (int i = 0; i < clients.size(); i++) {
                            
                                clients.get(i).oos.writeObject(readObject);
                                clients.get(i).oos.flush();
                            }              
                        }   
                                             
                    } else if (readObject instanceof UserWon) {
                        
                        UserWon uw = (UserWon) readObject;
                        
                        for (int i = 0; i < clients.size(); i++) {
                        
                            clients.get(i).oos.writeObject(uw);
                            clients.get(i).oos.flush();
                        }
                    }
                }
                
            } catch (IOException ioe) {
            } catch (ClassNotFoundException cnfe) { }
        }
    }
}