package network;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


@ServerEndpoint(value = "/main")
public class MainServerEndpoint {

    private Session session;
    private static final Set<MainServerEndpoint> serverEndpoints = new CopyOnWriteArraySet<>();
    private static final HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        this.session = session;
        serverEndpoints.add(this);
        users.put(session.getId(), id);
        broadcast("Connected !");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        broadcast(message);
        System.out.println(message);
    }

    @OnClose
    public void onClose(Session session) {
        serverEndpoints.remove(this);
        broadcast("Disconnected !");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    }

    private static void broadcast(String message) {
        serverEndpoints.forEach(endpoint -> {
            synchronized (endpoint){
                try{
                    endpoint.session.getBasicRemote().sendText(message);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
