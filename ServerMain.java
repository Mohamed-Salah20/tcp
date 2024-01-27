import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;
import java.util.List;
import java.util.ArrayList;
/**
 * ServerMain
 */
public class ServerMain {
    
    private static final int PORT_NUMBER = 2025;
    private List<ServerThread> clientThreads;   

    public ServerMain() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
        System.out.println("Port number " + PORT_NUMBER + " is open");
        clientThreads = new ArrayList<>();
        
        //infinite loop to keep listening & waiting for new connections & create a new thread
        while (true) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket,this);

            clientThreads.add(serverThread);

            Thread thread = new Thread(serverThread);
            thread.start();
        }
    }

    // private int countClientID = 1;
    public int getClientID(){
        return clientThreads.size();
    }
    

    public synchronized void broadcastMessage(String msg, ServerThread senderThread) {
        for (ServerThread clientThread : clientThreads) {
            if (clientThread != senderThread) {
                clientThread.sendMessage(msg);
                
            }
        }
    }

    public static void main(String[] args) {
        try {
            new ServerMain();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}