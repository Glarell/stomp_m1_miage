package network;

import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class MainClientEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("--- Connected " + session.getId());
        try{
            session.getBasicRemote().sendText("Je suis connecté");
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("--- Received : " + message);
    }

    public static void main(String[] args){
        ClientManager client = ClientManager.createClient();
        try{
            URI uri = new URI("ws://localhost:8080/stomp/main");
            Session session = client.connectToServer(MainClientEndpoint.class, uri);
            session.getBasicRemote().sendText("Je t'envoie un message");
        } catch (DeploymentException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
