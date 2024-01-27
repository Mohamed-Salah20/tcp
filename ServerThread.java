import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class ServerThread implements Runnable {
    
    private Socket socket; //so that other threads & classes cant access the same socket in a particular thread
    private ServerMain serverMain;
    
    private BufferedReader in_socket;
    private PrintWriter out_socket;
    
    public ServerThread(Socket socket,ServerMain serverMain) //has to be public so that we can call it from ServerMain class 
    {
        this.socket = socket;
        this.serverMain = serverMain;
    }
    @Override
    public void run() {
        
        final int CLIENT_ID = serverMain.getClientID();
        
        try {
        System.out.println("New Thread started from Client num " + CLIENT_ID + ", Address : " + socket.getInetAddress() + ", Port : " + socket.getPort());

            //IO
            in_socket = new BufferedReader(new InputStreamReader(
            socket.getInputStream())
            );
            out_socket = new PrintWriter(new OutputStreamWriter(
            socket.getOutputStream())
            );    
            //
        out_socket.println("WELCOME CLIENT " + CLIENT_ID + " TO THE SERVER, WRITE exit TO EXIT");
        out_socket.flush();
        
        while (true) {
            String msg = in_socket.readLine();
            System.out.println("Client " +CLIENT_ID+ " says : " + msg);
            
            if (msg.equalsIgnoreCase("exit")) {
                break;
            }else{
                serverMain.broadcastMessage("Client " + CLIENT_ID + " says: " + msg, this);
            }
        }
        socket.close();
        System.out.println("Client " +CLIENT_ID+" socket closed");
        
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }
    public void sendMessage(String message) {
        out_socket.println(message);
        out_socket.flush();

        System.out.println("Message sent by Client: " + message);
    }
}
