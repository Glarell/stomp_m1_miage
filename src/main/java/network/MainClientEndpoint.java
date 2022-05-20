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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void onMessage(String message, Session session) {
        Trame trame = TrameConstructor.parseTrameServeur(message);
        if (trame != null) {
            if (!first_received){
                first_received = true;
                if (trame.isERROR()){
                    try{
                        session.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else{
                    System.out.println(trame.toString());
                }
            }else{
                Pattern pattern = Pattern.compile("\\(x:(?<x>\\d+),y:(?<y>\\d+)\\) - \\((?<start>false|true)->(?<end>true|false)\\)");
                Matcher matcher = pattern.matcher("(x:10,y:9) - (true->false)");
                System.out.println(matcher.groupCount());
                if (matcher.find()) {
                    System.out.println("Found value: " + matcher.group("x") );
                    System.out.println("Found value: " + matcher.group("y") );
                    System.out.println("Found value: " + matcher.group("end") );
                    int x = Integer.parseInt(matcher.group("x"));
                    int y = Integer.parseInt(matcher.group("y"));
                    boolean value = Boolean.parseBoolean(matcher.group("end"));
                    Controller.changeGrid(x,y, new Button(false));
                } else {
                    System.out.println("NO MATCH");
                }
            }
        }

    }

    public static void main(String[] args){
        ClientManager client = ClientManager.createClient();
        try{
            URI uri = new URI("ws://localhost:8080/stomp/main/enzo");
            session = client.connectToServer(MainClientEndpoint.class, uri);
            session.getBasicRemote().sendText(TrameConstructor.createTrame("SUBSCRIBE", new HashMap<>(Map.of("destination", "test", "content-type","text/plain", "id","0")),"").toSend());
            App.main(args);
        } catch (DeploymentException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
