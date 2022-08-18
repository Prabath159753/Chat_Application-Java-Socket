package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author : Kavishka Prabath
 * @since : 0.1.0
 **/

public class ClientHandler extends Thread {

    private ArrayList<ClientHandler> clients;

    private Socket socket;

    public BufferedReader in;

    public PrintWriter writer;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) throws IOException {
        this.clients=clients;
        this.socket=socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run(){
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.equalsIgnoreCase( "exit")) {
                    break;
                }
                for (ClientHandler cl : clients) {
                    cl.writer.println(msg);
                    System.out.println(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                in.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
