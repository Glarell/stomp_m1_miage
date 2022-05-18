package network;

import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

@ClientEndpoint
public class MainClientEndpoint {

    public final String new_line = System.lineSeparator();
    public boolean first_received = false;

    @OnOpen
    public void onOpen(Session session) {
        try{
            StringBuilder stringBuilder = new StringBuilder();
            // TYPE
            stringBuilder.append("CONNECT").append(new_line);
            //HEADERS
            stringBuilder.append("accept-version:1.0").append(new_line);
            stringBuilder.append("host:localhost").append(new_line);
            // BLANK LINE
            stringBuilder.append(new_line);
            // END
            stringBuilder.append("^@");
            session.getBasicRemote().sendText(stringBuilder.toString());
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
                if (trame.getType().equals("ERROR")){
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
            URI uri = new URI("ws://localhost:8080/stomp/main");
            Session session = client.connectToServer(MainClientEndpoint.class, uri);
            while(true){

            }
        } catch (DeploymentException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
