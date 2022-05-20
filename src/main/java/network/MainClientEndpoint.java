package network;

import fx.App;
import fx.Button;
import fx.Controller;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@ClientEndpoint
public class MainClientEndpoint {

    public final String new_line = System.lineSeparator();
    public boolean first_received = false;

    public static Session session;

    @OnOpen
    public void onOpen(Session session) {
        try{
            // TYPE
            String stringBuilder = "CONNECT" + new_line +
                    //HEADERS
                    "version:1.0" + new_line +
                    "host:localhost" + new_line +
                    "content-type:text/plain" + new_line +
                    // BLANK LINE
                    new_line +
                    // END
                    "^@";
            session.getBasicRemote().sendText(stringBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        Trame trame = TrameConstructor.parseTrameServeur(message);
        if (trame != null) {
            if (!first_received){
                first_received = true;
                if (trame.isERROR()){
                    session.close();
                }else{
                    System.out.println(trame.toString());
                }
            }else{
                System.out.println(trame.toString());
            }
        }

    }

    public static void main(String[] args){
        ClientManager client = ClientManager.createClient();
        try{
            URI uri = new URI("ws://localhost:8080/stomp/main/enzo");
            session = client.connectToServer(MainClientEndpoint.class, uri);
            App.main(args);
        } catch (DeploymentException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
