package network;

import org.glassfish.tyrus.server.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TyrusServer {

    public static void main (String[] args) {
        Server server;
        server = new Server ("localhost", 8080, "/stomp", MainServerEndpoint.class);
        try {
            server.start();
            System.out.println("--- Server is running ! ---");
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            bufferRead.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
