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
    private static final HashMap<String, Boolean> users_connected = new HashMap<>();

    private final String new_line = System.lineSeparator();

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        this.session = session;
        serverEndpoints.add(this);
        users.put(session.getId(), id);
        users_connected.put(id, false);
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("id") String id) throws IOException {
        if(!users_connected.get(id)){
            Trame trame = TrameConstructor.parseTrameClient(message);
            System.out.println(trame.toString());
            if (trame.getType().equals("CONNECT")){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("CONNECTED").append(new_line);
                stringBuilder.append("version:1.0").append(new_line);
                stringBuilder.append("content-type:text/plain").append(new_line);
                stringBuilder.append(new_line).append("^@");
                session.getBasicRemote().sendText(stringBuilder.toString());
            }else{
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ERROR").append(new_line);
                stringBuilder.append("version:1.0").append(new_line);
                stringBuilder.append("content-type:text/plain").append(new_line);
                stringBuilder.append(new_line).append("No Connect Frame in first place").append(new_line);
                stringBuilder.append("^@");
                session.getBasicRemote().sendText(stringBuilder.toString());
            }
        }else{
            Trame trame = TrameConstructor.parseTrameClient(message);
            if (trame != null){
                System.out.println(trame.toString());
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        serverEndpoints.remove(this);
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
