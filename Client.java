import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
public class Client {
    
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT_NUMBER = 2025;
    
    public Client() throws IOException, SocketException {
        
        Socket socket = new Socket(IP_ADDRESS, PORT_NUMBER);
        
        System.out.println("Connected to Server");
         
        
        BufferedReader in_socket = new BufferedReader(new InputStreamReader(
            socket.getInputStream()
            ));
        PrintWriter out_socket = new PrintWriter(new OutputStreamWriter(
            socket.getOutputStream()
            ));
        Scanner scanner = new Scanner(System.in);
        

        new Thread(
            new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try {
                            String serverMsg = in_socket.readLine();
                            // Server closed the connection
                            if (serverMsg == null) {
                                break; 
                            }
                            System.out.println("Server Says: " + serverMsg);
                        } catch (IOException e) {
                            System.err.println("Exited program");
                            System.exit(1); // WARNING DISASTER ERROR, TODO later
                        }
                    }
                        
                }
                
            }
        ).start();

            
        while (true) {
            // System.out.println("Enter msg: ");
            String clientMsg = scanner.nextLine();
            out_socket.println(clientMsg);
            out_socket.flush();
            
            if (clientMsg.equalsIgnoreCase("exit")) {
                break;
            }
        }
        socket.close();
        System.out.println("Socket  Closed");
    }
    public static void main(String[] args) {
        try {
            new Client();
        }catch(SocketException e){
            System.out.println("connection error, Server Down");
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
